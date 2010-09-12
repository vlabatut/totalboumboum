package org.totalboumboum.network.newstream.message;

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
	/** server sends game information */ 
	UPDATE_GAME_INFO,
	/** server sends players list */
	UPDATE_PLAYERS_LIST,
	
	
	// tournament level
	/** client requests current tournament stats */
	REQUEST_TOURNAMENT_STATS,
	/** server sends current tournament stats */
	UPDATE_TOURNAMENT_STATS,

	// match level
	/** client requests current match stats */
	REQUEST_MATCH_STATS,
	/** server sends current match stats */
	UPDATE_MATCH_STATS,
	
	// round level
	/** client requests current round stats */
	REQUEST_ROUND_STATS,
	/** server sends current round stats */
	UPDATE_ROUND_STATS,
	
	// in-game level
	/** client indicates it leaves the tournament */
	INFO_GAME_QUIT,
	/** client indicates a player's action (while playing) */
	INFO_PLAYER_CONTROL,
	/** server sends an update event (while playing) */
	INFO_REPLAY
}
