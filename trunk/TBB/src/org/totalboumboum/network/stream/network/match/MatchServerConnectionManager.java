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
import org.totalboumboum.network.stream.network.AbstractConnectionManager;
import org.totalboumboum.statistics.detailed.StatisticMatch;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MatchServerConnectionManager extends AbstractConnectionManager<MatchServerConnectionListener,MatchServerConnection> implements MatchServerConnectionListener
{	
	public MatchServerConnectionManager() throws IOException
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void addConnection(Socket socket) throws IOException
	{	MatchServerConnection connection = new MatchServerConnection(socket);
		addConnection(connection);
	}
	
	@Override
	protected void addConnection(MatchServerConnection connection)
	{	connections.add(connection);
		connection.addListener(this);
	}

	@Override
	protected void clearConnections()
	{	for(MatchServerConnection connection: connections)
			connection.removeListener(this);
		connections.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateMatch(Match match) throws IOException
	{	for(MatchServerConnection connection: connections)
			connection.write(match);
	}
	
	public void updateStats(StatisticMatch stats) throws IOException
	{	for(MatchServerConnection connection: connections)
			connection.write(stats);
	}
	
	public void startRound(Boolean next) throws IOException
	{	for(MatchServerConnection connection: connections)
			connection.write(next);
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
