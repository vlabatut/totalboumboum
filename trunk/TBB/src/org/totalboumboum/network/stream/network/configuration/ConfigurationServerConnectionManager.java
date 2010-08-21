package org.totalboumboum.network.stream.network.configuration;

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
import java.net.Socket;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.configuration.profile.SpriteInfo;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.network.stream.network.AbstractConnectionManager;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ConfigurationServerConnectionManager extends AbstractConnectionManager<ConfigurationServerConnectionListener,ConfigurationServerConnection> implements ConfigurationServerConnectionListener
{	
	public ConfigurationServerConnectionManager(AbstractTournament tournament) throws IOException
	{	this.tournament = tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private AbstractTournament tournament;
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void addConnection(Socket socket) throws IOException
	{	ConfigurationServerConnection connection = new ConfigurationServerConnection(socket,tournament);
		addConnection(connection);
	}
	
	@Override
	protected synchronized void addConnection(ConfigurationServerConnection connection)
	{	connections.add(connection);
		connection.addListener(this);
	}

	@Override
	protected void clearConnections()
	{	for(ConfigurationServerConnection connection: connections)
			connection.removeListener(this);
		connections.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void startTournament(Boolean start) throws IOException
	{	for(ConfigurationServerConnection connection: connections)
			connection.write(start);
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void profileAdded(Profile profile)
	{	fireProfileAdded(profile);
	}

	@Override
	public void profileRemoved(String id)
	{	fireProfileRemoved(id);
	}

	@Override
	public void spriteChanged(String id, SpriteInfo sprite)
	{	fireSpriteChanged(id,sprite);
	}

	private void fireProfileAdded(Profile profile)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.profileAdded(profile);
	}
	
	private void fireProfileRemoved(String id)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.profileRemoved(id);
	}
	
	private void fireSpriteChanged(String id, SpriteInfo sprite)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.spriteChanged(id,sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
		}
	}
}
