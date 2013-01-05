package org.totalboumboum.engine.content.sprite.bomb;

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

import java.io.IOException;
import java.util.HashMap;

import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactory;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowBombFactory extends HollowSpriteFactory<Bomb>
{	private static final long serialVersionUID = 1L;

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
		// FALLING
		animeReplacements.put(GestureName.FALLING,GestureName.STANDING);
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
		// RELEASED
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
	
	public HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return HollowBombFactory.animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String bombName;

	public String getBombName()
	{	return bombName;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * no need to copy sprite-specific info (item name, etc)
	 * since it's not defined in the sprite file, but in the set file.
	 * consequently, it should be initialized after the copy, depending
	 * on the content of the set file.
	 */
	public HollowBombFactory copy()
	{	HollowBombFactory result = new HollowBombFactory();
		
		initCopy(result);
		
		return result;
	}
	
	public BombFactory fill(double zoomFactor, PredefinedColor color) throws IOException
	{	BombFactory result = new BombFactory(bombName);
		
		initFill(result,zoomFactor,color);

		return result;
	}
}
