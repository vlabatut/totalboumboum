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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.files.FileTools;

public class EngineConfiguration
{
	public EngineConfiguration copy()
	{	EngineConfiguration result = new EngineConfiguration();
	
		result.setAutoFps(autoFps);
		result.setFps(fps); 
		result.setSpeedCoeff(speedCoeff);
		
		result.setLogControls(logControls); 
		result.setLogControlsSeparately(logControlsSeparately);
		
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
	// CACHING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean fileCache = false;
	private boolean memoryCache = false;
	private HashMap<String,Object> cache = new HashMap<String,Object>();
	
	public boolean getFileCache()
	{	return fileCache;
	}
	public void setFileCache(boolean fileCache)
	{	this.fileCache = fileCache;
	}

	public boolean getMemoryCache()
	{	return memoryCache;
	}
	public void setMemoryCache(boolean memoryCache)
	{	this.memoryCache = memoryCache;
	}
	public void addMemoryCache(String key, Object value)
	{	cache.put(key,value);		
	}
	public Object getMemoryCache(String key)
	{	return cache.get(key);	
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
