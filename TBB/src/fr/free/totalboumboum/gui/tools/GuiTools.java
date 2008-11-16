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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.gui.common.ButtonAware;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.tools.ImageTools;

public class GuiTools
{	
	public static final String TOOLTIP = "Tooltip";
	
	/////////////////////////////////////////////////////////////////
	// MENUS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* MAIN */ 
	public static final String MENU_MAIN_BUTTON_ABOUT = "MenuMainButtonAbout";
	public static final String MENU_MAIN_BUTTON_AI = "MenuMainButtonAi";
	public static final String MENU_MAIN_BUTTON_HEROES = "MenuMainButtonHeroes";
	public static final String MENU_MAIN_BUTTON_LEVELS = "MenuMainButtonLevels";
	public static final String MENU_MAIN_BUTTON_OPTIONS = "MenuMainButtonOptions";
	public static final String MENU_MAIN_BUTTON_PROFILES = "MenuMainButtonProfiles";
	public static final String MENU_MAIN_BUTTON_QUICKMATCH = "MenuMainButtonQuickMatch";
	public static final String MENU_MAIN_BUTTON_STATISTICS = "MenuMainButtonStatistics";
	public static final String MENU_MAIN_BUTTON_TOURNAMENT = "MenuMainButtonTournament";
	/* AI */
		/* BUTTON */
		public static final String MENU_AI_BUTTON_BACK = "MenuAiButtonBack";	
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
		public static final String MENU_HERO_BUTTON_BACK = "MenuHeroButtonBack";	
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
		public static final String MENU_LEVEL_BUTTON_BACK = "MenuLevelButtonBack";	
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
		public static final String MENU_OPTIONS_BUTTON_ADVANCED = "MenuOptionsButtonAdvanced";
		public static final String MENU_OPTIONS_BUTTON_BACK = "MenuOptionsButtonBack";
		public static final String MENU_OPTIONS_BUTTON_CANCEL = "MenuOptionsButtonCancel";
		public static final String MENU_OPTIONS_BUTTON_CONFIRM = "MenuOptionsButtonConfirm";
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = "MenuOptionsButtonControls";
		public static final String MENU_OPTIONS_BUTTON_GAMEPLAY = "MenuOptionsButtonGameplay";
		public static final String MENU_OPTIONS_BUTTON_GUI = "MenuOptionsButtonGui";
		public static final String MENU_OPTIONS_BUTTON_NEXT = "MenuOptionsButtonNext";
		public static final String MENU_OPTIONS_BUTTON_PREVIOUS = "MenuOptionsButtonPrevious";
		public static final String MENU_OPTIONS_BUTTON_VIDEO = "MenuOptionsButtonVideo";
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
		public static final String MENU_PROFILES_BUTTON_BACK = "MenuProfilesButtonBack";
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
	/* TOURNAMENT */	
	public static final String MENU_TOURNAMENT_BUTTON_BACK = "MenuTournamentButtonBack";
	public static final String MENU_TOURNAMENT_BUTTON_CONTINUE = "MenuTournamentButtonContinue";
	public static final String MENU_TOURNAMENT_BUTTON_LOAD = "MenuTournamentButtonLoad";
	public static final String MENU_TOURNAMENT_BUTTON_NEW = "MenuTournamentButtonNew";
	public static final String MENU_TOURNAMENT_BUTTON_PLAYERS = "MenuTournamentButtonPlayers";
	public static final String MENU_TOURNAMENT_BUTTON_RULES = "MenuTournamentButtonRules";
	public static final String MENU_TOURNAMENT_BUTTON_SAVE_AS = "MenuTournamentButtonSaveAs";
	public static final String MENU_TOURNAMENT_BUTTON_START = "MenuTournamentButtonStart";
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
	// IMAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static BufferedImage absentImage;	
	
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
	public static BufferedImage loadIcon(String path, BufferedImage absent)
	{	BufferedImage image;
		try
		{	image = ImageTools.loadImage(path,null);
		}
		catch (IOException e)
		{	image = absent;
		}
		return image;	
	}
	
	public static void initImages()
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
	
