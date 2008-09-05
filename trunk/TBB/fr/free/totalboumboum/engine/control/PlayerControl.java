package fr.free.totalboumboum.engine.control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.data.profile.ControlSettings;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.Player;
import fr.free.totalboumboum.game.round.Round;



public class PlayerControl implements KeyListener
{	
	private Player player;
	// nécessaire pour éviter d'émettre des évènements de façon répétitive pour un seul pressage de touche
	private final HashMap<Integer,Boolean> keysPressed = new HashMap<Integer,Boolean>();
	
	public PlayerControl(Player player)
	{	this.player = player;
	}
	
	public Loop getLoop()
	{	return player.getLoop();
	}

	public Sprite getSprite()
	{	return player.getSprite();
	}
	public ControlSettings getControlSettings()
	{	return player.getControlSettings();
	}
	
	public Configuration getConfiguration()
	{	return player.getConfiguration();
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		if(!keysPressed.containsKey(keyCode) || !keysPressed.get(keyCode))
		{	keysPressed.put(keyCode, true);
			if (!getLoop().isPaused() && getControlSettings().containsOnKey(keyCode))
		    {	ControlCode controlCode = new ControlCode(keyCode,true);
				getSprite().putControlCode(controlCode);				
		    }
	    }
	}

	@Override
	public void keyReleased(KeyEvent e)
	{	int keyCode = e.getKeyCode();
		keysPressed.put(keyCode,false);	
		if (!getLoop().isPaused() && getControlSettings().containsOffKey(keyCode))
	    {	ControlCode controlCode = new ControlCode(keyCode,false);
			getSprite().putControlCode(controlCode);				
	    }
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{	// NOTE a priori inutile ici
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			player = null;
		}
	}
}
