package fr.free.totalboumboum.engine.content.sprite.floor;

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

import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class FloorFactory extends SpriteFactory<Floor>
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GESTURE PACK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final HashMap<GestureName,GestureName> animeReplacements = new HashMap<GestureName,GestureName>();		
	static
	{	// APPEARING
		// BOUNCING
		// BURNING
		// CRYING
		// EXULTING
		// HIDING
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
		// SPAWNING
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
	public Floor makeSprite(Tile tile)
	{	// init
		Floor result = new Floor();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// event
		EventManager eventManager = new FloorEventManager(result);
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

	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public FloorFactory cacheCopy(double zoomFactor)
	{	FloorFactory result = new FloorFactory();
		
		// misc
		result.base = base;
		result.name = name;
		
		// abilities
		result.setAbilities(abilities);
		
		// bombset
		Bombset bombsetCopy = bombset.cacheCopy();
		result.setBombset(bombsetCopy);
		
		// explosion
		Explosion explosionCopy = explosion.cacheCopy();
		result.setExplosion(explosionCopy);
		
		// gestures
		GesturePack gesturePackCopy = gesturePack.cacheCopy(zoomFactor);
		result.setGesturePack(gesturePackCopy);

		return result;
	}
}
