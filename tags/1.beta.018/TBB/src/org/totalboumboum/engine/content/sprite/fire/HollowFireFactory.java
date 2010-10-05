package org.totalboumboum.engine.content.sprite.fire;

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

import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactory;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowFireFactory extends HollowSpriteFactory<Fire>
{	private static final long serialVersionUID = 1L;

/*	public FireFactory(String name)
	{	this.name = name;
		
	}
*/	
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
		// RELEASED
		// SLIDING
		// SLIDING_FAILING
		// SPAWNING
		// STANDING
		// STANDING_FAILING
		// WAITING
		// WALKING		
	}
	
	public HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return HollowFireFactory.animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String firesetName;

	public void setFiresetName(String firesetName)
	{	this.firesetName = firesetName;	
	}
	
	public String getFiresetName()
	{	return firesetName;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * no need to copy sprite-specific info (item name, etc)
	 * since it's not defined in the sprite file, but in the set file.
	 * consequently, it should be initialized after the copy, depending
	 * on the content of the set file.
	 */
	public HollowFireFactory copy()
	{	HollowFireFactory result = new HollowFireFactory();
		
		// common stuff
		initCopy(result);
		
		return result;
	}

	public FireFactory fill(double zoomFactor) throws IOException
	{	FireFactory result = new FireFactory();
		
		// common stuff
		initFill(result,zoomFactor,null);
		
		// fireset
		result.setFiresetName(firesetName);

		return result;
	}
}
