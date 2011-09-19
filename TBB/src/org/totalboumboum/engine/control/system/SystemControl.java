package org.totalboumboum.engine.control.system;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
 * 
 * @author Vincent Labatut
 *
 */
public abstract class SystemControl implements KeyListener
{	
	public SystemControl(VisibleLoop loop)
	{	this.loop = loop;
		keysPressed = new HashMap<Integer,Boolean>();
	}

	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// n�cessaire pour �viter d'�mettre des �v�nements de fa�on répétitive pour un seul pressage de touche
	protected HashMap<Integer,Boolean> keysPressed;

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
	protected VisibleLoop loop;

	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			loop = null;
		}
	}
}
