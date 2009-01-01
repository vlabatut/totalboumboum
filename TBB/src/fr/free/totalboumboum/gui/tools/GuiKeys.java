package fr.free.totalboumboum.gui.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;

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

public class GuiKeys
{
	/////////////////////////////////////////////////////////////////
	// MISC	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final String ABOUT = "About";
	public static final String ADD = "Add";
	public static final String ADVANCED = "Advanced";
	public static final String AI = "Ai";
	public static final String ALLOWED = "Allowed";
	public static final String ARCHIVE = "Archive";
	public static final String AUTHOR = "Author";
	public static final String AUTOKILL = "Autokill";
	public static final String AUTOLOAD = "Autoload";
	public static final String AUTOSAVE = "Autosave";
	public static final String AVAILABLE = "Available";
	public static final String BACK = "Back";
	public static final String BLACK = "Black";
	public static final String BLUE = "Blue";
	public static final String BOMBEDS = "Bombeds";
	public static final String BOMBINGS = "Bombings";
	public static final String BOMBS = "Bombs";
	public static final String BOMBSET = "Bombset";
	public static final String BOTH = "Both";
	public static final String BROWN = "Brown";
	public static final String BROWSE = "Browse";
	public static final String BROWSER = "Browser";
	public static final String BUTTON = "Button";
	public static final String CANCEL = "Cancel";
	public static final String COLOR = "Color";
	public static final String COLORS = "Colors";
	public static final String COMMON = "Common";
	public static final String COMPLETE = "Complete";
	public static final String COMPUTER = "Computer";
	public static final String CONFIRM = "Confirm";
	public static final String CONFRONTATION = "Confrontation";
	public static final String CONFRONTATIONS = "Confrontations";
	public static final String CONSTANT = "Constant";
	public static final String CONTROLS = "Controls";
	public static final String COUNT = "Count";
	public static final String CROWNS = "Crowns";
	public static final String CUP = "Cup";
	public static final String CURRENT = "Current";
	public static final String CYAN = "Cyan";
	public static final String CUSTOM = "Custom";
	public static final String DATA = "Data";
	public static final String DELETE = "Delete";
	public static final String DESCRIPTION = "Description";
	public static final String DIMENSION = "Dimension";
	public static final String DISCRETIZE = "Discretize";
	public static final String DRAW = "Draw";
	public static final String FALSE = "False";
	public static final String FILE = "File";
	public static final String FINISH = "Finish";
	public static final String FIXED = "Fixed";
	public static final String FOLDER = "Folder";
	public static final String GAME = "Game";
	public static final String GRASS = "Grass";
	public static final String GREEN = "Green";
	public static final String GREY = "Grey";
	public static final String GUI = "Gui";
	public static final String HEADER = "Header";
	public static final String HERO = "Hero";
	public static final String HUMAN = "Human";
	public static final String IMAGE = "Image";
	public static final String INDIGO = "Indigo";
	public static final String INITIAL = "Initial";
	public static final String INSTANCE = "Instance";
	public static final String INVERTED = "Inverted";
	public static final String ITEMS = "Items";
	public static final String ITEMSET = "Itemset";
	public static final String LAST_STANDING = "LastStanding";
	public static final String LEAGUE = "League";
	public static final String LEFT = "Left";
	public static final String LEVEL = "Level";
	public static final String LEVELS = "Levels";
	public static final String LIMIT = "Limit";
	public static final String LIST = "List";
	public static final String LOAD = "Load";
	public static final String LOCATION = "Location";
	public static final String MAIN = "Main";
	public static final String MATCH = "Match";
	public static final String MATCHES = "Matches";
	public static final String MENU = "Menu";
	public static final String MINUS = "Minus";
	public static final String NAME = "Name";
	public static final String NEW = "New";
	public static final String NEXT = "Next";
	public static final String NO_SHARE = "NoShare";
	public static final String NO_CONTROLS = "NoControls";
	public static final String NONE = "None";
	public static final String OPTIONS = "Options";
	public static final String ORANGE = "Orange";
	public static final String ORDER = "Order";
	public static final String PACK = "Pack";
	public static final String PAGEDOWN = "PageDown";
	public static final String PAGEUP = "PageUp";
	public static final String PAINTINGS = "Paintings";
	public static final String PARENT = "Parent";
	public static final String PARTIAL = "Partial";
	public static final String PINK = "Pink";
	public static final String PLAY = "Play";
	public static final String PLAYER = "Player";
	public static final String PLAYERS = "Players";
	public static final String PLUS = "Plus";
	public static final String POINTS = "Points";
	public static final String PORTRAIT = "Portrait";
	public static final String PREVIEW = "Preview";
	public static final String PREVIOUS = "Previous";
	public static final String PROFILE = "Profile";
	public static final String PROFILES = "Profiles";
	public static final String PROGRESSBAR = "Progressbar";
	public static final String PURPLE = "Purple";
	public static final String QUICKMATCH = "Quickmatch";
	public static final String QUICKSTART = "Quickstart";
	public static final String QUIT = "Quit";
	public static final String RANDOM = "Random";
	public static final String RANK = "Rank";
	public static final String RANKS = "Ranks";
	public static final String RANKINGS = "Rankings";
	public static final String RANKPOINTS = "Rankpoints";
	public static final String RED = "Red";
	public static final String REGULAR = "Regular";
	public static final String RESOURCES = "Resources";
	public static final String RESULTS = "Results";
	public static final String RIGHT = "Right";
	public static final String ROUND = "Round";
	public static final String ROUNDS = "Rounds";
	public static final String RUST = "Rust";
	public static final String SAVE = "Save";
	public static final String SELECT = "Select";
	public static final String SEQUENCE = "Sequence";
	public static final String SETTINGS = "Settings";
	public static final String SINGLE = "Single";
	public static final String SPRITE = "Sprite";
	public static final String START = "Start";
	public static final String ULTRAMARINE = "Ultramarine";
	public static final String SCORE = "Score";
	public static final String SHARE = "Share";
	public static final String SELECTION = "Selection";
	public static final String SOURCE = "Source";
	public static final String STATISTICS = "Statistics";
	public static final String THEME = "Theme";
	public static final String TIME = "Time";
	public static final String TITLE = "Title";
	public static final String TOOLTIP = "Tooltip";
	public static final String TOTAL = "Total";
	public static final String TOURNAMENT = "Tournament";
	public static final String TRANSFER = "Transfer";
	public static final String TRUE = "True";
	public static final String TYPE = "Type";
	public static final String TYPES = "Types";
	public static final String USE = "Use";
	public static final String VALUE = "Value";
	public static final String VALUES = "Values";
	public static final String VAR = "Var";
	public static final String VIDEO = "Video";
	public static final String WHITE = "White";
	public static final String YELLOW = "Yellow";
	public static final String YOK = "Yok";
	
	/////////////////////////////////////////////////////////////////
	// MENUS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* MAIN */ 
	public static final String MENU_MAIN = MENU+MAIN;
		/* BUTTON */
		public static final String MENU_MAIN_BUTTON = MENU_MAIN+BUTTON;
		public static final String MENU_MAIN_BUTTON_ABOUT = MENU_MAIN_BUTTON+ABOUT;
		public static final String MENU_MAIN_BUTTON_LOAD = MENU_MAIN_BUTTON+LOAD;
		public static final String MENU_MAIN_BUTTON_OPTIONS = MENU_MAIN_BUTTON+OPTIONS;
		public static final String MENU_MAIN_BUTTON_PROFILES = MENU_MAIN_BUTTON+PROFILES;
		public static final String MENU_MAIN_BUTTON_QUICKMATCH = MENU_MAIN_BUTTON+QUICKMATCH;
		public static final String MENU_MAIN_BUTTON_QUIT = MENU_MAIN_BUTTON+QUIT;
		public static final String MENU_MAIN_BUTTON_RESOURCES = MENU_MAIN_BUTTON+RESOURCES;
		public static final String MENU_MAIN_BUTTON_STATISTICS = MENU_MAIN_BUTTON+STATISTICS;
		public static final String MENU_MAIN_BUTTON_TOURNAMENT = MENU_MAIN_BUTTON+TOURNAMENT;
	
