package fr.free.totalboumboum.gui.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;

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

public class GuiKeys
{
	public static final String TOOLTIP = "Tooltip";

	/////////////////////////////////////////////////////////////////
	// MENUS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* MAIN */ 
	public static final String MENU_MAIN_BUTTON = "MenuMainButton";
	public static final String MENU_MAIN_BUTTON_ABOUT = MENU_MAIN_BUTTON+"About";
	public static final String MENU_MAIN_BUTTON_AI = MENU_MAIN_BUTTON+"Ai";
	public static final String MENU_MAIN_BUTTON_HEROES = MENU_MAIN_BUTTON+"Heroes";
	public static final String MENU_MAIN_BUTTON_LEVELS = MENU_MAIN_BUTTON+"Levels";
	public static final String MENU_MAIN_BUTTON_OPTIONS = MENU_MAIN_BUTTON+"Options";
	public static final String MENU_MAIN_BUTTON_PROFILES = MENU_MAIN_BUTTON+"Profiles";
	public static final String MENU_MAIN_BUTTON_QUICKMATCH = MENU_MAIN_BUTTON+"QuickMatch";
	public static final String MENU_MAIN_BUTTON_STATISTICS = MENU_MAIN_BUTTON+"Statistics";
	public static final String MENU_MAIN_BUTTON_TOURNAMENT = MENU_MAIN_BUTTON+"Tournament";
	
	/* AI */
		/* BUTTON */
		public static final String MENU_AI_BUTTON = "MenuAiButton";	
		public static final String MENU_AI_BUTTON_BACK = MENU_AI_BUTTON+"Back";	
		/* SELECT */
		public static final String MENU_AI_SELECT_TITLE = "MenuAiSelectTitle";
			/* CLASS */
			public static final String MENU_AI_SELECT_CLASS_PAGEDOWN = "MenuAiSelectClassPageDown";
			public static final String MENU_AI_SELECT_CLASS_PAGEUP = "MenuAiSelectClassPageUp";
			public static final String MENU_AI_SELECT_CLASS_PARENT = "MenuAiSelectClassParent";
			/* PACKAGES */
			public static final String MENU_AI_SELECT_PACKAGE_PAGEDOWN = "MenuAiSelectPackagePageDown";
			public static final String MENU_AI_SELECT_PACKAGE_PAGEUP = "MenuAiSelectPackagePageUp";
			/* PREVIEW */
			public static final String MENU_AI_SELECT_PREVIEW_AUTHOR = "MenuAiSelectPreviewAuthor";
			public static final String MENU_AI_SELECT_PREVIEW_NAME = "MenuAiSelectPreviewName";
			public static final String MENU_AI_SELECT_PREVIEW_NOTES = "MenuAiSelectPreviewNotes";
			public static final String MENU_AI_SELECT_PREVIEW_PACKAGE = "MenuAiSelectPreviewPackage";
			
	/* HERO */
		/* BUTTON */
		public static final String MENU_HERO_BUTTON = "MenuHeroButton";	
		public static final String MENU_HERO_BUTTON_BACK = MENU_HERO_BUTTON+"Back";	
		/* SELECT */
		public static final String MENU_HERO_SELECT_TITLE = "MenuHeroSelectTitle";
			/* FOLDER */
			public static final String MENU_HERO_SELECT_FOLDER_PAGEDOWN = "MenuHeroSelectFolderPageDown";
			public static final String MENU_HERO_SELECT_FOLDER_PAGEUP = "MenuHeroSelectFolderPageUp";
			public static final String MENU_HERO_SELECT_FOLDER_PARENT = "MenuHeroSelectFolderParent";
			/* PACKAGES */
			public static final String MENU_HERO_SELECT_PACKAGE_PAGEDOWN = "MenuHeroSelectPackagePageDown";
			public static final String MENU_HERO_SELECT_PACKAGE_PAGEUP = "MenuHeroSelectPackagePageUp";
			/* PREVIEW */
			public static final String MENU_HERO_SELECT_PREVIEW_AUTHOR = "MenuHeroSelectPreviewAuthor";
			public static final String MENU_HERO_SELECT_PREVIEW_COLORS = "MenuHeroSelectPreviewColors";
			public static final String MENU_HERO_SELECT_PREVIEW_IMAGE = "MenuHeroSelectPreviewImage";
			public static final String MENU_HERO_SELECT_PREVIEW_NAME = "MenuHeroSelectPreviewName";
			public static final String MENU_HERO_SELECT_PREVIEW_PACKAGE = "MenuHeroSelectPreviewPackage";
			public static final String MENU_HERO_SELECT_PREVIEW_SOURCE = "MenuHeroSelectPreviewSource";
			
