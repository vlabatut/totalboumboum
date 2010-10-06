package org.totalboumboum.gui.tools;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.gui.common.structure.ButtonAware;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.images.ImageTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiTools
{	
	/////////////////////////////////////////////////////////////////
	// INIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void init()
	{	initFonts();
		initSizes();
		initImages();
	}
	
	public static void quickInit()
	{	initFonts();
	}

	/////////////////////////////////////////////////////////////////
	// STARTUP			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final int STARTUP_XML = 0;
	public static final int STARTUP_CONFIG = 1;
	public static final int STARTUP_GUI = 2;
	public static final int STARTUP_INIT = 3;
	public static final int STARTUP_STATS = 4;
	public static final int STARTUP_DONE = 5;
	public static final String STARTUP_MESSAGES[] = 
	{	"[Loading XML schemas]",
		"[Loading configuration]",
		"[Loading GUI]",
		"[Initializing GUI]",
		"[Loading statistics]",
		"[Done]"
	};
	public static final String STARTUP_LEGAL[] = 
	{	"Total Boum Boum version "+GameData.VERSION,
		new Character('\u00A9').toString()+" 2008-2010 Vincent Labatut",
		"Licensed under the GPL v2"
	};
	
	/////////////////////////////////////////////////////////////////
	// HELP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final int OPTION_HELP = 0;
	public static final String OPTION_HELP_MESSAGE = "In-line parameters allowed for this software:";
	public static final int OPTION_QUICK = 1;
	public static final int OPTION_WINDOW = 2;
	public static final String OPTIONS[] = 
	{	"help",
		"quick",
		"window"
	};
	public static final String OPTIONS_HELP[] = 
	{	"show this page (and does not launch the game)",
		"launch the game in quick mode, i.e. with a minimal graphical interface, and allows playing only one predefined round",
		"force the game to be displayed in a window, even if full screen is set in the game options"
	};

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
			image = ImageTools.getCompatibleImage(image);		
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
		{	String folder = baseFolder+GuiFileTools.FOLDER_CAMERA+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_RECORD_GAMES,
				GuiKeys.GAME_MATCH_BUTTON_RECORD_GAMES,
				GuiKeys.GAME_ROUND_BUTTON_RECORD_GAMES
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_DESCRIPTION+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_DESCRIPTION,
				GuiKeys.GAME_MATCH_BUTTON_DESCRIPTION,
				GuiKeys.GAME_ROUND_BUTTON_DESCRIPTION
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_HOME+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_QUIT,
				GuiKeys.GAME_MATCH_BUTTON_QUIT,
				GuiKeys.GAME_ROUND_BUTTON_QUIT,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_QUIT,
				GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_QUIT,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT,
				GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT,
				GuiKeys.MENU_REPLAY_ROUND_BUTTON_QUIT,
				GuiKeys.MENU_NETWORK_BUTTON_QUIT
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_BLUE+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_MENU,
				GuiKeys.GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,
				GuiKeys.GAME_ROUND_BUTTON_CURRENT_MATCH,
				GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS,
				GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS,
				GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS,
				GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS,
				GuiKeys.MENU_REPLAY_ROUND_BUTTON_BACK,
				GuiKeys.MENU_NETWORK_GAMES_BUTTON_PREVIOUS,
				GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS
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
		{	String folder = baseFolder+GuiFileTools.FOLDER_NETWORK+File.separator;
			String[] uses = 
			{	GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_PUBLISH,
				GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_PLAY+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_ROUND_BUTTON_PLAY,
				GuiKeys.MENU_REPLAY_ROUND_BUTTON_REPLAY
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_PLAYERS_BLOCKING+File.separator;
			String[] uses = 
			{	GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_BLOCK_PLAYERS,
				GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_BLOCK_PLAYERS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_PLAYERS_SELECTING+File.separator;
			String[] uses = 
			{	GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_VALIDATE
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
			{	GuiKeys.MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT,
				GuiKeys.MENU_QUICKMATCH_LEVELS_BUTTON_NEXT,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT,
				GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT,
				GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT,
				GuiKeys.MENU_NETWORK_GAMES_BUTTON_NEXT
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
		{	String folder = baseFolder+GuiFileTools.FOLDER_SAVE+File.separator;
			String[] uses = 
			{	GuiKeys.GAME_TOURNAMENT_BUTTON_SAVE,
				GuiKeys.GAME_MATCH_BUTTON_SAVE,
				GuiKeys.GAME_ROUND_BUTTON_SAVE
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
		// address
		{	String folder = baseFolder+GuiFileTools.FILE_ADDRESS;
			String[] uses =
			{	GuiKeys.COMMON_HOST_INFO_IP,
				GuiKeys.COMMON_GAME_LIST_HEADER_HOST_IP
			};
			loadTableImages(folder,uses);
		}
		// author
		{	String folder = baseFolder+GuiFileTools.FILE_AUTHOR;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_AUTHOR,
				GuiKeys.COMMON_AI_AUTHOR,
				GuiKeys.COMMON_SPRITE_AUTHOR,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_AUTHOR,
				GuiKeys.COMMON_MATCH_AUTHOR,
				GuiKeys.COMMON_ROUND_AUTHOR,
				GuiKeys.COMMON_TOURNAMENT_AUTHOR
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
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_BOMBS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_BOMBS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBS,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// color
		{	String folder = baseFolder+GuiFileTools.FILE_COLOR;
			String[] uses =
			{	GuiKeys.COMMON_PROFILES_COLOR,
				GuiKeys.MENU_PROFILES_EDIT_COLOR,
				GuiKeys.COMMON_SPRITE_COLORS,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_COLOR
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
			{	GuiKeys.COMMON_PROFILES_AI_NAME,
					GuiKeys.MENU_PROFILES_EDIT_AI
			};
			loadTableImages(folder,uses);
		}
		// confrontations
		{	String folder = baseFolder+GuiFileTools.FILE_CONFRONTATIONS;
			String[] uses =
			{	GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CONFRONTATIONS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CONFRONTATIONS,
				GuiKeys.COMMON_MATCH_ROUND_COUNT,
				GuiKeys.COMMON_ARCHIVE_CONFRONTATIONS,
				GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT,
				GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_PLAYED,
				GuiKeys.COMMON_HOST_INFO_PLAYED,
				GuiKeys.COMMON_GAME_LIST_HEADER_PLAYED
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
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_CROWNS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_CROWNS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CROWNS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_CROWNS,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_CROWNS
			};
			loadTableImages(folder,uses);
		}
		// custom points
		{	String folder = baseFolder+GuiFileTools.FILE_CUSTOM;
			String[] uses =
			{	GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_CUSTOM,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_CUSTOM,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_CUSTOM,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_LIMIT,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_LIMIT,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_LIMIT
			};
			loadTableImages(folder,uses);
		}
		// deaths
		{	String folder = baseFolder+GuiFileTools.FILE_DEATHS;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_BOMBEDS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_BOMBEDS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_BOMBEDS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBEDS,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBEDS
			};
			loadTableImages(folder,uses);
		}
		// dimension
		{	String folder = baseFolder+GuiFileTools.FILE_DIMENSION;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_DIMENSION,
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
		// draw
		{	String folder = baseFolder+GuiFileTools.FILE_DRAW;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_DRAWN
			};
			loadTableImages(folder,uses);
		}
		// edit
		{	String folder = baseFolder+GuiFileTools.FILE_EDIT;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_HEADER_BUTTON
			};
			loadTableImages(folder,uses);
		}
		// evolution
		{	String folder = baseFolder+GuiFileTools.FILE_EVOLUTION;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_EVOLUTION,
				GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_STATE,
				GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE
			};
			loadTableImages(folder,uses);
		}
		// false
		{	String folder = baseFolder+GuiFileTools.FILE_FALSE;
			String[] uses =
			{	GuiKeys.COMMON_DIALOG_CANCEL,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_DELETE
			};
			loadTableImages(folder,uses);
		}
		// hero
		{	String folder = baseFolder+GuiFileTools.FILE_HERO;
			String[] uses =
			{	GuiKeys.COMMON_PROFILES_HERO_NAME,
				GuiKeys.MENU_PROFILES_EDIT_HERO,
				GuiKeys.COMMON_SPRITE_IMAGE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_HERO,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_PORTRAIT,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_PORTRAIT,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_PORTRAIT,
				GuiKeys.COMMON_PLAYERS_LIST_HEADER_HERO,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT,
				GuiKeys.COMMON_GAME_INFO_PLAYER_COUNT,
				GuiKeys.COMMON_GAME_LIST_HEADER_PLAYER_COUNT
			};
			loadTableImages(folder,uses);
			loadTableImages(folder,uses);
		}
		// initial
		{	String folder = baseFolder+GuiFileTools.FILE_INITIAL;
			String[] uses =
			{	GuiKeys.COMMON_ITEMS_INITIAL_TITLE
			};
			loadTableImages(folder,uses);
		}
		// instance
		{	String folder = baseFolder+GuiFileTools.FILE_INSTANCE;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_INSTANCE,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_INSTANCE
			};
			loadTableImages(folder,uses);
		}
		// items
		{	String folder = baseFolder+GuiFileTools.FILE_ITEMS;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_ITEMS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_ITEMS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_ITEMS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_ITEMS,
				GuiKeys.COMMON_ITEMS_AVAILABLE_TITLE,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_ITEMS
			};
			loadTableImages(folder,uses);
		}
		// key
		{	String folder = baseFolder+GuiFileTools.FILE_KEY;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_HEADER_KEY,
				GuiKeys.COMMON_PLAYERS_LIST_HEADER_CONTROLS,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_CONTROLS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_BOMBINGS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_BOMBINGS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_BOMBINGS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_BOMBINGS,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBINGS
			};
			loadTableImages(folder,uses);
		}
		// laurels
		{	String folder = baseFolder+GuiFileTools.FILE_LAURELS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_RANKINGS,
				GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_RANKPOINTS,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_RANKPOINTS,
				GuiKeys.COMMON_POINTS_ROUND_HEADER_RANKPOINTS,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_RANK,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_RANK,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_RANK,
				GuiKeys.COMMON_PART_RANK
			};
			loadTableImages(folder,uses);
		}
		// level
		{	String folder = baseFolder+GuiFileTools.FILE_LEVEL;
			String[] uses =
			{	GuiKeys.COMMON_ROUND_LEVEL_FOLDER,
				GuiKeys.COMMON_LEVEL_TITLE,
				GuiKeys.COMMON_REPLAY_NAME
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
		// lose
		{	String folder = baseFolder+GuiFileTools.FILE_LOSE;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_LOST
			};
			loadTableImages(folder,uses);
		}
		// mean
		{	String folder = baseFolder+GuiFileTools.FILE_MEAN;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN
			};
			loadTableImages(folder,uses);
		}
		// misc
		{	String folder = baseFolder+GuiFileTools.FILE_MISC;
			String[] uses =
			{	GuiKeys.MENU_RESOURCES_AI_SELECT_NOTES,
				GuiKeys.COMMON_TOURNAMENT_TYPE,
				GuiKeys.COMMON_ARCHIVE_TYPE,
			};
			loadTableImages(folder,uses);
		}
		// name
		{	String folder = baseFolder+GuiFileTools.FILE_NAME;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_NAME,
				GuiKeys.COMMON_PLAYERS_LIST_HEADER_NAME,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_NAME,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_NAME,
				GuiKeys.COMMON_PROFILES_NAME,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_PROFILE,
				GuiKeys.COMMON_ARCHIVE_PLAYERS,
				GuiKeys.COMMON_PART_PLAYER,
				GuiKeys.MENU_PROFILES_EDIT_NAME,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_NAME,
				GuiKeys.COMMON_REPLAY_PLAYERS
			};
			loadTableImages(folder,uses);
		}
		// next
		{	String folder = baseFolder+GuiFileTools.FILE_NEXT;
			String[] uses =
			{	GuiKeys.COMMON_TRANSFER_RIGHT
			};
			loadTableImages(folder,uses);
		}
		// pack
		{	String folder = baseFolder+GuiFileTools.FILE_PACK;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_PACK,
				GuiKeys.COMMON_PROFILES_AI_PACK,
				GuiKeys.COMMON_PROFILES_HERO_PACK,
				GuiKeys.COMMON_AI_PACK,
				GuiKeys.COMMON_SPRITE_PACK,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_PACKAGE,
				GuiKeys.COMMON_ROUND_LEVEL_PACK,
				GuiKeys.COMMON_REPLAY_PACK
			};
			loadTableImages(folder,uses);
		}
		// paintings
		{	String folder = baseFolder+GuiFileTools.FILE_PAINTINGS;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_PAINTINGS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_PAINTINGS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_PAINTINGS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_PAINTINGS,
				GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_PAINTINGS
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
		// player unselected
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYER_UNSELECTED;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_READY
			};
			loadTableImages(folder,uses);
		}
		// players
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYERS;
			String[] uses =
			{	GuiKeys.COMMON_MATCH_ALLOWED_PLAYERS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_LAST_STANDING,
				GuiKeys.COMMON_ROUND_ALLOWED_PLAYERS,
				GuiKeys.COMMON_LEVEL_ALLOWED_PLAYERS,
				GuiKeys.COMMON_TOURNAMENT_ALLOWED_PLAYERS,
				GuiKeys.COMMON_GAME_INFO_ALLOWED_PLAYERS,
				GuiKeys.COMMON_GAME_LIST_HEADER_ALLOWED_PLAYER
			};
			loadTableImages(folder,uses);
		}
		// points
		{	String folder = baseFolder+GuiFileTools.FILE_POINTS;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_POINTS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_POINTS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_POINTS,
				GuiKeys.COMMON_POINTS_TOURNAMENT_TITLE,
				GuiKeys.COMMON_POINTS_MATCH_TITLE,
				GuiKeys.COMMON_POINTS_ROUND_TITLE			};
			loadTableImages(folder,uses);
		}
		// previous
		{	String folder = baseFolder+GuiFileTools.FILE_PREVIOUS;
			String[] uses =
			{	GuiKeys.COMMON_TRANSFER_LEFT
			};
			loadTableImages(folder,uses);
		}
		// profile
		{	String folder = baseFolder+GuiFileTools.FILE_PROFILE;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_LIST_HEADER_PROFILE,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_PROFILE,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_PROFILE,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_PROFILE,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_TYPE,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_TYPE
			};
			loadTableImages(folder,uses);
		}
		// rank
		{	String folder = baseFolder+GuiFileTools.FILE_RANK;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_LIST_HEADER_RANK,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_HEADER_RANK,
				GuiKeys.COMMON_PLAYERS_SELECTION_HEADER_RANK,
				GuiKeys.COMMON_PROFILES_RANK,
				GuiKeys.COMMON_GAME_INFO_AVERAGE_SCORE,
				GuiKeys.COMMON_GAME_LIST_HEADER_AVERAGE_LEVEL
			};
			loadTableImages(folder,uses);
		}
		// save
		{	String folder = baseFolder+GuiFileTools.FILE_SAVE;
			String[] uses =
			{	GuiKeys.COMMON_ARCHIVE_SAVE,
				GuiKeys.COMMON_REPLAY_SAVE
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
			{	GuiKeys.COMMON_LEVEL_SOURCE,
				GuiKeys.COMMON_SPRITE_SOURCE,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SOURCE,
				GuiKeys.COMMON_HOST_INFO_TYPE
			};
			loadTableImages(folder,uses);
		}
		// standard-deviation
		{	String folder = baseFolder+GuiFileTools.FILE_STDEV;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION
			};
			loadTableImages(folder,uses);
		}
		// star
		{	String folder = baseFolder+GuiFileTools.FILE_STAR;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_HEADER_PREFERRED,
				GuiKeys.COMMON_HOST_INFO_PREFERRED
			};
			loadTableImages(folder,uses);
		}
		// suicides
		{	String folder = baseFolder+GuiFileTools.FILE_SUICIDES;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_SCORES_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_SELF_BOMBINGS,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_SELF_BOMBINGS
			};
			loadTableImages(folder,uses);
		}
		// theme
		{	String folder = baseFolder+GuiFileTools.FILE_THEME;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_THEME,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_THEME
			};
			loadTableImages(folder,uses);
		}
		// time
		{	String folder = baseFolder+GuiFileTools.FILE_TIME;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_TIME,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_TIME,
				GuiKeys.COMMON_RESULTS_ROUND_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_TOURNAMENT_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_MATCH_HEADER_TIME,
				GuiKeys.COMMON_LIMIT_ROUND_HEADER_TIME,
				GuiKeys.COMMON_ARCHIVE_START,
				GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED
			};
			loadTableImages(folder,uses);
		}
		// title
		{	String folder = baseFolder+GuiFileTools.FILE_TITLE;
			String[] uses =
			{	GuiKeys.COMMON_LEVEL_NAME,
				GuiKeys.COMMON_AI_NAME,
				GuiKeys.COMMON_SPRITE_NAME,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PREVIEW_NAME,
				GuiKeys.COMMON_MATCH_NAME,
				GuiKeys.COMMON_ROUND_TITLE,
				GuiKeys.COMMON_TOURNAMENT_NAME,
				GuiKeys.COMMON_ARCHIVE_NAME,
				GuiKeys.COMMON_HOST_INFO_NAME,
				GuiKeys.COMMON_GAME_INFO_TOURNAMENT_NAME,
				GuiKeys.COMMON_GAME_LIST_HEADER_HOST_NAME
			};
			loadTableImages(folder,uses);
		}
		// total
		{	String folder = baseFolder+GuiFileTools.FILE_TOTAL;
			String[] uses =
			{	GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_TOTAL,
				GuiKeys.COMMON_RESULTS_MATCH_HEADER_TOTAL,
				GuiKeys.COMMON_POINTS_TOURNAMENT_HEADER_TOTAL,
				GuiKeys.COMMON_POINTS_MATCH_HEADER_TOTAL
			};
			loadTableImages(folder,uses);
		}
		// tournament
		{	String folder = baseFolder+GuiFileTools.FILE_TOURNAMENT;
			String[] uses =
			{	GuiKeys.COMMON_GAME_INFO_TOURNAMENT_TYPE,
				GuiKeys.COMMON_GAME_LIST_HEADER_TOURNAMENT_TYPE
			};
			loadTableImages(folder,uses);
		}
		// true
		{	String folder = baseFolder+GuiFileTools.FILE_TRUE;
			String[] uses =
			{	GuiKeys.COMMON_DIALOG_CONFIRM
			};
			loadTableImages(folder,uses);
		}
		// volatility
		{	String folder = baseFolder+GuiFileTools.FILE_VOLATILITY;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY
			};
			loadTableImages(folder,uses);
		}
		// win
		{	String folder = baseFolder+GuiFileTools.FILE_WIN;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_HEADER_WON
			};
			loadTableImages(folder,uses);
		}
	}
	
	private static void initDataImages()
	{	String baseFolder = GuiFileTools.getDataPath()+File.separator;
		// average
		{	String folder = baseFolder+GuiFileTools.FILE_AVERAGE;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_MEAN
			};
			loadTableImages(folder,uses);
		}
		// bombs
		{	String folder = baseFolder+GuiFileTools.FILE_BOMBS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_BOMBS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_BOMBS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// central
		{	String folder = baseFolder+GuiFileTools.FILE_CENTRAL;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_BUTTON_CENTRAL
			};
			loadTableImages(folder,uses);
		}
		// closed
		{	String folder = baseFolder+GuiFileTools.FILE_CLOSED;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_CLOSED
			};
			loadTableImages(folder,uses);
		}
		// command
		{	String folder = baseFolder+GuiFileTools.FILE_COMMAND;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_RETRIEVING,
				GuiKeys.COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_RETRIEVING
			};
			loadTableImages(folder,uses);
		}
		// computer
		{	String folder = baseFolder+GuiFileTools.FILE_COMPUTER;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_LIST_DATA_COMPUTER,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_DATA_COMPUTER,
				GuiKeys.COMMON_RESULTS_MATCH_DATA_COMPUTER,
				GuiKeys.COMMON_RESULTS_ROUND_DATA_COMPUTER,
				GuiKeys.COMMON_PLAYERS_SELECTION_DATA_COMPUTER,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_COMPUTER,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_AI
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
		// cup
		{	String folder = baseFolder+GuiFileTools.FILE_CUP;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_TYPE_CUP
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
		// direct
		{	String folder = baseFolder+GuiFileTools.FILE_DIRECT;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_BUTTON_DIRECT
			};
			loadTableImages(folder,uses);
		}
		// disconnection
		{	String folder = baseFolder+GuiFileTools.FILE_DISCONNECTION;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_UNKNOWN
			};
			loadTableImages(folder,uses);
		}
		// down
		{	String folder = baseFolder+GuiFileTools.FILE_DOWN;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_DOWN
			};
			loadTableImages(folder,uses);
		}
		// edit
		{	String folder = baseFolder+GuiFileTools.FILE_EDIT;
			String[] uses =
			{	GuiKeys.MENU_PROFILES_EDIT_AI_CHANGE,
				GuiKeys.MENU_PROFILES_EDIT_HERO_CHANGE,
				GuiKeys.MENU_PROFILES_EDIT_NAME_CHANGE,
				GuiKeys.COMMON_PLAYERS_SELECTION_DATA_ADD,
				GuiKeys.MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE,
				GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_SELECT,
				GuiKeys.COMMON_GAME_LIST_BUTTON_ADD
			};
			loadTableImages(folder,uses);
		}
		// equal
		{	String folder = baseFolder+GuiFileTools.FILE_EQUAL;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_SAME
			};
			loadTableImages(folder,uses);
		}
		// false
		{	String folder = baseFolder+GuiFileTools.FILE_FALSE;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE,
				GuiKeys.MENU_OPTIONS_VIDEO_LINE_DISABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_DISABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_DISABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_DISABLED,
				GuiKeys.MENU_PROFILES_EDIT_AI_RESET,
				GuiKeys.COMMON_PLAYERS_SELECTION_DATA_DELETE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_FALSE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_FALSE,
				GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DISABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALLAIS_DISABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_DISABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_DISABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_DISABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_DISABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_REINIT_DO,
				GuiKeys.COMMON_GAME_LIST_BUTTON_REMOVE
			};
			loadTableImages(folder,uses);
		}
		// finished
		{	String folder = baseFolder+GuiFileTools.FILE_FINISHED;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_FINISHED
			};
			loadTableImages(folder,uses);
		}
		// human
		{	String folder = baseFolder+GuiFileTools.FILE_HUMAN;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_LIST_DATA_HUMAN,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_DATA_HUMAN,
				GuiKeys.COMMON_RESULTS_MATCH_DATA_HUMAN,
				GuiKeys.COMMON_RESULTS_ROUND_DATA_HUMAN,
				GuiKeys.COMMON_PLAYERS_SELECTION_DATA_HUMAN,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_HUMAN,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_HUMAN
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
		// league
		{	String folder = baseFolder+GuiFileTools.FILE_LEAGUE;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_TYPE_LEAGUE
			};
			loadTableImages(folder,uses);
		}
		// left
		{	String folder = baseFolder+GuiFileTools.FILE_LEFT;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_EXIT
			};
			loadTableImages(folder,uses);
		}
		// minus
		{	String folder = baseFolder+GuiFileTools.FILE_MINUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_VIDEO_LINE_MINUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_MINUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_MINUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_MINUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_MINUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_MINUS,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER,
				GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_MINUS,
				GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_MINUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_MINUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_MINUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_MINUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_MINUS
			};
			loadTableImages(folder,uses);
		}
		// next
		{	String folder = baseFolder+GuiFileTools.FILE_NEXT;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT,
				GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_NEXT,
				GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT,
				GuiKeys.MENU_PROFILES_EDIT_COLOR_NEXT,
				GuiKeys.COMMON_PART_AFTER,
				GuiKeys.COMMON_LEG_RIGHT

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
		// no star
		{	String folder = baseFolder+GuiFileTools.FILE_NOSTAR;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_FAV_REGULAR
			};
			loadTableImages(folder,uses);
		}
		// open
		{	String folder = baseFolder+GuiFileTools.FILE_OPEN;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_OPEN
			};
			loadTableImages(folder,uses);
		}
		// page down
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_DOWN;
			String[] uses =
			{	GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEDOWN,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEDOWN,
				GuiKeys.COMMON_BROWSER_FILE_PAGEDOWN,
				GuiKeys.COMMON_BROWSER_PACK_PAGEDOWN,
				GuiKeys.COMMON_LEG_DOWN,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NEXT,
				GuiKeys.COMMON_GAME_LIST_BUTTON_NEXT
			};
			loadTableImages(folder,uses);
		}
		// page up
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_UP;
			String[] uses =
			{	GuiKeys.MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEUP,
				GuiKeys.MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEUP,
				GuiKeys.COMMON_BROWSER_FILE_PAGEUP,
				GuiKeys.COMMON_BROWSER_PACK_PAGEUP,
				GuiKeys.COMMON_LEG_UP,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PREVIOUS,
				GuiKeys.COMMON_GAME_LIST_BUTTON_PREVIOUS
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
		// player selected
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYER_SELECTED;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_SELECTION_DATA_CONFIRMED
			};
			loadTableImages(folder,uses);
		}
		// player unselected
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYER_UNSELECTED;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_SELECTION_DATA_UNCONFIRMED
			};
			loadTableImages(folder,uses);
		}
		// playing
		{	String folder = baseFolder+GuiFileTools.FILE_PLAYING;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_STATE_PLAYING
			};
			loadTableImages(folder,uses);
		}
		// plus
		{	String folder = baseFolder+GuiFileTools.FILE_PLUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_VIDEO_LINE_PLUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_PLUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_PLUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_PLUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_PLUS,
				GuiKeys.MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_PLUS,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER,
				GuiKeys.MENU_OPTIONS_AIS_LINE_UPS_PLUS,
				GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_PLUS
			};
			loadTableImages(folder,uses);
		}
		// previous
		{	String folder = baseFolder+GuiFileTools.FILE_PREVIOUS;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS,
				GuiKeys.MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS,
				GuiKeys.MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS,
				GuiKeys.MENU_PROFILES_EDIT_COLOR_PREVIOUS,
				GuiKeys.COMMON_PART_BEFORE,
				GuiKeys.COMMON_LEG_LEFT
			};
			loadTableImages(folder,uses);
		}
		// profile
		{	String folder = baseFolder+GuiFileTools.FILE_PROFILE;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOTH
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
		// remote
		{	String folder = baseFolder+GuiFileTools.FILE_REMOTE;
			String[] uses =
			{	GuiKeys.COMMON_PLAYERS_LIST_DATA_REMOTE,
				GuiKeys.COMMON_RESULTS_MATCH_DATA_REMOTE,
				GuiKeys.COMMON_RESULTS_TOURNAMENT_DATA_REMOTE,
				GuiKeys.COMMON_RESULTS_ROUND_DATA_REMOTE,
				GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_REMOTE,
				GuiKeys.COMMON_PLAYERS_SELECTION_DATA_REMOTE
			};
			loadTableImages(folder,uses);
		}
		// right
		{	String folder = baseFolder+GuiFileTools.FILE_RIGHT;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_ENTER
			};
			loadTableImages(folder,uses);
		}
		// sequence
		{	String folder = baseFolder+GuiFileTools.FILE_SEQUENCE;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SEQUENCE
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
		// single
		{	String folder = baseFolder+GuiFileTools.FILE_SINGLE;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_TYPE_SINGLE
			};
			loadTableImages(folder,uses);
		}
		// star
		{	String folder = baseFolder+GuiFileTools.FILE_STAR;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_FAV_PREFERRED
			};
			loadTableImages(folder,uses);
		}
		// suicides
		{	String folder = baseFolder+GuiFileTools.FILE_SUICIDES;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_MATCH_DATA_SELF_BOMBINGS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_SELF_BOMBINGS,
				GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_SELF_BOMBINGS
			};
			loadTableImages(folder,uses);
		}
		// table
		{	String folder = baseFolder+GuiFileTools.FILE_TABLE;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALLRANKS
			};
			loadTableImages(folder,uses);
		}
		// table bis
		{	String folder = baseFolder+GuiFileTools.FILE_TABLE_BIS;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALL
			};
			loadTableImages(folder,uses);
		}
		// table tris
		{	String folder = baseFolder+GuiFileTools.FILE_TABLE_TRIS;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NORANKS
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
		// total
		{	String folder = baseFolder+GuiFileTools.FILE_TOTAL;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_BUTTON_SUM
			};
			loadTableImages(folder,uses);
		}
		// true
		{	String folder = baseFolder+GuiFileTools.FILE_TRUE;
			String[] uses =
			{	GuiKeys.MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE,
				GuiKeys.MENU_OPTIONS_VIDEO_LINE_ENABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_CACHE_ENABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_ENABLED,
				GuiKeys.MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_ENABLED,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_TRUE,
				GuiKeys.MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_TRUE,
				GuiKeys.MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_ENABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALLAIS_ENABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_ENABLED,
				GuiKeys.MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_ENABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_ENABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_ENABLED,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_REINIT_DONE
			};
			loadTableImages(folder,uses);
		}
		// turning
		{	String folder = baseFolder+GuiFileTools.FILE_TURNING;
			String[] uses =
			{	GuiKeys.COMMON_GAME_LIST_DATA_TYPE_TURNING
			};
			loadTableImages(folder,uses);
		}
		// up
		{	String folder = baseFolder+GuiFileTools.FILE_UP;
			String[] uses =
			{	GuiKeys.COMMON_STATISTICS_PLAYER_COMMON_DATA_UP
			};
			loadTableImages(folder,uses);
		}
	}
				
	/////////////////////////////////////////////////////////////////
	// FONTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static Graphics graphics;
	public final static float FONT_RATIO = 0.8f; // font height relatively to the containing label (or component)
	public final static float FONT_TEXT_RATIO = 0.75f; // font height relatively to the container title font 

	public static Graphics getGraphics()
	{	return graphics;	
	}
	
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
		fontSize = fontSize + 6;
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
	public static int getOptimalFontSize(double width, double height, List<String> texts)
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

	/**
	 * process the maximal width for the given list of texts, relatively
	 * to the given font size
	 * @param fontSize
	 * @param texts
	 * @return
	 */
	public static int getMaximalWidth(float fontSize, List<String> texts)
	{	int result = 0;
		Iterator<String> it = texts.iterator();
		while(it.hasNext())
		{	String text = it.next();
			int length = getPixelWidth(10,text);
			if(length>result)
				result = length;
		}
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
	public final static float TABLE_HEADER_RATIO = 1.2f; //header height relatively to line height

	// panel split
	public final static float VERTICAL_SPLIT_RATIO = 0.25f;
	public final static float HORIZONTAL_SPLIT_RATIO = 0.07f;

	// modal dialog
	public final static float MODAL_DIALOG_RATIO = 0.4f;
	
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
	public final static Color COLOR_DIALOG_BACKGROUND = new Color(0,0,0,175);
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,150);
	public final static Color COLOR_TITLE_FOREGROUND = Color.BLACK;
	public final static Color COLOR_TABLE_SELECTED_PALE_BACKGROUND = new Color(204,18,128,50);
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
	public final static int ALPHA_DARKER_CHANGE = 55; 
	
	public static Color changeColorAlpha(Color color, int delta)
	{	Color result = color;
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		int a = color.getAlpha();
		int newAlpha = a + delta;
		if(newAlpha>0 && newAlpha<255)
			result = new Color(r,g,b,newAlpha);
		return result;
	}
	
