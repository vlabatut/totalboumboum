package org.totalboumboum.engine.content.manager.item;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.feature.action.GeneralAction;
import org.totalboumboum.engine.content.feature.action.release.SpecificRelease;
import org.totalboumboum.engine.content.feature.action.transmit.SpecificTransmit;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.feature.event.EngineEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.item.Item;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.statistics.detailed.StatisticAction;
import org.totalboumboum.statistics.detailed.StatisticEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FullItemManager extends ItemManager
{	
	public FullItemManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void addInitialItem(Item item)
	{	// record the initial item for future reinit
		originalInitialItems.add(item);
		// add the item to the list
		addItem(item,initialItems);
	}
	
	@Override
	public void reinitInitialItems()
	{	// init
		Tile tile = sprite.getTile();
		Itemset itemset = RoundVariables.instance.getItemset();
		initialItems.clear();
		
		// recreate all initial items
		for(Item item: originalInitialItems)
		{	String name = item.getItemName();
			Item itm = itemset.makeItem(name,tile);
			addItem(itm,initialItems);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLECTED ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void collectItem(Item item)
	{	// possibly remove the existing diseases
		cancelItems(item,initialItems);
		cancelItems(item,collectedItems);
		
		// add the item to the list
		addItem(item,collectedItems);
		
		// stats
		StatisticAction statAction = StatisticAction.GATHER_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getId();
		//String statTarget = item.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
		sprite.addStatisticEvent(statEvent);
	}
		
	private void addItem(Item item, LinkedList<Item> list)
	{	// add the item to the list
		list.offer(item);

		// update roles in abilities
		List<AbstractAbility> ab = item.getItemAbilities();
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
	@Override
	public void receiveItem(Item item)
	{	// add the item to a special list, which will be merged with collectedItems on the next update
		receivedItems.add(item);		

		// stats
		StatisticAction statAction = StatisticAction.RECEIVE_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getId();
		//String statTarget = item.getItemName(); //NOTE target should be who did give this item
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
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
	@Override
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
	@Override
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
			String statActor = sprite.getPlayer().getId();
			//String statTarget = item.getItemName();
			StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
			sprite.addStatisticEvent(statEvent);
		}
		
		return result;
	}
	
	@Override
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
							String statActor = sprite.getPlayer().getId();
							//String statTarget = temp.getItemName();
							StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
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
	@Override
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
				String statActor = sprite.getPlayer().getId();
				//String statTarget = item.getItemName(); //NOTE target should be who gave the item
				StatisticEvent statEvent = new StatisticEvent(statActor,statAction,null,statTime);
				sprite.addStatisticEvent(statEvent);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
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
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ItemManager copy(Sprite sprite)
	{	ItemManager result = new FullItemManager(sprite);
		return result;
	}
}
