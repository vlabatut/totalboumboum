package org.totalboumboum.engine.container.theme;

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
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.block.BlockFactory;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.floor.FloorFactory;
import org.xml.sax.SAXException;


public class Theme implements Serializable, CachableSpriteContainer
{	private static final long serialVersionUID = 1L;

	public static final String DEFAULT_GROUP = "default";
	public static final String GROUP_SEPARATOR = "-";
	public static final String PROPERTY_SEPARATOR = ":";

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String version;
	
	public String getVersion()
	{	return version;
	}
	
	public void setVersion(String version)
	{	this.version = version;
	}
	
	/////////////////////////////////////////////////////////////////
	// VERSION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;
	}
	
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String author;
	
	public String getAuthor()
	{	return author;
	}
	
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String source;
	
	public String getSource()
	{	return source;
	}
	
	public void setSource(String source)
	{	this.source = source;
	}
	
	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(BlockFactory blockFactory: blocks.values())
			blockFactory.setInstance(instance);
		for(FloorFactory floorFactory: floors.values())
			floorFactory.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// FLOORS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,FloorFactory> floors = new HashMap<String, FloorFactory>();

	public void addFloorFactory(String name, FloorFactory floorFactory)
	{	floors.put(name,floorFactory);
	}
	
	public Floor makeFloor(Tile tile)
	{	Entry<String,FloorFactory> entry = floors.entrySet().iterator().next();
		Floor result = entry.getValue().makeSprite(tile);
		//result.initGesture();
		return result;
	}
	
	public Floor makeFloor(String name, Tile tile)
	{	FloorFactory ff = floors.get(name);
if(ff==null)
	System.out.println(name);
		Floor result = ff.makeSprite(tile);
		//result.initGesture();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<String,BlockFactory> blocks = new HashMap<String, BlockFactory>();

	public void addBlockFactory(String name, BlockFactory blocFactory)
	{	blocks.put(name,blocFactory);
	}
	
	public Block makeBlock(String name, Tile tile)
	{	BlockFactory bf = blocks.get(name);
if(bf==null)
	System.out.println(name);
		Block result = bf.makeSprite(tile);
//NOTE dans ce type de méthode, il faut tester si le nom passé en paramètre a bien été trouvé !	
		//result.initGesture();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Theme deepCopy(double zoomFactor) throws IOException
	{	Theme result = new Theme();
	
		// misc
		result.author = author;
		result.name = name;
		result.source = source;
		result.version = version;
		
		// blocks
		for(Entry<String,BlockFactory> entry: blocks.entrySet())
		{	String key = entry.getKey();
			BlockFactory blocFactory = entry.getValue().deepCopy(zoomFactor);
			result.addBlockFactory(key,blocFactory);
		}
		
		// floors
		for(Entry<String,FloorFactory> entry: floors.entrySet())
		{	String key = entry.getKey();
			FloorFactory floorFactory = entry.getValue().deepCopy(zoomFactor);
			result.addFloorFactory(key,floorFactory);
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// floors
			{	Iterator<Entry<String,FloorFactory>> it = floors.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,FloorFactory> t = it.next();
					FloorFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
			// blocks
			{	Iterator<Entry<String,BlockFactory>> it = blocks.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,BlockFactory> t = it.next();
					BlockFactory temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
