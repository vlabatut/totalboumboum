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
import org.totalboumboum.game.network.host.HostType;
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
	
	private void updateCentralConnections()
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
	
	private void updateDirectConnections()
	{	for(HostInfo host: recordedHosts.values())
		{	if(host.getType()==HostType.DIRECT)
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
	// RECORDED HOSTS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,HostInfo> recordedHosts = null;
	
	private void updateRecordedHosts() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	if(recordedHosts==null)
			recordedHosts = HostsLoader.loadHosts();
	}
	
	public void saveRecordedHosts() throws IOException
	{	if(recordedHosts!=null)
			HostsSaver.saveHosts(recordedHosts);
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void updateConnections() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	updateRecordedHosts();
		updateDirectConnections();
		updateCentralConnections();
	}
}
