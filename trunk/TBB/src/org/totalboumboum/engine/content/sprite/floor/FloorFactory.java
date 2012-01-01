package org.totalboumboum.engine.content.sprite.floor;

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

import org.totalboumboum.engine.container.tile.Tile;
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
import org.totalboumboum.engine.content.manager.trajectory.TrajectoryManager;
import org.totalboumboum.engine.content.sprite.SpriteFactory;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FloorFactory extends SpriteFactory<Floor>
{	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Floor makeSprite(Tile tile)
	{	// init
		Floor result = new Floor();
		
		// common managers
		initSprite(result);
	
		// anime

		// trajectory
		TrajectoryManager trajectoryManager = new EmptyTrajectoryManager(result);
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
		
		// control
		ControlManager controlManager = new EmptyControlManager(result);
		result.setControlManager(controlManager);

		// event
		EventManager eventManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			eventManager = new EmptyEventManager(result);
		else
			eventManager = new FloorEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.initSprite(tile);
		return result;
	}
}
