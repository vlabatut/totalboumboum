package org.totalboumboum.engine.loop.event.control;

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

public class ControlEvent
{	private static final long serialVersionUID = 1L;
	
	public final static String SWITCH_DISPLAY_AIS_PAUSE = "SWITCH_DISPLAY_AIS_PAUSE";
	public final static String SWITCH_DISPLAY_ENGINE_PAUSE = "SWITCH_DISPLAY_ENGINE_PAUSE";
	public final static String SWITCH_DISPLAY_FPS = "SWITCH_DISPLAY_FPS";
	public final static String SWITCH_DISPLAY_PLAYERS_NAMES = "SWITCH_DISPLAY_PLAYERS_NAMES";
	public final static String SWITCH_DISPLAY_SPEED = "SWITCH_DISPLAY_SPEED";
	public final static String SWITCH_DISPLAY_TIME = "SWITCH_DISPLAY_TIME";
	
	protected ControlEvent(String name)
	{	this.name = name;
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
}
