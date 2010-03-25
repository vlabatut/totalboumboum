package org.totalboumboum.engine.content.sprite;

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

import java.io.Serializable;
import java.util.Iterator;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;

public abstract class HollowSpriteFactory<T extends Sprite> extends AbstractSpriteFactory<T,HollowGesturePack> implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// BASE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String base;

	public String getBase()
	{	return base;
	}
	
	public void setBase(String base)
	{	this.base = base;
	}
	
	/////////////////////////////////////////////////////////////////
	// EXPLOSION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String explosionName;

	public String getExplosionName()
	{	return explosionName;
	}
	
	public void setExplosionName(String explosionName)
	{	this.explosionName = explosionName;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	//TODO here
	
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			
			// abilities
			{	Iterator<AbstractAbility> it = abilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			
			// gestures
			gesturePack.finish();
			gesturePack = null;
			
			// misc
			name = null;
		}
	}
}
