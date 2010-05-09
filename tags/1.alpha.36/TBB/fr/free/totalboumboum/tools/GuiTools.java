package fr.free.totalboumboum.tools;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.gui.generic.ButtonAware;

public class GuiTools
{	
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
	public static final String TOURNAMENT_BUTTON_NEXT_MATCH = "TournamentButtonNextMatch";
	public static final String TOURNAMENT_BUTTON_RESULTS = "TournamentButtonResults";
	public static final String TOURNAMENT_BUTTON_STATISTICS = "TournamentButtonStatistics";
	
	// MATCH PANEL
	public static final String MATCH_BUTTON_QUIT = "MatchButtonQuit";
	public static final String MATCH_BUTTON_CURRENT_ROUND = "MatchButtonCurrentRound";
	public static final String MATCH_BUTTON_CURRENT_TOURNAMENT = "MatchButtonCurrentTournament";
	public static final String MATCH_BUTTON_DESCRIPTION = "MatchButtonDescription";
	public static final String MATCH_BUTTON_NEXT_MATCH = "MatchButtonNextMatch";
	public static final String MATCH_BUTTON_NEXT_ROUND = "MatchButtonNextRound";
	public static final String MATCH_BUTTON_RESULTS = "MatchButtonResults";
	public static final String MATCH_BUTTON_STATISTICS = "MatchButtonStatistics";
	
	// ROUND PANEL
	public static final String ROUND_BUTTON_QUIT = "RoundButtonQuit";
	public static final String ROUND_BUTTON_CURRENT_MATCH = "RoundButtonCurrentMatch";
	public static final String ROUND_BUTTON_DESCRIPTION = "RoundButtonDescription";
	public static final String ROUND_BUTTON_NEXT_ROUND = "RoundButtonNextRound";
	public static final String ROUND_BUTTON_PLAY = "RoundButtonPlay";
	public static final String ROUND_BUTTON_RESULTS = "RoundButtonResults";
	public static final String ROUND_BUTTON_STATISTICS = "RoundButtonStatistics";
	
	public static void setButtonText(String name, AbstractButton button, Configuration configuration)
	{	// text shown
		String text = configuration.getLanguage().getText(name);
		Font font = new Font(null,Font.BOLD,configuration.getTextHeight());
		button.setFont(font);
		button.setText(text);
		button.setActionCommand(name);
		// tooltip
		String toolTip = name+"Tooltip";
		text = configuration.getLanguage().getText(toolTip);
		button.setToolTipText(text);
	}
		
	public static JButton createVerticalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	// set text
		JButton result = new JButton();
		setButtonText(name,result,configuration);
		// dimension
		Dimension dim = configuration.getVerticalMenuButtonDimension();
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// add to panel
		panel.add(result);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		result.addActionListener(panel);
		return result;
	}

	public static JButton createHorizontalMenuButton(String name, ButtonAware panel, Configuration configuration)
	{	// set text
		JButton result = new JButton();
		setButtonText(name,result,configuration);
		// dimension
		Dimension dim = configuration.getHorizontalMenuButtonDimension();
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// add to panel
		panel.add(result);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		result.setActionCommand(name);
		result.addActionListener(panel);
		return result;
	}

	public static JToggleButton createHorizontalMenuToggleButton(String name, ButtonAware panel, Configuration configuration)
	{	// set text
		JToggleButton result = new JToggleButton();
		setButtonText(name,result,configuration);
		// dimension
		Dimension dim = configuration.getHorizontalMenuButtonDimension();
		result.setMinimumSize(dim);
		result.setMaximumSize(dim);
		result.setPreferredSize(dim);
		// add to panel
		panel.add(result);
		result.setAlignmentY(Component.CENTER_ALIGNMENT);
		result.setActionCommand(name);
		result.addActionListener(panel);
		return result;
	}
}