	/* LEVEL */
		/* BUTTON */
		public static final String MENU_LEVEL_BUTTON = "MenuLevelButton";	
		public static final String MENU_LEVEL_BUTTON_BACK = MENU_LEVEL_BUTTON+"Back";	
		/* SELECT */
		public static final String MENU_LEVEL_SELECT_TITLE = "MenuLevelSelectTitle";
			/* FOLDER */
			public static final String MENU_LEVEL_SELECT_FOLDER_PAGEDOWN = "MenuLevelSelectFolderPageDown";
			public static final String MENU_LEVEL_SELECT_FOLDER_PAGEUP = "MenuLevelSelectFolderPageUp";
			public static final String MENU_LEVEL_SELECT_FOLDER_PARENT = "MenuLevelSelectFolderParent";
			/* PACKAGES */
			public static final String MENU_LEVEL_SELECT_PACKAGE_PAGEDOWN = "MenuLevelSelectPackagePageDown";
			public static final String MENU_LEVEL_SELECT_PACKAGE_PAGEUP = "MenuLevelSelectPackagePageUp";
			/* PREVIEW */
			public static final String MENU_LEVEL_SELECT_PREVIEW_AUTHOR = "MenuLevelSelectPreviewAuthor";
			public static final String MENU_LEVEL_SELECT_PREVIEW_IMAGE = "MenuLevelSelectPreviewImage";
			public static final String MENU_LEVEL_SELECT_PREVIEW_INSTANCE = "MenuLevelSelectPreviewInstance";
			public static final String MENU_LEVEL_SELECT_PREVIEW_NAME = "MenuLevelSelectPreviewName";
			public static final String MENU_LEVEL_SELECT_PREVIEW_PACKAGE = "MenuLevelSelectPreviewPackage";
			public static final String MENU_LEVEL_SELECT_PREVIEW_SIZE = "MenuLevelSelectPreviewSize";
			public static final String MENU_LEVEL_SELECT_PREVIEW_SOURCE = "MenuLevelSelectPreviewSource";
			public static final String MENU_LEVEL_SELECT_PREVIEW_THEME = "MenuLevelSelectPreviewTheme";
			
