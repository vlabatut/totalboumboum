package org.totalboumboum.stream.network.data.host;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
public enum HostState
{	
	/** one can register to the game (if there's room, that is)*/
	OPEN,
	/** too late to register */
	CLOSED,
	/** the game is currently going on */
	PLAYING,
	/** the game is finished and no new one has been set up yet */
	FINISHED,
	/** currently retrieving the host state */
	RETRIEVING,
	/** host state currently unknown (disconnected) */
	UNKOWN;
}
