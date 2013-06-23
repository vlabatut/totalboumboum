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
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.engine.content.sprite.SpritePreview;
import org.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ProfileLoader
{	
	public static List<Profile> loadProfiles(ProfilesSelection profilesSelection) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	List<Profile> result = new ArrayList<Profile>();
		int size = profilesSelection.getProfileCount();
		for(int i=0;i<size;i++)
		{	// profile
			String id = profilesSelection.getId(i);
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

	public static HashMap<String,Profile> loadProfiles(List<String> playersIds) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	HashMap<String,Profile> result = new HashMap<String, Profile>();
		for(String playerId: playersIds)
		{	Profile profile = loadProfile(playerId);
			result.put(playerId,profile);
		}
		return result;
	}
	
	public static Profile loadProfile(String id) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String profilesFolder = FilePaths.getProfilesPath();
		File dataFile = new File(profilesFolder+File.separator+id+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PROFILE+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		Profile result = new Profile();
		result.setId(id);
		loadProfileElement(root,result);
		return result;
	}
	
	private static void loadProfileElement(Element root, Profile result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// general properties
    	Element generalElt = root.getChild(XmlNames.GENERAL);
		loadGeneralElement(generalElt,result);
		
		// artificial intelligence
		Element aiElt = root.getChild(XmlNames.AI);
		if(aiElt!=null)
			loadAiElement(aiElt,result);
		
		// sprite info
		Element characterElt = root.getChild(XmlNames.CHARACTER);
		loadSpriteElement(characterElt,result);
		
		// network stuff
		Element networkElt = root.getChild(XmlNames.NETWORK);
		loadNetworkElement(networkElt,result); 
	}
    
    private static void loadGeneralElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	result.setName(name);
    }
    
    private static void loadAiElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	result.setAiName(name.trim());
    	
    	// pack
    	String packname = root.getAttribute(XmlNames.PACK).getValue();
    	result.setAiPackname(packname.trim());
    }
    
    private static void loadSpriteElement(Element root, Profile result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// packname
    	String spritePackname = root.getAttribute(XmlNames.PACKNAME).getValue();
    	result.getDefaultSprite().setPack(spritePackname);
    	
    	// folder
    	String spriteFolder = root.getAttribute(XmlNames.NAME).getValue();
    	result.getDefaultSprite().setFolder(spriteFolder);
    	
    	// name
    	SpritePreview heroPreview = new SpritePreview();
		heroPreview = SpritePreviewLoader.loadHeroPreviewOnlyName(spritePackname,spriteFolder);
		String spriteName = heroPreview.getName();
		result.getDefaultSprite().setName(spriteName);
    	
		// color
		String spriteDefaultColorStr = root.getAttribute(XmlNames.COLOR).getValue().trim().toUpperCase(Locale.ENGLISH);
    	PredefinedColor spriteDefaultColor = PredefinedColor.valueOf(spriteDefaultColorStr);
    	result.getDefaultSprite().setColor(spriteDefaultColor);
		
    	// portraits
    	PredefinedColor spriteColor = result.getSpriteColor();
    	String packName = result.getSpritePack();
    	String folderName = result.getSpriteFolder();
		loadPortraits(result,packName,folderName,spriteColor);
    }	        

    private static void loadNetworkElement(Element root, Profile result)
    {	// last host
    	String lastHost = root.getAttribute(XmlNames.LAST_HOST).getValue();
    	result.setLastHost(lastHost.trim());
    }
    
    public static void reloadPortraits(Profile profile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String spritePackname = profile.getSpritePack();
		String spriteFoldername = profile.getSpriteFolder();
		PredefinedColor spriteColor = profile.getSpriteColor();
		loadPortraits(profile,spritePackname,spriteFoldername,spriteColor);
    }
    
    private static void loadPortraits(Profile profile, String spritePackname, String spriteFoldername, PredefinedColor spriteColor) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String folder = FilePaths.getHeroesPath() + File.separator + spritePackname;
		folder = folder + File.separator + spriteFoldername;
		Portraits portraits = PortraitsLoader.loadPortraits(folder,spriteColor);
		profile.setPortraits(portraits);
    }
    
    public static List<String> getIdsList()
    {	List<String> result = new ArrayList<String>();
    	
    	// get folder
    	String folderStr = FilePaths.getProfilesPath();
		File folder = new File(folderStr);
		FileFilter filter = new FileFilter()
		{	@Override
			public boolean accept(File pathname)
			{	String name = pathname.getName();
				int length = name.length();
				int extLength = FileNames.EXTENSION_XML.length();
				String ext = name.substring(length-extLength,length);
				return ext.equalsIgnoreCase(FileNames.EXTENSION_XML);
			}			
		};
		
		// get files
		File[] files = folder.listFiles(filter);
		for(File file: files)
		{	int length = file.getName().length();
			int extLength = FileNames.EXTENSION_XML.length();
			String id = file.getName().substring(0,length-extLength);
			//int id = Integer.parseInt(idStr);
			result.add(id);
		}
    
    	return result;
    }
}
