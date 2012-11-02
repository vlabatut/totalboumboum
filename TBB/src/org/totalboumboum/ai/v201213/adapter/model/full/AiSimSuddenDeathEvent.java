package org.totalboumboum.ai.v201213.adapter.model.full;

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
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.data.AiSprite;
import org.totalboumboum.ai.v201213.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataBlock;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataBomb;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataItem;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataSprite;
import org.totalboumboum.ai.v201213.adapter.data.internal.AiDataTile;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.block.Block;
import org.totalboumboum.engine.content.sprite.bomb.Bomb;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * 
 * @author Vincent Labatut
 *
 */
public class AiSimSuddenDeathEvent extends AiSuddenDeathEvent
{	
	/**
	 * Creates a new event with the specified time
	 * and sprites.
	 * 
	 * @param matrix
	 * 		Matrix of tiles composing the zone.
	 * @param time
	 * 		Time of this sudden death event.
	 * @param sprites
	 * 		Sprites destined to appear during this event.
	 */
	public AiSimSuddenDeathEvent(AiDataTile[][] matrix, long time, List<Sprite> sprites)
	{	// time
		this.time = time;
		
		// sprites
		List<AiDataSprite<?>> aiSprites = new ArrayList<AiDataSprite<?>>();
		for(Sprite s: sprites)
		{	// tile
			Tile t = s.getTile();
			int col = t.getCol();
			int row = t.getRow();
			AiDataTile tile = matrix[row][col];
			
			// block
			if(s instanceof Block)
			{	Block b = (Block) s;
				AiDataBlock block = new AiDataBlock(tile,b);
				aiSprites.add(block);
			}
			// bombs
			else if(s instanceof Block)
			{	Bomb b = (Bomb) s;
				AiDataBomb bomb = new AiDataBomb(tile,b);
				aiSprites.add(bomb);
			}
			else if(s instanceof Item)
			// item
			{	Item i = (Item) s;
				AiDataItem item = new AiDataItem(tile,i);
				aiSprites.add(item);
			}
		}	
		this.sprites.addAll(aiSprites);
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de sprites apparaissant à l'instant associé à cet évènement */
	private List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
	
	@Override
	public List<AiSprite> getSprites()
	{	List<AiSprite> result = new ArrayList<AiSprite>(sprites);
		return result;
	}
}
