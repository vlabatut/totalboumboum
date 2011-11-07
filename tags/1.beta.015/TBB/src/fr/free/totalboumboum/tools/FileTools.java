package fr.free.totalboumboum.tools;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;



public class FileTools
{
	public static final String EXTENSION_BACKUP = ".backup";
	public static final String EXTENSION_CLASS = ".class";
	public static final String EXTENSION_DATA = ".data";
	public static final String EXTENSION_FONT = ".ttf";
	public static final String EXTENSION_JAVA = ".java";
	public static final String EXTENSION_LOG = ".log";
	public static final String EXTENSION_SCHEMA = ".xsd";
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_XML = ".xml";
	//
	public static final String FILE_ABILITIES = "abilities";
	public static final String FILE_ABOUT = "about";
	public static final String FILE_AI = "ai";
	public static final String FILE_AI_EXCEPTIONS = "aiexceptions";
	public static final String FILE_AIS = "ais";
	public static final String FILE_AI_MAIN_CLASS = "AiMain";
	public static final String FILE_ANIMES = "animes";
	public static final String FILE_ARCHIVE = "archive";
	public static final String FILE_BLOCK = "block";
	public static final String FILE_BLOCKSET = "blockset";
	public static final String FILE_BOMB = "bomb";
	public static final String FILE_BOMBSET = "bombset";
	public static final String FILE_BOMBSETS = "bombsets";
	public static final String FILE_COLORMAP = "colormap";
	public static final String FILE_COLORMAPS = "colormaps";
	public static final String FILE_CONFIGURATION = "configuration";
	public static final String FILE_CONTROLS = "controls";
	public static final String FILE_ENGINE = "engine";
	public static final String FILE_EXPLOSIONS = "explosions";
	public static final String FILE_EXPLOSION = "explosion";
	public static final String FILE_FIRE = "fire";
	public static final String FILE_FIRESET = "fireset";
	public static final String FILE_FIRESETMAP = "firesetmap";
	public static final String FILE_FIRESETS = "firesets";
	public static final String FILE_FLOOR = "floor";
	public static final String FILE_FLOORSET = "floorset";
	public static final String FILE_GAME = "game";
	public static final String FILE_GAME_QUICKMATCH = "game_quickmatch";
	public static final String FILE_GAME_QUICKSTART = "game_quickstart";
	public static final String FILE_GAME_TOURNAMENT = "game_tournament";
	public static final String FILE_GESTUREMODULATIONS = "gesture_modulations";
	public static final String FILE_GESTUREPERMISSIONS = "gesture_permissions";
	public static final String FILE_HERO = "hero";
	public static final String FILE_ITEM = "item";
	public static final String FILE_ITEMS = "items";
	public static final String FILE_ITEMSETS = "itemsets";
	public static final String FILE_ITEMSET = "itemset";
	public static final String FILE_LEVEL = "level";
	public static final String FILE_LANGUAGE = "language";
	public static final String FILE_LEVELS = "levels";
	public static final String FILE_MATCH = "match";
	public static final String FILE_MATCHES = "matches";
	public static final String FILE_MATRICES = "matrices";
	public static final String FILE_MATRIX = "matrix";
	public static final String FILE_MODULATIONS = "modulations";
	public static final String FILE_PERMISSIONS = "permissions";
	public static final String FILE_PLAYER = "player";
	public static final String FILE_PLAYERS = "players";
	public static final String FILE_POINT = "point";
	public static final String FILE_POINTS = "points";
	public static final String FILE_PORTRAITS = "portraits";
	public static final String FILE_PROFILE = "profile";
	public static final String FILE_PROFILES = "profiles";
	public static final String FILE_PROPERTIES = "properties";
	public static final String FILE_ROUND = "round";
	public static final String FILE_ROUNDS = "rounds";
	public static final String FILE_SETTINGS = "settings";
	public static final String FILE_SPRITE = "sprite";
	public static final String FILE_SPRITES = "sprites";
	public static final String FILE_STATISTICS = "statistics";
	public static final String FILE_THEMES = "themes";
	public static final String FILE_THEME = "theme";
	public static final String FILE_TOURNAMENT = "tournament";
	public static final String FILE_TRAJECTORIES = "trajectories";
	public static final String FILE_VIDEO = "video";
	public static final String FILE_ZONE = "zone";
	public static final String FILE_ZONES = "zones";
	//
	public static final String FOLDER_ABILITIES = "abilities";
	public static final String FOLDER_ANIMES = "animes";
	public static final String FOLDER_AI = "ai";
	public static final String FOLDER_AUTOSAVE = "autosave";
	public static final String FOLDER_AUTOSAVE_BACKUP = "autosave_backup";
	public static final String FOLDER_BLOCKS = "blocks";
	public static final String FOLDER_BOMBS = "bombs";
	public static final String FOLDER_CACHE = "cache";
	public static final String FOLDER_CHARACTERS = "characters";
	public static final String FOLDER_CONFIGURATION = "configuration";
	public static final String FOLDER_CONTROLS = "controls";
	public static final String FOLDER_DATA = "data";
	public static final String FOLDER_DETAILED = "detailed";
	public static final String FOLDER_EXPLOSIONS = "explosions";
	public static final String FOLDER_FIRE = "fire";
	public static final String FOLDER_FIRES = "fires";
	public static final String FOLDER_FLOORS = "floors";
	public static final String FOLDER_GAMES = "games";
	public static final String FOLDER_GLICKO2 = "glicko2";
	public static final String FOLDER_GRAPHICS = "graphics";
	public static final String FOLDER_HEROES = "heroes";
	public static final String FOLDER_ITEMS = "items";
	public static final String FOLDER_INSTANCES = "instances";
	public static final String FOLDER_LEVELS = "levels";
	public static final String FOLDER_LOGS = "logs";
	public static final String FOLDER_MATCHES = "matches";
	public static final String FOLDER_MINE = "mine";
	public static final String FOLDER_MODULATIONS = "modulations";
	public static final String FOLDER_NORMAL = "normal";
	public static final String FOLDER_OVERALL = "overall";
	public static final String FOLDER_PENETRATION = "penetration";
	public static final String FOLDER_PERMISSIONS = "permissions";
	public static final String FOLDER_PLAYERS = "players";
	public static final String FOLDER_POINTS = "points";
	public static final String FOLDER_PORTRAITS = "portraits";
	public static final String FOLDER_PROFILES = "profiles";
	public static final String FOLDER_RAW = "raw";
	public static final String FOLDER_REMOTE = "remote";
	public static final String FOLDER_ROUNDS = "rounds";
	public static final String FOLDER_RESOURCES = "resources";
	public static final String FOLDER_SAVES = "saves";
	public static final String FOLDER_SCHEMAS = "schemas";
	public static final String FOLDER_SEARCH = "search";
	public static final String FOLDER_SETTINGS = "settings";
	public static final String FOLDER_SOURCE = "src";
	public static final String FOLDER_STATISTICS = "statistics";
	public static final String FOLDER_THEMES = "themes";
	public static final String FOLDER_TOURNAMENT = "tournament";
	public static final String FOLDER_TOURNAMENTS = "tournaments";
	public static final String FOLDER_TRAJECTORIES = "trajectories";
	public static final String FOLDER_XML = "xml";
	
