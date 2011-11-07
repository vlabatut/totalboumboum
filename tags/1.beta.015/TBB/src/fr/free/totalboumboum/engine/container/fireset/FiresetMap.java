package fr.free.totalboumboum.engine.container.fireset;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Map.Entry;

public class FiresetMap
{
	/////////////////////////////////////////////////////////////////
	// FIRESETS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,Fireset> firesets = new HashMap<String, Fireset>();
	
	public void addFireset(String name, Fireset fireset)
	{	firesets.put(name,fireset);		
	}
	
	public Fireset getFireset(String name)
	{	return firesets.get(name);		
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	for(Entry<String,Fireset> e: firesets.entrySet())
				e.getValue().finish();
			firesets.clear();
		}
	}

	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public FiresetMap cacheCopy(double zoomFactor)
	{	FiresetMap result = new FiresetMap();
	
		// firesets
		for(Entry<String,Fireset> entry: firesets.entrySet())
		{	String key = entry.getKey();
			Fireset fireset = entry.getValue().cacheCopy(zoomFactor);
			result.addFireset(key,fireset);
		}
		
		return result;
	}
}