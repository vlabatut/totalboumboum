package fr.free.totalboumboum.engine.content.sprite.item;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.gesture.GestureName;
import fr.free.totalboumboum.engine.content.feature.gesture.GesturePack;
import fr.free.totalboumboum.engine.content.manager.event.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;
import fr.free.totalboumboum.engine.content.sprite.item.ItemEventManager;

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
		result.itemAbilities = itemAbilities;
		result.itemProbabilities = itemProbabilities;
		
		// abilities
		result.setAbilities(abilities);
		
		// bombset
		Bombset bombsetCopy = bombset.cacheCopy();
		result.setBombset(bombsetCopy);
		
		// explosion
		if(explosion!=null)
		{	Explosion explosionCopy = explosion.cacheCopy();
			result.setExplosion(explosionCopy);
		}
		
		// gestures
		GesturePack gesturePackCopy = gesturePack.cacheCopy(zoomFactor);
		result.setGesturePack(gesturePackCopy);

		return result;
	}
}
