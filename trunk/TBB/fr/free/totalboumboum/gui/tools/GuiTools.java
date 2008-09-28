package fr.free.totalboumboum.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.data.configuration.GuiConfiguration;
import fr.free.totalboumboum.gui.generic.ButtonAware;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;

public class GuiTools
{	
	// names
	// MAIN MENU
	public static final String MAIN_MENU_BUTTON_HEROES = "MainMenuButtonHeroes";
	public static final String MAIN_MENU_BUTTON_LEVELS = "MainMenuButtonLevels";
	public static final String MAIN_MENU_BUTTON_OPTIONS = "MainMenuButtonOptions";
	public static final String MAIN_MENU_BUTTON_PROFILES = "MainMenuButtonProfiles";
	public static final String MAIN_MENU_BUTTON_QUICKMATCH = "MainMenuButtonQuickMatch";
	public static final String MAIN_MENU_BUTTON_STATISTICS = "MainMenuButtonStatistics";
	public static final String MAIN_MENU_BUTTON_TOURNAMENT = "MainMenuButtonTournament";
	
	// OPTIONS MENU
	public static final String OPTIONS_MENU_BUTTON_BACK = "OptionMenuButtonBack";
	public static final String OPTIONS_MENU_BUTTON_GAMEPLAY = "OptionMenuButtonGamePlay";
	public static final String OPTIONS_MENU_BUTTON_VIDEO = "OptionMenuButtonVideo";

	// TOURNAMENT MENU	
	public static final String TOURNAMENT_MENU_BUTTON_BACK = "TournamentMenuButtonBack";
	public static final String TOURNAMENT_MENU_BUTTON_CONTINUE = "TournamentMenuButtonContinue";
	public static final String TOURNAMENT_MENU_BUTTON_LOAD = "TournamentMenuButtonLoad";
	public static final String TOURNAMENT_MENU_BUTTON_NEW = "TournamentMenuButtonNew";
	public static final String TOURNAMENT_MENU_BUTTON_PLAYERS = "TournamentMenuButtonPlayers";
	public static final String TOURNAMENT_MENU_BUTTON_RULES = "TournamentMenuButtonRules";
	public static final String TOURNAMENT_MENU_BUTTON_SAVE_AS = "TournamentMenuButtonSaveAs";
	public static final String TOURNAMENT_MENU_BUTTON_START = "TournamentMenuButtonStart";

	// TOURNAMENT PANEL
	public static final String TOURNAMENT_BUTTON_QUIT = "TournamentButtonQuit";
	public static final String TOURNAMENT_BUTTON_CURRENT_MATCH = "TournamentButtonCurrentMatch";
	public static final String TOURNAMENT_BUTTON_DESCRIPTION = "TournamentButtonDescription";
	public static final String TOURNAMENT_BUTTON_FINISH = "TournamentButtonFinish";
	public static final String TOURNAMENT_BUTTON_MENU = "TournamentButtonMenu";
	public static final String TOURNAMENT_BUTTON_NEXT_MATCH = "TournamentButtonNextMatch";
	public static final String TOURNAMENT_BUTTON_RESULTS = "TournamentButtonResults";
	public static final String TOURNAMENT_BUTTON_STATISTICS = "TournamentButtonStatistics";
	
	// MATCH PANEL
	public static final String MATCH_BUTTON_QUIT = "MatchButtonQuit";
	public static final String MATCH_BUTTON_CURRENT_ROUND = "MatchButtonCurrentRound";
	public static final String MATCH_BUTTON_CURRENT_TOURNAMENT = "MatchButtonCurrentTournament";
	public static final String MATCH_BUTTON_DESCRIPTION = "MatchButtonDescription";
	public static final String MATCH_BUTTON_FINISH = "MatchButtonFinish";
	public static final String MATCH_BUTTON_NEXT_ROUND = "MatchButtonNextRound";
	public static final String MATCH_BUTTON_RESULTS = "MatchButtonResults";
	public static final String MATCH_BUTTON_STATISTICS = "MatchButtonStatistics";
	
