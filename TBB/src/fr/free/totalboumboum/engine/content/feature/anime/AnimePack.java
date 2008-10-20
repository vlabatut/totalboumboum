package fr.free.totalboumboum.engine.content.feature.anime;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.profile.PredefinedColor;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.ability.AbstractAbility;
import fr.free.totalboumboum.engine.content.feature.anime.AnimeDirection;



public class AnimePack
{	private HashMap<String,AnimeGesture> gestures;
	private String defaultAnime;
	private ArrayList<BufferedImage> images;
	private double scale;
	private PredefinedColor color;
	
	public AnimePack()
	{	gestures = new HashMap<String,AnimeGesture>();
		defaultAnime = null;
		images = new ArrayList<BufferedImage>();
		scale = 1;
		color = null;
	}

	public double getScale()
	{	return scale;
	}
	public void setScale(double scale)
	{	this.scale = scale;
	}

	public void setDefaultAnime(String gesture)
	{	this.defaultAnime = gesture;	
	}
	
	public AnimeDirection getAnimeDirection(String name,Direction direction)
	{	AnimeDirection result = null;
		AnimeGesture gesture;
		if(!gestures.containsKey(name))
		{	gesture = gestures.get(defaultAnime);
			result = gesture.getAnimeDirection(direction);
		}
		else
		{	gesture = gestures.get(name);
			result = gesture.getAnimeDirection(direction);
			if(result==null && !name.equals(defaultAnime))
				result = getAnimeDirection(defaultAnime,direction);
		}
		return result;
	}
	
	public void setAnimeDirection(String name, Direction direction, AnimeDirection anime)
	{	AnimeGesture gesture;
		if(!gestures.containsKey(name))
		{	gesture = new AnimeGesture();
			gesture.setName(name);
			gestures.put(name, gesture);
		}
		else
			gesture = gestures.get(name);
		gesture.setAnimeDirection(direction, anime);
	}
	public void addAnimeGesture(AnimeGesture animeGesture)
	{	gestures.put(animeGesture.getName(), animeGesture);
	}
	public void setAnimeGesture(String name,AnimeGesture animeGesture)
	{	gestures.put(name,animeGesture);
	}

	public void addImage(BufferedImage image)
	{	images.add(image);
	}
	public ArrayList<BufferedImage> getImages()
	{	return images;	
	}

	public PredefinedColor getColor()
	{	return color;
	}
	public void setColor(PredefinedColor color)
	{	this.color = color;
	}
	
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			// gestures
			{	Iterator<Entry<String,AnimeGesture>> it = gestures.entrySet().iterator();
				while(it.hasNext())
				{	Entry<String,AnimeGesture> t = it.next();
					AnimeGesture temp = t.getValue();
					temp.finish();
					it.remove();
				}
			}
			// images
			{	Iterator<BufferedImage> it = images.iterator();
				while(it.hasNext())
				{	BufferedImage temp = it.next();
					it.remove();
				}
			}
			// misc
			color = null;
			defaultAnime = null;
		}
	}
}
