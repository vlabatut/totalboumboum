package org.totalboumboum.engine.control.player;

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
import java.util.HashMap;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.player.ControlledPlayer;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LocalPlayerControl extends PlayerControl 
{	
	public LocalPlayerControl(ControlledPlayer player)
	{	super(player);
	}
	
	/////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ControlSettings getControlSettings()
	{	return player.getControlSettings();
	}
	
	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// nécessaire pour �viter d'�mettre des évènements de façon répétitive pour un seul pressage de touche
	private final HashMap<Integer,Boolean> pressedKeys = new HashMap<Integer,Boolean>();
	
	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
//System.out.println(player.getName()+":"+e.getKeyChar());	
		if(!pressedKeys.containsKey(keyCode) || !pressedKeys.get(keyCode))
		{	pressedKeys.put(keyCode, true);
			if (!RoundVariables.loop.getEnginePause() && getControlSettings().containsOnKey(keyCode))
		    {	ControlCode controlCode = new ControlCode(keyCode,true);
				getSprite().putControlCode(controlCode);				
		    }
	    }
	}

/*
 * TODO non-system keys should not be listened to when in pause 
 */
	
	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		pressedKeys.put(keyCode,false);	
		if (!RoundVariables.loop.getEnginePause() && getControlSettings().containsOffKey(keyCode))
	    {	ControlCode controlCode = new ControlCode(keyCode,false);
			getSprite().putControlCode(controlCode);				
	    }
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{	// should be useless here
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	finished = true;
			super.finish();
		}
	}
}