	/* RESOURCES */
	public static final String MENU_RESOURCES_BUTTON = "MenuResourcesButton";
	public static final String MENU_RESOURCES_BUTTON_AI = MENU_RESOURCES_BUTTON+"Ai";
	public static final String MENU_RESOURCES_BUTTON_BACK = MENU_RESOURCES_BUTTON+"Back";
	public static final String MENU_RESOURCES_BUTTON_HEROES = MENU_RESOURCES_BUTTON+"Heroes";
	public static final String MENU_RESOURCES_BUTTON_INSTANCES = MENU_RESOURCES_BUTTON+"Instances";
	public static final String MENU_RESOURCES_BUTTON_LEVELS = MENU_RESOURCES_BUTTON+"Levels";
	public static final String MENU_RESOURCES_BUTTON_MATCHES = MENU_RESOURCES_BUTTON+"Matches";
	public static final String MENU_RESOURCES_BUTTON_ROUNDS = MENU_RESOURCES_BUTTON+"Rounds";
	public static final String MENU_RESOURCES_BUTTON_TOURNAMENTS = MENU_RESOURCES_BUTTON+"Tournaments";
		/* AI */
			/* BUTTON */
			public static final String MENU_RESOURCES_AI_BUTTON = "MenuResourcesAiButton";	
			public static final String MENU_RESOURCES_AI_BUTTON_BACK = MENU_RESOURCES_AI_BUTTON+"Back";	
			/* SELECT */
			public static final String MENU_RESOURCES_AI_SELECT_TITLE = "MenuResourcesAiSelectTitle";
			public static final String MENU_RESOURCES_AI_SELECT_NOTES = "MenuResourcesAiSelectNotes";
		/* HERO */
			public static final String MENU_RESOURCES_HERO_TITLE = "MenuResourcesHeroTitle";
			/* BUTTON */
			public static final String MENU_RESOURCES_HERO_BUTTON = "MenuResourcesHeroButton";	
			public static final String MENU_RESOURCES_HERO_BUTTON_BACK = MENU_RESOURCES_HERO_BUTTON+"Back";	
		/* LEVEL */
			/* BUTTON */
			public static final String MENU_RESOURCES_LEVEL_BUTTON = "MenuResourcesLevelButton";	
			public static final String MENU_RESOURCES_LEVEL_BUTTON_BACK = MENU_RESOURCES_LEVEL_BUTTON+"Back";	
			/* SELECT */
			public static final String MENU_RESOURCES_LEVEL_SELECT_TITLE = "MenuResourcesLevelSelectTitle";
				/* FOLDER */
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEDOWN = "MenuResourcesLevelSelectFolderPageDown";
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEUP = "MenuResourcesLevelSelectFolderPageUp";
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PARENT = "MenuResourcesLevelSelectFolderParent";
				/* PACKAGES */
				public static final String MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEDOWN = "MenuResourcesLevelSelectPackagePageDown";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEUP = "MenuResourcesLevelSelectPackagePageUp";
				/* PREVIEW */
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_AUTHOR = "MenuResourcesLevelSelectPreviewAuthor";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE = "MenuResourcesLevelSelectPreviewImage";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_INSTANCE = "MenuResourcesLevelSelectPreviewInstance";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_NAME = "MenuResourcesLevelSelectPreviewName";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_PACKAGE = "MenuResourcesLevelSelectPreviewPackage";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SIZE = "MenuResourcesLevelSelectPreviewSize";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SOURCE = "MenuResourcesLevelSelectPreviewSource";
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_THEME = "MenuResourcesLevelSelectPreviewTheme";
		/* MATCH */
		public static final String MENU_RESOURCES_MATCH_TITLE = "MenuResourcesMatchTitle";
			/* BUTTON */
			public static final String MENU_RESOURCES_MATCH_BUTTON = "MenuResourcesMatchButton";	
			public static final String MENU_RESOURCES_MATCH_BUTTON_BACK = MENU_RESOURCES_MATCH_BUTTON+"Back";	
		/* ROUND */
		public static final String MENU_RESOURCES_ROUND_TITLE = "MenuResourcesRoundTitle";
			/* BUTTON */
			public static final String MENU_RESOURCES_ROUND_BUTTON = "MenuResourcesRoundButton";	
			public static final String MENU_RESOURCES_ROUND_BUTTON_BACK = MENU_RESOURCES_ROUND_BUTTON+"Back";	
		/* TOURNAMENT */
		public static final String MENU_RESOURCES_TOURNAMENT_TITLE = "MenuResourcesTournamentTitle";
			/* BUTTON */
			public static final String MENU_RESOURCES_TOURNAMENT_BUTTON = "MenuResourcesTournamentButton";	
			public static final String MENU_RESOURCES_TOURNAMENT_BUTTON_BACK = MENU_RESOURCES_TOURNAMENT_BUTTON+"Back";	
			
	/* OPTIONS */
	public static final String MENU_OPTIONS = MENU+OPTIONS;
		/* BUTTON */
		public static final String MENU_OPTIONS_BUTTON = "MenuOptionsButton";
		public static final String MENU_OPTIONS_BUTTON_ADVANCED = MENU_OPTIONS_BUTTON+ADVANCED;
		public static final String MENU_OPTIONS_BUTTON_BACK = MENU_OPTIONS_BUTTON+BACK;
		public static final String MENU_OPTIONS_BUTTON_CANCEL = MENU_OPTIONS_BUTTON+CANCEL;
		public static final String MENU_OPTIONS_BUTTON_CONFIRM = MENU_OPTIONS_BUTTON+CONFIRM;
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = MENU_OPTIONS_BUTTON+CONTROLS;
		public static final String MENU_OPTIONS_BUTTON_GAME = MENU_OPTIONS_BUTTON+GAME;
		public static final String MENU_OPTIONS_BUTTON_GUI = MENU_OPTIONS_BUTTON+GUI;
		public static final String MENU_OPTIONS_BUTTON_NEXT = MENU_OPTIONS_BUTTON+NEXT;
		public static final String MENU_OPTIONS_BUTTON_PREVIOUS = MENU_OPTIONS_BUTTON+PREVIOUS;
		public static final String MENU_OPTIONS_BUTTON_VIDEO = MENU_OPTIONS_BUTTON+VIDEO;
		/* ADVANCED */
		public static final String MENU_OPTIONS_ADVANCED_TITLE = "MenuOptionsAdvancedTitle";
			/* LINE */
				/* ADJUST FPS */
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_TITLE = "MenuOptionsAdvancedLineAdjustTitle";
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED = "MenuOptionsAdvancedLineAdjustDisabled";
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED = "MenuOptionsAdvancedLineAdjustEnabled";
				/* FPS */
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE = "MenuOptionsAdvancedLineFpsTitle";
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS = "MenuOptionsAdvancedLineFpsMinus";
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS = "MenuOptionsAdvancedLineFpsPlus";
				/* SPEED */
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE = "MenuOptionsAdvancedLineSpeedTitle";
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS = "MenuOptionsAdvancedLineSpeedMinus";
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS = "MenuOptionsAdvancedLineSpeedPlus";
		/* CONTROLS */
		public static final String MENU_OPTIONS_CONTROLS_TITLE = "MenuOptionsControlsTitle";
			/* HEADER */
			public static final String MENU_OPTIONS_CONTROLS_HEADER_KEY = "MenuOptionsControlsHeaderKey";
			public static final String MENU_OPTIONS_CONTROLS_HEADER_COMMAND = "MenuOptionsControlsHeaderCommand";
			public static final String MENU_OPTIONS_CONTROLS_HEADER_AUTO = "MenuOptionsControlsHeaderAuto";
			/* LINE */
				/* COMMAND */
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_UP = "MenuOptionsControlsLineCommandUp";
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_RIGHT = "MenuOptionsControlsLineCommandRight";
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_DOWN = "MenuOptionsControlsLineCommandDown";
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_LEFT = "MenuOptionsControlsLineCommandLeft";
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_DROPBOMB = "MenuOptionsControlsLineCommandDropbomb";
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_PUNCHBOMB = "MenuOptionsControlsLineCommandPunchbomb";
				/* AUTO */
				public static final String MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE = "MenuOptionsControlsLineAutoTrue";
				public static final String MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE = "MenuOptionsControlsLineAutoFalse";
		/* GAME */
		public static final String MENU_OPTIONS_GAME = MENU_OPTIONS+GAME;
			/* BUTTON */
			public static final String MENU_OPTIONS_GAME_BUTTON = MENU_OPTIONS_GAME+BUTTON;
			public static final String MENU_OPTIONS_GAME_BUTTON_QUICKSTART = MENU_OPTIONS_GAME_BUTTON+QUICKSTART;
			public static final String MENU_OPTIONS_GAME_BUTTON_QUICKMATCH = MENU_OPTIONS_GAME_BUTTON+QUICKMATCH;
			public static final String MENU_OPTIONS_GAME_BUTTON_TOURNAMENT = MENU_OPTIONS_GAME_BUTTON+TOURNAMENT;
			/* QUICK START */
			public static final String MENU_OPTIONS_GAME_QUICKSTART = MENU_OPTIONS_GAME+QUICKSTART;
			public static final String MENU_OPTIONS_GAME_QUICKSTART_TITLE = MENU_OPTIONS_GAME_QUICKSTART+TITLE;
				/* ROUND */
				public static final String MENU_OPTIONS_GAME_QUICKSTART_ROUND = MENU_OPTIONS_GAME_QUICKSTART+ROUND;
				public static final String MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE = MENU_OPTIONS_GAME_QUICKSTART_ROUND+BROWSE;
			/* QUICK MATCH */
			public static final String MENU_OPTIONS_GAME_QUICKMATCH = MENU_OPTIONS_GAME+QUICKMATCH;
			public static final String MENU_OPTIONS_GAME_QUICKMATCH_TITLE = MENU_OPTIONS_GAME_QUICKMATCH+TITLE;
				/* LEVELS */
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_LEVELS = MENU_OPTIONS_GAME_QUICKMATCH+LEVELS;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_FALSE = MENU_OPTIONS_GAME_QUICKMATCH_LEVELS+FALSE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_TRUE = MENU_OPTIONS_GAME_QUICKMATCH_LEVELS+TRUE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_LEVELS_USE = MENU_OPTIONS_GAME_QUICKMATCH_LEVELS+USE;
				/* PLAYERS */
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS = MENU_OPTIONS_GAME_QUICKMATCH+PLAYERS;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_FALSE = MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS+FALSE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_TRUE = MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS+TRUE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS_USE = MENU_OPTIONS_GAME_QUICKMATCH_PLAYERS+USE;
				/* SETTINGS */
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS = MENU_OPTIONS_GAME_QUICKMATCH+SETTINGS;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_FALSE = MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS+FALSE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_TRUE = MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS+TRUE;
				public static final String MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS_USE = MENU_OPTIONS_GAME_QUICKMATCH_SETTINGS+USE;
			/* TOURNAMENT */
			public static final String MENU_OPTIONS_GAME_TOURNAMENT = MENU_OPTIONS_GAME+TOURNAMENT;
			public static final String MENU_OPTIONS_GAME_TOURNAMENT_TITLE = MENU_OPTIONS_GAME_TOURNAMENT+TITLE;
				/* AUTOSAVE */
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE = MENU_OPTIONS_GAME_TOURNAMENT+AUTOSAVE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_FALSE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE+FALSE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_TRUE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE+TRUE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE_TITLE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOSAVE+TITLE;
				/* AUTOLOAD */
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD = MENU_OPTIONS_GAME_TOURNAMENT+AUTOLOAD;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_FALSE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD+FALSE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_TRUE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD+TRUE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD_TITLE = MENU_OPTIONS_GAME_TOURNAMENT_AUTOLOAD+TITLE;
				/* PLAYERS */
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS = MENU_OPTIONS_GAME_TOURNAMENT+PLAYERS;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_FALSE = MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS+FALSE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_TRUE = MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS+TRUE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS_USE = MENU_OPTIONS_GAME_TOURNAMENT_PLAYERS+USE;
				/* TOURNAMENT */
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT = MENU_OPTIONS_GAME_TOURNAMENT+TOURNAMENT;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_FALSE = MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT+FALSE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_TRUE = MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT+TRUE;
				public static final String MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT_USE = MENU_OPTIONS_GAME_TOURNAMENT_TOURNAMENT+USE;
		/* GUI */
		public static final String MENU_OPTIONS_GUI_TITLE = "MenuOptionsGuiTitle";
			/* LINE */
				/* LANGUAGE */
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE = "MenuOptionsGuiLineLanguageTitle";
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT = "MenuOptionsGuiLineLanguageNext";
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS = "MenuOptionsGuiLineLanguagePrevious";
				/* FONT */
				public static final String MENU_OPTIONS_GUI_LINE_FONT_TITLE = "MenuOptionsGuiLineFontTitle";
				public static final String MENU_OPTIONS_GUI_LINE_FONT_NEXT = "MenuOptionsGuiLineFontNext";
				public static final String MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS = "MenuOptionsGuiLineFontPrevious";
				/* BACKGROUND */
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE = "MenuOptionsGuiLineBackgroundTitle";
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT = "MenuOptionsGuiLineBackgroundNext";
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS = "MenuOptionsGuiLineBackgroundPrevious";
		/* VIDEO */
		public static final String MENU_OPTIONS_VIDEO_TITLE = "MenuOptionsVideoTitle";
			/* LINE */
			public static final String MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR = "MenuOptionsVideoLineBorderColor";
			public static final String MENU_OPTIONS_VIDEO_LINE_DISABLED = "MenuOptionsVideoLineDisabled";
			public static final String MENU_OPTIONS_VIDEO_LINE_ENABLED = "MenuOptionsVideoLineEnabled";
			public static final String MENU_OPTIONS_VIDEO_LINE_FULL_SCREEN = "MenuOptionsVideoLineFullScreen";
			public static final String MENU_OPTIONS_VIDEO_LINE_MINUS = "MenuOptionsVideoLineMinus";
			public static final String MENU_OPTIONS_VIDEO_LINE_PANEL_DIMENSION = "MenuOptionsVideoLinePanelDimension";
			public static final String MENU_OPTIONS_VIDEO_LINE_PLUS = "MenuOptionsVideoLinePlus";
			public static final String MENU_OPTIONS_VIDEO_LINE_SMOOTH_GRAPHICS = "MenuOptionsVideoLineSmoothGraphics";
			
