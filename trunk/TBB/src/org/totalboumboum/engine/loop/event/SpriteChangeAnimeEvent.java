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

import org.totalboumboum.engine.content.sprite.Sprite;

public class SpriteChangeAnimeEvent extends SpriteChangeEvent
{	
	public SpriteChangeAnimeEvent(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** change in the gesture (the name is transmitted */
	public static final String SPRITE_EVENT_GESTURE = "SPRITE_EVENT_GESTURE";
	/** change in the animation direction */
	public static final String SPRITE_EVENT_DIRECTION = "SPRITE_EVENT_DIRECTION";
	/** forced duration */
	public static final String SPRITE_EVENT_DURATION = "SPRITE_EVENT_DURATION";
	/** animation must be reinitialized */
	public static final String SPRITE_EVENT_REINIT = "SPRITE_EVENT_REINIT";
}
