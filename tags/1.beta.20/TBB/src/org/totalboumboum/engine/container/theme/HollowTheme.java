package org.totalboumboum.engine.container.theme;

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

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.content.sprite.block.BlockFactory;
import org.totalboumboum.engine.content.sprite.block.HollowBlockFactory;
import org.totalboumboum.engine.content.sprite.floor.FloorFactory;
import org.totalboumboum.engine.content.sprite.floor.HollowFloorFactory;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowTheme extends AbstractTheme implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// FLOORS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,HollowFloorFactory> floors = new HashMap<String, HollowFloorFactory>();

	public void addFloorFactory(String name, HollowFloorFactory floorFactory)
	{	floors.put(name,floorFactory);
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,HollowBlockFactory> blocks = new HashMap<String, HollowBlockFactory>();

	public void addBlockFactory(String name, HollowBlockFactory blocFactory)
	{	blocks.put(name,blocFactory);
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Theme fill(double zoomFactor) throws IOException
	{	Theme result = new Theme();
	
		// misc
		result.author = author;
		result.name = name;
		result.source = source;
		result.version = version;
		
		// blocks
		for(Entry<String,HollowBlockFactory> entry: blocks.entrySet())
		{	String key = entry.getKey();
			BlockFactory blocFactory = entry.getValue().fill(zoomFactor);
			result.addBlockFactory(key,blocFactory);
		}
		
		// floors
		for(Entry<String,HollowFloorFactory> entry: floors.entrySet())
		{	String key = entry.getKey();
			FloorFactory floorFactory = entry.getValue().fill(zoomFactor);
			result.addFloorFactory(key,floorFactory);
		}
		
		return result;
	}
}
