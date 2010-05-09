package fr.free.totalboumboum.engine.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.round.Round;


public class SystemControl implements KeyListener
{	private Loop loop;
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private HashMap<Integer,Boolean> keysPressed;
	
	public SystemControl(Loop loop)
	{	this.loop = loop;
		configuration = loop.getConfiguration();
		keysPressed = new HashMap<Integer,Boolean>();
	}

    private Configuration configuration;
	public Configuration getConfiguration()
	{	return configuration;	
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
					|| (keyCode == KeyEvent.VK_END)
					|| ((keyCode == KeyEvent.VK_C) && e.isControlDown()))
				loop.setLooping(false);
			
			// faire renaitre le bonhomme
			else if ((keyCode == KeyEvent.VK_1))
			{	//TODO à adapter car ça peut être intéressant pour le débug
				//loop.rebirth();
			}

			// debug : modifier la vitesse
			else if ((keyCode == KeyEvent.VK_PAGE_UP))
			{	getConfiguration().setSpeedCoeff(getConfiguration().getSpeedCoeff()*2);
			}
			else if ((keyCode == KeyEvent.VK_PAGE_DOWN))
			{	getConfiguration().setSpeedCoeff(getConfiguration().getSpeedCoeff()/2);
			}

			// debug : grille
			else if ((keyCode == KeyEvent.VK_F1))
			{	loop.setShowGrid(!loop.getShowGrid());
			}
			// debug : tiles positions
			else if ((keyCode == KeyEvent.VK_F2))
			{	loop.setShowTilesPositions((loop.getShowTilesPositions()+1)%3);
			}
			// debug : speed coeff
			else if ((keyCode == KeyEvent.VK_F3))
			{	loop.setShowSpeed(!loop.getShowSpeed());
			}
			// debug : sprites positions
			else if ((keyCode == KeyEvent.VK_F4))
			{	loop.setShowSpritesPositions((loop.getShowSpritesPositions()+1)%3);
			}
			// debug : time
			else if ((keyCode == KeyEvent.VK_F5))
			{	loop.setShowTime(!loop.getShowTime());
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
