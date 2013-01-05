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

import java.io.File;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiFileTools
{
	public static final String EXTENSION_SCHEMA = ".xsd";
	public static final String EXTENSION_DATA = ".xml";

	// files
	public final static String FILE_ADDRESS = "address.png";
	public final static String FILE_AUTHOR = "author.png";
	public final static String FILE_AUTOFIRE = "autofire.png";
	public final static String FILE_AVERAGE = "average.png";
	public final static String FILE_BOMBS = "bombs.png";
	public final static String FILE_CENTRAL = "central.png";
	public final static String FILE_CLOSED = "closed.png";
	public final static String FILE_COLOR = "color.png";
	public final static String FILE_COMMAND = "command.png";
	public final static String FILE_COMPUTER = "computer.png";
	public final static String FILE_CONFRONTATIONS = "confrontations.png";
	public final static String FILE_CONSTANT = "constant.png";
	public final static String FILE_CROWNS = "crowns.png";
	public final static String FILE_CUP = "cup.png";
	public final static String FILE_CUSTOM = "custom.png";
	public final static String FILE_DEATHS = "deaths.png";
	public final static String FILE_DOWN = "down.png";
	public final static String FILE_DIMENSION = "dimension.png";
	public final static String FILE_DIRECT = "direct.png";
	public final static String FILE_DISCONNECTION = "disconnection.png";
	public final static String FILE_DISCRETIZE = "discretize.png";
	public final static String FILE_DRAW = "draw.png";
	public final static String FILE_EDIT = "edit.png";
	public final static String FILE_EQUAL = "equal.png";
	public final static String FILE_EVOLUTION = "evolution.png";
	public final static String FILE_FALSE = "false.png";
	public final static String FILE_FINISHED = "finished.png";
	public final static String FILE_FRAGS = "frags.png";
	public final static String FILE_FRAME = "frame.png";
	public final static String FILE_GUI = "gui";
	public final static String FILE_HERO = "hero.png";
	public final static String FILE_HUMAN = "human.png";
	public final static String FILE_INITIAL = "initial.png";
	public final static String FILE_INSTANCE = "instance.png";
	public final static String FILE_INVERTED = "inverted.png";
	public final static String FILE_ITEMS = "items.png";
	public final static String FILE_KEY = "key.png";
	public final static String FILE_KILLS = "kills.png";
	public final static String FILE_LAURELS = "laurels.png";
	public final static String FILE_LEAGUE = "league.png";
	public final static String FILE_LEFT = "left.png";
	public final static String FILE_LEVEL = "level.png";
	public final static String FILE_LIMITS = "limits.png";
	public final static String FILE_LOSE = "lose.png";
	public final static String FILE_MINUS = "minus.png";
	public final static String FILE_MEAN = "mean.png";
	public final static String FILE_MISC = "misc.png";
	public final static String FILE_NAME = "name.png";
	public final static String FILE_NEXT = "next.png";
	public final static String FILE_NOSHARE = "noshare.png";
	public final static String FILE_NOSTAR = "nostar.png";
	public final static String FILE_OPEN = "open.png";
	public final static String FILE_PACK = "pack.png";
	public final static String FILE_PAINTINGS = "paintings.png";
	public final static String FILE_PARTIAL = "partial.png";
	public final static String FILE_PAGE_DOWN = "pgdown.png";
	public final static String FILE_PAGE_UP = "pgup.png";
	public final static String FILE_PLAYER_SELECTED = "player_selected.png";
	public final static String FILE_PLAYER_UNSELECTED = "player_unselected.png";
	public final static String FILE_PLAYERS = "players.png";
	public final static String FILE_PLAYING = "playing.png";
	public final static String FILE_PLUS = "plus.png";
	public final static String FILE_POINTS = "points.png";
	public final static String FILE_PREVIOUS = "previous.png";
	public final static String FILE_PROFILE = "profile.png";
	public final static String FILE_RANK = "rank.png";
	public final static String FILE_REGULAR = "regular.png";
	public final static String FILE_REMOTE = "remote.png";
	public final static String FILE_RIGHT = "right.png";
	public final static String FILE_SAVE = "save.png";
	public final static String FILE_SCORE = "score.png";
	public final static String FILE_SEQUENCE = "sequence.png";
	public final static String FILE_SHARE = "share.png";
	public final static String FILE_SINGLE = "single.png";
	public final static String FILE_SOURCE = "source.png";
	public final static String FILE_STDEV = "stdev.png";
	public final static String FILE_STAR = "star.png";
	public final static String FILE_SUICIDES = "suicides.png";
	public final static String FILE_TABLE = "table.png";
	public final static String FILE_TABLE_BIS = "tablebis.png";
	public final static String FILE_TABLE_TRIS = "tabletris.png";
	public final static String FILE_THEME = "theme.png";
	public final static String FILE_TIME = "time.png";
	public final static String FILE_TITLE = "title.png";
	public final static String FILE_TOTAL = "total.png";
	public final static String FILE_TOURNAMENT = "tournament.png";
	public final static String FILE_TRUE = "true.png";
	public final static String FILE_TURNING = "turning.png";
	public final static String FILE_UP = "up.png";
	public final static String FILE_VOLATILITY = "volatility.png";
	public final static String FILE_WIN = "win.png";

	// folders
	public static final String FOLDER_BACKGROUNDS = "backgrounds";
	public static final String FOLDER_BUTTONS = "buttons";
	public final static String FOLDER_CAMERA = "camera";
	public final static String FOLDER_DATA = "data";
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
	public final static String FOLDER_NETWORK = "network";
	public final static String FOLDER_PLAY = "play";
	public final static String FOLDER_PLAYERS_BLOCKING = "players_blocking";
	public final static String FOLDER_PLAYERS_SELECTING = "players_selecting";
	public final static String FOLDER_RESULTS = "results";
	public final static String FOLDER_RIGHT_BLUE = "right_blue";
	public final static String FOLDER_RIGHT_RED = "right_red";
	public final static String FOLDER_SAVE = "save";
	public final static String FOLDER_STATS = "stats";

	public static String getGuiPath()
	{	return org.totalboumboum.tools.files.FilePaths.getResourcesPath()+File.separator+FOLDER_GUI;		
	}
	
	public static String getButtonsPath()
	{	return getGuiPath()+File.separator+FOLDER_BUTTONS;		
	}
	
	public static String getFontsPath()
	{	return getGuiPath()+File.separator+FOLDER_FONTS;		
	}
	
	public static String getIconsPath()
	{	return getGuiPath()+File.separator+FOLDER_ICONS;		
	}
	public static String getHeadersPath()
	{	return getIconsPath()+File.separator+FOLDER_HEADERS;		
	}
	public static String getDataPath()
	{	return getIconsPath()+File.separator+FOLDER_DATA;		
	}

	public static String getImagesPath()
	{	return getGuiPath()+File.separator+FOLDER_IMAGES;		
	}
	public static String getBackgroundsPath()
	{	return getImagesPath()+File.separator+FOLDER_BACKGROUNDS;		
	}
	
	public static String getLanguagesPath()
	{	return getGuiPath()+File.separator+FOLDER_LANGUAGES;		
	}
	
}
