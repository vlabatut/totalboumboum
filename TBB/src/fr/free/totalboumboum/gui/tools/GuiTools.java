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
import java.awt.Toolkit;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.common.ButtonAware;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class GuiTools
{	
	public static final String TOOLTIP = "Tooltip";
	
	/////////////////////////////////////////////////////////////////
	// MENUS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/* MAIN */ 
	public static final String MENU_MAIN_BUTTON_HEROES = "MenuMainButtonHeroes";
	public static final String MENU_MAIN_BUTTON_LEVELS = "MenuMainButtonLevels";
	public static final String MENU_MAIN_BUTTON_OPTIONS = "MenuMainButtonOptions";
	public static final String MENU_MAIN_BUTTON_PROFILES = "MenuMainButtonProfiles";
	public static final String MENU_MAIN_BUTTON_QUICKMATCH = "MenuMainButtonQuickMatch";
	public static final String MENU_MAIN_BUTTON_STATISTICS = "MenuMainButtonStatistics";
	public static final String MAIN_MENU_BUTTON_TOURNAMENT = "MenuMainButtonTournament";
	/* OPTIONS */
		/* BUTTON */
		public static final String MENU_OPTIONS_BUTTON_BACK = "MenuOptionsButtonBack";
		public static final String MENU_OPTIONS_BUTTON_CONTROLS = "MenuOptionsButtonControls";
		public static final String MENU_OPTIONS_BUTTON_GAMEPLAY = "MenuOptionsButtonGameplay";
		public static final String MENU_OPTIONS_BUTTON_VIDEO = "MenuOptionsButtonVideo";
		/* CONTROLS */
		public static final String MENU_OPTIONS_CONTROLS_TITLE = "MenuOptionsControlsTitle";
		/* GAMEPLAY */
		public static final String MENU_OPTIONS_GAMEPLAY_TITLE = "MenuOptionsGameplayTitle";
		/* VIDEO */
		public static final String MENU_OPTIONS_VIDEO_TITLE = "MenuOptionsVideoTitle";
	/* TOURNAMENT */	
	public static final String MENU_TOURNAMENT_BUTTON_BACK = "MenuTournamentButtonBack";
	public static final String MENU_TOURNAMENT_BUTTON_CONTINUE = "MenuTournamentButtonContinue";
	public static final String MENU_TOURNAMENT_BUTTON_LOAD = "MenuTournamentButtonLoad";
	public static final String MENU_TOURNAMENT_BUTTON_NEW = "MenuTournamentButtonNew";
	public static final String MENU_TOURNAMENT_BUTTON_PLAYERS = "MenuTournamentButtonPlayers";
	public static final String MENU_TOURNAMENT_BUTTON_RULES = "MenuTournamentButtonRules";
	public static final String MENU_TOURNAMENT_BUTTON_SAVE_AS = "MenuTournamentButtonSaveAs";
	public static final String MENU_TOURNAMENT_BUTTON_START = "MenuTournamentButtonStart";

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
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME = "GameMatchDescriptionPlayersHeaderName";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK = "GameMatchDescriptionPlayersHeaderRank";
			/* DATA */
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_COMPUTER = "GameMatchDescriptionPlayersDataComputer";
			public static final String GAME_MATCH_DESCRIPTION_PLAYERS_DATA_HUMAN = "GameMatchDescriptionPlayersDataHuman";
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
		public static final String GAME_ROUND_RESULTS_HEADER_POINTS = "GameRoundResultsHeaderPoints";
		public static final String GAME_ROUND_RESULTS_HEADER_TIME = "GameRoundResultsHeaderTime";
		/* DATA */
		public static final String GAME_ROUND_RESULTS_DATA_COMPUTER = "GameRoundResultsDataComputer";
		public static final String GAME_ROUND_RESULTS_DATA_HUMAN = "GameRoundResultsDataHuman";
	/* STATISTICS */
	public static final String GAME_ROUND_STATISTICS_TITLE = "GameRoundStatisticsTitle";
		/* HEADER */
		public static final String GAME_ROUND_STATISTICS_HEADER_NAME = "GameRoundStatisticsHeaderName";
	
	
	
	
	
	
	// colors
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,200);
	public final static Color COLOR_TITLE_FOREGROUND = Color.BLACK;
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
	
	private static Graphics graphics;
	
	public static void setGraphics(Graphics g)
	{	graphics = g;	
	}

	public static void init(GuiConfiguration configuration, Graphics g)
	{	// init
		Dimension panelDimension = configuration.getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
		setGraphics(g);
		setGameFont(configuration.getFont());
		
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
		{	Iterator<Entry<String,String>> it = configuration.getLanguage().getTexts().entrySet().iterator();
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
		{	Iterator<Entry<String,String>> it = configuration.getLanguage().getTexts().entrySet().iterator();
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
		
		// images
		BufferedImage absent = ImageTools.getAbsentImage(64,64);
		BufferedImage image;
		// buttons
		{	String[] buttonStates = {ICON_NORMAL,ICON_NORMAL_SELECTED,
					ICON_DISABLED,ICON_DISABLED_SELECTED,
					ICON_ROLLOVER,ICON_ROLLOVER_SELECTED,
					ICON_PRESSED};
			//
			String baseFolder = GuiFileTools.getButtonsPath()+File.separator;
			{	String folder = baseFolder+GuiFileTools.FOLDER_DESCRIPTION+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_DESCRIPTION+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_DESCRIPTION+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_DESCRIPTION+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_BLUE+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_MENU+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_CURRENT_TOURNAMENT+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_CURRENT_MATCH+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_LEFT_RED+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_FINISH+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_FINISH+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_FINISH+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_PLAY+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_ROUND_BUTTON_PLAY+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_HOME+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_QUIT+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_QUIT+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_QUIT+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_RESULTS+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_RESULTS+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_RESULTS+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_RESULTS+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_BLUE+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_RIGHT_RED+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_CURRENT_MATCH+buttonStates[i],image);
					icons.put(GAME_TOURNAMENT_BUTTON_NEXT_MATCH+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_CURRENT_ROUND+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_NEXT_ROUND+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+GuiFileTools.FOLDER_STATS+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(GAME_TOURNAMENT_BUTTON_STATISTICS+buttonStates[i],image);
					icons.put(GAME_MATCH_BUTTON_STATISTICS+buttonStates[i],image);
					icons.put(GAME_ROUND_BUTTON_STATISTICS+buttonStates[i],image);
				}
			}
		}
		
		// header icons
		{	String folder = GuiFileTools.getHeadersPath()+File.separator;
			// author
			image = loadIcon(folder+GuiFileTools.FILE_AUTHOR,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_AUTHOR,image);
			// bombs
			image = loadIcon(folder+GuiFileTools.FILE_BOMBS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_BOMBS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_BOMBS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_BOMBS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_BOMBS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_BOMBS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_BOMBS,image);
			// confrontations
			image = loadIcon(folder+GuiFileTools.FILE_CONFRONTATIONS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CONFRONTATIONS,image);
			// constant
			image = loadIcon(folder+GuiFileTools.FILE_CONSTANT,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_CONSTANT,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_CONSTANT,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_CONSTANT,image);
			// crowns
			image = loadIcon(folder+GuiFileTools.FILE_CROWNS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_CROWNS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_CROWNS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_CROWNS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CROWNS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CROWNS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CROWNS,image);
			// custom points
			image = loadIcon(folder+GuiFileTools.FILE_CONFRONTATIONS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_CUSTOM,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_CUSTOM,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_CUSTOM,image);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_LIMIT,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_CUSTOM_LIMIT,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_CUSTOM_LIMIT,image);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_CUSTOM_POINTS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_CUSTOM_POINTS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_CUSTOM_POINTS,image);
			// deaths
			image = loadIcon(folder+GuiFileTools.FILE_DEATHS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_DEATHS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_DEATHS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_DEATHS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_DEATHS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_DEATHS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_DEATHS,image);
			// dimension
			image = loadIcon(folder+GuiFileTools.FILE_DIMENSION,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_DIMENSION,image);
			// constant
			image = loadIcon(folder+GuiFileTools.FILE_DISCRETIZE,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_DISCRETIZE,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_DISCRETIZE,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_DISCRETIZE,image);
			// initial
			image = loadIcon(folder+GuiFileTools.FILE_INITIAL,absent);
			icons.put(GAME_ROUND_DESCRIPTION_INITIALITEMS_TITLE,image);
			// instance
			image = loadIcon(folder+GuiFileTools.FILE_INSTANCE,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_INSTANCE,image);
			// items
			image = loadIcon(folder+GuiFileTools.FILE_ITEMS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_ITEMS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_ITEMS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_ITEMS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_ITEMS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_ITEMS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_ITEMS,image);
			icons.put(GAME_ROUND_DESCRIPTION_ITEMSET_TITLE,image);
			// kills
			image = loadIcon(folder+GuiFileTools.FILE_KILLS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_KILLS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_KILLS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_KILLS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_KILLS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_KILLS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_KILLS,image);
			// limits
			image = loadIcon(folder+GuiFileTools.FILE_LIMITS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_TITLE,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_TITLE,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_TITLE,image);
			// misc
			image = loadIcon(folder+GuiFileTools.FILE_MISC,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_MISC_TITLE,image);
			icons.put(GAME_MATCH_DESCRIPTION_MISC_TITLE,image);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_TITLE,image);
			// name
			image = loadIcon(folder+GuiFileTools.FILE_NAME,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_NAME,image);
			icons.put(GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_NAME,image);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_NAME,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_NAME,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_NAME,image);
			icons.put(GAME_TOURNAMENT_STATISTICS_HEADER_NAME,image);
			icons.put(GAME_MATCH_STATISTICS_HEADER_NAME,image);
			icons.put(GAME_ROUND_STATISTICS_HEADER_NAME,image);
			// pack
			image = loadIcon(folder+GuiFileTools.FILE_PACK,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_PACK,image);
			// paintings
			image = loadIcon(folder+GuiFileTools.FILE_PAINTINGS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_PAINTINGS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_PAINTINGS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_PAINTINGS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_PAINTINGS,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_PAINTINGS,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_PAINTINGS,image);
			// partial
			image = loadIcon(folder+GuiFileTools.FILE_PARTIAL,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_PARTIAL,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_PARTIAL,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_PARTIAL,image);
			// points
			image = loadIcon(folder+GuiFileTools.FILE_POINTS,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_POINTS,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_POINTS,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_POINTS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_TITLE,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_TITLE,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_TITLE,image);
			// rank
			image = loadIcon(folder+GuiFileTools.FILE_RANK,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_PLAYERS_HEADER_RANK,image);
			icons.put(GAME_MATCH_DESCRIPTION_PLAYERS_HEADER_RANK,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKINGS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKINGS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKINGS,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_RANKPOINTS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_RANKPOINTS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_RANKPOINTS,image);
			// score
			image = loadIcon(folder+GuiFileTools.FILE_SCORE,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_SCORE,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_SCORE,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_HEADER_SCORE,image);
			// source
			image = loadIcon(folder+GuiFileTools.FILE_SOURCE,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_SOURCE,image);
			// theme
			image = loadIcon(folder+GuiFileTools.FILE_THEME,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_THEME,image);
			// time
			image = loadIcon(folder+GuiFileTools.FILE_TIME,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_TIME,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_TIME,image);
			icons.put(GAME_ROUND_RESULTS_HEADER_TIME,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TIME,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TIME,image);
			icons.put(GAME_ROUND_DESCRIPTION_LIMIT_HEADER_TIME,image);
			// title
			image = loadIcon(folder+GuiFileTools.FILE_TITLE,absent);
			icons.put(GAME_ROUND_DESCRIPTION_MISC_HEADER_TITLE,image);
			// total
			image = loadIcon(folder+GuiFileTools.FILE_TOTAL,absent);
			icons.put(GAME_TOURNAMENT_RESULTS_HEADER_TOTAL,image);
			icons.put(GAME_MATCH_RESULTS_HEADER_TOTAL,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_LIMIT_HEADER_TOTAL,image);
			icons.put(GAME_MATCH_DESCRIPTION_LIMIT_HEADER_TOTAL,image);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_HEADER_TOTAL,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_HEADER_TOTAL,image);
		}			
		// data icons
		{	String folder = GuiFileTools.getDataPath()+File.separator;
			// bombs
			image = loadIcon(folder+GuiFileTools.FILE_BOMBS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_BOMBS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_BOMBS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_BOMBS,image);
			// computer
			image = loadIcon(folder+GuiFileTools.FILE_COMPUTER,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_COMPUTER,image);
			icons.put(GAME_MATCH_DESCRIPTION_PLAYERS_DATA_COMPUTER,image);
			icons.put(GAME_TOURNAMENT_RESULTS_DATA_COMPUTER,image);
			icons.put(GAME_MATCH_RESULTS_DATA_COMPUTER,image);
			icons.put(GAME_ROUND_RESULTS_DATA_COMPUTER,image);
			// crowns
			image = loadIcon(folder+GuiFileTools.FILE_CROWNS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_CROWNS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_CROWNS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_CROWNS,image);
			// deaths
			image = loadIcon(folder+GuiFileTools.FILE_DEATHS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_DEATHS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_DEATHS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_DEATHS,image);
			// human
			image = loadIcon(folder+GuiFileTools.FILE_HUMAN,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_PLAYERS_DATA_HUMAN,image);
			icons.put(GAME_MATCH_DESCRIPTION_PLAYERS_DATA_HUMAN,image);
			icons.put(GAME_TOURNAMENT_RESULTS_DATA_HUMAN,image);
			icons.put(GAME_MATCH_RESULTS_DATA_HUMAN,image);
			icons.put(GAME_ROUND_RESULTS_DATA_HUMAN,image);
			// inverted order
			image = loadIcon(folder+GuiFileTools.FILE_INVERTED,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_INVERTED,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_INVERTED,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_INVERTED,image);
			// items
			image = loadIcon(folder+GuiFileTools.FILE_ITEMS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_ITEMS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_ITEMS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_ITEMS,image);
			// kills
			image = loadIcon(folder+GuiFileTools.FILE_KILLS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_KILLS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_KILLS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_KILLS,image);
			// no share
			image = loadIcon(folder+GuiFileTools.FILE_NOSHARE,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_NOSHARE,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_NOSHARE,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_NOSHARE,image);
			// paintings
			image = loadIcon(folder+GuiFileTools.FILE_PAINTINGS,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PAINTINGS,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_PAINTINGS,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_PAINTINGS,image);
			// partial
			image = loadIcon(folder+GuiFileTools.FILE_PARTIAL,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_PARTIAL,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_PARTIAL,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_PARTIAL,image);
			// regular order
			image = loadIcon(folder+GuiFileTools.FILE_REGULAR,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_REGULAR,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_REGULAR,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_REGULAR,image);
			// share
			image = loadIcon(folder+GuiFileTools.FILE_SHARE,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_SHARE,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_SHARE,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_SHARE,image);
			// time
			image = loadIcon(folder+GuiFileTools.FILE_TIME,absent);
			icons.put(GAME_TOURNAMENT_DESCRIPTION_POINTS_DATA_TIME,image);
			icons.put(GAME_MATCH_DESCRIPTION_POINTS_DATA_TIME,image);
			icons.put(GAME_ROUND_DESCRIPTION_POINTS_DATA_TIME,image);
		}
	}
	
	
	public static int getSize(String key)
	{	int result = -1;
		Integer temp = sizes.get(key);
		if(temp!=null)
			result = temp.intValue();
		return result;
	}
	public static BufferedImage getIcon(String key)
	{	BufferedImage result;
		BufferedImage temp = icons.get(key);
		if(temp==null)
			result = ImageTools.getAbsentImage(64,64);
		else
			result = temp;
		return result;
	}
	
	public static Font gameFont;
	
	public static void setGameFont(Font gameFont)
	{	GuiTools.gameFont = gameFont;	
	}
	
	public static int getFontSize(double limit)
	{	int result = 0;
		int fheight;
		do
		{	result++;
			Font font = gameFont.deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<limit);
		return result;
	}
	public static int getFontSize(double width, double height, String text)
	{	int result = 0;
		int fheight,fwidth;
		do
		{	result++;
			Font font = gameFont.deriveFont((float)result);
			graphics.setFont(font);
			FontMetrics metrics = graphics.getFontMetrics(font);
			fheight = metrics.getHeight();
			Rectangle2D bounds = metrics.getStringBounds(text,graphics);
			fwidth = (int)bounds.getWidth();
		}
		while(fheight<height && fwidth<width);
		return result-1;
	}
	public static int getPixelHeight(float fontSize)
	{	int result;
		Font font = gameFont.deriveFont(fontSize);
		graphics.setFont(font);
		FontMetrics metrics = graphics.getFontMetrics(font);
		result = (int)(metrics.getHeight()*1.2);
		return result;
	}

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	public static void setButtonContent(String name, AbstractButton button, GuiConfiguration configuration)
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
			String text = configuration.getLanguage().getText(name);
			button.setText(text);
		}		
		// action command
		button.setActionCommand(name);
		// tooltip
		String toolTip = name+GuiTools.TOOLTIP;
		String text = configuration.getLanguage().getText(toolTip);
		button.setToolTipText(text);
	}		
	public static void initButton(AbstractButton result,String name, int width, int height, ButtonAware panel, GuiConfiguration configuration)
	{	// dimension
		Dimension dim = new Dimension(width,height);
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// set text
		setButtonContent(name,result,configuration);
		// add to panel
		panel.add(result);
		result.addActionListener(panel);
	}
	public static JButton createPrincipalVerticalMenuButton(String name, ButtonAware panel, GuiConfiguration configuration)
	{	int width = sizes.get(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		// font
		int fontSize = sizes.get(MENU_VERTICAL_PRIMARY_BUTTON_FONT_SIZE);
		Font font = configuration.getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}
	public static JButton createSecondaryVerticalMenuButton(String name, ButtonAware panel, GuiConfiguration configuration)
	{	int width = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		// font
		int fontSize = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_FONT_SIZE);
		Font font = configuration.getFont().deriveFont((float)fontSize);
		result.setFont(font);
		//
		return result;
	}	
	public static JButton createHorizontalMenuButton(String name, ButtonAware panel, GuiConfiguration configuration)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
	public static JToggleButton createHorizontalMenuToggleButton(String name, ButtonAware panel, GuiConfiguration configuration)
	{	int width = sizes.get(MENU_HORIZONTAL_BUTTON_WIDTH);
		int height = sizes.get(MENU_HORIZONTAL_BUTTON_HEIGHT);
		JToggleButton result = new JToggleButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		return result;
	}
}
