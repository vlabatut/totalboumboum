package org.totalboumboum.network.newstream.server;

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

import org.totalboumboum.network.newstream.server.configuration.ServerConfigurationConnection;
import org.totalboumboum.network.newstream.server.match.ServerMatchConnection;
import org.totalboumboum.network.newstream.server.round.ServerRoundConnection;
import org.totalboumboum.network.newstream.server.tournament.ServerTournamentConnection;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerGeneralConnection implements Runnable
{
	/////////////////////////////////////////////////////////////////
	// NEW CONNECTION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	
		/* TODO
		 * 
		 * - listen to the socket 
		 * - create a new thread for every new connection
		 * - this new thread calls handleNewConnection on it
		 */
	}
	
	private void handleNewConnection()
	{
		/* TODO
		 * - wait for the first message to be read
		 * - depending on the message, decide if the connection is config- or game-related
		 * - creates an instance of the appropriate class
		 */
		
		/**TODO
		 * >> it's actually simplest to put two threads every where
		 * >> the tournamentConnection class actually does the whole processing for tournament/match/round (?)
		 */
	}
	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerConfigurationConnection configurationConnection;
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerTournamentConnection tournamentConnection;

	/////////////////////////////////////////////////////////////////
	// MATCH 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerMatchConnection matchConnection;

	/////////////////////////////////////////////////////////////////
	// ROUND 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerRoundConnection roundConnection;

}
