package org.totalboumboum.ai.v201314.adapter.model.full;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.Collections;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiSprite;
import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Représente un évènement de la mort subite,
 * composé d'un instant exprimé en ms et d'un ensemble
 * de sprites destinés à apparaître à cet instant.
 * Les sprites sont forcément des blocs, des items ou
 * des bombes (pas de feu ni de personnages).
 * 
 * @author Vincent Labatut
 */
public class AiSimSuddenDeathEvent extends AiSuddenDeathEvent
{	/** Id de la classe */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new event with the specified time
	 * and sprites.
	 * 
	 * @param time
	 * 		Time of this sudden death event.
	 * @param sprites
	 * 		Sprites destined to appear during this event.
	 */
	protected AiSimSuddenDeathEvent(long time, List<AiSimSprite> sprites)
	{	// time
		this.time = time;
		
		// sprites
		this.sprites.addAll(sprites);
		neutralSprites.addAll(sprites);
	}
	
	/**
	 * Creates a new event which is a copy of the specified one.
	 * 
	 * @param zone
	 * 		Zone undergoing the event.
	 * @param event
	 * 		The event to be copied.
	 */
	protected AiSimSuddenDeathEvent(AiSimZone zone, AiSuddenDeathEvent event)
	{	// time
		this.time = event.getTime();
		
		// sprites
		for(AiSprite s: event.getSprites())
		{	// tile
			AiTile t = s.getTile();
			int col = t.getCol();
			int row = t.getRow();
			AiSimTile tile = zone.getTile(row,col);
			
			// block
			if(s instanceof AiBlock)
			{	AiBlock b = (AiBlock) s;
				AiSimBlock block = new AiSimBlock(tile,b);
				this.sprites.add(block);
				this.neutralSprites.add(block);
			}
			// bombs
			else if(s instanceof AiBomb)
			{	AiBomb b = (AiBomb) s;
				AiSimBomb bomb = new AiSimBomb(tile,b);
				this.sprites.add(bomb);
				this.neutralSprites.add(bomb);
			}
			else if(s instanceof AiItem)
			// item
			{	AiItem i = (AiItem) s;
				AiSimItem item = new AiSimItem(tile,i);
				this.sprites.add(item);
				this.neutralSprites.add(item);
			}
		}	
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Liste de sprites apparaissant à l'instant associé à cet évènement */
	private final List<AiSimSprite> sprites = new ArrayList<AiSimSprite>();
	/** Liste de sprites apparaissant à l'instant associé à cet évènement */
	private final List<AiSprite> neutralSprites = new ArrayList<AiSprite>();
	/** Version immuable de la liste neutre de sprites */
	private final List<AiSprite> externalNeutralSprites = Collections.unmodifiableList(neutralSprites);
	
	@Override
	public List<AiSprite> getSprites()
	{	return externalNeutralSprites;
	}
	
	/**
	 * Renvoie la représentation interne des sprites
	 * de cet évènement.
	 * 
	 * @return
	 * 		Une liste de sprites en représentation interne.
	 */
	protected List<AiSimSprite> getInternalSprites()
	{	return sprites;
	}
}