	/* OPTIONS */
		/* BUTTON */
		public static final String MENU_OPTIONS_BUTTON = "MenuOptionsButton";
		public static final String MENU_OPTIONS_BUTTON_ADVANCED = MENU_OPTIONS_BUTTON+"Advanced";
		public static final String MENU_OPTIONS_BUTTON_BACK = MENU_OPTIONS_BUTTON+"Back";
		public static final String MENU_OPTIONS_BUTTON_CANCEL = MENU_OPTIONS_BUTTON+"Cancel";
		public static final String MENU_OPTIONS_BUTTON_CONFIRM = MENU_OPTIONS_BUTTON+"Confirm";
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = MENU_OPTIONS_BUTTON+"Controls";
		public static final String MENU_OPTIONS_BUTTON_GAMEPLAY = MENU_OPTIONS_BUTTON+"Gameplay";
		public static final String MENU_OPTIONS_BUTTON_GUI = MENU_OPTIONS_BUTTON+"Gui";
		public static final String MENU_OPTIONS_BUTTON_NEXT = MENU_OPTIONS_BUTTON+"Next";
		public static final String MENU_OPTIONS_BUTTON_PREVIOUS = MENU_OPTIONS_BUTTON+"Previous";
		public static final String MENU_OPTIONS_BUTTON_VIDEO = MENU_OPTIONS_BUTTON+"Video";
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
		/* GAMEPLAY */
		public static final String MENU_OPTIONS_GAMEPLAY_TITLE = "MenuOptionsGameplayTitle";
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
		/* LIST */
		public static final String MENU_PROFILES_LIST_TITLE = "MenuProfilesListTitle";
		public static final String MENU_PROFILES_LIST_PAGEDOWN = "MenuProfilesListPageDown";
		public static final String MENU_PROFILES_LIST_PAGEUP = "MenuProfilesListPageUp";
		public static final String MENU_PROFILES_LIST_NEW_PROFILE = "MenuProfilesListNewProfile";
		/* PREVIEW */
		public static final String MENU_PROFILES_PREVIEW_AINAME = "MenuProfilesPreviewAiName";
		public static final String MENU_PROFILES_PREVIEW_AIPACK = "MenuProfilesPreviewAiPack";
		public static final String MENU_PROFILES_PREVIEW_COLOR = "MenuProfilesPreviewColor";
		public static final String MENU_PROFILES_PREVIEW_HERONAME = "MenuProfilesPreviewHeroName";
		public static final String MENU_PROFILES_PREVIEW_HEROPACK = "MenuProfilesPreviewHeroPack";
		public static final String MENU_PROFILES_PREVIEW_NAME = "MenuProfilesPreviewName";
	/* QUICKMATCH */	
		public static final String MENU_QUICKMATCH = "MenuQuickmatch";
		/* BUTTON */
			public static final String MENU_QUICKMATCH_BUTTON = MENU_QUICKMATCH+"Button";
			public static final String MENU_QUICKMATCH_BUTTON_DESCRIPTION = MENU_QUICKMATCH_BUTTON+"Description";
			public static final String MENU_QUICKMATCH_BUTTON_NEXT = MENU_QUICKMATCH_BUTTON+"Next";
			public static final String MENU_QUICKMATCH_BUTTON_PREVIOUS = MENU_QUICKMATCH_BUTTON+"Previous";
			public static final String MENU_QUICKMATCH_BUTTON_QUIT = MENU_QUICKMATCH_BUTTON+"Quit";
			public static final String MENU_QUICKMATCH_BUTTON_RESULTS = MENU_QUICKMATCH_BUTTON+"Results";
			public static final String MENU_QUICKMATCH_BUTTON_STATISTICS = MENU_QUICKMATCH_BUTTON+"Statistics";
		/* PROFILES */
			public static final String MENU_QUICKMATCH_PROFILES = MENU_QUICKMATCH+"Profiles";
			public static final String MENU_QUICKMATCH_TITLE = MENU_QUICKMATCH_PROFILES+"Title";
			/* CONTROLS */
			public static final String MENU_QUICKMATCH_PROFILES_CONTROLS_NEXT = MENU_QUICKMATCH_PROFILES+"ControlsNext";
			public static final String MENU_QUICKMATCH_PROFILES_CONTROLS_PREVIOUS = MENU_QUICKMATCH_PROFILES+"ControlsPrevious";
			public static final String MENU_QUICKMATCH_PROFILES_CONTROLS_VALUE = MENU_QUICKMATCH_PROFILES+"ControlsValue";
			/* COLOR */
			public static final String MENU_QUICKMATCH_PROFILES_COLOR_NEXT = MENU_QUICKMATCH_PROFILES+"ColorNext";
			public static final String MENU_QUICKMATCH_PROFILES_COLOR_PREVIOUS = MENU_QUICKMATCH_PROFILES+"ColorPrevious";
			public static final String MENU_QUICKMATCH_PROFILES_COLOR_VALUE = MENU_QUICKMATCH_PROFILES+"ColorValue";
			/* HERO */
			public static final String MENU_QUICKMATCH_PROFILES_HERO_BROWSE = MENU_QUICKMATCH_PROFILES+"HeroBrowse";
			public static final String MENU_QUICKMATCH_PROFILES_HERO_NEXT = MENU_QUICKMATCH_PROFILES+"HeroNext";
			public static final String MENU_QUICKMATCH_PROFILES_HERO_PREVIOUS = MENU_QUICKMATCH_PROFILES+"HeroPrevious";
			public static final String MENU_QUICKMATCH_PROFILES_HERO_VALUE = MENU_QUICKMATCH_PROFILES+"HeroValue";
			/* PROFILE */
			public static final String MENU_QUICKMATCH_PROFILES_PROFILE_BROWSE = MENU_QUICKMATCH_PROFILES+"ProfileBrowse";
			public static final String MENU_QUICKMATCH_PROFILES_PROFILE_NEXT = MENU_QUICKMATCH_PROFILES+"ProfileNext";
			public static final String MENU_QUICKMATCH_PROFILES_PROFILE_PREVIOUS = MENU_QUICKMATCH_PROFILES+"ProfilePrevious";
			public static final String MENU_QUICKMATCH_PROFILES_PROFILE_VALUE = MENU_QUICKMATCH_PROFILES+"ProfileValue";
			/* TYPE */
			public static final String MENU_QUICKMATCH_PROFILES_TYPE_VALUE = MENU_QUICKMATCH_PROFILES+"TypeValue";
		