	public static String getResourcesPath()
	{	return FOLDER_RESOURCES;		
	}
	
	public static String getAiPath()
	{	return getResourcesPath()+File.separator+FOLDER_AI;		
	}
	
	public static String getCachePath()
	{	return getResourcesPath()+File.separator+FOLDER_CACHE;		
	}
	public static String getCacheBombsPath()
	{	return getCachePath()+File.separator+FOLDER_BOMBS;		
	}
	public static String getCacheFiresPath()
	{	return getCachePath()+File.separator+FOLDER_FIRES;		
	}
	public static String getCacheHeroesPath()
	{	return getCachePath()+File.separator+FOLDER_HEROES;		
	}
	public static String getCacheItemsPath()
	{	return getCachePath()+File.separator+FOLDER_ITEMS;		
	}
	public static String getCacheThemesPath()
	{	return getCachePath()+File.separator+FOLDER_THEMES;		
	}

	public static String getHeroesPath()
	{	return getResourcesPath()+File.separator+FOLDER_HEROES;		
	}
	
	public static String getInstancesPath()
	{	return getResourcesPath()+File.separator+FOLDER_INSTANCES;		
	}	

	public static String getLevelsPath()
	{	return getResourcesPath()+File.separator+FOLDER_LEVELS;		
	}
	
