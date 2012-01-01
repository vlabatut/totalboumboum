package org.totalboumboum.configuration.engine;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.jdom.Comment;
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
public class EngineConfigurationSaver
{	
	public static void saveEngineConfiguration(EngineConfiguration engineConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveEngineElement(engineConfiguration);	
		
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_ENGINE+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ENGINE+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveEngineElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlNames.ENGINE); 

		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

		// timing
		Element timingElement = saveTimingElement(engineConfiguration);
		result.addContent(timingElement);
		
		// logs
		Element logElement = saveLogElement(engineConfiguration);
		result.addContent(logElement);
		
		// cache
		Element cacheElement = saveCacheElement(engineConfiguration);
		result.addContent(cacheElement);
		
		return result;
	}
	
	private static Element saveTimingElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlNames.TIMING);
		
		// adjust
		String adjust = Boolean.toString(engineConfiguration.getAutoFps());
		result.setAttribute(XmlNames.ADJUST,adjust);
		
		// fps
		String fps = Integer.toString(engineConfiguration.getFps());
		result.setAttribute(XmlNames.FPS,fps);
		
		// speed
		String speed = Double.toString(engineConfiguration.getSpeedCoeff());
		result.setAttribute(XmlNames.SPEED,speed);
				
		return result;
	}

	private static Element saveLogElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlNames.LOG);
		
		// controls
		String controls = Boolean.toString(engineConfiguration.getLogControls());
		result.setAttribute(XmlNames.CONTROLS,controls);
				
		return result;
	}

	private static Element saveCacheElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlNames.CACHE);
		
		// cache
		String cache = Boolean.toString(engineConfiguration.isSpriteMemoryCached());
		result.setAttribute(XmlNames.CACHE,cache);
				
		// limit
		String cacheLimit = Long.toString(engineConfiguration.getSpriteCacheLimit());
		result.setAttribute(XmlNames.CACHE_LIMIT,cacheLimit);
				
		return result;
	}
}
