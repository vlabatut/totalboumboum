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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;

public class Fireset implements Serializable
{	private static final long serialVersionUID = 1L;

	public Fireset()
	{	fireFactories = new HashMap<String,FireFactory>();
	}
	
	/////////////////////////////////////////////////////////////////
	// FIRE FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,FireFactory> fireFactories;
	
	public void addFireFactory(String name, FireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
		fireFactory.setFireset(this);
	}
	
	public Fire makeFire(String name, Tile tile)
	{	Fire result = null;
		if(name==null)
			name = fireFactories.keySet().iterator().next();
		FireFactory fireFactory = fireFactories.get(name);
if(fireFactory==null)
	System.out.println(name);
		result = fireFactory.makeSprite(tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;	
	}
	
	public void setName(String name)
	{	this.name = name;	
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<Entry<String,FireFactory>> it = fireFactories.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,FireFactory> t = it.next();
					FireFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Fireset cacheCopy(double zoomFactor)
	{	Fireset result = new Fireset();
	
		// name
		result.name = name;
	
		// fires
		for(Entry<String,FireFactory> entry: fireFactories.entrySet())
		{	String key = entry.getKey();
			FireFactory fireFactory = entry.getValue().cacheCopy(zoomFactor,result);
			result.addFireFactory(key,fireFactory);
		}
		
		return result;
	}
}