	public static void initButtonImages()
	{	// init
		String[] buttonStates = {ICON_NORMAL,ICON_NORMAL_SELECTED,
			ICON_DISABLED,ICON_DISABLED_SELECTED,
			ICON_ROLLOVER,ICON_ROLLOVER_SELECTED,
			ICON_PRESSED};
		String baseFolder = GuiFileTools.getButtonsPath()+File.separator;
		// images
		{	String folder = baseFolder+GuiFileTools.FOLDER_DESCRIPTION+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_DESCRIPTION,
				GAME_MATCH_BUTTON_DESCRIPTION,
				GAME_ROUND_BUTTON_DESCRIPTION
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_BLUE+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_MENU,
					GAME_MATCH_BUTTON_CURRENT_TOURNAMENT,
				GAME_ROUND_BUTTON_CURRENT_MATCH
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_RED+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_FINISH,
				GAME_MATCH_BUTTON_FINISH,
				GAME_ROUND_BUTTON_FINISH
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_PLAY+File.separator;
			String[] uses = 
			{	GAME_ROUND_BUTTON_PLAY
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_HOME+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_QUIT,
				GAME_MATCH_BUTTON_QUIT,
				GAME_ROUND_BUTTON_QUIT
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RESULTS+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_RESULTS,
				GAME_MATCH_BUTTON_RESULTS,
				GAME_ROUND_BUTTON_RESULTS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_BLUE+File.separator;
			String[] uses = 
			{	
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_RED+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_CURRENT_MATCH,
				GAME_TOURNAMENT_BUTTON_NEXT_MATCH,
				GAME_MATCH_BUTTON_CURRENT_ROUND,
				GAME_MATCH_BUTTON_NEXT_ROUND
			};
			loadButtonImages(buttonStates,folder,uses);
		}
		{	String folder = baseFolder+GuiFileTools.FOLDER_STATS+File.separator;
			String[] uses = 
			{	GAME_TOURNAMENT_BUTTON_STATISTICS,
				GAME_MATCH_BUTTON_STATISTICS,
				GAME_ROUND_BUTTON_STATISTICS
			};
			loadButtonImages(buttonStates,folder,uses);
		}
	}
	
	private static void loadTableImages(String folder, String[] uses)
	{	BufferedImage image = loadIcon(folder,absentImage);
		for(int j=0;j<uses.length;j++)
			icons.put(uses[j],image);
	}
	
	public static void initHeaderImages()
	{	String baseFolder = GuiFileTools.getHeadersPath()+File.separator;
		// author
		{	String folder = baseFolder+GuiFileTools.FILE_AUTHOR;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR,
				MENU_AI_SELECT_PREVIEW_AUTHOR,
				MENU_HERO_SELECT_PREVIEW_AUTHOR,
				MENU_LEVEL_SELECT_PREVIEW_AUTHOR
			};
			loadTableImages(folder,uses);
		}
		// autofire
		{	String folder = baseFolder+GuiFileTools.FILE_AUTOFIRE;
			String[] uses =
			{	MENU_OPTIONS_CONTROLS_HEADER_AUTO
			};
			loadTableImages(folder,uses);
		}
		// bombs
		{	String folder = baseFolder+GuiFileTools.FILE_BOMBS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_BOMBS,
				GAME_MATCH_RESULTS_HEADER_BOMBS,
				GAME_ROUND_RESULTS_HEADER_BOMBS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_BOMBS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_BOMBS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// color
		{	String folder = baseFolder+GuiFileTools.FILE_COLOR;
			String[] uses =
			{	MENU_PROFILES_PREVIEW_COLOR,
				MENU_PROFILES_EDIT_COLOR,
				MENU_HERO_SELECT_PREVIEW_COLORS
			};
			loadTableImages(folder,uses);
		}
		// command
		{	String folder = baseFolder+GuiFileTools.FILE_COMMAND;
			String[] uses =
			{	MENU_OPTIONS_CONTROLS_HEADER_COMMAND
			};
			loadTableImages(folder,uses);
		}
		// computer
		{	String folder = baseFolder+GuiFileTools.FILE_COMPUTER;
			String[] uses =
			{	MENU_PROFILES_PREVIEW_AINAME,
				MENU_PROFILES_EDIT_AI
			};
			loadTableImages(folder,uses);
		}
		// confrontations
		{	String folder = baseFolder+GuiFileTools.FILE_CONFRONTATIONS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS
			};
			loadTableImages(folder,uses);
		}
		// constant
		{	String folder = baseFolder+GuiFileTools.FILE_CONSTANT;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_CONSTANT,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_CONSTANT,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_CONSTANT
			};
			loadTableImages(folder,uses);
		}
		// crowns
		{	String folder = baseFolder+GuiFileTools.FILE_CROWNS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_CROWNS,
				GAME_MATCH_RESULTS_HEADER_CROWNS,
				GAME_ROUND_RESULTS_HEADER_CROWNS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CROWNS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CROWNS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CROWNS
			};
			loadTableImages(folder,uses);
		}
		// custom points
		{	String folder = baseFolder+GuiFileTools.FILE_CUSTOM;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CUSTOM,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CUSTOM,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CUSTOM,
				GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_LIMIT,
				GAME_MATCH_RESULTS_HEADER_CUSTOM_LIMIT,
				GAME_ROUND_RESULTS_HEADER_CUSTOM_LIMIT,
				GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_POINTS,
				GAME_MATCH_RESULTS_HEADER_CUSTOM_POINTS,
				GAME_ROUND_RESULTS_HEADER_CUSTOM_POINTS
			};
			loadTableImages(folder,uses);
		}
		// deaths
		{	String folder = baseFolder+GuiFileTools.FILE_DEATHS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_DEATHS,
				GAME_MATCH_RESULTS_HEADER_DEATHS,
				GAME_ROUND_RESULTS_HEADER_DEATHS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_DEATHS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_DEATHS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_DEATHS
			};
			loadTableImages(folder,uses);
		}
		// dimension
		{	String folder = baseFolder+GuiFileTools.FILE_DIMENSION;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION,
				MENU_LEVEL_SELECT_PREVIEW_SIZE
			};
			loadTableImages(folder,uses);
		}
		// discretize
		{	String folder = baseFolder+GuiFileTools.FILE_DISCRETIZE;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_DISCRETIZE,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_DISCRETIZE,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_DISCRETIZE
			};
			loadTableImages(folder,uses);
		}
		// hero
		{	String folder = baseFolder+GuiFileTools.FILE_HERO;
			String[] uses =
			{	MENU_PROFILES_PREVIEW_HERONAME,
				MENU_PROFILES_EDIT_HERO,
				MENU_HERO_SELECT_PREVIEW_IMAGE
			};
			loadTableImages(folder,uses);
		}
		// initial
		{	String folder = baseFolder+GuiFileTools.FILE_INITIAL;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_INITIALITEMS_TITLE
			};
			loadTableImages(folder,uses);
		}
		// instance
		{	String folder = baseFolder+GuiFileTools.FILE_INSTANCE;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE,
				MENU_LEVEL_SELECT_PREVIEW_INSTANCE
			};
			loadTableImages(folder,uses);
		}
		// items
		{	String folder = baseFolder+GuiFileTools.FILE_ITEMS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_ITEMS,
				GAME_MATCH_RESULTS_HEADER_ITEMS,
				GAME_ROUND_RESULTS_HEADER_ITEMS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_ITEMS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_ITEMS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_ITEMS,
				GAME_ROUND_DESCRIPTION_ITEMSET_TITLE
			};
			loadTableImages(folder,uses);
		}
		// key
		{	String folder = baseFolder+GuiFileTools.FILE_KEY;
			String[] uses =
			{	MENU_OPTIONS_CONTROLS_HEADER_KEY,
				GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_CONTROLS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_KILLS,
				GAME_MATCH_RESULTS_HEADER_KILLS,
				GAME_ROUND_RESULTS_HEADER_KILLS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_KILLS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_KILLS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_KILLS
			};
			loadTableImages(folder,uses);
		}
		// limits
		{	String folder = baseFolder+GuiFileTools.FILE_LIMITS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_LIMIT_TITLE,
				GAME_MATCH_DESCRIPTION_LIMIT_TITLE,
				GAME_ROUND_DESCRIPTION_LIMIT_TITLE
			};
			loadTableImages(folder,uses);
		}
		// misc
		{	String folder = baseFolder+GuiFileTools.FILE_MISC;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_MISC_TITLE,
				GAME_MATCH_DESCRIPTION_MISC_TITLE,
				GAME_ROUND_DESCRIPTION_MISC_TITLE,
				MENU_AI_SELECT_PREVIEW_NOTES
			};
			loadTableImages(folder,uses);
		}
		// name
		{	String folder = baseFolder+GuiFileTools.FILE_NAME;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_NAME,
				GAME_TOURNAMENT_RESULTS_HEADER_NAME,
				GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME,
				GAME_MATCH_RESULTS_HEADER_NAME,
				GAME_ROUND_RESULTS_HEADER_NAME,
				GAME_TOURNAMENT_STATISTICS_HEADER_NAME,
				GAME_MATCH_STATISTICS_HEADER_NAME,
				GAME_ROUND_STATISTICS_HEADER_NAME,
				MENU_PROFILES_PREVIEW_NAME,
				MENU_PROFILES_EDIT_NAME
			};
			loadTableImages(folder,uses);
		}
		// pack
		{	String folder = baseFolder+GuiFileTools.FILE_PACK;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK,
				MENU_PROFILES_PREVIEW_AIPACK,
				MENU_PROFILES_PREVIEW_HEROPACK,
				MENU_AI_SELECT_PREVIEW_PACKAGE,
				MENU_LEVEL_SELECT_PREVIEW_PACKAGE
			};
			loadTableImages(folder,uses);
		}
		// paintings
		{	String folder = baseFolder+GuiFileTools.FILE_PAINTINGS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_PAINTINGS,
				GAME_MATCH_RESULTS_HEADER_PAINTINGS,
				GAME_ROUND_RESULTS_HEADER_PAINTINGS,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_PAINTINGS,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_PAINTINGS,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_PAINTINGS
			};
			loadTableImages(folder,uses);
		}
		// partial
		{	String folder = baseFolder+GuiFileTools.FILE_PARTIAL;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_PARTIAL,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_PARTIAL,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_PARTIAL
			};
			loadTableImages(folder,uses);
		}
		// points
		{	String folder = baseFolder+GuiFileTools.FILE_POINTS;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_POINTS,
				GAME_MATCH_RESULTS_HEADER_POINTS,
				GAME_ROUND_RESULTS_HEADER_POINTS,
				GAME_TOURNAMENT_DESCRIPTION_POINTS_TITLE,
				GAME_MATCH_DESCRIPTION_POINTS_TITLE,
				GAME_ROUND_DESCRIPTION_POINTS_TITLE
			};
			loadTableImages(folder,uses);
		}
		// profile
		{	String folder = baseFolder+GuiFileTools.FILE_PROFILE;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_PROFILE,
				GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_PROFILE,
				GAME_TOURNAMENT_RESULTS_HEADER_PROFILE,
				GAME_MATCH_RESULTS_HEADER_PROFILE,
				GAME_ROUND_RESULTS_HEADER_PROFILE
			};
			loadTableImages(folder,uses);
		}
		// rank
		{	String folder = baseFolder+GuiFileTools.FILE_RANK;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_RANK,
				GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK,
				GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKINGS,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKINGS,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKINGS,
				GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKPOINTS,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKPOINTS,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKPOINTS
			};
			loadTableImages(folder,uses);
		}
		// score
		{	String folder = baseFolder+GuiFileTools.FILE_SCORE;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_SCORE,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_SCORE,
				GAME_ROUND_DESCRIPTION_POINTS_HEADER_SCORE
			};
			loadTableImages(folder,uses);
		}
		// source
		{	String folder = baseFolder+GuiFileTools.FILE_SOURCE;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE,
				MENU_HERO_SELECT_PREVIEW_SOURCE,
				MENU_LEVEL_SELECT_PREVIEW_SOURCE
			};
			loadTableImages(folder,uses);
		}
		// theme
		{	String folder = baseFolder+GuiFileTools.FILE_THEME;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME,
				MENU_LEVEL_SELECT_PREVIEW_THEME
			};
			loadTableImages(folder,uses);
		}
		// time
		{	String folder = baseFolder+GuiFileTools.FILE_TIME;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_TIME,
				GAME_MATCH_RESULTS_HEADER_TIME,
				GAME_ROUND_RESULTS_HEADER_TIME,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TIME,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TIME,
				GAME_ROUND_DESCRIPTION_LIMIT_HEADER_TIME
			};
			loadTableImages(folder,uses);
		}
		// title
		{	String folder = baseFolder+GuiFileTools.FILE_TITLE;
			String[] uses =
			{	GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE,
				MENU_AI_SELECT_PREVIEW_NAME,
				MENU_HERO_SELECT_PREVIEW_NAME,
				MENU_LEVEL_SELECT_PREVIEW_NAME
			};
			loadTableImages(folder,uses);
		}
		// total
		{	String folder = baseFolder+GuiFileTools.FILE_TOTAL;
			String[] uses =
			{	GAME_TOURNAMENT_RESULTS_HEADER_TOTAL,
				GAME_MATCH_RESULTS_HEADER_TOTAL,
				GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TOTAL,
				GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TOTAL,
				GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_TOTAL,
				GAME_MATCH_DESCRIPTION_POINTS_HEADER_TOTAL
			};
			loadTableImages(folder,uses);
		}
	}
	
	public static void initDataImages()
	{	String baseFolder = GuiFileTools.getDataPath()+File.separator;
		// bombs
		{	String folder = baseFolder+GuiFileTools.FILE_BOMBS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_BOMBS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_BOMBS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_BOMBS
			};
			loadTableImages(folder,uses);
		}
		// computer
		{	String folder = baseFolder+GuiFileTools.FILE_COMPUTER;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER,
				GAME_MATCH_DESCRIPTION_PLAYERS_DATA_COMPUTER,
				GAME_TOURNAMENT_RESULTS_DATA_COMPUTER,
				GAME_MATCH_RESULTS_DATA_COMPUTER,
				GAME_ROUND_RESULTS_DATA_COMPUTER
			};
			loadTableImages(folder,uses);
		}
		// crowns
		{	String folder = baseFolder+GuiFileTools.FILE_CROWNS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_CROWNS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_CROWNS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_CROWNS
			};
			loadTableImages(folder,uses);
		}
		// deaths
		{	String folder = baseFolder+GuiFileTools.FILE_DEATHS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_DEATHS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_DEATHS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_DEATHS
			};
			loadTableImages(folder,uses);
		}
		// edit
		{	String folder = baseFolder+GuiFileTools.FILE_EDIT;
			String[] uses =
			{	MENU_PROFILES_EDIT_AI_CHANGE,
				MENU_PROFILES_EDIT_HERO_CHANGE,
				MENU_PROFILES_EDIT_NAME_CHANGE
			};
			loadTableImages(folder,uses);
		}
		// false
		{	String folder = baseFolder+GuiFileTools.FILE_FALSE;
			String[] uses =
			{	MENU_OPTIONS_CONTROLS_LINE_AUTO_FALSE,
				MENU_OPTIONS_VIDEO_LINE_DISABLED,
				MENU_OPTIONS_ADVANCED_LINE_ADJUST_DISABLED,
				MENU_PROFILES_EDIT_AI_RESET
			};
			loadTableImages(folder,uses);
		}
		// human
		{	String folder = baseFolder+GuiFileTools.FILE_HUMAN;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN,
				GAME_MATCH_DESCRIPTION_PLAYERS_DATA_HUMAN,
				GAME_TOURNAMENT_RESULTS_DATA_HUMAN,
				GAME_MATCH_RESULTS_DATA_HUMAN,
				GAME_ROUND_RESULTS_DATA_HUMAN
			};
			loadTableImages(folder,uses);
		}
		// inverted order
		{	String folder = baseFolder+GuiFileTools.FILE_INVERTED;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_INVERTED,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_INVERTED,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_INVERTED
			};
			loadTableImages(folder,uses);
		}
		// items
		{	String folder = baseFolder+GuiFileTools.FILE_ITEMS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_ITEMS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_ITEMS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_ITEMS
			};
			loadTableImages(folder,uses);
		}
		// kills
		{	String folder = baseFolder+GuiFileTools.FILE_KILLS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_KILLS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_KILLS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_KILLS
			};
			loadTableImages(folder,uses);
		}
		// minus
		{	String folder = baseFolder+GuiFileTools.FILE_MINUS;
			String[] uses =
			{	MENU_OPTIONS_VIDEO_LINE_MINUS,
				MENU_OPTIONS_ADVANCED_LINE_FPS_MINUS,
				MENU_OPTIONS_ADVANCED_LINE_SPEED_MINUS
			};
			loadTableImages(folder,uses);
		}
		// next
		{	String folder = baseFolder+GuiFileTools.FILE_NEXT;
			String[] uses =
			{	MENU_OPTIONS_GUI_LINE_LANGUAGE_NEXT,
				MENU_OPTIONS_GUI_LINE_FONT_NEXT,
				MENU_OPTIONS_GUI_LINE_BACKGROUND_NEXT,
				MENU_PROFILES_EDIT_COLOR_NEXT
			};
			loadTableImages(folder,uses);
		}
		// no share
		{	String folder = baseFolder+GuiFileTools.FILE_NOSHARE;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_NOSHARE,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_NOSHARE,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_NOSHARE
			};
			loadTableImages(folder,uses);
		}
		// page down
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_DOWN;
			String[] uses =
			{	MENU_PROFILES_LIST_PAGEDOWN,
				MENU_AI_SELECT_CLASS_PAGEDOWN,
				MENU_AI_SELECT_PACKAGE_PAGEDOWN,
				MENU_HERO_SELECT_FOLDER_PAGEDOWN,
				MENU_HERO_SELECT_PACKAGE_PAGEDOWN,
				MENU_LEVEL_SELECT_FOLDER_PAGEDOWN,
				MENU_LEVEL_SELECT_PACKAGE_PAGEDOWN
			};
			loadTableImages(folder,uses);
		}
		// page up
		{	String folder = baseFolder+GuiFileTools.FILE_PAGE_UP;
			String[] uses =
			{	MENU_PROFILES_LIST_PAGEUP,
				MENU_AI_SELECT_CLASS_PAGEUP,
				MENU_AI_SELECT_PACKAGE_PAGEUP,
				MENU_HERO_SELECT_FOLDER_PAGEUP,
				MENU_HERO_SELECT_PACKAGE_PAGEUP,
				MENU_LEVEL_SELECT_FOLDER_PAGEUP,
				MENU_LEVEL_SELECT_PACKAGE_PAGEUP
			};
			loadTableImages(folder,uses);
		}
		// paintings
		{	String folder = baseFolder+GuiFileTools.FILE_PAINTINGS;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PAINTINGS,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_PAINTINGS,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_PAINTINGS
			};
			loadTableImages(folder,uses);
		}
		// partial
		{	String folder = baseFolder+GuiFileTools.FILE_PARTIAL;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PARTIAL,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_PARTIAL,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_PARTIAL
			};
			loadTableImages(folder,uses);
		}
		// plus
		{	String folder = baseFolder+GuiFileTools.FILE_PLUS;
			String[] uses =
			{	MENU_OPTIONS_VIDEO_LINE_PLUS,
				MENU_OPTIONS_ADVANCED_LINE_FPS_PLUS,
				MENU_OPTIONS_ADVANCED_LINE_SPEED_PLUS
			};
			loadTableImages(folder,uses);
		}
		// previous
		{	String folder = baseFolder+GuiFileTools.FILE_PREVIOUS;
			String[] uses =
			{	MENU_OPTIONS_GUI_LINE_LANGUAGE_PREVIOUS,
				MENU_OPTIONS_GUI_LINE_FONT_PREVIOUS,
				MENU_OPTIONS_GUI_LINE_BACKGROUND_PREVIOUS,
				MENU_PROFILES_EDIT_COLOR_PREVIOUS
			};
			loadTableImages(folder,uses);
		}
		// regular order
		{	String folder = baseFolder+GuiFileTools.FILE_REGULAR;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_REGULAR,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_REGULAR,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_REGULAR
			};
			loadTableImages(folder,uses);
		}
		// share
		{	String folder = baseFolder+GuiFileTools.FILE_SHARE;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_SHARE,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_SHARE,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_SHARE
			};
			loadTableImages(folder,uses);
		}
		// time
		{	String folder = baseFolder+GuiFileTools.FILE_TIME;
			String[] uses =
			{	GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_TIME,
				GAME_MATCH_DESCRIPTION_POINTS_DATA_TIME,
				GAME_ROUND_DESCRIPTION_POINTS_DATA_TIME
			};
			loadTableImages(folder,uses);
		}
		// true
		{	String folder = baseFolder+GuiFileTools.FILE_TRUE;
			String[] uses =
			{	MENU_OPTIONS_CONTROLS_LINE_AUTO_TRUE,
				MENU_OPTIONS_VIDEO_LINE_ENABLED,
				MENU_OPTIONS_ADVANCED_LINE_ADJUST_ENABLED
			};
			loadTableImages(folder,uses);
		}
	}
				
	/////////////////////////////////////////////////////////////////
	// FONTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static Graphics graphics;
	public final static float FONT_RATIO = 0.8f; // font height relatively to the containing label (or component)

	private static void initGraphics()
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

	/////////////////////////////////////////////////////////////////
	// SIZE 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	// margins
	public final static float PANEL_MARGIN_RATIO = 0.025f; 
	public static int panelMargin; // margin between the components of a frame
	public final static float SUBPANEL_MARGIN_RATIO = 0.005f; 
	public static int subPanelMargin;// margin between the components of a panel

	// titles
	public final static float SUBPANEL_TITLE_RATIO = 1.5f; // subpanel title height relatively to panel margin
	public static int subPanelTitleHeight; // height of a subpanel title bar relatively to the height of a panel title
	public final static float TABLE_HEADER_RATIO = 1.2f; //header high relatively to line height

	// panel split
	public final static float VERTICAL_SPLIT_RATIO = 0.25f;
	public final static float HORIZONTAL_SPLIT_RATIO = 0.07f;

	
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
	}
	
	
	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public final static Color COLOR_SPLASHSCREEN_TEXT = new Color(204,18,128);
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,150);
	public final static Color COLOR_TITLE_FOREGROUND = Color.BLACK;
	public final static Color COLOR_TABLE_SELECTED_BACKGROUND = new Color(204,18,128,80);
	public final static Color COLOR_TABLE_REGULAR_BACKGROUND = new Color(0,0,0,80);
	public final static Color COLOR_TABLE_NEUTRAL_BACKGROUND = new Color(0,0,0,20);
	public final static Color COLOR_TABLE_REGULAR_FOREGROUND = Color.BLACK;
	public final static Color COLOR_TABLE_HEADER_BACKGROUND = new Color(0,0,0,130);
	public final static Color COLOR_TABLE_HEADER_FOREGROUND = Color.WHITE;
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL1 = 80; //scores
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL2 = 140; // rounds/matches
	public final static int ALPHA_TABLE_REGULAR_BACKGROUND_LEVEL3 = 200; //portrait/name/total/points
	
	
	
	
	// menus
	public static final String MENU_HORIZONTAL_BUTTON_HEIGHT = "MENU_HORIZONTAL_BUTTON_HEIGHT";
	public static final String MENU_VERTICAL_BUTTON_HEIGHT = "MENU_VERTICAL_BUTTON_HEIGHT";
	public static final String MENU_VERTICAL_PRIMARY_BUTTON_FONT_SIZE = "MENU_VERTICAL_PRIMARY_BUTTON_FONT_SIZE";
	public static final String MENU_VERTICAL_SECONDARY_BUTTON_FONT_SIZE = "MENU_VERTICAL_SECONDARY_BUTTON_FONT_SIZE";
	public static final String MENU_HORIZONTAL_BUTTON_WIDTH = "MENU_HORIZONTAL_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_PRIMARY_BUTTON_WIDTH = "MENU_VERTICAL_PRIMARY_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_SECONDARY_BUTTON_WIDTH = "MENU_VERTICAL_SECONDARY_BUTTON_WIDTH";
	public static final String MENU_VERTICAL_BUTTON_SPACE = "MENU_VERTICAL_BUTTON_SPACE";
	public static final String MENU_HORIZONTAL_BUTTON_SPACE = "MENU_HORIZONTAL_BUTTON_SPACE";
	// splits
	public static final String HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT = "HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT";
	public static final String HORIZONTAL_SPLIT_MENU_PANEL_WIDTH = "HORIZONTAL_SPLIT_MENU_PANEL_WIDTH";
	public static final String HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT = "HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT";
	public static final String HORIZONTAL_SPLIT_DATA_PANEL_WIDTH = "HORIZONTAL_SPLIT_DATA_PANEL_WIDTH";
	public static final String VERTICAL_SPLIT_MENU_PANEL_HEIGHT = "VERTICAL_SPLIT_MENU_PANEL_HEIGHT";
	public static final String VERTICAL_SPLIT_MENU_PANEL_WIDTH = "VERTICAL_SPLIT_MENU_PANEL_WIDTH";
	public static final String VERTICAL_SPLIT_DATA_PANEL_HEIGHT = "VERTICAL_SPLIT_DATA_PANEL_HEIGHT";
	public static final String VERTICAL_SPLIT_DATA_PANEL_WIDTH = "VERTICAL_SPLIT_DATA_PANEL_WIDTH";
	// game
	public static final String GAME_DATA_PANEL_HEIGHT = "GAME_DATA_PANEL_HEIGHT";
	public static final String GAME_DATA_PANEL_WIDTH = "GAME_DATA_PANEL_WIDTH";
	public static final String GAME_DATA_LABEL_TITLE_HEIGHT = "GAME_DATA_LABEL_TITLE_HEIGHT";
	public static final String GAME_DATA_MARGIN_SIZE = "GAME_DATA_MARGIN_SIZE";
	public static final String GAME_TITLE_FONT_SIZE = "GAME_TITLE_FONT_SIZE";
	public static final String GAME_PROGRESSBAR_FONT_SIZE = "GAME_PROGRESSBAR_FONT_SIZE";
	public static final String GAME_DESCRIPTION_PANEL_WIDTH = "GAME_DESCRIPTION_PANEL_WIDTH";
	public static final String GAME_DESCRIPTION_PANEL_HEIGHT = "GAME_DESCRIPTION_PANEL_HEIGHT";
	public static final String GAME_DESCRIPTION_LABEL_LINE_WIDTH = "GAME_DESCRIPTION_LABEL_LINE_WIDTH";
	public static final String GAME_DESCRIPTION_LABEL_TEXT_HEIGHT = "GAME_DESCRIPTION_LABEL_TEXT_HEIGHT";
	public static final String GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE = "GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE";
	public static final String GAME_RESULTS_LABEL_LINE_HEIGHT = "GAME_RESULTS_LABEL_LINE_HEIGHT";
	public static final String GAME_RESULTS_LABEL_LINE_NUMBER = "GAME_RESULTS_LABEL_LINE_NUMBER";
	public static final String GAME_RESULTS_LABEL_HEADER_HEIGHT = "GAME_RESULTS_LABEL_HEADER_HEIGHT";
	public static final String GAME_RESULTS_LINE_FONT_SIZE = "GAME_RESULTS_LINE_FONT_SIZE";
	public static final String GAME_RESULTS_HEADER_FONT_SIZE = "GAME_RESULTS_HEADER_FONT_SIZE";
	public static final String GAME_RESULTS_MARGIN_SIZE = "GAME_RESULTS_MARGIN_SIZE";
	public static final String GAME_RESULTS_LABEL_NAME_WIDTH = "GAME_RESULTS_LABEL_NAME_WIDTH";
		
	// sizes
	private static final HashMap<String,Integer> sizes = new HashMap<String,Integer>();
	
	// icons
	public static final String ICON_NORMAL = "normal";
	public static final String ICON_NORMAL_SELECTED = "normal_selected";
	public static final String ICON_DISABLED = "disabled";
	public static final String ICON_DISABLED_SELECTED = "disabled_selected";
	public static final String ICON_ROLLOVER = "rollover";
	public static final String ICON_ROLLOVER_SELECTED = "rollover_selected";
	public static final String ICON_PRESSED = "pressed";
	private static final HashMap<String,BufferedImage> icons = new HashMap<String,BufferedImage>();
	

	public static void init()
	{	initGraphics();
		initSizes();
		
		
		
		
		// buttons
		int menuVerticalButtonHeight = (int)(height*0.05);
		sizes.put(MENU_VERTICAL_BUTTON_HEIGHT,menuVerticalButtonHeight);
		int menuHorizontalButtonHeight = (int)(height*0.07);
		sizes.put(MENU_HORIZONTAL_BUTTON_HEIGHT,menuHorizontalButtonHeight);
		int menuHorizontalButtonWidth = menuHorizontalButtonHeight;
		sizes.put(MENU_HORIZONTAL_BUTTON_WIDTH,menuHorizontalButtonWidth);
		int menuHorizontalButtonSpace = (int)(width*0.025);
		sizes.put(MENU_HORIZONTAL_BUTTON_SPACE,menuHorizontalButtonSpace);
		int gameProgressbarFontSize = getFontSize(menuHorizontalButtonHeight*0.6); 
		sizes.put(GAME_PROGRESSBAR_FONT_SIZE,gameProgressbarFontSize);
		int menuVerticalPrimaryButtonWidth = (int)(width*0.33);
		sizes.put(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH,menuVerticalPrimaryButtonWidth);
		int menuVerticalSecondaryButtonWidth = (int)(width*0.25);
		sizes.put(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH,menuVerticalSecondaryButtonWidth);
		int menuVerticalSecondaryButtonSpace = (int)(height*0.025);
		sizes.put(MENU_VERTICAL_BUTTON_SPACE,menuVerticalSecondaryButtonSpace);
		
		// menu panels
		int horizontalSplitMenuPanelHeight = menuHorizontalButtonHeight; 
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT,horizontalSplitMenuPanelHeight);
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_WIDTH,width);
		int horizontalSplitDataPanelHeight = height-horizontalSplitMenuPanelHeight; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT,horizontalSplitDataPanelHeight);
		int horizontalSplitDataPanelWidth = width; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_WIDTH,horizontalSplitDataPanelWidth);
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_HEIGHT,height);
		int verticalSplitMenuPanelWidth = menuVerticalSecondaryButtonWidth;
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_WIDTH,verticalSplitMenuPanelWidth);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_HEIGHT,height);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_WIDTH,width-verticalSplitMenuPanelWidth);
		
		// fonts
		int menuVerticalPrimaryButtonFontSize;
		{	Iterator<Entry<String,String>> it = GuiConfiguration.getMiscConfiguration().getLanguage().getTexts().entrySet().iterator();
			String longest = "";
			while(it.hasNext())
			{	Entry<String,String> temp = it.next();
				String key = temp.getKey();
				String value = temp.getValue();
				if(!key.endsWith("Tooltip") && (key.startsWith("MenuMainButton") || key.startsWith("MenuOptionsButton")))
				{	if(value.length()>longest.length())
						longest = value;
				}
			}
			menuVerticalPrimaryButtonFontSize = getFontSize(menuVerticalPrimaryButtonWidth*0.8,menuVerticalButtonHeight*0.9,longest);
		}
		sizes.put(MENU_VERTICAL_PRIMARY_BUTTON_FONT_SIZE, menuVerticalPrimaryButtonFontSize);
		//
		int menuVerticalSecondaryButtonFontSize;
		{	Iterator<Entry<String,String>> it = GuiConfiguration.getMiscConfiguration().getLanguage().getTexts().entrySet().iterator();
			String longest = "";
			while(it.hasNext())
			{	Entry<String,String> temp = it.next();
				String key = temp.getKey();
				String value = temp.getValue();
				if(!key.endsWith("Tooltip") && key.startsWith("MenuTournamentButton"))
				{	if(value.length()>longest.length())
						longest = value;
				}
			}
			menuVerticalSecondaryButtonFontSize = getFontSize(menuVerticalSecondaryButtonWidth*0.8,menuVerticalButtonHeight*0.9,longest);
		}
		sizes.put(MENU_VERTICAL_SECONDARY_BUTTON_FONT_SIZE, menuVerticalSecondaryButtonFontSize);
		//
		int gameTitleFontSize = (int)(menuVerticalPrimaryButtonFontSize*1.3);
		sizes.put(GAME_TITLE_FONT_SIZE, gameTitleFontSize);

		// labels
		int gameDataLabelTitleHeight = getPixelHeight((float)gameTitleFontSize);
		sizes.put(GAME_DATA_LABEL_TITLE_HEIGHT, gameDataLabelTitleHeight);
		// game panel
		int gameDataMarginSize = (int)(width*0.025);
		sizes.put(GAME_DATA_MARGIN_SIZE,gameDataMarginSize);
		int gameDataPanelHeight = horizontalSplitDataPanelHeight-3*gameDataMarginSize-gameDataLabelTitleHeight;
		sizes.put(GAME_DATA_PANEL_HEIGHT,gameDataPanelHeight);
		int gameDataPanelWidth = horizontalSplitDataPanelWidth-2*gameDataMarginSize;
		sizes.put(GAME_DATA_PANEL_WIDTH,gameDataPanelWidth);
		int gameResultsMarginSize = (int)(height*0.005);
		sizes.put(GAME_RESULTS_MARGIN_SIZE,gameResultsMarginSize);
		int gameResultsLabelLineNumber = 16;
		sizes.put(GAME_RESULTS_LABEL_LINE_NUMBER,gameResultsLabelLineNumber);
		int gameResultsLabelLineHeight = (gameDataPanelHeight-gameResultsLabelLineNumber*gameResultsMarginSize)/17;
		sizes.put(GAME_RESULTS_LABEL_LINE_HEIGHT,gameResultsLabelLineHeight);
		int gameResultLineFontSize = getFontSize(gameResultsLabelLineHeight*0.9);
		sizes.put(GAME_RESULTS_LINE_FONT_SIZE,gameResultLineFontSize);
		int gameResultLabelNameWidth = (int)(gameDataPanelWidth/3);
		sizes.put(GAME_RESULTS_LABEL_NAME_WIDTH,gameResultLabelNameWidth);
		int gameResultsLabelHeaderHeight = gameDataPanelHeight-16*gameResultsMarginSize-16*gameResultsLabelLineHeight;
		sizes.put(GAME_RESULTS_LABEL_HEADER_HEIGHT,gameResultsLabelHeaderHeight);
		int gameResultHeaderFontSize = getFontSize(gameResultsLabelHeaderHeight*0.9);
		sizes.put(GAME_RESULTS_HEADER_FONT_SIZE,gameResultHeaderFontSize);
		int gameDescriptionPanelWidth = (gameDataPanelWidth-gameDataMarginSize)/2;
		sizes.put(GAME_DESCRIPTION_PANEL_WIDTH,gameDescriptionPanelWidth);
		int gameDescriptionPanelHeight = (gameDataPanelHeight-2*gameDataMarginSize)/3;
		sizes.put(GAME_DESCRIPTION_PANEL_HEIGHT,gameDescriptionPanelHeight);
		int gameDescriptionLabelLineWidth = gameDescriptionPanelWidth-2*gameResultsMarginSize;
		sizes.put(GAME_DESCRIPTION_LABEL_LINE_WIDTH,gameDescriptionLabelLineWidth);
		int gameDescriptionLabelTextHeight = gameDescriptionPanelHeight-3*gameResultsMarginSize-gameResultsLabelHeaderHeight;
		sizes.put(GAME_DESCRIPTION_LABEL_TEXT_HEIGHT,gameDescriptionLabelTextHeight);
		int gameDescriptionLabelTextFontSize = gameDescriptionLabelTextHeight/7;
		sizes.put(GAME_DESCRIPTION_LABEL_TEXT_FONT_SIZE,gameDescriptionLabelTextFontSize);
		
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public static void setButtonContent(String name, AbstractButton button)
	{	// content
		if(icons.containsKey(name+ICON_NORMAL))
		{	// normal icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setIcon(ii);
			}
			// disabled icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledIcon(ii);
			}
			// pressed icon
			{	BufferedImage icon = getIcon(name+ICON_PRESSED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setPressedIcon(ii);
			}
			// selected icon
			{	BufferedImage icon = getIcon(name+ICON_NORMAL_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setSelectedIcon(ii);
			}
			// disabled selected icon
			{	BufferedImage icon = getIcon(name+ICON_DISABLED_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setDisabledSelectedIcon(ii);
			}
			// rollover icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
				ImageIcon ii = new ImageIcon(icon);
				button.setRolloverEnabled(true);
				button.setRolloverIcon(ii);
			}
			// rollover selected icon
			{	BufferedImage icon = getIcon(name+ICON_ROLLOVER_SELECTED);
				double zoom = button.getPreferredSize().getHeight()/(double)icon.getHeight();
				icon = ImageTools.resize(icon,zoom*0.9,true);
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
		String tooltipKey = name+GuiTools.TOOLTIP;
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(tooltipKey);
		button.setToolTipText(tooltip);
	}		
	public static void initButton(AbstractButton result,String name, int width, int height, ButtonAware panel)
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
	public static JButton createPrincipalVerticalMenuButton(String name, ButtonAware panel)
	{	int width = sizes.get(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		// font
		int fontSize = sizes.get(MENU_VERTICAL_PRIMARY_BUTTON_FONT_SIZE);
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}
	public static JButton createSecondaryVerticalMenuButton(String name, ButtonAware panel)
	{	int width = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		// font
		int fontSize = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_FONT_SIZE);
		Font font = GuiConfiguration.getMiscConfiguration().getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}	
	public static JButton createHorizontalMenuButton(String name, ButtonAware panel)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JToggleButton createHorizontalMenuToggleButton(String name, ButtonAware panel)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
}