	/* TOURNAMENT */	
	public static final String MENU_TOURNAMENT_BUTTON = "MenuTournamentButton";
	public static final String MENU_TOURNAMENT_BUTTON_BACK = MENU_TOURNAMENT_BUTTON+"Back";
	public static final String MENU_TOURNAMENT_BUTTON_CONTINUE = MENU_TOURNAMENT_BUTTON+"Continue";
	public static final String MENU_TOURNAMENT_BUTTON_LOAD = MENU_TOURNAMENT_BUTTON+"Load";
	public static final String MENU_TOURNAMENT_BUTTON_NEW = MENU_TOURNAMENT_BUTTON+"New";
	public static final String MENU_TOURNAMENT_BUTTON_PLAYERS = MENU_TOURNAMENT_BUTTON+"Players";
	public static final String MENU_TOURNAMENT_BUTTON_RULES = MENU_TOURNAMENT_BUTTON+"Rules";
	public static final String MENU_TOURNAMENT_BUTTON_SAVE_AS = MENU_TOURNAMENT_BUTTON+"SaveAs";
	public static final String MENU_TOURNAMENT_BUTTON_START = MENU_TOURNAMENT_BUTTON+"Start";
	
	/* COLORS */
	public static final String COLOR = "Color";
	public static final String COLOR_BLACK = COLOR+"Black";
	public static final String COLOR_BLUE = COLOR+"Blue";
	public static final String COLOR_BROWN = COLOR+"Brown";
	public static final String COLOR_CYAN = COLOR+"Cyan";
	public static final String COLOR_GRASS = COLOR+"Grass";
	public static final String COLOR_GREEN = COLOR+"Green";
	public static final String COLOR_GREY = COLOR+"Grey";
	public static final String COLOR_INDIGO = COLOR+"Indigo";
	public static final String COLOR_ORANGE = COLOR+"Orange";
	public static final String COLOR_PINK = COLOR+"Pink";
	public static final String COLOR_PURPLE = COLOR+"Purple";
	public static final String COLOR_RED = COLOR+"Red";
	public static final String COLOR_RUST = COLOR+"Rust";
	public static final String COLOR_ULTRAMARINE = COLOR+"Ultramarine";
	public static final String COLOR_WHITE = COLOR+"White";
	public static final String COLOR_YELLOW = COLOR+"Yellow";
	
