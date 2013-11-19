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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;

/**
 * General event concerning the evolution of a sprite.
 * 
 * @author Vincent Labatut
 */
public abstract class SpriteEvent extends ReplayEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new sprite-related event.
	 * 
	 * @param sprite
	 * 		The concerned sprite.
	 */
	protected SpriteEvent(Sprite sprite)
	{	super();
		spriteId = sprite.getId();
		Tile tile = sprite.getTile();
		spriteInfo = sprite.getName()+"@("+tile.getCol()+","+tile.getRow()+")";
	}
		
	/////////////////////////////////////////////////////////////////
	// ID					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Id of the concerned sprite */
	protected int spriteId;
	
	/**
	 * Returns the id of the concerned sprite.
	 * 
	 * @return
	 * 		Id of the concerned sprite
	 */
	public int getSpriteId()
	{	return spriteId;
	}

	/////////////////////////////////////////////////////////////////
	// DEBUG INFO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Debug info regarding the sprite */
	protected transient String spriteInfo = "N/A";
	
	/**
	 * Returns some debug info regarding the sprite.
	 * 
	 * @return
	 * 		Debug info regarding the sprite.
	 */
	public String getSpriteInfo()
	{	return spriteInfo;
	}
}
