package fr.free.totalboumboum.engine.container.theme;

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

import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.block.BlockFactory;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactory;

public class Theme
{	
	public static final String DEFAULT_GROUP = "default";
	public static final String GROUP_SEPARATOR = ".";
	
	// components
	private HashMap<String,BlockFactory> blocks;
	private HashMap<String,FloorFactory> floors;
	/*
	 * folder devrait en fait correspondre au nom du thème
	 */
	public Theme(HashMap<String,BlockFactory> blocks, HashMap<String,FloorFactory> floors)
	{	this.blocks = blocks; 
		this.floors = floors; 
	}
	
	public Floor makeFloor()
	{	Entry<String,FloorFactory> entry = floors.entrySet().iterator().next();
		Floor result = entry.getValue().makeSprite();
		result.initGesture();
		return result;
	}
	public Floor makeFloor(String name)
	{	Floor result = floors.get(name).makeSprite();
		result.initGesture();
		return result;
	}
	
	public Block makeBlock(String name)
	{	Block result = blocks.get(name).makeSprite();
//NOTE dans ce type de méthode, il faut tester si le nom passé en paramètre a bien été trouvé !	
		result.initGesture();
		return result;
	}

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

