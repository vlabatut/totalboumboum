package org.totalboumboum.configuration.engine;

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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.gesture.anime.Colormap;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.images.ImageCache;

public class EngineConfiguration
{
	public EngineConfiguration copy()
	{	EngineConfiguration result = new EngineConfiguration();
	
		result.setAutoFps(autoFps);
		result.setFps(fps); 
		result.setSpeedCoeff(speedCoeff);
		
		result.setLogControls(logControls); 
		result.setLogControlsSeparately(logControlsSeparately);
		
		result.setMemoryCached(memoryCached);
		result.setMemoryCacheLimit(memoryCacheLimit);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TIMING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean autoFps;
	/** frames per second */
	private int fps;
	/** engine update persiod in milliseconds */
	private long milliPeriod;
	/** engine update persiod in nanoseconds */
	private long nanoPeriod;
	/** speed coefficient */
	private double speedCoeff; //NOTE speedcoeff à descendre au niveau de loop, car il peut dépendre du level

	public void setAutoFps(boolean autoFps)
	{	this.autoFps = autoFps;		
	}
	public boolean getAutoFps()
	{	return autoFps;		
	}
	
	public void setFps(int fps)
	{	this.fps = fps;
		milliPeriod = (long) (1000.0 / fps);
		nanoPeriod = milliPeriod * 1000000L;
	}
	public int getFps()
	{	return fps;
	}
	public long getMilliPeriod()
	{	return milliPeriod;
	}
	public long getNanoPeriod()
	{	return nanoPeriod;
	}
	
	public double getSpeedCoeff()
	{	return speedCoeff;
	}
	public void setSpeedCoeff(double speedCoeff)
	{	this.speedCoeff = speedCoeff;
	}

	/////////////////////////////////////////////////////////////////
	// SPRITE CACHE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean fileCached = false;
	private boolean memoryCached = false;
	private HashMap<String,Cachable> cache = new HashMap<String,Cachable>();
	private LinkedList<String> cacheNames = new LinkedList<String>();
	private long memoryCacheLimit = 250; // expressed in number of sprites
	
	public boolean getFileCached()
	{	return fileCached;
	}
	public void setFileCached(boolean fileCached)
	{	this.fileCached = fileCached;
	}

	public boolean getMemoryCached()
	{	return memoryCached;
	}
	public void setMemoryCached(boolean memoryCached)
	{	this.memoryCached = memoryCached;
	}
	
	public void addToMemoryCache(String key, Cachable value)
	{	// possibly erase some existing objects
		while(cache.size()>memoryCacheLimit)
		{	// erase older object
			String older = cacheNames.poll();
			cache.remove(older);
		}

		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 

		// put new object
		cache.put(key,value);
		cacheNames.offer(key);
	}
	
	public Cachable getFromMemoryCache(String key)
	{	return cache.get(key);	
	}
	
	public void clearMemoryCache()
	{	// erase all objects
		cache.clear();
		cacheNames.clear();
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	public long getMemoryCacheLimit()
	{	return memoryCacheLimit;
	}
	public void setMemoryCacheLimit(long memoryCacheLimit)
	{	this.memoryCacheLimit = memoryCacheLimit;
	}

	/////////////////////////////////////////////////////////////////
	// IMAGE CACHE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean imageCached = true;
	private long imageCacheLimit = 128*1024*1024;
	private ImageCache imageCache = new ImageCache();
	
	public boolean isImageCached()
	{	return imageCached;
	}
	public void setImageCached(boolean imageCached)
	{	this.imageCached = imageCached;
	}

	public void addToImageCache(String imgPath, Colormap colormap)
	{	imageCache.addImage(imgPath, colormap);
	}

	/** 
	 * note: the color parameter only indicates the use of a colormap. 
	 * it should be null if no colormap is used, even in the case of a colored sprite
	 */
	public BufferedImage retrieveFromImageCache(String imgPath, PredefinedColor color, double zoom) throws IOException
    {	return imageCache.retrieveImage(imgPath,color,zoom);
    }
    
    public void clearImageCache()
    {	imageCache.clear();
    }
    public void cleanImageCache()
    {	imageCache.clean();
    }

	public long getImageCacheLimit()
	{	return imageCacheLimit;
	}
	public void setImageCacheLimit(long imageCacheLimit)
	{	this.imageCacheLimit = imageCacheLimit;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS LOG		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean logControls = false;
	private boolean logControlsSeparately = false;
	private OutputStream controlsLogStream;

	public boolean getLogControls()
	{	return logControls;
	}
	
	public void setLogControls(boolean logControls)
	{	this.logControls = logControls;
	}
	
	public void setLogControlsSeparately(boolean logControlsSeparately)
	{	this.logControlsSeparately = logControlsSeparately;
	}
	
	public void initControlsLogStream() throws FileNotFoundException
	{	// init path
		String path = FilePaths.getLogsPath()+File.separator;
		// put the date
		if(logControlsSeparately)
			path = path + FileTools.getFilenameCompatibleCurrentTime() + "_";
		// put name and extension
		path = path + FileNames.FILE_CONTROLS + FileNames.EXTENSION_LOG;
		// open file
		File logFile = new File(path);
		if(logFile.exists())
			logFile.delete();
		FileOutputStream fileOut = new FileOutputStream(logFile);
		controlsLogStream = new BufferedOutputStream(fileOut);
	}
	
	public void closeControlsLogStream() throws IOException
	{	controlsLogStream.close();		
	}
	
	public OutputStream getControlsLogOutput()
	{	return controlsLogStream;
	}
}
