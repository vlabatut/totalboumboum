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
import org.totalboumboum.game.round.RoundVariables;

public class SpriteChangePositionEvent extends SpriteChangeEvent
{	
	public SpriteChangePositionEvent(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// CHANGES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** change in the horizontal position */
	public static final String SPRITE_EVENT_POSITION_X = "SPRITE_EVENT_POSITION_X";
	/** change in the vertical position */
	public static final String SPRITE_EVENT_POSITION_Y = "SPRITE_EVENT_POSITION_Y";
	/** change in the height */
	public static final String SPRITE_EVENT_POSITION_Z = "SPRITE_EVENT_POSITION_Z";

	/////////////////////////////////////////////////////////////////
	// SEND EVENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean getSendEvent()
	{	return sendEvent && !RoundVariables.filterEvents;	
	}
}
