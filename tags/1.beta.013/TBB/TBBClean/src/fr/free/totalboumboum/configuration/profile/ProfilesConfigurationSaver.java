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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

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
	{	Element result = new Element(XmlTools.ELT_PROFILES);
		
		// general
		Element generalElement = saveGeneralElement(profilesConfiguration);
		result.addContent(generalElement);
		
		// list
		Element listElement = saveListElement(profilesConfiguration);
		result.addContent(listElement);

		return result;		
	}
	
	private static Element saveListElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.ELT_LIST);
		HashMap<String,String> profiles = profilesConfiguration.getProfiles();
		Iterator<Entry<String,String>> it = profiles.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,String> entry = it.next();
			String file = entry.getKey();
			String name = entry.getValue();
			Element element = new Element(XmlTools.ELT_PROFILE);
			element.setAttribute(XmlTools.ATT_FILE,file);
			element.setAttribute(XmlTools.ATT_NAME,name);
			result.addContent(element);
		}
		return result;
	}	

	private static Element saveGeneralElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.ELT_GENERAL);
		String lastProfile = Integer.toString(profilesConfiguration.getLastProfileIndex());
		result.setAttribute(XmlTools.ATT_LAST,lastProfile);
		return result;
	}
}
