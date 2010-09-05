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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostInfo;
import org.totalboumboum.network.host.HostState;
import org.totalboumboum.network.newstream.event.ConfigurationNetworkMessage;
import org.totalboumboum.network.newstream.event.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerGeneralConnection implements Runnable
{	
	public ServerGeneralConnection(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles)
	{	// set data
		this.playerProfiles.addAll(playerProfiles);
		initGameInfo(allowedPlayers,tournamentName,tournamentType,playerScores,playerProfiles);
		
		// launch thread
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME INFO	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameInfo gameInfo = null;
	
	private void initGameInfo(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles)
	{	gameInfo = new GameInfo();
	
		// players
		gameInfo.setAllowedPlayers(allowedPlayers);
		gameInfo.setTournamentName(tournamentName);
		int playerCount = playerProfiles.size();
		gameInfo.setPlayerCount(playerCount);
		
		// average score
		double averageScore = 0;
		for(Double d: playerScores)
			averageScore = averageScore + d;
		averageScore = averageScore / playerCount;
		gameInfo.setAverageScore(averageScore);

		// tournament type
		gameInfo.setTournamentType(tournamentType);

		// host info
		HostInfo hostInfo = Configuration.getConnectionsConfiguration().getLocalHostInfo();
//		result.setLastIp(lastIp);	// TODO info locale au client
//		result.setPreferred(preferred); // TODO info locale au client
//		result.setUses(uses); 		// TODO info locale au client
		hostInfo.setState(hostState);
		hostInfo.setCentral(central);
		hostInfo.setDirect(direct);
		gameInfo.setHostInfo(hostInfo);
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST STATE	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostState hostState = HostState.UNKOWN;
	
	public void setHostState(HostState hostState)
	{	this.hostState = hostState;
	}
	
	/////////////////////////////////////////////////////////////////
	// CENTRAL		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean central;
	
	public void setCentral(boolean central)
	{	this.central = central;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECT		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean direct;
	
	public void setDirect(boolean direct)
	{	this.direct = direct;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> playerProfiles = new ArrayList<Profile>();
	
	public void addProfile(int index, Profile profile)
	{	//TODO
	}

	public void setProfile(int index, Profile profile)
	{	//TODO
	}

	public void removeProfile(int index)
	{
		// TODO
	}
	
	public void removeProfile(Profile profile)
	{
		// TODO
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
	
	public synchronized void propagateMessage(NetworkMessage message)
	{	for(ServerIndividualConnection connection: individualConnections)
		{	if(message instanceof ConfigurationNetworkMessage && !connection.getMode())
				connection.writeMessage(message);
			else if(!(message instanceof ConfigurationNetworkMessage) && connection.getMode())
				connection.writeMessage(message);
		}
	}
	
	public synchronized void createConnection(Socket socket) throws IOException
	{	ServerIndividualConnection individualConnection = new ServerIndividualConnection(this,socket);
		individualConnections.add(individualConnection);
	}
	
	public synchronized void removeConnection(ServerIndividualConnection connection)
	{	connection.finish();
		individualConnections.remove(connection);
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
