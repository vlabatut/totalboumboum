package org.totalboumboum.configuration.profiles;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfigurationSaver;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfigurationSaver;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.configuration.game.tournament.TournamentConfigurationSaver;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.profile.ProfileSaver;
import org.totalboumboum.game.profile.SpriteInfo;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.Match;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
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

	public HashMap<String,String> getProfiles()
	{	return profiles;	
	}
	
	public void addProfile(String id, String name)
	{	profiles.put(id,name);
	}
	
	public void removeProfile(String id)
	{	profiles.remove(id);
	}

	public String createProfile(String name) throws IOException, ParserConfigurationException, SAXException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// refresh counter
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();
		
		// create profile
		Profile newProfile = new Profile();
		String hostId = Configuration.getConnectionsConfiguration().getHostId();
		newProfile.setLastHost(hostId);
		newProfile.setName(name);
		SpriteInfo spriteInfo = newProfile.getDefaultSprite();
		String spritePack = "superbomberman1";
		spriteInfo.setPack(spritePack);
		String spriteFolder = "shirobon";
		spriteInfo.setFolder(spriteFolder);
		PredefinedColor spriteColor = PredefinedColor.WHITE;
		spriteInfo.setColor(spriteColor);
		
		// create file
		//Integer id = nextProfile/*+FileTools.EXTENSION_DATA*/;
		ProfileSaver.saveProfile(newProfile,id);
		
		// add/save in config
		addProfile(id,name);
		ProfilesConfigurationSaver.saveProfilesConfiguration(this);
		
		// register in stats
		GameStatistics.addPlayer(id);
		
		return id;
	}
	
	/**
	 * used during network game, to synch
	 * profiles across platforms
	 */
	public void insertProfile(Profile profile) throws IOException, ParserConfigurationException, SAXException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// create file
		String id = profile.getId();
		if(!profiles.containsKey(id))
		{	ProfileSaver.saveProfile(profile,id);
			
			// add/save in config
			addProfile(id,profile.getName());
			ProfilesConfigurationSaver.saveProfilesConfiguration(this);
			
			// register in stats
			GameStatistics.addPlayer(id);
		}
	}
	
	public void deleteProfile(Profile profile) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// delete file
		String id = profile.getId();
		String path = FilePaths.getProfilesPath()+File.separator+id+FileNames.EXTENSION_XML;
		File file = new File(path);
		file.delete();
		
		// delete entry in quickstart
		QuickStartConfiguration quickStartConfiguration = Configuration.getGameConfiguration().getQuickStartConfiguration();
		ProfilesSelection profilesSelection = quickStartConfiguration.getProfilesSelection();
		if(profilesSelection.containsProfile(id))
		{	profilesSelection.removeProfile(id);
			QuickStartConfigurationSaver.saveQuickStartConfiguration(quickStartConfiguration);
		}
		
		// delete entry in quickmatch
		QuickMatchConfiguration quickMatchConfiguration = Configuration.getGameConfiguration().getQuickMatchConfiguration();
		profilesSelection = quickMatchConfiguration.getProfilesSelection();
		if(profilesSelection.containsProfile(id))
		{	profilesSelection.removeProfile(id);
			QuickMatchConfigurationSaver.saveQuickMatchConfiguration(quickMatchConfiguration);
		}
		
		// delete entry in tournament
		TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration();
		profilesSelection = tournamentConfiguration.getProfilesSelection();
		if(profilesSelection.containsProfile(id))
		{	profilesSelection.removeProfile(id);
			TournamentConfigurationSaver.saveTournamentConfiguration(tournamentConfiguration);
		}
			
		// delete entry in profiles
		removeProfile(id);
		ProfilesConfigurationSaver.saveProfilesConfiguration(this);
		
		// remove from ranking service
		GameStatistics.deletePlayer(id);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getNextFreeControls(List<Profile> profiles, int start)
	{	/// init
		Iterator<Profile> it = profiles.iterator();
		List<Integer> occupied = new ArrayList<Integer>();
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

	public PredefinedColor getNextFreeColor(List<Profile> profiles, Profile profile, PredefinedColor color)
	{	PredefinedColor result = null;
		// used colors
		List<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteColor();
			usedColors.add(clr);
		}
		// preferred colors
		List<PredefinedColor> preferredColors = new ArrayList<PredefinedColor>();
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

	public boolean isFreeColor(List<Profile> profiles, PredefinedColor color)
	{	// used colors
		List<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteColor();
			usedColors.add(clr);
		}
		boolean result = !usedColors.contains(color);
		return result;
	}
	
	public static ProfilesSelection getSelection(List<Profile> profiles)
	{	ProfilesSelection result = new ProfilesSelection();
		for(Profile p: profiles)
		{	String id = p.getId();
			PredefinedColor color = p.getSpriteColor();
			int controlsIndex = p.getControlSettingsIndex();
			String[] hero = {p.getSpritePack(),p.getSpriteFolder()};
			result.addProfile(id,color,controlsIndex,hero);			
		}
		return result;
	}
	
	public static void randomlyCompleteProfiles(List<Profile> profiles, int number) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	// list of ids minus already selected players 
		List<String> playersIds = ProfileLoader.getIdsList();
		for(Profile profile: profiles)
		{	String playerId = profile.getId();
			playersIds.remove(playerId);
		}
		
		// randomly select players
		List<Profile> additionalProfiles = new ArrayList<Profile>();
		for(int i=profiles.size();i<number;i++)
		{	int index = (int)(Math.random()*playersIds.size());
			String playerId = playersIds.get(index);
			playersIds.remove(index);
			Profile profile = ProfileLoader.loadProfile(playerId);
			additionalProfiles.add(profile);
		}
		
		// add additional profiles the original selection
		addAllProfiles(profiles,additionalProfiles);
	}
	
	private static void addAllProfiles(List<Profile> profiles, List<Profile> additionalProfiles) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();

		// complete selection with free colored players
		Iterator<Profile> it = additionalProfiles.iterator();
		while(it.hasNext())
		{	Profile profile = it.next();
			// check if color is free
			PredefinedColor selectedColor = profile.getSpriteColor();
			if(profilesConfiguration.isFreeColor(profiles,selectedColor))
			{	profiles.add(profile);
				it.remove();
			}
		}

		// change color and add remaining players
		it = additionalProfiles.iterator();
		while(it.hasNext())
		{	Profile profile = it.next();
			// find another color
			PredefinedColor selectedColor = profile.getSpriteColor();
			while(!profilesConfiguration.isFreeColor(profiles,selectedColor))
				selectedColor = profilesConfiguration.getNextFreeColor(profiles,profile,selectedColor);
			profile.getSelectedSprite().setColor(selectedColor);
			ProfileLoader.reloadPortraits(profile);
			profiles.add(profile);
		}			
	}

	public static void rankCompleteProfiles(List<Profile> profiles, int number, Profile reference) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	// list of previously selected players
		List<String> profilesIds = new ArrayList<String>();
		for(Profile profile: profiles)
			profilesIds.add(profile.getId());
		
		// list of ids minus already selected players 
		List<String> playersIds = ProfileLoader.getIdsList();
		for(Profile profile: profiles)
		{	String playerId = profile.getId();
			playersIds.remove(playerId);
		}

		// process a list of related players
		RankingService rankingService = GameStatistics.getRankingService();
		String referenceId = reference.getId();
		Set<Match> matches = rankingService.getMatches(referenceId);
		List<Profile> additionalProfiles = new ArrayList<Profile>();
		int n = number - profiles.size();
		int i = 0;
		Iterator<Match> it = matches.iterator();
		while(it.hasNext() && i<n)
		{	Match match = it.next();
			String opponentId = match.getPlayerId();
			if(!profilesIds.contains(opponentId))
			{	Profile profile = ProfileLoader.loadProfile(opponentId);
				additionalProfiles.add(profile);			
				i++;
			}
		}
		
		// add additional profiles the original selection
		addAllProfiles(profiles,additionalProfiles);
	}
	
}