	/* PROFILES */
		/* BUTTON */
		public static final String MENU_PROFILES_BUTTON = "MenuProfilesButton";
		public static final String MENU_PROFILES_BUTTON_BACK = MENU_PROFILES_BUTTON+"Back";
		public static final String MENU_PROFILES_BUTTON_CANCEL = "MenuProfilesButtonCancel";
		public static final String MENU_PROFILES_BUTTON_CONFIRM = "MenuProfilesButtonConfirm";
		public static final String MENU_PROFILES_BUTTON_DELETE = "MenuProfilesButtonDelete";
		public static final String MENU_PROFILES_BUTTON_MODIFY = "MenuProfilesButtonModify";
		public static final String MENU_PROFILES_BUTTON_NEW = "MenuProfilesButtonNew";
		/* EDIT */
		public static final String MENU_PROFILES_EDIT_TITLE = "MenuProfilesEditTitle";		
		public static final String MENU_PROFILES_EDIT_AI = "MenuProfilesEditAi";
		public static final String MENU_PROFILES_EDIT_AI_CHANGE = "MenuProfilesEditAiChange";
		public static final String MENU_PROFILES_EDIT_AI_RESET = "MenuProfilesEditAiReset";
		public static final String MENU_PROFILES_EDIT_COLOR = "MenuProfilesEditColor";
		public static final String MENU_PROFILES_EDIT_COLOR_NEXT = "MenuProfilesEditColorNext";
		public static final String MENU_PROFILES_EDIT_COLOR_PREVIOUS = "MenuProfilesEditColorPrevious";
		public static final String MENU_PROFILES_EDIT_HERO = "MenuProfilesEditHero";
		public static final String MENU_PROFILES_EDIT_HERO_CHANGE = "MenuProfilesEditHeroChange";
		public static final String MENU_PROFILES_EDIT_NAME = "MenuProfilesEditName";
		public static final String MENU_PROFILES_EDIT_NAME_CHANGE = "MenuProfilesEditNameChange";
		/* SELECT */
		public static final String MENU_PROFILES_SELECT_TITLE = "MenuProfilesSelectTitle";
		public static final String MENU_PROFILES_SELECT_NEW_PROFILE = "MenuProfilesSelectNewProfile";
	/* QUICKMATCH */	
	public static final String MENU_QUICKMATCH = MENU+QUICKMATCH;
		/* LEVELS */
		public static final String MENU_QUICKMATCH_LEVELS = MENU_QUICKMATCH+LEVELS;
		public static final String MENU_QUICKMATCH_LEVELS_TITLE = MENU_QUICKMATCH_LEVELS+TITLE;
			/* BUTTONS */
			public static final String MENU_QUICKMATCH_LEVELS_BUTTON = MENU_QUICKMATCH_LEVELS+BUTTON;
			public static final String MENU_QUICKMATCH_LEVELS_BUTTON_NEXT = MENU_QUICKMATCH_LEVELS_BUTTON+NEXT;
			public static final String MENU_QUICKMATCH_LEVELS_BUTTON_PREVIOUS = MENU_QUICKMATCH_LEVELS_BUTTON+PREVIOUS;
			public static final String MENU_QUICKMATCH_LEVELS_BUTTON_QUIT = MENU_QUICKMATCH_LEVELS_BUTTON+QUIT;
		/* PLAYERS */
		public static final String MENU_QUICKMATCH_PLAYERS = MENU_QUICKMATCH+PLAYERS;
		public static final String MENU_QUICKMATCH_PLAYERS_TITLE = MENU_QUICKMATCH_PLAYERS+TITLE;
			/* BUTTONS */
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON = MENU_QUICKMATCH_PLAYERS+BUTTON;
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON_CANCEL = MENU_QUICKMATCH_PLAYERS_BUTTON+CANCEL;
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON_CONFIRM = MENU_QUICKMATCH_PLAYERS_BUTTON+CONFIRM;
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON_NEXT = MENU_QUICKMATCH_PLAYERS_BUTTON+NEXT;
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON_PREVIOUS = MENU_QUICKMATCH_PLAYERS_BUTTON+PREVIOUS;
			public static final String MENU_QUICKMATCH_PLAYERS_BUTTON_QUIT = MENU_QUICKMATCH_PLAYERS_BUTTON+QUIT;
		/* SETTINGS */
		public static final String MENU_QUICKMATCH_SETTINGS = MENU_QUICKMATCH+SETTINGS;
		public static final String MENU_QUICKMATCH_SETTINGS_TITLE = MENU_QUICKMATCH_SETTINGS+TITLE;
			/* BUTTONS */
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON = MENU_QUICKMATCH_SETTINGS+BUTTON;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT = MENU_QUICKMATCH_SETTINGS_BUTTON+NEXT;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS = MENU_QUICKMATCH_SETTINGS_BUTTON+PREVIOUS;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_QUIT = MENU_QUICKMATCH_SETTINGS_BUTTON+QUIT;
			/* LEVELS */
			public static final String MENU_QUICKMATCH_SETTINGS_LEVELS = MENU_QUICKMATCH_SETTINGS+LEVELS;
				/* ORDER */
				public static final String MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER = MENU_QUICKMATCH_SETTINGS_LEVELS+ORDER;
				public static final String MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_FIXED = MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER+FIXED;
				public static final String MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_RANDOM = MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER+RANDOM;
				public static final String MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER_TITLE = MENU_QUICKMATCH_SETTINGS_LEVELS_ORDER+TITLE;
			/* LIMIT */
			public static final String MENU_QUICKMATCH_SETTINGS_LIMIT = MENU_QUICKMATCH_SETTINGS+LIMIT;
				/* ROUNDS */
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS = MENU_QUICKMATCH_SETTINGS_LIMIT+ROUNDS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_MINUS = MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS+MINUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_PLUS = MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS+PLUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS_TITLE = MENU_QUICKMATCH_SETTINGS_LIMIT_ROUNDS+TITLE;			
				/* POINTS */
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS = MENU_QUICKMATCH_SETTINGS_LIMIT+POINTS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_MINUS = MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS+MINUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_PLUS = MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS+PLUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS_TITLE = MENU_QUICKMATCH_SETTINGS_LIMIT_POINTS+TITLE;			
				/* TIME */
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_TIME = MENU_QUICKMATCH_SETTINGS_LIMIT+TIME;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_MINUS = MENU_QUICKMATCH_SETTINGS_LIMIT_TIME+MINUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_PLUS = MENU_QUICKMATCH_SETTINGS_LIMIT_TIME+PLUS;
				public static final String MENU_QUICKMATCH_SETTINGS_LIMIT_TIME_TITLE = MENU_QUICKMATCH_SETTINGS_LIMIT_TIME+TITLE;			
			/* PLAYERS */
			public static final String MENU_QUICKMATCH_SETTINGS_PLAYERS = MENU_QUICKMATCH_SETTINGS+PLAYERS;
				/* LOCATION */
				public static final String MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION = MENU_QUICKMATCH_SETTINGS_PLAYERS+LOCATION;
				public static final String MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_FIXED = MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION+FIXED;
				public static final String MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_RANDOM = MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION+RANDOM;
				public static final String MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION_TITLE = MENU_QUICKMATCH_SETTINGS_PLAYERS_LOCATION+TITLE;
			/* POINTS */
			public static final String MENU_QUICKMATCH_SETTINGS_POINTS = MENU_QUICKMATCH_SETTINGS+POINTS;
				/* RANKS */
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_RANKS = MENU_QUICKMATCH_SETTINGS_POINTS+RANKS;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_TITLE = MENU_QUICKMATCH_SETTINGS_POINTS_RANKS+TITLE;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_RANKS_VALUE = MENU_QUICKMATCH_SETTINGS_POINTS_RANKS+VALUE;
				/* DRAW */
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW = MENU_QUICKMATCH_SETTINGS_POINTS+DRAW;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_AUTOKILL = MENU_QUICKMATCH_SETTINGS_POINTS_DRAW+AUTOKILL;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_BOTH = MENU_QUICKMATCH_SETTINGS_POINTS_DRAW+BOTH;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_NONE = MENU_QUICKMATCH_SETTINGS_POINTS_DRAW+NONE;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TIME = MENU_QUICKMATCH_SETTINGS_POINTS_DRAW+TIME;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_DRAW_TITLE = MENU_QUICKMATCH_SETTINGS_POINTS_DRAW+TITLE;
				/* SHARE */
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_SHARE = MENU_QUICKMATCH_SETTINGS_POINTS+SHARE;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_TITLE = MENU_QUICKMATCH_SETTINGS_POINTS_SHARE+TITLE;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_VAR = MENU_QUICKMATCH_SETTINGS_POINTS_SHARE+VAR;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_SHARE_YOK = MENU_QUICKMATCH_SETTINGS_POINTS_SHARE+YOK;
				/* VALUES */
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_VALUES = MENU_QUICKMATCH_SETTINGS_POINTS+VALUES;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_MINUS = MENU_QUICKMATCH_SETTINGS_POINTS_VALUES+MINUS;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_PLUS = MENU_QUICKMATCH_SETTINGS_POINTS_VALUES+PLUS;
				public static final String MENU_QUICKMATCH_SETTINGS_POINTS_VALUES_TITLE = MENU_QUICKMATCH_SETTINGS_POINTS_VALUES+TITLE;
			
