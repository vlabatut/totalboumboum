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

import org.totalboumboum.engine.loop.VisibleLoop;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LocalSytemControl extends SystemControl
{	
	public LocalSytemControl(VisibleLoop loop)
	{	super(loop);
	}

	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		if(!keysPressed.containsKey(keyCode) || !keysPressed.get(keyCode))
		{	keysPressed.put(keyCode, true);
		
			// force game termination
			if ((keyCode == KeyEvent.VK_ESCAPE)
//					|| (keyCode == KeyEvent.VK_END)
//					|| ((keyCode == KeyEvent.VK_C) && e.isControlDown())
				)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_CANCEL_ROUND);
				loop.processEvent(controlEvent);
			}
			
			// faire renaitre le bonhomme
//			else if(keyCode == KeyEvent.VK_1)
//			{	//NOTE à adapter car ça peut être intéressant pour le débug
				//loop.rebirth();
//			}

			// debug: change speed
			else if(keyCode == KeyEvent.VK_PAGE_UP)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_SPEED_UP);
				loop.processEvent(controlEvent);
			}
			else if(keyCode == KeyEvent.VK_PAGE_DOWN)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_SLOW_DOWN);
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
			// debug: FPS/UPS
			else if(keyCode == KeyEvent.VK_F4)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_FPS);
				loop.processEvent(controlEvent);
			}
			// debug: speed coeff
			else if(keyCode == KeyEvent.VK_F5)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_SPEED);
				loop.processEvent(controlEvent);
			}
			// debug: time
			else if(keyCode == KeyEvent.VK_F6)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_TIME);
				loop.processEvent(controlEvent);
			}
			// debug: names
			else if(keyCode == KeyEvent.VK_F7)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_DISPLAY_PLAYERS_NAMES);
				loop.processEvent(controlEvent);
			}
			
			// debug: engine pause
			else if(keyCode == KeyEvent.VK_END)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.SWITCH_ENGINE_PAUSE);
				loop.processEvent(controlEvent);
			}
			else if(keyCode == KeyEvent.VK_HOME)
			{	SystemControlEvent controlEvent = new SystemControlEvent(SystemControlEvent.REQUIRE_ENGINE_STEP);
				loop.processEvent(controlEvent);
			}
			
			// debug: AIs
			else if(keyCode>=KeyEvent.VK_0 && keyCode<=KeyEvent.VK_9)
			{	int index;
				if(keyCode==KeyEvent.VK_0)
					index = 9;
				else
					index = keyCode-KeyEvent.VK_1;
				String name;
				if(keysPressed.containsKey(KeyEvent.VK_SHIFT) && keysPressed.get(KeyEvent.VK_SHIFT))
					name = SystemControlEvent.SWITCH_DISPLAY_AIS_PATHS;
				else if(keysPressed.containsKey(KeyEvent.VK_ALT) && keysPressed.get(KeyEvent.VK_ALT))
					name = SystemControlEvent.SWITCH_DISPLAY_AIS_COLORS;
				else if(keysPressed.containsKey(KeyEvent.VK_CONTROL) && keysPressed.get(KeyEvent.VK_CONTROL))
					name = SystemControlEvent.SWITCH_DISPLAY_AIS_TEXTS;
				else
					name = SystemControlEvent.SWITCH_AIS_PAUSE;
				SystemControlEvent controlEvent = new SystemControlEvent(name,index);
				loop.processEvent(controlEvent);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		keysPressed.put(keyCode, false);
	}
}
