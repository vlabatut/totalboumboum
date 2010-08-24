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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.network.newstream.event.ConfigurationNetworkMessage;
import org.totalboumboum.network.newstream.event.NetworkMessage;

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
}
