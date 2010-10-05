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
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.block.BlockFactory;
import org.totalboumboum.engine.content.sprite.floor.Floor;
import org.totalboumboum.engine.content.sprite.floor.FloorFactory;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Theme extends AbstractTheme
{	
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
		
		// record/transmit event
		String name = entry.getKey();
		SpriteCreationEvent event = new SpriteCreationEvent(result,name);
		RoundVariables.writeEvent(event);
		
		//result.initGesture();
		return result;
	}
	
	public Floor makeFloor(String name, Tile tile)
	{	FloorFactory ff = floors.get(name);
if(ff==null)
	System.err.println("makeFloor: sprite '"+name+"' not found");
		Floor result = ff.makeSprite(tile);
		
		// record/transmit event
		SpriteCreationEvent event = new SpriteCreationEvent(result,name);
		RoundVariables.writeEvent(event);

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
	System.err.println("makeBlock: sprite '"+name+"' not found");
		Block result = bf.makeSprite(tile);
//NOTE dans ce type de méthode, il faut tester si le nom passé en paramètre a bien été trouvé !
		
		// record/transmit event
		SpriteCreationEvent event = new SpriteCreationEvent(result,name);
		RoundVariables.writeEvent(event);

		//result.initGesture();
		return result;
	}
}
