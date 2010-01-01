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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class EngineConfigurationSaver
{	
	public static void saveEngineConfiguration(EngineConfiguration engineConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveEngineElement(engineConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveEngineElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.ENGINE); 

		// timing
		Element timingElement = saveTimingElement(engineConfiguration);
		result.addContent(timingElement);
		
		// logs
		Element logElement = saveLogElement(engineConfiguration);
		result.addContent(logElement);
		
		return result;
	}
	
	private static Element saveTimingElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.TIMING);
		
		// adjust
		String adjust = Boolean.toString(engineConfiguration.getAutoFps());
		result.setAttribute(XmlTools.ADJUST,adjust);
		
		// fps
		String fps = Integer.toString(engineConfiguration.getFps());
		result.setAttribute(XmlTools.FPS,fps);
		
		// speed
		String speed = Double.toString(engineConfiguration.getSpeedCoeff());
		result.setAttribute(XmlTools.SPEED,speed);
				
		return result;
	}

	private static Element saveLogElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.LOG);
		
		// controls
		String controls = Boolean.toString(engineConfiguration.getLogControls());
		result.setAttribute(XmlTools.CONTROLS,controls);
				
		return result;
	}
}