	/* TOURNAMENT */
	public static final String MENU_TOURNAMENT = MENU+TOURNAMENT;
		/* BUTTONS */
		public static final String MENU_TOURNAMENT_BUTTON = MENU_TOURNAMENT+BUTTON;
		public static final String MENU_TOURNAMENT_BUTTON_QUIT = MENU_TOURNAMENT_BUTTON+QUIT;
		/* LOAD */
		public static final String MENU_TOURNAMENT_LOAD = MENU_TOURNAMENT+LOAD;
		public static final String MENU_TOURNAMENT_LOAD_TITLE = MENU_TOURNAMENT_LOAD+TITLE;
			/* BUTTONS */
			public static final String MENU_TOURNAMENT_LOAD_BUTTON = MENU_TOURNAMENT_LOAD+BUTTON;
			public static final String MENU_TOURNAMENT_LOAD_BUTTON_CANCEL = MENU_TOURNAMENT_LOAD_BUTTON+CANCEL;
			public static final String MENU_TOURNAMENT_LOAD_BUTTON_CONFIRM = MENU_TOURNAMENT_LOAD_BUTTON+CONFIRM;
			public static final String MENU_TOURNAMENT_LOAD_BUTTON_DELETE = MENU_TOURNAMENT_LOAD_BUTTON+DELETE;
		/* PLAYERS */
		public static final String MENU_TOURNAMENT_PLAYERS = MENU_TOURNAMENT+PLAYERS;
		public static final String MENU_TOURNAMENT_PLAYERS_TITLE = MENU_TOURNAMENT_PLAYERS+TITLE;
			/* BUTTONS */
			public static final String MENU_TOURNAMENT_PLAYERS_BUTTON = MENU_TOURNAMENT_PLAYERS+BUTTON;
			public static final String MENU_TOURNAMENT_PLAYERS_BUTTON_CANCEL = MENU_TOURNAMENT_PLAYERS_BUTTON+CANCEL;
			public static final String MENU_TOURNAMENT_PLAYERS_BUTTON_CONFIRM = MENU_TOURNAMENT_PLAYERS_BUTTON+CONFIRM;
			public static final String MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT = MENU_TOURNAMENT_PLAYERS_BUTTON+NEXT;
			public static final String MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS = MENU_TOURNAMENT_PLAYERS_BUTTON+PREVIOUS;
		/* SETTINGS */
		public static final String MENU_TOURNAMENT_SETTINGS = MENU_TOURNAMENT+SETTINGS;
		public static final String MENU_TOURNAMENT_SETTINGS_TITLE = MENU_TOURNAMENT_SETTINGS+TITLE;
			/* BUTTONS */
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON = MENU_TOURNAMENT_SETTINGS+BUTTON;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_CANCEL = MENU_TOURNAMENT_SETTINGS_BUTTON+CANCEL;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_CONFIRM = MENU_TOURNAMENT_SETTINGS_BUTTON+CONFIRM;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT = MENU_TOURNAMENT_SETTINGS_BUTTON+NEXT;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS = MENU_TOURNAMENT_SETTINGS_BUTTON+PREVIOUS;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_SELECT = MENU_TOURNAMENT_SETTINGS_BUTTON+SELECT;
	
