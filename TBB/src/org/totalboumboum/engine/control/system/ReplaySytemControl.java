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
import java.util.HashMap;

import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.event.control.ControlEvent;

public class ReplaySytemControl extends SystemControl
{	
	private ServerLoop loop;
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private HashMap<Integer,Boolean> keysPressed;
	
	public ReplaySytemControl(ReplayLoop loop)
	{	super(loop);
	}

	// handles termination and game-play keys
	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		if(!keysPressed.containsKey(keyCode) || !keysPressed.get(keyCode))
		{	keysPressed.put(keyCode, true);
		
			// termination keys
			// allow a convenient exit from the full screen configuration
			if ((keyCode == KeyEvent.VK_ESCAPE)
//					|| (keyCode == KeyEvent.VK_END)
//					|| ((keyCode == KeyEvent.VK_C) && e.isControlDown())
				)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.REQUIRE_CANCEL_ROUND);
				loop.processEvent(controlEvent);
			}
			
			// replay: fast forward
			else if(keyCode == KeyEvent.VK_RIGHT)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_FAST_FORWARD);
				loop.processEvent(controlEvent);
			}
			// replay: backward
			else if(keyCode == KeyEvent.VK_LEFT)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_BACKWARD);
				loop.processEvent(controlEvent);
			}
			// replay: speed up
			else if(keyCode == KeyEvent.VK_UP)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.REQUIRE_SPEED_UP);
				loop.processEvent(controlEvent);
			}
			// replay: slow down
			else if(keyCode == KeyEvent.VK_DOWN)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.REQUIRE_SLOW_DOWN);
				loop.processEvent(controlEvent);
			}
			// replay: pause/play
			else if(keyCode == KeyEvent.VK_SPACE)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_ENGINE_PAUSE);
				loop.processEvent(controlEvent);
			}
			
			// debug: grid
			else if(keyCode == KeyEvent.VK_F1)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_GRID);
				loop.processEvent(controlEvent);
			}
			// debug: tiles positions
			else if(keyCode == KeyEvent.VK_F2)
			{	ControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_TILES_POSITIONS,ControlEvent.MODE);
				else
					controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_TILES_POSITIONS,ControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: sprites positions
			else if(keyCode == KeyEvent.VK_F3)
			{	ControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS,ControlEvent.MODE);
				else
					controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS,ControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: FPS/UPS
			else if(keyCode == KeyEvent.VK_F4)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_FPS);
				loop.processEvent(controlEvent);
			}
			// debug: speed coeff
			else if(keyCode == KeyEvent.VK_F5)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_SPEED);
				loop.processEvent(controlEvent);
			}
			// debug: time
			else if(keyCode == KeyEvent.VK_F6)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_TIME);
				loop.processEvent(controlEvent);
			}
			// debug: names
			else if(keyCode == KeyEvent.VK_F7)
			{	ControlEvent controlEvent = new ControlEvent(ControlEvent.SWITCH_DISPLAY_PLAYERS_NAMES);
				loop.processEvent(controlEvent);
			}
		}
	}
}
