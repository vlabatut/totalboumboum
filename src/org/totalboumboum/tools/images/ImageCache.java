package org.totalboumboum.tools.images;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorFolder;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRule;

/**
 * This class manages all cached images.
 * 
 * @author Vincent Labatut
 */
public class ImageCache
{	
	/////////////////////////////////////////////////////////////////
	// CACHED IMAGES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of all images currently in memory */
	private Map<String,CachedImage> imageMap = new HashMap<String, CachedImage>();

	/**
	 * Returns the specified image from the cache, or loads it if necessary.
	 * 
	 * @param imagePath
	 * 		Path of the targeted file.
	 * @param colorRule
	 * 		Color of the image.
	 * @param zoom
	 * 		Zoom factor.
	 * @return
	 * 		The corresponding image.
	 * 
	 * @throws IOException
	 * 		Problem while loading the image.
	 */
	public BufferedImage retrieveImage(String imagePath, ColorRule colorRule, double zoom) throws IOException
	{	BufferedImage result = null;
		String path = null;
		ColorMap colorMap = null;
		String basePath = colorRule.getParent().getLocalPath();

		// indexed colors
		if(colorRule instanceof ColorMap)
		{	colorMap = (ColorMap) colorRule;
			path = basePath+File.separator+imagePath;
		}
		
		// not indexed colors
		else if(colorRule instanceof ColorFolder)
		{	String folder = ((ColorFolder)colorRule).getFolder();
			path = basePath+File.separator+folder+File.separator+imagePath;
			colorMap = null;
		}
		
		// colorless
		else
		{	path = basePath+File.separator+imagePath;
			colorMap = null;
		}
		
		CachedImage cachedImage = getImage(path);
		result = cachedImage.getImage(colorMap,zoom);

		return result;
	}

	/**
	 * Returns the cached image object for the specified file.
	 * 
	 * @param path
	 * 		Path of the image.
	 * @return
	 * 		The corresponding cached image object.
	 */
	private CachedImage getImage(String path)
	{	CachedImage result = imageMap.get(path);
//System.out.println(path+": "+(result==null));
//if(path.equals("resources\\instances\\superbomberman1\\items\\punch\\animes\\released\\1.gif"))
//	System.out.println();
		if(result==null)
		{	result = new CachedImage(this,path);
			imageMap.put(path,result);
			imageNames.offer(path);
		}
		else
		{	imageNames.remove(path);
			imageNames.offer(path);
		}
		return result;
	}
	
	/**
	 * Clears the cache.
	 */
    public void clear()
    {	// erase all images
		imageMap.clear();
    	imageNames.clear();
    	size = 0;

    	// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
    }
    
    /**
     * Cleans the cache by removing unused images.
     */
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
	/** Names of the cached files */
    private LinkedList<String> imageNames = new LinkedList<String>();

	/////////////////////////////////////////////////////////////////
	// CACHE SIZE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    /** Approximate current size of the cache */
	private double size = 0;
	
	/**
	 * Returns the approximate current size of the cache.
	 * 
	 * @return
	 * 		Approximate size of the cache (in terms of memory).
	 */
	public double getSize()
	{	return size;		
	}
	
	/**
	 * Updates the current size of the cache.
	 * 
	 * @param delta
	 * 		Change in cache size (in terms of memory).
	 */
	public void changeSize(double delta)
	{	size = size + delta;
	}
}
