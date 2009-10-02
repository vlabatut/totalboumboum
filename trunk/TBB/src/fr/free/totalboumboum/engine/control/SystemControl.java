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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.loop.Loop;

public class SystemControl implements KeyListener
{	
	private Loop loop;
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private HashMap<Integer,Boolean> keysPressed;
	
	public SystemControl(Loop loop)
	{	this.loop = loop;
		keysPressed = new HashMap<Integer,Boolean>();
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
				loop.setCanceled(true);
			
			// faire renaitre le bonhomme
//			else if(keyCode == KeyEvent.VK_1)
//			{	//TODO à adapter car ça peut être intéressant pour le débug
				//loop.rebirth();
//			}

			// debug : modifier la vitesse
			else if(keyCode == KeyEvent.VK_PAGE_UP)
			{	Configuration.getEngineConfiguration().setSpeedCoeff(Configuration.getEngineConfiguration().getSpeedCoeff()*2);
			}
			else if(keyCode == KeyEvent.VK_PAGE_DOWN)
			{	Configuration.getEngineConfiguration().setSpeedCoeff(Configuration.getEngineConfiguration().getSpeedCoeff()/2);
			}

			// debug : grille
			else if(keyCode == KeyEvent.VK_F1)
			{	loop.setShowGrid(!loop.getShowGrid());
			}
			// debug : tiles positions
			else if(keyCode == KeyEvent.VK_F2)
			{	loop.switchShowTilesPositions();
			}
			// debug : sprites positions
			else if(keyCode == KeyEvent.VK_F3)
			{	loop.switchShowSpritesPositions();
			}
			// debug : FPS/UPS
			else if(keyCode == KeyEvent.VK_F4)
			{	loop.switchShowFPS();
			}
			// debug : speed coeff
			else if(keyCode == KeyEvent.VK_F5)
			{	loop.switchShowSpeed();
			}
			// debug : time
			else if(keyCode == KeyEvent.VK_F6)
			{	loop.switchShowTime();
			}
			
			// debug : engine pause
			else if(keyCode == KeyEvent.VK_END)
			{	loop.switchEnginePause();
			}
			
			// debug : AIs pause
			else if(keyCode == KeyEvent.VK_1)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(0);
				else
					loop.switchShowAiInfo(0);
			}
			else if(keyCode == KeyEvent.VK_2)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(1);
				else
					loop.switchShowAiInfo(1);
			}
			else if(keyCode == KeyEvent.VK_3)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(2);
				else
					loop.switchShowAiInfo(2);
			}
			else if(keyCode == KeyEvent.VK_4)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(3);
				else
					loop.switchShowAiInfo(3);
			}
			else if(keyCode == KeyEvent.VK_5)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(4);
				else
					loop.switchShowAiInfo(4);
			}
			else if(keyCode == KeyEvent.VK_6)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(5);
				else
					loop.switchShowAiInfo(5);
			}
			else if(keyCode == KeyEvent.VK_7)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(6);
				else
					loop.switchShowAiInfo(6);
			}
			else if(keyCode == KeyEvent.VK_8)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(7);
				else
					loop.switchShowAiInfo(7);
			}
			else if(keyCode == KeyEvent.VK_9)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(8);
				else
					loop.switchShowAiInfo(8);
			}
			else if(keyCode == KeyEvent.VK_0)
			{	if(keysPressed.containsKey(KeyEvent.VK_SHIFT))
					loop.switchAiPause(9);
				else
					loop.switchShowAiInfo(9);
			}
		}
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
