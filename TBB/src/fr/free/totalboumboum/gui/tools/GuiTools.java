package fr.free.totalboumboum.gui.tools;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.gui.common.structure.ButtonAware;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.tools.ImageTools;

public class GuiTools
{	
	public static void init()
	{	initFonts();
		initSizes();
		initImages();
	}
	
	public static void quickInit()
	{	initFonts();
	}

	/////////////////////////////////////////////////////////////////
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// icons
	private static final String ICON_NORMAL = "normal";
	private static final String ICON_NORMAL_SELECTED = "normal_selected";
	private static final String ICON_DISABLED = "disabled";
	private static final String ICON_DISABLED_SELECTED = "disabled_selected";
	private static final String ICON_ROLLOVER = "rollover";
	private static final String ICON_ROLLOVER_SELECTED = "rollover_selected";
	private static final String ICON_PRESSED = "pressed";
	private static BufferedImage absentImage;	
	private static final HashMap<String,BufferedImage> icons = new HashMap<String,BufferedImage>();
	
	/**
	 * get the image for the specified key	
	 */
	public static BufferedImage getIcon(String key)
	{	BufferedImage result;
		BufferedImage temp = icons.get(key);
		if(temp==null)
			result = ImageTools.getAbsentImage(64,64);
		else
			result = temp;
		return result;
	}

	/**
	 * tests if an image is stored for the specified key
	 * @param key
	 * @return
	 */
	public static boolean hasIcon(String key)
	{	return icons.containsKey(key);		
	}

	/**
	 * loads an image. If the image cannot be loaded, it is replaced by a
	 * standard image (eg a red cross) 
	 * @param path
	 * @param absent
	 * @return
	 */
	private static BufferedImage loadIcon(String path, BufferedImage absent)
	{	BufferedImage image;
		try
		{	image = ImageTools.loadImage(path,null);
		}
		catch (IOException e)
		{	image = absent;
		}
		return image;	
	}
	
	private static void initImages()
	{	// absent
		absentImage = ImageTools.getAbsentImage(64,64);
		// init
		initButtonImages();
		initHeaderImages();
		initDataImages();		
	}
	
	private static void loadButtonImages(String[] buttonStates, String folder, String[] uses)
	{	for(int i=0;i<buttonStates.length;i++)
		{	BufferedImage image = loadIcon(folder+buttonStates[i]+".png",absentImage);
			for(int j=0;j<uses.length;j++)
				icons.put(uses[j]+buttonStates[i],image);
		}
	}
	
