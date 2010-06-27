package org.totalboumboum.engine.content.manager.item;

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
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ItemManager
{	
	public ItemManager(Sprite sprite)
	{	this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Sprite sprite;
	
	/////////////////////////////////////////////////////////////////
	// ABILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
	
	public List<AbstractAbility> getItemAbilities()
	{	return abilities;
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final LinkedList<Item> originalInitialItems = new LinkedList<Item>();
	protected final LinkedList<Item> initialItems = new LinkedList<Item>();

	public abstract void addInitialItem(Item item);
	
	public abstract void reinitInitialItems();
	
	/////////////////////////////////////////////////////////////////
	// COLLECTED ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final LinkedList<Item> collectedItems = new LinkedList<Item>();
	
	public abstract void collectItem(Item item);

	/////////////////////////////////////////////////////////////////
	// RECEIVE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final LinkedList<Item> receivedItems = new LinkedList<Item>();

	public abstract void receiveItem(Item item);
	
	/////////////////////////////////////////////////////////////////
	// RELEASE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void releaseLastItem();
	
	public abstract void releaseRandomItem();
	
	public abstract void releaseAllItems();

	/////////////////////////////////////////////////////////////////
	// TRANSMIT ITEM	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract void transmitAllItems(Sprite target);

	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** marker allowing to start updating items only when the round has actually started */
	protected boolean start = false;
	
	public void start()
	{	start = true;	
	}
	
	public abstract void update();

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract ItemManager copy(Sprite sprite);
}
