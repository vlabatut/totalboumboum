package org.totalboumboum.gui.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.totalboumboum.gui.data.configuration.GuiConfiguration;


/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GuiKeys
{
	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final String ABOUT = "About";
	public static final String ADD = "Add";
	public static final String ADJUST = "Adjust";
	public static final String ADVANCE = "Advance";
	public static final String ADVANCED = "Advanced";
	public static final String AFTER = "After";
	public static final String AI = "Ai";
	public static final String AIS = "Ais";
	public static final String ALL = "All";
	public static final String ALLOWED = "Allowed";
	public static final String ARCHIVE = "Archive";
	public static final String AUTHOR = "Author";
	public static final String AUTO = "Auto";
	public static final String AUTOKILL = "Autokill";
	public static final String AUTOLOAD = "Autoload";
	public static final String AUTOSAVE = "Autosave";
	public static final String AVAILABLE = "Available";
	public static final String AVERAGE = "Average";
	public static final String BACK = "Back";
	public static final String BACKWARD = "Backward";
	public static final String BACKGROUND = "Background";
	public static final String BEFORE = "Before";
	public static final String BLACK = "Black";
	public static final String BLOCK = "Block";
	public static final String BLUE = "Blue";
	public static final String BOMBEDS = "Bombeds";
	public static final String BOMBINGS = "Bombings";
	public static final String BOMB = "Bomb";
	public static final String BOMBS = "Bombs";
	public static final String BOMBSET = "Bombset";
	public static final String BOTH = "Both";
	public static final String BROWN = "Brown";
	public static final String BROWSE = "Browse";
	public static final String BROWSER = "Browser";
	public static final String BUTTON = "Button";
	public static final String CACHE = "Cache";
	public static final String CANCEL = "Cancel";
	public static final String CENTRAL = "Central";
	public static final String CHANGE = "Change";
	public static final String CLOSED = "Closed";
	public static final String COLOR = "Color";
	public static final String COLORS = "Colors";
	public static final String COMMON = "Common";
	public static final String COMPLETE = "Complete";
	public static final String COMPUTER = "Computer";
	public static final String CONFIRM = "Confirm";
	public static final String CONFIRMED = "Confirmed";
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
	public static final String DEFAULT = "Default";
	public static final String DELAY = "Delay";
	public static final String DELETE = "Delete";
	public static final String DESCRIPTION = "Description";
	public static final String DEVIATION = "Deviation";
	public static final String DIALOG = "Dialog";
	public static final String DIMENSION = "Dimension";
	public static final String DIRECT = "Direct";
	public static final String DISABLED = "Disabled";
	public static final String DISCRETIZE = "Discretize";
	public static final String DISPLAY = "Display";
	public static final String DO = "Do";
	public static final String DONE = "Done";
	public static final String DOWN = "Down";
	public static final String DRAW = "Draw";
	public static final String DRAWN = "Drawn";
	public static final String EDIT = "Edit";
	public static final String ENABLED = "Enabled";
	public static final String ENTER = "Enter";
	public static final String ENTRY = "Entry";
	public static final String EVOLUTION = "Evolution";
	public static final String EXCEPTIONS = "Exceptions";
	public static final String EXIT = "Exit";
	public static final String FALSE = "False";
	public static final String FASTER = "Faster";
	public static final String FAV = "Fav";
	public static final String FILE = "File";
	public static final String FINISH = "Finish";
	public static final String FINISHED = "Finished";
	public static final String FIRESETMAP = "Firesetmap";
	public static final String FIXED = "Fixed";
	public static final String FOLDER = "Folder";
	public static final String FONT = "Font";
	public static final String FORWARD = "Forward";
	public static final String FPS = "Fps";
	public static final String FULL = "Full";
	public static final String GAME = "Game";
	public static final String GAMES = "Games";
	public static final String GLICKO2 = "Glicko2";
	public static final String GO = "Go";
	public static final String GRAPHICS = "Graphics";
	public static final String GRASS = "Grass";
	public static final String GREEN = "Green";
	public static final String GREY = "Grey";
	public static final String GUI = "Gui";
	public static final String HEADER = "Header";
	public static final String HERO = "Hero";
	public static final String HEROES = "Heroes";
	public static final String HIDE = "Hide";
	public static final String HOST = "Host";
	public static final String HUMAN = "Human";
	public static final String IMAGE = "Image";
	public static final String INCLUDE = "Include";
	public static final String INDIGO = "Indigo";
	public static final String INFO = "Info";
	public static final String INITIAL = "Initial";
	public static final String INSTANCE = "Instance";
	public static final String INSTANCES = "Instances";
	public static final String INVERTED = "Inverted";
	public static final String IP = "Ip";
	public static final String ITEMS = "Items";
	public static final String ITEMSET = "Itemset";
	public static final String LANGUAGE = "Language";
	public static final String LAST_STANDING = "LastStanding";
	public static final String LEAGUE = "League";
	public static final String LEFT = "Left";
	public static final String LEG = "Leg";
	public static final String LEVEL = "Level";
	public static final String LEVELS = "Levels";
	public static final String LIMIT = "Limit";
	public static final String LINE = "Line";
	public static final String LIST = "List";
	public static final String LOAD = "Load";
	public static final String LOCATION = "Location";
	public static final String LOG = "Log";
	public static final String LOST = "Lost";
	public static final String MAIN = "Main";
	public static final String MATCH = "Match";
	public static final String MATCHES = "Matches";
	public static final String MEAN = "Mean";
	public static final String MENU = "Menu";
	public static final String MESSAGES = "Messages";
	public static final String MINUS = "Minus";
	public static final String MODIFY = "Modify";
	public static final String NAME = "Name";
	public static final String NETWORK = "Network";
	public static final String NEW = "New";
	public static final String NEXT = "Next";
	public static final String NO = "No";
	public static final String NON = "Non";
	public static final String NONE = "None";
	public static final String NOTES = "Notes";
	public static final String OPEN = "Open";
	public static final String OPTIONS = "Options";
	public static final String ORANGE = "Orange";
	public static final String ORDER = "Order";
	public static final String PACK = "Pack";
	public static final String PACKAGE = "Package";
	public static final String PAGEDOWN = "PageDown";
	public static final String PAGEUP = "PageUp";
	public static final String PAINTINGS = "Paintings";
	public static final String PANEL = "Panel";
	public static final String PARENT = "Parent";
	public static final String PART = "Part";
	public static final String PARTIAL = "Partial";
	public static final String PER = "Per";
	public static final String PERIOD = "Period";
	public static final String PINK = "Pink";
	public static final String PLAY = "Play";
	public static final String PLAYED = "Played";
	public static final String PLAYER = "Player";
	public static final String PLAYERS = "Players";
	public static final String PLAYING = "Playing";
	public static final String PLUS = "Plus";
	public static final String POINTS = "Points";
	public static final String PORTRAIT = "Portrait";
	public static final String PREFERRED = "Preferred";
	public static final String PREVIEW = "Preview";
	public static final String PREVIOUS = "Previous";
	public static final String PROFILE = "Profile";
	public static final String PROFILES = "Profiles";
	public static final String PROGRESSBAR = "Progressbar";
	public static final String PUBLISH = "Publish";
	public static final String PURPLE = "Purple";
	public static final String QUESTION = "Question";
	public static final String QUICKMATCH = "Quickmatch";
	public static final String QUICKSTART = "Quickstart";
	public static final String QUIT = "Quit";
	public static final String RANDOM = "Random";
	public static final String RANK = "Rank";
	public static final String RANKS = "Ranks";
	public static final String RANKINGS = "Rankings";
	public static final String RANKPOINTS = "Rankpoints";
	public static final String RATING = "Rating";
	public static final String READY = "Ready";
	public static final String RECORD = "Record";
	public static final String RED = "Red";
	public static final String REGISTER = "Register";
	public static final String REGULAR = "Regular";
	public static final String REINIT = "Reinit";
	public static final String REMOTE = "Remote";
	public static final String REMOVE = "Remove";
	public static final String REPLAY = "Replay";
	public static final String RESET = "Reset";
	public static final String RESOURCES = "Resources";
	public static final String RESULTS = "Results";
	public static final String RETRIEVING = "Retrieving";
	public static final String RIGHT = "Right";
	public static final String ROUND = "Round";
	public static final String ROUNDS = "Rounds";
	public static final String RUST = "Rust";
	public static final String SAME = "Same";
	public static final String SAVE = "Save";
	public static final String SCORE = "Score";
	public static final String SCORES = "Scores";
	public static final String SCREEN = "Screen";
	public static final String SELECT = "Select";
	public static final String SELECTION = "Selection";
	public static final String SELF = "Self";
	public static final String SEQUENCE = "Sequence";
	public static final String SET = "Set";
	public static final String SETTINGS = "Settings";
	public static final String SHARE = "Share";
	public static final String SIMULATION = "Simulation";
	public static final String SINGLE = "Single";
	public static final String SIZE = "Size";
	public static final String SLOWER = "Slower";
	public static final String SMOOTH = "Smooth";
	public static final String SOURCE = "Source";
	public static final String SPEED = "Speed";
	public static final String SPRITE = "Sprite";
	public static final String STANDARD = "Standard";
	public static final String START = "Start";
	public static final String STATE = "State";
	public static final String STATISTICS = "Statistics";
	public static final String SUDDEN_DEATH = "SuddenDeath";
	public static final String SUM = "Sum";
	public static final String TEXT = "Text";
	public static final String THEME = "Theme";
	public static final String TIME = "Time";
	public static final String TITLE = "Title";
	public static final String TOOLTIP = "Tooltip";
	public static final String TOTAL = "Total";
	public static final String TOURNAMENT = "Tournament";
	public static final String TOURNAMENTS = "Tournaments";
	public static final String TRANSFER = "Transfer";
	public static final String TRUE = "True";
	public static final String TURNING = "Turning";
	public static final String TYPE = "Type";
	public static final String TYPES = "Types";
	public static final String ULTRAMARINE = "Ultramarine";
	public static final String UNCONFIRMED = "Unconfirmed";
	public static final String UNDECIDED = "Undecided";
	public static final String UNKNOWN = "Unknown";
	public static final String UNREGISTER = "Unregister";
	public static final String UP = "Up";
	public static final String UPS = "Ups";
	public static final String USE = "Use";
	public static final String USELESS = "Useless";
	public static final String VALIDATE = "Validate";
	public static final String VALUE = "Value";
	public static final String VALUES = "Values";
	public static final String VAR = "Var";
	public static final String VIDEO = "Video";
	public static final String VOLATILITY = "Volatility";
	public static final String WHITE = "White";
	public static final String WON = "Won";
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
		public static final String MENU_MAIN_BUTTON_NETWORK = MENU_MAIN_BUTTON+NETWORK;
		public static final String MENU_MAIN_BUTTON_OPTIONS = MENU_MAIN_BUTTON+OPTIONS;
		public static final String MENU_MAIN_BUTTON_PROFILES = MENU_MAIN_BUTTON+PROFILES;
		public static final String MENU_MAIN_BUTTON_QUICKMATCH = MENU_MAIN_BUTTON+QUICKMATCH;
		public static final String MENU_MAIN_BUTTON_QUIT = MENU_MAIN_BUTTON+QUIT;
		public static final String MENU_MAIN_BUTTON_REPLAY = MENU_MAIN_BUTTON+REPLAY;
		public static final String MENU_MAIN_BUTTON_RESOURCES = MENU_MAIN_BUTTON+RESOURCES;
		public static final String MENU_MAIN_BUTTON_STATISTICS = MENU_MAIN_BUTTON+STATISTICS;
		public static final String MENU_MAIN_BUTTON_TOURNAMENT = MENU_MAIN_BUTTON+TOURNAMENT;
	
	/* ABOUT */
	public static final String MENU_ABOUT = MENU+ABOUT;
	public static final String MENU_ABOUT_TITLE = MENU_ABOUT+TITLE;

	/* RESOURCES */
	public static final String MENU_RESOURCES = MENU+RESOURCES;
	public static final String MENU_RESOURCES_BUTTON = MENU_RESOURCES+BUTTON;
	public static final String MENU_RESOURCES_BUTTON_AI = MENU_RESOURCES_BUTTON+AI;
	public static final String MENU_RESOURCES_BUTTON_BACK = MENU_RESOURCES_BUTTON+BACK;
	public static final String MENU_RESOURCES_BUTTON_HEROES = MENU_RESOURCES_BUTTON+HEROES;
	public static final String MENU_RESOURCES_BUTTON_INSTANCES = MENU_RESOURCES_BUTTON+INSTANCES;
	public static final String MENU_RESOURCES_BUTTON_LEVELS = MENU_RESOURCES_BUTTON+LEVELS;
	public static final String MENU_RESOURCES_BUTTON_MATCHES = MENU_RESOURCES_BUTTON+MATCHES;
	public static final String MENU_RESOURCES_BUTTON_ROUNDS = MENU_RESOURCES_BUTTON+ROUNDS;
	public static final String MENU_RESOURCES_BUTTON_TOURNAMENTS = MENU_RESOURCES_BUTTON+TOURNAMENTS;
		/* AI */
			/* BUTTON */
			public static final String MENU_RESOURCES_AI = MENU_RESOURCES+AI;	
			public static final String MENU_RESOURCES_AI_BUTTON = MENU_RESOURCES_AI+BUTTON;	
			public static final String MENU_RESOURCES_AI_BUTTON_BACK = MENU_RESOURCES_AI_BUTTON+BACK;	
			/* SELECT */
			public static final String MENU_RESOURCES_AI_SELECT = MENU_RESOURCES_AI+SELECT;
			public static final String MENU_RESOURCES_AI_SELECT_TITLE = MENU_RESOURCES_AI_SELECT+TITLE;
			public static final String MENU_RESOURCES_AI_SELECT_NOTES = MENU_RESOURCES_AI_SELECT+NOTES;
		/* HERO */
		public static final String MENU_RESOURCES_HERO = MENU_RESOURCES+HERO;
		public static final String MENU_RESOURCES_HERO_TITLE = MENU_RESOURCES_HERO+TITLE;
			/* BUTTON */
			public static final String MENU_RESOURCES_HERO_BUTTON = MENU_RESOURCES_HERO+BUTTON;	
			public static final String MENU_RESOURCES_HERO_BUTTON_BACK = MENU_RESOURCES_HERO_BUTTON+BACK;	
		/* LEVEL */
		public static final String MENU_RESOURCES_LEVEL = MENU_RESOURCES+LEVEL;	
			/* BUTTON */
			public static final String MENU_RESOURCES_LEVEL_BUTTON = MENU_RESOURCES_LEVEL+BUTTON;	
			public static final String MENU_RESOURCES_LEVEL_BUTTON_BACK = MENU_RESOURCES_LEVEL_BUTTON+BACK;	
			/* SELECT */
			public static final String MENU_RESOURCES_LEVEL_SELECT = MENU_RESOURCES_LEVEL+SELECT;
			public static final String MENU_RESOURCES_LEVEL_SELECT_TITLE = MENU_RESOURCES_LEVEL_SELECT+TITLE;
				/* FOLDER */
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER = MENU_RESOURCES_LEVEL_SELECT+FOLDER;
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEDOWN = MENU_RESOURCES_LEVEL_SELECT_FOLDER+PAGEDOWN;
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PAGEUP = MENU_RESOURCES_LEVEL_SELECT_FOLDER+PAGEUP;
				public static final String MENU_RESOURCES_LEVEL_SELECT_FOLDER_PARENT = MENU_RESOURCES_LEVEL_SELECT_FOLDER+PARENT;
				/* PACKAGES */
				public static final String MENU_RESOURCES_LEVEL_SELECT_PACKAGE = MENU_RESOURCES_LEVEL_SELECT+PACKAGE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEDOWN = MENU_RESOURCES_LEVEL_SELECT_PACKAGE+PAGEDOWN;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PACKAGE_PAGEUP = MENU_RESOURCES_LEVEL_SELECT_PACKAGE+PAGEUP;
				/* PREVIEW */
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW = MENU_RESOURCES_LEVEL_SELECT+PREVIEW;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_AUTHOR = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+AUTHOR;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_IMAGE = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+IMAGE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_INSTANCE = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+INSTANCE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_NAME = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+NAME;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_PACKAGE = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+PACKAGE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SIZE = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+SIZE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_SOURCE = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+SOURCE;
				public static final String MENU_RESOURCES_LEVEL_SELECT_PREVIEW_THEME = MENU_RESOURCES_LEVEL_SELECT_PREVIEW+THEME;
		/* MATCH */
		public static final String MENU_RESOURCES_MATCH = MENU_RESOURCES+MATCH;
		public static final String MENU_RESOURCES_MATCH_TITLE = MENU_RESOURCES_MATCH+TITLE;
			/* BUTTON */
			public static final String MENU_RESOURCES_MATCH_BUTTON = MENU_RESOURCES_MATCH+BUTTON;	
			public static final String MENU_RESOURCES_MATCH_BUTTON_BACK = MENU_RESOURCES_MATCH_BUTTON+BACK;	
		/* ROUND */
		public static final String MENU_RESOURCES_ROUND = MENU_RESOURCES+ROUND;
		public static final String MENU_RESOURCES_ROUND_TITLE = MENU_RESOURCES_ROUND+TITLE;
			/* BUTTON */
			public static final String MENU_RESOURCES_ROUND_BUTTON = MENU_RESOURCES_ROUND+BUTTON;	
			public static final String MENU_RESOURCES_ROUND_BUTTON_BACK = MENU_RESOURCES_ROUND_BUTTON+BACK;	
		/* TOURNAMENT */
		public static final String MENU_RESOURCES_TOURNAMENT = MENU_RESOURCES+TOURNAMENT;
		public static final String MENU_RESOURCES_TOURNAMENT_TITLE = MENU_RESOURCES_TOURNAMENT+TITLE;
			/* BUTTON */
			public static final String MENU_RESOURCES_TOURNAMENT_BUTTON = MENU_RESOURCES_TOURNAMENT+BUTTON;	
			public static final String MENU_RESOURCES_TOURNAMENT_BUTTON_BACK = MENU_RESOURCES_TOURNAMENT_BUTTON+BACK;	
			
	/* OPTIONS */
	public static final String MENU_OPTIONS = MENU+OPTIONS;
		/* BUTTON */
		public static final String MENU_OPTIONS_BUTTON = MENU_OPTIONS+BUTTON;
		public static final String MENU_OPTIONS_BUTTON_ADVANCED = MENU_OPTIONS_BUTTON+ADVANCED;
		public static final String MENU_OPTIONS_BUTTON_AIS = MENU_OPTIONS_BUTTON+AIS;
		public static final String MENU_OPTIONS_BUTTON_BACK = MENU_OPTIONS_BUTTON+BACK;
		public static final String MENU_OPTIONS_BUTTON_CANCEL = MENU_OPTIONS_BUTTON+CANCEL;
		public static final String MENU_OPTIONS_BUTTON_CONFIRM = MENU_OPTIONS_BUTTON+CONFIRM;
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = MENU_OPTIONS_BUTTON+CONTROLS;
		public static final String MENU_OPTIONS_BUTTON_GAME = MENU_OPTIONS_BUTTON+GAME;
		public static final String MENU_OPTIONS_BUTTON_GUI = MENU_OPTIONS_BUTTON+GUI;
		public static final String MENU_OPTIONS_BUTTON_NEXT = MENU_OPTIONS_BUTTON+NEXT;
		public static final String MENU_OPTIONS_BUTTON_PREVIOUS = MENU_OPTIONS_BUTTON+PREVIOUS;
		public static final String MENU_OPTIONS_BUTTON_STATISTICS = MENU_OPTIONS_BUTTON+STATISTICS;
		public static final String MENU_OPTIONS_BUTTON_VIDEO = MENU_OPTIONS_BUTTON+VIDEO;
		/* CONFIRM */
		public static final String MENU_OPTIONS_CONFIRM = MENU_OPTIONS+CONFIRM;
		public static final String MENU_OPTIONS_CONFIRM_QUESTION = MENU_OPTIONS_CONFIRM+QUESTION;
		public static final String MENU_OPTIONS_CONFIRM_TITLE = MENU_OPTIONS_CONFIRM+TITLE;
		
		/* ADVANCED */
		public static final String MENU_OPTIONS_ADVANCED = MENU_OPTIONS+ADVANCED;
		public static final String MENU_OPTIONS_ADVANCED_TITLE = MENU_OPTIONS_ADVANCED+TITLE;
			/* LINE */
			public static final String MENU_OPTIONS_ADVANCED_LINE = MENU_OPTIONS_ADVANCED+LINE;
				/* ADJUST FPS */
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST = MENU_OPTIONS_ADVANCED_LINE+ADJUST;
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_TITLE = MENU_OPTIONS_ADVANCED_LINE_ADJUST+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED = MENU_OPTIONS_ADVANCED_LINE_ADJUST+DISABLED;
				public static final String MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED = MENU_OPTIONS_ADVANCED_LINE_ADJUST+ENABLED;
				/* CACHE */
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE = MENU_OPTIONS_ADVANCED_LINE+CACHE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_TITLE = MENU_OPTIONS_ADVANCED_LINE_CACHE+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_DISABLED = MENU_OPTIONS_ADVANCED_LINE_CACHE+DISABLED;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_ENABLED = MENU_OPTIONS_ADVANCED_LINE_CACHE+ENABLED;
				/* CACHE LIMIT */
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT = MENU_OPTIONS_ADVANCED_LINE+CACHE+LIMIT;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_TITLE = MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_MINUS = MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT+MINUS;
				public static final String MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT_PLUS = MENU_OPTIONS_ADVANCED_LINE_CACHE_LIMIT+PLUS;
				/* FPS */
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS = MENU_OPTIONS_ADVANCED_LINE+FPS;
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_TITLE = MENU_OPTIONS_ADVANCED_LINE_FPS+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS = MENU_OPTIONS_ADVANCED_LINE_FPS+MINUS;
				public static final String MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS = MENU_OPTIONS_ADVANCED_LINE_FPS+PLUS;
				/* RECORD GAMES */
				public static final String MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES = MENU_OPTIONS_ADVANCED_LINE+RECORD+GAMES;
				public static final String MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_TITLE = MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_DISABLED = MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES+DISABLED;
				public static final String MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES_ENABLED = MENU_OPTIONS_ADVANCED_LINE_RECORD_GAMES+ENABLED;
				/* SPEED */
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED = MENU_OPTIONS_ADVANCED_LINE+SPEED;
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_TITLE = MENU_OPTIONS_ADVANCED_LINE_SPEED+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS = MENU_OPTIONS_ADVANCED_LINE_SPEED+MINUS;
				public static final String MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS = MENU_OPTIONS_ADVANCED_LINE_SPEED+PLUS;
				/* LOG EXCEPTIONS */
				public static final String MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS = MENU_OPTIONS_ADVANCED_LINE+LOG+CONTROLS;
				public static final String MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_TITLE = MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS+TITLE;
				public static final String MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_DISABLED = MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS+DISABLED;
				public static final String MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS_ENABLED = MENU_OPTIONS_ADVANCED_LINE_LOG_CONTROLS+ENABLED;
		/* AIS */
		public static final String MENU_OPTIONS_AIS = MENU_OPTIONS+AIS;
		public static final String MENU_OPTIONS_AIS_TITLE = MENU_OPTIONS_AIS+TITLE;
			/* LINE */
			public static final String MENU_OPTIONS_AIS_LINE = MENU_OPTIONS_AIS+LINE;
				/* AUTO ADVANCE */
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE = MENU_OPTIONS_AIS_LINE+AUTO+ADVANCE;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_TITLE = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DISABLED = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE+DISABLED;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_ENABLED = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE+ENABLED;
				/* AUTO ADVANCE DELAY */
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY = MENU_OPTIONS_AIS_LINE+AUTO+ADVANCE+DELAY;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_TITLE = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_MINUS = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY+MINUS;
				public static final String MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY_PLUS = MENU_OPTIONS_AIS_LINE_AUTO_ADVANCE_DELAY+PLUS;
				/* UPS */
				public static final String MENU_OPTIONS_AIS_LINE_UPS = MENU_OPTIONS_AIS_LINE+UPS;
				public static final String MENU_OPTIONS_AIS_LINE_UPS_TITLE = MENU_OPTIONS_AIS_LINE_UPS+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_UPS_MINUS = MENU_OPTIONS_AIS_LINE_UPS+MINUS;
				public static final String MENU_OPTIONS_AIS_LINE_UPS_PLUS = MENU_OPTIONS_AIS_LINE_UPS+PLUS;
				/* HIDE ALL AIS */
				public static final String MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS = MENU_OPTIONS_AIS_LINE+HIDE+ALL+AIS;
				public static final String MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_TITLE = MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_DISABLED = MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS+DISABLED;
				public static final String MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS_ENABLED = MENU_OPTIONS_AIS_LINE_HIDE_ALL_AIS+ENABLED;
				/* BOMB USELESS AIS */
				public static final String MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS = MENU_OPTIONS_AIS_LINE+BOMB+USELESS+AIS;
				public static final String MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_TITLE = MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_MINUS = MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS+MINUS;
				public static final String MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_PLUS = MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS+PLUS;
				public static final String MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS_DISABLED = MENU_OPTIONS_AIS_LINE_BOMB_USELESS_AIS+DISABLED;
				/* DISPLAY EXCEPTIONS */
				public static final String MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS = MENU_OPTIONS_AIS_LINE+DISPLAY+EXCEPTIONS;
				public static final String MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_TITLE = MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_DISABLED = MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS+DISABLED;
				public static final String MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS_ENABLED = MENU_OPTIONS_AIS_LINE_DISPLAY_EXCEPTIONS+ENABLED;
				/* LOG EXCEPTIONS */
				public static final String MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS = MENU_OPTIONS_AIS_LINE+LOG+EXCEPTIONS;
				public static final String MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_TITLE = MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS+TITLE;
				public static final String MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_DISABLED = MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS+DISABLED;
				public static final String MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS_ENABLED = MENU_OPTIONS_AIS_LINE_LOG_EXCEPTIONS+ENABLED;
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
				public static final String MENU_OPTIONS_CONTROLS_LINE_COMMAND_TRIGGERBOMB = "MenuOptionsControlsLineCommandTriggerbomb";
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
		public static final String MENU_OPTIONS_GUI = MENU_OPTIONS+GUI;
		public static final String MENU_OPTIONS_GUI_TITLE = MENU_OPTIONS_GUI+TITLE;
			/* LINE */
			public static final String MENU_OPTIONS_GUI_LINE = MENU_OPTIONS_GUI+LINE;
				/* LANGUAGE */
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE = MENU_OPTIONS_GUI_LINE+LANGUAGE;
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_TITLE = MENU_OPTIONS_GUI_LINE_LANGUAGE+TITLE;
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT = MENU_OPTIONS_GUI_LINE_LANGUAGE+NEXT;
				public static final String MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS = MENU_OPTIONS_GUI_LINE_LANGUAGE+PREVIOUS;
				/* FONT */
				public static final String MENU_OPTIONS_GUI_LINE_FONT = MENU_OPTIONS_GUI_LINE+FONT;
				public static final String MENU_OPTIONS_GUI_LINE_FONT_TITLE = MENU_OPTIONS_GUI_LINE_FONT+TITLE;
				public static final String MENU_OPTIONS_GUI_LINE_FONT_NEXT = MENU_OPTIONS_GUI_LINE_FONT+NEXT;
				public static final String MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS = MENU_OPTIONS_GUI_LINE_FONT+PREVIOUS;
				/* BACKGROUND */
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND = MENU_OPTIONS_GUI_LINE+BACKGROUND;
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_TITLE = MENU_OPTIONS_GUI_LINE_BACKGROUND+TITLE;
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT = MENU_OPTIONS_GUI_LINE_BACKGROUND+NEXT;
				public static final String MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS = MENU_OPTIONS_GUI_LINE_BACKGROUND+PREVIOUS;
		/* STATISTICS */
		public static final String MENU_OPTIONS_STATISTICS = MENU_OPTIONS+STATISTICS;
		public static final String MENU_OPTIONS_STATISTICS_TITLE = MENU_OPTIONS_STATISTICS+TITLE;
			/* LINE */
			public static final String MENU_OPTIONS_STATISTICS_LINE = MENU_OPTIONS_STATISTICS+LINE;
				/* INCLUDE */
				public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE = MENU_OPTIONS_STATISTICS_LINE+INCLUDE;
					/* QUICKSTARTS */
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART = MENU_OPTIONS_STATISTICS_LINE_INCLUDE+QUICKSTART;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_TITLE = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_DISABLED = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART+DISABLED;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART_ENABLED = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_QUICKSTART+ENABLED;
					/* SIMULATIONS */
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION = MENU_OPTIONS_STATISTICS_LINE_INCLUDE+SIMULATION;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_TITLE = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_DISABLED = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION+DISABLED;
					public static final String MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION_ENABLED = MENU_OPTIONS_STATISTICS_LINE_INCLUDE_SIMULATION+ENABLED;
				/* GLICKO-2 */
				public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2 = MENU_OPTIONS_STATISTICS_LINE+GLICKO2;
					/* DEFAULT RATING */
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING = MENU_OPTIONS_STATISTICS_LINE_GLICKO2+DEFAULT+RATING;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_TITLE = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_MINUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING+MINUS;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_PLUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING+PLUS;
					/* DEFAULT RATING DEVIATION */
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING+DEVIATION;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_TITLE = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_MINUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION+MINUS;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION_PLUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_DEVIATION+PLUS;
					/* DEFAULT RATING VOLATILITY */
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING+VOLATILITY;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_TITLE = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_MINUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY+MINUS;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY_PLUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_DEFAULT_RATING_VOLATILITY+PLUS;
					/* GAMES PER PERIOD */
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD = MENU_OPTIONS_STATISTICS_LINE_GLICKO2+GAMES+PER+PERIOD;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_TITLE = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD+TITLE;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_MINUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD+MINUS;
					public static final String MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD_PLUS = MENU_OPTIONS_STATISTICS_LINE_GLICKO2_GAMES_PER_PERIOD+PLUS;
				/* REINIT */
				public static final String MENU_OPTIONS_STATISTICS_LINE_REINIT = MENU_OPTIONS_STATISTICS_LINE+REINIT;
				public static final String MENU_OPTIONS_STATISTICS_LINE_REINIT_TITLE = MENU_OPTIONS_STATISTICS_LINE_REINIT+TITLE;
				public static final String MENU_OPTIONS_STATISTICS_LINE_REINIT_DO = MENU_OPTIONS_STATISTICS_LINE_REINIT+DO;
				public static final String MENU_OPTIONS_STATISTICS_LINE_REINIT_DONE = MENU_OPTIONS_STATISTICS_LINE_REINIT+DONE;
			/* DIALOG */
			public static final String MENU_OPTIONS_STATISTICS_DIALOG = MENU_OPTIONS_STATISTICS+DIALOG;
			public static final String MENU_OPTIONS_STATISTICS_DIALOG_TITLE = MENU_OPTIONS_STATISTICS_DIALOG+TITLE;
			public static final String MENU_OPTIONS_STATISTICS_DIALOG_QUESTION = MENU_OPTIONS_STATISTICS_DIALOG+QUESTION;			
		/* VIDEO */
		public static final String MENU_OPTIONS_VIDEO = MENU_OPTIONS+VIDEO;
		public static final String MENU_OPTIONS_VIDEO_TITLE = MENU_OPTIONS_VIDEO+TITLE;
			/* LINE */
			public static final String MENU_OPTIONS_VIDEO_LINE = MENU_OPTIONS_VIDEO+LINE;
			public static final String MENU_OPTIONS_VIDEO_LINE_DISABLED = MENU_OPTIONS_VIDEO_LINE+DISABLED;
			public static final String MENU_OPTIONS_VIDEO_LINE_ENABLED = MENU_OPTIONS_VIDEO_LINE+ENABLED;
			public static final String MENU_OPTIONS_VIDEO_LINE_FULL_SCREEN = MENU_OPTIONS_VIDEO_LINE+FULL+SCREEN;
			public static final String MENU_OPTIONS_VIDEO_LINE_MINUS = MENU_OPTIONS_VIDEO_LINE+MINUS;
			public static final String MENU_OPTIONS_VIDEO_LINE_PANEL_DIMENSION = MENU_OPTIONS_VIDEO_LINE+PANEL+DIMENSION;
			public static final String MENU_OPTIONS_VIDEO_LINE_PLUS = MENU_OPTIONS_VIDEO_LINE+PLUS;
			public static final String MENU_OPTIONS_VIDEO_LINE_SMOOTH_GRAPHICS = MENU_OPTIONS_VIDEO_LINE+SMOOTH+GRAPHICS;
				/* COLOR */
			public static final String MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR = "MenuOptionsVideoLineBorderColor";
				public static final String MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR_BLACK = MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR+BLACK;
				public static final String MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR_NONE = MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR+NONE;
				public static final String MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR_TITLE = MENU_OPTIONS_VIDEO_LINE_BORDER_COLOR+TITLE;
			
	/* PROFILES */
	public static final String MENU_PROFILES = MENU+PROFILES;
		/* BUTTON */
		public static final String MENU_PROFILES_BUTTON = MENU_PROFILES+BUTTON;
		public static final String MENU_PROFILES_BUTTON_BACK = MENU_PROFILES_BUTTON+BACK;
		public static final String MENU_PROFILES_BUTTON_CANCEL = MENU_PROFILES_BUTTON+CANCEL;
		public static final String MENU_PROFILES_BUTTON_CONFIRM = MENU_PROFILES_BUTTON+CONFIRM;
		public static final String MENU_PROFILES_BUTTON_DELETE = MENU_PROFILES_BUTTON+DELETE;
		public static final String MENU_PROFILES_BUTTON_MODIFY = MENU_PROFILES_BUTTON+MODIFY;
		public static final String MENU_PROFILES_BUTTON_NEW = MENU_PROFILES_BUTTON+NEW;
		/* EDIT */
		public static final String MENU_PROFILES_EDIT = MENU_PROFILES+EDIT;		
		public static final String MENU_PROFILES_EDIT_TITLE = MENU_PROFILES_EDIT+TITLE;		
		public static final String MENU_PROFILES_EDIT_AI = MENU_PROFILES_EDIT+AI;
		public static final String MENU_PROFILES_EDIT_AI_CHANGE = MENU_PROFILES_EDIT_AI+CHANGE;
		public static final String MENU_PROFILES_EDIT_AI_RESET = MENU_PROFILES_EDIT_AI+RESET;
		public static final String MENU_PROFILES_EDIT_COLOR = MENU_PROFILES_EDIT+COLOR;
		public static final String MENU_PROFILES_EDIT_COLOR_NEXT = MENU_PROFILES_EDIT_COLOR+NEXT;
		public static final String MENU_PROFILES_EDIT_COLOR_PREVIOUS = MENU_PROFILES_EDIT_COLOR+PREVIOUS;
		public static final String MENU_PROFILES_EDIT_HERO = MENU_PROFILES_EDIT+HERO;
		public static final String MENU_PROFILES_EDIT_HERO_CHANGE = MENU_PROFILES_EDIT_HERO+CHANGE;
		public static final String MENU_PROFILES_EDIT_NAME = MENU_PROFILES_EDIT+NAME;
		public static final String MENU_PROFILES_EDIT_NAME_CHANGE = MENU_PROFILES_EDIT_NAME+CHANGE;
		/* DELETE */
		public static final String MENU_PROFILES_DELETE = MENU_PROFILES+DELETE;
		public static final String MENU_PROFILES_DELETE_QUESTION = MENU_PROFILES_DELETE+QUESTION;
		public static final String MENU_PROFILES_DELETE_TITLE = MENU_PROFILES_DELETE+TITLE;
		/* NEW */
		public static final String MENU_PROFILES_NEW = MENU_PROFILES+NEW;
		public static final String MENU_PROFILES_NEW_NAME = MENU_PROFILES_NEW+NAME;
		public static final String MENU_PROFILES_NEW_QUESTION = MENU_PROFILES_NEW+QUESTION;
		public static final String MENU_PROFILES_NEW_TITLE = MENU_PROFILES_NEW+TITLE;
		/* SELECT */
		public static final String MENU_PROFILES_SELECT = MENU_PROFILES+SELECT;
		public static final String MENU_PROFILES_SELECT_TITLE = MENU_PROFILES_SELECT+TITLE;
		
	/* STATISTICS NOTE*/	
	public static final String MENU_STATISTICS = MENU+STATISTICS;
		/* PLAYER */
		public static final String MENU_STATISTICS_PLAYER = MENU_STATISTICS+PLAYER;
		public static final String MENU_STATISTICS_PLAYER_TITLE = MENU_STATISTICS_PLAYER+TITLE;
			/* BUTTON */
			public static final String MENU_STATISTICS_PLAYER_BUTTON = MENU_STATISTICS_PLAYER+BUTTON;
			public static final String MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS = MENU_STATISTICS_PLAYER_BUTTON+CONFRONTATIONS;
			public static final String MENU_STATISTICS_PLAYER_BUTTON_GLICKO2 = MENU_STATISTICS_PLAYER_BUTTON+GLICKO2;
			public static final String MENU_STATISTICS_PLAYER_BUTTON_SCORES = MENU_STATISTICS_PLAYER_BUTTON+SCORES;
		
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
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_BLOCK_PLAYERS = MENU_QUICKMATCH_SETTINGS_BUTTON+BLOCK+PLAYERS;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_NEXT = MENU_QUICKMATCH_SETTINGS_BUTTON+NEXT;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_PREVIOUS = MENU_QUICKMATCH_SETTINGS_BUTTON+PREVIOUS;
			public static final String MENU_QUICKMATCH_SETTINGS_BUTTON_PUBLISH = MENU_QUICKMATCH_SETTINGS_BUTTON+PUBLISH;
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
			/* SUDDEN DEATH */
			public static final String MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH = MENU_QUICKMATCH_SETTINGS+SUDDEN_DEATH;
				public static final String MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_TITLE = MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH+TITLE;
				public static final String MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_VAR = MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH+VAR;
				public static final String MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH_YOK = MENU_QUICKMATCH_SETTINGS_SUDDEN_DEATH+YOK;

	/* REPLAY */
	public static final String MENU_REPLAY = MENU+REPLAY;
		/* LOAD */
		public static final String MENU_REPLAY_LOAD = MENU_REPLAY+LOAD;
		public static final String MENU_REPLAY_LOAD_TITLE = MENU_REPLAY_LOAD+TITLE;
			/* BUTTONS */
			public static final String MENU_REPLAY_LOAD_BUTTON = MENU_REPLAY_LOAD+BUTTON;
			public static final String MENU_REPLAY_LOAD_BUTTON_CANCEL = MENU_REPLAY_LOAD_BUTTON+CANCEL;
			public static final String MENU_REPLAY_LOAD_BUTTON_CONFIRM = MENU_REPLAY_LOAD_BUTTON+CONFIRM;
			public static final String MENU_REPLAY_LOAD_BUTTON_DELETE = MENU_REPLAY_LOAD_BUTTON+DELETE;
			/* DELETE */
			public static final String MENU_REPLAY_LOAD_DELETE = MENU_REPLAY_LOAD+DELETE;
			public static final String MENU_REPLAY_LOAD_DELETE_TITLE = MENU_REPLAY_LOAD_DELETE+TITLE;
			public static final String MENU_REPLAY_LOAD_DELETE_QUESTION = MENU_REPLAY_LOAD_DELETE+QUESTION;
		/* CONTROLS */
		public static final String MENU_REPLAY_CONTROLS = MENU_REPLAY+CONTROLS;
			/* BUTTONS */
			public static final String MENU_REPLAY_CONTROLS_BUTTON = MENU_REPLAY_CONTROLS+BUTTON;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_BACK = MENU_REPLAY_CONTROLS_BUTTON+BACK;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_BACKWARD = MENU_REPLAY_CONTROLS_BUTTON+BACKWARD;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_FASTER = MENU_REPLAY_CONTROLS_BUTTON+FASTER;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_FORWARD = MENU_REPLAY_CONTROLS_BUTTON+FORWARD;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_PLAY = MENU_REPLAY_CONTROLS_BUTTON+PLAY;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_QUIT = MENU_REPLAY_CONTROLS_BUTTON+QUIT;
			public static final String MENU_REPLAY_CONTROLS_BUTTON_SLOWER = MENU_REPLAY_CONTROLS_BUTTON+SLOWER;
		/* ROUND */
		public static final String MENU_REPLAY_ROUND = MENU_REPLAY+ROUND;
			/* BUTTONS */
			public static final String MENU_REPLAY_ROUND_BUTTON = MENU_REPLAY_ROUND+BUTTON;
			public static final String MENU_REPLAY_ROUND_BUTTON_BACK = MENU_REPLAY_ROUND_BUTTON+BACK;
			public static final String MENU_REPLAY_ROUND_BUTTON_QUIT = MENU_REPLAY_ROUND_BUTTON+QUIT;
			public static final String MENU_REPLAY_ROUND_BUTTON_REPLAY = MENU_REPLAY_ROUND_BUTTON+REPLAY;
				
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
			/* DELETE */
			public static final String MENU_TOURNAMENT_LOAD_DELETE = MENU_TOURNAMENT_LOAD+DELETE;
			public static final String MENU_TOURNAMENT_LOAD_DELETE_TITLE = MENU_TOURNAMENT_LOAD_DELETE+TITLE;
			public static final String MENU_TOURNAMENT_LOAD_DELETE_QUESTION = MENU_TOURNAMENT_LOAD_DELETE+QUESTION;
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
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_BLOCK_PLAYERS = MENU_TOURNAMENT_SETTINGS_BUTTON+BLOCK+PLAYERS;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_CANCEL = MENU_TOURNAMENT_SETTINGS_BUTTON+CANCEL;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_CONFIRM = MENU_TOURNAMENT_SETTINGS_BUTTON+CONFIRM;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT = MENU_TOURNAMENT_SETTINGS_BUTTON+NEXT;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH = MENU_TOURNAMENT_SETTINGS_BUTTON+PUBLISH;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS = MENU_TOURNAMENT_SETTINGS_BUTTON+PREVIOUS;
			public static final String MENU_TOURNAMENT_SETTINGS_BUTTON_SELECT = MENU_TOURNAMENT_SETTINGS_BUTTON+SELECT;
	
		/* NETWORK GAME */
		public static final String MENU_NETWORK = MENU+NETWORK;
			/* BUTTONS */
			public static final String MENU_NETWORK_BUTTON = MENU_NETWORK+BUTTON;
			public static final String MENU_NETWORK_BUTTON_QUIT = MENU_NETWORK_BUTTON+QUIT;
			/* GAMES */
			public static final String MENU_NETWORK_GAMES = MENU_NETWORK+GAMES;
			public static final String MENU_NETWORK_GAMES_TITLE = MENU_NETWORK_GAMES+TITLE;
				/* ADD HOST */
				public static final String MENU_NETWORK_GAMES_ADD_HOST = MENU_NETWORK_GAMES+ADD+HOST;
				public static final String MENU_NETWORK_GAMES_ADD_HOST_TITLE = MENU_NETWORK_GAMES_ADD_HOST+TITLE;
				public static final String MENU_NETWORK_GAMES_ADD_HOST_TEXT = MENU_NETWORK_GAMES_ADD_HOST+TEXT;
				/* SET HOST */
				public static final String MENU_NETWORK_GAMES_SET_HOST = MENU_NETWORK_GAMES+SET+HOST;
				public static final String MENU_NETWORK_GAMES_SET_HOST_TITLE = MENU_NETWORK_GAMES_SET_HOST+TITLE;
				public static final String MENU_NETWORK_GAMES_SET_HOST_TEXT = MENU_NETWORK_GAMES_SET_HOST+TEXT;
				/* BUTTONS */
				public static final String MENU_NETWORK_GAMES_BUTTON = MENU_NETWORK_GAMES+BUTTON;
				public static final String MENU_NETWORK_GAMES_BUTTON_NEXT = MENU_NETWORK_GAMES_BUTTON+NEXT;
				public static final String MENU_NETWORK_GAMES_BUTTON_PREVIOUS = MENU_NETWORK_GAMES_BUTTON+PREVIOUS;
			/* PLAYERS */
			public static final String MENU_NETWORK_PLAYERS = MENU_NETWORK+PLAYERS;
			public static final String MENU_NETWORK_PLAYERS_TITLE = MENU_NETWORK_PLAYERS+TITLE;
				/* BUTTONS */
				public static final String MENU_NETWORK_PLAYERS_BUTTON = MENU_NETWORK_PLAYERS+BUTTON;
				public static final String MENU_NETWORK_PLAYERS_BUTTON_CANCEL = MENU_NETWORK_PLAYERS_BUTTON+CANCEL;
				public static final String MENU_NETWORK_PLAYERS_BUTTON_CONFIRM = MENU_NETWORK_PLAYERS_BUTTON+CONFIRM;
				public static final String MENU_NETWORK_PLAYERS_BUTTON_VALIDATE = MENU_NETWORK_PLAYERS_BUTTON+VALIDATE;
				public static final String MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS = MENU_NETWORK_PLAYERS_BUTTON+PREVIOUS;
		
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
			/* CONFIRM */
			public static final String GAME_SAVE_CONFIRM = GAME_SAVE+CONFIRM;
			public static final String GAME_SAVE_CONFIRM_QUESTION = GAME_SAVE_CONFIRM+QUESTION;
			public static final String GAME_SAVE_CONFIRM_TITLE = GAME_SAVE_CONFIRM+TITLE;
			/* DELETE */
			public static final String GAME_SAVE_DELETE = GAME_SAVE+DELETE;
			public static final String GAME_SAVE_DELETE_QUESTION = GAME_SAVE_DELETE+QUESTION;
			public static final String GAME_SAVE_DELETE_TITLE = GAME_SAVE_DELETE+TITLE;
			/* NEW */
			public static final String GAME_SAVE_NEW = GAME_SAVE+NEW;
			public static final String GAME_SAVE_NEW_NAME = GAME_SAVE_NEW+NAME;
			public static final String GAME_SAVE_NEW_QUESTION = GAME_SAVE_NEW+QUESTION;
			public static final String GAME_SAVE_NEW_TITLE = GAME_SAVE_NEW+TITLE;
			
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
			public static final String GAME_TOURNAMENT_BUTTON_RECORD_GAMES = GAME_TOURNAMENT_BUTTON+RECORD+GAMES;
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
			public static final String GAME_MATCH_BUTTON_RECORD_GAMES = GAME_MATCH_BUTTON+RECORD+GAMES;
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
			public static final String GAME_ROUND_BUTTON_RECORD_GAMES = GAME_ROUND_BUTTON+RECORD+GAMES;
			public static final String GAME_ROUND_BUTTON_RESULTS = GAME_ROUND_BUTTON+RESULTS;
			public static final String GAME_ROUND_BUTTON_SAVE = GAME_ROUND_BUTTON+SAVE;
			public static final String GAME_ROUND_BUTTON_STATISTICS = GAME_ROUND_BUTTON+STATISTICS;
			/* PROGRESS BAR */	
			public static final String GAME_ROUND_PROGRESSBAR = GAME_ROUND+PROGRESSBAR;
			public static final String GAME_ROUND_PROGRESSBAR_BOMBSET = GAME_ROUND_PROGRESSBAR+BOMBSET;
			public static final String GAME_ROUND_PROGRESSBAR_COMPLETE = GAME_ROUND_PROGRESSBAR+COMPLETE;
			public static final String GAME_ROUND_PROGRESSBAR_FIRESETMAP = GAME_ROUND_PROGRESSBAR+FIRESETMAP;
			public static final String GAME_ROUND_PROGRESSBAR_ITEMSET = GAME_ROUND_PROGRESSBAR+ITEMSET;
			public static final String GAME_ROUND_PROGRESSBAR_PLAYER = GAME_ROUND_PROGRESSBAR+PLAYER;
			public static final String GAME_ROUND_PROGRESSBAR_THEME = GAME_ROUND_PROGRESSBAR+THEME;
			public static final String GAME_ROUND_PROGRESSBAR_SIMULATION = GAME_ROUND_PROGRESSBAR+SIMULATION;
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
			/* MESSAGES */
			public static final String GAME_ROUND_MESSAGES = GAME_ROUND+MESSAGES;
			public static final String GAME_ROUND_MESSAGES_READY = GAME_ROUND_MESSAGES+READY;
			public static final String GAME_ROUND_MESSAGES_SET = GAME_ROUND_MESSAGES+SET;
			public static final String GAME_ROUND_MESSAGES_GO = GAME_ROUND_MESSAGES+GO;
		
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

	/* DIALOG */
	public static final String COMMON_DIALOG = COMMON+DIALOG;
	public static final String COMMON_DIALOG_CANCEL = COMMON_DIALOG+CANCEL;
	public static final String COMMON_DIALOG_CONFIRM = COMMON_DIALOG+CONFIRM;

	/* GAME INFO */
	public static final String COMMON_GAME_INFO = COMMON+GAME+INFO;
	public static final String COMMON_GAME_INFO_ALLOWED_PLAYERS = COMMON_GAME_INFO+ALLOWED+PLAYERS;
	public static final String COMMON_GAME_INFO_AVERAGE_SCORE = COMMON_GAME_INFO+AVERAGE+SCORE;
	public static final String COMMON_GAME_INFO_PLAYER_COUNT = COMMON_GAME_INFO+PLAYER+COUNT;
	public static final String COMMON_GAME_INFO_TOURNAMENT_NAME = COMMON_GAME_INFO+TOURNAMENT+NAME;
		/* GAME INFO TOURNAMENT STATE */
		public static final String COMMON_GAME_INFO_TOURNAMENT_STATE = COMMON_GAME_INFO+TOURNAMENT+STATE;
			/* GAME INFO TOURNAMENT STATE DATA */
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA = COMMON_GAME_INFO_TOURNAMENT_STATE+DATA;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_CLOSED = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+CLOSED;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_FINISHED = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+FINISHED;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_OPEN = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+OPEN;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_PLAYING = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+PLAYING;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_RETRIEVING = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+RETRIEVING;
			public static final String COMMON_GAME_INFO_TOURNAMENT_STATE_DATA_UNKNOWN = COMMON_GAME_INFO_TOURNAMENT_STATE_DATA+UNKNOWN;
		/* GAME INFO TOURNAMENT TYPE */
		public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE = COMMON_GAME_INFO+TOURNAMENT+TYPE;
			/* GAME INFO TOURNAMENT TYPE DATA */
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA = COMMON_GAME_INFO_TOURNAMENT_TYPE+DATA;
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_CUP = COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA+CUP;
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_LEAGUE = COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA+LEAGUE;
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_SEQUENCE = COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA+SEQUENCE;
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_SINGLE = COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA+SINGLE;
			public static final String COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA_TURNING = COMMON_GAME_INFO_TOURNAMENT_TYPE_DATA+TURNING;

	/* GAME LIST */
	public static final String COMMON_GAME_LIST = COMMON+GAME+LIST;
		/* GAME LIST BUTTON */
		public static final String COMMON_GAME_LIST_BUTTON = COMMON_GAME_LIST+BUTTON;
		public static final String COMMON_GAME_LIST_BUTTON_ADD = COMMON_GAME_LIST_BUTTON+ADD;
		public static final String COMMON_GAME_LIST_BUTTON_CENTRAL = COMMON_GAME_LIST_BUTTON+CENTRAL;
		public static final String COMMON_GAME_LIST_BUTTON_DIRECT = COMMON_GAME_LIST_BUTTON+DIRECT;
		public static final String COMMON_GAME_LIST_BUTTON_NEXT = COMMON_GAME_LIST_BUTTON+NEXT;
		public static final String COMMON_GAME_LIST_BUTTON_PREVIOUS = COMMON_GAME_LIST_BUTTON+PREVIOUS;
		public static final String COMMON_GAME_LIST_BUTTON_REMOVE = COMMON_GAME_LIST_BUTTON+REMOVE;
		/* GAME LIST HEADER */
		public static final String COMMON_GAME_LIST_HEADER = COMMON_GAME_LIST+HEADER;
		public static final String COMMON_GAME_LIST_HEADER_ALLOWED_PLAYER = COMMON_GAME_LIST_HEADER+ALLOWED+PLAYER;
		public static final String COMMON_GAME_LIST_HEADER_AVERAGE_LEVEL = COMMON_GAME_LIST_HEADER+AVERAGE+LEVEL;
		public static final String COMMON_GAME_LIST_HEADER_BUTTON = COMMON_GAME_LIST_HEADER+BUTTON;
		public static final String COMMON_GAME_LIST_HEADER_HOST_IP = COMMON_GAME_LIST_HEADER+HOST+IP;
		public static final String COMMON_GAME_LIST_HEADER_HOST_NAME = COMMON_GAME_LIST_HEADER+HOST+NAME;
		public static final String COMMON_GAME_LIST_HEADER_PLAYED = COMMON_GAME_LIST_HEADER+PLAYED;
		public static final String COMMON_GAME_LIST_HEADER_PLAYER_COUNT = COMMON_GAME_LIST_HEADER+PLAYER+COUNT;
		public static final String COMMON_GAME_LIST_HEADER_PREFERRED = COMMON_GAME_LIST_HEADER+PREFERRED;
		public static final String COMMON_GAME_LIST_HEADER_TOURNAMENT_STATE = COMMON_GAME_LIST_HEADER+TOURNAMENT+STATE;
		public static final String COMMON_GAME_LIST_HEADER_TOURNAMENT_TYPE = COMMON_GAME_LIST_HEADER+TOURNAMENT+TYPE;
		/* GAME LIST DATA */
		public static final String COMMON_GAME_LIST_DATA = COMMON_GAME_LIST+DATA;
		public static final String COMMON_GAME_LIST_DATA_FAV_PREFERRED = COMMON_GAME_LIST_DATA+FAV+PREFERRED;
		public static final String COMMON_GAME_LIST_DATA_FAV_REGULAR = COMMON_GAME_LIST_DATA+FAV+REGULAR;
		public static final String COMMON_GAME_LIST_DATA_STATE_CLOSED = COMMON_GAME_LIST_DATA+STATE+CLOSED;
		public static final String COMMON_GAME_LIST_DATA_STATE_FINISHED = COMMON_GAME_LIST_DATA+STATE+FINISHED;
		public static final String COMMON_GAME_LIST_DATA_STATE_OPEN = COMMON_GAME_LIST_DATA+STATE+OPEN;
		public static final String COMMON_GAME_LIST_DATA_STATE_PLAYING = COMMON_GAME_LIST_DATA+STATE+PLAYING;
		public static final String COMMON_GAME_LIST_DATA_STATE_UNKNOWN = COMMON_GAME_LIST_DATA+STATE+UNKNOWN;
		public static final String COMMON_GAME_LIST_DATA_STATE_RETRIEVING = COMMON_GAME_LIST_DATA+STATE+RETRIEVING;
		public static final String COMMON_GAME_LIST_DATA_TYPE_CUP = COMMON_GAME_LIST_DATA+TYPE+CUP;
		public static final String COMMON_GAME_LIST_DATA_TYPE_LEAGUE = COMMON_GAME_LIST_DATA+TYPE+LEAGUE;
		public static final String COMMON_GAME_LIST_DATA_TYPE_SEQUENCE = COMMON_GAME_LIST_DATA+TYPE+SEQUENCE;
		public static final String COMMON_GAME_LIST_DATA_TYPE_SINGLE = COMMON_GAME_LIST_DATA+TYPE+SINGLE;
		public static final String COMMON_GAME_LIST_DATA_TYPE_TURNING = COMMON_GAME_LIST_DATA+TYPE+TURNING;

	/* HOST INFO */
	public static final String COMMON_HOST_INFO = COMMON+HOST+INFO;
	public static final String COMMON_HOST_INFO_IP = COMMON_HOST_INFO+IP;
	public static final String COMMON_HOST_INFO_NAME = COMMON_HOST_INFO+NAME;
	public static final String COMMON_HOST_INFO_PLAYED = COMMON_HOST_INFO+PLAYED;
		/* HOST INFO PREFERRED*/
		public static final String COMMON_HOST_INFO_PREFERRED = COMMON_HOST_INFO+PREFERRED;
			/* HOST INFO PREFERRED DATA */
			public static final String COMMON_HOST_INFO_PREFERRED_DATA = COMMON_HOST_INFO_PREFERRED+DATA;
			public static final String COMMON_HOST_INFO_PREFERRED_DATA_PREFERRED = COMMON_HOST_INFO_PREFERRED_DATA+PREFERRED;
			public static final String COMMON_HOST_INFO_PREFERRED_DATA_NON_PREFERRED = COMMON_HOST_INFO_PREFERRED_DATA+NON+PREFERRED;
		/* HOST INFO TYPE*/
		public static final String COMMON_HOST_INFO_TYPE = COMMON_HOST_INFO+TYPE;
			/* HOST INFO TYPE DATA */
			public static final String COMMON_HOST_INFO_TYPE_DATA = COMMON_HOST_INFO_TYPE+DATA;
			public static final String COMMON_HOST_INFO_TYPE_DATA_BOTH = COMMON_HOST_INFO_TYPE_DATA+BOTH;
			public static final String COMMON_HOST_INFO_TYPE_DATA_CENTRAL = COMMON_HOST_INFO_TYPE_DATA+CENTRAL;
			public static final String COMMON_HOST_INFO_TYPE_DATA_DIRECT = COMMON_HOST_INFO_TYPE_DATA+DIRECT;

	/* ITEMS */
	public static final String COMMON_ITEMS = COMMON+ITEMS;
		/* AVAILABLE */
		public static final String COMMON_ITEMS_AVAILABLE = COMMON_ITEMS+AVAILABLE;
		public static final String COMMON_ITEMS_AVAILABLE_TITLE = COMMON_ITEMS_AVAILABLE+TITLE;
		/* INITIAL */
		public static final String COMMON_ITEMS_INITIAL = COMMON_ITEMS+INITIAL;
		public static final String COMMON_ITEMS_INITIAL_TITLE = COMMON_ITEMS_INITIAL+TITLE;

	/* LEG */
	public static final String COMMON_LEG = COMMON+LEG;
	public static final String COMMON_LEG_DOWN = COMMON_LEG+DOWN;
	public static final String COMMON_LEG_LEFT = COMMON_LEG+LEFT;
	public static final String COMMON_LEG_RIGHT = COMMON_LEG+RIGHT;
	public static final String COMMON_LEG_UP = COMMON_LEG+UP;

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
			public static final String COMMON_LIMIT_MATCH_HEADER_SELF_BOMBINGS = COMMON_LIMIT_MATCH_HEADER+SELF+BOMBINGS;
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
			public static final String COMMON_LIMIT_ROUND_HEADER_SELF_BOMBINGS = COMMON_LIMIT_ROUND_HEADER+SELF+BOMBINGS;
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
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_SELF_BOMBINGS = COMMON_LIMIT_TOURNAMENT_HEADER+SELF+BOMBINGS;
			public static final String COMMON_LIMIT_TOURNAMENT_HEADER_TIME = COMMON_LIMIT_TOURNAMENT_HEADER+TIME;

	/* MATCH */
	public static final String COMMON_MATCH = COMMON+MATCH;
	public static final String COMMON_MATCH_AUTHOR = COMMON_MATCH+AUTHOR;
	public static final String COMMON_MATCH_NAME = COMMON_MATCH+NAME;
	public static final String COMMON_MATCH_ALLOWED_PLAYERS = COMMON_MATCH+ALLOWED+PLAYERS;
	public static final String COMMON_MATCH_ROUND_COUNT = COMMON_MATCH+ROUND+COUNT;
			
	/* PART */
	public static final String COMMON_PART = COMMON+PART;
	public static final String COMMON_PART_AFTER = COMMON_PART+AFTER;
	public static final String COMMON_PART_BEFORE = COMMON_PART+BEFORE;
	public static final String COMMON_PART_PLAYER = COMMON_PART+PLAYER;
	public static final String COMMON_PART_RANK = COMMON_PART+RANK;
	public static final String COMMON_PART_UNDECIDED = COMMON_PART+UNDECIDED;

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
			public static final String COMMON_PLAYERS_LIST_DATA_REMOTE = COMMON_PLAYERS_LIST_DATA+REMOTE;
			public static final String COMMON_PLAYERS_LIST_DATA_NO_CONTROLS = COMMON_PLAYERS_LIST_DATA+NO+CONTROLS;
		/* SELECTION */
		public static final String COMMON_PLAYERS_SELECTION = COMMON_PLAYERS+SELECTION;
			/* HEADER */
			public static final String COMMON_PLAYERS_SELECTION_HEADER = COMMON_PLAYERS_SELECTION+HEADER;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_COLOR = COMMON_PLAYERS_SELECTION_HEADER+COLOR;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_CONTROLS = COMMON_PLAYERS_SELECTION_HEADER+CONTROLS;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_DELETE = COMMON_PLAYERS_SELECTION_HEADER+DELETE;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_HERO = COMMON_PLAYERS_SELECTION_HEADER+HERO;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_PROFILE = COMMON_PLAYERS_SELECTION_HEADER+PROFILE;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_RANK = COMMON_PLAYERS_SELECTION_HEADER+RANK;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_READY = COMMON_PLAYERS_SELECTION_HEADER+READY;
			public static final String COMMON_PLAYERS_SELECTION_HEADER_TYPE = COMMON_PLAYERS_SELECTION_HEADER+TYPE;
			/* DATA */
			public static final String COMMON_PLAYERS_SELECTION_DATA = COMMON_PLAYERS_SELECTION+DATA;
			public static final String COMMON_PLAYERS_SELECTION_DATA_ADD = COMMON_PLAYERS_SELECTION_DATA+ADD;
			public static final String COMMON_PLAYERS_SELECTION_DATA_COMPUTER = COMMON_PLAYERS_SELECTION_DATA+COMPUTER;
			public static final String COMMON_PLAYERS_SELECTION_DATA_CONFIRMED = COMMON_PLAYERS_SELECTION_DATA+CONFIRMED;
			public static final String COMMON_PLAYERS_SELECTION_DATA_DELETE = COMMON_PLAYERS_SELECTION_DATA+DELETE;
			public static final String COMMON_PLAYERS_SELECTION_DATA_HUMAN = COMMON_PLAYERS_SELECTION_DATA+HUMAN;
			public static final String COMMON_PLAYERS_SELECTION_DATA_REMOTE = COMMON_PLAYERS_SELECTION_DATA+REMOTE;
			public static final String COMMON_PLAYERS_SELECTION_DATA_UNCONFIRMED = COMMON_PLAYERS_SELECTION_DATA+UNCONFIRMED;

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
			public static final String COMMON_POINTS_MATCH_DATA_NOSHARE = COMMON_POINTS_MATCH_DATA+NO+SHARE;
			public static final String COMMON_POINTS_MATCH_DATA_PAINTINGS = COMMON_POINTS_MATCH_DATA+PAINTINGS;
			public static final String COMMON_POINTS_MATCH_DATA_PARTIAL = COMMON_POINTS_MATCH_DATA+PARTIAL;
			public static final String COMMON_POINTS_MATCH_DATA_REGULAR = COMMON_POINTS_MATCH_DATA+REGULAR;
			public static final String COMMON_POINTS_MATCH_DATA_SELF_BOMBINGS = COMMON_POINTS_MATCH_DATA+SELF+BOMBINGS;
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
			public static final String COMMON_POINTS_ROUND_DATA_NOSHARE = COMMON_POINTS_ROUND_DATA+NO+SHARE;
			public static final String COMMON_POINTS_ROUND_DATA_PAINTINGS = COMMON_POINTS_ROUND_DATA+PAINTINGS;
			public static final String COMMON_POINTS_ROUND_DATA_PARTIAL = COMMON_POINTS_ROUND_DATA+PARTIAL;
			public static final String COMMON_POINTS_ROUND_DATA_REGULAR = COMMON_POINTS_ROUND_DATA+REGULAR;
			public static final String COMMON_POINTS_ROUND_DATA_SELF_BOMBINGS = COMMON_POINTS_ROUND_DATA+SELF+BOMBINGS;
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
			public static final String COMMON_POINTS_TOURNAMENT_DATA_NOSHARE = COMMON_POINTS_TOURNAMENT_DATA+NO+SHARE;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_PAINTINGS = COMMON_POINTS_TOURNAMENT_DATA+PAINTINGS;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_PARTIAL = COMMON_POINTS_TOURNAMENT_DATA+PARTIAL;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_REGULAR = COMMON_POINTS_TOURNAMENT_DATA+REGULAR;
			public static final String COMMON_POINTS_TOURNAMENT_DATA_SELF_BOMBINGS = COMMON_POINTS_TOURNAMENT_DATA+SELF+BOMBINGS;
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
	public static final String COMMON_PROFILES_RANK = COMMON_PROFILES+RANK;
			
	/* REPLAY */
	public static final String COMMON_REPLAY = COMMON+REPLAY;
	public static final String COMMON_REPLAY_NAME = COMMON_REPLAY+NAME;
	public static final String COMMON_REPLAY_PACK = COMMON_REPLAY+PACK;			
	public static final String COMMON_REPLAY_PLAYERS = COMMON_REPLAY+PLAYERS;
	public static final String COMMON_REPLAY_SAVE = COMMON_REPLAY+SAVE;

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
			public static final String COMMON_RESULTS_MATCH_HEADER_RANK = COMMON_RESULTS_MATCH_HEADER+RANK;
			public static final String COMMON_RESULTS_MATCH_HEADER_SELF_BOMBINGS = COMMON_RESULTS_MATCH_HEADER+SELF+BOMBINGS;
			public static final String COMMON_RESULTS_MATCH_HEADER_TIME = COMMON_RESULTS_MATCH_HEADER+TIME;
			public static final String COMMON_RESULTS_MATCH_HEADER_TOTAL = COMMON_RESULTS_MATCH_HEADER+TOTAL;
			/* DATA */
			public static final String COMMON_RESULTS_MATCH_DATA = COMMON_RESULTS_MATCH+DATA;
			public static final String COMMON_RESULTS_MATCH_DATA_COMPUTER = COMMON_RESULTS_MATCH_DATA+COMPUTER;
			public static final String COMMON_RESULTS_MATCH_DATA_HUMAN = COMMON_RESULTS_MATCH_DATA+HUMAN;
			public static final String COMMON_RESULTS_MATCH_DATA_REMOTE = COMMON_RESULTS_MATCH_DATA+REMOTE;
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
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_RANK = COMMON_RESULTS_TOURNAMENT_HEADER+RANK;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_SELF_BOMBINGS = COMMON_RESULTS_TOURNAMENT_HEADER+SELF+BOMBINGS;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_TIME = COMMON_RESULTS_TOURNAMENT_HEADER+TIME;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_TOTAL = COMMON_RESULTS_TOURNAMENT_HEADER+TOTAL;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_PLAYED = COMMON_RESULTS_TOURNAMENT_HEADER+PLAYED;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_WON = COMMON_RESULTS_TOURNAMENT_HEADER+WON;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_DRAWN = COMMON_RESULTS_TOURNAMENT_HEADER+DRAWN;
			public static final String COMMON_RESULTS_TOURNAMENT_HEADER_LOST = COMMON_RESULTS_TOURNAMENT_HEADER+LOST;
			/* DATA */
			public static final String COMMON_RESULTS_TOURNAMENT_DATA = COMMON_RESULTS_TOURNAMENT+DATA;
			public static final String COMMON_RESULTS_TOURNAMENT_DATA_COMPUTER = COMMON_RESULTS_TOURNAMENT_DATA+COMPUTER;
			public static final String COMMON_RESULTS_TOURNAMENT_DATA_HUMAN = COMMON_RESULTS_TOURNAMENT_DATA+HUMAN;
			public static final String COMMON_RESULTS_TOURNAMENT_DATA_REMOTE = COMMON_RESULTS_TOURNAMENT_DATA+REMOTE;
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
			public static final String COMMON_RESULTS_ROUND_HEADER_RANK = COMMON_RESULTS_ROUND_HEADER+RANK;
			public static final String COMMON_RESULTS_ROUND_HEADER_SELF_BOMBINGS = COMMON_RESULTS_ROUND_HEADER+SELF+BOMBINGS;
			public static final String COMMON_RESULTS_ROUND_HEADER_TIME = COMMON_RESULTS_ROUND_HEADER+TIME;
			/* DATA */
			public static final String COMMON_RESULTS_ROUND_DATA = COMMON_RESULTS_ROUND+DATA;
			public static final String COMMON_RESULTS_ROUND_DATA_COMPUTER = COMMON_RESULTS_ROUND_DATA+COMPUTER;
			public static final String COMMON_RESULTS_ROUND_DATA_HUMAN = COMMON_RESULTS_MATCH_DATA+HUMAN;
			public static final String COMMON_RESULTS_ROUND_DATA_REMOTE = COMMON_RESULTS_MATCH_DATA+REMOTE;
	
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
	
	/* STATISTICS */	
	public static final String COMMON_STATISTICS = COMMON+STATISTICS;
		/* PLAYER */
		public static final String COMMON_STATISTICS_PLAYER = COMMON_STATISTICS+PLAYER;
			/* COMMON */
			public static final String COMMON_STATISTICS_PLAYER_COMMON = COMMON_STATISTICS_PLAYER+COMMON;
				/* HEADER */
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER = COMMON_STATISTICS_PLAYER_COMMON+HEADER;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER_NAME = COMMON_STATISTICS_PLAYER_COMMON_HEADER+NAME;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER_PORTRAIT = COMMON_STATISTICS_PLAYER_COMMON_HEADER+PORTRAIT;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER_RANK = COMMON_STATISTICS_PLAYER_COMMON_HEADER+RANK;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER_EVOLUTION= COMMON_STATISTICS_PLAYER_COMMON_HEADER+EVOLUTION;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_HEADER_TYPE = COMMON_STATISTICS_PLAYER_COMMON_HEADER+TYPE;
				/* DATA */
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA = COMMON_STATISTICS_PLAYER_COMMON+DATA;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_COMPUTER = COMMON_STATISTICS_PLAYER_COMMON_DATA+COMPUTER;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_HUMAN = COMMON_STATISTICS_PLAYER_COMMON_DATA+HUMAN;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_REMOTE = COMMON_STATISTICS_PLAYER_COMMON_DATA+REMOTE;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_NO_RANK = COMMON_STATISTICS_PLAYER_COMMON_DATA+NO+RANK;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_MEAN = COMMON_STATISTICS_PLAYER_COMMON_DATA+MEAN;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_TOTAL = COMMON_STATISTICS_PLAYER_COMMON_DATA+TOTAL;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_ENTER = COMMON_STATISTICS_PLAYER_COMMON_DATA+ENTER;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_EXIT = COMMON_STATISTICS_PLAYER_COMMON_DATA+EXIT;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_SAME = COMMON_STATISTICS_PLAYER_COMMON_DATA+SAME;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_UP = COMMON_STATISTICS_PLAYER_COMMON_DATA+UP;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_DATA_DOWN = COMMON_STATISTICS_PLAYER_COMMON_DATA+DOWN;
				/* BUTTON */
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON = COMMON_STATISTICS_PLAYER_COMMON+BUTTON;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_REGISTER = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+REGISTER;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_UNREGISTER = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+UNREGISTER;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NEXT = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+NEXT;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_PREVIOUS = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+PREVIOUS;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_SUM = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+SUM;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_MEAN = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+MEAN;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_AI = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+AI;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_HUMAN = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+HUMAN;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_BOTH = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+BOTH;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALLRANKS = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+ALL+RANKS;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_NORANKS = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+NO+RANKS;
				public static final String COMMON_STATISTICS_PLAYER_COMMON_BUTTON_ALL = COMMON_STATISTICS_PLAYER_COMMON_BUTTON+ALL;
			/* GLICKO2 */
			public static final String COMMON_STATISTICS_PLAYER_GLICKO2 = COMMON_STATISTICS_PLAYER+GLICKO2;
			public static final String COMMON_STATISTICS_PLAYER_GLICKO2_TITLE = COMMON_STATISTICS_PLAYER_GLICKO2+TITLE;
				/* HEADER */
				public static final String COMMON_STATISTICS_PLAYER_GLICKO2_HEADER = COMMON_STATISTICS_PLAYER_GLICKO2+HEADER;
				public static final String COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_MEAN = COMMON_STATISTICS_PLAYER_GLICKO2_HEADER+MEAN;
				public static final String COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_ROUND_COUNT = COMMON_STATISTICS_PLAYER_GLICKO2_HEADER+ROUND+COUNT;
				public static final String COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_STANDARD_DEVIATION = COMMON_STATISTICS_PLAYER_GLICKO2_HEADER+STANDARD+DEVIATION;
				public static final String COMMON_STATISTICS_PLAYER_GLICKO2_HEADER_VOLATILITY = COMMON_STATISTICS_PLAYER_GLICKO2_HEADER+VOLATILITY;
			/* SCORES */
			public static final String COMMON_STATISTICS_PLAYER_SCORES = COMMON_STATISTICS_PLAYER+SCORES;
			public static final String COMMON_STATISTICS_PLAYER_SCORES_TITLE = COMMON_STATISTICS_PLAYER_SCORES+TITLE;
				/* HEADER */
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER = COMMON_STATISTICS_PLAYER_SCORES+HEADER;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBINGS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+BOMBINGS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBEDS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+BOMBEDS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_BOMBS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+BOMBS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_CROWNS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+CROWNS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_ITEMS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+ITEMS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_PAINTINGS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+PAINTINGS;
				public static final String COMMON_STATISTICS_PLAYER_SCORES_HEADER_SELF_BOMBINGS = COMMON_STATISTICS_PLAYER_SCORES_HEADER+SELF+BOMBINGS;
			/* CONFRONTATIONS */
			public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS = COMMON_STATISTICS_PLAYER+CONFRONTATIONS;
			public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_TITLE = COMMON_STATISTICS_PLAYER_CONFRONTATIONS+TITLE;
				/* HEADER */
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER = COMMON_STATISTICS_PLAYER_CONFRONTATIONS+HEADER;
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_DRAWN = COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER+ROUNDS+DRAWN;
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_LOST = COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER+ROUNDS+LOST;
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_WON = COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER+ROUNDS+WON;
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_ROUNDS_PLAYED = COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER+ROUNDS+PLAYED;
				public static final String COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER_TIME_PLAYED = COMMON_STATISTICS_PLAYER_CONFRONTATIONS_HEADER+TIME+PLAYED;

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
	public static List<String> getKeysLike(String keyStart)
	{	List<String> result = new ArrayList<String>();
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
