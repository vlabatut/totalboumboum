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

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostInfo;
import org.totalboumboum.network.newstream.event.ConfigurationNetworkMessage;
import org.totalboumboum.network.newstream.event.NetworkInfo;
import org.totalboumboum.network.newstream.event.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientGeneralConnection
{
	public ClientGeneralConnection(List<HostInfo> hosts)
	{	// init direct connections
		//List<GameInfo> gameInfos = Configuration.getConnectionsConfiguration().getDirectConnections();
		for(HostInfo hostInfo: hosts)
			createConnection(hostInfo);
		
		// TODO for central connection, a special connection will be defined for the configuration stage
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ClientIndividualConnection> individualConnections = new ArrayList<ClientIndividualConnection>();
	
	public void requestGameInfos()
	{	NetworkMessage message = new ConfigurationNetworkMessage(NetworkInfo.REQUEST_GAME_INFO);
		for(ClientIndividualConnection connection: individualConnections)
			connection.writeMessage(message);
	}
	
	public void createConnection(HostInfo hostInfo)
	{	ClientIndividualConnection individualConnection = new ClientIndividualConnection(this,hostInfo);
		individualConnections.add(individualConnection);
	}
	
	public void removeConnection(ClientIndividualConnection connection)
	{	connection.finish();
		individualConnections.remove(connection);
	}
	
	public void gameInfoChanged(ClientIndividualConnection connection)
	{	int index = individualConnections.indexOf(connection);
		fireConnectionGameInfoChanged(connection,index);
	}

	public void profilesChanged(ClientIndividualConnection connection)
	{	int index = individualConnections.indexOf(connection);
		fireConnectionProfilesChanged(connection,index);
	}

	public List<GameInfo> getGameList()
	{	List<GameInfo> result = new ArrayList<GameInfo>();
		for(ClientIndividualConnection connection: individualConnections)
			result.add(connection.getGameInfo());
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ClientGeneralConnectionListener> listeners = new ArrayList<ClientGeneralConnectionListener>();
	
	public void addListener(ClientGeneralConnectionListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeListener(ClientGeneralConnectionListener listener)
	{	listeners.remove(listener);
	}
	
//	private void fireConnectionAdded(ClientIndividualConnection connection, int index)
//	{	for(ClientGeneralConnectionListener listener: listeners)
//			listener.connectionAdded(connection,index);
//	}

//	private void fireConnectionRemoved(ClientIndividualConnection connection, int index)
//	{	for(ClientGeneralConnectionListener listener: listeners)
//			listener.connectionRemoved(connection,index);
//	}

	private void fireConnectionGameInfoChanged(ClientIndividualConnection connection, int index)
	{	for(ClientGeneralConnectionListener listener: listeners)
			listener.connectionGameInfoChanged(connection,index);
	}

	private void fireConnectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	for(ClientGeneralConnectionListener listener: listeners)
			listener.connectionProfilesChanged(connection,index);
	}
}
