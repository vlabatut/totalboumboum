package org.totalboumboum.game.profile;

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

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;

/**
 * Loads all selected profiles to XML files.
 * 
 * @author Vincent Labatut
 */
public class ProfileSaver
{	
	/**
	 * Records the specified profile.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @param id
	 * 		Profile id.
	 * 
	 * @throws IOException
	 * 		Problem while accessing a file.
	 */
	public static void saveProfile(Profile profile, String id) throws IOException
	{	// build document
		Element root = saveProfileElement(profile);
		
		// save file
		String file = FilePaths.getProfilesPath()+File.separator+id+FileNames.EXTENSION_XML;
		File dataFile = new File(file);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PROFILE+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	/**
	 * Processes the main profile element of the XML file.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @return
	 * 		Produced XML element.
	 */
	private static Element saveProfileElement(Profile profile)
	{	Element result = new Element(XmlNames.PROFILE);
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

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
		
		// network stuff
		Element networkElement = saveNetworkElement(profile);
		result.addContent(networkElement);

		return result;
	}
	
	/**
	 * Processes the element of the XML file
	 * representing the profile general info.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @return
	 * 		Produced XML element.
	 */
	private static Element saveGeneralElement(Profile profile)
	{	Element result = new Element(XmlNames.GENERAL);
		String name = profile.getName();
		result.setAttribute(XmlNames.NAME,name);
		return result;
	}

	/**
	 * Processes the element of the XML file
	 * representing the profile ai info.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @return
	 * 		Produced XML element.
	 */
	private static Element saveAiElement(Profile profile)
	{	Element result = new Element(XmlNames.AI);
		// name
		String name = profile.getAiName();
		result.setAttribute(XmlNames.NAME,name);
		// pack
		String packname = profile.getAiPackname();
		result.setAttribute(XmlNames.PACK,packname);
		//
		return result;
	}
	
	/**
	 * Processes the element of the XML file
	 * representing the profile sprite info.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @return
	 * 		Produced XML element.
	 */
	private static Element saveCharacterElement(Profile profile)
	{	Element result = new Element(XmlNames.CHARACTER);
		// name
		String name = profile.getSpriteFolder();
		result.setAttribute(XmlNames.NAME,name);
		// pack
		String packname = profile.getSpritePack();
		result.setAttribute(XmlNames.PACKNAME,packname);
		// colors
		String defaultColor = profile.getDefaultSprite().getColor().toString();
		result.setAttribute(XmlNames.COLOR,defaultColor);
		//
		return result;
	}

	/**
	 * Processes the element of the XML file
	 * representing the profile network info.
	 * 
	 * @param profile
	 * 		Profile object.
	 * @return
	 * 		Produced XML element.
	 */
	private static Element saveNetworkElement(Profile profile)
	{	Element result = new Element(XmlNames.NETWORK);
		String lastHost = profile.getLastHost();
		result.setAttribute(XmlNames.LAST_HOST,lastHost);
		return result;
	}
}
