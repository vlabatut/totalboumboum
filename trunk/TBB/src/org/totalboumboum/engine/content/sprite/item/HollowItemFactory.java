package org.totalboumboum.engine.content.sprite.item;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactory;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowItemFactory extends HollowSpriteFactory<Item>
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
		animeReplacements.put(GestureName.RELEASED,GestureName.APPEARING);
		// SLIDING
		// SLIDING_FAILING
		// SPAWNING
		// STANDING
		animeReplacements.put(GestureName.STANDING,null);
		// STANDING_FAILING
		// WAITING
		// WALKING		
	}
	
	public HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return HollowItemFactory.animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES / ITEMREFS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilities given by this item */
	private List<List<AbstractAbility>> itemAbilities;
	/** alternatively: existing bonuses given by this item */
	private List<String> itemrefs;
	/** probabilities associated to the list of abilities **/
	private List<Float> itemProbabilities;
	
	public void setItemAbilities(List<List<AbstractAbility>> itemAbilities, List<String> itemref, List<Float> itemProbabilities)
	{	this.itemAbilities = itemAbilities;
		this.itemrefs = itemref;
		this.itemProbabilities = itemProbabilities;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String itemName;
	
	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * no need to copy sprite-specific info (item name, etc)
	 * since it's not defined in the sprite file, but in the set file.
	 * consequently, it should be initialized after the copy, depending
	 * on the content of the set file.
	 */
	public HollowItemFactory copy()
	{	HollowItemFactory result = new HollowItemFactory();
		
		initCopy(result);
		
		return result;
	}

	public ItemFactory fill(double zoomFactor) throws IOException
	{	ItemFactory result = new ItemFactory(itemName);
		
		// -- common stuff --
		initFill(result,zoomFactor,null);
	
		// --- item stuff ---
		// item abilities
		List<List<AbstractAbility>> itemAbilitiesCopy = new ArrayList<List<AbstractAbility>>();
		for(List<AbstractAbility> abilityList: itemAbilities)
		{	List<AbstractAbility> temp = new ArrayList<AbstractAbility>();
			for(AbstractAbility ability: abilityList)
				temp.add(ability.copy());
			itemAbilitiesCopy.add(temp);
		}
		result.itemAbilities = itemAbilitiesCopy;
		
		// item refs
		List<String> itemrefsCopy = new ArrayList<String>();
		for(String itemref: itemrefs)
			itemrefsCopy.add(itemref);
		result.itemrefs = itemrefsCopy;
		
		// item probabilities
		List<Float> itemProbabilitiesCopy = new ArrayList<Float>();
		for(Float proba: itemProbabilities)
			itemProbabilitiesCopy.add(proba);
		result.itemProbabilities = itemProbabilitiesCopy;

		return result;
	}
}
