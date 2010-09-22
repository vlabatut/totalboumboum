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
		 * if true, the client selects a game and enters the players selection screen
		 * else, it means it enters another game (but might come back to game selection) 
		 * */
		ENTERS_PLAYERS_SELECTION,
		/** client abort players selection to go back to the game selection screen */
		EXITS_PLAYERS_SELECTION,
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
		REQUEST_GAME_INFO,
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
		REQUEST_PLAYERS_LIST,
		/** client adds a new player */
		REQUEST_PLAYERS_ADD,
		/** client remove one of its players */ 
		REQUEST_PLAYERS_REMOVE,
		/** client change one of its players color*/ 
		REQUEST_PLAYERS_CHANGE_COLOR,
		/** client change one of its players sprite*/ 
		REQUEST_PLAYERS_CHANGE_HERO,
		/** client replace one of its players by another one */ 
		REQUEST_PLAYERS_SET,
		/** client sends its current state */ 
		UPDATE_CLIENT_STATE,
		
		/** server requests client current state */ 
//		REQUEST_CLIENT_STATE, //pas nécessaire, à éviter même
		/** server sends game information */ 
		UPDATE_GAME_INFO,
		/** server sends players list */
		UPDATE_PLAYERS_LIST,
	
	/* 
	 * tournament level
	 */
		/** client requests current tournament stats */
		REQUEST_TOURNAMENT_STATS,
		/** server sends current tournament stats */
		UPDATE_TOURNAMENT_STATS,

	/* 
	 * match level
	 */
		/** client requests current match stats */
		REQUEST_MATCH_STATS,
		/** server sends current match stats */
		UPDATE_MATCH_STATS,
	
	/* 
	 * round level
	 */
		/** client requests current round stats */
		REQUEST_ROUND_STATS,
		/** server sends current round stats */
		UPDATE_ROUND_STATS,
	
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
		ANSWER_RECONNECTION,
		/** client asks for reconnection */
		REQUEST_RECONNECTION,
		/** client or server asks for disconnection*/
		REQUEST_DISCONNECTION,
		/** client sends its new state */
		UPDATING_STATE
}

/**
 * reconnection process:
 * 	- client connects and sends a REQUEST_RECONNECTION message, with its id
 * 	- server checks if the id's
 * 	- sends back an ANSWER_RECONNECTION message with a boolean showing acceptation or reject
 *  - if accepted, the server then sends the necessary updates to the client
 */

/**
 * deconnection process:
 *  - none. can be seen as accidental, so no need to exchange any specific message 
 */