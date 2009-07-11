package fr.free.totalboumboum.engine.content.sprite.bomb;

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

import java.io.Serializable;
import java.util.HashMap;

import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class BombFactory extends SpriteFactory<Bomb> implements Serializable
{	private static final long serialVersionUID = 1L;

	public BombFactory(String bombName)
	{	this.bombName = bombName;
	}	
	
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
		// JUMPING
		// LANDING
		animeReplacements.put(GestureName.LANDING,GestureName.STANDING);
		// OSCILLATING
		animeReplacements.put(GestureName.OSCILLATING,GestureName.STANDING);
		// OSCILLATING_FAILING
		animeReplacements.put(GestureName.OSCILLATING_FAILING,GestureName.STANDING_FAILING);
		// PUNCHED
		animeReplacements.put(GestureName.PUNCHED,GestureName.STANDING);
		// PUNCHING
		// PUSHING
		// SLIDING
		animeReplacements.put(GestureName.SLIDING,GestureName.STANDING);
		// SLIDING_FAILING
		animeReplacements.put(GestureName.SLIDING_FAILING,GestureName.STANDING_FAILING);
		// SPAWNING
		// STANDING
		animeReplacements.put(GestureName.STANDING,null);
		// STANDING_FAILING
		animeReplacements.put(GestureName.STANDING_FAILING,GestureName.STANDING);
		// WAITING
		// WALKING		
	}
	
	public static HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String bombName;

	public Bomb makeSprite()
	{	// init
		Bomb result = new Bomb();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// delay
//		value = configuration.getBombSetting(Configuration.BOMB_SETTING_LIFETIME);
//		result.addDelay(DelayManager.DL_EXPLOSION,value);
		// event
		EventManager eventManager = new BombEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		result.setBombName(bombName);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public BombFactory copy()
	{	BombFactory result = new BombFactory(bombName);
		result.setAbilities(abilities);
		result.setBombset(null); //TODO to be completed later (?)
		result.setExplosion(explosion);
		result.setGesturePack(gesturePack.copy());
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
