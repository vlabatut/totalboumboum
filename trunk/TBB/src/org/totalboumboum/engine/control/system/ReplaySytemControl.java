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

import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * This class is used to handle the
 * server-side system controls during 
 * a network game.
 * 
 * @author Vincent Labatut
 */
public class ReplaySytemControl extends SystemControl
{	
	/**
	 * Builds a standard control object.
	 * 
	 * @param loop
	 * 		Loop to control.
	 */
	public ReplaySytemControl(ReplayLoop loop)
	{	super(loop);
	}

	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_CANCEL_ROUND);
				loop.processEvent(controlEvent);
			}
			
			// print screen
//			else if(keyCode == KeyEvent.VK_PRINTSCREEN)	// TODO this key works on release only!
//			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_PRINT_SCREEN);
//				loop.processEvent(controlEvent);
//			}
			
			// replay: fast forward
			else if(keyCode == KeyEvent.VK_RIGHT)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_FAST_FORWARD);
				loop.processEvent(controlEvent);
			}
			// replay: backward
			else if(keyCode == KeyEvent.VK_LEFT)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_BACKWARD);
				loop.processEvent(controlEvent);
			}
			// replay: speed up
			else if(keyCode == KeyEvent.VK_UP)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_SPEED_UP);
				loop.processEvent(controlEvent);
			}
			// replay: slow down
			else if(keyCode == KeyEvent.VK_DOWN)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_SLOW_DOWN);
				loop.processEvent(controlEvent);
			}
			// replay: pause/play
			else if(keyCode == KeyEvent.VK_SPACE)
			{	SystemControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_ENGINE_PAUSE,SystemControlEvent.MODE);
				else
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_ENGINE_PAUSE,SystemControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			
			// debug: grid
			else if(keyCode == KeyEvent.VK_F1)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_GRID);
				loop.processEvent(controlEvent);
			}
			// debug: tiles positions
			else if(keyCode == KeyEvent.VK_F2)
			{	SystemControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_TILES_POSITIONS,SystemControlEvent.MODE);
				else
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_TILES_POSITIONS,SystemControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: sprites positions
			else if(keyCode == KeyEvent.VK_F3)
			{	SystemControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS,SystemControlEvent.MODE);
				else
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPRITES_POSITIONS,SystemControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: hide/display sprites
			else if(keyCode == KeyEvent.VK_F4)
			{	SystemControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPRITES,SystemControlEvent.MODE);
				else
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPRITES,SystemControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: FPS/UPS
			else if(keyCode == KeyEvent.VK_F5)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_FPS);
				loop.processEvent(controlEvent);
			}
			// debug: speed coeff
			else if(keyCode == KeyEvent.VK_F6)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPEED);
				loop.processEvent(controlEvent);
			}
			// debug: time
			else if(keyCode == KeyEvent.VK_F7)
			{	SystemControlEvent controlEvent;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_TIME,SystemControlEvent.MODE);
				else
					controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_TIME,SystemControlEvent.REGULAR);
				loop.processEvent(controlEvent);
			}
			// debug: names
			else if(keyCode == KeyEvent.VK_F8)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_PLAYERS_NAMES);
				loop.processEvent(controlEvent);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		keysPressed.put(keyCode, false);
		
		// print screen
		if(keyCode == KeyEvent.VK_PRINTSCREEN)
		{	SystemControlEvent controlEvent = null;
			if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
				controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_PRINT_SCREEN,SystemControlEvent.MODE);
			else
				controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_PRINT_SCREEN,SystemControlEvent.REGULAR);
			loop.processEvent(controlEvent);
		}

		// replay: fast forward
		else if(keyCode == KeyEvent.VK_RIGHT)
		{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_FAST_FORWARD);
			loop.processEvent(controlEvent);
		}
		// replay: backward
		else if(keyCode == KeyEvent.VK_LEFT)
		{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_BACKWARD);
			loop.processEvent(controlEvent);
		}
	}
}
