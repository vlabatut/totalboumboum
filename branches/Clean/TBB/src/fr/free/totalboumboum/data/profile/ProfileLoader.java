package fr.free.totalboumboum.data.profile;

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

public class ProfileLoader
{	
	public static Profile loadProfile(String name) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Profile result;
		String profilesFolder = FileTools.getProfilesPath();
		String individualFolder = profilesFolder+File.separator+name;
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		result = loadProfileElement(root,individualFolder);
		return result;
	}
	
	private static Profile loadProfileElement(Element root,String individualFolder) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Profile result = new Profile();
    	// general properties
    	Element general = root.getChild(XmlTools.ELT_GENERAL);
		loadGeneralElement(general,result);
		// artificial intelligence
		Element ai = root.getChild(XmlTools.ELT_AI);
		if(ai!=null)
			loadAiElement(ai,result);
		// sprite info
		Element character = root.getChild(XmlTools.ELT_CHARACTER);
		loadSpriteElement(character,result);
		// controls file
		String controlFile = individualFolder+File.separator+FileTools.FILE_CONTROLS+FileTools.EXTENSION_DATA;
		ControlSettings controlSettings = ControlSettingsLoader.loadControlSettings(controlFile);
		result.setControlSettings(controlSettings);
		//
		return result;
	}
    
    private static void loadGeneralElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.setName(name);
    }
    
    private static void loadAiElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.setAiName(name.trim());
    	// pack
    	String packname = root.getAttribute(XmlTools.ATT_PACK).getValue();
    	result.setAiPackname(packname.trim());
    }
    
    private static void loadSpriteElement(Element root, Profile result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// packname
    	String spritePackname = root.getAttribute(XmlTools.ATT_PACKNAME).getValue();
    	result.setSpritePack(spritePackname);
    	// name
    	String spriteName = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.setSpriteName(spriteName);
    	// color
    	String spriteColorStr = root.getAttribute(XmlTools.ATT_COLOR).getValue().trim().toUpperCase();
    	PredefinedColor spriteColor = PredefinedColor.valueOf(spriteColorStr);
    	result.setSpriteColor(spriteColor);
    	// portraits
    	String folder = FileTools.getHeroesPath() + File.separator + spritePackname;
    	folder = folder + File.separator + spriteName;
    	Portraits portraits = PortraitsLoader.loadPortraits(folder,spriteColor);
    	result.setPortraits(portraits);
    }	        
}
