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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EmptyItemManager extends ItemManager
{	
	public EmptyItemManager(Sprite sprite)
	{	super(sprite);
	}

	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void addInitialItem(Item item)
	{	
		// useless here
	}
	
	@Override
	public void reinitInitialItems()
	{			
		// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// COLLECTED ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void collectItem(Item item)
	{	
		// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// RECEIVE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void receiveItem(Item item)
	{	
		// useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// RELEASE ITEMS			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void releaseLastItem()
	{	
		// useless here
	}
	
	@Override
	public void releaseRandomItem()
	{	
		// useless here
	}
	
	@Override
	public void releaseAllItems()
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// TRANSMIT ITEM	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void transmitAllItems(Sprite target)
	{	
		// useless here
	}
		
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void update()
	{	
		// useless here
	}

	/////////////////////////////////////////////////////////////////
	// COPY					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public ItemManager copy(Sprite sprite)
	{	ItemManager result = new EmptyItemManager(sprite);
		return result;
	}
}
