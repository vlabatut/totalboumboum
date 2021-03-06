package org.totalboumboum.engine.content.feature.action.transmit;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.engine.content.feature.action.ActionName;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.item.Item;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SpecificTransmit extends SpecificAction
{
	/**
	 * fake action, just for test purposes
	 */
	public SpecificTransmit(Sprite actor, Sprite target)
	{	super(ActionName.TRANSMIT,actor,target);
	}

	public SpecificTransmit(Sprite actor, Sprite target, Item object)
	{	super(ActionName.TRANSMIT,actor,target);
		this.object = object;
	}

	/////////////////////////////////////////////////////////////////
	// OBJECT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Item object;
	
	public Item getObject()
	{	return object;	
	}
}
