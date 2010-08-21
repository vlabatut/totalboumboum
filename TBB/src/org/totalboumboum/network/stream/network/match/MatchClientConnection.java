package org.totalboumboum.network.stream.network.match;

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

import org.totalboumboum.game.match.Match;
import org.totalboumboum.network.stream.network.AbstractConnection;
import org.totalboumboum.statistics.detailed.StatisticMatch;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MatchClientConnection extends AbstractConnection<MatchClientConnectionListener>
{	
	public MatchClientConnection(Socket socket) throws IOException
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
	{	if(data instanceof Match)
		{	Match match = (Match)data;
			fireMatchUpdated(match);
		}
		else if(data instanceof StatisticMatch)
		{	StatisticMatch stats = (StatisticMatch)data;
			fireStatsUpdated(stats);
		}
		else if(data instanceof Boolean)
		{	Boolean next = (Boolean) data;
			fireRoundStarted(next);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireMatchUpdated(Match match)
	{	for(MatchClientConnectionListener listener: listeners)
			listener.matchUpdated(match);
	}

	private void fireStatsUpdated(StatisticMatch stats)
	{	for(MatchClientConnectionListener listener: listeners)
			listener.statsUpdated(stats);
	}

	private void fireRoundStarted(Boolean start)
	{	for(MatchClientConnectionListener listener: listeners)
			listener.roundStarted(start);
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
