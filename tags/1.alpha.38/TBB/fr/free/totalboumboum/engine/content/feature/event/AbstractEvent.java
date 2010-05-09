package fr.free.totalboumboum.engine.content.feature.event;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.anime.AnimeGesture;

public class AbstractEvent
{
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		}
	}

}
