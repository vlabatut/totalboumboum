package fr.free.totalboumboum.engine.content.manager.item;

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
import java.util.Iterator;
import java.util.LinkedList;

import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.release.SpecificRelease;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.event.EngineEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.game.statistics.StatisticAction;
import fr.free.totalboumboum.game.statistics.StatisticEvent;

public class ItemManager
{	
	public ItemManager(Sprite sprite)
	{	this.sprite = sprite;
		collectedItems = new LinkedList<Item>();
		abilities = new ArrayList<AbstractAbility>();
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<AbstractAbility> abilities;
	
	public ArrayList<AbstractAbility> getItemAbilities()
	{	return abilities;
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<Item> initialItems = new ArrayList<Item>();

	public void addInitialItem(Item item)
	{	initialItems.add(item);
		addItem(item);
	}
	
	public void reinitInitialItems()
	{	for(Item item: initialItems)
		{	item.reinitItemAbilities();
			addItem(item);
		}		
	}
//TODO the item have to be recreated (tahnks to their name), or else it won't be possible to infect with initial items, and for instance to put a disease as an initial item
	
	/////////////////////////////////////////////////////////////////
	// INGAME ITEMS				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LinkedList<Item> collectedItems;
	
	public void addIngameItem(Item item)
	{	// add item
		addItem(item);	
		// stats (doesn't count initial items)
		StatisticAction statAction = StatisticAction.GATHER_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getFileName();
		String statTarget = item.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
		sprite.addStatisticEvent(statEvent);
	}
	
	private void addItem(Item item)
	{	// possibly remove the existing diseases
		StateAbility ability = item.modulateStateAbility(StateAbilityName.ITEM_CANCEL_GROUP);
		if(ability.isActive())
		{	float groupNumber = ability.getStrength();
			Iterator<Item> i = collectedItems.iterator();
			while(i.hasNext())
			{	Item temp = i.next();
				StateAbility ab = temp.modulateStateAbility(StateAbilityName.ITEM_GROUP);
				if(ab.isActive())
				{	float grpNbr = ab.getStrength();
					if(grpNbr==groupNumber)
					{	i.remove();
						EngineEvent event = new EngineEvent(EngineEvent.END_SPRITE);
						item.processEvent(event);
					}
				}
			}
		}
		
		// add the item to the list
		collectedItems.offer(item);
		ArrayList<AbstractAbility> ab = item.getItemAbilities();
		Iterator<AbstractAbility> i = ab.iterator();
		while(i.hasNext())
		{	AbstractAbility temp = i.next();
			if(temp instanceof ActionAbility)
			{	GeneralAction action = ((ActionAbility)temp).getAction();
				action.addActor(sprite.getRole());
			}
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// RELEASE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean releaseLastItem()
	{	boolean result = false;
		int index = collectedItems.size();
		while(index>=0 && !result)
		{	index--;
			Item item = collectedItems.get(index);
			result = releaseItem(item);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public boolean releaseRandomItem()
	{	boolean result = false;
		ArrayList<Item> tempList = (ArrayList<Item>)collectedItems.clone();
		while(!tempList.isEmpty() && !result)
		{	int index = (int)(Math.random()*tempList.size());
			Item item = tempList.get(index);
			result = releaseItem(item);
		}
		return result;
	}

	public boolean releaseItem(Item item)
	{	boolean result = false;
		if(!initialItems.contains(item))
		{	result = true;
			// remove the item from the list
			collectedItems.remove(item);
			
			StateAbility ability = item.modulateStateAbility(StateAbilityName.ITEM_ON_DEATH_ACTION);
			if(ability.isActive())
			{	// possibly reinit the item abilities
				if(ability.getStrength()==2)
					item.reinitItemAbilities();
				// release the item
				SpecificRelease releaseAction = new SpecificRelease(sprite,item);
				ActionEvent evt = new ActionEvent(releaseAction);
				item.processEvent(evt);
				//EngineEvent event = new EngineEvent(EngineEvent.HIDE_OVER);
				//item.processEvent(event);
			}
			else
			{	EngineEvent event = new EngineEvent(EngineEvent.END_SPRITE);
				item.processEvent(event);
			}
			
			// stats
			StatisticAction statAction = StatisticAction.LOSE_ITEM;
			long statTime = sprite.getLoopTime();
			String statActor = sprite.getPlayer().getFileName();
			String statTarget = item.getItemName();
			StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
			sprite.addStatisticEvent(statEvent);
		}
		return result;
	}
	
	public void releaseAllItems()
	{	int index = 0;
		while(index<collectedItems.size())
		{	Item item = collectedItems.get(index);
			boolean result = releaseItem(item);
			if(!result)
				index++;
		}
		collectedItems.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTAGION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void contagion(Sprite target)
	{	
		
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void update()
	{	abilities = new ArrayList<AbstractAbility>();
		// adding the items abilities
		Iterator<Item> i = collectedItems.iterator();
		while(i.hasNext())
		{	Item item = i.next();
			Iterator<AbstractAbility> j = item.getItemAbilities().iterator();
			while(j.hasNext())
			{	// if the ability is over, it's removed from the item's list
				AbstractAbility ab = j.next();
				if(ab.getTime()==0 || ab.getUses()==0)
					j.remove();
				else
					abilities.add(ab);
			}
			// if the item has no ability remaining, it's removed from the manager's list
			if(item.getItemAbilities().size()==0)
			{	i.remove();
				EngineEvent event = new EngineEvent(EngineEvent.END_SPRITE);
				item.processEvent(event);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// abilities
			{	Iterator<AbstractAbility> it = abilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// items
			{	Iterator<Item> it = collectedItems.iterator();
				while(it.hasNext())
				{	Item temp = it.next();
					temp.finish();
					it.remove();
				}
			}
			// misc
			sprite = null;
		}
	}
}
