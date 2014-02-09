package org.totalboumboum.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
 * This type represents the various types of tournaments.
 * 
 * @author Vincent Labatut
 */
public enum TournamentType
{
	/** Knock-out type tournament (direct eliminations) */
	CUP,
	/** Round-robin tournament (each player meets all the others) */
	LEAGUE,
	/** Sequence of matches opposing the same players */
	SEQUENCE,
	/** One single match */
	SINGLE,
	/** Around the table tournament (players alternatively play and wait) */ 
	TURNING;
	
	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Express this enum value as a string.
	 * The initial is uppercase, the rest is lowercase.
	 * 
	 * @return
	 * 		A string representation of this score.
	 */
	public String stringFormat()
	{	StringBuffer result = new StringBuffer();
		String raw = this.toString(); 
		result.append(raw.substring(0,1));
		raw = raw.toLowerCase();
		result.append(raw.substring(1,raw.length()));
		return result.toString();
	}
}