	// ROUND PANEL
	public static final String ROUND_BUTTON_QUIT = "RoundButtonQuit";
	public static final String ROUND_BUTTON_CURRENT_MATCH = "RoundButtonCurrentMatch";
	public static final String ROUND_BUTTON_DESCRIPTION = "RoundButtonDescription";
	public static final String ROUND_BUTTON_FINISH = "RoundButtonFinish";
	public static final String ROUND_BUTTON_PLAY = "RoundButtonPlay";
	public static final String ROUND_BUTTON_RESULTS = "RoundButtonResults";
	public static final String ROUND_BUTTON_STATISTICS = "RoundButtonStatistics";
	public static final String ROUND_PROGRESSBAR_BOMBSET = "RoundProgressbarBombset";
	public static final String ROUND_PROGRESSBAR_COMMON = "RoundProgressbarCommon";
	public static final String ROUND_PROGRESSBAR_COMPLETE = "RoundProgressbarComplete";
	public static final String ROUND_PROGRESSBAR_ITEMSET = "RoundProgressbarItemset";
	public static final String ROUND_PROGRESSBAR_PLAYER = "RoundProgressbarPlayer";
	public static final String ROUND_PROGRESSBAR_THEME = "RoundProgressbarTheme";
	public static final String ROUND_PROGRESSBAR_ZONE = "RoundProgressbarZone";
	
	// GAME PANEL
	public static final String GAME_TOURNAMENT_TITLE_DESCRIPTION = "GameTournamentTitleDescription";
	public static final String GAME_TOURNAMENT_TITLE_RESULTS = "GameTournamentTitleResults";
	public static final String GAME_TOURNAMENT_TITLE_STATISTICS = "GameTournamentTitleStatistics";
	public static final String GAME_TOURNAMENT_HEADER_NAME = "GameTournamentHeaderName";
	public static final String GAME_TOURNAMENT_HEADER_POINTS = "GameTournamentHeaderPoints";
	public static final String GAME_TOURNAMENT_HEADER_BOMBS = "GameTournamentHeaderBombs";
	public static final String GAME_TOURNAMENT_HEADER_DEATHS = "GameTournamentHeaderDeaths";
	public static final String GAME_TOURNAMENT_HEADER_ITEMS = "GameTournamentHeaderItems";
	public static final String GAME_TOURNAMENT_HEADER_KILLS = "GameTournamentHeaderKills";
	public static final String GAME_TOURNAMENT_HEADER_RANK = "GameTournamentHeaderRank";
	public static final String GAME_TOURNAMENT_HEADER_TOTAL = "GameTournamentHeaderTotal";
	public static final String GAME_TOURNAMENT_HEADER_MATCH = "GameTournamentHeaderMatch";
	public static final String GAME_TOURNAMENT_HEADER_NOTES = "GameTournamentHeaderNotes";	
	public static final String GAME_TOURNAMENT_HEADER_LIMITS = "GameTournamentHeaderLimits";	
	public static final String GAME_TOURNAMENT_HEADER_POINTSPROCESS = "GameTournamentHeaderPointprocess";	
	public static final String GAME_TOURNAMENT_LIMIT_CONFRONTATIONS = "GameTournamentLimitConfrontations";
	public static final String GAME_TOURNAMENT_LIMIT_POINTS = "GameTournamentLimitPoints";
	public static final String GAME_TOURNAMENT_LIMIT_TOTAL = "GameTournamentLimitTotal";
	public static final String GAME_TOURNAMENT_LIMIT_BOMBS = "GameTournamentLimitBombs";
	public static final String GAME_TOURNAMENT_LIMIT_DEATHS = "GameTournamentLimitDeaths";
	public static final String GAME_TOURNAMENT_LIMIT_ITEMS = "GameTournamentLimitItems";
	public static final String GAME_TOURNAMENT_LIMIT_KILLS = "GameTournamentLimitKills";
	
