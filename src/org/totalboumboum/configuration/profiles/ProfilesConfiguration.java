package org.totalboumboum.configuration.profiles;

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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.ai.AisConfiguration.TournamentAutoAdvance;
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
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.glicko2.jrs.Match;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * This class handles everything concerning
 * profile settings and selection (as in selecting 
 * players for a tournament or game).
 * 
 * @author Vincent Labatut
 */
public class ProfilesConfiguration
{
	/**
	 * Copy the current configuration,
	 * to be able to restore it later.
	 * 
	 * @return
	 * 		A copy of this object.
	 */
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
	/** Current profiles */
	private HashMap<String,String> profiles = new HashMap<String,String>();

	/**
	 * Returns the current profile names.
	 * 
	 * @return
	 * 		Current profile names.
	 */
	public HashMap<String,String> getProfiles()
	{	return profiles;	
	}

	/**
	 * Adds a profile name.
	 * 
	 * @param id
	 * 		Unique id of the profile.
	 * @param name
	 * 		Name of the profile.
	 */
	public void addProfile(String id, String name)
	{	profiles.put(id,name);
	}
	
	/**
	 * Removes a profile.
	 * 
	 * @param id
	 * 		Unique id of the profile to be removed.
	 */
	public void removeProfile(String id)
	{	profiles.remove(id);
	}

	/**
	 * Creates a new profile, initializes
	 * all the appropriate data structures.
	 * 
	 * @param name
	 * 		Name of the new profile.
	 * @return
	 * 		The corresponding newly created profile.
	 * 
	 * @throws IOException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws ParserConfigurationException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws SAXException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws IllegalArgumentException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws SecurityException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws IllegalAccessException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws NoSuchFieldException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 * @throws ClassNotFoundException
	 * 		Problem while creating/modifying a file regarding profile creation.
	 */
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
	 * Used during network game, to synch
	 * profiles across platforms.
	 * 
	 * @param profile
	 * 		The profile to be inserted.
	 *  
	 * @throws IOException
	 * 		Problem while accessing the profile. 
	 * @throws ParserConfigurationException 
	 * 		Problem while accessing the profile. 
	 * @throws SAXException 
	 * 		Problem while accessing the profile. 
	 * @throws IllegalArgumentException 
	 * 		Problem while accessing the profile. 
	 * @throws SecurityException 
	 * 		Problem while accessing the profile. 
	 * @throws IllegalAccessException 
	 * 		Problem while accessing the profile. 
	 * @throws NoSuchFieldException 
	 * 		Problem while accessing the profile. 
	 * @throws ClassNotFoundException 
	 * 		Problem while accessing the profile. 
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
	
	/**
	 * Deleting an existing profile.
	 * 
	 * @param profile
	 * 		Profile to be deleted.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the profile. 
	 * @throws SAXException
	 * 		Problem while accessing the profile. 
	 * @throws IOException
	 * 		Problem while accessing the profile. 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the profile. 
	 * @throws SecurityException
	 * 		Problem while accessing the profile. 
	 * @throws IllegalAccessException
	 * 		Problem while accessing the profile. 
	 * @throws NoSuchFieldException
	 * 		Problem while accessing the profile. 
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the profile. 
	 */
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
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Gets the next controls not already used by a player.
	 * 
	 * @param profiles
	 * 		List of selected profiles.
	 * @param start
	 * 		Starting control number.
	 * @return
	 * 		Number of the next free controls.
	 */
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

	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Gets the next color not already used by a player.
	 * 
	 * @param profiles
	 * 		List of selected profiles.
	 * @param profile
	 * 		Reference profile (ignore its color).
	 * @param color
	 * 		Starting color.
	 * @return
	 * 		The next free color.
	 */
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

