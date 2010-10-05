package org.totalboumboum.engine.content.manager.bombset;

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

import java.util.LinkedList;

import org.totalboumboum.engine.container.bombset.Bombset;
import org.totalboumboum.engine.content.feature.action.drop.SpecificDrop;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class BombsetManager
{	
	public BombsetManager(Sprite sprite)
	{	this.sprite = sprite;
		bombset = null;
		droppedBombs = new LinkedList<Bomb>();
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Bombset bombset;
	
	public void setBombset(Bombset bombset)
	{	this.bombset = bombset;		
	}
	
	public Bombset getBombset()
	{	return bombset;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// DROP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected LinkedList<Bomb> droppedBombs;
	
	public abstract Bomb makeBomb();
	
	public abstract void dropBomb(SpecificDrop dropAction);

	public LinkedList<Bomb> getDroppedBombs()
	{	return droppedBombs;
	}

	/////////////////////////////////////////////////////////////////
	// TRIGGER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void triggerBomb();
	
	public abstract void triggerAllBombs();
	
	/////////////////////////////////////////////////////////////////
	// UPDATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void update();

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract BombsetManager copy(Sprite sprite);
}
