package fr.free.totalboumboum.tools;

import java.io.File;



public class FileTools
{
	public static final String EXTENSION_SCHEMA = ".xsd";
	public static final String EXTENSION_DATA = ".xml";
	//
	public static final String FILE_ABILITIES = "abilities";
	public static final String FILE_ANIMES = "animes";
	public static final String FILE_BLOCK = "block";
	public static final String FILE_BOMB = "bomb";
	public static final String FILE_BOMBSETS = "bombsets";
	public static final String FILE_BOMBSET = "bombset";
	public static final String FILE_COLORMAP = "colormap";
	public static final String FILE_COLORMAPS = "colormaps";
	public static final String FILE_CONFIGURATION = "configuration";
	public static final String FILE_CONTROLS = "controls";
	public static final String FILE_EXPLOSIONS = "explosions";
	public static final String FILE_EXPLOSION = "explosion";
	public static final String FILE_FIRE = "fire";
	public static final String FILE_FIRESET = "fireset";
	public static final String FILE_FIRESETS = "firesets";
	public static final String FILE_FLOOR = "floor";
	public static final String FILE_MATCH = "match";
	public static final String FILE_MATCHES = "matches";
	public static final String FILE_GESTUREPERMISSIONS = "gesturepermissions";
	public static final String FILE_HERO = "hero";
	public static final String FILE_ITEM = "item";
	public static final String FILE_ITEMS = "items";
	public static final String FILE_ITEMSETS = "itemsets";
	public static final String FILE_ITEMSET = "itemset";
	public static final String FILE_LEVEL = "level";
	public static final String FILE_LANGUAGE = "language";
	public static final String FILE_LEVELS = "levels";
	public static final String FILE_MATRICES = "matrices";
	public static final String FILE_MATRIX = "matrix";
	public static final String FILE_PERMISSIONS = "permissions";
	public static final String FILE_PLAYER = "player";
	public static final String FILE_PLAYERS = "players";
	public static final String FILE_POINT = "point";
	public static final String FILE_POINTS = "points";
	public static final String FILE_PORTRAITS = "portraits";
	public static final String FILE_PROFILE = "profile";
	public static final String FILE_PROFILES = "profiles";
	public static final String FILE_PROPERTIES = "properties";
	public static final String FILE_SETTINGS = "settings";
	public static final String FILE_SPRITE = "sprite";
	public static final String FILE_SPRITES = "sprites";
	public static final String FILE_STATISTICS = "statistics";
	public static final String FILE_THEMES = "themes";
	public static final String FILE_THEME = "theme";
	public static final String FILE_TOURNAMENT = "tournament";
	public static final String FILE_TRAJECTORIES = "trajectories";
	public static final String FILE_ZONE = "zone";
	public static final String FILE_ZONES = "zones";
	//
	public static final String FOLDER_ABILITIES = "abilities";
	public static final String FOLDER_BOMBS = "bombs";
	public static final String FOLDER_CHARACTERS = "characters";
	public static final String FOLDER_DATA = "data";
	public static final String FOLDER_EXPLOSIONS = "explosions";
	public static final String FOLDER_FIRE = "fire";
	public static final String FOLDER_FIRES = "fires";
	public static final String FOLDER_FONTS = "fonts";
	public static final String FOLDER_GAMES = "games";
	public static final String FOLDER_GRAPHICS = "graphics";
	public static final String FOLDER_HEROES = "heroes";
	public static final String FOLDER_ITEMS = "items";
	public static final String FOLDER_INSTANCES = "instances";
	public static final String FOLDER_LANGUAGES = "languages";
	public static final String FOLDER_LEVELS = "levels";
	public static final String FOLDER_MATCHES = "matches";
	public static final String FOLDER_MISC = "misc";
	public static final String FOLDER_MINE = "mine";
	public static final String FOLDER_NORMAL = "normal";
	public static final String FOLDER_PENETRATION = "penetration";
	public static final String FOLDER_PERMISSIONS = "permissions";
	public static final String FOLDER_PLAYERS = "players";
	public static final String FOLDER_POINTS = "points";
	public static final String FOLDER_PORTRAITS = "portraits";
	public static final String FOLDER_PROFILES = "profiles";
	public static final String FOLDER_REMOTE = "remote";
	public static final String FOLDER_RESOURCES = "resources";
	public static final String FOLDER_SCHEMAS = "schemas";
	public static final String FOLDER_SEARCH = "search";
	public static final String FOLDER_SETTINGS = "settings";
	public static final String FOLDER_SOURCE = "src";
	public static final String FOLDER_STATISTICS = "statistics";
	public static final String FOLDER_THEMES = "themes";
	public static final String FOLDER_TOURNAMENTS = "tournaments";
	public static final String FOLDER_TRAJECTORIES = "trajectories";
	public static final String FOLDER_XML = "xml";
	
	public static String getResourcesPath()
	{	return FOLDER_RESOURCES;		
	}
	
	public static String getHeroesPath()
	{	return getResourcesPath()+File.separator+FOLDER_HEROES;		
	}
	
	public static String getInstancesPath()
	{	return getResourcesPath()+File.separator+FOLDER_INSTANCES;		
	}
	
	public static String getLanguagesPath()
	{	return getResourcesPath()+File.separator+FOLDER_LANGUAGES;		
	}

	public static String getLevelsPath()
	{	return getResourcesPath()+File.separator+FOLDER_LEVELS;		
	}
	
	public static String getMiscPath()
	{	return getResourcesPath()+File.separator+FOLDER_MISC;		
	}
	public static String getFontsPath()
	{	return getMiscPath()+File.separator+FOLDER_FONTS;		
	}
	
	public static String getSchemasPath()
	{	return getResourcesPath()+File.separator+FOLDER_SCHEMAS;		
	}
	
	public static String getSettingsPath()
	{	return getResourcesPath()+File.separator+FOLDER_SETTINGS;		
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
	public static String getTournamentsPath()
	{	return getSettingsPath()+File.separator+FOLDER_TOURNAMENTS;		
	}

	public static String getStatisticsPath()
	{	return getResourcesPath()+File.separator+FOLDER_STATISTICS;		
	}
}