	/**
	 * Checks if the specified color is already used in the
	 * specified profile selection.
	 * 
	 * @param profiles
	 * 		Selection of profiles.
	 * @param color
	 * 		Color to be checked.
	 * @return
	 * 		{@code true} iff no selected profile uses the specified color.
	 */
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
	
//	/**
//	 * Performs the same processing than {@link #isFreeColor(List, PredefinedColor)},
//	 * but ignores the color of the profile at position {@code ignoreIndex}.
//	 * 
//	 * @param profiles
//	 * 		Profiles to be considered.
//	 * @param color
//	 * 		Color to be tested.
//	 * @param ignoreIndex
//	 * 		Position of the ignored profile.
//	 * @return
//	 * 		{@code true} iff no profile (other than the ignored one) uses the specified color.
//	 */
//	public boolean isFreeColor(List<Profile> profiles, PredefinedColor color, int ignoreIndex)
//	{	// used colors
//		List<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
//		int index = 0;
//		for(Profile p: profiles)
//		{	PredefinedColor clr = p.getSpriteColor();
//			if(index!=ignoreIndex)
//				usedColors.add(clr);
//			index++;
//		}
//		boolean result = !usedColors.contains(color);
//		return result;
//	}
	
	
	/////////////////////////////////////////////////////////////////
	// PLAYER SELECTION		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of players to be excluded from automatic selections */
	private static List<String> BLACK_LIST = Arrays.asList(
//		"bbe5482b-6fa0-4204-a5a6-43c4efff917c",	// 0910.Fındık+Şırın
//		"3c898e42-a3e2-4b6d-866e-f672ec19d326",	// 0910.Mançuhan+Pınarer
//		"309ac62f-8346-4281-83ab-086902e9e39c",	// 0910.Demirci+Düzok+Ergök
//		"ac0853de-bb8f-4141-8fa9-4a3b2708d331"	// 0910.Dane+Şatır
		);
	
	/**
	 * Builds a selection of profiles from
	 * the specified list.
	 * 
	 * @param profiles
	 * 		The selected profiles.
	 * @return
	 * 		The corresponding selection object.
	 */
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
	
	/**
	 * Randomly completes the specified list of profiles,
	 * in order to get a list containing the specified 
	 * number of players.
	 * 
	 * @param profiles
	 * 		List to be completed.
	 * @param number
	 * 		Desired number of profiles in the complete list.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading profiles.
	 * @throws SAXException
	 * 		Problem while loading profiles.
	 * @throws IOException
	 * 		Problem while loading profiles.
	 * @throws ClassNotFoundException
	 * 		Problem while loading profiles.
	 * @throws IllegalArgumentException
	 * 		Problem while loading profiles.
	 * @throws SecurityException
	 * 		Problem while loading profiles.
	 * @throws IllegalAccessException
	 * 		Problem while loading profiles.
	 * @throws NoSuchFieldException
	 * 		Problem while loading profiles.
	 */
	public static void completeProfilesRandomly(List<Profile> profiles, int number) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	// list of ids minus already selected players 
		List<String> playersIds = ProfileLoader.getIdsList();
		for(Profile profile: profiles)
		{	String playerId = profile.getId();
			playersIds.remove(playerId);
		}
		
		// and minus black-listed players
		for(String playerId: BLACK_LIST)
			playersIds.remove(playerId);

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
	
	/**
	 * Randomly selects a maximum of players
	 * for the specified tournamnet.
	 * 
	 * @param tournamentConfiguration
	 * 		Object to used to store the auto-advance index
	 * @return
	 * 		List of selected profiles.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading profiles.
	 * @throws SAXException
	 * 		Problem while loading profiles.
	 * @throws IOException
	 * 		Problem while loading profiles.
	 * @throws ClassNotFoundException
	 * 		Problem while loading profiles.
	 * @throws IllegalArgumentException
	 * 		Problem while loading profiles.
	 * @throws SecurityException
	 * 		Problem while loading profiles.
	 * @throws IllegalAccessException
	 * 		Problem while loading profiles.
	 * @throws NoSuchFieldException
	 * 		Problem while loading profiles.
	 */
	public static List<Profile> selectProfilesRandomly(TournamentConfiguration tournamentConfiguration) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	List<Profile> selectedProfiles = new ArrayList<Profile>();
		Set<Integer> allowedNbr = tournamentConfiguration.getTournament().getAllowedPlayerNumbers();
	
		// get player list
		RankingService rankingService = GameStatistics.getRankingService();
		List<String> playerIds = new ArrayList<String>(rankingService.getPlayers());
		
		// remove black-listed players
		for(String playerId: BLACK_LIST)
			playerIds.remove(playerId);

		// select an appropriate number of players depending on the tournament
		TreeSet<Integer> temp = new TreeSet<Integer>(allowedNbr);
		int limit = temp.last();
			
		// possibly complete with randomly selected players
		for(int k=0;k<limit;k++)
		{	int index = (int)(Math.random()*playerIds.size());
			String playerId = playerIds.get(index);
			playerIds.remove(index);
			Profile profile = ProfileLoader.loadProfile(playerId);
			selectedProfiles.add(profile);
		}

