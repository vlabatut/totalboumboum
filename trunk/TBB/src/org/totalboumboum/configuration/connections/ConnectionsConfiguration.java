package org.totalboumboum.configuration.connections;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.HashMap;
import java.util.List;

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
	private String centralIp;

	public void setCentralIp(String centralIp)
	{	this.centralIp = centralIp;		
	}
	
	public String getCentralIp()
	{	return centralIp;		
	}

	/////////////////////////////////////////////////////////////////
	// HOSTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,HostInfo> hosts = null;
	
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
	
	public void saveHosts() throws IOException
	{	if(hosts!=null)
			HostsSaver.saveHosts(hosts);
	}
	
	public HashMap<String,HostInfo> getHosts()
	{	return hosts;	
	}
	
	public HostInfo getLocalHostInfo()
	{	HostInfo result = new HostInfo();
		result.setId(hostId);
		result.setName(hostName);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PORT					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int port;
	
	public int getPort()
	{	return port;
	}

	public void setPort(int port)
	{	this.port = port;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST NAME			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String hostName;
	
	public String getHostName()
	{	return hostName;
	}

	public void setHostName(String hostName)
	{	this.hostName = hostName;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOST ID				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String hostId;
	
	public String getHostId()
	{	return hostId;
	}

	public void setHostId(String hostId)
	{	this.hostId = hostId;
	}
	
	/////////////////////////////////////////////////////////////////
	// MAC ADDRESSES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<String> macAddresses = new ArrayList<String>();
	
	public boolean hasMacAddress(String address)
	{	boolean result = macAddresses.contains(address);
		return result;
	}
	
	public void setMacAddresses(List<String> addresses)
	{	macAddresses = addresses;
	}
	
	public List<String> getMacAddresses()
	{	return macAddresses;
	}
	
	/////////////////////////////////////////////////////////////////
	// SERVER GENERAL CONNECTION	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerGeneralConnection serverConnection;
	
	public ServerGeneralConnection getServerConnection()
	{	return serverConnection;
	}

	public void setServerConnection(ServerGeneralConnection serverConnection)
	{	this.serverConnection = serverConnection;
	}

	/////////////////////////////////////////////////////////////////
	// CLIENT GENERAL CONNECTION	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientGeneralConnection clientConnection;
	
	public ClientGeneralConnection getClientConnection()
	{	return clientConnection;
	}

	public void setClientConnection(ClientGeneralConnection clientConnection)
	{	this.clientConnection = clientConnection;
	}
	
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
	private List<UpdateListener> listeners = new ArrayList<UpdateListener>();
	
	public void addListener(UpdateListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeListener(UpdateListener listener)
	{	listeners.remove(listener);
	}
	
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
