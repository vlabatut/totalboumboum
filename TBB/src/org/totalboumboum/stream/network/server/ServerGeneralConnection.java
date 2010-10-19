package org.totalboumboum.stream.network.server;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.control.player.RemotePlayerControl;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.message.MessageName;
import org.totalboumboum.stream.network.message.NetworkMessage;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerGeneralConnection implements Runnable
{	
	public ServerGeneralConnection(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles, boolean direct, boolean central)
	{	// set data
		initPlayerProfiles(playerProfiles);
		initGameInfo(allowedPlayers,tournamentName,tournamentType,playerScores,playerProfiles,direct,central);
		
		// launch thread
		Thread thread = new Thread(this);
		thread.start();
	}
		
	/////////////////////////////////////////////////////////////////
	// REMOTE PLAYER CONTROL	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ControlSettings> controlSettings;
	private RemotePlayerControl remotePlayerControl = null;
	private Lock controlsLock = new ReentrantLock();
	
	public void setRemotePlayerControl(RemotePlayerControl remotePlayerControl)
	{	this.remotePlayerControl = remotePlayerControl;
	}
	
	protected void controlSettingsReceived(List<ControlSettings> controlSettings, ServerIndividualConnection connection)
	{	// get the ids of the profiles corresponding to the specified connection
		List<String> ids = new ArrayList<String>();
		connectionsLock.lock();
		{	for(String id: playerConnections.keySet())
			{	ServerIndividualConnection cx = playerConnections.get(id);
				if(cx==connection)
					ids.add(id);
			}
		}
		connectionsLock.unlock();
		
		// update the controlSettings for the corresponding profiles
		controlsLock.lock();
		{	for(int j=0;j<ids.size();j++)
			{	String id = ids.get(j);
				ControlSettings cs = controlSettings.get(j);
				Profile p = null;
				int index = 0;
				int i = 0;
				while(p==null && i<playerProfiles.size())
				{	Profile temp = playerProfiles.get(i);
					if(temp.getId().equals(id))
						p = temp;
					else if(temp.isRemote())
						index++;
				}
				this.controlSettings.set(index,cs);
			}
		}
		controlsLock.unlock();
	}

	/////////////////////////////////////////////////////////////////
	// GAME INFO	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameInfo gameInfo = null;
	private Lock gameInfoLock = new ReentrantLock();
	
	private void initGameInfo(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles, boolean direct, boolean central)
	{	gameInfoLock.lock();
		
		gameInfo = new GameInfo();
	
		// players
		gameInfo.setAllowedPlayers(allowedPlayers);
		gameInfo.setTournamentName(tournamentName);
		int playerCount = playerProfiles.size();
		gameInfo.setPlayerCount(playerCount);
		
		// average score
		double averageScore = 0;
		for(Double d: playerScores)
			averageScore = averageScore + d;
		averageScore = averageScore / playerScores.size();
		gameInfo.setAverageScore(averageScore);		
		// tournament type
		gameInfo.setTournamentType(tournamentType);

		// host info
		HostInfo hostInfo = Configuration.getConnectionsConfiguration().getLocalHostInfo();
//		result.setLastIp(lastIp);	// TODO info locale au client
//		result.setPreferred(preferred); // TODO info locale au client
//		result.setUses(uses); 		// TODO info locale au client
		hostInfo.setState(HostState.OPEN);
		hostInfo.setCentral(central);
		hostInfo.setDirect(direct);
		gameInfo.setHostInfo(hostInfo);
		
		gameInfoLock.unlock();
	}
	
	public GameInfo getGameInfo()
	{	GameInfo result = null;
		
		gameInfoLock.lock();
		result = gameInfo.copy();
		gameInfoLock.unlock();
		
		return result;
	}
	
	public void updateHostState(HostState hostState)
	{	NetworkMessage message;
		
		gameInfoLock.lock();
		
		gameInfo.getHostInfo().setState(hostState);
		GameInfo copy = gameInfo.copy();
		message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,copy);
		
		gameInfoLock.unlock();
		
		propagateMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> playerProfiles = new ArrayList<Profile>();
	private final HashMap<String,ServerIndividualConnection> playerConnections = new HashMap<String, ServerIndividualConnection>(); 
	private Lock profileLock = new ReentrantLock();
	
	public void switchPlayersSelection()
	{	gameInfoLock.lock();
		{	HostInfo hostInfo = gameInfo.getHostInfo();
			HostState state = hostInfo.getState();
			if(state==HostState.OPEN)
				state = HostState.CLOSED;
			else
				state = HostState.OPEN;
			hostInfo.setState(state);
			
			NetworkMessage message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
			propagateMessage(message);
		}
		gameInfoLock.unlock();
	}
	
	public List<Profile> getPlayerProfiles()
	{	List<Profile> result;
	
		profileLock.lock();
		result = new ArrayList<Profile>(playerProfiles);
		profileLock.unlock();
		
		return result;
	}
	
	private void initPlayerProfiles(List<Profile> playerProfiles)
	{	profileLock.lock();
		{	this.playerProfiles.addAll(playerProfiles);
			for(Profile profile: playerProfiles)
			{	playerConnections.put(profile.getId(),null);
				profile.setReady(!profile.isRemote());
			}
		}
		profileLock.unlock();
	}
	
	public boolean containsProfile(Profile profile)
	{	boolean result = false;
		String id = profile.getId();
		
		profileLock.lock();
		{	Iterator<Profile> it = playerProfiles.iterator();
			while(it.hasNext() && !result)
			{	Profile p = it.next();
				String pid = p.getId();
				result = pid.equals(id);
			}
		}
		profileLock.unlock();
		
		return result;
	}
	
	public void profileAdded(Profile profile, ServerIndividualConnection connection)
	{	if(!containsProfile(profile))
		{	profileLock.lock();
			{	int size = playerProfiles.size();
				
				gameInfoLock.lock();
				{	// update player count
					int playerCount = gameInfo.getPlayerCount();
					playerCount ++;
					gameInfo.setPlayerCount(playerCount);
					
					// update average score
					double averageScore = gameInfo.getAverageScore();
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating playerRating = rankingService.getPlayerRating(profile.getId());
					double score = 0;
					if(playerRating!=null)
						score = playerRating.getRating();
					averageScore = (averageScore*size+score) / (size+1);
					gameInfo.setAverageScore(averageScore);
			
					// send the appropriate message 
					NetworkMessage message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
					propagateMessage(message);
				}		
				gameInfoLock.unlock();
			
				// update players list
				playerProfiles.add(profile);
				playerConnections.put(profile.getId(),connection);
				
				// send the appropriate message
				NetworkMessage message = new NetworkMessage(MessageName.UPDATING_PLAYERS_LIST,playerProfiles);
				propagateMessage(message);
			}
			profileLock.unlock();
		}
	}
	
	/** 
	 * NOTE: necessarily local 
	 */
	public void profilesAdded(List<Profile> profiles)
	{	profileLock.lock();
		{	for(Profile profile: profiles)
			{	if(!playerProfiles.contains(profile))
					profileAdded(profile,null);
			}
		}		
		profileLock.unlock();
	}

	/**
	 * some profile was switched for another one
	 */
	public void profileSet(int index, Profile profile, ServerIndividualConnection connection)
	{	if(!containsProfile(profile))
		{	profileLock.lock();
			{	Profile oldProfile = playerProfiles.get(index);
				int size = playerProfiles.size();
		
				gameInfoLock.lock();
				{	// update average score
					double averageScore = gameInfo.getAverageScore();
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating oldRating = rankingService.getPlayerRating(oldProfile.getId()); 
					double oldScore = 0;
					if(oldRating!=null)
						oldScore = oldRating.getRating();
					averageScore = averageScore - oldScore/size;
					PlayerRating newRating = rankingService.getPlayerRating(profile.getId());
					double newScore = 0;
					if(newRating!=null)
						newScore = newRating.getRating();
					averageScore = averageScore + newScore/size;
					gameInfo.setAverageScore(averageScore);
		
					// send the appropriate message 
					NetworkMessage message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
					propagateMessage(message);
				}		
				gameInfoLock.unlock();
			
				// update profiles list
				playerConnections.remove(oldProfile.getId());
				playerConnections.put(profile.getId(),connection);
				playerProfiles.set(index,profile);
				
				// send the appropriate message 
				NetworkMessage message = new NetworkMessage(MessageName.UPDATING_PLAYERS_LIST,playerProfiles);
				propagateMessage(message);
			}
			profileLock.unlock();
		}
	}

	/**
	 * just a change of color or sprite in a profile
	 * or of readyness
	 */
	public void profileModified(Profile profile)
	{	profileLock.lock();
		{	// send the appropriate message
			NetworkMessage message = new NetworkMessage(MessageName.UPDATING_PLAYERS_LIST,playerProfiles);
			propagateMessage(message);
		}		
		profileLock.unlock();
	}

	public void profileRemoved(int index)
	{	profileLock.lock();
		{	Profile profile = playerProfiles.get(index);
			profileRemoved(profile);
		}
		profileLock.unlock();
	}
	
	public void profileRemoved(Profile profile)
	{	int size;
		
		profileLock.lock();
		{	size = playerProfiles.size();
	
			gameInfoLock.lock();
			{	// update player count
				int playerCount = gameInfo.getPlayerCount();
				playerCount --;
				gameInfo.setPlayerCount(playerCount);
				
				// update average score
				double averageScore = 0;
				if(playerCount>0)
				{	averageScore = gameInfo.getAverageScore();
					RankingService rankingService = GameStatistics.getRankingService();
					PlayerRating playerRating = rankingService.getPlayerRating(profile.getId());
					double score = playerRating.getRating();
					averageScore = (averageScore*size - score) / (size-1);
				}
				gameInfo.setAverageScore(averageScore);
		
				// send the appropriate message 
				NetworkMessage message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
				propagateMessage(message);
			}		
			gameInfoLock.unlock();
		
			// update players list
			playerProfiles.remove(profile);
			playerConnections.remove(profile.getId());
			
			// send the appropriate message
			NetworkMessage message = new NetworkMessage(MessageName.UPDATING_PLAYERS_LIST,playerProfiles);
			propagateMessage(message);
		}
		profileLock.unlock();
	}

//	boolean found = false;
//	Iterator<Profile> it = playerProfiles.iterator();
//	do
//	{	Profile p = it.next();
//		if(p.getId().equals(id))
//		{	playerProfiles.remove(p);
//			found = true;
//		}
//	}
//	while(it.hasNext() && !found);
	
	public void playerSelectionExited(ServerIndividualConnection connection)
	{	profileLock.lock();
		{	List<Profile> temp = new ArrayList<Profile>(playerProfiles);
			for(Profile profile: temp)
			{	ServerIndividualConnection ct = playerConnections.get(profile.getId());
				if(ct==connection)
				{	profileRemoved(profile);
					fireProfileRemoved(profile);
				}
			}
		}
		profileLock.unlock();
	}
	
	public void playerSelectionConfirmed(ServerIndividualConnection connection, boolean confirmation)
	{	profileLock.lock();
		{	List<Profile> temp = new ArrayList<Profile>(playerProfiles);
			for(Profile profile: temp)
			{	ServerIndividualConnection ct = playerConnections.get(profile.getId());
				if(ct==connection)
				{	profile.setReady(confirmation);
					fireProfileModified(profile);
					profileModified(profile);
				}
			}
		}
		profileLock.unlock();
	}
	
	public void playersAddRequested(Profile profile, ServerIndividualConnection connection)
	{	
//		Set<Integer> allowedPlayers;
		int index;
		
//		gameInfoLock.lock();
//		{	allowedPlayers = new TreeSet<Integer>(gameInfo.getAllowedPlayers());
//		}
//		gameInfoLock.unlock();
		
		profileLock.lock();
//		{	int playerCount = playerProfiles.size();
//			if(allowedPlayers.contains(playerCount+1))
			{	// images must be loaded because they did not pass the stream	
				try
				{	ProfileLoader.reloadPortraits(profile);
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				// if profile new, must be inserted in the local DB	
				try
				{	Configuration.getProfilesConfiguration().insertProfile(profile);
				}
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				profileAdded(profile,connection);
			}
			index = playerProfiles.indexOf(profile);
//		}
		profileLock.unlock();

		fireProfileAdded(index,profile);
	}
	
	public void playersChangeRequestedColor(Profile profile, ServerIndividualConnection connection)
	{	profileLock.lock();
		{	String id = profile.getId();
			Profile prof = null;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
					prof = p;
			}
			while(it.hasNext() && prof==null);
			
			if(prof!=null && connection==playerConnections.get(id))
			{	PredefinedColor color = profile.getSpriteColor();
				color = Configuration.getProfilesConfiguration().getNextFreeColor(playerProfiles,prof,color);
				prof.getSelectedSprite().setColor(color);
				try
				{	// images must be loaded anyway because they did not pass the stream	
					ProfileLoader.reloadPortraits(prof);
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				//prof.synch(profile);
				profileModified(profile);
			}
		}
		profileLock.unlock();

		fireProfileModified(profile);
	}
	
	public void playersChangeRequestedHero(Profile profile, ServerIndividualConnection connection)
	{	profileLock.lock();
		{	String id = profile.getId();
			Profile prof = null;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
					prof = p;
			}
			while(it.hasNext() && prof==null);
			
			if(prof!=null && connection==playerConnections.get(id))
			{	try
				{	// images must be loaded because they did not pass the stream	
					ProfileLoader.reloadPortraits(profile);
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				prof.synch(profile);
				profileModified(profile);
			}
		}
		profileLock.unlock();

		fireProfileModified(profile);
	}
	
	public void playersRemoveRequested(String id, ServerIndividualConnection connection)
	{	Profile profile = null;
	
		profileLock.lock();
		{	Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
					profile = p;
			}
			while(it.hasNext() && profile==null);
			
			if(profile!=null && connection==playerConnections.get(id))
				profileRemoved(profile);
		}
		profileLock.unlock();
		
		fireProfileRemoved(profile);
	}
	
	public void playersSetRequested(String id, Profile profile, ServerIndividualConnection connection)
	{	int index;
		
		profileLock.lock();
		{	Profile oldProfile = null;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
					oldProfile = p;
			}
			while(it.hasNext() && oldProfile==null);
			
			index = playerProfiles.indexOf(oldProfile);
			if(oldProfile!=null && connection==playerConnections.get(id))
			{	// images must be loaded because they did not pass the stream	
				try
				{	ProfileLoader.reloadPortraits(profile);
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				// if profile new, must be inserted in the local DB	
				try
				{	Configuration.getProfilesConfiguration().insertProfile(profile);
				}
				catch (IllegalArgumentException e)
				{	e.printStackTrace();
				}
				catch (SecurityException e)
				{	e.printStackTrace();
				}
				catch (IOException e)
				{	e.printStackTrace();
				}
				catch (ParserConfigurationException e)
				{	e.printStackTrace();
				}
				catch (SAXException e)
				{	e.printStackTrace();
				}
				catch (IllegalAccessException e)
				{	e.printStackTrace();
				}
				catch (NoSuchFieldException e)
				{	e.printStackTrace();
				}
				catch (ClassNotFoundException e)
				{	e.printStackTrace();
				}
				profileSet(index,profile,connection);
			}
		}
		profileLock.unlock();
		
		fireProfileSet(index,profile);
	}
	
	public boolean areAllPlayersReady()
	{	boolean result = true;

		profileLock.lock();
		{	Iterator<Profile> it = playerProfiles.iterator();
			while(result && it.hasNext())
			{	Profile profile = it.next();
				result = profile.isReady();
			}
		}
		profileLock.unlock();
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// RUNNABLE		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	// init
		ConnectionsConfiguration connectionConfiguration = Configuration.getConnectionsConfiguration();
		int port = connectionConfiguration.getPort();
		
		// create socket
		ServerSocket serverSocket = null;
		try
		{	serverSocket = new ServerSocket(port);
System.out.println(serverSocket.getLocalSocketAddress());		
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		
		// wait for new connections
		while(!isFinished())
		{	try
			{	Socket socket = serverSocket.accept();
				createConnection(socket);
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// GAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Lock roundLock = new ReentrantLock();
	private Condition roundCondition = roundLock.newCondition();
	
	public void startTournament(AbstractTournament tournament)
	{	// announce the tournament is starting to concerned clients
		{	NetworkMessage message = new NetworkMessage(MessageName.STARTING_TOURNAMENT,tournament);
			propagateMessage(message);
		}
		
		// announce the change in GameInfo to all clients
		gameInfoLock.lock();
		{	gameInfo.getHostInfo().setState(HostState.PLAYING);
			NetworkMessage message = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
			propagateMessage(message);
		}		
		gameInfoLock.unlock();
	}
	
	public void finishRound(StatisticRound stats)
	{	// announce the tournament is starting to concerned clients
		{	NetworkMessage message = new NetworkMessage(MessageName.UPDATING_ROUND_STATS,stats);
			propagateMessage(message);
		}
	}
	
	public void sendReplay(ReplayEvent event)
	{	NetworkMessage message = new NetworkMessage(MessageName.INFO_REPLAY,event);
		propagateMessage(message);
	}
	
	public void updateZoomCoef(double zoomCoef)
	{	// init connection readyness
		connectionsLock.lock();
		{	individualConnectionsReady.clear();
			for(int i=0;i<individualConnections.size();i++)
				individualConnectionsReady.add(false);
		}
		connectionsLock.unlock();

		// init controlSettings list
		connectionsLock.lock();
		{	controlSettings = new ArrayList<ControlSettings>();
			for(int i=0;i<playerProfiles.size();i++)
				controlSettings.add(null);
		}
		connectionsLock.unlock();
		
		// propagate message to client
		NetworkMessage message = new NetworkMessage(MessageName.UPDATING_ZOOM_COEFF,zoomCoef);
		propagateMessage(message);
	}
	
	public void waitForClients()
	{	roundLock.lock();
		{	// check if all clients are ready
			boolean ready = true;
			Iterator<Boolean> it = individualConnectionsReady.iterator();
			while(it.hasNext() && ready)
			{	boolean value = it.next();
				ready = ready && value;
			}
			
			// otherwise, wait for all clients to be ready
			try
			{	roundCondition.await();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			
			// set the controlSettings
			connectionsLock.lock();
			{	remotePlayerControl.setControlSettings(controlSettings);
			}
		}
		roundLock.lock();
	}
	
	protected void loadingComplete(ServerIndividualConnection connection)
	{	roundLock.lock();
		{	boolean ready = true;
			connectionsLock.lock();
			{	// update this connection readyness
				int index = individualConnections.indexOf(connection);
				individualConnectionsReady.set(index,true);

				// check if all clients are ready
				Iterator<Boolean> it = individualConnectionsReady.iterator();
				while(it.hasNext() && ready)
				{	boolean value = it.next();
					ready = ready && value;
				}
			}
			connectionsLock.unlock();
			
			// if it is the case :
			if(ready)
			{	// start all remote clients
				NetworkMessage message = new NetworkMessage(MessageName.STARTING_ROUND);
				propagateMessage(message);
				
				// wake up the game thread
				roundCondition.notify();
			}
		}
		roundLock.lock();
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ServerIndividualConnection> individualConnections = new ArrayList<ServerIndividualConnection>();
	private final List<Boolean> individualConnectionsReady = new ArrayList<Boolean>();
	private Lock connectionsLock = new ReentrantLock();
	
	public void terminateConnection()
	{	connectionsLock.lock();
		for(ServerIndividualConnection connection: individualConnections)
			connection.finish();
		connectionsLock.unlock();
	}
	
	public void propagateMessage(NetworkMessage message)
	{	connectionsLock.lock();
		{	for(ServerIndividualConnection connection: individualConnections)
				connection.propagateMessage(message);
		}
		connectionsLock.unlock();
	}
	
	public void createConnection(Socket socket)
	{	connectionsLock.lock();
		{	try
			{	ServerIndividualConnection individualConnection = new ServerIndividualConnection(this,socket);
				individualConnections.add(individualConnection);
			}
			catch (IOException e)
			{	System.err.println("java.net.SocketException: Connection reset while creating streams");
				//e.printStackTrace();
			}
		}
		connectionsLock.unlock();
	}
	
	public void removeConnection(ServerIndividualConnection connection)
	{	connectionsLock.lock();
	
		connection.finish();
		individualConnections.remove(connection);
		
		connectionsLock.unlock();
	}
	
	public ServerIndividualConnection getConnection(int index)
	{	ServerIndividualConnection result;
		connectionsLock.lock();
		
		result = individualConnections.get(index);
	
		connectionsLock.unlock();
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void controlReceived(RemotePlayerControlEvent event)
	{	remotePlayerControl.addEvent(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ServerGeneralConnectionListener> listeners = new ArrayList<ServerGeneralConnectionListener>();
	private Lock listenersLock = new ReentrantLock();
	
	public void addListener(ServerGeneralConnectionListener listener)
	{	listenersLock.lock();
		{	if(!listeners.contains(listener))
				listeners.add(listener);
		}
		listenersLock.unlock();
	}
	
	public void removeListener(ServerGeneralConnectionListener listener)
	{	listenersLock.lock();
		{	listeners.remove(listener);
		}
		listenersLock.unlock();
	}
	
	private void fireProfileRemoved(Profile profile)
	{	List<ServerGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ServerGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ServerGeneralConnectionListener listener: list)
			listener.profileRemoved(profile);
	}

	private void fireProfileAdded(int index, Profile profile)
	{	List<ServerGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ServerGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ServerGeneralConnectionListener listener: list)
			listener.profileAdded(index,profile);
	}

	private void fireProfileModified(Profile profile)
	{	List<ServerGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ServerGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ServerGeneralConnectionListener listener: list)
			listener.profileModified(profile);
	}

	private void fireProfileSet(int index, Profile profile)
	{	List<ServerGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ServerGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ServerGeneralConnectionListener listener: list)
			listener.profileSet(index,profile);
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	private Lock finishedLock = new ReentrantLock();
	
	public boolean isFinished()
	{	boolean result;
		finishedLock.lock();
		result = finished;
		finishedLock.unlock();
		return result;
	}
	
	public void finish()
	{	finishedLock.lock();
		finished = true;
		finishedLock.unlock();
	}
}
