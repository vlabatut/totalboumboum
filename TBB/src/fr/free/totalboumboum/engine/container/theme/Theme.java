package fr.free.totalboumboum.engine.container.theme;

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
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.block.BlockFactory;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactory;

public class Theme
{	
	public static final String DEFAULT_GROUP = "default";
	public static final String GROUP_SEPARATOR = ".";

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
	// FLOORS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,FloorFactory> floors;

	public void addFloorFactory(String name, FloorFactory floorFactory)
	{	floors.put(name,floorFactory);
	}
	
	public Floor makeFloor()
	{	Entry<String,FloorFactory> entry = floors.entrySet().iterator().next();
		Floor result = entry.getValue().makeSprite();
		result.initGesture();
		return result;
	}
	
	public Floor makeFloor(String name)
	{	FloorFactory ff = floors.get(name);
if(ff==null)
	System.out.println(name);
		Floor result = ff.makeSprite();
		result.initGesture();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCKS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,BlockFactory> blocks;

	public void addBlockFactory(String name, BlockFactory blocFactory)
	{	blocks.put(name,blocFactory);
	}
	
	public Block makeBlock(String name)
	{	BlockFactory bf = blocks.get(name);
if(bf==null)
	System.out.println(name);
		Block result = bf.makeSprite();
//NOTE dans ce type de m�thode, il faut tester si le nom pass� en param�tre a bien �t� trouv� !	
		result.initGesture();
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

