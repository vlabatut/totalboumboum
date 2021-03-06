package org.totalboumboum.game.profile;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
 * Loads all selected profiles from XML files.
 * 
 * @author Vincent Labatut
 */
public class ProfileLoader
{	
	/**
	 * Loads all selected profiles.
	 * 
	 * @param profilesSelection
	 * 		Selected profile ids.
	 * @return
	 * 		Loaded profiles
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading a profile file.
	 * @throws SecurityException
	 * 		Problem while loading a profile file.
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile file.
	 * @throws SAXException
	 * 		Problem while loading a profile file.
	 * @throws IOException
	 * 		Problem while loading a profile file.
	 * @throws IllegalAccessException
	 * 		Problem while loading a profile file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile file.
	 */
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

	/**
	 * Loading a profile list.
	 * 
	 * @param playersIds
	 * 		Ids of the concerned profiles.
	 * @return
	 * 		Objects representing the profiles.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading a profile file.
	 * @throws SecurityException
	 * 		Problem while loading a profile file.
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile file.
	 * @throws SAXException
	 * 		Problem while loading a profile file.
	 * @throws IOException
	 * 		Problem while loading a profile file.
	 * @throws IllegalAccessException
	 * 		Problem while loading a profile file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile file.
	 */
	public static HashMap<String,Profile> loadProfiles(List<String> playersIds) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	HashMap<String,Profile> result = new HashMap<String, Profile>();
		for(String playerId: playersIds)
		{	Profile profile = loadProfile(playerId);
			result.put(playerId,profile);
		}
		return result;
	}
	
	/**
	 * Loads a single profile.
	 * 
	 * @param id
	 * 		Profile id.
	 * @return
	 * 		Object representing the profile.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile file.
	 * @throws SAXException
	 * 		Problem while loading a profile file.
	 * @throws IOException
	 * 		Problem while loading a profile file.
	 * @throws IllegalArgumentException
	 * 		Problem while loading a profile file.
	 * @throws SecurityException
	 * 		Problem while loading a profile file.
	 * @throws IllegalAccessException
	 * 		Problem while loading a profile file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile file.
	 */
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
	
	/**
	 * Processes the main element of a profile XML file.
	 * 
	 * @param root
	 * 		Main element.
	 * @param result
	 * 		Profile to be completed.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading a profile file.
	 * @throws SecurityException
	 * 		Problem while loading a profile file.
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile file.
	 * @throws SAXException
	 * 		Problem while loading a profile file.
	 * @throws IOException
	 * 		Problem while loading a profile file.
	 * @throws IllegalAccessException
	 * 		Problem while loading a profile file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile file.
	 */
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
    
	/**
	 * Loads the profile general info.
	 * 
	 * @param root
	 * 		Element containing the general info.
	 * @param result
	 * 		Profile to be completed.
	 */
    private static void loadGeneralElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	result.setName(name);
    }
    
	/**
	 * Loads the profile ai info.
	 * 
	 * @param root
	 * 		Element containing the ai info.
	 * @param result
	 * 		Profile to be completed.
	 */
    private static void loadAiElement(Element root, Profile result)
    {	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue();
    	result.setAiName(name.trim());
    	
    	// pack
    	String packname = root.getAttribute(XmlNames.PACK).getValue();
    	result.setAiPackname(packname.trim());
    }
    
	/**
	 * Loads the profile sprite info.
	 * 
	 * @param root
	 * 		Element containing the sprite info.
	 * @param result
	 * 		Profile to be completed.
	 * 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile file.
	 * @throws SAXException 
	 * 		Problem while loading a profile file.
	 * @throws IOException 
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile file.
	 */
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

	/**
	 * Loads the profile network info.
	 * 
	 * @param root
	 * 		Element containing the network info.
	 * @param result
	 * 		Profile to be completed.
	 */
   private static void loadNetworkElement(Element root, Profile result)
    {	// last host
    	String lastHost = root.getAttribute(XmlNames.LAST_HOST).getValue();
    	result.setLastHost(lastHost.trim());
    }
    
	/**
	 * Reloads the profile portraits
	 * 
	 * @param result
	 * 		Profile to be completed.
	 * 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile file.
	 * @throws SAXException 
	 * 		Problem while loading a profile file.
	 * @throws IOException 
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile file.
	 */
   public static void reloadPortraits(Profile result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String spritePackname = result.getSpritePack();
		String spriteFoldername = result.getSpriteFolder();
		PredefinedColor spriteColor = result.getSpriteColor();
		loadPortraits(result,spritePackname,spriteFoldername,spriteColor);
    }
    
	/**
	 * Loads the profile portraits.
	 * 
	 * @param result
	 * 		Profile to be completed.
	 * @param spritePackname
	 * 		Pack containing the sprite.
	 * @param spriteFoldername
	 * 		Folder containing the sprite files.
	 * @param spriteColor
	 * 		Color of the sprite.
	 * 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile file.
	 * @throws SAXException 
	 * 		Problem while loading a profile file.
	 * @throws IOException 
	 * 		Problem while loading a profile file.
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile file.
	 */
   private static void loadPortraits(Profile result, String spritePackname, String spriteFoldername, PredefinedColor spriteColor) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String folder = FilePaths.getHeroesPath() + File.separator + spritePackname;
		folder = folder + File.separator + spriteFoldername;
		Portraits portraits = PortraitsLoader.loadPortraits(folder,spriteColor);
		result.setPortraits(portraits);
    }
    
   /**
    * Returns the list of all currently existing profile ids in the game.
    * 
    * @return
    * 		List of profile ids.
    */
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
