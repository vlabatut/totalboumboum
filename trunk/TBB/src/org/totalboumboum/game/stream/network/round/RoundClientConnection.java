package org.totalboumboum.game.stream.network.round;

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
import org.totalboumboum.game.stream.network.connection.AbstractConnection;
import org.totalboumboum.game.stream.network.connection.RoundClientConnectionListener;
import org.totalboumboum.statistics.detailed.StatisticMatch;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundClientConnection extends AbstractConnection<RoundClientConnectionListener>
{	
	public RoundClientConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateMatch(Match match) throws IOException
	{	write(match);
	}
	
	public void updateStats(StatisticMatch stats) throws IOException
	{	write(stats);
	}
	
	public void startRound(Boolean next) throws IOException
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
