package fr.free.totalboumboum.engine.container.itemset;

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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.content.sprite.item.ItemFactory;


public class Itemset
{	// components
	private HashMap<String,ItemFactory> itemFactories;

	public Itemset(HashMap<String,ItemFactory> itemFactories) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.itemFactories = itemFactories;
	}
	
	public Item makeItem(String name)
	{	Item result = null;
		ItemFactory itemFactory = itemFactories.get(name);
if(itemFactory==null)
	System.out.println(name);
		result = itemFactory.makeSprite();
		return result;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// factories
			{	Iterator<Entry<String,ItemFactory>> it = itemFactories.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,ItemFactory> t = it.next();
					ItemFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}

