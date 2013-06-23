package org.totalboumboum.engine.content.sprite;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class HollowSpriteFactory<T extends Sprite> extends AbstractSpriteFactory<T,HollowGesturePack> implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// BASE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String base;

	public String getBase()
	{	return base;
	}
	
	public void setBase(String base)
	{	this.base = base;
	}
	
	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * used to clone an abstract HollowFactory to be completed
	 * by additional data. All the content is keeped as is (same objects)
	 * but the containers are cloned, since their own content may be changed
	 * through inheritance.
	 */
	public void initCopy(HollowSpriteFactory<T> result)
	{	// gesture pack
		HollowGesturePack gesturePackCopy = gesturePack.copy();
		result.setGesturePack(gesturePackCopy);
		
		// abilities
		List<AbstractAbility> abilitiesCopy = new ArrayList<AbstractAbility>(abilities);
		result.setAbilities(abilitiesCopy);
		
		// explosions
		result.setExplosionName(explosionName);
		
		// misc
		result.setName(name);
	}
	
	/**
	 * used when generating an actual Factory from a HollowFactory.
	 * Images names are replaced by the actual images, scalable stuff
	 * is scaled, etc.
	 * @throws IOException 
	 */
	public void initFill(SpriteFactory<T> result, double zoomFactor, PredefinedColor color) throws IOException
	{	// gesture pack: deep copy (zoom, color...)
		GesturePack gesturePackCopy = gesturePack.fill(zoomFactor,color);
		result.setGesturePack(gesturePackCopy);
		
		// abilities: the same (must be copied only when making sprites)
		List<AbstractAbility> abilitiesCopy = new ArrayList<AbstractAbility>(abilities);
		result.setAbilities(abilitiesCopy);
		
		// explosions: name only
		result.setExplosionName(explosionName);
		
		// misc
		result.setName(name);
		result.setColor(color);
	}
}
