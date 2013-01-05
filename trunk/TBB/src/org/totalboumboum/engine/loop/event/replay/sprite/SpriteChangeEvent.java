package org.totalboumboum.engine.loop.event.replay.sprite;

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

import java.util.HashMap;

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SpriteChangeEvent extends SpriteEvent
{	private static final long serialVersionUID = 1L;

	protected SpriteChangeEvent(Sprite sprite)
	{	super(sprite);
		sendEvent = false;
	}

	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected HashMap<String,Object> changes = new HashMap<String,Object>();
	
	public HashMap<String,Object> getChanges()
	{	return changes;
	}

	public void setChange(String key, Object value)
	{	changes.put(key,value);
		sendEvent = true;
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
