package org.totalboumboum.stream.network.client;

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
public enum ClientState
{	// config level
	/** client currently choosing a game */
	SELECTING_GAME,
	/** client has chosen a game, currently selecting his players */
	SELECTING_PLAYERS,
	/** client has chosen his players, currently waiting for the tournament to begin */
	WAITING_TOURNAMENT
	
	// tournament level
	
	// match level
	
	// round level
	
	// in-game level
}
