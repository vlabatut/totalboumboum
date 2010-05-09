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

public class EngineConfigurationLoader
{	
	public static EngineConfiguration loadEngineConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	EngineConfiguration result = new EngineConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ENGINE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadEngineElement(root,result);
		return result;
	}

	private static void loadEngineElement(Element root, EngineConfiguration result)
	{	Element element; 
		// auto fps
		element = root.getChild(XmlTools.ELT_ADJUST);
		loadAdjustElement(element,result);
		// fps
		element = root.getChild(XmlTools.ELT_FPS);
		loadFpsElement(element,result);
		// speed
		element = root.getChild(XmlTools.ELT_SPEED);
		loadSpeedElement(element,result);
	}
	
	private static void loadAdjustElement(Element root, EngineConfiguration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		boolean autoFps = Boolean.valueOf(value);
		result.setAutoFps(autoFps);
	}
	
	private static void loadFpsElement(Element root, EngineConfiguration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		int fps = Integer.valueOf(value);
		result.setFps(fps);
	}
	
	private static void loadSpeedElement(Element root, EngineConfiguration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		float speedCoeff = Float.valueOf(value);
		result.setSpeedCoeff(speedCoeff);
	}	
}
