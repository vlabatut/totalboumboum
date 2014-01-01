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

/**
 * Represents the insertion of a sprite in the game.
 * 
 * @author Vincent Labatut
 */
public class SpriteInsertionEvent extends SpriteEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new SpriteInsertionEvent
	 * 
	 * @param sprite
	 * 		The sprite to be inserted.
	 */
	public SpriteInsertionEvent(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "SpriteInsertionEvent("+time+":"+spriteId+"): " + getSpriteInfo() + " ";
		return result;
	}
}