	/* GAME */
		/* SAVE */
		public static final String GAME_SAVE = GAME+SAVE;
		public static final String GAME_SAVE_TITLE = GAME_SAVE+TITLE;
			/* BUTTONS */	
			public static final String GAME_SAVE_BUTTON = GAME_SAVE+BUTTON;
			public static final String GAME_SAVE_BUTTON_CANCEL = GAME_SAVE_BUTTON+CANCEL;
			public static final String GAME_SAVE_BUTTON_CONFIRM = GAME_SAVE_BUTTON+CONFIRM;
			public static final String GAME_SAVE_BUTTON_DELETE = GAME_SAVE_BUTTON+DELETE;
			public static final String GAME_SAVE_BUTTON_NEW = GAME_SAVE_BUTTON+NEW;
		/* TOURNAMENT */
		public static final String GAME_TOURNAMENT = GAME+TOURNAMENT;
			/* BUTTONS */	
			public static final String GAME_TOURNAMENT_BUTTON = GAME_TOURNAMENT+BUTTON;
			public static final String GAME_TOURNAMENT_BUTTON_CURRENT_MATCH = GAME_TOURNAMENT_BUTTON+CURRENT+MATCH;
			public static final String GAME_TOURNAMENT_BUTTON_DESCRIPTION = GAME_TOURNAMENT_BUTTON+DESCRIPTION;
			public static final String GAME_TOURNAMENT_BUTTON_FINISH = GAME_TOURNAMENT_BUTTON+FINISH;
			public static final String GAME_TOURNAMENT_BUTTON_MENU = GAME_TOURNAMENT_BUTTON+MENU;
			public static final String GAME_TOURNAMENT_BUTTON_NEXT_MATCH = GAME_TOURNAMENT_BUTTON+NEXT+MATCH;
			public static final String GAME_TOURNAMENT_BUTTON_QUIT = GAME_TOURNAMENT_BUTTON+QUIT;
			public static final String GAME_TOURNAMENT_BUTTON_RESULTS = GAME_TOURNAMENT_BUTTON+RESULTS;
			public static final String GAME_TOURNAMENT_BUTTON_SAVE = GAME_TOURNAMENT_BUTTON+SAVE;
			public static final String GAME_TOURNAMENT_BUTTON_STATISTICS = GAME_TOURNAMENT_BUTTON+STATISTICS;
			/* DESCRIPTION */
			public static final String GAME_TOURNAMENT_DESCRIPTION = GAME_TOURNAMENT+DESCRIPTION;
			public static final String GAME_TOURNAMENT_DESCRIPTION_TITLE = GAME_TOURNAMENT_DESCRIPTION+TITLE;
			/* RESULTS */
			public static final String GAME_TOURNAMENT_RESULTS = GAME_TOURNAMENT+RESULTS;
			public static final String GAME_TOURNAMENT_RESULTS_TITLE = GAME_TOURNAMENT_RESULTS+TITLE;
			/* STATISTICS */
			public static final String GAME_TOURNAMENT_STATISTICS = GAME_TOURNAMENT+STATISTICS;
			public static final String GAME_TOURNAMENT_STATISTICS_TITLE = GAME_TOURNAMENT_STATISTICS+TITLE;
		/* MATCH */
		public static final String GAME_MATCH = GAME+MATCH;
			/* BUTTON */	
			public static final String GAME_MATCH_BUTTON = GAME_MATCH+BUTTON;
			public static final String GAME_MATCH_BUTTON_QUIT = GAME_MATCH_BUTTON+QUIT;
			public static final String GAME_MATCH_BUTTON_CURRENT_ROUND = GAME_MATCH_BUTTON+CURRENT+ROUND;
			public static final String GAME_MATCH_BUTTON_CURRENT_TOURNAMENT = GAME_MATCH_BUTTON+CURRENT+TOURNAMENT;
			public static final String GAME_MATCH_BUTTON_DESCRIPTION = GAME_MATCH_BUTTON+DESCRIPTION;
			public static final String GAME_MATCH_BUTTON_FINISH = GAME_MATCH_BUTTON+FINISH;
			public static final String GAME_MATCH_BUTTON_NEXT_ROUND = GAME_MATCH_BUTTON+NEXT+ROUND;
			public static final String GAME_MATCH_BUTTON_RESULTS = GAME_MATCH_BUTTON+RESULTS;
			public static final String GAME_MATCH_BUTTON_SAVE = GAME_MATCH_BUTTON+SAVE;
			public static final String GAME_MATCH_BUTTON_STATISTICS = GAME_MATCH_BUTTON+STATISTICS;
			/* DESCRIPTION */
			public static final String GAME_MATCH_DESCRIPTION = GAME_MATCH+DESCRIPTION;
			public static final String GAME_MATCH_DESCRIPTION_TITLE = GAME_MATCH_DESCRIPTION+TITLE;
			/* RESULTS */
			public static final String GAME_MATCH_RESULTS = GAME_MATCH+RESULTS;
			public static final String GAME_MATCH_RESULTS_TITLE = GAME_MATCH_RESULTS+TITLE;
			/* STATISTICS */
			public static final String GAME_MATCH_STATISTICS = GAME_MATCH+STATISTICS;
			public static final String GAME_MATCH_STATISTICS_TITLE = GAME_MATCH_STATISTICS+TITLE;
		/* ROUND */
		public static final String GAME_ROUND = GAME+ROUND;
			/* BUTTON */	
			public static final String GAME_ROUND_BUTTON = GAME_ROUND+BUTTON;
			public static final String GAME_ROUND_BUTTON_QUIT = GAME_ROUND_BUTTON+QUIT;
			public static final String GAME_ROUND_BUTTON_CURRENT_MATCH = GAME_ROUND_BUTTON+CURRENT+MATCH;
			public static final String GAME_ROUND_BUTTON_DESCRIPTION = GAME_ROUND_BUTTON+DESCRIPTION;
			public static final String GAME_ROUND_BUTTON_FINISH = GAME_ROUND_BUTTON+FINISH;
			public static final String GAME_ROUND_BUTTON_PLAY = GAME_ROUND_BUTTON+PLAY;
			public static final String GAME_ROUND_BUTTON_RESULTS = GAME_ROUND_BUTTON+RESULTS;
			public static final String GAME_ROUND_BUTTON_SAVE = GAME_ROUND_BUTTON+SAVE;
			public static final String GAME_ROUND_BUTTON_STATISTICS = GAME_ROUND_BUTTON+STATISTICS;
			/* PROGRESS BAR */	
			public static final String GAME_ROUND_PROGRESSBAR = GAME_ROUND+PROGRESSBAR;
			public static final String GAME_ROUND_PROGRESSBAR_BOMBSET = GAME_ROUND_PROGRESSBAR+BOMBSET;
			public static final String GAME_ROUND_PROGRESSBAR_COMPLETE = GAME_ROUND_PROGRESSBAR+COMPLETE;
			public static final String GAME_ROUND_PROGRESSBAR_ITEMSET = GAME_ROUND_PROGRESSBAR+ITEMSET;
			public static final String GAME_ROUND_PROGRESSBAR_PLAYER = GAME_ROUND_PROGRESSBAR+PLAYER;
			public static final String GAME_ROUND_PROGRESSBAR_THEME = GAME_ROUND_PROGRESSBAR+THEME;
			/* DESCRIPTION */
			public static final String GAME_ROUND_DESCRIPTION = GAME_ROUND+DESCRIPTION;
			public static final String GAME_ROUND_DESCRIPTION_TITLE = GAME_ROUND_DESCRIPTION+TITLE;
			public static final String GAME_ROUND_DESCRIPTION_PREVIEW = GAME_ROUND_DESCRIPTION+PREVIEW;
			/* RESULTS */
			public static final String GAME_ROUND_RESULTS = GAME_ROUND+RESULTS;
			public static final String GAME_ROUND_RESULTS_TITLE = GAME_ROUND_RESULTS+TITLE;
			/* STATISTICS */
			public static final String GAME_ROUND_STATISTICS = GAME_ROUND+STATISTICS;
			public static final String GAME_ROUND_STATISTICS_TITLE = GAME_ROUND_STATISTICS+TITLE;
		
	/////////////////////////////////////////////////////////////////
	// COMMON CONTENT	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* AI */
	public static final String COMMON_AI = COMMON+AI;
	public static final String COMMON_AI_AUTHOR = COMMON_AI+AUTHOR;
	public static final String COMMON_AI_NAME = COMMON_AI+NAME;
	public static final String COMMON_AI_PACK = COMMON_AI+PACK;			
			
	/* ARCHIVE */
	public static final String COMMON_ARCHIVE = COMMON+ARCHIVE;
	public static final String COMMON_ARCHIVE_NAME = COMMON_ARCHIVE+NAME;
	public static final String COMMON_ARCHIVE_TYPE = COMMON_ARCHIVE+TYPE;			
	public static final String COMMON_ARCHIVE_CONFRONTATIONS = COMMON_ARCHIVE+CONFRONTATIONS;
	public static final String COMMON_ARCHIVE_PLAYERS = COMMON_ARCHIVE+PLAYERS;
	public static final String COMMON_ARCHIVE_START = COMMON_ARCHIVE+START;			
	public static final String COMMON_ARCHIVE_SAVE = COMMON_ARCHIVE+SAVE;
		/* TYPES */
		public static final String COMMON_ARCHIVE_TYPES = COMMON_ARCHIVE+TYPES;
		public static final String COMMON_ARCHIVE_TYPES_CUP = COMMON_ARCHIVE_TYPES+CUP;
		public static final String COMMON_ARCHIVE_TYPES_LEAGUE = COMMON_ARCHIVE_TYPES+LEAGUE;
		public static final String COMMON_ARCHIVE_TYPES_SEQUENCE = COMMON_ARCHIVE_TYPES+SEQUENCE;
		public static final String COMMON_ARCHIVE_TYPES_SINGLE = COMMON_ARCHIVE_TYPES+SINGLE;

	/* BROWSER */
	public static final String COMMON_BROWSER = COMMON+BROWSER;
		/* FILE */
		public static final String COMMON_BROWSER_FILE = COMMON_BROWSER+FILE;
		public static final String COMMON_BROWSER_FILE_PAGEDOWN = COMMON_BROWSER_FILE+PAGEDOWN;
		public static final String COMMON_BROWSER_FILE_PAGEUP = COMMON_BROWSER_FILE+PAGEUP;
		public static final String COMMON_BROWSER_FILE_PARENT = COMMON_BROWSER_FILE+PARENT;
		/* PACK */
		public static final String COMMON_BROWSER_PACK = COMMON_BROWSER+PACK;
		public static final String COMMON_BROWSER_PACK_PAGEDOWN = COMMON_BROWSER_PACK+PAGEDOWN;
		public static final String COMMON_BROWSER_PACK_PAGEUP = COMMON_BROWSER_PACK+PAGEUP;

	/* COLORS */
	public static final String COMMON_COLOR = COMMON+COLOR;
		public static final String COMMON_COLOR_BLACK = COMMON_COLOR+BLACK;
		public static final String COMMON_COLOR_BLUE = COMMON_COLOR+BLUE;
		public static final String COMMON_COLOR_BROWN = COMMON_COLOR+BROWN;
		public static final String COMMON_COLOR_CYAN = COMMON_COLOR+CYAN;
		public static final String COMMON_COLOR_GRASS = COMMON_COLOR+GRASS;
		public static final String COMMON_COLOR_GREEN = COMMON_COLOR+GREEN;
		public static final String COMMON_COLOR_GREY = COMMON_COLOR+GREY;
		public static final String COMMON_COLOR_INDIGO = COMMON_COLOR+INDIGO;
		public static final String COMMON_COLOR_ORANGE = COMMON_COLOR+ORANGE;
		public static final String COMMON_COLOR_PINK = COMMON_COLOR+PINK;
		public static final String COMMON_COLOR_PURPLE = COMMON_COLOR+PURPLE;
		public static final String COMMON_COLOR_RED = COMMON_COLOR+RED;
		public static final String COMMON_COLOR_RUST = COMMON_COLOR+RUST;
		public static final String COMMON_COLOR_ULTRAMARINE = COMMON_COLOR+ULTRAMARINE;
		public static final String COMMON_COLOR_WHITE = COMMON_COLOR+WHITE;
		public static final String COMMON_COLOR_YELLOW = COMMON_COLOR+YELLOW;	
		
	/* ITEMS */
	public static final String COMMON_ITEMS = COMMON+ITEMS;
		/* AVAILABLE */
		public static final String COMMON_ITEMS_AVAILABLE = COMMON_ITEMS+AVAILABLE;
		public static final String COMMON_ITEMS_AVAILABLE_TITLE = COMMON_ITEMS_AVAILABLE+TITLE;
		/* INITIAL */
		public static final String COMMON_ITEMS_INITIAL = COMMON_ITEMS+INITIAL;
		public static final String COMMON_ITEMS_INITIAL_TITLE = COMMON_ITEMS_INITIAL+TITLE;

	/* LEVEL */
	public static final String COMMON_LEVEL = COMMON+LEVEL;
	public static final String COMMON_LEVEL_TITLE = COMMON_LEVEL+TITLE;
	public static final String COMMON_LEVEL_ALLOWED_PLAYERS = COMMON_LEVEL+ALLOWED+PLAYERS;
	public static final String COMMON_LEVEL_AUTHOR = COMMON_LEVEL+AUTHOR;
	public static final String COMMON_LEVEL_DIMENSION = COMMON_LEVEL+DIMENSION;
	public static final String COMMON_LEVEL_INSTANCE = COMMON_LEVEL+INSTANCE;
	public static final String COMMON_LEVEL_NAME = COMMON_LEVEL+NAME;
	public static final String COMMON_LEVEL_PACK = COMMON_LEVEL+PACK;
	public static final String COMMON_LEVEL_SOURCE = COMMON_LEVEL+SOURCE;
	public static final String COMMON_LEVEL_THEME = COMMON_LEVEL+THEME;
		
