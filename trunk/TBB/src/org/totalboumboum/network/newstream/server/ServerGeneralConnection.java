package org.totalboumboum.network.newstream.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.network.newstream.event.ConfigurationNetworkMessage;
import org.totalboumboum.network.newstream.event.NetworkMessage;

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


/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerGeneralConnection implements Runnable
{
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
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ServerIndividualConnection> inividualConfigConnections = new ArrayList<ServerIndividualConnection>();
	private final List<ServerIndividualConnection> inividualTournamentConnections = new ArrayList<ServerIndividualConnection>();
	
	public void propagateMessage(NetworkMessage message)
	{	// select the appropriate connections
		List<ServerIndividualConnection> individualConnections;
		if(message instanceof ConfigurationNetworkMessage)
			individualConnections = inividualConfigConnections;
		else 
			individualConnections = inividualTournamentConnections;
		
		// send the message
		for(ServerIndividualConnection connection: individualConnections)
			connection.writeMessage(message);
	}
}
