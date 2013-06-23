package org.totalboumboum.engine.content.manager.explosion;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.engine.container.explosionset.Explosion;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ExplosionManager
{	
	public ExplosionManager(Sprite sprite)
	{	this.sprite = sprite;
		explosion = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	public Sprite getSprite()
	{	return sprite;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLAME RANGE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected int flameRange;
	
	public int getFlameRange()
	{	return flameRange;
	}
	
	public void setFlameRange(int flameRange)
	{	this.flameRange = flameRange;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Explosion explosion;
	
	public void setExplosion(Explosion explosion)
	{	this.explosion = explosion;	
	}
	
	public Explosion getExplosion()
	{	return explosion;	
	}
	
	public abstract long getExplosionDuration();
	
	/**
	 * create the explosion and returns the list of concerned tiles
	 * 
	 * @param fake	false if one just wants the tile list and not the actual explosion
	 * @return	the list of tiles to be put on fire
	 */
	public abstract List<Tile> makeExplosion(boolean fake);

	public abstract boolean isPenetrating();

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract ExplosionManager copy(Sprite sprite);
}
