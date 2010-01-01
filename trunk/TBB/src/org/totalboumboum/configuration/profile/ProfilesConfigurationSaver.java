package org.totalboumboum.configuration.profile;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class ProfilesConfigurationSaver
{	
	public static void saveProfilesConfiguration(ProfilesConfiguration profilesConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveProfilesElement(profilesConfiguration);	
		
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveProfilesElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.PROFILES);
		
		// general
		Element generalElement = saveGeneralElement(profilesConfiguration);
		result.addContent(generalElement);
		
		// list
		Element listElement = saveListElement(profilesConfiguration);
		result.addContent(listElement);

		return result;		
	}
	
	private static Element saveListElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.LIST);
		HashMap<Integer,String> profiles = profilesConfiguration.getProfiles();
		Iterator<Entry<Integer,String>> it = profiles.entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,String> entry = it.next();
			String idStr = entry.getKey().toString();
			String name = entry.getValue();
			Element element = new Element(XmlTools.PROFILE);
			element.setAttribute(XmlTools.FILE,idStr);
			element.setAttribute(XmlTools.NAME,name);
			result.addContent(element);
		}
		return result;
	}	

	private static Element saveGeneralElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.GENERAL);
		String lastProfile = Integer.toString(profilesConfiguration.getLastProfileIndex());
		result.setAttribute(XmlTools.LAST,lastProfile);
		return result;
	}
}
