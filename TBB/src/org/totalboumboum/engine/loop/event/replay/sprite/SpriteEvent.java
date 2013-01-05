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
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SpriteEvent extends ReplayEvent
{	private static final long serialVersionUID = 1L;

	protected SpriteEvent(Sprite sprite)
	{	super();
		spriteId = sprite.getId();
		Tile tile = sprite.getTile();
		spriteInfo = sprite.getName()+"@("+tile.getCol()+","+tile.getRow()+")";
	}
		
	/////////////////////////////////////////////////////////////////
	// ID					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected int spriteId;
	
	public int getSpriteId()
	{	return spriteId;
	}

	/////////////////////////////////////////////////////////////////
	// DEBUG INFO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected transient String spriteInfo = "N/A";
	
	public String getSpriteInfo()
	{	return spriteInfo;
	}
}
