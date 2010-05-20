package org.totalboumboum.engine.control.system;

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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import org.totalboumboum.engine.loop.VisibleLoop;

public abstract class SystemControl implements KeyListener
{	
	protected VisibleLoop loop;
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private HashMap<Integer,Boolean> keysPressed;
	
	public SystemControl(VisibleLoop loop)
	{	this.loop = loop;
		keysPressed = new HashMap<Integer,Boolean>();
	}

	// handles termination and game-play keys
	@Override
	public void keyPressed(KeyEvent e)
	{	// to override
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		keysPressed.put(keyCode, false);		
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{	// NOTE a priori inutile ici
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			loop = null;
		}
	}
}
