package org.totalboumboum.ai.v201213.adapter.data.internal;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * Les sprites sont forcément des blocs, des items ou
 * des bombes (pas de feu ni de personnages).
 * 
 * @author Vincent Labatut
 *
 */
public class AiDataSuddenDeathEvent extends AiSuddenDeathEvent
{	
	/**
	 * Creates a new event with the specified time
	 * and sprites.
	 * 
	 * @param zone
	 * 		Zone undergoing the event.
	 * @param time
	 * 		Time of this sudden death event.
	 * @param spriteMap
	 * 		Sprites destined to appear during this event.
	 */
	public AiDataSuddenDeathEvent(AiDataZone zone, long time, HashMap<Tile,List<Sprite>> spriteMap)
	{	// time
		this.time = time;
		
		// tiles
		for(Entry<Tile, List<Sprite>> entry: spriteMap.entrySet())
		{	// tile
			Tile t = entry.getKey();
			int col = t.getCol();
			int row = t.getRow();
			AiDataTile tile = zone.getTile(row,col);
			
			// sprites
			List<Sprite> sprites = entry.getValue();
			for(Sprite s: sprites)
			{	// block
				if(s instanceof Block)
				{	Block b = (Block) s;
					AiDataBlock block = new AiDataBlock(tile,b);
					this.sprites.add(block);
				}
				// bombs
				else if(s instanceof Bomb)
				{	Bomb b = (Bomb) s;
					AiDataBomb bomb = new AiDataBomb(tile,b);
					this.sprites.add(bomb);
				}
				else if(s instanceof Item)
				// item
				{	Item i = (Item) s;
					AiDataItem item = new AiDataItem(tile,i);
					this.sprites.add(item);
				}
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de sprites apparaissant à l'instant associé à cet évènement */
	private List<AiDataSprite<?>> sprites = new ArrayList<AiDataSprite<?>>();
	
	@Override
	public List<AiSprite> getSprites()
	{	List<AiSprite> result = new ArrayList<AiSprite>(sprites);
		return result;
	}
}
