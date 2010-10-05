package org.totalboumboum.engine.container.itemset;

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

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.content.sprite.item.HollowItemFactory;
import org.totalboumboum.engine.content.sprite.item.ItemFactory;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowItemset extends AbstractItemset implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	public HollowItemset()
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,HollowItemFactory> itemFactories = new HashMap<String, HollowItemFactory>();

	public void addItemFactory(String name, HollowItemFactory itemFactory)
	{	itemFactories.put(name,itemFactory);		
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Itemset fill(double zoomFactor) throws IOException
	{	Itemset result = new Itemset();
	
		// items
		for(Entry<String,HollowItemFactory> entry: itemFactories.entrySet())
		{	String key = entry.getKey();
			ItemFactory itemFactory = entry.getValue().fill(zoomFactor);
			result.addItemFactory(key,itemFactory);
		}
		
		return result;
	}

}
