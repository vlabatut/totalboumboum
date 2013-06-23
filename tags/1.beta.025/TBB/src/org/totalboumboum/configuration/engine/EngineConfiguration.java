package org.totalboumboum.configuration.engine;

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
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;

import org.totalboumboum.engine.container.CachableSpriteContainer;
import org.totalboumboum.engine.content.feature.gesture.anime.color.ColorRule;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.images.ImageCache;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EngineConfiguration
{
	public EngineConfiguration copy()
	{	EngineConfiguration result = new EngineConfiguration();
	
		result.setAutoFps(autoFps);
		result.setFps(fps); 
		result.setSpeedCoeff(speedCoeff);
		
		result.setLogControls(logControls); 
		result.setLogControlsSeparately(logControlsSeparately);
		
		result.setSpriteMemoryCached(spriteMemoryCached);
		result.setSpriteCacheLimit(spriteCacheLimit);
		
		result.setRecordRounds(recordRounds);

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
	private boolean spriteFileCached = false;
	private boolean spriteMemoryCached = false;
	private HashMap<String,CachableSpriteContainer> spriteCache = new HashMap<String,CachableSpriteContainer>();
	private LinkedList<String> spriteCacheNames = new LinkedList<String>();
	private long spriteCacheLimit = 250; // expressed in number of sprites
	
	public boolean isSpriteFileCached()
	{	return spriteFileCached;
	}
	public void setSpriteFileCached(boolean spriteFileCached)
	{	this.spriteFileCached = spriteFileCached;
	}

	public boolean isSpriteMemoryCached()
	{	return spriteMemoryCached;
	}
	public void setSpriteMemoryCached(boolean spriteMemoryCached)
	{	this.spriteMemoryCached = spriteMemoryCached;
	}
	
	public void addToSpriteCache(String key, CachableSpriteContainer value)
	{	// possibly erase some existing objects
		while(spriteCache.size()>spriteCacheLimit)
		{	// erase older object
			String older = spriteCacheNames.poll();
			spriteCache.remove(older);
		}

		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 

		// put new object
		spriteCache.put(key,value);
		spriteCacheNames.offer(key);
	}
	
	public CachableSpriteContainer getFromSpriteCache(String key)
	{	return spriteCache.get(key);	
	}
	
	public void clearSpriteCache()
	{	// erase all objects
		spriteCache.clear();
		spriteCacheNames.clear();
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	public long getSpriteCacheLimit()
	{	return spriteCacheLimit;
	}
	public void setSpriteCacheLimit(long memoryCacheLimit)
	{	this.spriteCacheLimit = memoryCacheLimit;
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

	/** 
	 * retrieve an image for the cache, loading it if necessary,
	 * or processing it by resizing/coloring an existing neutral image.
	 * Note: the color parameter only indicates the use of a colormap, 
	 * it should be null if no colormap is used (even in the case of a colored sprite).
	 */
	public BufferedImage retrieveFromImageCache(String imagePath, ColorRule colorRule, double zoom) throws IOException
    {	return imageCache.retrieveImage(imagePath,colorRule,zoom);
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

	/////////////////////////////////////////////////////////////////
	// RECORD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean recordRounds = false;

	public boolean isRecordRounds()
	{	return recordRounds;
	}
	
	public void setRecordRounds(boolean recordRounds)
	{	this.recordRounds = recordRounds;
	}
}
