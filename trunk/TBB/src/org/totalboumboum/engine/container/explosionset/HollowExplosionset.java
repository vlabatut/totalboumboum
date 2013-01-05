package org.totalboumboum.engine.container.explosionset;

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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.totalboumboum.engine.container.CachableSpriteContainer;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowExplosionset extends AbstractExplosionset<HollowExplosion> implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	public HollowExplosionset()
	{	explosions = new HashMap<String,HollowExplosion>();
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used when generating actual Sprites from Hollow objects.
	 * Images names are replaced by the actual images, scalable stuff
	 * is scaled, etc.
	 */
	public Explosionset fill(double zoomFactor)
	{	Explosionset result = new Explosionset();
	
		for(Entry<String,HollowExplosion> entry: explosions.entrySet())
		{	String key = entry.getKey();
			HollowExplosion hollowExplosion = entry.getValue();
			Explosion explosion = hollowExplosion.fill(zoomFactor);
			result.addExplosion(key,explosion);	
		}
	
		return result;
	}
}
