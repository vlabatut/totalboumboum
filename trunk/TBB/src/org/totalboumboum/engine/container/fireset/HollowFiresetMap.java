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
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.xml.sax.SAXException;

public class HollowFiresetMap implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(Fireset fireset: firesets.values())
			fireset.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

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
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public HollowFiresetMap deepCopy(double zoomFactor) throws IOException
	{	HollowFiresetMap result = new HollowFiresetMap();
	
		// firesets
		for(Entry<String,Fireset> entry: firesets.entrySet())
		{	String key = entry.getKey();
			Fireset fireset = entry.getValue().deepCopy(zoomFactor);
			result.addFireset(key,fireset);
		}
		
		return result;
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
}
