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
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.control.player.RemotePlayerControl;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.message.MessageName;
import org.totalboumboum.stream.network.message.NetworkMessage;
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
	// CONTROL SETTINGS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ControlSettings> controlSettings; //TODO to be initialized
	
	public ControlSettings getControlSettings(int index)
	{	ControlSettings result = controlSettings.get(index);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// REMOTE PLAYER CONTROL	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RemotePlayerControl remotePlayerControl = null;
	
	public void setRemotePlayerControl(RemotePlayerControl remotePlayerControl)
	{	this.remotePlayerControl = remotePlayerControl;
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
		message = new NetworkMessage(MessageName.UPDATE_GAME_INFO,copy);
		
		gameInfoLock.unlock();
		
		propagateMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> playerProfiles = new ArrayList<Profile>();
	private final HashMap<String,ServerIndividualConnection> playerConnections = new HashMap<String, ServerIndividualConnection>(); 
	private Lock profileLock = new ReentrantLock();
	
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
				playerConnections.put(profile.getId(),null);
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
		profileLock.lock();
		
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
					NetworkMessage message = new NetworkMessage(MessageName.UPDATE_GAME_INFO,gameInfo);
					propagateMessage(message);
				}		
				gameInfoLock.lock();
			
				// update players list
				playerProfiles.add(profile);
				playerConnections.put(profile.getId(),connection);
				
				// send the appropriate message
				NetworkMessage message = new NetworkMessage(MessageName.UPDATE_PLAYERS_LIST,playerProfiles);
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
					NetworkMessage message = new NetworkMessage(MessageName.UPDATE_GAME_INFO,gameInfo);
					propagateMessage(message);
				}		
				gameInfoLock.lock();
			
				// update profiles list
				playerConnections.remove(oldProfile.getId());
				playerConnections.put(profile.getId(),connection);
				playerProfiles.set(index,profile);
				
				// send the appropriate message 
				NetworkMessage message = new NetworkMessage(MessageName.UPDATE_PLAYERS_LIST,playerProfiles);
				propagateMessage(message);
			}
			profileLock.unlock();
		}
	}

	/**
	 * just a change of color or sprite in a profile
	 */
	public void profileModified(Profile profile)
	{	profileLock.lock();
		{	// send the appropriate message
			NetworkMessage message = new NetworkMessage(MessageName.UPDATE_PLAYERS_LIST,playerProfiles);
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
				NetworkMessage message = new NetworkMessage(MessageName.UPDATE_GAME_INFO,gameInfo);
				propagateMessage(message);
			}		
			gameInfoLock.lock();
		
			// update players list
			playerProfiles.remove(profile);
			playerConnections.remove(profile.getId());
			
			// send the appropriate message
			NetworkMessage message = new NetworkMessage(MessageName.UPDATE_PLAYERS_LIST,playerProfiles);
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
		{	for(Profile profile: playerProfiles)
			{	ServerIndividualConnection ct = playerConnections.get(profile.getId());
				if(ct==connection)
				{	profileRemoved(profile);
					fireProfileRemoved(profile);
				}
			}
		}
		profileLock.unlock();
	}
	
	public void playersAddRequested(Profile profile, ServerIndividualConnection connection)
	{	Set<Integer> allowedPlayers;
		int index;
		
		gameInfoLock.lock();
		{	allowedPlayers = new TreeSet<Integer>(gameInfo.getAllowedPlayers());
		}
		gameInfoLock.unlock();
		
		profileLock.lock();
		{	int playerCount = playerProfiles.size();
			if(allowedPlayers.contains(playerCount+1))
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
				profileAdded(profile,connection);
			}
			index = playerProfiles.indexOf(profile);
		}
		profileLock.unlock();

		fireProfileAdded(index,profile);
	}
	
	public void playersChangeRequested(Profile profile, ServerIndividualConnection connection)
	{	profileLock.lock();
		{	String id = profile.getId();
			Profile prof = null;
			boolean found = false;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
				{	prof = p;
					found = true;
				}
			}
			while(it.hasNext() && !found);
			
			if(profile!=null && connection==playerConnections.get(id))
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
		{	boolean found = false;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
				{	profile = p;
					found = true;
				}
			}
			while(it.hasNext() && !found);
			
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
			boolean found = false;
			Iterator<Profile> it = playerProfiles.iterator();
			do
			{	Profile p = it.next();
				if(p.getId().equals(id))
				{	oldProfile = p;
					found = true;
				}
			}
			while(it.hasNext() && !found);
			
			index = playerProfiles.indexOf(oldProfile);
			if(oldProfile!=null && connection==playerConnections.get(id))
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
				profileSet(index,profile,connection);
			}
		}
		profileLock.unlock();
		
		fireProfileSet(index,profile);
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
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ServerIndividualConnection> individualConnections = new ArrayList<ServerIndividualConnection>();
	private Lock connectionsLock = new ReentrantLock();
	
	public void propagateMessage(NetworkMessage message)
	{	MessageName info = message.getInfo();
		connectionsLock.lock();
	
		for(ServerIndividualConnection connection: individualConnections)
		{	boolean send = false;
			
			if(info==MessageName.UPDATE_GAME_INFO)
				send = connection.getState()==ClientState.SELECTING_GAME
						|| connection.getState()==ClientState.SELECTING_PLAYERS;
			else if(info==MessageName.UPDATE_PLAYERS_LIST)
				send = connection.getState()==ClientState.SELECTING_PLAYERS;
			
			if(send)	
				connection.writeMessage(message);
		}
		
		connectionsLock.unlock();
	}
	
	public void createConnection(Socket socket) throws IOException
	{	connectionsLock.lock();
	
		ServerIndividualConnection individualConnection = new ServerIndividualConnection(this,socket);
		individualConnections.add(individualConnection);
		
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
	
	public void addListener(ServerGeneralConnectionListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeListener(ServerGeneralConnectionListener listener)
	{	listeners.remove(listener);
	}
	
	private void fireProfileRemoved(Profile profile)
	{	for(ServerGeneralConnectionListener listener: listeners)
			listener.profileRemoved(profile);
	}

	private void fireProfileAdded(int index, Profile profile)
	{	for(ServerGeneralConnectionListener listener: listeners)
			listener.profileAdded(index,profile);
	}

	private void fireProfileModified(Profile profile)
	{	for(ServerGeneralConnectionListener listener: listeners)
			listener.profileModified(profile);
	}

	private void fireProfileSet(int index, Profile profile)
	{	for(ServerGeneralConnectionListener listener: listeners)
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
