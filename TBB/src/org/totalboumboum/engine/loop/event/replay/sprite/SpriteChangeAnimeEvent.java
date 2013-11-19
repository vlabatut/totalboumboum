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

import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Reflects a change in the animation of a sprite.
 * 
 * @author Vincent Labatut
 */
public class SpriteChangeAnimeEvent extends SpriteChangeEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new animation-related sprite.
	 * 
	 * @param sprite
	 * 		The concerned sprite.
	 */
	public SpriteChangeAnimeEvent(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Change in the gesture (the name is transmitted */
	public static final String SPRITE_EVENT_GESTURE = "SPRITE_EVENT_GESTURE";
	/** Change in the animation direction */
	public static final String SPRITE_EVENT_DIRECTION = "SPRITE_EVENT_DIRECTION";
	/** Forced duration */
	public static final String SPRITE_EVENT_DURATION = "SPRITE_EVENT_DURATION";
	/** Animation must be reinitialized */
	public static final String SPRITE_EVENT_REINIT = "SPRITE_EVENT_REINIT";

	/////////////////////////////////////////////////////////////////
	// SEND EVENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public boolean getSendEvent()
	{	return sendEvent;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "SpriteChangeAnimeEvent("+time+":"+spriteId+"): " + getSpriteInfo() + " ";
		if(changes.containsKey(SPRITE_EVENT_GESTURE))
			result = result + "gesture=" + changes.get(SPRITE_EVENT_GESTURE) + " ";
		if(changes.containsKey(SPRITE_EVENT_DIRECTION))
			result = result + "direction=" + changes.get(SPRITE_EVENT_DIRECTION) + " ";
		if(changes.containsKey(SPRITE_EVENT_DURATION))
			result = result + "duration=" + changes.get(SPRITE_EVENT_DURATION) + " ";
		if(changes.containsKey(SPRITE_EVENT_REINIT))
			result = result + "reinit=" + changes.get(SPRITE_EVENT_REINIT) + " ";
		return result;
	}
}