	/* LIMIT */
	public static final String COMMON_LIMIT = COMMON+LIMIT;
		/* MATCH */
		public static final String COMMON_LIMIT_MATCH = COMMON_LIMIT+MATCH;
		public static final String COMMON_LIMIT_MATCH_TITLE = COMMON_LIMIT_MATCH+TITLE;
			/* HEADER */
			public static final String COMMON_LIMIT_MATCH_HEADER = COMMON_LIMIT_MATCH+HEADER;
			public static final String COMMON_LIMIT_MATCH_HEADER_BOMBEDS = COMMON_LIMIT_MATCH_HEADER+BOMBEDS;
			public static final String COMMON_LIMIT_MATCH_HEADER_BOMBINGS = COMMON_LIMIT_MATCH_HEADER+BOMBINGS;
			public static final String COMMON_LIMIT_MATCH_HEADER_BOMBS = COMMON_LIMIT_MATCH_HEADER+BOMBS;
			public static final String COMMON_LIMIT_MATCH_HEADER_CONFRONTATIONS = COMMON_LIMIT_MATCH_HEADER+CONFRONTATIONS;
			public static final String COMMON_LIMIT_MATCH_HEADER_CROWNS = COMMON_LIMIT_MATCH_HEADER+CROWNS;
			public static final String COMMON_LIMIT_MATCH_HEADER_CUSTOM = COMMON_LIMIT_MATCH_HEADER+CUSTOM;
			public static final String COMMON_LIMIT_MATCH_HEADER_ITEMS = COMMON_LIMIT_MATCH_HEADER+ITEMS;
			public static final String COMMON_LIMIT_MATCH_HEADER_PAINTINGS = COMMON_LIMIT_MATCH_HEADER+PAINTINGS;
			public static final String COMMON_LIMIT_MATCH_HEADER_TIME = COMMON_LIMIT_MATCH_HEADER+TIME;
		/* ROUND */
		public static final String COMMON_LIMIT_ROUND = COMMON_LIMIT+ROUND;
		public static final String COMMON_LIMIT_ROUND_TITLE = COMMON_LIMIT_ROUND+TITLE;
			/* HEADER */
			public static final String COMMON_LIMIT_ROUND_HEADER = COMMON_LIMIT_ROUND+HEADER;
			public static final String COMMON_LIMIT_ROUND_HEADER_BOMBEDS = COMMON_LIMIT_ROUND_HEADER+BOMBEDS;
			public static final String COMMON_LIMIT_ROUND_HEADER_BOMBINGS = COMMON_LIMIT_ROUND_HEADER+BOMBINGS;
			public static final String COMMON_LIMIT_ROUND_HEADER_BOMBS = COMMON_LIMIT_ROUND_HEADER+BOMBS;
			public static final String COMMON_LIMIT_ROUND_HEADER_CROWNS = COMMON_LIMIT_ROUND_HEADER+CROWNS;
			public static final String COMMON_LIMIT_ROUND_HEADER_CUSTOM = COMMON_LIMIT_ROUND_HEADER+CUSTOM;
			public static final String COMMON_LIMIT_ROUND_HEADER_ITEMS = COMMON_LIMIT_ROUND_HEADER+ITEMS;
			public static final String COMMON_LIMIT_ROUND_HEADER_LAST_STANDING = COMMON_LIMIT_ROUND_HEADER+LAST_STANDING;
			public static final String COMMON_LIMIT_ROUND_HEADER_PAINTINGS = COMMON_LIMIT_ROUND_HEADER+PAINTINGS;
			public static final String COMMON_LIMIT_ROUND_HEADER_TIME = COMMON_LIMIT_ROUND_HEADER+TIME;
		/* TOURNAMENT */
		public static final String COMMON_LIMIT_TOURNAMENT = COMMON_LIMIT+TOURNAMENT;
		public static final String COMMON_LIMIT_TOURNAMENT_TITLE = COMMON_LIMIT_TOURNAMENT+TITLE;
			/* HEADER */
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER = COMMON_LIMIT_TOURNAMENT+HEADER;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_BOMBEDS = COMMON_LIMIT_TOURNAMENT_HEADER+BOMBEDS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_BOMBINGS = COMMON_LIMIT_TOURNAMENT_HEADER+BOMBINGS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_BOMBS = COMMON_LIMIT_TOURNAMENT_HEADER+BOMBS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_CONFRONTATIONS = COMMON_LIMIT_TOURNAMENT_HEADER+CONFRONTATIONS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_CROWNS = COMMON_LIMIT_TOURNAMENT_HEADER+CROWNS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_CUSTOM = COMMON_LIMIT_TOURNAMENT_HEADER+CUSTOM;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_ITEMS = COMMON_LIMIT_TOURNAMENT_HEADER+ITEMS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_PAINTINGS = COMMON_LIMIT_TOURNAMENT_HEADER+PAINTINGS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_TIME = COMMON_LIMIT_TOURNAMENT_HEADER+TIME;

	/* MATCH */
	public static final String COMMON_MATCH = COMMON+MATCH;
	public static final String COMMON_MATCH_AUTHOR = COMMON_MATCH+AUTHOR;
	public static final String COMMON_MATCH_NAME = COMMON_MATCH+NAME;
	public static final String COMMON_MATCH_ALLOWED_PLAYERS = COMMON_MATCH+ALLOWED+PLAYERS;
	public static final String COMMON_MATCH_ROUND_COUNT = COMMON_MATCH+ROUND+COUNT;
			
	/* PLAYERS */
	public static final String COMMON_PLAYERS = COMMON+PLAYERS;
		/* LIST */
		public static final String COMMON_PLAYERS_LIST = COMMON_PLAYERS+LIST;
			/* HEADER */
			public static final String COMMON_PLAYERS_LIST_HEADER = COMMON_PLAYERS_LIST+HEADER;
			public static final String COMMON_PLAYERS_LIST_HEADER_CONTROLS = COMMON_PLAYERS_LIST_HEADER+CONTROLS;
			public static final String COMMON_PLAYERS_LIST_HEADER_HERO = COMMON_PLAYERS_LIST_HEADER+HERO;
			public static final String COMMON_PLAYERS_LIST_HEADER_NAME = COMMON_PLAYERS_LIST_HEADER+NAME;
			public static final String COMMON_PLAYERS_LIST_HEADER_PROFILE = COMMON_PLAYERS_LIST_HEADER+PROFILE;
			public static final String COMMON_PLAYERS_LIST_HEADER_RANK = COMMON_PLAYERS_LIST_HEADER+RANK;
			/* DATA */
			public static final String COMMON_PLAYERS_LIST_DATA = COMMON_PLAYERS_LIST+DATA;
			public static final String COMMON_PLAYERS_LIST_DATA_COMPUTER = COMMON_PLAYERS_LIST_DATA+COMPUTER;
			public static final String COMMON_PLAYERS_LIST_DATA_CONTROLS = COMMON_PLAYERS_LIST_DATA+CONTROLS;
			public static final String COMMON_PLAYERS_LIST_DATA_HUMAN = COMMON_PLAYERS_LIST_DATA+HUMAN;
			public static final String COMMON_PLAYERS_LIST_DATA_NO_CONTROLS = COMMON_PLAYERS_LIST_DATA+NO_CONTROLS;
		/* SELECTION */
		public static final String COMMON_PLAYERS_SELECTION = COMMON_PLAYERS+SELECTION;
			/* HEADER */
			public static final String COMMON_PLAYERS_SELECTION_HEADER = COMMON_PLAYERS_SELECTION+HEADER;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_COLOR = COMMON_PLAYERS_SELECTION_HEADER+COLOR;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_CONTROLS = COMMON_PLAYERS_SELECTION_HEADER+CONTROLS;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_HERO = COMMON_PLAYERS_SELECTION_HEADER+HERO;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_PROFILE = COMMON_PLAYERS_SELECTION_HEADER+PROFILE;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_TYPE = COMMON_PLAYERS_SELECTION_HEADER+TYPE;
			/* DATA */
			public static final String COMMON_PLAYERS_SELECTION_DATA = COMMON_PLAYERS_SELECTION+DATA;
			public static final String COMMON_PLAYERS_SELECTION_DATA_ADD = COMMON_PLAYERS_SELECTION_DATA+ADD;
			public static final String COMMON_PLAYERS_SELECTION_DATA_COMPUTER = COMMON_PLAYERS_SELECTION_DATA+COMPUTER;
			public static final String COMMON_PLAYERS_SELECTION_DATA_DELETE = COMMON_PLAYERS_SELECTION_DATA+DELETE;
			public static final String COMMON_PLAYERS_SELECTION_DATA_HUMAN = COMMON_PLAYERS_SELECTION_DATA+HUMAN;

