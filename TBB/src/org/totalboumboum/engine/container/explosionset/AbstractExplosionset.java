package org.totalboumboum.engine.container.explosionset;

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

import java.util.HashMap;

public abstract class AbstractExplosionset<T extends AbstractExplosion>
{	
	/////////////////////////////////////////////////////////////////
	// EXPLOSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected HashMap<String,T> explosions;
	
	@SuppressWarnings("unused")
	private void setExplosions(HashMap<String,T> explosions)
	{	this.explosions = explosions;
	}
	
	public void addExplosion(String name, T explosion)
	{	explosions.put(name,explosion);
	}
	
	public T getExplosion(String name)
	{	T result = explosions.get(name);
		return result;
	}
}
