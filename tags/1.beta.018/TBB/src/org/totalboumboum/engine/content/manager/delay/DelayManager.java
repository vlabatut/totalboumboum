package org.totalboumboum.engine.content.manager.delay;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class DelayManager
{	
	public static final String DL_DEFEAT = "DL_DEFEAT";
	public static final String DL_ENTER = "DL_ENTER";
	public static final String DL_EXPLOSION = "DL_EXPLOSION";
	public static final String DL_LATENCY = "DL_LATENCY";
	public static final String DL_OSCILLATION = "DL_OSCILLATION";
	public static final String DL_REBIRTH = "DL_REBIRTH";
	public static final String DL_RELEASE = "DL_RELEASE";
	public static final String DL_SPAWN = "DL_SPAWN";
	public static final String DL_START = "DL_START";
	public static final String DL_VICTORY = "DL_VICTORY";
	public static final String DL_WAIT = "DL_WAIT";

	public DelayManager(Sprite sprite)
	{	this.sprite = sprite;
		delays = new HashMap<String,Double>();
		addedDelays = new HashMap<String,Double>();
		removedDelays = new ArrayList<String>();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;

	/////////////////////////////////////////////////////////////////
	// DELAYS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected HashMap<String,Double> delays;
	protected HashMap<String,Double> addedDelays;
	protected List<String> removedDelays;

	public abstract void addDelay(String name, double duration);
	
	public abstract void addIterDelay(String name, int iterations);
	
	public abstract void removeDelay(String name);
	
	/**
	 * returns the current delay associated to the name parameter,
	 * or a negative value if no delay is associated to the name. 
	 * @param name
	 * @return	a double corresponding to a delay
	 */
	public abstract double getDelay(String name);
	
	public abstract boolean hasDelay(String name);

	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void update();

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract DelayManager copy(Sprite sprite);
}
