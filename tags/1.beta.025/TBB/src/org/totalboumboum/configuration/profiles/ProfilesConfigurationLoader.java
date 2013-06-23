package org.totalboumboum.configuration.profiles;

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
import java.util.Iterator;
import java.util.List;

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
public class ProfilesConfigurationLoader
{	
	public static ProfilesConfiguration loadProfilesConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	ProfilesConfiguration result = new ProfilesConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_PROFILES+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PROFILES+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadProfilesElement(root,result);
		return result;
	}

	private static void loadProfilesElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// general
		Element generalElement = root.getChild(XmlNames.GENERAL);
		loadGeneralElement(generalElement,result);
		// list
		Element listElement = root.getChild(XmlNames.LIST);
		loadListElement(listElement,result);
	}

	@SuppressWarnings("unchecked")
	private static void loadListElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlNames.PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp,result);
		}
	}
		
	private static void loadProfileElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String id = root.getAttribute(XmlNames.FILE).getValue().trim();
		//Integer id = Integer.parseInt(idStr);
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		result.addProfile(id,name);	
	}

	private static void loadGeneralElement(Element root, ProfilesConfiguration result)
	{	//String lastProfileStr = root.getAttribute(XmlNames.LAST).getValue().trim();
		//int lastProfile = Integer.parseInt(lastProfileStr);
		//result.setLastProfileIndex(lastProfile);
	}
}
