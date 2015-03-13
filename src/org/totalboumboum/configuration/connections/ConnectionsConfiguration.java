package org.totalboumboum.configuration.connections;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostsLoader;
import org.totalboumboum.stream.network.data.host.HostsSaver;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.event.EventName;
import org.totalboumboum.tools.event.UpdateEvent;
import org.totalboumboum.tools.event.UpdateListener;
import org.xml.sax.SAXException;

/**
 * This class handles all options regarding
 * the network communication aspects.
 * 
 * @author Vincent Labatut
 */
public class ConnectionsConfiguration
{
	/**
	 * Copy the current configuration,
	 * to be able to restore it later.
	 * 
	 * @return
	 * 		A copy of this object.
	 */
	public ConnectionsConfiguration copy()
	{	ConnectionsConfiguration result = new ConnectionsConfiguration();
	
		result.centralIp = centralIp;
		result.port = port;
		result.hostName = hostName;
		result.hostId = hostId;
	
		result.hosts.putAll(hosts);
		result.macAddresses.addAll(macAddresses);

		result.clientConnection = clientConnection;
		result.serverConnection = serverConnection;
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CENTRAL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Address of the central, i.e. TBB central server */
	private String centralIp;

	/**
	 * Changes the address of TBB central sever
	 * (synchronizes stats and other stuff).
	 * 
	 * @param centralIp
	 * 		Address of the central server.
	 */
	public void setCentralIp(String centralIp)
	{	this.centralIp = centralIp;		
	}
	
	/**
	 * Returns the address of TBB central sever
	 * (synchronizes stats and other stuff).
	 * 
	 * @return
	 * 		Address of the central server.
	 */
	public String getCentralIp()
	{	return centralIp;		
	}

	/////////////////////////////////////////////////////////////////
	// HOSTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of known hosts */
	private Map<String,HostInfo> hosts = null;
	
	/**
	 * Refreshes the info regarding a host.
	 * 
	 * @param hostInfo
	 * 		Updated host info.
	 */
	public void synchronizeHost(HostInfo hostInfo)
	{	// synch host
		HostInfo hi = hosts.get(hostInfo.getId());
		if(hi==null)
		{	hosts.put(hostInfo.getId(),hostInfo);
		}
		else
		{	hi.setLastIp(hostInfo.getLastIp());
			hi.setLastPort(hostInfo.getLastPort());
		}
		
		// save changes
/*		try
		{	saveHosts();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}*/
	}
	
	/**
	 * Records the map of known hosts.
	 * 
	 * @throws IOException
	 * 		Problem while recording the file.
	 */
	public void saveHosts() throws IOException
	{	if(hosts!=null)
			HostsSaver.saveHosts(hosts);
	}
	
	/**
	 * Gets the map of known hosts.
	 * 
	 * @return
	 * 		Map of known hosts.
	 */
	public Map<String,HostInfo> getHosts()
	{	return hosts;	
	}
	
	/**
	 * Returns the info describing the local host.
	 * 
	 * @return
	 * 		Local host description.
	 */
	public HostInfo getLocalHostInfo()
	{	HostInfo result = new HostInfo();
		result.setId(hostId);
		result.setName(hostName);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PORT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Port used by the local host */
	private int port;
	
	/**
	 * Returns the port used by the local host.
	 * 
	 * @return
	 * 		Port used by the local host.
	 */
	public int getPort()
	{	return port;
	}

	/**
	 * Changes the port used by the local host.
	 * 
	 * @param port
	 * 		New port to be used by the local host.
	 */
	public void setPort(int port)
	{	this.port = port;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST NAME			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name used by the local host */
	private String hostName;
	
	/**
	 * Returns the name used by the local host.
	 * 
	 * @return
	 * 		Name used by the local host.
	 */
	public String getHostName()
	{	return hostName;
	}

	/**
	 * Changes the name used by the local host.
	 * 
	 * @param hostName
	 * 		New name to be used by the local host.
	 */
	public void setHostName(String hostName)
	{	this.hostName = hostName;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST ID				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Unique id used by the local host */
	private String hostId;
	
	/**
	 * Returns the unique id used by the local host.
	 * 
	 * @return
	 * 		Unique id used by the local host.
	 */
	public String getHostId()
	{	return hostId;
	}

	/**
	 * Changes the unique id used by the local host.
	 * 
	 * @param hostId
	 * 		New unique id to be used by the local host.
	 */
	public void setHostId(String hostId)
	{	this.hostId = hostId;
	}
	
	/////////////////////////////////////////////////////////////////
	// MAC ADDRESSES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** MAC addresses of the devices used for communication (usually, a single one) */
	private List<String> macAddresses = new ArrayList<String>();
	
	/**
	 * Whether or not the specified MAC address was previously used
	 * by the local host.
	 * 
	 * @param address
	 * 		A MAC address.
	 * @return
	 * 		{@code true} iff the specified address was previously used.
	 */
	public boolean hasMacAddress(String address)
	{	boolean result = macAddresses.contains(address);
		return result;
	}
	
	/**
	 * Changes the list of MAC addresses for the local host.
	 * 
	 * @param addresses
	 * 		Local host MAC addresses.
	 */
	public void setMacAddresses(List<String> addresses)
	{	macAddresses = addresses;
	}
	
	/**
	 * Returns the list of MAC addresses for the local host.
	 * 
	 * @return
	 * 		A list of MAC addresses.
	 */
	public List<String> getMacAddresses()
	{	return macAddresses;
	}
	
	/////////////////////////////////////////////////////////////////
	// SERVER GENERAL CONNECTION	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Connection with the current game server */
	private ServerGeneralConnection serverConnection;
	
	/**
	 * Returns the connection to the current game server.
	 * 
	 * @return
	 * 		Connection to the game server.
	 */
	public ServerGeneralConnection getServerConnection()
	{	return serverConnection;
	}

	/**
	 * Changes the connection to the current game server.
	 * 
	 * @param serverConnection
	 * 		New game servere connection.
	 */
	public void setServerConnection(ServerGeneralConnection serverConnection)
	{	this.serverConnection = serverConnection;
	}

	/////////////////////////////////////////////////////////////////
	// CLIENT GENERAL CONNECTION	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current client connection */
	private ClientGeneralConnection clientConnection;
	
	/**
	 * Returns the current client connection.
	 * 
	 * @return
	 * 		Client connection.
	 */
	public ClientGeneralConnection getClientConnection()
	{	return clientConnection;
	}
	
	/**
	 * Changes the current client connection.
	 * 
	 * @param clientConnection
	 * 		New client connection.
	 */
	public void setClientConnection(ClientGeneralConnection clientConnection)
	{	this.clientConnection = clientConnection;
	}
	
	/**
	 * Initializes the client connection.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while creating the connection.
	 * @throws SecurityException
	 * 		Problem while creating the connection.
	 * @throws ParserConfigurationException
	 * 		Problem while creating the connection.
	 * @throws SAXException
	 * 		Problem while creating the connection.
	 * @throws IOException
	 * 		Problem while creating the connection.
	 * @throws IllegalAccessException
	 * 		Problem while creating the connection.
	 * @throws NoSuchFieldException
	 * 		Problem while creating the connection.
	 * @throws ClassNotFoundException
	 * 		Problem while creating the connection.
	 */
	public void initClientConnection() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(clientConnection==null)
		{	hosts = HostsLoader.loadHosts();
			List<HostInfo> h = new ArrayList<HostInfo>(hosts.values());
			clientConnection = new ClientGeneralConnection(h);
		}
	}

	/////////////////////////////////////////////////////////////////
	// GENERAL CONNECTION			/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Closes the current connection.
	 */
	public void terminateConnection()
	{	if(clientConnection!=null)
		{	clientConnection.terminateConnection();
			clientConnection = null;
		}
		
		if(serverConnection!=null)
		{	serverConnection.terminateConnection();
			serverConnection = null;
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Objects listening to network configuration changes */
	private List<UpdateListener> listeners = new ArrayList<UpdateListener>();
	
	/**
	 * Adds a new lister to this configuration object.
	 * 
	 * @param listener
	 * 		New listener.
	 */
	public void addListener(UpdateListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	/**
	 * Removes the specified lister from this configuration object.
	 * 
	 * @param listener
	 * 		Listener to be removed.
	 */
	public void removeListener(UpdateListener listener)
	{	listeners.remove(listener);
	}
	
	/**
	 * Fetch an event to all listeners registered
	 * for this configuration object.
	 * 
	 * @param event
	 * 		Fired event.
	 */
	private void fireUpdateEvent(UpdateEvent event)
	{	for(UpdateListener listener: listeners)
			listener.updated(event);
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	private boolean finished = false;
	
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	if(!finished)
		{	finished = true;
			
			UpdateEvent event = new UpdateEvent(this,EventName.FINISHED);
			fireUpdateEvent(event);
			listeners.clear();
			
			centralIp = null;
			hosts = null;
		}
	}
}
