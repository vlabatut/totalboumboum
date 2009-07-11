package fr.free.totalboumboum.engine.content.sprite.fire;

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

import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class FireFactory extends SpriteFactory<Fire>
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
		// JUMPING
		// LANDING
		// OSCILLATING
		// OSCILLATING_FAILING
		// PUNCHED
		// PUNCHING
		// PUSHING
		// SLIDING
		// SLIDING_FAILING
		// SPAWNING
		// STANDING
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
	public Fire makeSprite()
	{	// init
		Fire result = new Fire();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// event
		EventManager eventManager = new FireEventManager(result);
		result.setEventManager(eventManager);
		// fireset name
		result.setFiresetName(fireset.getName());
		
		// result
//		result.initGesture();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Fireset fireset;

	public void setFireset(Fireset fireset)
	{	this.fireset = fireset;	
	}
	
	public Fireset getFireset()
	{	return fireset;			
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			fireset = null;
		}
	}
}
