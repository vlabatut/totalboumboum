package fr.free.totalboumboum.engine.container.fireset;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;

public class Fireset
{	private HashMap<String,FireFactory> fireFactories;
	private String name;
	
	public Fireset()
	{	fireFactories = new HashMap<String,FireFactory>();
	}
	
	public void addFireFactory(String name, FireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
		fireFactory.setFireset(this);
	}
	
	public Fire makeFire(String name)
	{	Fire result = null;
		FireFactory fireFactory = fireFactories.get(name);
		result = fireFactory.makeSprite();
		return result;
	}

	public String getName()
	{	return name;	
	}
	
	public void setName(String name)
	{	this.name = name;	
	}
	
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
}
