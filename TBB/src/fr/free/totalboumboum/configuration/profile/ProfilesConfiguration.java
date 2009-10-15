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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.statistics.GameStatistics;
import fr.free.totalboumboum.game.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.tools.FileTools;

public class ProfilesConfiguration
{
	public ProfilesConfiguration copy()
	{	ProfilesConfiguration result = new ProfilesConfiguration();
		// loaded profiles
		Iterator<Entry<String,String>> it = profiles.entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,String> entry = it.next();
			String value = entry.getValue();
			String key = entry.getKey();
			result.addProfile(key,value);			
		}
		//
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,String> profiles = new HashMap<String,String>();
	private int lastProfileIndex = 0;

	public HashMap<String,String> getProfiles()
	{	return profiles;	
	}
	
	public void addProfile(String file, String name)
	{	profiles.put(file,name);
	}
	
	public void removeProfile(int id)
	{	profiles.remove(id);		
	}
	
	public int getLastProfileIndex()
	{	return lastProfileIndex;
	}

	public void setLastProfileIndex(int lastProfileIndex)
	{	this.lastProfileIndex = lastProfileIndex;
	}
	
	public String createProfile(String name) throws IOException, ParserConfigurationException, SAXException
	{	// refresh counter
		int lastProfile = getLastProfileIndex();
		int nextProfile = lastProfile+1;
		setLastProfileIndex(nextProfile);
		
		// create profile
		Profile newProfile = new Profile();
		newProfile.setName(name);
		SpriteInfo spriteInfo = newProfile.getDefaultSprite();
		String spritePack = "superbomberman1";
		spriteInfo.setPack(spritePack);
		String spriteFolder = "shirobon";
		spriteInfo.setFolder(spriteFolder);
		PredefinedColor spriteColor = PredefinedColor.WHITE;
		spriteInfo.setColor(spriteColor);
		
		// create file
		String fileName = Integer.toString(nextProfile)/*+FileTools.EXTENSION_DATA*/;			
		ProfileSaver.saveProfile(newProfile, fileName);
		
		// add/save in config
		addProfile(fileName,name);
		ProfilesConfigurationSaver.saveProfilesConfiguration(this);
		
		// register in ranking service
		RankingService rankingService = GameStatistics.getRankingService();
		rankingService.registerPlayer(nextProfile);
		
		return fileName;
	}
	
	public void deleteProfile(Profile profile) throws ParserConfigurationException, SAXException, IOException
	{	// delete file
		int id = profile.getId();
		String path = FileTools.getProfilesPath()+File.separator+id+FileTools.EXTENSION_XML;
		File file = new File(path);
		file.delete();
		
		// delete entry in config
		removeProfile(id);
		ProfilesConfigurationSaver.saveProfilesConfiguration(this);
		
		// remove from ranking service
		RankingService rankingService = GameStatistics.getRankingService();
		rankingService.deregisterPlayer(id);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getNextFreeControls(ArrayList<Profile> profiles, int start)
	{	/// init
		Iterator<Profile> it = profiles.iterator();
		ArrayList<Integer> occupied = new ArrayList<Integer>();
		while(it.hasNext())
		{	Profile profile = it.next();
			int index = profile.getControlSettingsIndex();
			if(index>0)
				occupied.add(index);
		}
		// next free index
		boolean found = false;
		int result = 0;
		int test = 1;
		while(!found && test<=GameData.CONTROL_COUNT)
		{	int temp = (start+test)%(GameData.CONTROL_COUNT+1);
			if(occupied.contains(temp))
				test++;
			else
			{	result = temp;
				found = true;
			}
		}
		if(!found)
			result = start;
		return result;
	}

	public PredefinedColor getNextFreeColor(ArrayList<Profile> profiles, Profile profile, PredefinedColor color)
	{	PredefinedColor result = null;
		// used colors
		ArrayList<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteColor();
			usedColors.add(clr);
		}
		// preferred colors
		ArrayList<PredefinedColor> preferredColors = new ArrayList<PredefinedColor>();
		for(PredefinedColor c: PredefinedColor.values())
		{	if(c==color || (!usedColors.contains(c) && !preferredColors.contains(c)))
				preferredColors.add(c);
		}
		// select a color
		int currentColorIndex = preferredColors.indexOf(color);
		int index = (currentColorIndex+1) % preferredColors.size();
		if(index<preferredColors.size())
			result = preferredColors.get(index);
		return result;
	}

	public boolean isFreeColor(ArrayList<Profile> profiles, PredefinedColor color)
	{	// used colors
		ArrayList<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteColor();
			usedColors.add(clr);
		}
		boolean result = !usedColors.contains(color);
		return result;
	}
	
	public static ProfilesSelection getSelection(ArrayList<Profile> profiles)
	{	ProfilesSelection result = new ProfilesSelection();
		for(Profile p: profiles)
		{	int id = p.getId();
			PredefinedColor color = p.getSpriteColor();
			int controlsIndex = p.getControlSettingsIndex();
			String[] hero = {p.getSpritePack(),p.getSpriteFolder()};
			result.addProfile(id,color,controlsIndex,hero);			
		}
		return result;
	}
}
