package org.totalboumboum.tools.files;

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

public class FilePaths
{
	public static String getResourcesPath()
	{	return FileNames.FOLDER_RESOURCES;		
	}
	
	public static String getAisPath()
	{	return getResourcesPath() +
			File.separator + FileNames.FOLDER_AI +
			File.separator + FileNames.FOLDER_ORG +		
			File.separator + FileNames.FOLDER_TOTALBOUMBOUM +		
			File.separator + FileNames.FOLDER_AI;		
	}
	
	public static String getCachePath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_CACHE;		
	}
	public static String getCacheBombsPath()
	{	return getCachePath()+File.separator+FileNames.FOLDER_BOMBS;		
	}
	public static String getCacheFiresPath()
	{	return getCachePath()+File.separator+FileNames.FOLDER_FIRES;		
	}
	public static String getCacheHeroesPath()
	{	return getCachePath()+File.separator+FileNames.FOLDER_HEROES;		
	}
	public static String getCacheItemsPath()
	{	return getCachePath()+File.separator+FileNames.FOLDER_ITEMS;		
	}
	public static String getCacheThemesPath()
	{	return getCachePath()+File.separator+FileNames.FOLDER_THEMES;		
	}

	public static String getHeroesPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_HEROES;		
	}
	
	public static String getInstancesPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_INSTANCES;		
	}	

	public static String getLevelsPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_LEVELS;		
	}
	
	public static String getLogsPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_LOGS;		
	}
	
	public static String getSchemasPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_SCHEMAS;		
	}
	
	public static String getSettingsPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_SETTINGS;		
	}
	public static String getConfigurationPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_CONFIGURATION;		
	}
	public static String getControlsPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_CONTROLS;		
	}
	public static String getMatchesPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_MATCHES;		
	}
	public static String getPointsPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_POINTS;		
	}
	public static String getProfilesPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_PROFILES;		
	}
	public static String getRoundsPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_ROUNDS;		
	}
	public static String getTournamentsPath()
	{	return getSettingsPath()+File.separator+FileNames.FOLDER_TOURNAMENTS;		
	}

	public static String getSavesPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_SAVES;		
	}

	public static String getStatisticsPath()
	{	return getResourcesPath()+File.separator+FileNames.FOLDER_STATISTICS;		
	}
	public static String getDetailedStatisticsPath()
	{	return getStatisticsPath()+File.separator+FileNames.FOLDER_DETAILED;		
	}
	public static String getGlicko2Path()
	{	return getStatisticsPath()+File.separator+FileNames.FOLDER_GLICKO2;		
	}
	public static String getOverallStatisticsPath()
	{	return getStatisticsPath()+File.separator+FileNames.FOLDER_OVERALL;		
	}
}
