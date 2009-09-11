package fr.free.totalboumboum.engine.content.sprite.block;

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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class BlockFactory extends SpriteFactory<Block>
{	
	/////////////////////////////////////////////////////////////////
	// GESTURE PACK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final HashMap<GestureName,GestureName> animeReplacements = new HashMap<GestureName,GestureName>();		
	static
	{	// APPEARING
		// BOUNCING
		// BURNING
		animeReplacements.put(GestureName.BURNING,null);
		// CRYING
		// EXULTING
		// HIDING
		animeReplacements.put(GestureName.HIDING,GestureName.NONE);
		// JUMPING
		// LANDING
		// OSCILLATING
		// OSCILLATING_FAILING
		// PUNCHED
		// PUNCHING
		// PUSHING
		// RELEASED
		// SLIDING
		// SLIDING_FAILING
		// STANDING
		animeReplacements.put(GestureName.STANDING,null);
		// STANDING_FAILING
		// WAITING
		// WALKING		
	}
	
	public static HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Block makeSprite(Tile tile)
	{	// init
		Block result = new Block();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// event
		EventManager eventManager = new BlockEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.initSprite(tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
