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

import java.io.IOException;
import java.util.HashMap;

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactory;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowHeroFactory extends HollowSpriteFactory<Hero> implements CachableSpriteContainer
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
	
	public HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return HollowHeroFactory.animeReplacements;
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
	public HollowHeroFactory copy()
	{	HollowHeroFactory result = new HollowHeroFactory();
		
		initCopy(result);
		
		return result;
	}

	public HeroFactory fill(double zoomFactor, PredefinedColor color) throws IOException
	{	HeroFactory result = new HeroFactory();
		
		initFill(result,zoomFactor,color);

		return result;
	}
}
