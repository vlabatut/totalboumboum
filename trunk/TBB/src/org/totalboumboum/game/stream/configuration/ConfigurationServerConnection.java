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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.game.stream.network.AbstractConnection;
import org.totalboumboum.game.tournament.AbstractTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ConfigurationServerConnection extends AbstractConnection<ConfigurationServerConnectionListener>
{	
	public ConfigurationServerConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void addProfile(Profile profile) throws IOException
	{	write(profile);
	}
	
	public void removeProfile(Profile profile) throws IOException
	{	Integer id = profile.getId();
		write(id);
	}
	
	public void changeSprite(Profile profile) throws IOException
	{	List<Object> list = new ArrayList<Object>();
		list.add(profile.getId());
		list.add(profile.getSelectedSprite());
		write(list);
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	@Override
	public void dataRead(Object data)
	{	if(data instanceof AbstractTournament)
		{	AbstractTournament tournament = (AbstractTournament)data;
			fireTournamentRead(tournament);
		}
		else if(data instanceof List)
		{	List<Profile> profiles = (List<Profile>)data;
			fireProfilesRead(profiles);
		}
		else if(data instanceof Boolean)
		{	Boolean start = (Boolean) data;
			fireTournamentStarted(start);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireTournamentRead(AbstractTournament tournament)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.tournamentRead(tournament);
	}

	private void fireProfilesRead(List<Profile> profiles)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.profilesRead(profiles);
	}

	private void fireTournamentStarted(Boolean start)
	{	for(ConfigurationServerConnectionListener listener: listeners)
			listener.tournamentStarted(start);
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
