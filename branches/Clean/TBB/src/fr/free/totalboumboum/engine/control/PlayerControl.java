package fr.free.totalboumboum.engine.control;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.GameVariables;
import fr.free.totalboumboum.configuration.controls.ControlSettings;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.player.Player;

public class PlayerControl implements KeyListener
{	
	public PlayerControl(Player player)
	{	this.player = player;
	}
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Player player;
	
	public Sprite getSprite()
	{	return player.getSprite();
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
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private final HashMap<Integer,Boolean> pressedKeys = new HashMap<Integer,Boolean>();
	
	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
//System.out.println(player.getName()+":"+e.getKeyChar());	
		if(!pressedKeys.containsKey(keyCode) || !pressedKeys.get(keyCode))
		{	pressedKeys.put(keyCode, true);
			if (!GameVariables.loop.isPaused() && getControlSettings().containsOnKey(keyCode))
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
		if (!GameVariables.loop.isPaused() && getControlSettings().containsOffKey(keyCode))
	    {	ControlCode controlCode = new ControlCode(keyCode,false);
			getSprite().putControlCode(controlCode);				
	    }
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{	// NOTE a priori inutile ici
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			player = null;
		}
	}
}
