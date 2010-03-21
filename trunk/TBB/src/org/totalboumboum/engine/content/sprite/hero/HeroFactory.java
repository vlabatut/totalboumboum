package org.totalboumboum.engine.content.sprite.hero;

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

import java.util.ArrayList;
import java.util.HashMap;

import org.totalboumboum.engine.container.Cachable;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;

public class HeroFactory extends SpriteFactory<Hero> implements Cachable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GESTURE PACK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static final HashMap<GestureName,GestureName> animeReplacements = new HashMap<GestureName,GestureName>();		
	static
	{	// APPEARING
		// BOUNCING
		animeReplacements.put(GestureName.BOUNCING,GestureName.STANDING);
		// BURNING
		animeReplacements.put(GestureName.BURNING,null);
		// CRYING
		animeReplacements.put(GestureName.CRYING,GestureName.BURNING);
		// EXULTING
		animeReplacements.put(GestureName.EXULTING,GestureName.JUMPING);
		// HIDING
		// JUMPING
		animeReplacements.put(GestureName.JUMPING,GestureName.STANDING);
		// LANDING
		animeReplacements.put(GestureName.LANDING,GestureName.STANDING);
		// OSCILLATING
		// OSCILLATING_FAILING
		// PUNCHED
		// PUNCHING
		animeReplacements.put(GestureName.PUNCHING,GestureName.STANDING);
		// PUSHING
		animeReplacements.put(GestureName.PUSHING,GestureName.WALKING);
		// RELEASED
		// SLIDING
		// SLIDING_FAILING
		// SPAWNING
		// STANDING
		animeReplacements.put(GestureName.STANDING,null);
		// STANDING_FAILING
		// WAITING
		animeReplacements.put(GestureName.WAITING,GestureName.STANDING);
		// WALKING		
		animeReplacements.put(GestureName.WALKING,null);
	}
	
	public static HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Hero makeSprite(Tile tile)
	{	// init
		Hero result = new Hero();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// delay
//		double value = configuration.getHeroSetting(Configuration.HERO_SETTING_WAIT_DELAY);
//		result.addDelay(DelayManager.DL_WAIT,value);
		// event
		EventManager eventManager = new HeroEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.initSprite(tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public HeroFactory deepCopy(double zoomFactor, )
	{	HeroFactory result = new HeroFactory();
		
		// misc
		result.base = base;
		result.name = name;
		
		// abilities
		ArrayList<AbstractAbility> abilitiesCopy = new ArrayList<AbstractAbility>();
		for(AbstractAbility ability: abilities)
			abilitiesCopy.add(ability.cacheCopy(zoomFactor));
		result.setAbilities(abilitiesCopy);
		
		// bombset
		result.setBombsetColor(bombsetColor);
		
		// explosion
		result.setExplosionName(explosionName);
		
		// gestures
		GesturePack gesturePackCopy = gesturePack.cacheCopy(zoomFactor);
		result.setGesturePack(gesturePackCopy);

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
