package org.totalboumboum.game.stream.network.connection;

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
import org.totalboumboum.game.stream.network.connection.AbstractConnection;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientConnection extends AbstractConnection<ConfigurationClientConnectionListener>
{	
	public ClientConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateProfiles(List<Profile> profiles) throws IOException
	{	write(profiles);
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateTournament(AbstractTournament tournament) throws IOException
	{	write(tournament);
	}
	
	public void updateTournamentStats(StatisticTournament stats) throws IOException
	{	write(stats);
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	

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
	// LISTENERS				/////////////////////////////////////
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
