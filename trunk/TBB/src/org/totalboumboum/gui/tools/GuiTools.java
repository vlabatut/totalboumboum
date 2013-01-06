package org.totalboumboum.gui.tools;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Sets of fields and methods related
 * to GUI management.
 * 
 * @author Vincent Labatut
 */
public class GuiTools
{	
	/////////////////////////////////////////////////////////////////
	// INIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Initializes the fields in this class,
	 * for a full GUI.
	 */
	public static void init()
	{	initImages();
	}
	
	/////////////////////////////////////////////////////////////////
	// STARTUP			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Startup message index */
	public static final int STARTUP_XML = 0;
	/** Startup message index */
	public static final int STARTUP_CONFIG = 1;
	/** Startup message index */
	public static final int STARTUP_GUI = 2;
	/** Startup message index */
	public static final int STARTUP_INIT = 3;
	/** Startup message index */
	public static final int STARTUP_STATS = 4;
	/** Startup message index */
	public static final int STARTUP_DONE = 5;
	/** Array of startup messages */
	public static final String STARTUP_MESSAGES[] = 
	{	"[Loading XML schemas]",
		"[Loading configuration]",
		"[Loading GUI]",
		"[Initializing GUI]",
		"[Loading statistics]",
		"[Done]"
	};
	/** Copyright message */
	public static final String STARTUP_LEGAL[] = 
	{	"Total Boum Boum version "+GameData.VERSION,
		new Character('\u00A9').toString()+" 2008-2013 Vincent Labatut",
		"Licensed under the GPL v2"
	};
	
	/////////////////////////////////////////////////////////////////
	// HELP				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Help message */
	public static final String OPTION_HELP_MESSAGE = "In-line parameters allowed for this software:";
	/** Option index */
	public static final int OPTION_HELP = 0;
	/** Option index */
	public static final int OPTION_QUICK = 1;
	/** Option index */
	public static final int OPTION_WINDOW = 2;
	/** Options tags */
	public static final String OPTIONS[] = 
	{	"help",
		"quick",
		"window"
	};
	/** Option descriptions */
	public static final String OPTIONS_HELP[] = 
	{	"show this page (and does not launch the game)",
		"launch the game in quick mode, i.e. with a minimal graphical interface, and allows playing only one predefined round",
		"force the game to be displayed in a window, even if full screen is set in the game options"
	};

	/////////////////////////////////////////////////////////////////
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Part of an icon button file name */
	protected static final String ICON_NORMAL = "normal";
	/** Part of an icon button file name */
	protected static final String ICON_NORMAL_SELECTED = "normal_selected";
	/** Part of an icon button file name */
	protected static final String ICON_DISABLED = "disabled";
	/** Part of an icon button file name */
	protected static final String ICON_DISABLED_SELECTED = "disabled_selected";
	/** Part of an icon button file name */
	protected static final String ICON_ROLLOVER = "rollover";
	/** Part of an icon button file name */
	protected static final String ICON_ROLLOVER_SELECTED = "rollover_selected";
	/** Part of an icon button file name */
	protected static final String ICON_PRESSED = "pressed";
	/** Standard replacement image used when some image is missing */
	protected static BufferedImage absentImage;	
	/** List of images already loaded */
	protected static final Map<String,BufferedImage> icons = new HashMap<String,BufferedImage>();
	
	/**
	 * Get the image for the specified key.
	 * 
	 * @param key
	 * 		Id requested.
	 * @return
	 * 		The corresponding image.
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
	 * Tests if an image is stored for the specified key.
	 * 
	 * @param key
	 * 		Key of interest.
	 * @return
	 * 		{@code true} iff an image is associated to this key.
	 */
	public static boolean hasIcon(String key)
	{	return icons.containsKey(key);		
	}

	/**
	 * Loads an image. If the image cannot be loaded, it is replaced by a
	 * standard image (e.g. a red cross).
	 *  
	 * @param path
	 * 		Path of the file.
	 * @param absent
	 * 		What to put instead if the file is missing.
	 * @return
	 * 		The resulting image.
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
	
	/**
	 * Initializes all images.
	 */
	private static void initImages()
	{	// absent
		absentImage = ImageTools.getAbsentImage(64,64);
		// init
		initButtonImages();
		initHeaderImages();
		initDataImages();		
	}
	
	/**
	 * Load all images for a button.
	 * 
	 * @param buttonStates
	 * 		
	 * @param folder
	 * @param uses
	 */
	private static void loadButtonImages(String[] buttonStates, String folder, String[] uses)
	{	for(int i=0;i<buttonStates.length;i++)
		{	BufferedImage image = loadIcon(folder+buttonStates[i]+".png",absentImage);
			for(int j=0;j<uses.length;j++)
				icons.put(uses[j]+buttonStates[i],image);
		}
	}
	
	/**
	 * Initializes all button images.
	 */
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
	
	/**
	 * Load the images for table-related icons.
	 * 
	 * @param folder
	 * 		Folder containing the images.
	 * @param uses
	 * 		List of keys associated to these images.
	 */
	private static void loadTableImages(String folder, String[] uses)
	{	BufferedImage image = loadIcon(folder,absentImage);
		for(int j=0;j<uses.length;j++)
			icons.put(uses[j],image);
	}
	
	/**
	 * Initializes all images for
	 * table headers.
	 */
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
	
	/**
	 * Initializes all images for
	 * icons contained in tables 
	 * (but headers).
	 */
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBS
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_CROWNS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_CROWNS
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBEDS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBEDS
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
				GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_DISABLED,
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_ITEMS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_ITEMS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_BOMBINGS,
				GuiKeys.COMMON_POINTS_MATCH_DATA_BOMBINGS,
				GuiKeys.COMMON_POINTS_ROUND_DATA_BOMBINGS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_BOMBINGS
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
				GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_MINUS,
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_PAINTINGS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_PAINTINGS
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
				GuiKeys.MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_PLUS,
				GuiKeys.MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_PLUS
			};
			loadTableImages(folder,uses);
		}
		// points
		{	String folder = baseFolder+GuiFileTools.FILE_POINTS;
			String[] uses =
			{	GuiKeys.COMMON_EVOLUTION_BUTTON_POINTS
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
				GuiKeys.COMMON_POINTS_TOURNAMENT_DATA_SELF_BOMBINGS,
				GuiKeys.COMMON_EVOLUTION_BUTTON_SELF_BOMBINGS
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
				GuiKeys.COMMON_POINTS_ROUND_DATA_TIME,
				GuiKeys.COMMON_EVOLUTION_BUTTON_TIME
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
				GuiKeys.MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_ENABLED,
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
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static int indexOfComponent(Container container, Component component)
	{	Component[] components = container.getComponents();
		List<Component> list = Arrays.asList(components);
		int result = list.indexOf(component);
		return result;
	}
}
