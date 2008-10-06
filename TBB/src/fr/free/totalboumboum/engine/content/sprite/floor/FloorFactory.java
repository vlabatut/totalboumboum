package fr.free.totalboumboum.engine.content.sprite.floor;

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

import java.util.ArrayList;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimePack;
import fr.free.totalboumboum.engine.content.feature.explosion.Explosion;
import fr.free.totalboumboum.engine.content.feature.permission.PermissionPack;
import fr.free.totalboumboum.engine.content.feature.trajectory.TrajectoryPack;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;
import fr.free.totalboumboum.engine.loop.Loop;


public class FloorFactory extends SpriteFactory<Floor>
{	
	public FloorFactory(Level level)
	{	super(level);
	}	
	
	public Floor makeSprite()
	{	// init
		Floor result = new Floor(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// event
		EventManager eventManager = new FloorEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