	/* POINTS */
	public static final String COMMON_POINTS = COMMON+POINTS;
		/* MATCH */
		public static final String COMMON_POINTS_MATCH = COMMON_POINTS+MATCH;
		public static final String COMMON_POINTS_MATCH_TITLE = COMMON_POINTS_MATCH+TITLE;
			/* HEADER */
			public static final String COMMON_POINTS_MATCH_HEADER = COMMON_POINTS_MATCH+HEADER;
			public static final String COMMON_POINTS_MATCH_HEADER_CONSTANT = COMMON_POINTS_MATCH_HEADER+CONSTANT;
			public static final String COMMON_POINTS_MATCH_HEADER_DISCRETIZE = COMMON_POINTS_MATCH_HEADER+DISCRETIZE;
			public static final String COMMON_POINTS_MATCH_HEADER_PARTIAL = COMMON_POINTS_MATCH_HEADER+PARTIAL;
			public static final String COMMON_POINTS_MATCH_HEADER_RANKINGS = COMMON_POINTS_MATCH_HEADER+RANKINGS;
			public static final String COMMON_POINTS_MATCH_HEADER_RANKPOINTS = COMMON_POINTS_MATCH_HEADER+RANKPOINTS;
			public static final String COMMON_POINTS_MATCH_HEADER_SCORE = COMMON_POINTS_MATCH_HEADER+SCORE;
			public static final String COMMON_POINTS_MATCH_HEADER_TOTAL = COMMON_POINTS_MATCH_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_POINTS_MATCH_DATA = COMMON_POINTS_MATCH+DATA;
			public static final String COMMON_POINTS_MATCH_DATA_BOMBEDS = COMMON_POINTS_MATCH_DATA+BOMBEDS;
			public static final String COMMON_POINTS_MATCH_DATA_BOMBINGS = COMMON_POINTS_MATCH_DATA+BOMBINGS;
			public static final String COMMON_POINTS_MATCH_DATA_BOMBS = COMMON_POINTS_MATCH_DATA+BOMBS;
			public static final String COMMON_POINTS_MATCH_DATA_CROWNS = COMMON_POINTS_MATCH_DATA+CROWNS;
			public static final String COMMON_POINTS_MATCH_DATA_INVERTED = COMMON_POINTS_MATCH_DATA+INVERTED;
			public static final String COMMON_POINTS_MATCH_DATA_ITEMS = COMMON_POINTS_MATCH_DATA+ITEMS;
			public static final String COMMON_POINTS_MATCH_DATA_NOSHARE = COMMON_POINTS_MATCH_DATA+NO_SHARE;
			public static final String COMMON_POINTS_MATCH_DATA_PAINTINGS = COMMON_POINTS_MATCH_DATA+PAINTINGS;
			public static final String COMMON_POINTS_MATCH_DATA_PARTIAL = COMMON_POINTS_MATCH_DATA+PARTIAL;
			public static final String COMMON_POINTS_MATCH_DATA_REGULAR = COMMON_POINTS_MATCH_DATA+REGULAR;
			public static final String COMMON_POINTS_MATCH_DATA_SHARE = COMMON_POINTS_MATCH_DATA+SHARE;
			public static final String COMMON_POINTS_MATCH_DATA_TIME = COMMON_POINTS_MATCH_DATA+TIME;
		/* ROUND */
		public static final String COMMON_POINTS_ROUND = COMMON_POINTS+ROUND;
		public static final String COMMON_POINTS_ROUND_TITLE = COMMON_POINTS_ROUND+TITLE;
			/* HEADER */
			public static final String COMMON_POINTS_ROUND_HEADER = COMMON_POINTS_ROUND+HEADER;
			public static final String COMMON_POINTS_ROUND_HEADER_CONSTANT = COMMON_POINTS_ROUND_HEADER+CONSTANT;
			public static final String COMMON_POINTS_ROUND_HEADER_DISCRETIZE = COMMON_POINTS_ROUND_HEADER+DISCRETIZE;
			public static final String COMMON_POINTS_ROUND_HEADER_PARTIAL = COMMON_POINTS_ROUND_HEADER+PARTIAL;
			public static final String COMMON_POINTS_ROUND_HEADER_RANKINGS = COMMON_POINTS_ROUND_HEADER+RANKINGS;
			public static final String COMMON_POINTS_ROUND_HEADER_RANKPOINTS = COMMON_POINTS_ROUND_HEADER+RANKPOINTS;
			public static final String COMMON_POINTS_ROUND_HEADER_SCORE = COMMON_POINTS_ROUND_HEADER+SCORE;
			public static final String COMMON_POINTS_ROUND_HEADER_TOTAL = COMMON_POINTS_ROUND_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_POINTS_ROUND_DATA = COMMON_POINTS_ROUND+DATA;
			public static final String COMMON_POINTS_ROUND_DATA_BOMBEDS = COMMON_POINTS_ROUND_DATA+BOMBEDS;
			public static final String COMMON_POINTS_ROUND_DATA_BOMBINGS = COMMON_POINTS_ROUND_DATA+BOMBINGS;
			public static final String COMMON_POINTS_ROUND_DATA_BOMBS = COMMON_POINTS_ROUND_DATA+BOMBS;
			public static final String COMMON_POINTS_ROUND_DATA_CROWNS = COMMON_POINTS_ROUND_DATA+CROWNS;
			public static final String COMMON_POINTS_ROUND_DATA_INVERTED = COMMON_POINTS_ROUND_DATA+INVERTED;
			public static final String COMMON_POINTS_ROUND_DATA_ITEMS = COMMON_POINTS_ROUND_DATA+ITEMS;
			public static final String COMMON_POINTS_ROUND_DATA_NOSHARE = COMMON_POINTS_ROUND_DATA+NO_SHARE;
			public static final String COMMON_POINTS_ROUND_DATA_PAINTINGS = COMMON_POINTS_ROUND_DATA+PAINTINGS;
			public static final String COMMON_POINTS_ROUND_DATA_PARTIAL = COMMON_POINTS_ROUND_DATA+PARTIAL;
			public static final String COMMON_POINTS_ROUND_DATA_REGULAR = COMMON_POINTS_ROUND_DATA+REGULAR;
			public static final String COMMON_POINTS_ROUND_DATA_SHARE = COMMON_POINTS_ROUND_DATA+SHARE;
			public static final String COMMON_POINTS_ROUND_DATA_TIME = COMMON_POINTS_ROUND_DATA+TIME;
		/* TOURNAMENT */
		public static final String COMMON_POINTS_TOURNAMENT = COMMON_POINTS+TOURNAMENT;
		public static final String COMMON_POINTS_TOURNAMENT_TITLE = COMMON_POINTS_TOURNAMENT+TITLE;
			/* HEADER */
			public static final String COMMON_POINTS_TOURNAMENT_HEADER = COMMON_POINTS_TOURNAMENT+HEADER;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_CONSTANT = COMMON_POINTS_TOURNAMENT_HEADER+CONSTANT;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_DISCRETIZE = COMMON_POINTS_TOURNAMENT_HEADER+DISCRETIZE;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_PARTIAL = COMMON_POINTS_TOURNAMENT_HEADER+PARTIAL;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_RANKINGS = COMMON_POINTS_TOURNAMENT_HEADER+RANKINGS;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_RANKPOINTS = COMMON_POINTS_TOURNAMENT_HEADER+RANKPOINTS;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_SCORE = COMMON_POINTS_TOURNAMENT_HEADER+SCORE;
			public static final String COMMON_POINTS_TOURNAMENT_HEADER_TOTAL = COMMON_POINTS_TOURNAMENT_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_POINTS_TOURNAMENT_DATA = COMMON_POINTS_TOURNAMENT+DATA;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_BOMBEDS = COMMON_POINTS_TOURNAMENT_DATA+BOMBEDS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_BOMBINGS = COMMON_POINTS_TOURNAMENT_DATA+BOMBINGS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_BOMBS = COMMON_POINTS_TOURNAMENT_DATA+BOMBS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_CROWNS = COMMON_POINTS_TOURNAMENT_DATA+CROWNS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_INVERTED = COMMON_POINTS_TOURNAMENT_DATA+INVERTED;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_ITEMS = COMMON_POINTS_TOURNAMENT_DATA+ITEMS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_NOSHARE = COMMON_POINTS_TOURNAMENT_DATA+NO_SHARE;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_PAINTINGS = COMMON_POINTS_TOURNAMENT_DATA+PAINTINGS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_PARTIAL = COMMON_POINTS_TOURNAMENT_DATA+PARTIAL;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_REGULAR = COMMON_POINTS_TOURNAMENT_DATA+REGULAR;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_SHARE = COMMON_POINTS_TOURNAMENT_DATA+SHARE;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_TIME = COMMON_POINTS_TOURNAMENT_DATA+TIME;

	/* PROFILES */
	public static final String COMMON_PROFILES = COMMON+PROFILES;
	public static final String COMMON_PROFILES_AI_NAME = COMMON_PROFILES+AI+NAME;
	public static final String COMMON_PROFILES_AI_PACK = COMMON_PROFILES+AI+PACK;
	public static final String COMMON_PROFILES_COLOR = COMMON_PROFILES+COLOR;
	public static final String COMMON_PROFILES_HERO_NAME = COMMON_PROFILES+HERO+NAME;
	public static final String COMMON_PROFILES_HERO_PACK = COMMON_PROFILES+HERO+PACK;
	public static final String COMMON_PROFILES_NAME = COMMON_PROFILES+NAME;
			