	/////////////////////////////////////////////////////////////////
	// GAME TOURNAMENT	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* BUTTONS */	
	public static final String GAME_TOURNAMENT_BUTTON_QUIT = "GameTournamentButtonQuit";
	public static final String GAME_TOURNAMENT_BUTTON_CURRENT_MATCH = "GameTournamentButtonCurrentMatch";
	public static final String GAME_TOURNAMENT_BUTTON_DESCRIPTION = "GameTournamentButtonDescription";
	public static final String GAME_TOURNAMENT_BUTTON_FINISH = "GameTournamentButtonFinish";
	public static final String GAME_TOURNAMENT_BUTTON_MENU = "GameTournamentButtonMenu";
	public static final String GAME_TOURNAMENT_BUTTON_NEXT_MATCH = "GameTournamentButtonNextMatch";
	public static final String GAME_TOURNAMENT_BUTTON_RESULTS = "GameTournamentButtonResults";
	public static final String GAME_TOURNAMENT_BUTTON_STATISTICS = "GameTournamentButtonStatistics";
	/* DESCRIPTION */
	public static final String GAME_TOURNAMENT_DESCRIPTION_TITLE = "GameTournamentDescriptionTitle";
		/* LIMIT */
		public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_TITLE = "GameTournamentDescriptionLimitTitle";
			/* HEADER */
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_BOMBS = "GameTournamentDescriptionLimitHeaderBombs";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS = "GameTournamentDescriptionLimitHeaderConfrontations";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CROWNS = "GameTournamentDescriptionLimitHeaderCrowns";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CUSTOM = "GameTournamentDescriptionLimitHeaderCustom";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_DEATHS = "GameTournamentDescriptionLimitHeaderDeaths";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_ITEMS = "GameTournamentDescriptionLimitHeaderItems";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_KILLS = "GameTournamentDescriptionLimitHeaderKills";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_PAINTINGS = "GameTournamentDescriptionLimitHeaderPaintings";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TIME = "GameTournamentDescriptionLimitHeaderTime";
			public static final String GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TOTAL = "GameTournamentDescriptionLimitHeaderTotal";
		/* MISC */
		public static final String GAME_TOURNAMENT_DESCRIPTION_MISC_TITLE = "GameTournamentDescriptionMiscTitle";
		/* NOTES */
		public static final String GAME_TOURNAMENT_DESCRIPTION_NOTES_TITLE = "GameTournamentDescriptionNotesTitle";
		/* PLAYERS */
			/* HEADER */
			public static final String GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_NAME = "GameTournamentDescriptionPlayersHeaderName";
			public static final String GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_PROFILE = "GameTournamentDescriptionPlayersHeaderProfile";
			public static final String GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_RANK = "GameTournamentDescriptionPlayersHeaderRank";
			/* DATA */
			public static final String GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER = "GameTournamentDescriptionPlayersDataComputer";
			public static final String GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN = "GameTournamentDescriptionPlayersDataHuman";
		/* POINTS */
		public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_TITLE = "GameTournamentDescriptionPointsTitle";
			/* HEADER */
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_CONSTANT = "GameTournamentDescriptionPointsHeaderConstant";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_DISCRETIZE = "GameTournamentDescriptionPointsHeaderDiscretize";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_PARTIAL = "GameTournamentDescriptionPointsHeaderPartial";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKINGS = "GameTournamentDescriptionPointsHeaderRankings";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKPOINTS = "GameTournamentDescriptionPointsHeaderRankpoints";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_SCORE = "GameTournamentDescriptionPointsHeaderScore";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_TOTAL = "GameTournamentDescriptionPointsHeaderTotal";
			/* DATA */
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_BOMBS = "GameTournamentDescriptionPointsDataBombs";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_CROWNS = "GameTournamentDescriptionPointsDataCrowns";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_DEATHS = "GameTournamentDescriptionPointsDataDeaths";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_INVERTED = "GameTournamentDescriptionPointsDataInverted";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_ITEMS = "GameTournamentDescriptionPointsDataItems";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_KILLS = "GameTournamentDescriptionPointsDataKills";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_NOSHARE = "GameTournamentDescriptionPointsDataNoshare";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PAINTINGS = "GameTournamentDescriptionPointsDataPaintings";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PARTIAL = "GameTournamentDescriptionPointsDataPartial";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_REGULAR = "GameTournamentDescriptionPointsDataRegular";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_SHARE = "GameTournamentDescriptionPointsDataShare";
			public static final String GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_TIME = "GameTournamentDescriptionPointsDataTime";
	/* RESULTS */
	public static final String GAME_TOURNAMENT_RESULTS_TITLE = "GameTournamentResultsTitle";
		/* HEADER */
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_BOMBS = "GameTournamentResultsHeaderBombs";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CROWNS = "GameTournamentResultsHeaderCrowns";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_LIMIT = "GameTournamentResultsHeaderCustomLimit";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_POINTS = "GameTournamentResultsHeaderCustomPoints";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_DEATHS = "GameTournamentResultsHeaderDeaths";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_ITEMS = "GameTournamentResultsHeaderItems";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_KILLS = "GameTournamentResultsHeaderKills";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_MATCH = "GameTournamentResultsHeaderMatch";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_NAME = "GameTournamentResultsHeaderName";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_PAINTINGS = "GameTournamentResultsHeaderPaintings";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_PROFILE = "GameTournamentResultsHeaderProfile";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_POINTS = "GameTournamentResultsHeaderPoints";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_TIME = "GameTournamentResultsHeaderTime";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_TOTAL = "GameTournamentResultsHeaderTotal";
		/* DATA */
		public static final String GAME_TOURNAMENT_RESULTS_DATA_COMPUTER = "GameTournamentResultsDataComputer";
		public static final String GAME_TOURNAMENT_RESULTS_DATA_HUMAN = "GameTournamentResultsDataHuman";
	/* STATISTICS */
	public static final String GAME_TOURNAMENT_STATISTICS_TITLE = "GameTournamentStatisticsTitle";
		/* HEADER */
		public static final String GAME_TOURNAMENT_STATISTICS_HEADER_NAME = "GameTournamentStatisticsHeaderName";
	
