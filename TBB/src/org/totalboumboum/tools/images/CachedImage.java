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
import java.io.File;
import java.io.IOException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorMap;

public class CachedImage
{
	public CachedImage(ImageCache imageCache, String imagePath)
	{	this.imageCache = imageCache;
		this.imagePath = imagePath;
	}

	/////////////////////////////////////////////////////////////////
	// IMAGE CACHE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ImageCache imageCache = null;
		
	/////////////////////////////////////////////////////////////////
	// REGULAR IMAGE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BufferedImage regularImage = null; 
	private double regularImageSize = 0;
	
    /////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String imagePath = null;
	
//	public BufferedImage getImage(ColorMap colormap, double zoom) throws IOException
//	{	BufferedImage result;
//		
//		// no dim changes requested
//		if(zoom==1)
//		{	// no original image yet
//			if(regularImage==null)
//			{	regularImage = ImageTools.loadImage(imagePath,null);
//				File file = new File(imagePath);
//				regularImageSize = file.length();
//				imageCache.increaseSize(regularImageSize);
//			}
//			result = regularImage;
//		}
//		// dimension changes requested
//		else
//		{	// zoomed image not in cache yet 
//			if(zoomedImage==null || currentZoom!=zoom)
//			{	// get the regular image
//				BufferedImage baseImage = null;
//				double baseImageSize = 0;
//				// already loaded
//				if(regularImage!=null)
//				{	baseImage = regularImage;
//					baseImageSize = regularImageSize;
//				}
//				// must be loaded
//				else
//				{	// load
//					baseImage = ImageTools.loadImage(imagePath,null);
//					File file = new File(imagePath);
//					baseImageSize = file.length();
//					// cache (possibly)
//					if(Configuration.getEngineConfiguration().isImageCached())
//					{	regularImage = baseImage;
//						regularImageSize = baseImageSize;
//						imageCache.increaseSize(regularImageSize);
//					}
//				}
//				
//				// zoom the image
//				zoomedImage = ImageTools.resize(baseImage,zoom,Configuration.getVideoConfiguration().getSmoothGraphics());
//				currentZoom = zoom;
//				zoomedImageSize = baseImageSize*Math.pow(zoom,2);
//				imageCache.increaseSize(zoomedImageSize);
//			}
//			result = zoomedImage;
//		}
//				
//		// colormap processing
//		if(colormap!=null)
//			result = ImageTools.getColoredImage(result,colormap);
//	
//		return result;
//	}

	public BufferedImage getImage(ColorMap colormap, double zoom) throws IOException
	{	BufferedImage result;
		
		// get original image
		if(regularImage==null)
		{	
//			if(imagePath.equals("resources\\heroes\\nesbomberman2\\shirobon\\animes\\shadow.png"))
//			System.out.println();
			
			regularImage = ImageTools.loadImage(imagePath,null);
			File file = new File(imagePath);
			regularImageSize = file.length();
			imageCache.increaseSize(regularImageSize);
		}
		result = regularImage;
		
		// possibly change its color
		if(colormap!=null)
			result = ImageTools.getColoredImage(result,colormap);
		
		// optimize image
		result = ImageTools.getCompatibleImage(result);

		// possibly image size
		if(zoom!=1)
			result = ImageTools.getResizedImage(result,zoom,Configuration.getVideoConfiguration().getSmoothGraphics());
					
		return result;
	}

	public double getTotalSize()
	{	double result = regularImageSize;
		return result;
	}
}
