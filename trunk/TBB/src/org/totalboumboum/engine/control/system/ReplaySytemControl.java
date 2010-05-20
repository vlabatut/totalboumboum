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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.engine.loop.ServerLoop;

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
			{	loop.setCanceled(true);
			}
			
			// replay: fast forward
			else if(keyCode == KeyEvent.VK_RIGHT)
			{	//TODO loop.setShowGrid(!loop.getShowGrid());
			}
			// replay: backward
			else if(keyCode == KeyEvent.VK_LEFT)
			{	//TODO loop.setShowGrid(!loop.getShowGrid());
			}
			// replay: speed up
			else if(keyCode == KeyEvent.VK_UP)
			{	Configuration.getEngineConfiguration().setSpeedCoeff(Configuration.getEngineConfiguration().getSpeedCoeff()*2);
			}
			// replay: slow down
			else if(keyCode == KeyEvent.VK_DOWN)
			{	Configuration.getEngineConfiguration().setSpeedCoeff(Configuration.getEngineConfiguration().getSpeedCoeff()/2);
			}
			// replay: pause/play
			else if(keyCode == KeyEvent.VK_SPACE)
			{	loop.switchEnginePause();
			}
			
			// debug: grid
			else if(keyCode == KeyEvent.VK_F1)
			{	loop.setShowGrid(!loop.getShowGrid());
			}
			// debug: tiles positions
			else if(keyCode == KeyEvent.VK_F2)
			{	loop.switchShowTilesPositions();
			}
			// debug: sprites positions
			else if(keyCode == KeyEvent.VK_F3)
			{	loop.switchShowSpritesPositions();
			}
			// debug: FPS/UPS
			else if(keyCode == KeyEvent.VK_F4)
			{	loop.switchShowFPS();
			}
			// debug: speed coeff
			else if(keyCode == KeyEvent.VK_F5)
			{	loop.switchShowSpeed();
			}
			// debug: time
			else if(keyCode == KeyEvent.VK_F6)
			{	loop.switchShowTime();
			}
			// debug: names
			else if(keyCode == KeyEvent.VK_F7)
			{	loop.switchShowNames();
			}
		}
	}
}
