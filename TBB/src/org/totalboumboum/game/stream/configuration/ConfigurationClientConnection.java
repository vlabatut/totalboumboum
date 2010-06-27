package org.totalboumboum.game.stream.configuration;

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
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.configuration.profile.SpriteInfo;
import org.totalboumboum.game.stream.AbstractConnection;
import org.totalboumboum.game.tournament.AbstractTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ConfigurationClientConnection extends AbstractConnection<ConfigurationClientConnectionListener>
{	
	public ConfigurationClientConnection(Socket socket, AbstractTournament tournament) throws IOException
	{	super(socket);
		write(tournament);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void startTournament(Boolean start) throws IOException
	{	write(start);
	}
	
	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	@Override
	public void dataRead(Object data)
	{	if(data instanceof Profile)
		{	Profile profile = (Profile)data;
			fireProfileAdded(profile);
		}
		else if(data instanceof Integer)
		{	Integer id = (Integer)data;
			fireProfileRemoved(id);
		}
		else if(data instanceof List)
		{	List<Object> temp = (List<Object>)data;
			Integer id = (Integer)temp.get(0);
			SpriteInfo sprite = (SpriteInfo)temp.get(1);
			fireSpriteChanged(id,sprite);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireProfileAdded(Profile profile)
	{	for(ConfigurationClientConnectionListener listener: listeners)
			listener.profileAdded(profile);
	}
	
	private void fireProfileRemoved(Integer id)
	{	for(ConfigurationClientConnectionListener listener: listeners)
			listener.profileRemoved(id);
	}
	
	private void fireSpriteChanged(Integer id, SpriteInfo sprite)
	{	for(ConfigurationClientConnectionListener listener: listeners)
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
