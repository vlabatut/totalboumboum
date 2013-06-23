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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class EngineConfigurationLoader
{	
	public static EngineConfiguration loadEngineConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	EngineConfiguration result = new EngineConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_ENGINE+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ENGINE+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadEngineElement(root,result);
		return result;
	}

	private static void loadEngineElement(Element root, EngineConfiguration result)
	{	Element element; 
		// timing
		element = root.getChild(XmlNames.TIMING);
		loadTimingElement(element,result);
		
		// logs
		element = root.getChild(XmlNames.LOG);
		loadLogElement(element,result);
		
		// cache
		element = root.getChild(XmlNames.CACHE);
		loadCacheElement(element,result);
	}
	
	private static void loadTimingElement(Element root, EngineConfiguration result)
	{	// auto fps
		String autoFpsStr = root.getAttribute(XmlNames.ADJUST).getValue().trim();
		boolean autoFps = Boolean.valueOf(autoFpsStr);
		result.setAutoFps(autoFps);

		// fps
		String fpsStr = root.getAttribute(XmlNames.FPS).getValue().trim();
		int fps = Integer.valueOf(fpsStr);
		result.setFps(fps);

		// speed
		String speedStr = root.getAttribute(XmlNames.SPEED).getValue().trim();
		float speed = Float.valueOf(speedStr);
		result.setSpeedCoeff(speed);
	}
	
	private static void loadLogElement(Element root, EngineConfiguration result)
	{	// controls
		String controlsStr = root.getAttribute(XmlNames.CONTROLS).getValue().trim();
		boolean controls = Boolean.valueOf(controlsStr);
		result.setLogControls(controls);
	}

	private static void loadCacheElement(Element root, EngineConfiguration result)
	{	// cache
		String cacheStr = root.getAttribute(XmlNames.CACHE).getValue().trim();
		boolean cache = Boolean.valueOf(cacheStr);
		result.setSpriteMemoryCached(cache);

		// cache limit
		String cacheLimitStr = root.getAttribute(XmlNames.CACHE_LIMIT).getValue().trim();
		long cacheLimit = Long.valueOf(cacheLimitStr);
		result.setSpriteCacheLimit(cacheLimit);
	}
}
