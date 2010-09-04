package org.totalboumboum.network.newstream.event;

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
public enum NetworkInfo
{	// config level
	/** client requests game information */ 
	REQUEST_GAME_INFO,
	/** client requests players list */
	REQUEST_PLAYERS_LIST,
	/** client adds a new player */
	REQUEST_PLAYERS_ADD,
	
	/**
	 * TODO faire un dessin pr visualiser les ps
	 * >> possible qu'il y ait concurrence qd plusieurs clients veulent ajouter un jour en même temps
	 */
	
	// tournament level
	/** client requests current tournament stats */
	REQUEST_TOURNAMENT_STATS,

	// match level
	/** client requests current match stats */
	REQUEST_MATCH_STATS,
	
	// round level
	/** client requests current round stats */
	REQUEST_ROUND_STATS,
	
	// in-game level
	/** client indicates it leaves the tournament */
	INFO_GAME_QUIT,
	/** client indicates a player's move (while playing) */
	INFO_GAME_MOVE
}
