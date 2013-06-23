package org.totalboumboum.engine.container.fireset;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map.Entry;

import org.totalboumboum.engine.content.sprite.fire.FireFactory;
import org.totalboumboum.engine.content.sprite.fire.HollowFireFactory;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowFireset extends AbstractFireset implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// FIRE FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,HollowFireFactory> fireFactories = new HashMap<String,HollowFireFactory>();
	
	public void addFireFactory(String name, HollowFireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
		fireFactory.setFiresetName(name);
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Fireset fill(double zoomFactor) throws IOException
	{	Fireset result = new Fireset();
	
		// name
		result.name = name;
	
		// fires
		for(Entry<String,HollowFireFactory> entry: fireFactories.entrySet())
		{	String key = entry.getKey();
			FireFactory fireFactory = entry.getValue().fill(zoomFactor);
			result.addFireFactory(key,fireFactory);
		}
		
		return result;
	}
}
