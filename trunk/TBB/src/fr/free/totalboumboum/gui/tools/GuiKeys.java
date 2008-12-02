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
	/////////////////////////////////////////////////////////////////
	// MISC	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final String TOOLTIP = "Tooltip";
	
	public static final String DATA = "Data";
	public static final String HEADER = "Header";
	public static final String SCORE = "Score";
	public static final String PARTIAL = "Partial";
	public static final String GAME = "Game";
	public static final String DESCRIPTION = "Description";
	public static final String LIMIT = "Limit";
	public static final String TITLE = "Title";
	
	public static final String BOMBS = "Bombs";
	public static final String CROWNS = "Crowns";
	public static final String BOMBEDS = "Bombeds";
	public static final String ITEMS = "Items";
	public static final String BOMBINGS = "Bombings";
	public static final String PAINTINGS = "Paintings";
	public static final String TIME = "Time";

	public static final String CONFRONTATIONS = "Confrontations";
	public static final String CUSTOM = "Custom";
	public static final String LAST_STANDING = "LastStanding";

	public static final String TOURNAMENT = "Tournament";
	public static final String MATCH = "Match";
	public static final String ROUND = "Round";

	public static final String POINTS = "Points";
	public static final String RANKPOINTS = "Rankpoints";
	public static final String SHARE = "Share";
	public static final String NO_SHARE = "NoShare";
	public static final String DISCRETIZE = "Discretize";
	public static final String RANKINGS = "Rankings";
	public static final String INVERTED = "Inverted";
	public static final String REGULAR = "Regular";
	public static final String CONSTANT = "Constant";
	public static final String TOTAL = "Total";

	
	/////////////////////////////////////////////////////////////////
	// MENUS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* MAIN */ 
	public static final String MENU_MAIN_BUTTON = "MenuMainButton";
	public static final String MENU_MAIN_BUTTON_ABOUT = MENU_MAIN_BUTTON+"About";
	public static final String MENU_MAIN_BUTTON_OPTIONS = MENU_MAIN_BUTTON+"Options";
	public static final String MENU_MAIN_BUTTON_PROFILES = MENU_MAIN_BUTTON+"Profiles";
	public static final String MENU_MAIN_BUTTON_QUICKMATCH = MENU_MAIN_BUTTON+"QuickMatch";
	public static final String MENU_MAIN_BUTTON_RESOURCES = MENU_MAIN_BUTTON+"Resources";
	public static final String MENU_MAIN_BUTTON_STATISTICS = MENU_MAIN_BUTTON+"Statistics";
	public static final String MENU_MAIN_BUTTON_TOURNAMENT = MENU_MAIN_BUTTON+"Tournament";
	
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
				/* CLASS */
				public static final String MENU_RESOURCES_AI_SELECT_CLASS_PAGEDOWN = "MenuResourcesAiSelectClassPageDown";
				public static final String MENU_RESOURCES_AI_SELECT_CLASS_PAGEUP = "MenuResourcesAiSelectClassPageUp";
				public static final String MENU_RESOURCES_AI_SELECT_CLASS_PARENT = "MenuResourcesAiSelectClassParent";
				/* PACKAGES */
				public static final String MENU_RESOURCES_AI_SELECT_PACKAGE_PAGEDOWN = "MenuResourcesAiSelectPackagePageDown";
				public static final String MENU_RESOURCES_AI_SELECT_PACKAGE_PAGEUP = "MenuResourcesAiSelectPackagePageUp";
				/* PREVIEW */
				public static final String MENU_RESOURCES_AI_SELECT_PREVIEW_AUTHOR = "MenuResourcesAiSelectPreviewAuthor";
				public static final String MENU_RESOURCES_AI_SELECT_PREVIEW_NAME = "MenuResourcesAiSelectPreviewName";
				public static final String MENU_RESOURCES_AI_SELECT_PREVIEW_NOTES = "MenuResourcesAiSelectPreviewNotes";
				public static final String MENU_RESOURCES_AI_SELECT_PREVIEW_PACKAGE = "MenuResourcesAiSelectPreviewPackage";			
		/* HERO */
			/* BUTTON */
			public static final String MENU_RESOURCES_HERO_BUTTON = "MenuResourcesHeroButton";	
			public static final String MENU_RESOURCES_HERO_BUTTON_BACK = MENU_RESOURCES_HERO_BUTTON+"Back";	
			/* SELECT */
			public static final String MENU_RESOURCES_HERO_SELECT_TITLE = "MenuResourcesHeroSelectTitle";
				/* FOLDER */
				public static final String MENU_RESOURCES_HERO_SELECT_FOLDER_PAGEDOWN = "MenuResourcesHeroSelectFolderPageDown";
				public static final String MENU_RESOURCES_HERO_SELECT_FOLDER_PAGEUP = "MenuResourcesHeroSelectFolderPageUp";
				public static final String MENU_RESOURCES_HERO_SELECT_FOLDER_PARENT = "MenuResourcesHeroSelectFolderParent";
				/* PACKAGES */
				public static final String MENU_RESOURCES_HERO_SELECT_PACKAGE_PAGEDOWN = "MenuResourcesHeroSelectPackagePageDown";
				public static final String MENU_RESOURCES_HERO_SELECT_PACKAGE_PAGEUP = "MenuResourcesHeroSelectPackagePageUp";
				/* PREVIEW */
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_AUTHOR = "MenuResourcesHeroSelectPreviewAuthor";
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_COLORS = "MenuResourcesHeroSelectPreviewColors";
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_IMAGE = "MenuResourcesHeroSelectPreviewImage";
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_NAME = "MenuResourcesHeroSelectPreviewName";
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_PACKAGE = "MenuResourcesHeroSelectPreviewPackage";
				public static final String MENU_RESOURCES_HERO_SELECT_PREVIEW_SOURCE = "MenuResourcesHeroSelectPreviewSource";				
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
			/* BUTTON */
			public static final String MENU_RESOURCES_MATCH_BUTTON = "MenuResourcesMatchButton";	
			public static final String MENU_RESOURCES_MATCH_BUTTON_BACK = MENU_RESOURCES_MATCH_BUTTON+"Back";	
			/* SELECT */
			public static final String MENU_RESOURCES_MATCH_SELECT_TITLE = "MenuResourcesMatchSelectTitle";
				/* LIST */
				public static final String MENU_RESOURCES_MATCH_SELECT_LIST_PAGEDOWN = "MenuResourcesMatchSelectListPageDown";
				public static final String MENU_RESOURCES_MATCH_SELECT_LIST_PAGEUP = "MenuResourcesMatchSelectListPageUp";
				/* PREVIEW */
				public static final String MENU_RESOURCES_MATCH_SELECT_PREVIEW_AUTHOR = "MenuResourcesMatchSelectPreviewAuthor";
				public static final String MENU_RESOURCES_MATCH_SELECT_PREVIEW_NAME = "MenuResourcesMatchSelectPreviewName";
				public static final String MENU_RESOURCES_MATCH_SELECT_PREVIEW_PLAYERS = "MenuResourcesMatchSelectPreviewPlayers";
				public static final String MENU_RESOURCES_MATCH_SELECT_PREVIEW_ROUNDS = "MenuResourcesMatchSelectPreviewRounds";
		/* ROUND */
			/* BUTTON */
			public static final String MENU_RESOURCES_ROUND_BUTTON = "MenuResourcesRoundButton";	
			public static final String MENU_RESOURCES_ROUND_BUTTON_BACK = MENU_RESOURCES_ROUND_BUTTON+"Back";	
			/* SELECT */
			public static final String MENU_RESOURCES_ROUND_SELECT_TITLE = "MenuResourcesRoundSelectTitle";
				/* LIST */
				public static final String MENU_RESOURCES_ROUND_SELECT_LIST_PAGEDOWN = "MenuResourcesRoundSelectListPageDown";
				public static final String MENU_RESOURCES_ROUND_SELECT_LIST_PAGEUP = "MenuResourcesRoundSelectListPageUp";
				/* PREVIEW */
				public static final String MENU_RESOURCES_ROUND_SELECT_PREVIEW_AUTHOR = "MenuResourcesRoundSelectPreviewAuthor";
				public static final String MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_FOLDER = "MenuResourcesRoundSelectPreviewLevelFolder";
				public static final String MENU_RESOURCES_ROUND_SELECT_PREVIEW_LEVEL_PACK = "MenuResourcesRoundSelectPreviewLevelPack";
				public static final String MENU_RESOURCES_ROUND_SELECT_PREVIEW_NAME = "MenuResourcesRoundSelectPreviewName";
				public static final String MENU_RESOURCES_ROUND_SELECT_PREVIEW_PLAYERS = "MenuResourcesRoundSelectPreviewPlayers";
			
	/* OPTIONS */
		/* BUTTON */
		public static final String MENU_OPTIONS_BUTTON = "MenuOptionsButton";
		public static final String MENU_OPTIONS_BUTTON_ADVANCED = MENU_OPTIONS_BUTTON+"Advanced";
		public static final String MENU_OPTIONS_BUTTON_BACK = MENU_OPTIONS_BUTTON+"Back";
		public static final String MENU_OPTIONS_BUTTON_CANCEL = MENU_OPTIONS_BUTTON+"Cancel";
		public static final String MENU_OPTIONS_BUTTON_CONFIRM = MENU_OPTIONS_BUTTON+"Confirm";
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = MENU_OPTIONS_BUTTON+"Controls";
		public static final String MENU_OPTIONS_BUTTON_GAME = MENU_OPTIONS_BUTTON+"Game";
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
		/* GAME */
			/* BUTTON */
			public static final String MENU_OPTIONS_GAME = "MenuOptionsGame";
			public static final String MENU_OPTIONS_GAME_BUTTON = MENU_OPTIONS_GAME+"Button";
			public static final String MENU_OPTIONS_GAME_BUTTON_QUICKSTART = MENU_OPTIONS_GAME_BUTTON+"Quickstart";
			public static final String MENU_OPTIONS_GAME_BUTTON_QUICKMATCH = MENU_OPTIONS_GAME_BUTTON+"Quickmatch";
			public static final String MENU_OPTIONS_GAME_BUTTON_TOURNAMENT = MENU_OPTIONS_GAME_BUTTON+"Tournament";
			/* QUICK START */
			public static final String MENU_OPTIONS_GAME_QUICKSTART = MENU_OPTIONS_GAME+"Quickstart";
			public static final String MENU_OPTIONS_GAME_QUICKSTART_TITLE = MENU_OPTIONS_GAME_QUICKSTART+"Title";
				/* ROUND */
				public static final String MENU_OPTIONS_GAME_QUICKSTART_ROUND = MENU_OPTIONS_GAME_QUICKSTART+"Round";
				public static final String MENU_OPTIONS_GAME_QUICKSTART_ROUND_BROWSE = MENU_OPTIONS_GAME_QUICKSTART_ROUND+"Browse";
				/* PLAYERS */
				public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS = MENU_OPTIONS_GAME_QUICKSTART+"Players";
				public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TITLE = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"Title";
				public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_CONTROLS = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"Controls";
				public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_COLOR = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"Color";
					/* HERO */
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_HEADER = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"HeroHeader";
						/* BUTTON */
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_BUTTON = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"HeroButton";
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_BUTTON_CANCEL = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_BUTTON+"Cancel";
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_BUTTON_CONFIRM = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_HERO_BUTTON+"Confirm";
					/* PROFILE */
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_ADD = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"ProfileAdd";
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_DELETE = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"ProfileDelete";
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_HEADER = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"ProfileHeader";
						/* BUTTON */
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_BUTTON = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"ProfileButton";
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_BUTTON_CANCEL = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_BUTTON+"Cancel";
						public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_BUTTON_CONFIRM = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_PROFILE_BUTTON+"Confirm";
					/* TYPE */
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_COMPUTER = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"TypeComputer";
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HEADER = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"TypeHeader";
					public static final String MENU_OPTIONS_GAME_QUICKSTART_PLAYERS_TYPE_HUMAN = MENU_OPTIONS_GAME_QUICKSTART_PLAYERS+"TypeHuman";
			/* QUICK MATCH */
			public static final String MENU_OPTIONS_GAME_QUICKMATCH_TITLE = "MenuOptionsGameQuickmatchTitle";
			/* TOURNAMENT */
			public static final String MENU_OPTIONS_GAME_TOURNAMENT_TITLE = "MenuOptionsGameTournamentTitle";
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
		public static final String MENU_QUICKMATCH_BUTTON_NEXT = MENU_QUICKMATCH_BUTTON+"Next";
		public static final String MENU_QUICKMATCH_BUTTON_PREVIOUS = MENU_QUICKMATCH_BUTTON+"Previous";
		public static final String MENU_QUICKMATCH_BUTTON_QUIT = MENU_QUICKMATCH_BUTTON+"Quit";
		/* PLAYERS */
		public static final String MENU_QUICKMATCH_PLAYERS = MENU_QUICKMATCH+"Players";
		public static final String MENU_QUICKMATCH_PLAYERS_TITLE = MENU_QUICKMATCH_PLAYERS+"Title";
		public static final String MENU_QUICKMATCH_PLAYERS_CONTROLS = MENU_QUICKMATCH_PLAYERS+"Controls";
		public static final String MENU_QUICKMATCH_PLAYERS_COLOR = MENU_QUICKMATCH_PLAYERS+"Color";
			/* HERO */
			public static final String MENU_QUICKMATCH_PLAYERS_HERO_HEADER = MENU_QUICKMATCH_PLAYERS+"HeroHeader";
				/* BUTTON */
				public static final String MENU_QUICKMATCH_PLAYERS_HERO_BUTTON = MENU_QUICKMATCH_PLAYERS+"HeroButton";
				public static final String MENU_QUICKMATCH_PLAYERS_HERO_BUTTON_CANCEL = MENU_QUICKMATCH_PLAYERS_HERO_BUTTON+"Cancel";
				public static final String MENU_QUICKMATCH_PLAYERS_HERO_BUTTON_CONFIRM = MENU_QUICKMATCH_PLAYERS_HERO_BUTTON+"Confirm";
			/* PROFILE */
			public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_ADD = MENU_QUICKMATCH_PLAYERS+"ProfileAdd";
			public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_DELETE = MENU_QUICKMATCH_PLAYERS+"ProfileDelete";
			public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_HEADER = MENU_QUICKMATCH_PLAYERS+"ProfileHeader";
				/* BUTTON */
				public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_BUTTON = MENU_QUICKMATCH_PLAYERS+"ProfileButton";
				public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_BUTTON_CANCEL = MENU_QUICKMATCH_PLAYERS_PROFILE_BUTTON+"Cancel";
				public static final String MENU_QUICKMATCH_PLAYERS_PROFILE_BUTTON_CONFIRM = MENU_QUICKMATCH_PLAYERS_PROFILE_BUTTON+"Confirm";
			/* TYPE */
			public static final String MENU_QUICKMATCH_PLAYERS_TYPE_COMPUTER = MENU_QUICKMATCH_PLAYERS+"TypeComputer";
			public static final String MENU_QUICKMATCH_PLAYERS_TYPE_HEADER = MENU_QUICKMATCH_PLAYERS+"TypeHeader";
			public static final String MENU_QUICKMATCH_PLAYERS_TYPE_HUMAN = MENU_QUICKMATCH_PLAYERS+"TypeHuman";
		
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
	/* RESULTS */
	public static final String GAME_TOURNAMENT_RESULTS_TITLE = "GameTournamentResultsTitle";
		/* HEADER */
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_BOMBS = "GameTournamentResultsHeaderBombs";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CROWNS = "GameTournamentResultsHeaderCrowns";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_LIMIT = "GameTournamentResultsHeaderCustomLimit";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_POINTS = "GameTournamentResultsHeaderCustomPoints";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_BOMBEDS = "GameTournamentResultsHeaderBombeds";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_ITEMS = "GameTournamentResultsHeaderItems";
		public static final String GAME_TOURNAMENT_RESULTS_HEADER_BOMBINGS = "GameTournamentResultsHeaderBombings";
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
	/* RESULTS */
	public static final String GAME_MATCH_RESULTS_TITLE = "GameMatchResultsTitle";
		/* HEADER */
		public static final String GAME_MATCH_RESULTS_HEADER_BOMBS = "GameMatchResultsHeaderBombs";
		public static final String GAME_MATCH_RESULTS_HEADER_CROWNS = "GameMatchResultsHeaderCrowns";
		public static final String GAME_MATCH_RESULTS_HEADER_CUSTOM_LIMIT = "GameMatchResultsHeaderCustomLimit";
		public static final String GAME_MATCH_RESULTS_HEADER_CUSTOM_POINTS = "GameMatchResultsHeaderCustomPoints";
		public static final String GAME_MATCH_RESULTS_HEADER_BOMBEDS = "GameMatchResultsHeaderBombeds";
		public static final String GAME_MATCH_RESULTS_HEADER_ITEMS = "GameMatchResultsHeaderItems";
		public static final String GAME_MATCH_RESULTS_HEADER_BOMBINGS = "GameMatchResultsHeaderBombings";
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
	public static final String GAME_ROUND_PROGRESSBAR_COMPLETE = "GameRoundProgressbarComplete";
	public static final String GAME_ROUND_PROGRESSBAR_ITEMSET = "GameRoundProgressbarItemset";
	public static final String GAME_ROUND_PROGRESSBAR_PLAYER = "GameRoundProgressbarPlayer";
	public static final String GAME_ROUND_PROGRESSBAR_THEME = "GameRoundProgressbarTheme";
	/* DESCRIPTION */
	public static final String GAME_ROUND_DESCRIPTION_TITLE = "GameRoundDescriptionTitle";
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
		/* PREVIEW */
		public static final String GAME_ROUND_DESCRIPTION_PREVIEW_TITLE = "GameRoundDescriptionPreviewTitle";
	/* RESULTS */
	public static final String GAME_ROUND_RESULTS_TITLE = "GameRoundResultsTitle";
		/* HEADER */
		public static final String GAME_ROUND_RESULTS_HEADER_BOMBS = "GameRoundResultsHeaderBombs";
		public static final String GAME_ROUND_RESULTS_HEADER_CROWNS = "GameRoundResultsHeaderCrowns";
		public static final String GAME_ROUND_RESULTS_HEADER_CUSTOM_LIMIT = "GameRoundResultsHeaderCustomLimit";
		public static final String GAME_ROUND_RESULTS_HEADER_CUSTOM_POINTS = "GameRoundResultsHeaderCustomPoints";
		public static final String GAME_ROUND_RESULTS_HEADER_BOMBEDS = "GameRoundResultsHeaderBombeds";
		public static final String GAME_ROUND_RESULTS_HEADER_ITEMS = "GameRoundResultsHeaderItems";
		public static final String GAME_ROUND_RESULTS_HEADER_BOMBINGS = "GameRoundResultsHeaderBombings";
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
	// COMMON CONTENT	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static final String COMMON = "Common";
	/* COLORS */
		public static final String COLOR = "Color";
		public static final String COMMON_COLOR = COMMON+COLOR;
		public static final String BLACK = "Black";
		public static final String BLUE = "Blue";
		public static final String BROWN = "Brown";
		public static final String CYAN = "Cyan";
		public static final String GRASS = "Grass";
		public static final String GREEN = "Green";
		public static final String GREY = "Grey";
		public static final String INDIGO = "Indigo";
		public static final String ORANGE = "Orange";
		public static final String PINK = "Pink";
		public static final String PURPLE = "Purple";
		public static final String RED = "Red";
		public static final String RUST = "Rust";
		public static final String ULTRAMARINE = "Ultramarine";
		public static final String WHITE = "White";
		public static final String YELLOW = "Yellow";
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
	/* ITEMS */
	public static final String COMMON_ITEMS = COMMON+ITEMS;
		/* AVAILABLE */
		public static final String AVAILABLE = "Available";
		public static final String COMMON_ITEMS_AVAILABLE = COMMON_ITEMS+AVAILABLE;
		public static final String COMMON_ITEMS_AVAILABLE_TITLE = COMMON_ITEMS_AVAILABLE+TITLE;
		/* INITIAL */
		public static final String INITIAL = "Initial";
		public static final String COMMON_ITEMS_INITIAL = COMMON_ITEMS+INITIAL;
		public static final String COMMON_ITEMS_INITIAL_TITLE = COMMON_ITEMS_INITIAL+TITLE;


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