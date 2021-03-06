package org.totalboumboum.engine.content.manager.bombset;

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

import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * Shallow version of a bombset manager, used for replay (lighter process).
 * 
 * @author Vincent Labatut
 */
public class EmptyBombsetManager extends BombsetManager
{	
	/**
	 * Builds a new shallow bombset manager for the specified sprite.
	 * 
	 * @param sprite
	 * 		Sprite possessing the new bombset manager.
	 */
	public EmptyBombsetManager(Sprite sprite)
	{	super(sprite);
	}
	
	/////////////////////////////////////////////////////////////////
	// DROP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Bomb makeBomb()
	{	Bomb result = null;
		return result;
	}
	
	@Override
	public void dropBomb(SpecificDrop dropAction)
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// TRIGGER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void triggerBomb()
	{	
		// useless here
	}
	
	@Override
	public void triggerAllBombs()
	{	
		// useless here
	}	
	
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void update()
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public BombsetManager copy(Sprite sprite)
	{	BombsetManager result = new EmptyBombsetManager(sprite); 
		return result;
	}
}
