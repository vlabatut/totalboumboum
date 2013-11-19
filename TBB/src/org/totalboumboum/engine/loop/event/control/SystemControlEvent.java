package org.totalboumboum.engine.loop.event.control;

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
 * Codes used to represent specific in-game system events
 * 
 * @author Vincent Labatut
 *
 */
public class SystemControlEvent
{	/** Prematurely end a round */
	public final static String REQUIRE_CANCEL_ROUND = "REQUIRE_CANCEL_ROUND";
	/** Make a screen capture */
	public final static String REQUIRE_PRINT_SCREEN = "REQUIRE_PRINT_SCREEN";
	/** Execute a single engine iteration */
	public final static String REQUIRE_ENGINE_STEP = "REQUIRE_ENGINE_STEP";
	/** Display next start message */
	public final static String REQUIRE_NEXT_MESSAGE = "REQUIRE_NEXT_MESSAGE";
	/** Record the current percepts for the concerned agent */
	public final static String REQUIRE_RECORD_AI_PERCEPTS = "REQUIRE_RECORD_AI_PERCEPTS";
	/** Slow down engine */
	public final static String REQUIRE_SLOW_DOWN = "REQUIRE_SLOW_DOWN";
	/** Speed up engine */
	public final static String REQUIRE_SPEED_UP = "REQUIRE_SPEED_UP";
	/** Pause agent */
	public final static String SWITCH_AIS_PAUSE = "SWITCH_AIS_PAUSE";
	/** Replay command: backwards */
	public final static String SWITCH_BACKWARD = "SWITCH_BACKWARD";
	/** Display agent colored tiles */
	public final static String SWITCH_DISPLAY_AIS_COLORS = "SWITCH_DISPLAY_AIS_COLORS";
	/** Display agent paths */
	public final static String SWITCH_DISPLAY_AIS_PATHS = "SWITCH_DISPLAY_AIS_PATHS";
	/** Display agent texts */
	public final static String SWITCH_DISPLAY_AIS_TEXTS = "SWITCH_DISPLAY_AIS_TEXTS";
	/** Display engine FPS */
	public final static String SWITCH_DISPLAY_FPS = "SWITCH_DISPLAY_FPS";
	/** Display tile grid */
	public final static String SWITCH_DISPLAY_GRID = "SWITCH_DISPLAY_GRID";
	/** Display player names */
	public final static String SWITCH_DISPLAY_PLAYERS_NAMES = "SWITCH_DISPLAY_PLAYERS_NAMES";
	/** Display current engine speed */ 
	public final static String SWITCH_DISPLAY_SPEED = "SWITCH_DISPLAY_SPEED";
	/** Display/hide certain sprites */
	public final static String SWITCH_DISPLAY_SPRITES = "SWITCH_DISPLAY_SPRITES";
	/** Display sprite positions */
	public final static String SWITCH_DISPLAY_SPRITES_POSITIONS = "SWITCH_DISPLAY_SPRITES_POSITIONS";
	/** Display tiles positions */
	public final static String SWITCH_DISPLAY_TILES_POSITIONS = "SWITCH_DISPLAY_TILES_POSITIONS";
	/** Display current time */
	public final static String SWITCH_DISPLAY_TIME = "SWITCH_DISPLAY_TIME";
	/** Display CPU usage */
	public final static String SWITCH_DISPLAY_EFFECTIVE_USAGE = "SWITCH_DISPLAY_EFFECTIVE_USAGE";
	/** Display CPU usage */
	public final static String SWITCH_DISPLAY_REALTIME_USAGE = "SWITCH_DISPLAY_REALTIME_USAGE";
	/** Pause engine */
	public final static String SWITCH_ENGINE_PAUSE = "SWITCH_ENGINE_PAUSE";
	/** Replay: go fast forward */
	public final static String SWITCH_FAST_FORWARD = "SWITCH_FAST_FORWARD";
	
	/** Regular key use */
	public final static int REGULAR = 0;
	/** Modal key use */
	public final static int MODE = 1;

	/**
	 * Builds a new event
	 * with the specified name.
	 * 
	 * @param name
	 * 		Event name.
	 */
	public SystemControlEvent(String name)
	{	this.name = name;
		index = REGULAR;
	}
	
	/**
	 * Builds a new event
	 * with the specified name and index
	 * (some numeric parameter).
	 * 
	 * @param name
	 * 		Event name.
	 * @param index
	 * 		Event index.
	 */
	public SystemControlEvent(String name, int index)
	{	this.name = name;
		this.index = index;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of this event */
	private String name;

	/**
	 * Returns the name of this event.
	 * 
	 * @return
	 * 		Event name.
	 */
	public String getName()
	{	return name;	
	}
	
	/**
	 * Compares the specified string
	 * with the name of this event.
	 * 
	 * @param name
	 * 		Name to be compared with this event name.
	 * @return
	 * 		{@code true} iff the names are the same.
	 */
	public boolean hasName(String name)
	{	return this.name.equals(name);
	}

	/////////////////////////////////////////////////////////////////
	// INDEX				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Index (numerical parameter) of this event */
	private int index;
	
	/**
	 * Returns the index (numerical parameter)
	 * of this event.
	 * 
	 * @return
	 * 		Index of this event.
	 */
	public int getIndex()
	{	return index;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "SystemControlEvent("+name+":"+index;
		return result;
	}
}
