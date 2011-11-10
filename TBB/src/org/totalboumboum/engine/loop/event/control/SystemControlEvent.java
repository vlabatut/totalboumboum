package org.totalboumboum.engine.loop.event.control;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
public class SystemControlEvent
{	private static final long serialVersionUID = 1L;
	
	public final static String REQUIRE_CANCEL_ROUND = "REQUIRE_CANCEL_ROUND";
	public final static String REQUIRE_ENGINE_STEP = "REQUIRE_ENGINE_STEP";
	public final static String REQUIRE_NEXT_MESSAGE = "REQUIRE_NEXT_MESSAGE";
	public final static String REQUIRE_SLOW_DOWN = "REQUIRE_SLOW_DOWN";
	public final static String REQUIRE_SPEED_UP = "REQUIRE_SPEED_UP";
	public final static String SWITCH_AIS_PAUSE = "SWITCH_AIS_PAUSE";
	public final static String SWITCH_BACKWARD = "SWITCH_BACKWARD";
	public final static String SWITCH_DISPLAY_AIS_COLORS = "SWITCH_DISPLAY_AIS_COLORS";
	public final static String SWITCH_DISPLAY_AIS_PATHS = "SWITCH_DISPLAY_AIS_PATHS";
	public final static String SWITCH_DISPLAY_AIS_TEXTS = "SWITCH_DISPLAY_AIS_TEXTS";
	public final static String SWITCH_DISPLAY_FPS = "SWITCH_DISPLAY_FPS";
	public final static String SWITCH_DISPLAY_GRID = "SWITCH_DISPLAY_GRID";
	public final static String SWITCH_DISPLAY_PLAYERS_NAMES = "SWITCH_DISPLAY_PLAYERS_NAMES";
	public final static String SWITCH_DISPLAY_SPEED = "SWITCH_DISPLAY_SPEED";
	public final static String SWITCH_DISPLAY_SPRITES_POSITIONS = "SWITCH_DISPLAY_SPRITES_POSITIONS";
	public final static String SWITCH_DISPLAY_TILES_POSITIONS = "SWITCH_DISPLAY_TILES_POSITIONS";
	public final static String SWITCH_DISPLAY_TIME = "SWITCH_DISPLAY_TIME";
	public final static String SWITCH_DISPLAY_EFFECTIVE_USAGE = "SWITCH_DISPLAY_EFFECTIVE_USAGE";
	public final static String SWITCH_DISPLAY_REALTIME_USAGE = "SWITCH_DISPLAY_REALTIME_USAGE";
	public final static String SWITCH_ENGINE_PAUSE = "SWITCH_ENGINE_PAUSE";
	public final static String SWITCH_FAST_FORWARD = "SWITCH_FAST_FORWARD";
	
	public final static int REGULAR = 0;
	public final static int MODE = 1;

	public SystemControlEvent(String name)
	{	this.name = name;
	}
	
	public SystemControlEvent(String name, int index)
	{	this.name = name;
		this.index = index;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;

	public String getName()
	{	return name;	
	}
	
	public boolean hasName(String name)
	{	return this.name.equals(name);
	}

	/////////////////////////////////////////////////////////////////
	// INDEX				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int index;
	
	public int getIndex()
	{	return index;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "SystemControlEvent("+name+":"+index;
		return result;
	}
}