	/////////////////////////////////////////////////////////////////
	// GAME MATCH		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* BUTTONS */	
	public static final String GAME_MATCH_BUTTON_QUIT = "GameMatchButtonQuit";
	public static final String GAME_MATCH_BUTTON_CURRENT_ROUND = "GameMatchButtonCurrentRound";
	public static final String GAME_MATCH_BUTTON_CURRENT_TOURNAMENT = "GameMatchButtonCurrentTournament";
	public static final String GAME_MATCH_BUTTON_DESCRIPTION = "GameMatchButtonDescription";
	public static final String GAME_MATCH_BUTTON_FINISH = "GameMatchButtonFinish";
	public static final String GAME_MATCH_BUTTON_NEXT_ROUND = "GameMatchButtonNextRound";
	public static final String GAME_MATCH_BUTTON_RESULTS = "GameMatchButtonResults";
	public static final String GAME_MATCH_BUTTON_STATISTICS = "GameMatchButtonStatistics";
	/* DESCRIPTION */
	public static final String GAME_MATCH_DESCRIPTION_TITLE = "GameMatchDescriptionTitle";
		/* LIMIT */
		public static final String GAME_MATCH_DESCRIPTION_LIMIT_TITLE = "GameMatchDescriptionLimitTitle";
			/* HEADER */
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_BOMBS = "GameMatchDescriptionLimitHeaderBombs";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS = "GameMatchDescriptionLimitHeaderConfrontations";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CROWNS = "GameMatchDescriptionLimitHeaderCrowns";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CUSTOM = "GameMatchDescriptionLimitHeaderCustom";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_DEATHS = "GameMatchDescriptionLimitHeaderDeaths";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_ITEMS = "GameMatchDescriptionLimitHeaderItems";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_KILLS = "GameMatchDescriptionLimitHeaderKills";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_PAINTINGS = "GameMatchDescriptionLimitHeaderPaintings";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TIME = "GameMatchDescriptionLimitHeaderTime";
			public static final String GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TOTAL = "GameMatchDescriptionLimitHeaderTotal";
		/* MISC */
		public static final String GAME_MATCH_DESCRIPTION_MISC_TITLE = "GameMatchDescriptionMiscTitle";
		/* NOTES */
		public static final String GAME_MATCH_DESCRIPTION_NOTES_TITLE = "GameMatchDescriptionNotesTitle";
		/* PLAYERS */
			/* HEADER */
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_CONTROLS = "GameMatchDescriptionPlayersHeaderControls";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME = "GameMatchDescriptionPlayersHeaderName";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_PROFILE = "GameMatchDescriptionPlayersHeaderProfile";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK = "GameMatchDescriptionPlayersHeaderRank";
			/* DATA */
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_COMPUTER = "GameMatchDescriptionPlayersDataComputer";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_CONTROLS = "GameMatchDescriptionPlayersDataControls";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_HUMAN = "GameMatchDescriptionPlayersDataHuman";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_NOCONTROLS = "GameMatchDescriptionPlayersDataNocontrols";
		/* POINTS */
		public static final String GAME_MATCH_DESCRIPTION_POINTS_TITLE = "GameMatchDescriptionPointsTitle";
			/* HEADER */
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_CONSTANT = "GameMatchDescriptionPointsHeaderConstant";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_DISCRETIZE = "GameMatchDescriptionPointsHeaderDiscretize";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_PARTIAL = "GameMatchDescriptionPointsHeaderPartial";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKINGS = "GameMatchDescriptionPointsHeaderRankings";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKPOINTS = "GameMatchDescriptionPointsHeaderRankpoints";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_SCORE = "GameMatchDescriptionPointsHeaderScore";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_HEADER_TOTAL = "GameMatchDescriptionPointsHeaderTotal";
			/* DATA */
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_BOMBS = "GameMatchDescriptionPointsDataBombs";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_CROWNS = "GameMatchDescriptionPointsDataCrowns";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_DEATHS = "GameMatchDescriptionPointsDataDeaths";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_INVERTED = "GameMatchDescriptionPointsDataInverted";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_ITEMS = "GameMatchDescriptionPointsDataItems";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_KILLS = "GameMatchDescriptionPointsDataKills";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_NOSHARE = "GameMatchDescriptionPointsDataNoshare";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_PAINTINGS = "GameMatchDescriptionPointsDataPaintings";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_PARTIAL = "GameMatchDescriptionPointsDataPartial";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_REGULAR = "GameMatchDescriptionPointsDataRegular";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_SHARE = "GameMatchDescriptionPointsDataShare";
			public static final String GAME_MATCH_DESCRIPTION_POINTS_DATA_TIME = "GameMatchDescriptionPointsDataTime";
	/* RESULTS */
	public static final String GAME_MATCH_RESULTS_TITLE = "GameMatchResultsTitle";
		/* HEADER */
		public static final String GAME_MATCH_RESULTS_HEADER_BOMBS = "GameMatchResultsHeaderBombs";
		public static final String GAME_MATCH_RESULTS_HEADER_CROWNS = "GameMatchResultsHeaderCrowns";
		public static final String GAME_MATCH_RESULTS_HEADER_CUSTOM_LIMIT = "GameMatchResultsHeaderCustomLimit";
		public static final String GAME_MATCH_RESULTS_HEADER_CUSTOM_POINTS = "GameMatchResultsHeaderCustomPoints";
		public static final String GAME_MATCH_RESULTS_HEADER_DEATHS = "GameMatchResultsHeaderDeaths";
		public static final String GAME_MATCH_RESULTS_HEADER_ITEMS = "GameMatchResultsHeaderItems";
		public static final String GAME_MATCH_RESULTS_HEADER_KILLS = "GameMatchResultsHeaderKills";
		public static final String GAME_MATCH_RESULTS_HEADER_NAME = "GameMatchResultsHeaderName";
		public static final String GAME_MATCH_RESULTS_HEADER_PAINTINGS = "GameMatchResultsHeaderPaintings";
		public static final String GAME_MATCH_RESULTS_HEADER_POINTS = "GameMatchResultsHeaderPoints";
		public static final String GAME_MATCH_RESULTS_HEADER_PROFILE = "GameMatchResultsHeaderProfile";
		public static final String GAME_MATCH_RESULTS_HEADER_ROUND = "GameMatchResultsHeaderRound";
		public static final String GAME_MATCH_RESULTS_HEADER_TIME = "GameMatchResultsHeaderTime";
		public static final String GAME_MATCH_RESULTS_HEADER_TOTAL = "GameMatchResultsHeaderTotal";
		/* DATA */
		public static final String GAME_MATCH_RESULTS_DATA_COMPUTER = "GameMatchResultsDataComputer";
		public static final String GAME_MATCH_RESULTS_DATA_HUMAN = "GameMatchResultsDataHuman";
	/* STATISTICS */
	public static final String GAME_MATCH_STATISTICS_TITLE = "GameMatchStatisticsTitle";
		/* HEADER */
		public static final String GAME_MATCH_STATISTICS_HEADER_NAME = "GameMatchStatisticsHeaderName";
	
