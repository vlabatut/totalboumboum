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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ProfileLoader
{	
	public static Profile loadProfile(String fileName) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String profilesFolder = FileTools.getProfilesPath();
		File dataFile = new File(profilesFolder+File.separator+fileName+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		Profile result = new Profile();
		result.setFileName(fileName);
		loadProfileElement(root,result);
		return result;
	}
	
	private static void loadProfileElement(Element root, Profile result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// general properties
    	Element general = root.getChild(XmlTools.ELT_GENERAL);
		loadGeneralElement(general,result);
		
		// artificial intelligence
		Element ai = root.getChild(XmlTools.ELT_AI);
		if(ai!=null)
			loadAiElement(ai,result);
		
		// colors
		Element colors = root.getChild(XmlTools.ELT_COLORS);
		loadColorsElement(colors,result);
		
		// sprite info
		Element character = root.getChild(XmlTools.ELT_CHARACTER);
		loadSpriteElement(character,result);
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
    	
    	// folder
    	String spriteFolder = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.setSpriteFolder(spriteFolder);
    	
    	// name
    	SpritePreview heroPreview = new SpritePreview();
		heroPreview = SpritePreviewLoader.loadHeroPreview(spritePackname,spriteFolder);
		String spriteName = heroPreview.getName();
		result.setSpriteName(spriteName);
    	
    	// portraits
    	PredefinedColor spriteColor = result.getSpriteSelectedColor();
		loadPortraits(result,spritePackname,spriteFolder,spriteColor);
    }	        

    @SuppressWarnings("unchecked")
	private static void loadColorsElement(Element root, Profile result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	List<Element> elements = root.getChildren(XmlTools.ATT_COLOR);
    	Iterator<Element> it = elements.iterator();
    	while(it.hasNext())
    	{	Element tempElt = it.next();
	    	// rank
    		String rankStr = tempElt.getAttributeValue(XmlTools.ATT_RANK);
    		int rank = Integer.parseInt(rankStr);
    		// color
    		String spriteColorStr = tempElt.getAttribute(XmlTools.ATT_NAME).getValue().trim().toUpperCase(Locale.ENGLISH);
	    	PredefinedColor spriteColor = PredefinedColor.valueOf(spriteColorStr);
	    	// add to profile
	    	result.setSpriteColor(spriteColor,rank);
    	}
    }
    
    public static void reloadPortraits(Profile profile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String spritePackname = profile.getSpritePack();
		String spriteFoldername = profile.getSpriteFolder();
		PredefinedColor spriteColor = profile.getSpriteSelectedColor();
		loadPortraits(profile,spritePackname,spriteFoldername,spriteColor);
    }
    
    private static void loadPortraits(Profile profile, String spritePackname, String spriteFoldername, PredefinedColor spriteColor) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String folder = FileTools.getHeroesPath() + File.separator + spritePackname;
		folder = folder + File.separator + spriteFoldername;
		Portraits portraits = PortraitsLoader.loadPortraits(folder,spriteColor);
		profile.setPortraits(portraits);
    }
}
