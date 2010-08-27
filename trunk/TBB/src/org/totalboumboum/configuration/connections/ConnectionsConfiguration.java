package org.totalboumboum.configuration.connections;

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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostInfo;
import org.totalboumboum.network.host.HostsLoader;
import org.totalboumboum.network.host.HostsSaver;
import org.totalboumboum.tools.event.EventName;
import org.totalboumboum.tools.event.UpdateEvent;
import org.totalboumboum.tools.event.UpdateListener;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ConnectionsConfiguration
{
	public ConnectionsConfiguration copy()
	{	ConnectionsConfiguration result = new ConnectionsConfiguration();
	
		result.setCentralIp(centralIp);

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// CENTRAL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InetAddress centralIp;

	public void setCentralIp(InetAddress centralIp)
	{	this.centralIp = centralIp;		
	}
	
	public InetAddress getCentralIp()
	{	return centralIp;		
	}

	/////////////////////////////////////////////////////////////////
	// CENTRAL CONNECTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<GameInfo> centralConnections = new ArrayList<GameInfo>();
	
	private void updateCentralConnections()
	{	if(centralConnections.isEmpty())
		{	// TODO use the central ip to get all connections
		}
	}
	
	public List<GameInfo> getCentralConnections()
	{	return centralConnections;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECT CONNECTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<GameInfo> directConnections = new ArrayList<GameInfo>();
	
	private void updateDirectConnections()
	{	if(directConnections.isEmpty())
		{	for(HostInfo host: hosts.values())
			{	if(host.isDirect())
				{	GameInfo gameInfo = new GameInfo();
					gameInfo.setHostInfo(host);
					directConnections.add(gameInfo);
				}
			}
		}
	}
	
	public List<GameInfo> getDirectConnections()
	{	return directConnections;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOSTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,HostInfo> hosts = null;
	
	private void updateHosts() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(hosts==null)
			hosts = HostsLoader.loadHosts();
	}
	
	public HostInfo createHost(InetAddress ip)
	{	HostInfo result = null;
	
		// TODO to be defined
		
		return result;
	}
	
	public void saveHosts() throws IOException
	{	if(hosts!=null)
			HostsSaver.saveHosts(hosts);
	}
	
	public HostInfo getLocalHostInfo()
	{	HostInfo result = new HostInfo();
		result.setId(hostId);
		result.setName(hostName);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void updateConnections() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	updateHosts();
		updateDirectConnections();
		updateCentralConnections();
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
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			
			UpdateEvent event = new UpdateEvent(this,EventName.FINISHED);
			fireUpdateEvent(event);
			listeners.clear();
			
			centralIp = null;
			centralConnections = null;
			directConnections = null;
			hosts = null;
		}
	}
}
