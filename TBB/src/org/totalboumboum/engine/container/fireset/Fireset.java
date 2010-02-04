package org.totalboumboum.engine.container.fireset;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.fire.FireFactory;
import org.xml.sax.SAXException;

public class Fireset
{	
	public Fireset()
	{	fireFactories = new HashMap<String,FireFactory>();
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(FireFactory fireFactory: fireFactories.values())
			fireFactory.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRE FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,FireFactory> fireFactories;
	
	public void addFireFactory(String name, FireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
		fireFactory.setFiresetName(name);
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
	public long getMemSize()
	{	long result = 0;
		
		// fires
		for(FireFactory ff: fireFactories.values())
			result = result + ff.getMemSize();
		
		return result;
	}

/*	public Fireset cacheCopy(double zoomFactor)
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
	}*/
}
