package fr.free.totalboumboum.engine.content.sprite.bomb;

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

import java.io.Serializable;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.manager.EventManager;
import fr.free.totalboumboum.engine.content.sprite.SpriteFactory;

public class BombFactory extends SpriteFactory<Bomb> implements Serializable
{	private static final long serialVersionUID = 1L;

	private String bombName;
	
	public BombFactory(Level level, String bombName)
	{	super(level);
		this.bombName = bombName;
	}	
	
	public Bomb makeSprite()
	{	// init
		Bomb result = new Bomb(level);
		
		// common managers
		setCommonManager(result);
	
		// specific managers
		// delay
//		value = configuration.getBombSetting(Configuration.BOMB_SETTING_LIFETIME);
//		result.addDelay(DelayManager.DL_EXPLOSION,value);
		// event
		EventManager eventManager = new BombEventManager(result);
		result.setEventManager(eventManager);
		
		// result
//		result.initGesture();
		result.setBombName(bombName);
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