	private static void initButtonImages()
	{	// init
		String[] buttonStates = {ICON_NORMAL,ICON_NORMAL_SELECTED,
			ICON_DISABLED,ICON_DISABLED_SELECTED,
			ICON_ROLLOVER,ICON_ROLLOVER_SELECTED,
			ICON_PRESSED};
		String baseFolder = GuiFileTools.getButtonsPath()+File.separator;
		// images
		{	String folder = baseFolder+GuiFileTools.FOLDER_DESCRIPTION+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION,
				GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION,
				GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_BLUE+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_MENU,
				GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,
				GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH,
				GuiKeys.MENU_QUICKMATCH_BUTTON_PREVIOUS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_RED+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_FINISH,
				GuiKeys.GAME_MATCH_BUTTON_FINISH,
				GuiKeys.GAME_ROUND_BUTTON_FINISH
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_PLAY+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_ROUND_BUTTON_PLAY
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_HOME+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT,
				GuiKeys.GAME_MATCH_BUTTON_QUIT,
				GuiKeys.GAME_ROUND_BUTTON_QUIT,
				GuiKeys.MENU_QUICKMATCH_BUTTON_QUIT
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RESULTS+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_RESULTS,
				GuiKeys.GAME_MATCH_BUTTON_RESULTS,
				GuiKeys.GAME_ROUND_BUTTON_RESULTS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_BLUE+File.separator;
			String[] uses = 
			{	GuiKeys.MENU_QUICKMATCH_BUTTON_NEXT
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_RED+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_CURRENT_MATCH,
				GuiKeys.GAME_TOURNAMENT_BUTTON_NEXT_MATCH,
				GuiKeys.GAME_MATCH_BUTTON_CURRENT_ROUND,
				GuiKeys.GAME_MATCH_BUTTON_NEXT_ROUND
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_STATS+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_STATISTICS,
				GuiKeys.GAME_MATCH_BUTTON_STATISTICS,
				GuiKeys.GAME_ROUND_BUTTON_STATISTICS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
	}
	
	private static void loadTableImages(String folder, String[] uses)
	{	BufferedImage image = loadIcon(folder,absentImage);
		for(int j=0;j<uses.length;j++)
			icons.put(uses[j],image);
	}
	
	private static void initHeaderImages()
	{	String baseFolder = GuiFileTools.getHeadersPath()+File.separator;
		// author
		{	String folder = baseFolder+GuiFileTools.FILE_AUTHOR;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_AUTHOR,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_AUTHOR,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_AUTHOR,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_AUTHOR,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_AUTHOR
			};
			loadTableImages(folder,uses);
		}
		// autofire
		{	String folder = baseFolder+GuiFileTools.FILE_AUTOFIRE;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_AUTO
			};
			loadTableImages(folder,uses);
		}
		// bombs
		{	String folder = baseFolder+GuiFileTools.FILE_BOMBS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_BOMBS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// color
		{	String folder = baseFolder+GuiFileTools.FILE_COLOR;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_PREVIEW_COLOR,
				GuiKeys.MENU_PROFILES_EDIT_COLOR,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_COLORS,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_COLOR,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_COLOR
			};
			loadTableImages(folder,uses);
		}
		// command
		{	String folder = baseFolder+GuiFileTools.FILE_COMMAND;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_COMMAND
			};
			loadTableImages(folder,uses);
		}
		// computer
		{	String folder = baseFolder+GuiFileTools.FILE_COMPUTER;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_PREVIEW_AINAME,
					GuiKeys.MENU_PROFILES_EDIT_AI
			};
			loadTableImages(folder,uses);
		}
		// confrontations
		{	String folder = baseFolder+GuiFileTools.FILE_CONFRONTATIONS;
			String[] uses =
			{	GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CONFRONTATIONS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CONFRONTATIONS,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_ROUNDS
			};
			loadTableImages(folder,uses);
		}
		// constant
		{	String folder = baseFolder+GuiFileTools.FILE_CONSTANT;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_CONSTANT,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_CONSTANT,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_CONSTANT
			};
			loadTableImages(folder,uses);
		}
		// crowns
		{	String folder = baseFolder+GuiFileTools.FILE_CROWNS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_CROWNS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_CROWNS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_CROWNS
			};
			loadTableImages(folder,uses);
		}
		// custom points
		{	String folder = baseFolder+GuiFileTools.FILE_CUSTOM;
			String[] uses =
			{	GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CUSTOM,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CUSTOM,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_CUSTOM,
				GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_LIMIT,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_CUSTOM_LIMIT,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_CUSTOM_LIMIT,
				GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_POINTS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_CUSTOM_POINTS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_CUSTOM_POINTS
			};
			loadTableImages(folder,uses);
		}
		// deaths
		{	String folder = baseFolder+GuiFileTools.FILE_DEATHS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBEDS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_BOMBEDS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBEDS
			};
			loadTableImages(folder,uses);
		}
		// dimension
		{	String folder = baseFolder+GuiFileTools.FILE_DIMENSION;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SIZE
			};
			loadTableImages(folder,uses);
		}
		// discretize
		{	String folder = baseFolder+GuiFileTools.FILE_DISCRETIZE;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_DISCRETIZE,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_DISCRETIZE,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_DISCRETIZE
			};
			loadTableImages(folder,uses);
		}
		// hero
		{	String folder = baseFolder+GuiFileTools.FILE_HERO;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_PREVIEW_HERONAME,
				GuiKeys.MENU_PROFILES_EDIT_HERO,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_IMAGE,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_HERO_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_HEADER
			};
			loadTableImages(folder,uses);
			loadTableImages(folder,uses);
		}
		// initial
		{	String folder = baseFolder+GuiFileTools.FILE_INITIAL;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_INITIALITEMS_TITLE
			};
			loadTableImages(folder,uses);
		}
		// instance
		{	String folder = baseFolder+GuiFileTools.FILE_INSTANCE;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_INSTANCE
			};
			loadTableImages(folder,uses);
		}
		// items
		{	String folder = baseFolder+GuiFileTools.FILE_ITEMS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_ITEMS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_ITEMS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_ITEMS,
				GuiKeys.GAME_ROUND_DESCRIPTION_ITEMSET_TITLE
			};
			loadTableImages(folder,uses);
		}
		// key
		{	String folder = baseFolder+GuiFileTools.FILE_KEY;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_KEY,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_CONTROLS,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_CONTROLS,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_CONTROLS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_BOMBINGS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_BOMBINGS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBINGS
			};
			loadTableImages(folder,uses);
		}
		// level
		{	String folder = baseFolder+GuiFileTools.FILE_LEVEL;
			String[] uses =
			{	GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_FOLDER
			};
			loadTableImages(folder,uses);
		}
		// limits
		{	String folder = baseFolder+GuiFileTools.FILE_LIMITS;
			String[] uses =
			{	GuiKeys.COMMON_LIMIT_TOURNAMENT_TITLE,
				GuiKeys.COMMON_LIMIT_MATCH_TITLE,
				GuiKeys.COMMON_LIMIT_ROUND_TITLE
			};
			loadTableImages(folder,uses);
		}
		// misc
		{	String folder = baseFolder+GuiFileTools.FILE_MISC;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_MISC_TITLE,
				GuiKeys.GAME_MATCH_DESCRIPTION_MISC_TITLE,
				GuiKeys.GAME_ROUND_DESCRIPTION_MISC_TITLE,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_NOTES
			};
			loadTableImages(folder,uses);
		}
		// name
		{	String folder = baseFolder+GuiFileTools.FILE_NAME;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_NAME,
				GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_NAME,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_NAME,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_NAME,
				GuiKeys.GAME_TOURNAMENT_STATISTICS_HEADER_NAME,
				GuiKeys.GAME_MATCH_STATISTICS_HEADER_NAME,
				GuiKeys.GAME_ROUND_STATISTICS_HEADER_NAME,
				GuiKeys.MENU_PROFILES_PREVIEW_NAME,
				GuiKeys.MENU_PROFILES_EDIT_NAME,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_HEADER
			};
			loadTableImages(folder,uses);
		}
		// pack
		{	String folder = baseFolder+GuiFileTools.FILE_PACK;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK,
				GuiKeys.MENU_PROFILES_PREVIEW_AIPACK,
				GuiKeys.MENU_PROFILES_PREVIEW_HEROPACK,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_PACKAGE,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_PACKAGE,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_PACKAGE,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_PACK
			};
			loadTableImages(folder,uses);
		}
		// paintings
		{	String folder = baseFolder+GuiFileTools.FILE_PAINTINGS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_PAINTINGS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_PAINTINGS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_PAINTINGS
			};
			loadTableImages(folder,uses);
		}
		// partial
		{	String folder = baseFolder+GuiFileTools.FILE_PARTIAL;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_PARTIAL,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_PARTIAL,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_PARTIAL
			};
			loadTableImages(folder,uses);
		}
		// players
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYERS;
			String[] uses =
			{	GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_PLAYERS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_LAST_STANDING,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_PLAYERS
			};
			loadTableImages(folder,uses);
		}
		// points
		{	String folder = baseFolder+GuiFileTools.FILE_POINTS;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_POINTS,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_POINTS,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_POINTS,
				GuiKeys.COMMON_POINTS_TOURNAMENT_TITLE,
				GuiKeys.COMMON_POINTS_MATCH_TITLE,
				GuiKeys.COMMON_POINTS_ROUND_TITLE,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_POINTS,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_POINTS
			};
			loadTableImages(folder,uses);
		}
		// profile
		{	String folder = baseFolder+GuiFileTools.FILE_PROFILE;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_PROFILE,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_PROFILE,
				GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_PROFILE,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_PROFILE,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_PROFILE,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_HEADER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HEADER
			};
			loadTableImages(folder,uses);
		}
		// rank
		{	String folder = baseFolder+GuiFileTools.FILE_RANK;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_RANK,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK,
				GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_RANKPOINTS,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_RANKPOINTS,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_RANKPOINTS
			};
			loadTableImages(folder,uses);
		}
		// score
		{	String folder = baseFolder+GuiFileTools.FILE_SCORE;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_SCORE,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_SCORE,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_SCORE
			};
			loadTableImages(folder,uses);
		}
		// source
		{	String folder = baseFolder+GuiFileTools.FILE_SOURCE;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_SOURCE,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SOURCE
			};
			loadTableImages(folder,uses);
		}
		// theme
		{	String folder = baseFolder+GuiFileTools.FILE_THEME;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_THEME
			};
			loadTableImages(folder,uses);
		}
		// time
		{	String folder = baseFolder+GuiFileTools.FILE_TIME;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_TIME,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_TIME,
				GuiKeys.GAME_ROUND_RESULTS_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_TIME
			};
			loadTableImages(folder,uses);
		}
		// title
		{	String folder = baseFolder+GuiFileTools.FILE_TITLE;
			String[] uses =
			{	GuiKeys.GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PREVIEW_NAME,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PREVIEW_NAME,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_NAME,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_PREVIEW_NAME,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_PREVIEW_NAME
			};
			loadTableImages(folder,uses);
		}
		// total
		{	String folder = baseFolder+GuiFileTools.FILE_TOTAL;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_RESULTS_HEADER_TOTAL,
				GuiKeys.GAME_MATCH_RESULTS_HEADER_TOTAL,
				GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_TOTAL,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_TOTAL
			};
			loadTableImages(folder,uses);
		}
	}
	
	private static void initDataImages()
	{	String baseFolder = GuiFileTools.getDataPath()+File.separator;
		// bombs
		{	String folder = baseFolder+GuiFileTools.FILE_BOMBS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_BOMBS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_BOMBS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// computer
		{	String folder = baseFolder+GuiFileTools.FILE_COMPUTER;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_DATA_COMPUTER,
				GuiKeys.GAME_TOURNAMENT_RESULTS_DATA_COMPUTER,
				GuiKeys.GAME_MATCH_RESULTS_DATA_COMPUTER,
				GuiKeys.GAME_ROUND_RESULTS_DATA_COMPUTER,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_COMPUTER,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_COMPUTER
			};
			loadTableImages(folder,uses);
		}
		// crowns
		{	String folder = baseFolder+GuiFileTools.FILE_CROWNS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_CROWNS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_CROWNS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_CROWNS
			};
			loadTableImages(folder,uses);
		}
		// deaths
		{	String folder = baseFolder+GuiFileTools.FILE_DEATHS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_BOMBEDS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_BOMBEDS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBEDS
			};
			loadTableImages(folder,uses);
		}
		// edit
		{	String folder = baseFolder+GuiFileTools.FILE_EDIT;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_EDIT_AI_CHANGE,
				GuiKeys.MENU_PROFILES_EDIT_HERO_CHANGE,
				GuiKeys.MENU_PROFILES_EDIT_NAME_CHANGE,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_ADD,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_ADD,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE
			};
			loadTableImages(folder,uses);
		}
		// false
		{	String folder = baseFolder+GuiFileTools.FILE_FALSE;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE,
				GuiKeys.MENU_OPTIONS_VIDEO_LINE_DISABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED,
				GuiKeys.MENU_PROFILES_EDIT_AI_RESET,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_PROFILE_DELETE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_DELETE
			};
			loadTableImages(folder,uses);
		}
		// human
		{	String folder = baseFolder+GuiFileTools.FILE_HUMAN;
			String[] uses =
			{	GuiKeys.GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN,
				GuiKeys.GAME_MATCH_DESCRIPTION_PLAYERS_DATA_HUMAN,
				GuiKeys.GAME_TOURNAMENT_RESULTS_DATA_HUMAN,
				GuiKeys.GAME_MATCH_RESULTS_DATA_HUMAN,
				GuiKeys.GAME_ROUND_RESULTS_DATA_HUMAN,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_TYPE_HUMAN,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HUMAN
			};
			loadTableImages(folder,uses);
		}
		// inverted order
		{	String folder = baseFolder+GuiFileTools.FILE_INVERTED;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_INVERTED,
				GuiKeys.COMMON_POINTS_MATCH_DATA_INVERTED,
				GuiKeys.COMMON_POINTS_ROUND_DATA_INVERTED
			};
			loadTableImages(folder,uses);
		}
		// items
		{	String folder = baseFolder+GuiFileTools.FILE_ITEMS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_ITEMS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_ITEMS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_ITEMS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_BOMBINGS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_BOMBINGS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBINGS
			};
			loadTableImages(folder,uses);
		}
		// minus
		{	String folder = baseFolder+GuiFileTools.FILE_MINUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_VIDEO_LINE_MINUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS
			};
			loadTableImages(folder,uses);
		}
		// next
		{	String folder = baseFolder+GuiFileTools.FILE_NEXT;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT,
				GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_NEXT,
				GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT,
				GuiKeys.MENU_PROFILES_EDIT_COLOR_NEXT
			};
			loadTableImages(folder,uses);
		}
		// no share
		{	String folder = baseFolder+GuiFileTools.FILE_NOSHARE;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_NOSHARE,
				GuiKeys.COMMON_POINTS_MATCH_DATA_NOSHARE,
				GuiKeys.COMMON_POINTS_ROUND_DATA_NOSHARE
			};
			loadTableImages(folder,uses);
		}
		// page down
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_DOWN;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_LIST_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_AI_SELECT_CLASS_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PACKAGE_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_FOLDER_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PACKAGE_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_LIST_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_LIST_PAGEDOWN
			};
			loadTableImages(folder,uses);
		}
		// page up
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_UP;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_LIST_PAGEUP,
				GuiKeys.MENU_RESOURCES_AI_SELECT_CLASS_PAGEUP,
				GuiKeys.MENU_RESOURCES_AI_SELECT_PACKAGE_PAGEUP,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_FOLDER_PAGEUP,
				GuiKeys.MENU_RESOURCES_HERO_SELECT_PACKAGE_PAGEUP,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEUP,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEUP,
				GuiKeys.MENU_RESOURCES_MATCH_SELECT_LIST_PAGEUP,
				GuiKeys.MENU_RESOURCES_ROUND_SELECT_LIST_PAGEUP
			};
			loadTableImages(folder,uses);
		}
		// paintings
		{	String folder = baseFolder+GuiFileTools.FILE_PAINTINGS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_PAINTINGS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_PAINTINGS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_PAINTINGS
			};
			loadTableImages(folder,uses);
		}
		// partial
		{	String folder = baseFolder+GuiFileTools.FILE_PARTIAL;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_PARTIAL,
				GuiKeys.COMMON_POINTS_MATCH_DATA_PARTIAL,
				GuiKeys.COMMON_POINTS_ROUND_DATA_PARTIAL
			};
			loadTableImages(folder,uses);
		}
		// plus
		{	String folder = baseFolder+GuiFileTools.FILE_PLUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_VIDEO_LINE_PLUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS
			};
			loadTableImages(folder,uses);
		}
		// previous
		{	String folder = baseFolder+GuiFileTools.FILE_PREVIOUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS,
				GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS,
				GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS,
				GuiKeys.MENU_PROFILES_EDIT_COLOR_PREVIOUS
			};
			loadTableImages(folder,uses);
		}
		// regular order
		{	String folder = baseFolder+GuiFileTools.FILE_REGULAR;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_REGULAR,
				GuiKeys.COMMON_POINTS_MATCH_DATA_REGULAR,
				GuiKeys.COMMON_POINTS_ROUND_DATA_REGULAR
			};
			loadTableImages(folder,uses);
		}
		// share
		{	String folder = baseFolder+GuiFileTools.FILE_SHARE;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_SHARE,
				GuiKeys.COMMON_POINTS_MATCH_DATA_SHARE,
				GuiKeys.COMMON_POINTS_ROUND_DATA_SHARE
			};
			loadTableImages(folder,uses);
		}
		// time
		{	String folder = baseFolder+GuiFileTools.FILE_TIME;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_TIME,
				GuiKeys.COMMON_POINTS_MATCH_DATA_TIME,
				GuiKeys.COMMON_POINTS_ROUND_DATA_TIME
			};
			loadTableImages(folder,uses);
		}
		// true
		{	String folder = baseFolder+GuiFileTools.FILE_TRUE;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE,
				GuiKeys.MENU_OPTIONS_VIDEO_LINE_ENABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED
			};
			loadTableImages(folder,uses);
		}
	}
				
	/////////////////////////////////////////////////////////////////
	// FONTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static Graphics graphics;
	public final static float FONT_RATIO = 0.8f; // font height relatively to the containing label (or component)

	private static void initFonts()
	{	BufferedImage img = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);
		graphics = img.getGraphics();			
	}
	
	/**
	 * process the maximal font size for the specified height limit,
	 * whatever the displayed text will be
	 * @param limit
	 * @return
	 */
	public static int getFontSize(double limit)
	{	int result = 0;
		int fheight;
		do
		{	result++;
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<limit);
		return result;
	}

	/**
	 * process the maximal font size for the specified width and height limits
	 * and given text.
	 * @param width
	 * @param height
	 * @param text
	 * @return
	 */
	public static int getFontSize(double width, double height, String text)
	{	int result = 0;
		int fheight,fwidth;
		do
		{	result++;
			Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
			Rectangle2D bounds = metrics.getStringBounds(text,graphics);
			fwidth = (int)bounds.getWidth();
		}
		while(fheight<height && fwidth<width);
		return result-1;
	}
	
	/**
	 * process the pixel height corresponding to the specified font size 
	 * @param fontSize
	 * @return
	 */
	public static int getPixelHeight(float fontSize)
	{	int result;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		result = (int)(metrics.getHeight()*1.2);
		return result;
	}
	
	/**
	 * process the pixel width corresponding to the specified font size
	 * @param fontSize
	 * @param text
	 * @return
	 */
	public static int getPixelWidth(float fontSize, String text)
	{	int result;
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont(fontSize);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		Rectangle2D bounds = metrics.getStringBounds(text,graphics);
		result = (int)bounds.getWidth();
		return result;
	}
	
	/**
	 * process the maximal font size fot the given dimensions and the set of texts
	 * @param width
	 * @param height
	 * @param texts
	 * @return
	 */
	public static int getOptimalFontSize(double width, double height, ArrayList<String> texts)
	{	int result;
		Iterator<String> it = texts.iterator();
		int longest = 0;
		String longestString = null;
		while(it.hasNext())
		{	String text = it.next();
			int length = getPixelWidth(10,text);
			if(length>longest)
			{	longest = length;
				longestString = text;
			}
		}
		result = getFontSize(width,height,longestString);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SIZE 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	// margins
	private final static float PANEL_MARGIN_RATIO = 0.025f; 
	public static int panelMargin; // margin between the components of a frame
	private final static float SUBPANEL_MARGIN_RATIO = 0.005f; 
	public static int subPanelMargin;// margin between the components of a panel

	// titles
	private final static float SUBPANEL_TITLE_RATIO = 1.5f; // subpanel title height relatively to panel margin
	public static int subPanelTitleHeight; // height of a subpanel title bar relatively to the height of a panel title
	public final static float TABLE_HEADER_RATIO = 1.2f; //header high relatively to line height

	// panel split
	public final static float VERTICAL_SPLIT_RATIO = 0.25f;
	public final static float HORIZONTAL_SPLIT_RATIO = 0.07f;

	// buttons
	private final static float BUTTON_TEXT_HEIGHT_RATIO = 0.05f; // height of a button relatively to the panel height
	public static int buttonTextHeight;
	private final static float BUTTON_TEXT_WIDTH_RATIO = 0.33f; // width of a button relatively to the panel width
	public static int buttonTextWidth;
//	private final static float BUTTON_ICON_SIZE_RATIO = 0.07f; // height of a button relatively to the panel height
//	public static int buttonIconSize;
	private final static float BUTTON_HORIZONTAL_SPACE_RATIO = 0.025f; // space between buttons relatively to the panel width
	public static int buttonHorizontalSpace;
	private final static float BUTTON_VERTICAL_SPACE_RATIO = 0.025f; // space between buttons relatively to the panel height
	public static int buttonVerticalSpace;
	public final static float BUTTON_ICON_MARGIN_RATION = 0.9f;
	
	private static void initSizes()
	{	// panel
		Dimension panelDimension = Configuration.getVideoConfiguration().getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
				
		// margins
		panelMargin = (int)(width*PANEL_MARGIN_RATIO);
		subPanelMargin = (int)(height*SUBPANEL_MARGIN_RATIO);
		
		// titles
		subPanelTitleHeight = (int)(panelMargin*SUBPANEL_TITLE_RATIO);
		subPanelTitleHeight = (int)(panelMargin*SUBPANEL_TITLE_RATIO);
		
		// buttons
		buttonTextHeight = (int)(height*BUTTON_TEXT_HEIGHT_RATIO);
		buttonTextWidth = (int)(width*BUTTON_TEXT_WIDTH_RATIO);
//		buttonIconSize = (int)(height*BUTTON_ICON_SIZE_RATIO);
		buttonHorizontalSpace = (int)(width*BUTTON_HORIZONTAL_SPACE_RATIO);
		buttonVerticalSpace = (int)(height*BUTTON_VERTICAL_SPACE_RATIO);
	}
	
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public final static Color COLOR_SPLASHSCREEN_TEXT = new Color(204,18,128);
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,150);
	public final static Color COLOR_TITLE_FOREGROUND = Color.BLACK;
	public final static Color COLOR_TABLE_SELECTED_BACKGROUND = new Color(204,18,128,80);
	public final static Color COLOR_TABLE_SELECTED_DARK_BACKGROUND = new Color(204,18,128,130);
	public final static Color COLOR_TABLE_REGULAR_BACKGROUND = new Color(0,0,0,80);
	public final static Color COLOR_TABLE_NEUTRAL_BACKGROUND = new Color(0,0,0,20);
	public final static Color COLOR_TABLE_REGULAR_FOREGROUND = Color.BLACK;
	public final static Color COLOR_TABLE_HEADER_BACKGROUND = new Color(0,0,0,130);
	public final static Color COLOR_TABLE_HEADER_FOREGROUND = Color.WHITE;
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1 = 80; //scores
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2 = 140; // rounds/matches
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3 = 200; //portrait/name/total/points
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * automatically define the content of a button : images or text
	 */
	public static void setButtonContent(String name, AbstractButton button)
	{	// content
		if(icons.containsKey(name+ICON_NORMAL))
		{	// normal icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setIcon(ii);
			}
			// disabled icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledIcon(ii);
			}
			// pressed icon
			{	BufferedImage icon = getIcon(name+ICON_PRESSED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setPressedIcon(ii);
			}
			// selected icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setSelectedIcon(ii);
			}
			// disabled selected icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledSelectedIcon(ii);
			}
			// rollover icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverEnabled(true);
				button.setRolloverIcon(ii);
			}
			// rollover selected icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverSelectedIcon(ii);
			}
			// 
			button.setBorderPainted(false);
			button.setBorder(null);
			button.setMargin(null);
	        button.setContentAreaFilled(false);
	        button.setFocusPainted(false);
		}
		else
		{	// text 
			String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(name);
			button.setText(text);
		}		
		// action command
		button.setActionCommand(name);
		// tooltip
		String tooltipKey = name+GuiKeys.TOOLTIP;
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(tooltipKey);
		button.setToolTipText(tooltip);
	}		
	
	/**
	 * automatically initializes a button and its content
	 * @param result
	 * @param name
	 * @param width
	 * @param height
	 * @param panel
	 */
	private static void initButton(AbstractButton result, String name, int width, int height, ButtonAware panel)
	{	// dimension
		Dimension dim = new Dimension(width,height);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// set text
		setButtonContent(name,result);
		// add to panel
		panel.add(result);
		result.addActionListener(panel);
	}
	
	public static JButton createButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}

	public static JToggleButton createToggleButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}
}
