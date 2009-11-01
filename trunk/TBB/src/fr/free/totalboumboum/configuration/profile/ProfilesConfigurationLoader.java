package fr.free.totalboumboum.configuration.profile;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

public class ProfilesConfigurationLoader
{	
	public static ProfilesConfiguration loadProfilesConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	ProfilesConfiguration result = new ProfilesConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadProfilesElement(root,result);
		return result;
	}

	private static void loadProfilesElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// general
		Element generalElement = root.getChild(XmlTools.GENERAL);
		loadGeneralElement(generalElement,result);
		// list
		Element listElement = root.getChild(XmlTools.LIST);
		loadListElement(listElement,result);
	}

	@SuppressWarnings("unchecked")
	private static void loadListElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlTools.PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp,result);
		}
	}
		
	private static void loadProfileElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String file = root.getAttribute(XmlTools.FILE).getValue().trim();
		String name = root.getAttribute(XmlTools.NAME).getValue().trim();
		result.addProfile(file,name);	
	}

	private static void loadGeneralElement(Element root, ProfilesConfiguration result)
	{	String lastProfileStr = root.getAttribute(XmlTools.LAST).getValue().trim();
		int lastProfile = Integer.parseInt(lastProfileStr);
		result.setLastProfileIndex(lastProfile);
	}
}
