package org.totalboumboum.engine.control.system;

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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import org.totalboumboum.engine.loop.VisibleLoop;

/**
 * General class in charge of
 * monitoring the system keys
 * during game.
 * 
 * @author Vincent Labatut
 */
public abstract class SystemControl implements KeyListener
{	
	/**
	 * Builds a standard control object.
	 * 
	 * @param loop
	 * 		Loop to control.
	 */
	public SystemControl(VisibleLoop loop)
	{	this.loop = loop;
		keysPressed = new HashMap<Integer,Boolean>();
	}

	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Records the keys currently pressed */
	protected HashMap<Integer,Boolean> keysPressed; // nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche

	@Override
	public void keyPressed(KeyEvent e)
	{	// to override
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	// to override	
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{	// (a priori) useless here
	}

	/////////////////////////////////////////////////////////////////
	// LOOP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Object displaying the game */
	protected VisibleLoop loop;

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	private boolean finished = false;
	
	/**
	 * Cleanly finishes this object.
	 */
	public void finish()
	{	if(!finished)
		{	finished = true;
			loop = null;
		}
	}
}
