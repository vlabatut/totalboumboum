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

import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbility;
import fr.free.totalboumboum.engine.content.feature.ability.StateAbilityName;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.action.release.SpecificRelease;
import fr.free.totalboumboum.engine.content.feature.action.transmit.SpecificTransmit;
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
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Sprite sprite;
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
	
	public ArrayList<AbstractAbility> getItemAbilities()
	{	return abilities;
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final LinkedList<Item> originalInitialItems = new LinkedList<Item>();
	private final LinkedList<Item> initialItems = new LinkedList<Item>();

	public void addInitialItem(Item item)
	{	// record the initial item for future reinit
		originalInitialItems.add(item);
		// add the item to the list
		addItem(item,initialItems);
	}
	
	public void reinitInitialItems()
	{	// init
		Tile tile = sprite.getTile();
		Itemset itemset = GameVariables.level.getItemset();
		initialItems.clear();
		
		// recreate all initial items
		for(Item item: originalInitialItems)
		{	String name = item.getItemName();
			itemset.makeItem(name,tile);
			addItem(item,initialItems);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLECTED ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final LinkedList<Item> collectedItems = new LinkedList<Item>();
	
	public void collectItem(Item item)
	{	// possibly remove the existing diseases
		cancelItems(item,initialItems);
		cancelItems(item,collectedItems);
		
		// add the item to the list
		addItem(item,collectedItems);
		
		// stats
		StatisticAction statAction = StatisticAction.GATHER_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getFileName();
		String statTarget = item.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
		sprite.addStatisticEvent(statEvent);
	}
		
	private void addItem(Item item, LinkedList<Item> list)
	{	// add the item to the list
		list.offer(item);

		// update roles in abilities
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
	// RECEIVE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final LinkedList<Item> receivedItems = new LinkedList<Item>();

	public void receiveItem(Item item)
	{	// add the item to a special list, which will be merged with collectedItems on the next update
		receivedItems.add(item);		

		// stats
		StatisticAction statAction = StatisticAction.RECEIVE_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getFileName();
		String statTarget = item.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
		sprite.addStatisticEvent(statEvent);
}
	
	private void mergeReceivedItems()
	{	for(Item item: receivedItems)
			addItem(item,collectedItems);
		receivedItems.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// RELEASE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void releaseLastItem()
	{	boolean done = false;
		int index = 0;
		while(index<collectedItems.size() && !done)
		{	Item item = collectedItems.get(index);
			done = releaseItem(item);
			index++;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void releaseRandomItem()
	{	LinkedList<Item> list = (LinkedList<Item>)collectedItems.clone();
		boolean done = false;
		while(!!collectedItems.isEmpty() && !done)
		{	int index = (int)(Math.random()*collectedItems.size());
			Item item = collectedItems.get(index);
			done = releaseItem(item);
			list.remove(item);
		}
	}

	private boolean releaseItem(Item item)
	{	boolean result = false;
			
		StateAbility ability = item.modulateStateAbility(StateAbilityName.ITEM_ON_DEATH_ACTION);
		if(ability.isActive())
		{	// check if the hero can release the item
			SpecificRelease releaseAction = new SpecificRelease(sprite,item);
			ActionAbility ab = sprite.modulateAction(releaseAction);
			if(ab.isActive())
			{	int mode = (int)ability.getStrength();
				// possibly reinit the item abilities
				if(mode==StateAbilityName.ITEM_ON_DEATH_RELEASE_REINIT)
					item.reinitItemAbilities();
				// release the item
				ActionEvent evt = new ActionEvent(releaseAction);
				item.processEvent(evt);
				//EngineEvent event = new EngineEvent(EngineEvent.HIDE_OVER);
				//item.processEvent(event);
				result = true;
			}
		}
		else
		{	// the item is just lost
			EngineEvent event = new EngineEvent(EngineEvent.END_SPRITE);
			item.processEvent(event);
			result = true;
		}
		
		// only if the item could actually be released/lost
		if(result)
		{	// remove the item from the list
			collectedItems.remove(item);

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
	}

	/////////////////////////////////////////////////////////////////
	// CANCEL ITEM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void cancelItems(Item item, LinkedList<Item> list)
	{	// get the canceled group number
		StateAbility ability = item.modulateStateAbility(StateAbilityName.ITEM_CANCEL_GROUP);
		if(ability.isActive())
		{	float groupNumber = ability.getStrength();
			
			// look for items from the same group
			Iterator<Item> i = list.iterator();
			while(i.hasNext())
			{	Item temp = i.next();
				ability = temp.modulateStateAbility(StateAbilityName.ITEM_GROUP);
				if(ability.getStrength()==groupNumber)
				{	// get the canceling mode
					ability = temp.modulateStateAbility(StateAbilityName.ITEM_ON_CANCEL_ACTION);
					if(ability.isActive())
					{	boolean done = false;
						int mode = (int)ability.getStrength();
						
						// the item is just lost
						if(mode==StateAbilityName.ITEM_ON_CANCEL_DISAPEAR)
						{	// send an event
							EngineEvent event = new EngineEvent(EngineEvent.END_SPRITE);
							item.processEvent(event);
							done = true;
						}
						
						// the item is released
						else
						{	// check if the hero can release the item
							SpecificRelease releaseAction = new SpecificRelease(sprite,temp);
							ActionAbility ab = sprite.modulateAction(releaseAction);
							if(ab.isActive())
							{	// possibly reinit the item abilities
								if(ability.getStrength()==StateAbilityName.ITEM_ON_CANCEL_RELEASE_REINIT)
									temp.reinitItemAbilities();
								// release the item
								ActionEvent evt = new ActionEvent(releaseAction);
								temp.processEvent(evt);
								done = true;
							}
						}
						
						// only if the item could actually be released/lost
						if(done)
						{	// remove the item from the list
							i.remove();

							// stats
							StatisticAction statAction = StatisticAction.LOSE_ITEM;
							long statTime = sprite.getLoopTime();
							String statActor = sprite.getPlayer().getFileName();
							String statTarget = temp.getItemName();
							StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
							sprite.addStatisticEvent(statEvent);
						}
					}
				}
			}
		}
		
	}
	
	/////////////////////////////////////////////////////////////////
	// TRANSMIT ITEM	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void transmitAllItems(Sprite target)
	{	// processing the initial items
		transmitItems(target,initialItems);

		// processing the collected items
		transmitItems(target,collectedItems);
	}
	
	private void transmitItems(Sprite target, LinkedList<Item> list)
	{	Iterator<Item> i = list.iterator();
		while(i.hasNext())
		{	Item item = i.next();
			StateAbility ability = item.modulateStateAbility(StateAbilityName.ITEM_CONTAGION_MODE);
			if(ability.isActive())
			{	// possibly clone the item
				if(ability.getStrength()==StateAbilityName.ITEM_CONTAGION_SHARE_ONLY
					|| ability.getStrength()==StateAbilityName.ITEM_CONTAGION_SHARE_REINIT)
					item = item.copy();
				else if(ability.getStrength()==StateAbilityName.ITEM_CONTAGION_GIVE_ONLY
					|| ability.getStrength()==StateAbilityName.ITEM_CONTAGION_GIVE_REINIT)
					i.remove();
					
				// possibly reinit the item abilities
				if(ability.getStrength()==StateAbilityName.ITEM_CONTAGION_GIVE_REINIT
					|| ability.getStrength()==StateAbilityName.ITEM_CONTAGION_SHARE_REINIT)
					item.reinitItemAbilities();
				
				// transmit the item
				SpecificTransmit transmitAction = new SpecificTransmit(sprite,target,item);
				ActionEvent evt = new ActionEvent(transmitAction);
				target.processEvent(evt);

				// stats
				StatisticAction statAction = StatisticAction.TRANSMIT_ITEM;
				long statTime = sprite.getLoopTime();
				String statActor = sprite.getPlayer().getFileName();
				String statTarget = item.getItemName();
				StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
				sprite.addStatisticEvent(statEvent);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marker allowing to start updating items only when the round has actually started */
	private boolean start = false;
	
	public void start()
	{	start = true;	
	}
	
	public void update()
	{	if(start)
		{	// reinit abilities
			abilities.clear();
			
			// get the items received from other players
			mergeReceivedItems();
			
			// adding the initial items abilities
			updateAbilities(initialItems,true);
	
			// adding the collected items abilities
			updateAbilities(collectedItems,false);
		}
	}
	
	private void updateAbilities(LinkedList<Item> list, boolean keepItems)
	{	Iterator<Item> i = list.iterator();
		while(i.hasNext())
		{	Item item = i.next();
			Iterator<AbstractAbility> j = item.getItemAbilities().iterator();
			while(j.hasNext())
			{	// if the ability is over, it's removed from the item's list
				AbstractAbility ab = j.next();
				if(ab.getTime()==0 || ab.getUses()==0)
				{	if(!keepItems)
						j.remove();
				}
				else
					abilities.add(ab);
			}
			// if the item has no ability remaining, it's removed from the manager's list
			if(item.getItemAbilities().size()==0 && !keepItems)
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
			{	initialItems.clear();
				Iterator<Item> it = collectedItems.iterator();
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
