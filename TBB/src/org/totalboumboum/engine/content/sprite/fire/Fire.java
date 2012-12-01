package org.totalboumboum.engine.content.sprite.fire;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Contact;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.feature.Orientation;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.TilePosition;
import org.totalboumboum.engine.content.feature.ability.ActionAbility;
import org.totalboumboum.engine.content.feature.action.SpecificAction;
import org.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import org.totalboumboum.engine.content.feature.event.ActionEvent;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Fire extends Sprite
{	
	public Fire()
	{	super();
	}	

	/////////////////////////////////////////////////////////////////
	// ROLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Role getRole()
	{	return Role.FIRE;
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String firesetName;
	
	public String getFiresetName()
	{	return firesetName;	
	}
	
	public void setFiresetName(String firesetName)
	{	this.firesetName = firesetName;	
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public boolean consumeTile(Tile tile, boolean fake)
	{	boolean result = false;
//if(!fake)
//	System.out.println();
		List<Sprite> sprites = tile.getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext())
		{	Sprite ts = i.next();
			result = consumeSprite(ts,fake) || result;
		}
		return result || sprites.isEmpty();
	}
	
	public boolean consumeSprite(Sprite sprite, boolean fake)
	{	SpecificAction specificAction = new SpecificConsume(this,sprite,Direction.NONE,Contact.INTERSECTION,TilePosition.SAME,Orientation.NEUTRAL);
		ActionAbility ability = modulateAction(specificAction);
		boolean result = ability.isActive();
		if(result && !fake)
		{	ActionEvent e = new ActionEvent(specificAction);
			sprite.processEvent(e);
		}
		return result;
	}
}
