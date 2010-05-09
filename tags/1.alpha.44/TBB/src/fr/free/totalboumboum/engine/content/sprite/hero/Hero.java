package fr.free.totalboumboum.engine.content.sprite.hero;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;

public class Hero extends Sprite
{	
	public Hero(Level level)
	{	super(level);
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
