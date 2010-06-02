package org.totalboumboum.engine.content.sprite.hero;

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

public class HeroFactory extends SpriteFactory<Hero>
{	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Hero makeSprite(Tile tile)
	{	// init
		Hero result = new Hero();
		
		// common managers
		initSprite(result);
	
		// specific managers
		// delay
//		double value = configuration.getHeroSetting(Configuration.HERO_SETTING_WAIT_DELAY);
//		result.addDelay(DelayManager.DL_WAIT,value);
		// event
		EventManager eventManager;
		if(RoundVariables.loop instanceof ReplayLoop)
			eventManager = new ReplayEventManager(result);
		else
			eventManager = new HeroEventManager(result);
		result.setEventManager(eventManager);
		
		// result
		result.initSprite(tile);
		return result;
	}
}
