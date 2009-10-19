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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
	public static ArrayList<Profile> loadProfiles(ProfilesSelection profilesSelection) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	ArrayList<Profile> result = new ArrayList<Profile>();
		int size = profilesSelection.getProfileCount();
		for(int i=0;i<size;i++)
		{	// profile
			int id = profilesSelection.getIds(i);
			Profile profile = loadProfile(id);
			SpriteInfo selectedSprite = profile.getSelectedSprite();
			// sprite
			String packName = profilesSelection.getHero(i)[0];
			selectedSprite.setPack(packName);
			String folderName = profilesSelection.getHero(i)[1];
			selectedSprite.setFolder(folderName);
			SpritePreview heroPreview = SpritePreviewLoader.loadHeroPreviewOnlyName(packName,folderName);
			selectedSprite.setName(heroPreview.getName());
			// color
			selectedSprite.setColor(profilesSelection.getColor(i));
			// controls
			int controlsIndex = profilesSelection.getControlsIndex(i);
			profile.setControlSettingsIndex(controlsIndex);
			// result
			reloadPortraits(profile);
			result.add(profile);
		}
		return result;
	}

	public static HashMap<Integer,Profile> loadProfiles(List<Integer> playersIds) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	HashMap<Integer,Profile> result = new HashMap<Integer, Profile>();
		for(Integer playerId: playersIds)
		{	Profile profile = loadProfile(playerId);
			result.put(playerId,profile);
		}
		return result;
	}
	
	public static Profile loadProfile(int id) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String profilesFolder = FileTools.getProfilesPath();
		File dataFile = new File(profilesFolder+File.separator+id+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILE+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		Profile result = new Profile();
		result.setId(id);
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
    	result.getDefaultSprite().setPack(spritePackname);
    	
    	// folder
    	String spriteFolder = root.getAttribute(XmlTools.ATT_NAME).getValue();
    	result.getDefaultSprite().setFolder(spriteFolder);
    	
    	// name
    	SpritePreview heroPreview = new SpritePreview();
		heroPreview = SpritePreviewLoader.loadHeroPreviewOnlyName(spritePackname,spriteFolder);
		String spriteName = heroPreview.getName();
		result.getDefaultSprite().setName(spriteName);
    	
		// color
		String spriteDefaultColorStr = root.getAttribute(XmlTools.ATT_COLOR).getValue().trim().toUpperCase(Locale.ENGLISH);
    	PredefinedColor spriteDefaultColor = PredefinedColor.valueOf(spriteDefaultColorStr);
    	result.getDefaultSprite().setColor(spriteDefaultColor);
		
    	// portraits
    	PredefinedColor spriteColor = result.getSpriteColor();
    	String packName = result.getSpritePack();
    	String folderName = result.getSpriteFolder();
		loadPortraits(result,packName,folderName,spriteColor);
    }	        

    public static void reloadPortraits(Profile profile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String spritePackname = profile.getSpritePack();
		String spriteFoldername = profile.getSpriteFolder();
		PredefinedColor spriteColor = profile.getSpriteColor();
		loadPortraits(profile,spritePackname,spriteFoldername,spriteColor);
    }
    
    private static void loadPortraits(Profile profile, String spritePackname, String spriteFoldername, PredefinedColor spriteColor) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String folder = FileTools.getHeroesPath() + File.separator + spritePackname;
		folder = folder + File.separator + spriteFoldername;
		Portraits portraits = PortraitsLoader.loadPortraits(folder,spriteColor);
		profile.setPortraits(portraits);
    }
    
    public static List<Integer> getIdsList()
    {	ArrayList<Integer> result = new ArrayList<Integer>();
    	
    	// get folder
    	String folderStr = FileTools.getProfilesPath();
		File folder = new File(folderStr);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	String name = pathname.getName();
				int length = name.length();
				int extLength = FileTools.EXTENSION_XML.length();
				String ext = name.substring(length-extLength,length);
				return ext.equalsIgnoreCase(FileTools.EXTENSION_XML);
			}			
		};
		
		// get files
		File[] files = folder.listFiles(filter);
		for(File file: files)
		{	int length = file.getName().length();
			int extLength = FileTools.EXTENSION_XML.length();
			String idStr = file.getName().substring(0,length-extLength);
			int id = Integer.parseInt(idStr);
			result.add(id);
		}
    
    	return result;
    }
}
