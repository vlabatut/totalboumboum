package org.totalboumboum.stream.network.message;

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
public enum MessageName
{	
	
	/* 
	 * config level
	 */
		/**
		 * client indcates it validates its player selection and starts
		 * waiting for the tournament to start.
		 */
		CONFIRMING_PLAYERS_SELECTION,
		/** the opposite */
		UNCONFIRMING_PLAYERS_SELECTION,
		/** 
		 * if true, the client selects a game and enters the players selection screen
		 * else, it means it enters another game (but might come back to game selection) 
		 * */
		ENTERING_PLAYERS_SELECTION,
		/** client abort players selection to go back to the game selection screen */
		EXITING_PLAYERS_SELECTION,
		/** 
		 * client requests game information
		 * >must send its id in the message
		 * >possible causes:
		 * 		- client connects for the first time
		 * 		- client tries to reconnect (after accidental disconnection)
		 * 		- a manual refresh was performed
		 * 		- central checks for progress
		 * >behavior:
		 * 		- server always fulfills this request (whatever C/S state) 
		 */ 
		REQUESTING_GAME_INFO,
		/** 
		 * client requests players list
		 * >possible causes:
		 * 		- client switches from game selection to player selection
		 * 		- client just reconnected at this stage
		 * 		- central might be interested too
		 * >behavior:
		 * 		- server answers only when it's open
		 * 		- or to the central, anytime
		 */
		REQUESTING_PLAYERS_LIST,
		/** client adds a new player */
		REQUESTING_PLAYERS_ADD,
		/** client remove one of its players */ 
		REQUESTING_PLAYERS_REMOVE,
		/** client change one of its players color*/ 
		REQUESTING_PLAYERS_CHANGE_COLOR,
		/** client change one of its players sprite*/ 
		REQUESTING_PLAYERS_CHANGE_HERO,
		/** client replace one of its players by another one */ 
		REQUESTING_PLAYERS_SET,
		/** client sends its current state */ 
		UPDATING_CLIENT_STATE,
		
		/** server requests client current state */ 
//		REQUESTING_CLIENT_STATE, //pas nécessaire, à éviter même
		/** server sends game information */ 
		UPDATING_GAME_INFO,
		/** server sends players list */
		UPDATING_PLAYERS_LIST,
	
	/* 
	 * tournament level
	 */
		/** client requests current tournament stats */
		REQUESTING_TOURNAMENT_STATS,
		/** server starts tournament */
		STARTING_TOURNAMENT,
		/** server sends current tournament stats */
		UPDATING_TOURNAMENT_STATS,

	/* 
	 * match level
	 */
		/** client requests current match stats */
		REQUESTING_MATCH_STATS,
		/** server sends current match stats */
		UPDATING_MATCH_STATS,
	
	/* 
	 * round level
	 */
		/** client requests current round stats */
		REQUESTING_ROUND_STATS,
		/** server sends current round stats */
		UPDATING_ROUND_STATS,
		/** server sending zoom coefficient to the clients */
		UPDATING_ZOOM_COEFF,
	
	/* 
	 * in-game level
	 */
		/** client indicates it leaves the tournament */
		INFO_GAME_QUIT,
		/** client indicates a player's action (while playing) */
		INFO_PLAYER_CONTROL,
		/** server sends an update event (while playing) */
		INFO_REPLAY,
		
	/* 
	 * common
	 */
		/** server accept/reject reconnection */
		ANSWERING_RECONNECTION,
		/** client asks for reconnection */
		REQUESTING_RECONNECTION,
		/** client or server asks for disconnection*/
		REQUESTING_DISCONNECTION,
		/** client sends its new state */
		UPDATING_STATE
}
