package org.totalboumboum.tools.files;

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

/**
 * Class defining all sorts of methods
 * related to file paths.
 * 
 * @author Vincent Labatut
 */
public class FilePaths
{
	/**
	 * Returns the path of the game resources.
	 * 
	 * @return
	 * 		Path of the resources.
	 */
	public static String getResourcesPath()
	{	return FileNames.FILE_RESOURCES;		
	}
	
    /////////////////////////////////////////////////////////////////
	// AGENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the root folder containing
	 * all agent packs.
	 * 
	 * @return
	 * 		The path of root of agent packs.
	 */
	public static String getAiPath()
	{	return getResourcesPath() +
		File.separator + FileNames.FILE_AI;
	}
	
	/**
	 * Returns the path of the folder containing
	 * all agents.
	 * 
	 * @return
	 * 		Path of the agents.
	 */
	public static String getAisPath()
	{	return getAiPath() +
			File.separator + FileNames.FILE_ORG +		
			File.separator + FileNames.FILE_TOTALBOUMBOUM +		
			File.separator + FileNames.FILE_AI;		
	}
	
    /////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * storing all cache files.
	 * 
	 * @return
	 * 		Path of the cache files.
	 */
	public static String getCachePath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_CACHE;		
	}
	
	/**
	 * Returns the path of the folder
	 * storing bomb-related cache files.
	 * 
	 * @return
	 * 		Path of the bomb-related cache files.
	 */
	public static String getCacheBombsPath()
	{	return getCachePath()+File.separator+FileNames.FILE_BOMBS;		
	}

	/**
	 * Returns the path of the folder
	 * storing explosion-related cache files.
	 * 
	 * @return
	 * 		Path of the explosion-related cache files.
	 */
	public static String getCacheExplosionsPath()
	{	return getCachePath()+File.separator+FileNames.FILE_EXPLOSIONS;		
	}

	/**
	 * Returns the path of the folder
	 * storing fire-related cache files.
	 * 
	 * @return
	 * 		Path of the fire-related cache files.
	 */
	public static String getCacheFiresPath()
	{	return getCachePath()+File.separator+FileNames.FILE_FIRES;		
	}

	/**
	 * Returns the path of the folder
	 * storing heroe-related cache files.
	 * 
	 * @return
	 * 		Path of the hero-related cache files.
	 */
	public static String getCacheHeroesPath()
	{	return getCachePath()+File.separator+FileNames.FILE_HEROES;		
	}

	/**
	 * Returns the path of the folder
	 * storing item-related cache files.
	 * 
	 * @return
	 * 		Path of the item-related cache files.
	 */
	public static String getCacheItemsPath()
	{	return getCachePath()+File.separator+FileNames.FILE_ITEMS;		
	}

	/**
	 * Returns the path of the folder
	 * storing theme-related cache files.
	 * 
	 * @return
	 * 		Path of the theme-related cache files.
	 */
	public static String getCacheThemesPath()
	{	return getCachePath()+File.separator+FileNames.FILE_THEMES;		
	}

	/**
	 * Returns the path of the folder
	 * used to store different types of captures.
	 * 
	 * @return
	 * 		Path of the capture folder.
	 */
	public static String getCapturePath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_CAPTURES;		
	}
	
    /////////////////////////////////////////////////////////////////
	// CAPTURES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * used to store screen captures.
	 * 
	 * @return
	 * 		Path of the screen capture folder.
	 */
	public static String getCaptureImagesPath()
	{	return getCapturePath()+File.separator+FileNames.FILE_IMAGES;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store percept captures.
	 * 
	 * @return
	 * 		Path of the percept capture folder.
	 */
	public static String getCapturePerceptsPath()
	{	return getCapturePath()+File.separator+FileNames.FILE_PERCEPTS;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store replay files.
	 * 
	 * @return
	 * 		Path of the replay folder.
	 */
	public static String getCaptureReplaysPath()
	{	return getCapturePath()+File.separator+FileNames.FILE_REPLAYS;		
	}
	
    /////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * containing hero data.
	 * 
	 * @return
	 * 		Path of the hero folder.
	 */
	public static String getHeroesPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_HEROES;		
	}
	
	/**
	 * Returns the path of the folder
	 * containing instance data.
	 * 
	 * @return
	 * 		Path of the instance folder.
	 */
	public static String getInstancesPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_INSTANCES;		
	}	

	/**
	 * Returns the path of the folder
	 * containing level data.
	 * 
	 * @return
	 * 		Path of the level folder.
	 */
	public static String getLevelsPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_LEVELS;		
	}
	
    /////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * used to store logs.
	 * 
	 * @return
	 * 		Path of the log folder.
	 */
	public static String getLogsPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_LOGS;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store XML schemas (i.e. XSD files).
	 * 
	 * @return
	 * 		Path of the schema folder.
	 */
	public static String getSchemasPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_SCHEMAS;		
	}
	
	/**
	 * Returns the path of a folder
	 * containing various files.
	 * 
	 * @return
	 * 		Path of the misc folder.
	 */
	public static String getMiscPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_MISC;		
	}
	
    /////////////////////////////////////////////////////////////////
	// OPTIONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * used to store the game settings.
	 * 
	 * @return
	 * 		Path of the settings folder.
	 */
	public static String getSettingsPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_SETTINGS;		
	}

	/**
	 * Returns the path of the folder
	 * used to store the game configuration settings.
	 * 
	 * @return
	 * 		Path of the configuration folder.
	 */
	public static String getConfigurationPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_CONFIGURATION;		
	}

	/**
	 * Returns the path of the folder
	 * used to store controls settings.
	 * 
	 * @return
	 * 		Path of the controls folder.
	 */
	public static String getControlsPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_CONTROLS;		
	}

    /////////////////////////////////////////////////////////////////
	// SETTINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * used to store match data.
	 * 
	 * @return
	 * 		Path of the match folder.
	 */
	public static String getMatchesPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_MATCHES;		
	}

	/**
	 * Returns the path of the folder
	 * used to store points data.
	 * 
	 * @return
	 * 		Path of the point folder.
	 */
	public static String getPointsPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_POINTS;		
	}

	/**
	 * Returns the path of the folder
	 * used to store profile data.
	 * 
	 * @return
	 * 		Path of the profile folder.
	 */
	public static String getProfilesPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_PROFILES;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store round data.
	 * 
	 * @return
	 * 		Path of the round folder.
	 */
	public static String getRoundsPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_ROUNDS;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store tournament data.
	 * 
	 * @return
	 * 		Path of the tournament folder.
	 */
	public static String getTournamentsPath()
	{	return getSettingsPath()+File.separator+FileNames.FILE_TOURNAMENTS;		
	}

	/**
	 * Returns the path of the folder
	 * used to store saved data.
	 * 
	 * @return
	 * 		Path of the saves folder.
	 */
	public static String getSavesPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_SAVES;		
	}

    /////////////////////////////////////////////////////////////////
	// STATS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the path of the folder
	 * used to store statistics.
	 * 
	 * @return
	 * 		Path of the stat folder.
	 */
	public static String getStatisticsPath()
	{	return getResourcesPath()+File.separator+FileNames.FILE_STATISTICS;		
	}

	/**
	 * Returns the path of the folder
	 * used to store detailed statistics.
	 * 
	 * @return
	 * 		Path of the detailed stats folder.
	 */
	public static String getDetailedStatisticsPath()
	{	return getStatisticsPath()+File.separator+FileNames.FILE_DETAILED;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store Glicko-2 data.
	 * 
	 * @return
	 * 		Path of the Glicko-2 folder.
	 */
	public static String getGlicko2Path()
	{	return getStatisticsPath()+File.separator+FileNames.FILE_GLICKO2;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store overall statistics.
	 * 
	 * @return
	 * 		Path of the overall stats folder.
	 */
	public static String getOverallStatisticsPath()
	{	return getStatisticsPath()+File.separator+FileNames.FILE_OVERALL;		
	}
	
	/**
	 * Returns the path of the folder
	 * used to store host statistics data.
	 * 
	 * @return
	 * 		Path of the host stats folder.
	 */
	public static String getHostsStatisticsPath()
	{	return getStatisticsPath()+File.separator+FileNames.FILE_HOSTS;		
	}
}