	public static String getLogsPath()
	{	return getResourcesPath()+File.separator+FOLDER_LOGS;		
	}
	
	public static String getSchemasPath()
	{	return getResourcesPath()+File.separator+FOLDER_SCHEMAS;		
	}
	
	public static String getSettingsPath()
	{	return getResourcesPath()+File.separator+FOLDER_SETTINGS;		
	}
	public static String getConfigurationPath()
	{	return getSettingsPath()+File.separator+FOLDER_CONFIGURATION;		
	}
	public static String getControlsPath()
	{	return getSettingsPath()+File.separator+FOLDER_CONTROLS;		
	}
	public static String getMatchesPath()
	{	return getSettingsPath()+File.separator+FOLDER_MATCHES;		
	}
	public static String getPointsPath()
	{	return getSettingsPath()+File.separator+FOLDER_POINTS;		
	}
	public static String getProfilesPath()
	{	return getSettingsPath()+File.separator+FOLDER_PROFILES;		
	}
	public static String getRoundsPath()
	{	return getSettingsPath()+File.separator+FOLDER_ROUNDS;		
	}
	public static String getTournamentsPath()
	{	return getSettingsPath()+File.separator+FOLDER_TOURNAMENTS;		
	}

	public static String getSavesPath()
	{	return getResourcesPath()+File.separator+FOLDER_SAVES;		
	}

	public static String getStatisticsPath()
	{	return getResourcesPath()+File.separator+FOLDER_STATISTICS;		
	}
	public static String getDetailedStatisticsPath()
	{	return getStatisticsPath()+File.separator+FOLDER_DETAILED;		
	}
	public static String getGlicko2Path()
	{	return getStatisticsPath()+File.separator+FOLDER_GLICKO2;		
	}
	public static String getOverallStatisticsPath()
	{	return getStatisticsPath()+File.separator+FOLDER_OVERALL;		
	}







	
	
	
	public static File getFile(String fileName, File[] list)
	{	File result = null;
		int i = 0;
		while(i<list.length && result==null)
		{	String fName = list[i].getName();
			if(fName.equalsIgnoreCase(fileName))
				result = list[i];
			else
				i++;				
		}
		return result;
	}
	
	public static void deleteDirectory(File dir)
	{	if(dir.exists() && dir.isDirectory())
		{	File[] files = dir.listFiles();
			for(File f: files)
			{	if(f.isFile())
					f.delete();
				else
					deleteDirectory(f);
			}
			dir.delete();
		}		
	}
	
	public static void copyDirectory(File source, File target) throws IOException
	{	// recursively copy a directory
		if (source.isDirectory())
		{	if (!target.exists())
				target.mkdir();
            String[] files = source.list();
            for(int i=0;i<files.length;i++)
            {	String fileName = files[i];
            	File src = new File(source, fileName);
            	File trgt = new File(target, fileName);
            	copyDirectory(src,trgt);
            }
		}
		// or bit-to-bit copy a file
		else
		{	FileInputStream in = new FileInputStream(source);
			FileOutputStream out = new FileOutputStream(target);
            byte[] buf = new byte[1024];
            int len;
            while ((len=in.read(buf))>0)
            	out.write(buf, 0, len);
            in.close();
            out.close();
        }
	}
	
	public static String getFilenameCompatibleCurrentTime()
	{	Calendar cal = new GregorianCalendar();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		int sec = cal.get(Calendar.SECOND);
		String secStr = nf.format(sec);
		int min = cal.get(Calendar.MINUTE);
		String minStr = nf.format(min);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourStr = nf.format(hour);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String dayStr = nf.format(day);
		int month = cal.get(Calendar.MONTH);
		String monthStr = nf.format(month);
		int year = cal.get(Calendar.YEAR);
		String yearStr = Integer.toString(year);
		String result = yearStr+"."+monthStr+"."+dayStr+"_"+hourStr+"."+minStr+"."+secStr;
		return result;
	}
}