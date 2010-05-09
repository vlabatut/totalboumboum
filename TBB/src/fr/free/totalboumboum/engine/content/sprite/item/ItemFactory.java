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
import java.util.Iterator;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;
import fr.free.totalboumboum.engine.content.sprite.item.ItemEventManager;

public class ItemFactory extends SpriteFactory<Item>
{	private ArrayList<AbstractAbility> itemAbilities;
	private String itemName;
	
	public ItemFactory(Level level, String itemName)
	{	super(level);
		this.itemName = itemName;
	}
	
	public void setItemAbilities(ArrayList<AbstractAbility> itemAbilities)
	{	this.itemAbilities = itemAbilities;
	}
	
	public Item makeSprite()
	{	// init
		Item result = new Item(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// item ability
		result.addItemAbilities(itemAbilities);
		// event
		EventManager eventManager = new ItemEventManager(result);
		result.setEventManager(eventManager);
		// result
//		result.initGesture();
		result.setItemName(itemName);
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// item abilities
			{	Iterator<AbstractAbility> it = itemAbilities.iterator();
				while(it.hasNext())
				{	AbstractAbility temp = it.next();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
