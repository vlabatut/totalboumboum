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

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.ActionAbility;
import fr.free.totalboumboum.engine.content.feature.event.ActionEvent;
import fr.free.totalboumboum.engine.content.feature.gesture.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.gesture.action.Role;
import fr.free.totalboumboum.engine.content.feature.gesture.action.SpecificAction;
import fr.free.totalboumboum.engine.content.sprite.getModulationStateAbilities;

public class Fire extends getModulationStateAbilities
{	
	private String firesetName;
	
	public Fire(Level level)
	{	super(level);
	}	

	public String getFiresetName()
	{	return firesetName;	
	}
	public void setFiresetName(String firesetName)
	{	this.firesetName = firesetName;	
	}
	
	public void consumeTile(Tile tile)
	{	ArrayList<getModulationStateAbilities> sprites = tile.getSprites();
		Iterator<getModulationStateAbilities> i = sprites.iterator();
		while(i.hasNext())
		{	getModulationStateAbilities ts = i.next();
			consumeSprite(ts);
		}	
	}
	
	public void consumeSprite(getModulationStateAbilities sprite)
	{	SpecificAction specificAction = new SpecificAction(AbstractAction.CONSUME,this,sprite,Direction.NONE);
		ActionAbility ability = computeAbility(specificAction);
		if(ability.isActive())
		{	ActionEvent e = new ActionEvent(specificAction);
			sprite.processEvent(e);
		}
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			owner = null;
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Role getRole()
	{	return Role.FIRE;
	}

}
