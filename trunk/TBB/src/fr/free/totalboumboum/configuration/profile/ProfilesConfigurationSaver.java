package fr.free.totalboumboum.configuration.profile;

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
import java.util.ArrayList;
import java.util.Iterator;

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
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveProfilesElement(ProfilesConfiguration profilesConfiguration)
	{	Element result = new Element(XmlTools.ELT_PROFILES); 
		ArrayList<String> profiles = profilesConfiguration.getProfiles();
		Iterator<String> it = profiles.iterator();
		while(it.hasNext())
		{	String profile = it.next();
			Element element = new Element(XmlTools.ELT_PROFILE);
			result.setAttribute(XmlTools.ATT_VALUE,profile);
			result.addContent(element);
		}
		return result;
	}	
}