	/////////////////////////////////////////////////////////////////
	// GAME ROUND		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* BUTTONS */	
	public static final String GAME_ROUND_BUTTON_QUIT = "GameRoundButtonQuit";
	public static final String GAME_ROUND_BUTTON_CURRENT_MATCH = "GameRoundButtonCurrentMatch";
	public static final String GAME_ROUND_BUTTON_DESCRIPTION = "GameRoundButtonDescription";
	public static final String GAME_ROUND_BUTTON_FINISH = "GameRoundButtonFinish";
	public static final String GAME_ROUND_BUTTON_PLAY = "GameRoundButtonPlay";
	public static final String GAME_ROUND_BUTTON_RESULTS = "GameRoundButtonResults";
	public static final String GAME_ROUND_BUTTON_STATISTICS = "GameRoundButtonStatistics";
	/* PROGRESS BAR */	
	public static final String GAME_ROUND_PROGRESSBAR_BOMBSET = "GameRoundProgressbarBombset";
	public static final String GAME_ROUND_PROGRESSBAR_COMMON = "GameRoundProgressbarCommon";
	public static final String GAME_ROUND_PROGRESSBAR_COMPLETE = "GameRoundProgressbarComplete";
	public static final String GAME_ROUND_PROGRESSBAR_ITEMSET = "GameRoundProgressbarItemset";
	public static final String GAME_ROUND_PROGRESSBAR_PLAYER = "GameRoundProgressbarPlayer";
	public static final String GAME_ROUND_PROGRESSBAR_THEME = "GameRoundProgressbarTheme";
	public static final String GAME_ROUND_PROGRESSBAR_ZONE = "GameRoundProgressbarZone";
	/* DESCRIPTION */
	public static final String GAME_ROUND_DESCRIPTION_TITLE = "GameRoundDescriptionTitle";
		/* INITIAL ITEMS */
		public static final String GAME_ROUND_DESCRIPTION_INITIALITEMS_TITLE = "GameRoundDescriptionInitialItemsTitle";
		/* ITEMSET */
		public static final String GAME_ROUND_DESCRIPTION_ITEMSET_TITLE = "GameRoundDescriptionItemset";
		/* LIMIT */
		public static final String GAME_ROUND_DESCRIPTION_LIMIT_TITLE = "GameRoundDescriptionLimitTitle";
			/* HEADER */
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_BOMBS = "GameRoundDescriptionLimitHeaderBombs";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CROWNS = "GameRoundDescriptionLimitHeaderCrowns";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CUSTOM = "GameRoundDescriptionLimitHeaderCustom";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_DEATHS = "GameRoundDescriptionLimitHeaderDeaths";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_ITEMS = "GameRoundDescriptionLimitHeaderItems";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_KILLS = "GameRoundDescriptionLimitHeaderKills";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_PAINTINGS = "GameRoundDescriptionLimitHeaderPaintings";
			public static final String GAME_ROUND_DESCRIPTION_LIMIT_HEADER_TIME = "GameRoundDescriptionLimitHeaderTime";
		/* MISC */
		public static final String GAME_ROUND_DESCRIPTION_MISC_TITLE = "GameRoundDescriptionMiscTitle";
			/* HEADER */
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR = "GameRoundDescriptionMiscHeaderAuthor";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION = "GameRoundDescriptionMiscHeaderDimension";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE = "GameRoundDescriptionMiscHeaderInstance";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK = "GameRoundDescriptionMiscHeaderPack";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE = "GameRoundDescriptionMiscHeaderSource";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME = "GameRoundDescriptionMiscHeaderTheme";
			public static final String GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE = "GameRoundDescriptionMiscHeaderTitle";
		/* NOTES */
		public static final String GAME_ROUND_DESCRIPTION_NOTES_TITLE = "GameRoundDescriptionNotesTitle";
		/* POINTS */
		public static final String GAME_ROUND_DESCRIPTION_POINTS_TITLE = "GameRoundDescriptionPointsTitle";
			/* HEADER */
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_CONSTANT = "GameRoundDescriptionPointsHeaderConstant";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_DISCRETIZE = "GameRoundDescriptionPointsHeaderDiscretize";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_PARTIAL = "GameRoundDescriptionPointsHeaderPartial";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKINGS = "GameRoundDescriptionPointsHeaderRankings";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKPOINTS = "GameRoundDescriptionPointsHeaderRankpoints";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_HEADER_SCORE = "GameRoundDescriptionPointsHeaderScore";
			/* DATA */
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_BOMBS = "GameRoundDescriptionPointsDataBombs";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_CROWNS = "GameRoundDescriptionPointsDataCrowns";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_DEATHS = "GameRoundDescriptionPointsDataDeaths";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_INVERTED = "GameRoundDescriptionPointsDataInverted";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_ITEMS = "GameRoundDescriptionPointsDataItems";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_KILLS = "GameRoundDescriptionPointsDataKills";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_NOSHARE = "GameRoundDescriptionPointsDataNoshare";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_PAINTINGS = "GameRoundDescriptionPointsDataPaintings";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_PARTIAL = "GameRoundDescriptionPointsDataPartial";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_REGULAR = "GameRoundDescriptionPointsDataRegular";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_SHARE = "GameRoundDescriptionPointsDataShare";
			public static final String GAME_ROUND_DESCRIPTION_POINTS_DATA_TIME = "GameRoundDescriptionPointsDataTime";
		/* PREVIEW */
		public static final String GAME_ROUND_DESCRIPTION_PREVIEW_TITLE = "GameRoundDescriptionPreviewTitle";
	/* RESULTS */
	public static final String GAME_ROUND_RESULTS_TITLE = "GameRoundResultsTitle";
		/* HEADER */
		public static final String GAME_ROUND_RESULTS_HEADER_BOMBS = "GameRoundResultsHeaderBombs";
		public static final String GAME_ROUND_RESULTS_HEADER_CROWNS = "GameRoundResultsHeaderCrowns";
		public static final String GAME_ROUND_RESULTS_HEADER_CUSTOM_LIMIT = "GameRoundResultsHeaderCustomLimit";
		public static final String GAME_ROUND_RESULTS_HEADER_CUSTOM_POINTS = "GameRoundResultsHeaderCustomPoints";
		public static final String GAME_ROUND_RESULTS_HEADER_DEATHS = "GameRoundResultsHeaderDeaths";
		public static final String GAME_ROUND_RESULTS_HEADER_ITEMS = "GameRoundResultsHeaderItems";
		public static final String GAME_ROUND_RESULTS_HEADER_KILLS = "GameRoundResultsHeaderKills";
		public static final String GAME_ROUND_RESULTS_HEADER_NAME = "GameRoundResultsHeaderName";
		public static final String GAME_ROUND_RESULTS_HEADER_PAINTINGS = "GameRoundResultsHeaderPaintings";
		public static final String GAME_ROUND_RESULTS_HEADER_PROFILE = "GameRoundResultsHeaderProfile";
		public static final String GAME_ROUND_RESULTS_HEADER_POINTS = "GameRoundResultsHeaderPoints";
		public static final String GAME_ROUND_RESULTS_HEADER_TIME = "GameRoundResultsHeaderTime";
		/* DATA */
		public static final String GAME_ROUND_RESULTS_DATA_COMPUTER = "GameRoundResultsDataComputer";
		public static final String GAME_ROUND_RESULTS_DATA_HUMAN = "GameRoundResultsDataHuman";
	/* STATISTICS */
	public static final String GAME_ROUND_STATISTICS_TITLE = "GameRoundStatisticsTitle";
		/* HEADER */
		public static final String GAME_ROUND_STATISTICS_HEADER_NAME = "GameRoundStatisticsHeaderName";

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