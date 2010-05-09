package fr.free.totalboumboum.gui.tools;

import java.io.File;

public class GuiFileTools
{
	public static final String EXTENSION_SCHEMA = ".xsd";
	public static final String EXTENSION_DATA = ".xml";

	// files
	public final static String FILE_BOMBS = "bombs.png";
	public final static String FILE_CROWNS = "crowns.png";
	public final static String FILE_DEATHS = "deaths.png";
	public final static String FILE_FRAGS = "frags.png";
	public final static String FILE_FRAME = "frame.png";
	public final static String FILE_GUI = "gui";
	public final static String FILE_ITEMS = "items.png";
	public final static String FILE_KILLS = "kills.png";
	public final static String FILE_NAME = "name.png";
	public final static String FILE_PAINTINGS = "paintings.png";
	public final static String FILE_POINTS = "points.png";
	public final static String FILE_RANK = "rank.png";
	public final static String FILE_TIME = "time.png";
	public final static String FILE_TOTAL = "total.png";

	// folders
	public static final String FOLDER_BUTTONS = "buttons";
	public final static String FOLDER_DESCRIPTION = "description";
	public static final String FOLDER_FONTS = "fonts";
	public static final String FOLDER_GUI = "gui";
	public static final String FOLDER_HEADERS = "headers";
	public final static String FOLDER_HOME = "home";
	public static final String FOLDER_ICONS = "icons";
	public static final String FOLDER_IMAGES = "images";
	public static final String FOLDER_LANGUAGES = "languages";
	public final static String FOLDER_LEFT_BLUE = "left_blue";
	public final static String FOLDER_LEFT_RED = "left_red";
	public final static String FOLDER_PLAY = "play";
	public final static String FOLDER_RESULTS = "results";
	public final static String FOLDER_RIGHT_BLUE = "right_blue";
	public final static String FOLDER_RIGHT_RED = "right_red";
	public final static String FOLDER_STATS = "stats";

	public static String getGuiPath()
	{	return fr.free.totalboumboum.tools.FileTools.getResourcesPath()+File.separator+FOLDER_GUI;		
	}
	public static String getFontsPath()
	{	return getGuiPath()+File.separator+FOLDER_FONTS;		
	}
	public static String getIconsPath()
	{	return getGuiPath()+File.separator+FOLDER_ICONS;		
	}
	public static String getImagesPath()
	{	return getGuiPath()+File.separator+FOLDER_IMAGES;		
	}
	public static String getLanguagesPath()
	{	return getGuiPath()+File.separator+FOLDER_LANGUAGES;		
	}
	
}
