package org.totalboumboum.tools;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
 * General class with various information
 * regarding the program.
 * 
 * @author Vincent Labatut
 */
public class GameData
{
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Version of the game */
	public static final String VERSION = "1.beta.024";
	/** Tolerance when comparing real values */
	public static final double TOLERANCE = 0.01;
	/** Whether or node the game is executed in quickstart mode */
	public static boolean quickMode;
	/** Whether this is a production version */
	public static boolean PRODUCTION = false;
	
	/////////////////////////////////////////////////////////////////
	// TILE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Traditional dimension of a tile */
	public static final int STANDARD_TILE_DIMENSION = 16;
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of (human) controls */
	public static final int CONTROL_COUNT = 5; 

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Maximal number of players for a game */
	public static final int MAX_PROFILES_COUNT = 16;

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Duration for the "ready" step */
	public static final double READY_TIME = 1000;
	/** Duration for the "set" step */
	public static final double SET_TIME = 1000;
	/** Duration for the "go" step */
	public static final double GO_TIME = 750;
}
