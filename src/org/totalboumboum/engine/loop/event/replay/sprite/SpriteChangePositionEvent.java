package org.totalboumboum.engine.loop.event.replay.sprite;

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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Reflects the fact a sprite moved.
 * 
 * @author Vincent Labatut
 */
public class SpriteChangePositionEvent extends SpriteChangeEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new move-related event.
	 * 
	 * @param sprite
	 * 		The concerned sprite.
	 */
	public SpriteChangePositionEvent(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Change in the horizontal position */
	public static final String SPRITE_EVENT_POSITION_X = "SPRITE_EVENT_POSITION_X";
	/** Change in the vertical position */
	public static final String SPRITE_EVENT_POSITION_Y = "SPRITE_EVENT_POSITION_Y";
	/** Change in the height */
	public static final String SPRITE_EVENT_POSITION_Z = "SPRITE_EVENT_POSITION_Z";

	/////////////////////////////////////////////////////////////////
	// SEND EVENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean getSendEvent()
	{	return sendEvent && !RoundVariables.getFilterEvents();	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "SpriteChangePositionEvent("+time+":"+spriteId+"): " + getSpriteInfo() + " ";
		if(changes.containsKey(SPRITE_EVENT_POSITION_X))
			result = result + "x=" + changes.get(SPRITE_EVENT_POSITION_X) + " ";
		if(changes.containsKey(SPRITE_EVENT_POSITION_Y))
			result = result + "y=" + changes.get(SPRITE_EVENT_POSITION_Y) + " ";
		if(changes.containsKey(SPRITE_EVENT_POSITION_Z))
			result = result + "z=" + changes.get(SPRITE_EVENT_POSITION_Z) + " ";
		return result;
	}
}
