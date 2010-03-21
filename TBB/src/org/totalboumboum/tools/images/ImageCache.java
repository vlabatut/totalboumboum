package org.totalboumboum.tools.images;

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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.gesture.anime.color.Colormap;

public class ImageCache
{	
	/////////////////////////////////////////////////////////////////
	// CACHED IMAGES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,CachedImage> imageMap = new HashMap<String, CachedImage>();

	public void addImage(String imgPath, Colormap colormap)
	{	CachedImage cachedImage = imageMap.get(imgPath);
		if(cachedImage==null)
		{	cachedImage = new CachedImage(this,imgPath,colormap);
			imageMap.put(imgPath,cachedImage);
			imageNames.offer(imgPath);
		}
		else
			cachedImage.addColormap(colormap);
	}
	
	public BufferedImage retrieveImage(String imgPath, PredefinedColor color, double zoom) throws IOException
    {	// load or get the image
		CachedImage cachedImage = imageMap.get(imgPath);
if(cachedImage==null)
	System.out.println("ERREUR: image pas en cache:"+imgPath);
    	
		// update FIFO
		imageNames.removeFirstOccurrence(imgPath);
		imageNames.offer(imgPath);
		
    	// return image
    	BufferedImage result = cachedImage.getImage(zoom,color);
    	return result;
    }
    
    public void clear()
    {	// erase all images
		imageMap.clear();
    	imageNames.clear();
    	size = 0;

    	// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
    }
    
    public void clean()
    {	// erase unused images
    	while(size>Configuration.getEngineConfiguration().getImageCacheLimit())
		{	String older = imageNames.poll();
			CachedImage temp = imageMap.get(older);
			size = size - temp.getTotalSize();
			imageMap.remove(older);
		}
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
    }

	/////////////////////////////////////////////////////////////////
	// CACHED NAMES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LinkedList<String> imageNames = new LinkedList<String>();

	/////////////////////////////////////////////////////////////////
	// CACHE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double size = 0;
	
	public double getSize()
	{	return size;		
	}
	
	public void increaseSize(double imageSize)
	{	size = size + imageSize;
	}
}
