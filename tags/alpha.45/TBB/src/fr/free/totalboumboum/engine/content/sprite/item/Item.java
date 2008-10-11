package fr.free.totalboumboum.engine.content.sprite.item;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;


public class Item extends Sprite
{	/** abilities given by this item */
	private ArrayList<AbstractAbility> itemAbilities;
	private String itemName;
	
	public Item(Level level)
	{	super(level);
	itemAbilities = new ArrayList<AbstractAbility>();
	}

	public ArrayList<AbstractAbility> getItemAbilities()
	{	return itemAbilities;
	}
	public void addItemAbilities(ArrayList<AbstractAbility> abilities)
	{	Iterator<AbstractAbility> i = abilities.iterator();
		while(i.hasNext())
			addItemAbility(i.next());
	}
	public void addItemAbility(AbstractAbility ability)
	{	AbstractAbility copy = ability.copy();
		itemAbilities.add(copy);
	}
	
	public String getItemName()
	{	return itemName;
	}
	public void setItemName(String itemName)
	{	this.itemName = itemName;
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
