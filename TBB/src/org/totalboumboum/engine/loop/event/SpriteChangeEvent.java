package org.totalboumboum.engine.loop.event;

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

import java.util.HashMap;

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.gesture.GestureName;

public class SpriteChangeEvent extends ReplayEvent
{
	
	//TODO utiliser directement le sprite, comme pour l'autre evt
	
	public SpriteChangeEvent(int spriteId, long time, Integer x, Integer y, GestureName gesture, Direction direction)
	{	this.time = time;
		changes.put(SPRITE_EVENT_POSITION_X,x);
		changes.put(SPRITE_EVENT_POSITION_Y,y);
		changes.put(SPRITE_EVENT_GESTURE,gesture);
		changes.put(SPRITE_EVENT_DIRECTION,direction);
	}

	public SpriteChangeEvent(int spriteId, long time, GestureName gesture, Direction direction)
	{	this.time = time;
		changes.put(SPRITE_EVENT_GESTURE,gesture);
		changes.put(SPRITE_EVENT_DIRECTION,direction);
	}

	public SpriteChangeEvent(int spriteId, long time, GestureName gesture)
	{	this.time = time;
		changes.put(SPRITE_EVENT_GESTURE,gesture);
	}

	public SpriteChangeEvent(int spriteId, long time, Direction direction)
	{	this.time = time;
		changes.put(SPRITE_EVENT_DIRECTION,direction);
	}

	public SpriteChangeEvent(int spriteId, long time, Integer x, Integer y)
	{	this.time = time;
		changes.put(SPRITE_EVENT_POSITION_X,x);
		changes.put(SPRITE_EVENT_POSITION_Y,y);
	}

	/////////////////////////////////////////////////////////////////
	// ID					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int spriteId;
	
	public int getSpriteId()
	{	return spriteId;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long time;
	
	public long getTime()
	{	return time;
	}
	
	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final String SPRITE_EVENT_POSITION_X = "SPRITE_EVENT_POSITION_X";
	public static final String SPRITE_EVENT_POSITION_Y = "SPRITE_EVENT_POSITION_Y";
	public static final String SPRITE_EVENT_POSITION_Z = "SPRITE_EVENT_POSITION_Z";
	public static final String SPRITE_EVENT_GESTURE = "SPRITE_EVENT_GESTURE";
	public static final String SPRITE_EVENT_DIRECTION = "SPRITE_EVENT_DIRECTION";
	public static final String SPRITE_EVENT_DURATION = "SPRITE_EVENT_DURATION";
	private HashMap<String,Object> changes = new HashMap<String,Object>();
	
	public HashMap<String,Object> getChanges()
	{	return changes;
	}

/*
	/////////////////////////////////////////////////////////////////
	// POSITION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Integer x;
	private Integer y;
	
	public Integer getX()
	{	return x;
	}

	public Integer getY()
	{	return y;
	}

	/////////////////////////////////////////////////////////////////
	// GESTURE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GestureName gesture;

	public GestureName getGesture()
	{	return gesture;
	}

	/////////////////////////////////////////////////////////////////
	// DIRECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Direction direction;

	public Direction getDirection()
	{	return direction;
	}
*/
}
