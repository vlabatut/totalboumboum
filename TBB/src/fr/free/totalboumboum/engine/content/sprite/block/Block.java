package fr.free.totalboumboum.engine.content.sprite.block;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;
import fr.free.totalboumboum.engine.content.sprite.Sprite;
import fr.free.totalboumboum.engine.loop.Loop;

public class Block extends Sprite
{
	public Block(Level level)
	{	super(level);
	}	

	public void finish()
	{	if(!finished)
		{	super.finish();
		}
	}
}