	public static final String GAME_MATCH_TITLE_DESCRIPTION = "GameMatchTitleDescription";
	public static final String GAME_MATCH_TITLE_RESULTS = "GameMatchTitleResults";
	public static final String GAME_MATCH_TITLE_STATISTICS = "GameMatchTitleStatistics";
	public static final String GAME_MATCH_HEADER_NAME = "GameMatchHeaderName";
	public static final String GAME_MATCH_HEADER_POINTS = "GameMatchHeaderPoints";
	public static final String GAME_MATCH_HEADER_BOMBS = "GameMatchHeaderBombs";
	public static final String GAME_MATCH_HEADER_DEATHS = "GameMatchHeaderDeaths";
	public static final String GAME_MATCH_HEADER_ITEMS = "GameMatchHeaderItems";
	public static final String GAME_MATCH_HEADER_KILLS = "GameMatchHeaderKills";
	public static final String GAME_MATCH_HEADER_RANK = "GameMatchHeaderRank";
	public static final String GAME_MATCH_HEADER_TOTAL = "GameMatchHeaderTotal";
	public static final String GAME_MATCH_HEADER_ROUND = "GameMatchHeaderRound";
	public static final String GAME_MATCH_HEADER_NOTES = "GameMatchHeaderNotes";	
	public static final String GAME_MATCH_HEADER_LIMITS = "GameMatchHeaderLimits";	
	public static final String GAME_MATCH_HEADER_POINTSPROCESS = "GameMatchHeaderPointprocess";	
	public static final String GAME_MATCH_LIMIT_CONFRONTATIONS = "GameMatchLimitConfrontations";
	public static final String GAME_MATCH_LIMIT_POINTS = "GameMatchLimitPoints";
	public static final String GAME_MATCH_LIMIT_TOTAL = "GameMatchLimitTotal";
	public static final String GAME_MATCH_LIMIT_BOMBS = "GameMatchLimitBombs";
	public static final String GAME_MATCH_LIMIT_DEATHS = "GameMatchLimitDeaths";
	public static final String GAME_MATCH_LIMIT_ITEMS = "GameMatchLimitItems";
	public static final String GAME_MATCH_LIMIT_KILLS = "GameMatchLimitKills";
	
	public static final String GAME_ROUND_TITLE_DESCRIPTION = "GameRoundTitleDescription";
	public static final String GAME_ROUND_TITLE_RESULTS = "GameRoundTitleResults";
	public static final String GAME_ROUND_TITLE_STATISTICS = "GameRoundTitleStatistics";
	public static final String GAME_ROUND_HEADER_NAME = "GameRoundHeaderName";
	public static final String GAME_ROUND_HEADER_POINTS = "GameRoundHeaderPoints";
	public static final String GAME_ROUND_HEADER_BOMBS = "GameRoundHeaderBombs";
	public static final String GAME_ROUND_HEADER_DEATHS = "GameRoundHeaderDeaths";
	public static final String GAME_ROUND_HEADER_ITEMS = "GameRoundHeaderItems";
	public static final String GAME_ROUND_HEADER_KILLS = "GameRoundHeaderKills";
	public static final String GAME_ROUND_HEADER_RANK = "GameRoundHeaderRank";
	public static final String GAME_ROUND_HEADER_CROWNS = "GameRoundHeaderCrowns";
	public static final String GAME_ROUND_HEADER_FRAGS = "GameRoundHeaderFrags";
	public static final String GAME_ROUND_HEADER_PAINTINGS = "GameRoundHeaderPaintings";
	public static final String GAME_ROUND_HEADER_TIME = "GameRoundHeaderTime";
	public static final String GAME_ROUND_HEADER_LIMITS = "GameRoundHeaderLimits";	
	public static final String GAME_ROUND_LIMIT_POINTS = "GameRoundLimitPoints";
	public static final String GAME_ROUND_LIMIT_BOMBS = "GameRoundLimitBombs";
	public static final String GAME_ROUND_LIMIT_DEATHS = "GameRoundLimitDeaths";
	public static final String GAME_ROUND_LIMIT_ITEMS = "GameRoundLimitItems";
	public static final String GAME_ROUND_LIMIT_KILLS = "GameRoundLimitKills";
	public static final String GAME_ROUND_LIMIT_TIME = "GameRoundLimitTime";
	public static final String GAME_ROUND_HEADER_INITIAL_ITEMS = "GameRoundHeaderInitialItems";	
	
	
	
	
	
	// colors
	public final static Color COLOR_COMMON_BACKGROUND = new Color(255,255,255,150);
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
	public static final String MENU_ALL_BUTTON_FONT_SIZE = "MENU_ALL_BUTTON_FONT_SIZE";
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
	

