package fr.free.totalboumboum.configuration;

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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ConfigurationLoader
{	
	public static Configuration loadConfiguration() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	Configuration result = new Configuration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConfigurationElement(root);
		return result;
	}

	private static void loadConfigurationElement(Element root) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	Element element; 
		// profiles
		element = root.getChild(XmlTools.ELT_PROFILES);
		loadProfilesElement(element);
	}
		
	@SuppressWarnings("unchecked")
	private static void loadProfilesElement(Element root) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlTools.ELT_PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp);
		}
	}
	
	private static void loadProfileElement(Element root) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
//		Profile profile = ProfileLoader.loadProfile(value);			
//		result.addProfile(profile);
Configuration.addProfile(value);	
	}
}
