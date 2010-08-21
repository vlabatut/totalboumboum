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
public class TournamentClientConnection extends AbstractConnection<TournamentClientConnectionListener>
{	
	public TournamentClientConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void dataRead(Object data)
	{	if(data instanceof AbstractTournament)
		{	AbstractTournament tournament = (AbstractTournament)data;
			fireTournamentUpdated(tournament);
		}
		else if(data instanceof StatisticTournament)
		{	StatisticTournament stats = (StatisticTournament)data;
			fireStatsUpdated(stats);
		}
		else if(data instanceof Boolean)
		{	Boolean next = (Boolean) data;
			fireMatchStarted(next);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireTournamentUpdated(AbstractTournament tournament)
	{	for(TournamentClientConnectionListener listener: listeners)
			listener.tournamentUpdated(tournament);
	}

	private void fireStatsUpdated(StatisticTournament stats)
	{	for(TournamentClientConnectionListener listener: listeners)
			listener.statsUpdated(stats);
	}

	private void fireMatchStarted(Boolean start)
	{	for(TournamentClientConnectionListener listener: listeners)
			listener.matchStarted(start);
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