	public static void init(GuiConfiguration configuration, Graphics g)
	{	// init
		Dimension panelDimension = configuration.getPanelDimension();
		int width = panelDimension.width;
		int height = panelDimension.height;
		
		// buttons
		int verticalMenuButtonHeight = (int)(height*0.05);
		sizes.put(MENU_VERTICAL_BUTTON_HEIGHT,verticalMenuButtonHeight);
		int horizontalMenuButtonHeight = (int)(height*0.07);
		sizes.put(MENU_HORIZONTAL_BUTTON_HEIGHT,horizontalMenuButtonHeight);
		int horizontalMenuButtonWidth = horizontalMenuButtonHeight;
		sizes.put(MENU_HORIZONTAL_BUTTON_WIDTH,horizontalMenuButtonWidth);
		sizes.put(MENU_HORIZONTAL_BUTTON_SPACE,(int)(width*0.025));
		int gameProgressbarFontSize = getFontSize(horizontalMenuButtonHeight*0.6, configuration, g); 
		sizes.put(GAME_PROGRESSBAR_FONT_SIZE,gameProgressbarFontSize);
		sizes.put(MENU_VERTICAL_PRIMARY_BUTTON_WIDTH,(int)(width*0.33));
		int secondaryVerticalMenuButtonWIdth = (int)(width*0.25);
		sizes.put(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH,secondaryVerticalMenuButtonWIdth);
		sizes.put(MENU_VERTICAL_BUTTON_SPACE,(int)(height*0.025));
		
		// menu panels
		int horizontalSplitMenuPanelHeight = horizontalMenuButtonHeight; 
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_HEIGHT,horizontalSplitMenuPanelHeight);
		sizes.put(HORIZONTAL_SPLIT_MENU_PANEL_WIDTH,width);
		int horizontalSplitDataPanelHeight = height-horizontalSplitMenuPanelHeight; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_HEIGHT,horizontalSplitDataPanelHeight);
		int horizontalSplitDataPanelWidth = width; 
		sizes.put(HORIZONTAL_SPLIT_DATA_PANEL_WIDTH,horizontalSplitDataPanelHeight);
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_HEIGHT,height);
		int verticalSplitMenuPanelWidth = secondaryVerticalMenuButtonWIdth;
		sizes.put(VERTICAL_SPLIT_MENU_PANEL_WIDTH,verticalSplitMenuPanelWidth);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_HEIGHT,height);
		sizes.put(VERTICAL_SPLIT_DATA_PANEL_WIDTH,width-verticalSplitMenuPanelWidth);
		// font
		int menuButtonFontSize = getFontSize(verticalMenuButtonHeight*0.9,configuration,g);
		sizes.put(MENU_ALL_BUTTON_FONT_SIZE, menuButtonFontSize);
		
		// font
		int gameTitleFontSize = (int)(menuButtonFontSize*1.3);
		sizes.put(GAME_TITLE_FONT_SIZE, gameTitleFontSize);
		// labels
		int gameDataLabelTitleHeight;
		{	Font font = configuration.getFont().deriveFont((float)gameTitleFontSize);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			gameDataLabelTitleHeight = (int)(metrics.getHeight()*1.2);
		}
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
		int gameResultLineFontSize = getFontSize(gameResultsLabelLineHeight*0.9, configuration, g);
		sizes.put(GAME_RESULTS_LINE_FONT_SIZE,gameResultLineFontSize);
		int gameResultLabelNameWidth = (int)(gameDataPanelWidth/3);
		sizes.put(GAME_RESULTS_LABEL_NAME_WIDTH,gameResultLabelNameWidth);
		int gameResultsLabelHeaderHeight = gameDataPanelHeight-16*gameResultsMarginSize-16*gameResultsLabelLineHeight;
		sizes.put(GAME_RESULTS_LABEL_HEADER_HEIGHT,gameResultsLabelHeaderHeight);
		int gameResultHeaderFontSize = getFontSize(gameResultsLabelHeaderHeight*0.9, configuration, g);
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
		
		// icons
		BufferedImage absent = ImageTools.getAbsentImage(64,64);
		BufferedImage image;
		// buttons
		{	String[] buttonStates = {ICON_NORMAL,ICON_NORMAL_SELECTED,
					ICON_DISABLED,ICON_DISABLED_SELECTED,
					ICON_ROLLOVER,ICON_ROLLOVER_SELECTED,
					ICON_PRESSED};
			//
			String baseFolder = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FOLDER_BUTTONS;
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_DESCRIPTION+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_DESCRIPTION+buttonStates[i],image);
					icons.put(MATCH_BUTTON_DESCRIPTION+buttonStates[i],image);
					icons.put(ROUND_BUTTON_DESCRIPTION+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_LEFT_BLUE+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_MENU+buttonStates[i],image);
					icons.put(MATCH_BUTTON_CURRENT_TOURNAMENT+buttonStates[i],image);
					icons.put(ROUND_BUTTON_CURRENT_MATCH+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_LEFT_RED+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_FINISH+buttonStates[i],image);
					icons.put(MATCH_BUTTON_FINISH+buttonStates[i],image);
					icons.put(ROUND_BUTTON_FINISH+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_PLAY+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(ROUND_BUTTON_PLAY+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_HOME+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_QUIT+buttonStates[i],image);
					icons.put(MATCH_BUTTON_QUIT+buttonStates[i],image);
					icons.put(ROUND_BUTTON_QUIT+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_RESULTS+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_RESULTS+buttonStates[i],image);
					icons.put(MATCH_BUTTON_RESULTS+buttonStates[i],image);
					icons.put(ROUND_BUTTON_RESULTS+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_RIGHT_BLUE+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_RIGHT_RED+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_CURRENT_MATCH+buttonStates[i],image);
					icons.put(TOURNAMENT_BUTTON_NEXT_MATCH+buttonStates[i],image);
					icons.put(MATCH_BUTTON_CURRENT_ROUND+buttonStates[i],image);
					icons.put(MATCH_BUTTON_NEXT_ROUND+buttonStates[i],image);
				}
			}
			{	String folder = baseFolder+File.separator+GuiFileTools.FOLDER_STATS+File.separator;
				for(int i=0;i<buttonStates.length;i++)
				{	image = loadIcon(folder+buttonStates[i]+".png",absent);
					icons.put(TOURNAMENT_BUTTON_STATISTICS+buttonStates[i],image);
					icons.put(MATCH_BUTTON_STATISTICS+buttonStates[i],image);
					icons.put(ROUND_BUTTON_STATISTICS+buttonStates[i],image);
				}
			}
		}
		
		// tables
		{	String folder = GuiFileTools.getIconsPath()+File.separator+GuiFileTools.FOLDER_HEADERS+File.separator;
			// bombs
			image = loadIcon(folder+GuiFileTools.FILE_BOMBS,absent);
			icons.put(GAME_TOURNAMENT_HEADER_BOMBS,image);
			icons.put(GAME_MATCH_HEADER_BOMBS,image);
			icons.put(GAME_ROUND_HEADER_BOMBS,image);
			icons.put(GAME_TOURNAMENT_LIMIT_BOMBS,image);
			icons.put(GAME_MATCH_LIMIT_BOMBS,image);
			icons.put(GAME_ROUND_LIMIT_BOMBS,image);
			// confrontations
			image = loadIcon(folder+GuiFileTools.FILE_CONFRONTATIONS,absent);
			icons.put(GAME_TOURNAMENT_LIMIT_CONFRONTATIONS,image);
			icons.put(GAME_MATCH_LIMIT_CONFRONTATIONS,image);
			// crowns
			image = loadIcon(folder+GuiFileTools.FILE_CROWNS,absent);
			icons.put(GAME_ROUND_HEADER_CROWNS,image);
			// deaths
			image = loadIcon(folder+GuiFileTools.FILE_DEATHS,absent);
			icons.put(GAME_TOURNAMENT_HEADER_DEATHS,image);
			icons.put(GAME_MATCH_HEADER_DEATHS,image);
			icons.put(GAME_ROUND_HEADER_DEATHS,image);
			icons.put(GAME_TOURNAMENT_LIMIT_DEATHS,image);
			icons.put(GAME_MATCH_LIMIT_DEATHS,image);
			icons.put(GAME_ROUND_LIMIT_DEATHS,image);
			// frags
			image = loadIcon(folder+GuiFileTools.FILE_FRAGS,absent);
			icons.put(GAME_ROUND_HEADER_FRAGS,image);
			// items
			image = loadIcon(folder+GuiFileTools.FILE_ITEMS,absent);
			icons.put(GAME_TOURNAMENT_HEADER_ITEMS,image);
			icons.put(GAME_MATCH_HEADER_ITEMS,image);
			icons.put(GAME_ROUND_HEADER_ITEMS,image);
			icons.put(GAME_TOURNAMENT_LIMIT_ITEMS,image);
			icons.put(GAME_MATCH_LIMIT_ITEMS,image);
			icons.put(GAME_ROUND_LIMIT_ITEMS,image);
			// kills
			image = loadIcon(folder+GuiFileTools.FILE_KILLS,absent);
			icons.put(GAME_TOURNAMENT_HEADER_KILLS,image);
			icons.put(GAME_MATCH_HEADER_KILLS,image);
			icons.put(GAME_ROUND_HEADER_KILLS,image);
			icons.put(GAME_TOURNAMENT_LIMIT_KILLS,image);
			icons.put(GAME_MATCH_LIMIT_KILLS,image);
			icons.put(GAME_ROUND_LIMIT_KILLS,image);
			// name
			image = loadIcon(folder+GuiFileTools.FILE_NAME,absent);
			icons.put(GAME_TOURNAMENT_HEADER_NAME,image);
			icons.put(GAME_MATCH_HEADER_NAME,image);
			icons.put(GAME_ROUND_HEADER_NAME,image);
			// paintings
			image = loadIcon(folder+GuiFileTools.FILE_PAINTINGS,absent);
			icons.put(GAME_ROUND_HEADER_PAINTINGS,image);
			// points
			image = loadIcon(folder+GuiFileTools.FILE_POINTS,absent);
			icons.put(GAME_TOURNAMENT_HEADER_POINTS,image);
			icons.put(GAME_MATCH_HEADER_POINTS,image);
			icons.put(GAME_ROUND_HEADER_POINTS,image);
			icons.put(GAME_TOURNAMENT_LIMIT_POINTS,image);
			icons.put(GAME_MATCH_LIMIT_POINTS,image);
			icons.put(GAME_ROUND_LIMIT_POINTS,image);
			// rank
			image = loadIcon(folder+GuiFileTools.FILE_RANK,absent);
			icons.put(GAME_TOURNAMENT_HEADER_RANK,image);
			icons.put(GAME_MATCH_HEADER_RANK,image);
			icons.put(GAME_ROUND_HEADER_RANK,image);
			// time
			image = loadIcon(folder+GuiFileTools.FILE_TIME,absent);
			icons.put(GAME_ROUND_HEADER_TIME,image);
			icons.put(GAME_ROUND_LIMIT_TIME,image);
			// total
			image = loadIcon(folder+GuiFileTools.FILE_TOTAL,absent);
			icons.put(GAME_TOURNAMENT_HEADER_TOTAL,image);
			icons.put(GAME_MATCH_HEADER_TOTAL,image);
			icons.put(GAME_TOURNAMENT_LIMIT_TOTAL,image);
			icons.put(GAME_MATCH_LIMIT_TOTAL,image);
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
	public static int getFontSize(double limit, GuiConfiguration configuration, Graphics g)
	{	int result = 0;
		int fheight;
		do
		{	result++;
			Font font = configuration.getFont().deriveFont((float)result);
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics(font);
			fheight = metrics.getHeight();
		}
		while(fheight<limit);
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
			Font font = configuration.getFont().deriveFont((float)sizes.get(MENU_ALL_BUTTON_FONT_SIZE));
			button.setFont(font);
			button.setText(text);
		}		
		// action command
		button.setActionCommand(name);
		// tooltip
		String toolTip = name+"Tooltip";
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
		return result;
	}
	public static JButton createSecondaryVerticalMenuButton(String name, ButtonAware panel, GuiConfiguration configuration)
	{	int width = sizes.get(MENU_VERTICAL_SECONDARY_BUTTON_WIDTH);
		int height = sizes.get(MENU_VERTICAL_BUTTON_HEIGHT);
		JButton result = new JButton();
		initButton(result,name,width,height,panel,configuration);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
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
