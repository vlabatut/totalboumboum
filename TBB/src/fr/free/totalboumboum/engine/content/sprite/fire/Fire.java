package fr.free.totalboumboum.engine.content.sprite.fire;

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

import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.Role;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.action.consume.SpecificConsume;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.sprite.Sprite;

public class Fire extends Sprite
{	
	private String firesetName;
	
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
	public String getFiresetName()
	{	return firesetName;	
	}
	
	public void setFiresetName(String firesetName)
	{	this.firesetName = firesetName;	
	}
	
	/////////////////////////////////////////////////////////////////
	// EXECUTION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void appear(Direction direction)
	{	((FireEventManager)eventManager).appear(direction);		
	}
	
	public void consumeTile(Tile tile)
	{	ArrayList<Sprite> sprites = tile.getSprites();
		Iterator<Sprite> i = sprites.iterator();
		while(i.hasNext())
		{	Sprite ts = i.next();
			consumeSprite(ts);
		}	
	}
	
	public void consumeSprite(Sprite sprite)
	{	SpecificAction specificAction = new SpecificConsume(this,sprite,Direction.NONE,Contact.INTERSECTION,TilePosition.SAME,Orientation.NEUTRAL);
		ActionAbility ability = modulateAction(specificAction);
		if(ability.isActive())
		{	ActionEvent e = new ActionEvent(specificAction);
			sprite.processEvent(e);
		}
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			owner = null;
		}
	}

}
