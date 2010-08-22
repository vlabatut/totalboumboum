package org.totalboumboum.network.newstream.server.configuration;

import org.totalboumboum.network.newstream.server.ServerGeneralConnection;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerConfigurationConnection
{

	// this is necessary in order to fetch stuff read from the clien
	private ServerGeneralConnection generalConnection;
	
	public ServerConfigurationConnection()
	{	
		/*
		 *		- add the gameInfo to the queue of object to be written
		 * 		- create the second thread, send it execute processOutput
		 * 		- the first thread then goes executing processInput
		 */
	}
	
	private void processInput()
	{
		/*TODO
		 * - write the game info using gameInfoRequested
		 * - infinite loop: 
		 * 	- wait for the next message
		 * 	- process this message
		 *  
		 */
	}

	private void processOutput()
	{
		/*TODO
		 * - infinite loop: 
		 * 	- wait for the next message
		 * 	- process this message
		 *  
		 */
	}

	private void gameInfoRequested()
	{
		// TODO write the game info
		// (using a queue, see the old classes, need a local write method)
	}
	
	private void playersListRequested()
	{
		// TODO write the players list
	}
}
