package org.totalboumboum.network.newstream.server;

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
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.control.player.RemotePlayerControl;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostInfo;
import org.totalboumboum.network.host.HostState;
import org.totalboumboum.network.newstream.event.NetworkInfo;
import org.totalboumboum.network.newstream.event.NetworkMessage;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerGeneralConnection implements Runnable
{	
	public ServerGeneralConnection(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles, boolean direct, boolean central)
	{	// set data
		this.playerProfiles.addAll(playerProfiles);
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
		message = new NetworkMessage(NetworkInfo.UPDATE_GAME_INFO,copy);
		
		gameInfoLock.unlock();
		
		propagateMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> playerProfiles = new ArrayList<Profile>();
	private Lock profileLock = new ReentrantLock();
	
	public void profileAdded(Profile profile)
	{	gameInfoLock.lock();
		{	// update player count
			int playerCount = gameInfo.getPlayerCount();
			playerCount ++;
			gameInfo.setPlayerCount(playerCount);
			
			// update average score
			double averageScore = gameInfo.getAverageScore();
			RankingService rankingService = GameStatistics.getRankingService();
			PlayerRating playerRating = rankingService.getPlayerRating(profile.getId());
			double score = playerRating.getRating();
			averageScore = (averageScore*playerProfiles.size()+score) / (playerProfiles.size()+1);
			gameInfo.setAverageScore(averageScore);
	
			// send the appropriate message 
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_GAME_INFO,gameInfo);
			propagateMessage(message);
		}		
		gameInfoLock.lock();
		
		profileLock.lock();
		{	// update players list
			playerProfiles.add(profile);
			
			// send the appropriate message
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_PLAYERS_LIST,playerProfiles);
			propagateMessage(message);
		}
		profileLock.unlock();
	}
	
	public void profilesAdded(List<Profile> profiles)
	{	profileLock.lock();
		
		for(Profile profile: profiles)
		{	if(!playerProfiles.contains(profile))
				profileAdded(profile);
		}
		
		profileLock.unlock();
	}

	/**
	 * some profile was switched for another one
	 */
	public void profileSet(int index, Profile profile)
	{	gameInfoLock.lock();
		{	// update average score
			double averageScore = gameInfo.getAverageScore();
			Profile oldProfile = playerProfiles.get(index);
			RankingService rankingService = GameStatistics.getRankingService();
			PlayerRating oldRating = rankingService.getPlayerRating(oldProfile.getId()); 
			double oldScore = oldRating.getRating();
			averageScore = averageScore - oldScore/playerProfiles.size();
			PlayerRating newRating = rankingService.getPlayerRating(profile.getId());
			double newScore = newRating.getRating();
			averageScore = averageScore + newScore/playerProfiles.size();
			gameInfo.setAverageScore(averageScore);

			// send the appropriate message 
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_GAME_INFO,gameInfo);
			propagateMessage(message);
		}		
		gameInfoLock.lock();
		
		profileLock.lock();
		{	// update profiles list
			playerProfiles.set(index,profile);
			
			// send the appropriate message 
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_PLAYERS_LIST,playerProfiles);
			propagateMessage(message);
		}		
		profileLock.unlock();
	}

	/**
	 * just a change of color or sprite in a profile
	 */
	public void profileModified(Profile profile)
	{	profileLock.lock();
		{	// send the appropriate message
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_PLAYERS_LIST,playerProfiles);
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
	{	gameInfoLock.lock();
		{	// update player count
			int playerCount = gameInfo.getPlayerCount();
			playerCount --;
			gameInfo.setPlayerCount(playerCount);
			
			// update average score
			double averageScore = gameInfo.getAverageScore();
			RankingService rankingService = GameStatistics.getRankingService();
			PlayerRating playerRating = rankingService.getPlayerRating(profile.getId());
			double score = playerRating.getRating();
			averageScore = (averageScore*playerProfiles.size() - score) / (playerProfiles.size()-1);
			gameInfo.setAverageScore(averageScore);
	
			// send the appropriate message 
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_GAME_INFO,gameInfo);
			propagateMessage(message);
		}		
		gameInfoLock.lock();
		
		profileLock.lock();
		{	// update players list
			playerProfiles.remove(profile);
			
			// send the appropriate message
			NetworkMessage message = new NetworkMessage(NetworkInfo.UPDATE_PLAYERS_LIST,playerProfiles);
			propagateMessage(message);
		}
		profileLock.unlock();
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
	{	NetworkInfo info = message.getInfo();
		connectionsLock.lock();
	
		for(ServerIndividualConnection connection: individualConnections)
		{	boolean send = false;
			
			if(info==NetworkInfo.UPDATE_GAME_INFO)
				send = connection.getState()==ClientState.SELECTING_GAME
						|| connection.getState()==ClientState.SELECTING_PLAYERS;
			else if(info==NetworkInfo.UPDATE_PLAYERS_LIST)
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
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
		
	public synchronized boolean isFinished()
	{	return finished;
	}
	
	public synchronized void finish()
	{	finished = true;
	}
}
