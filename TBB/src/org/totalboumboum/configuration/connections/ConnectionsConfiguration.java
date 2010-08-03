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

import org.totalboumboum.game.network.game.GameInfo;
import org.totalboumboum.game.network.host.HostInfo;
import org.totalboumboum.game.network.host.HostsLoader;
import org.totalboumboum.game.network.host.HostsSaver;
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
	
	private void initCentralConnections()
	{
		// TODO use the central ip to get all connections
	}
	
	public List<GameInfo> getCentralConnections()
	{	return centralConnections;
	}
	
	/////////////////////////////////////////////////////////////////
	// DIRECT CONNECTIONS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<GameInfo> directConnections = new ArrayList<GameInfo>();
	
	private void initDirectConnections()
	{	for(HostInfo host: preferredHosts.values())
		{	if(host.isPreferred())
			{	GameInfo gameInfo = new GameInfo();
				gameInfo.setHostInfo(host);
				directConnections.add(gameInfo);
			}
		}
	}
	
	public List<GameInfo> getDirectConnections()
	{	return directConnections;
	}
	
	/////////////////////////////////////////////////////////////////
	// PREFERRED HOSTS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,HostInfo> preferredHosts = null;
	
	private void loadPreferredHosts() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(preferredHosts==null)
			preferredHosts = HostsLoader.loadHosts();
	}
	
	private void savePreferredHosts() throws IOException
	{	if(preferredHosts!=null)
			HostsSaver.saveHosts(preferredHosts);
	}
	
	public HashMap<String,HostInfo> getPreferredHosts()
	{	return preferredHosts;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initConnections() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	loadPreferredHosts();
		initDirectConnections();
	}
}
