package org.totalboumboum.tools.images;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorFolder;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRule;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ImageCache
{	
	/////////////////////////////////////////////////////////////////
	// CACHED IMAGES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,CachedImage> imageMap = new HashMap<String, CachedImage>();

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
	
	public void changeSize(double delta)
	{	size = size + delta;
	}
}