	/* RESULTS */
	public static final String COMMON_RESULTS = COMMON+RESULTS;
		/* MATCH */
		public static final String COMMON_RESULTS_MATCH = COMMON_RESULTS+MATCH;
			/* HEADER */
			public static final String COMMON_RESULTS_MATCH_HEADER = COMMON_RESULTS_MATCH+HEADER;
			public static final String COMMON_RESULTS_MATCH_HEADER_BOMBEDS = COMMON_RESULTS_MATCH_HEADER+BOMBEDS;
			public static final String COMMON_RESULTS_MATCH_HEADER_BOMBINGS = COMMON_RESULTS_MATCH_HEADER+BOMBINGS;
			public static final String COMMON_RESULTS_MATCH_HEADER_BOMBS = COMMON_RESULTS_MATCH_HEADER+BOMBS;
			public static final String COMMON_RESULTS_MATCH_HEADER_CONFRONTATION = COMMON_RESULTS_MATCH_HEADER+CONFRONTATION;
			public static final String COMMON_RESULTS_MATCH_HEADER_CROWNS = COMMON_RESULTS_MATCH_HEADER+CROWNS;
			public static final String COMMON_RESULTS_MATCH_HEADER_LIMIT = COMMON_RESULTS_MATCH_HEADER+LIMIT;
			public static final String COMMON_RESULTS_MATCH_HEADER_ITEMS = COMMON_RESULTS_MATCH_HEADER+ITEMS;
			public static final String COMMON_RESULTS_MATCH_HEADER_NAME = COMMON_RESULTS_MATCH_HEADER+NAME;
			public static final String COMMON_RESULTS_MATCH_HEADER_PAINTINGS = COMMON_RESULTS_MATCH_HEADER+PAINTINGS;
			public static final String COMMON_RESULTS_MATCH_HEADER_POINTS = COMMON_RESULTS_MATCH_HEADER+POINTS;
			public static final String COMMON_RESULTS_MATCH_HEADER_PORTRAIT = COMMON_RESULTS_MATCH_HEADER+PORTRAIT;
			public static final String COMMON_RESULTS_MATCH_HEADER_PROFILE = COMMON_RESULTS_MATCH_HEADER+PROFILE;
			public static final String COMMON_RESULTS_MATCH_HEADER_TIME = COMMON_RESULTS_MATCH_HEADER+TIME;
			public static final String COMMON_RESULTS_MATCH_HEADER_TOTAL = COMMON_RESULTS_MATCH_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_RESULTS_MATCH_DATA = COMMON_RESULTS_MATCH+DATA;
			public static final String COMMON_RESULTS_MATCH_DATA_COMPUTER = COMMON_RESULTS_MATCH_DATA+COMPUTER;
			public static final String COMMON_RESULTS_MATCH_DATA_HUMAN = COMMON_RESULTS_MATCH_DATA+HUMAN;
		/* TOURNAMENT */
		public static final String COMMON_RESULTS_TOURNAMENT = COMMON_RESULTS+TOURNAMENT;
			/* HEADER */
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER = COMMON_RESULTS_TOURNAMENT+HEADER;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_BOMBEDS = COMMON_RESULTS_TOURNAMENT_HEADER+BOMBEDS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_BOMBINGS = COMMON_RESULTS_TOURNAMENT_HEADER+BOMBINGS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_BOMBS = COMMON_RESULTS_TOURNAMENT_HEADER+BOMBS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_CONFRONTATION = COMMON_RESULTS_TOURNAMENT_HEADER+CONFRONTATION;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_CROWNS = COMMON_RESULTS_TOURNAMENT_HEADER+CROWNS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_LIMIT = COMMON_RESULTS_TOURNAMENT_HEADER+LIMIT;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_ITEMS = COMMON_RESULTS_TOURNAMENT_HEADER+ITEMS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_NAME = COMMON_RESULTS_TOURNAMENT_HEADER+NAME;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_PAINTINGS = COMMON_RESULTS_TOURNAMENT_HEADER+PAINTINGS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_POINTS = COMMON_RESULTS_TOURNAMENT_HEADER+POINTS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_PORTRAIT = COMMON_RESULTS_TOURNAMENT_HEADER+PORTRAIT;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_PROFILE = COMMON_RESULTS_TOURNAMENT_HEADER+PROFILE;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_TIME = COMMON_RESULTS_TOURNAMENT_HEADER+TIME;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_TOTAL = COMMON_RESULTS_TOURNAMENT_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_RESULTS_TOURNAMENT_DATA = COMMON_RESULTS_TOURNAMENT+DATA;
			public static final String COMMON_RESULTS_TOURNAMENT_DATA_COMPUTER = COMMON_RESULTS_TOURNAMENT_DATA+COMPUTER;
			public static final String COMMON_RESULTS_TOURNAMENT_DATA_HUMAN = COMMON_RESULTS_TOURNAMENT_DATA+HUMAN;
		/* ROUND */
		public static final String COMMON_RESULTS_ROUND = COMMON_RESULTS+ROUND;
			/* HEADER */
			public static final String COMMON_RESULTS_ROUND_HEADER = COMMON_RESULTS_ROUND+HEADER;
			public static final String COMMON_RESULTS_ROUND_HEADER_BOMBEDS = COMMON_RESULTS_ROUND_HEADER+BOMBEDS;
			public static final String COMMON_RESULTS_ROUND_HEADER_BOMBINGS = COMMON_RESULTS_ROUND_HEADER+BOMBINGS;
			public static final String COMMON_RESULTS_ROUND_HEADER_BOMBS = COMMON_RESULTS_ROUND_HEADER+BOMBS;
			public static final String COMMON_RESULTS_ROUND_HEADER_CROWNS = COMMON_RESULTS_ROUND_HEADER+CROWNS;
			public static final String COMMON_RESULTS_ROUND_HEADER_LIMIT = COMMON_RESULTS_ROUND_HEADER+LIMIT;
			public static final String COMMON_RESULTS_ROUND_HEADER_ITEMS = COMMON_RESULTS_ROUND_HEADER+ITEMS;
			public static final String COMMON_RESULTS_ROUND_HEADER_NAME = COMMON_RESULTS_ROUND_HEADER+NAME;
			public static final String COMMON_RESULTS_ROUND_HEADER_PAINTINGS = COMMON_RESULTS_ROUND_HEADER+PAINTINGS;
			public static final String COMMON_RESULTS_ROUND_HEADER_POINTS = COMMON_RESULTS_ROUND_HEADER+POINTS;
			public static final String COMMON_RESULTS_ROUND_HEADER_PORTRAIT = COMMON_RESULTS_ROUND_HEADER+PORTRAIT;
			public static final String COMMON_RESULTS_ROUND_HEADER_PROFILE = COMMON_RESULTS_ROUND_HEADER+PROFILE;
			public static final String COMMON_RESULTS_ROUND_HEADER_TIME = COMMON_RESULTS_ROUND_HEADER+TIME;
			/* DATA */
			public static final String COMMON_RESULTS_ROUND_DATA = COMMON_RESULTS_ROUND+DATA;
			public static final String COMMON_RESULTS_ROUND_DATA_COMPUTER = COMMON_RESULTS_ROUND_DATA+COMPUTER;
			public static final String COMMON_RESULTS_ROUND_DATA_HUMAN = COMMON_RESULTS_MATCH_DATA+HUMAN;
	
	/* ROUND */
	public static final String COMMON_ROUND = COMMON+ROUND;
	public static final String COMMON_ROUND_ALLOWED_PLAYERS = COMMON_ROUND+ALLOWED+PLAYERS;
	public static final String COMMON_ROUND_AUTHOR = COMMON_ROUND+AUTHOR;
	public static final String COMMON_ROUND_LEVEL_FOLDER = COMMON_ROUND+LEVEL+FOLDER;
	public static final String COMMON_ROUND_LEVEL_PACK = COMMON_ROUND+LEVEL+PACK;
	public static final String COMMON_ROUND_TITLE = COMMON_ROUND+TITLE;
	
	/* SPRITE */
	public static final String COMMON_SPRITE = COMMON+SPRITE;
	public static final String COMMON_SPRITE_AUTHOR = COMMON_SPRITE+AUTHOR;
	public static final String COMMON_SPRITE_COLORS = COMMON_SPRITE+COLORS;
	public static final String COMMON_SPRITE_IMAGE = COMMON_SPRITE+IMAGE;
	public static final String COMMON_SPRITE_NAME = COMMON_SPRITE+NAME;
	public static final String COMMON_SPRITE_PACK = COMMON_SPRITE+PACK;
	public static final String COMMON_SPRITE_SOURCE = COMMON_SPRITE+SOURCE;	
	
	/* TOURNAMENT */
	public static final String COMMON_TOURNAMENT = COMMON+TOURNAMENT;
	public static final String COMMON_TOURNAMENT_AUTHOR = COMMON_TOURNAMENT+AUTHOR;
	public static final String COMMON_TOURNAMENT_NAME = COMMON_TOURNAMENT+NAME;
	public static final String COMMON_TOURNAMENT_ALLOWED_PLAYERS = COMMON_TOURNAMENT+ALLOWED+PLAYERS;
	public static final String COMMON_TOURNAMENT_TYPE = COMMON_TOURNAMENT+TYPE;
		/* TYPES */
		public static final String COMMON_TOURNAMENT_TYPES = COMMON_TOURNAMENT+TYPES;
		public static final String COMMON_TOURNAMENT_TYPES_CUP = COMMON_TOURNAMENT_TYPES+CUP;
		public static final String COMMON_TOURNAMENT_TYPES_LEAGUE = COMMON_TOURNAMENT_TYPES+LEAGUE;
		public static final String COMMON_TOURNAMENT_TYPES_SEQUENCE = COMMON_TOURNAMENT_TYPES+SEQUENCE;
		public static final String COMMON_TOURNAMENT_TYPES_SINGLE = COMMON_TOURNAMENT_TYPES+SINGLE;

	/* TRANSFER */
	public static final String COMMON_TRANSFER = COMMON+TRANSFER;
	public static final String COMMON_TRANSFER_LEFT = COMMON_TRANSFER+LEFT;
	public static final String COMMON_TRANSFER_RIGHT = COMMON_TRANSFER+RIGHT;
			
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * processes the list of keys starting like the parameter.
	 * usefull to process the font size for a given set of buttons
	 * sharing the same key start.
	 * @param keyStart
	 * @return
	 */
	public static ArrayList<String> getKeysLike(String keyStart)
	{	ArrayList<String> result = new ArrayList<String>();
		Iterator<Entry<String,String>> it = GuiConfiguration.getMiscConfiguration().getLanguage().getTexts().entrySet().iterator();
		while(it.hasNext())
		{	Entry<String,String> temp = it.next();
			String key = temp.getKey();
			String value = temp.getValue();
			if(!key.endsWith(GuiKeys.TOOLTIP) && (key.startsWith(keyStart)))
				result.add(value);
		}
		
		return result;
	}
}