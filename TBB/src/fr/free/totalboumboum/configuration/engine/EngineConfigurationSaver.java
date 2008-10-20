package fr.free.totalboumboum.configuration.engine;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class EngineConfigurationSaver
{	
	public static void saveEngineConfiguration(EngineConfiguration engineConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveEngineElement(engineConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveEngineElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.ELT_ENGINE); 
		// fps
		Element fpsElement = saveFpsElement(engineConfiguration);
		result.addContent(fpsElement);
		// speed
		Element speedElement = saveSpeedElement(engineConfiguration);
		result.addContent(speedElement);
		//
		return result;
	}
	
	private static Element saveFpsElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.ELT_FPS);
		String fps = Integer.toString(engineConfiguration.getFps());
		result.setAttribute(XmlTools.ATT_VALUE,fps);
		return result;
	}
	
	private static Element saveSpeedElement(EngineConfiguration engineConfiguration)
	{	Element result = new Element(XmlTools.ELT_SPEED);
		String fps = Double.toString(engineConfiguration.getSpeedCoeff());
		result.setAttribute(XmlTools.ATT_VALUE,fps);
		return result;
	}
}
