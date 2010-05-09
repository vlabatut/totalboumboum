package fr.free.totalboumboum.engine.content.feature.anime;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeDirection;


public class AnimeGesture
{	private HashMap<Direction,AnimeDirection> animes;
	private String name; //debug
	
	public AnimeGesture()
	{	animes = new HashMap<Direction,AnimeDirection>();
	}

	public AnimeDirection getAnimeDirection(Direction direction)
	{	AnimeDirection result = animes.get(direction);
		if(result==null)
			result = animes.get(direction.getHorizontalPrimary());
		if(result==null)
			result = animes.get(direction.getVerticalPrimary());
		if(result==null && direction!=Direction.NONE)
			result = animes.get(Direction.NONE);
		if(result==null && animes.size()>0)
			result = animes.entrySet().iterator().next().getValue();
		return result;
	}
	public void addAnimeDirection(AnimeDirection anime)
	{	animes.put(anime.getDirection(), anime);
	}
	public void setAnimeDirection(Direction direction,AnimeDirection anime)
	{	animes.put(direction, anime);
	}
	
	public AnimeGesture copy(ArrayList<BufferedImage> images, ArrayList<BufferedImage> copyImages)
	{	AnimeGesture result = new AnimeGesture();
		// animes
		Iterator<Map.Entry<Direction,AnimeDirection>> i = animes.entrySet().iterator();
		while(i.hasNext())
		{	Map.Entry<Direction,AnimeDirection> temp = i.next();
			AnimeDirection copyAd = temp.getValue().copy(images,copyImages);
			result.setAnimeDirection(temp.getKey(),copyAd);
		}
		// name
		result.setName(name);
		//
		return result;
	}

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}

	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<Direction,AnimeDirection>> it = animes.entrySet().iterator();
				while(it.hasNext())
				{	Entry<Direction,AnimeDirection> t = it.next();
					AnimeDirection temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
		}
	}
}
