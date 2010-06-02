package org.totalboumboum.engine.content.sprite.bomb;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.manager.event.ReplayEventManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;

public class BombFactory extends SpriteFactory<Bomb>
{	
	public BombFactory(String bombName)
	{	this.bombName = bombName;
	}	
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String bombName;

	public String getBombName()
	{	return bombName;	
	}
	
	public Bomb makeSprite(Tile tile)
	{	// init
		Bomb result = new Bomb();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// delay
//		value = configuration.getBombSetting(Configuration.BOMB_SETTING_LIFETIME);
//		result.addDelay(DelayManager.DL_EXPLOSION,value);
		// event
		EventManager eventManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			eventManager = new ReplayEventManager(result);
		else
			eventManager = new BombEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.setBombName(bombName);
		result.initSprite(tile);
		return result;
	}
}
