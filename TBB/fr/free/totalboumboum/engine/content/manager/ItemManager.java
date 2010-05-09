package fr.free.totalboumboum.engine.content.manager;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.statistics.StatisticAction;
import fr.free.totalboumboum.data.statistics.StatisticEvent;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.action.GeneralAction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.content.sprite.item.Item;


public class ItemManager
{	private Sprite sprite;
	private LinkedList<Item> collectedItems;
	private ArrayList<AbstractAbility> abilities;
	
	public ItemManager(Sprite sprite)
	{	this.sprite = sprite;
		configuration = sprite.getConfiguration();
		collectedItems = new LinkedList<Item>();
		abilities = new ArrayList<AbstractAbility>();
	}

    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;		
	}
	
	public ArrayList<AbstractAbility> getItemAbilities()
	{	return abilities;
	}
	
	public void update()
	{	abilities = new ArrayList<AbstractAbility>();
		// adding the items' abilities
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
				item.endSprite();
			}
		}
	}
	
	/* 
	 * ********************************************
	 * ITEMS
	 * ********************************************
	 */
	
	public void addItem(Item item)
	{	collectedItems.offer(item);
		item.setToBeRemovedFromTile(true);
		ArrayList<AbstractAbility> ab = item.getItemAbilities();
		Iterator<AbstractAbility> i = ab.iterator();
		while(i.hasNext())
		{	AbstractAbility temp = i.next();
			if(temp instanceof ActionAbility)
			{	GeneralAction action = ((ActionAbility)temp).getAction();
				action.setActor(sprite.getClass());
			}
		}
		// stats
		StatisticAction statAction = StatisticAction.GATHER_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getName();
		String statTarget = item.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
		sprite.addStatisticEvent(statEvent);
	}
	
	public Item dropItem()
	{	return dropItem(collectedItems.size()-1);
	}
	public Item dropItem(int index)
	{	Item result = null;
		if(index<collectedItems.size())
		{	result = collectedItems.get(index);
			collectedItems.remove(index);
		}
		// stats
		StatisticAction statAction = StatisticAction.LOSE_ITEM;
		long statTime = sprite.getLoopTime();
		String statActor = sprite.getPlayer().getName();
		String statTarget = result.getItemName();
		StatisticEvent statEvent = new StatisticEvent(statActor,statAction,statTarget,statTime);
		sprite.addStatisticEvent(statEvent);
		//
		return result;
	}
	public Item dropRandomItem()
	{	Item result;
		int index = (int)(Math.random()*collectedItems.size());
		result = dropItem(index);
		return result;
	}
	public ArrayList<Item> dropAllItems()
	{	ArrayList<Item> result = new ArrayList<Item>();
		while(collectedItems.size()>0)
			result.add(dropItem(0));
		return result;
	}

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
