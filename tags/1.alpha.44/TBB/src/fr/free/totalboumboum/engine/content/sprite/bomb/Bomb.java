package fr.free.totalboumboum.engine.content.sprite.bomb;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;

public class Bomb extends Sprite
{	private String bombName;
	
	public Bomb(Level level)
	{	super(level);
	}

	public String getBombName()
	{	return bombName;
	}
	public void setBombName(String bombName)
	{	this.bombName = bombName;
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
