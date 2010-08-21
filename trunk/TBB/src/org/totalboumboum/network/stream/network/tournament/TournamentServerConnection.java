package org.totalboumboum.network.stream.network.tournament;

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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.network.stream.network.AbstractConnection;
import org.totalboumboum.statistics.detailed.StatisticTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TournamentServerConnection extends AbstractConnection<TournamentServerConnectionListener>
{	
	public TournamentServerConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateTournament(AbstractTournament tournament) throws IOException
	{	write(tournament);
	}
	
	public void updateStats(StatisticTournament stats) throws IOException
	{	write(stats);
	}
	
	public void startMatch(Boolean next) throws IOException
	{	write(next);
	}
	
	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void dataRead(Object data)
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
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
