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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GestureName;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;
import org.totalboumboum.engine.content.sprite.item.ItemEventManager;


public class ItemFactory extends SpriteFactory<Item>
{	private static final long serialVersionUID = 1L;

	public ItemFactory(String itemName)
	{	this.itemName = itemName;
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
	
	public static HashMap<GestureName,GestureName> getAnimeReplacements()
	{	return animeReplacements;
	}

	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilities given by this item */
	private ArrayList<ArrayList<AbstractAbility>> itemAbilities;
	/** probability associated to the list of abilities **/
	private ArrayList<Float> itemProbabilities;
	
	public void setItemAbilities(ArrayList<ArrayList<AbstractAbility>> itemAbilities, ArrayList<Float> itemProbabilities)
	{	this.itemAbilities = itemAbilities;
		this.itemProbabilities = itemProbabilities;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String itemName;
	
	public Item makeSprite(Tile tile)
	{	// init
		Item result = new Item();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// item abilities
		double proba = Math.random();
//System.out.println("name: "+itemName+" proba: "+proba);		
		double total = 0;
		int index = 0;
		ArrayList<AbstractAbility> abilities = null;
		while(index<itemAbilities.size() && abilities==null)
		{	total = total + itemProbabilities.get(index);
			if(proba<total)
				abilities = itemAbilities.get(index);
			else
				index ++;
		}
//for(AbstractAbility a: abilities)
//if(a instanceof StateAbility)
//System.out.println("\t- "+((StateAbility)a).getName()+", : "+a.getStrength());
		result.initItemAbilities(abilities);
		// event
		EventManager eventManager = new ItemEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.setItemName(itemName);
		result.initSprite(tile);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			// item abilities
			{	for(ArrayList<AbstractAbility> list: itemAbilities)
				{	Iterator<AbstractAbility> it = list.iterator();
					while(it.hasNext())
					{	AbstractAbility temp = it.next();
						temp.finish();
						it.remove();
					}
				}
				itemAbilities.clear();
				itemProbabilities.clear();
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// CACHE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ItemFactory cacheCopy(double zoomFactor)
	{	ItemFactory result = new ItemFactory(itemName);
		
		// misc
		result.base = base;
		result.name = name;
		
		// item abilities
		ArrayList<ArrayList<AbstractAbility>> itemAbilitiesCopy = new ArrayList<ArrayList<AbstractAbility>>();
		for(ArrayList<AbstractAbility> abilityList: itemAbilities)
		{	ArrayList<AbstractAbility> temp = new ArrayList<AbstractAbility>();
			for(AbstractAbility ability: abilityList)
				temp.add(ability.cacheCopy(zoomFactor));
			itemAbilitiesCopy.add(temp);
		}
		result.itemAbilities = itemAbilitiesCopy;
		
		// item probabilities
		ArrayList<Float> itemProbabilitiesCopy = new ArrayList<Float>();
		for(Float proba: itemProbabilities)
			itemProbabilitiesCopy.add(proba);
		result.itemProbabilities = itemProbabilitiesCopy;

		// item name
		result.itemName = itemName;
		
		// abilities
		ArrayList<AbstractAbility> abilitiesCopy = new ArrayList<AbstractAbility>();
		for(AbstractAbility ability: abilities)
			abilitiesCopy.add(ability.cacheCopy(zoomFactor));
		result.setAbilities(abilities);
		
		// bombset
		result.setBombsetColor(bombsetColor);
		
		// explosion
		result.setExplosionName(explosionName);
		
		// gestures
		GesturePack gesturePackCopy = gesturePack.cacheCopy(zoomFactor);
		result.setGesturePack(gesturePackCopy);

		return result;
	}
}
