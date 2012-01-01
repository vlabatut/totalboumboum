package org.totalboumboum.configuration.profiles;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
public class ProfilesConfigurationSaver
{	
	public static void saveProfilesConfiguration(ProfilesConfiguration profilesConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveProfilesElement(profilesConfiguration);	
		
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_PROFILES+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PROFILES+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveProfilesElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlNames.PROFILES);
		
		// general
		Element generalElement = saveGeneralElement(profilesConfiguration);
		result.addContent(generalElement);
		
		// list
		Element listElement = saveListElement(profilesConfiguration);
		result.addContent(listElement);

		return result;		
	}
	
	private static Element saveListElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlNames.LIST);
		HashMap<String,String> profiles = profilesConfiguration.getProfiles();
		Iterator<Entry<String,String>> it = profiles.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,String> entry = it.next();
			String id = entry.getKey();
			String name = entry.getValue();
			Element element = new Element(XmlNames.PROFILE);
			element.setAttribute(XmlNames.FILE,id);
			element.setAttribute(XmlNames.NAME,name);
			result.addContent(element);
		}
		return result;
	}	

	private static Element saveGeneralElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlNames.GENERAL);
		//String lastProfile = Integer.toString(profilesConfiguration.getLastProfileIndex());
		//result.setAttribute(XmlNames.LAST,lastProfile);
		return result;
	}
}
