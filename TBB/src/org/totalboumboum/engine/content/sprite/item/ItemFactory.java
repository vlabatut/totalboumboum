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
import java.util.List;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.bombset.EmptyBombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.control.EmptyControlManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.manager.event.EmptyEventManager;
import org.totalboumboum.engine.content.manager.item.EmptyItemManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.trajectory.EmptyTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.FullTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;
import org.totalboumboum.engine.content.sprite.item.ItemEventManager;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ItemFactory extends SpriteFactory<Item>
{	private static final long serialVersionUID = 1L;

	public ItemFactory(String itemName)
	{	this.itemName = itemName;
	}
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES / ITEMREFS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abilities given by this item */
	protected List<List<AbstractAbility>> itemAbilities;
	/** alternatively: existing bonuses given by this item */
	protected List<String> itemrefs;
	/** probability associated to the list of abilities **/
	protected List<Float> itemProbabilities;
	
	public void setItemAbilities(List<List<AbstractAbility>> itemAbilities, List<String> itemref, List<Float> itemProbabilities)
	{	this.itemAbilities = itemAbilities;
		this.itemrefs = itemref;
		this.itemProbabilities = itemProbabilities;
	}
	
	public List<AbstractAbility> drawItemAbilities()
	{	List<AbstractAbility> result = null;
		double proba = Math.random();
		//System.out.println("name: "+itemName+" proba: "+proba);		
		double total = 0;
		int index = 0;

		if(!itemrefs.isEmpty())
		{	String name = null;
			while(index<itemrefs.size() && name==null)
			{	total = total + itemProbabilities.get(index);
				if(proba<total)
					name = itemrefs.get(index);
				else
					index ++;
			}
//System.out.println("name: "+name);			
			result = instance.getItemset().getItemAbilities(name);
		}
		else if(!itemAbilities.isEmpty())
		{	while(index<itemAbilities.size() && result==null)
			{	total = total + itemProbabilities.get(index);
				if(proba<total)
					result = itemAbilities.get(index);
				else
					index ++;
			}
//for(AbstractAbility a: abilities)
//if(a instanceof StateAbility)
//System.out.println("\t- "+((StateAbility)a).getName()+", : "+a.getStrength());
		}
		else
		{	result = new ArrayList<AbstractAbility>();
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String itemName;
	
	public Item makeSprite(Tile tile)
	{	// init
		Item result = new Item();

		// common managers
		initSprite(result);
	
		// anime

		// trajectory
		TrajectoryManager trajectoryManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			trajectoryManager = new EmptyTrajectoryManager(result);
		else
			trajectoryManager = new FullTrajectoryManager(result);
		result.setTrajectoryManager(trajectoryManager);
		
		// bombset
		BombsetManager bombsetManager = new EmptyBombsetManager(result);
		bombsetManager.setBombset(bombset);
//if(bombset==null)
//	System.out.println();
		result.setBombsetManager(bombsetManager);
		
		// explosion
		
		// modulations
		
		// item
		ItemManager itemManager = new EmptyItemManager(result);
		result.setItemManager(itemManager);
		
		// ability
		
		// delay
		
		// control
		ControlManager controlManager = new EmptyControlManager(result);
		result.setControlManager(controlManager);

		// item abilities
		List<AbstractAbility> itemAbilities = drawItemAbilities();
		result.initItemAbilities(itemAbilities);
		
		// event
		EventManager eventManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			eventManager = new EmptyEventManager(result);
		else
			eventManager = new ItemEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.setItemName(itemName);
		result.initSprite(tile);		
		
		return result;
	}
}
