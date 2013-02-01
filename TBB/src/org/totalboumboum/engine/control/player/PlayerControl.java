package org.totalboumboum.engine.control.player;

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

import java.awt.event.KeyListener;

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.player.ControlledPlayer;

/**
 * General class in charge of
 * monitoring the players keys
 * during game.
 * 
 * @author Vincent Labatut
 */
public abstract class PlayerControl implements KeyListener
{	
	/**
	 * Builds an object monitoring
	 * the specified player.
	 * 
	 * @param player
	 * 		Player to monitor.
	 */
	public PlayerControl(ControlledPlayer player)
	{	this.player = player;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Monitored player */
	protected ControlledPlayer player;
	
	/**
	 * Returns the sprite of the
	 * monitored player.
	 * 
	 * @return
	 * 		Sprite of the monitored player.
	 */
	public Sprite getSprite()
	{	return player.getSprite();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	protected boolean finished = false;
	
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	player = null;
	}
}
