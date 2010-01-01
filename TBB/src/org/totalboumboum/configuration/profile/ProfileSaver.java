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

import org.jdom.Element;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;


public class ProfileSaver
{	
	public static void saveProfile(Profile profile, Integer id) throws IOException
	{	// build document
		Element root = saveProfileElement(profile);	
		// save file
		String file = FileTools.getProfilesPath()+File.separator+Integer.toString(id)+FileTools.EXTENSION_XML;
		File dataFile = new File(file);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveProfileElement(Profile profile)
	{	Element result = new Element(XmlTools.PROFILE);
		
		// general properties
		Element generalElement = saveGeneralElement(profile);
		result.addContent(generalElement);
		
		// artificial intelligence
		if(profile.hasAi())
		{	Element aiElement = saveAiElement(profile);
			result.addContent(aiElement);
		}
		
		// sprite info
		Element characterElement = saveCharacterElement(profile);
		result.addContent(characterElement);
		
		return result;
	}
	
	private static Element saveGeneralElement(Profile profile)
	{	Element result = new Element(XmlTools.GENERAL);
		String name = profile.getName();
		result.setAttribute(XmlTools.NAME,name);
		return result;
	}

	private static Element saveAiElement(Profile profile)
	{	Element result = new Element(XmlTools.AI);
		// name
		String name = profile.getAiName();
		result.setAttribute(XmlTools.NAME,name);
		// pack
		String packname = profile.getAiPackname();
		result.setAttribute(XmlTools.PACK,packname);
		//
		return result;
	}
	
	private static Element saveCharacterElement(Profile profile)
	{	Element result = new Element(XmlTools.CHARACTER);
		// name
		String name = profile.getSpriteFolder();
		result.setAttribute(XmlTools.NAME,name);
		// pack
		String packname = profile.getSpritePack();
		result.setAttribute(XmlTools.PACKNAME,packname);
		// colors
		String defaultColor = profile.getDefaultSprite().getColor().toString();
		result.setAttribute(XmlTools.COLOR,defaultColor);
		//
		return result;
	}
}
