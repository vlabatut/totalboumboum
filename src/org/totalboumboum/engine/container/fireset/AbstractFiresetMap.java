package org.totalboumboum.engine.container.fireset;

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

import java.util.HashMap;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractFiresetMap<T extends AbstractFireset>
{	
	/////////////////////////////////////////////////////////////////
	// FIRESETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final HashMap<String,T> firesets = new HashMap<String, T>();
	
	public void addFireset(String name, T fireset)
	{	firesets.put(name,fireset);		
	}
	
	public T getFireset(String name)
	{	return firesets.get(name);		
	}
}
