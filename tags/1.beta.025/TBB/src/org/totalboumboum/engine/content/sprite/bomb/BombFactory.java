package org.totalboumboum.engine.content.sprite.bomb;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbilityName;
import org.totalboumboum.engine.content.manager.bombset.BombsetManager;
import org.totalboumboum.engine.content.manager.bombset.EmptyBombsetManager;
import org.totalboumboum.engine.content.manager.bombset.FullBombsetManager;
import org.totalboumboum.engine.content.manager.control.ControlManager;
import org.totalboumboum.engine.content.manager.control.EmptyControlManager;
import org.totalboumboum.engine.content.manager.event.EventManager;
import org.totalboumboum.engine.content.manager.event.EmptyEventManager;
import org.totalboumboum.engine.content.manager.item.EmptyItemManager;
import org.totalboumboum.engine.content.manager.item.ItemManager;
import org.totalboumboum.engine.content.manager.trajectory.EmptyTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.FullTrajectoryManager;
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
	
	// NOTE not tested yet
	public long getBombDuration()
	{	long result = 0;
		AbstractAbility ability = null;
		Iterator<AbstractAbility> it = abilities.iterator();
		while(ability==null && it.hasNext())
		{	AbstractAbility temp = it.next();
			if(temp instanceof StateAbility)
			{	StateAbility sa = (StateAbility)temp;
				if(sa.getName()==StateAbilityName.BOMB_TRIGGER_TIMER)
					ability = sa;
			}
		}
		if(ability!=null)
			result = (long)ability.getStrength();
		return result;
	}
	
	public Bomb makeSprite(Tile tile)
	{	// init
		Bomb result = new Bomb();
		
		// common managers
		initSprite(result);
	
		// anime

		// trajectory
		TrajectoryManager trajectoryManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			trajectoryManager = new EmptyTrajectoryManager(result);
		else
			trajectoryManager = new FullTrajectoryManager(result);
		result.setTrajectoryManager(trajectoryManager);
		
		// bombset
		BombsetManager bombsetManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			bombsetManager = new EmptyBombsetManager(result);
		else
		{	bombsetManager = new FullBombsetManager(result);
			bombsetManager.setBombset(bombset);
		}
//if(bombset==null)
//	System.out.println();
		result.setBombsetManager(bombsetManager);
		
		// explosion
		
		// modulations
		
		// item
		ItemManager itemManager = new EmptyItemManager(result);
		result.setItemManager(itemManager);
		
		// ability
		
		// delay
//		value = configuration.getBombSetting(Configuration.BOMB_SETTING_LIFETIME);
//		result.addDelay(DelayManager.DL_EXPLOSION,value);
		
		// control
		ControlManager controlManager = new EmptyControlManager(result);
		result.setControlManager(controlManager);

		// event
		EventManager eventManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			eventManager = new EmptyEventManager(result);
		else
			eventManager = new BombEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.setBombName(bombName);
		result.initSprite(tile);
		return result;
	}
}