		// create selection
		List<Profile> result = new ArrayList<Profile>();
		addAllProfiles(result,selectedProfiles);
		return result;
	}
	
	/**
	 * Adds all the specified profile to the
	 * specified selection, so that there
	 * is no incompatibility in terms of colors.
	 * 
	 * @param profiles
	 * 		Base selection.
	 * @param additionalProfiles
	 * 		Profiles to add to the selection.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile. 
	 * @throws SAXException
	 * 		Problem while loading a profile. 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile. 
	 */
	private static void addAllProfiles(List<Profile> profiles, List<Profile> additionalProfiles) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	ProfilesConfiguration profilesConfiguration = Configuration.getProfilesConfiguration();

		// store player positions
		final Map<Profile,Integer> positions = new HashMap<Profile, Integer>();
		for(int i=0;i<profiles.size();i++)
			positions.put(profiles.get(i),i);
		for(int i=0;i<additionalProfiles.size();i++)
			positions.put(additionalProfiles.get(i),i+profiles.size());
	
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
		
		// restore original order
		Collections.sort(profiles,new Comparator<Profile>()
		{	@Override
			public int compare(Profile o1, Profile o2)
			{	int index1 = positions.get(o1);
				int index2 = positions.get(o2);
				int result = index1 - index2;
				return result;
			}	
		});
	}

	/**
	 * Complete the specified selection
	 * with some players whose Glicko-2 ranking
	 * is related.
	 * 
	 * @param profiles
	 * 		Incomplete profile selection.
	 * @param number
	 * 		Size of the final selection.
	 * @param reference
	 * 		Player of reference (in terms of Glicko-2 ranking).
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading a profile. 
	 * @throws SAXException
	 * 		Problem while loading a profile. 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException
	 * 		Problem while loading a profile. 
	 * @throws IllegalArgumentException
	 * 		Problem while loading a profile. 
	 * @throws SecurityException
	 * 		Problem while loading a profile. 
	 * @throws IllegalAccessException
	 * 		Problem while loading a profile. 
	 * @throws NoSuchFieldException
	 * 		Problem while loading a profile. 
	 */
	public static void completeProfilesByGlicko(List<Profile> profiles, int number, Profile reference) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
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
		
		// and minus black-listed players
		for(String playerId: BLACK_LIST)
			playersIds.remove(playerId);

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
	
	/**
	 * Automatically selects player for the 
	 * tournament mode of the auto-advance
	 * system. The first tournament involves
	 * the best 16 players, the second the 16
	 * next ones, and so on.
	 * 
	 * @param tournamentConfiguration
	 * 		Object to used to store the auto-advance index
	 * @return
	 * 		List of selected profiles.
	 * 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws SAXException 
	 * 		Problem while loading a profile. 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile. 
	 * @throws NoSuchFieldException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalAccessException 
	 * 		Problem while loading a profile. 
	 * @throws SecurityException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalArgumentException 
	 * 		Problem while loading a profile. 
	 */
	public static List<Profile> selectProfilesByRank(TournamentConfiguration tournamentConfiguration) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException
	{	List<Profile> selectedProfiles = new ArrayList<Profile>();
		int autoAdvanceIndex = tournamentConfiguration.getAutoAdvanceIndex();
//		int autoAdvanceIndex = Configuration.getGameConfiguration().getTournamentConfiguration().getAutoAdvanceIndex();
		Set<Integer> allowedNbr = tournamentConfiguration.getTournament().getAllowedPlayerNumbers();
		
		int previousAaIndex = autoAdvanceIndex;
	
		// get player ranks
		RankingService rankingService = GameStatistics.getRankingService();
		Set<String> playerIds = rankingService.getPlayers();
		String playerRanks[] = new String[playerIds.size()];
		for(String playerId: playerIds)
		{	int rank = rankingService.getPlayerRank(playerId);
			playerRanks[rank-1] = playerId;
		}
		
		// select an appropriate number of players depending on the tournament
		int limit;
		int remaining = playerIds.size() - autoAdvanceIndex;
		TreeSet<Integer> sortedAllowed = new TreeSet<Integer>(allowedNbr);
		NavigableSet<Integer> temp = sortedAllowed.tailSet(remaining, true);
		if(temp.isEmpty())
		{	if(sortedAllowed.isEmpty())
				limit = 0;
			else
				limit = sortedAllowed.last();
		}
		else
		{	temp = sortedAllowed.headSet(temp.first(), true);
			Iterator<Integer> it = temp.descendingIterator();
			int tempLimit;
			do
			{	tempLimit = it.next();
			}
			while(playerIds.size()<tempLimit && it.hasNext());
			if(playerIds.size()<tempLimit)
				limit = 0;
			else
				limit = tempLimit;
		}
		
		// fill a list with the appropriate number of players starting from the index
		int i = 0;
		while(i<limit && autoAdvanceIndex<playerIds.size())
		{	String playerId = playerRanks[autoAdvanceIndex];
			// check if the player is not blacklisted, first
			if(BLACK_LIST.contains(playerId))
				autoAdvanceIndex++;
			// otherwise, add it to the list
			else
			{	Profile profile = ProfileLoader.loadProfile(playerId);
				selectedProfiles.add(profile);
				autoAdvanceIndex++;
				i++;
			}
		}
		
		// possibly complete with randomly selected players
		List<String> possiblePlayers = new ArrayList<String>();
		for(int j=0;j<previousAaIndex;j++)
		{	String playerId = playerRanks[j];
			if(!BLACK_LIST.contains(playerId))
				possiblePlayers.add(playerId);
		}
		for(int k=i;k<limit;k++)
		{	int index = (int)(Math.random()*possiblePlayers.size());
			String playerId = possiblePlayers.get(index);
			possiblePlayers.remove(index);
			Profile profile = ProfileLoader.loadProfile(playerId);
			selectedProfiles.add(profile);
		}
		
		// possibly reset the index
		if(autoAdvanceIndex==playerIds.size())
			autoAdvanceIndex = 0;
		tournamentConfiguration.setAutoAdvanceIndex(autoAdvanceIndex);
		Configuration.getGameConfiguration().getTournamentConfiguration().setAutoAdvanceIndex(autoAdvanceIndex);

		// create selection
		List<Profile> result = new ArrayList<Profile>();
		addAllProfiles(result,selectedProfiles);
		return result;
	}
	
	/**
	 * Automatically selects all players from
	 * the specified pack. Adds other players
	 * if needed for the selected tournament.
	 * 
	 * @param tournamentConfiguration
	 * 		Object to used to store the auto-advance index
	 * @param pack
	 * 		Pack one wants to focus on, or {@code null} to consider all players.
	 * @return
	 * 		List of selected profiles.
	 * 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws SAXException 
	 * 		Problem while loading a profile. 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile. 
	 * @throws NoSuchFieldException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalAccessException 
	 * 		Problem while loading a profile. 
	 * @throws SecurityException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalArgumentException 
	 * 		Problem while loading a profile. 
	 */
	public static List<Profile> selectProfilesByPack(TournamentConfiguration tournamentConfiguration, String pack) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException
	{	List<Profile> selectedProfiles = new ArrayList<Profile>();
		Set<Integer> allowedNbr = tournamentConfiguration.getTournament().getAllowedPlayerNumbers();
		
		// get the player ids
		RankingService rankingService = GameStatistics.getRankingService();
		List<String> playerIds = new ArrayList<String>(rankingService.getPlayers());
		
		// remove black-listed players
		for(String playerId: BLACK_LIST)
			playerIds.remove(playerId);

		// filter them depending on their pack
		{	Iterator<String> it = playerIds.iterator();
			while(it.hasNext())
			{	String playerId = it.next();
				Profile profile = ProfileLoader.loadProfile(playerId);
				String p = profile.getAiPackname();
				if(p!=null && p.equals(pack))
				{	selectedProfiles.add(profile);
					it.remove();
				}
			}
		}
		
		// select an appropriate number of players depending on the tournament
		int limit;
		TreeSet<Integer> sortedAllowed = new TreeSet<Integer>(allowedNbr);
		NavigableSet<Integer> temp = sortedAllowed.tailSet(selectedProfiles.size(), true);
		if(temp.isEmpty())
		{	if(sortedAllowed.isEmpty())
				limit = 0;
			else
				limit = sortedAllowed.last();
		}
		else
		{	temp = sortedAllowed.headSet(temp.first(), true);
			limit = temp.first();
		}

		// rank the player in alphabetical order
		Collections.sort(selectedProfiles, new Comparator<Profile>()
		{	@Override
			public int compare(Profile o1, Profile o2)
			{	String name1 = o1.getAiName();
				String name2 = o2.getAiName();
				int result = name1.compareTo(name2);
				return result;
			}
		});
		
		// possibly remove some players if there're too many
		while(limit<selectedProfiles.size() && limit>0)
		{	int index = (int)Math.random()*selectedProfiles.size();
			selectedProfiles.remove(index);
		}
		
		// possibly complete with randomly selected players
		for(int k=selectedProfiles.size();k<limit;k++)
		{	int index = (int)(Math.random()*playerIds.size());
			String playerId = playerIds.get(index);
			playerIds.remove(index);
			Profile profile = ProfileLoader.loadProfile(playerId);
			selectedProfiles.add(profile);
		}
		
		// create selection
		List<Profile> result = new ArrayList<Profile>();
		addAllProfiles(result,selectedProfiles);
		return result;
	}

	/**
	 * Automatically selects a maximum of players having
	 * played lessrounds than the others. This can
	 * be used to let them catch up the rest
	 * of the ranking.
	 * 
	 * @param tournamentConfiguration
	 * 		Object to used to store the auto-advance index
	 * @return
	 * 		List of selected profiles.
	 * 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws SAXException 
	 * 		Problem while loading a profile. 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile. 
	 * @throws NoSuchFieldException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalAccessException 
	 * 		Problem while loading a profile. 
	 * @throws SecurityException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalArgumentException 
	 * 		Problem while loading a profile. 
	 */
	public static List<Profile> selectProfilesByConfrontations(TournamentConfiguration tournamentConfiguration) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException
	{	List<Profile> selectedProfiles = new ArrayList<Profile>();
		Set<Integer> allowedNbr = tournamentConfiguration.getTournament().getAllowedPlayerNumbers();
		
		// get the player ids, ranked by number of confrontations
		RankingService rankingService = GameStatistics.getRankingService();
		TreeSet<String> playerIds = new TreeSet<String>(new Comparator<String>()
		{	@Override
			public int compare(String o1, String o2)
			{	Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
				PlayerStats playerStats1 = playersStats.get(o1);
				PlayerStats playerStats2 = playersStats.get(o2);
				
				long played1 = playerStats1.getRoundsPlayed();
				long played2 = playerStats2.getRoundsPlayed();
				int result = (int)(played1 - played2);
				
				if(result==0)
				{	long time1 = playerStats1.getScore(Score.TIME);
					long time2 = playerStats2.getScore(Score.TIME);
					result = (int)(time1 - time2);

					if(result==0)
						result = o1.compareTo(o2);
				}
				return result;
			}
		});
		playerIds.addAll(rankingService.getPlayers());
		
		// remove black-listed players
		for(String playerId: BLACK_LIST)
			playerIds.remove(playerId);

		// keep the most players amongst those having played less
		int limit = Collections.max(allowedNbr);
		Iterator<String> it = playerIds.iterator();
		for(int i=0;i<limit;i++)
		{	String playerId = it.next();
//			Map<String,PlayerStats> playersStats = GameStatistics.getPlayersStats();
//			PlayerStats playerStats = playersStats.get(playerId);
//			long conf = playerStats.getRoundsPlayed();
			Profile profile = ProfileLoader.loadProfile(playerId);
			selectedProfiles.add(profile);
		}
		
		// create selection
		List<Profile> result = new ArrayList<Profile>();
		addAllProfiles(result,selectedProfiles);
		return result;
	}
	
	/**
	 * Automatically selects the players of a tournament,
	 * depending on the AI settings.
	 * 
	 * @param tournamentConfiguration
	 * 		Object to used to store the auto-advance index
	 * @return
	 * 		List of selected profiles.
	 * 
	 * @throws IOException
	 * 		Problem while loading a profile. 
	 * @throws SAXException 
	 * 		Problem while loading a profile. 
	 * @throws ParserConfigurationException 
	 * 		Problem while loading a profile. 
	 * @throws ClassNotFoundException 
	 * 		Problem while loading a profile. 
	 * @throws NoSuchFieldException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalAccessException 
	 * 		Problem while loading a profile. 
	 * @throws SecurityException 
	 * 		Problem while loading a profile. 
	 * @throws IllegalArgumentException 
	 * 		Problem while loading a profile. 
	 */
	public static List<Profile> selectProfiles(TournamentConfiguration tournamentConfiguration) throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException, ParserConfigurationException, SAXException, IOException
	{	AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
		TournamentAutoAdvance mode = aisConfiguration.getTournamentAutoAdvanceMode();
		
		List<Profile> result = null;
		switch(mode)
		{	case CONFRONTATIONS:
				result = selectProfilesByConfrontations(tournamentConfiguration);
				break;
			case PACK:
				String pack = aisConfiguration.getTournamentAutoAdvancePack();
				result = selectProfilesByPack(tournamentConfiguration, pack);
				break;
			case RANDOM:
				result = selectProfilesRandomly(tournamentConfiguration);
				break;
			case RANKS:
				result = selectProfilesByRank(tournamentConfiguration);
				break;
		}
		
		return result;
	}
}
