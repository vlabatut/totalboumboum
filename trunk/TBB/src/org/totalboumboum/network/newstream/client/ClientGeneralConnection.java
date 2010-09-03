package org.totalboumboum.network.newstream.client;

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
public class ClientGeneralConnection implements Runnable
{
	public ClientGeneralConnection(Set<Integer> allowedPlayers, String tournamentName, TournamentType tournamentType, List<Double> playerScores, List<Profile> playerProfiles)
	{	this.playerProfiles.addAll(playerProfiles);
		initGameInfo(allowedPlayers,tournamentName,tournamentType,playerScores,playerProfiles);
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
		{	
			serverSocket = new ServerSocket(port);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		
		// wait for new connections
		while(true)
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
	private final List<ClientIndividualConnection> individualConnections = new ArrayList<ClientIndividualConnection>();
	
	public synchronized void propagateMessage(NetworkMessage message)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(message instanceof ConfigurationNetworkMessage && !connection.getMode())
				connection.writeMessage(message);
			else if(!(message instanceof ConfigurationNetworkMessage) && connection.getMode())
				connection.writeMessage(message);
		}
	}
	
	public synchronized void createConnection(Socket socket) throws IOException
	{	ClientIndividualConnection individualConnection = new ClientIndividualConnection(this,socket);
		individualConnections.add(individualConnection);
	}
	
	public synchronized void removeConnection(ClientIndividualConnection connection)
	{	connection.finish();
		individualConnections.remove(connection);
	}
}