/*	public static void changeColorMouseEntered(Component component)
	{	Color oldColor = component.getBackground();
		Color newColor = changeColorAlpha(oldColor,+54);
		component.setBackground(newColor);
	}
	
	public static void changeColorMouseExited(Component component)
	{	Color oldColor = component.getBackground();
		int a = oldColor.getAlpha();
		if(a>0 && a<255)
		{	Color newColor = changeColorAlpha(oldColor,-54);
			component.setBackground(newColor);
		}
	}*/
	
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static int indexOfComponent(Container container, Component component)
	{	Component[] components = container.getComponents();
		List<Component> list = Arrays.asList(components);
		int result = list.indexOf(component);
		return result;
	}
	
	
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
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setIcon(ii);
			}
			// disabled icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledIcon(ii);
			}
			// pressed icon
			{	BufferedImage icon = getIcon(name+ICON_PRESSED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setPressedIcon(ii);
			}
			// selected icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setSelectedIcon(ii);
			}
			// disabled selected icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledSelectedIcon(ii);
			}
			// rollover icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverEnabled(true);
				button.setRolloverIcon(ii);
			}
			// rollover selected icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.getResizedImage(icon,zoom*BUTTON_ICON_MARGIN_RATION,true);
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
		if(panel!=null)
		{	panel.add(result);
			result.addActionListener(panel);
		}
	}
	
	public static JButton createButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}

	public static JToggleButton createToggleButton(String name, int width, int height, int fontSize, ButtonAware panel)
	{	JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		// font
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}
}
