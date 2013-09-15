package org.totalboumboum.gui.menus.tournament;

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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration.AutoAdvance;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.configuration.game.tournament.TournamentConfigurationSaver;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiMiscTools;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.server.ServerGeneralConnexion;
import org.totalboumboum.tools.GameData;
import org.xml.sax.SAXException;

/**
 * Menu allowing to set a new tournament.
 * 
 * @author Vincent Labatut
 */
public class TournamenuMenu extends InnerMenuPanel implements DataPanelListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates the tournament menu.
	 * 
	 * @param container
	 * 		Container for this panel.
	 * @param parent
	 * 		Menu parent of this menu.
	 */
	public TournamenuMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

		// buttons
		initButtons();	

		tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration().copy();
		
		// panels
		playersData = new PlayersData(container);
		playersData.addListener(this);
		settingsData = new SettingsData(container);
		settingsData.addListener(this);
	}
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Definitely quit the tournament */
	private JButton buttonQuit;
	/** Go to the panel preceeding players selection */
	private JButton buttonPlayersPrevious;
	/** Go to the panel following players selection */
	private JButton buttonPlayersNext;
	/** Go to the panel preceeding settings definition */
	private JButton buttonSettingsPrevious;
	/** Go to the panel following settings definition */
	private JButton buttonSettingsNext;
	/** Publish tournament online */
	private JButton buttonPublish;
	/** Block selection of online players */
	private JToggleButton buttonBlockPlayers;
	/** Width of all buttons */
	private int buttonWidth;
	/** Height of all buttons */
	private int buttonHeight;

	/**
	 * Creates all necessary buttons.
	 */
	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();

		buttonQuit = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersNext = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonSettingsPrevious = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonSettingsNext = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonPublish = GuiButtonTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH,buttonWidth,buttonHeight,1,this);
buttonPublish.setEnabled(!GameData.PRODUCTION);
		buttonBlockPlayers = GuiButtonTools.createToggleButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_BLOCK_PLAYERS,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	/**
	 * Displays buttons of the players selection panel.
	 */
	private void setButtonsPlayers()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonPlayersPrevious);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(buttonPublish);
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(buttonPlayersNext);
	}
	
	/**
	 * Displays buttons of the settings panel.
	 */
	private void setButtonsSettings()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonSettingsPrevious);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(buttonSettingsNext);
	}
	
	/** Appropriately enable/disable buttons */
	private void refreshButtons()
	{	AbstractTournament tournament = tournamentConfiguration.getTournament();
		if(tournament==null || !tournament.getAllowedPlayerNumbers().contains(playersData.getSelectedProfiles().size()))
			buttonPlayersNext.setEnabled(false);
		else
		{	ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
			if(connexion==null || connexion.areAllPlayersReady())
				buttonPlayersNext.setEnabled(true);
			else
				buttonPlayersNext.setEnabled(false);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Configuration of the tournament (settings, selected players...) */
	private TournamentConfiguration tournamentConfiguration;
	
	/**
	 * Initializes the tournament configuration panels.
	 */
	public void initTournament()
	{	// init configuration
		tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration().copy();
		if(!tournamentConfiguration.getUseLastPlayers())
			tournamentConfiguration.reinitPlayers();
		
		try
		{	if(!tournamentConfiguration.getUseLastTournament())
				tournamentConfiguration.reinitTournament();
			else
				tournamentConfiguration.loadLastTournament();
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		
		// set panel
		settingsData.setTournamentConfiguration(tournamentConfiguration);
		container.setDataPart(settingsData);
		setButtonsSettings();
	}

	/**
	 * Changes the selected players
	 * for the tournament.
	 */
	private void setTournamentPlayers()
	{	List<Profile> selectedProfiles = playersData.getSelectedProfiles();
		AbstractTournament tournament = tournamentConfiguration.getTournament();
		tournament.setProfiles(selectedProfiles);
	}
	
	/**
	 * Changes the general settings of the tournament.
	 */
	private void setTournamentSettings()
	{	//	
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying general tournament settings */
	private SettingsData settingsData;
	/** Panel displaying the players selection */
	private PlayersData playersData;
	/** Panel displaying the actual tournament */
	private TournamentSplitPanel tournamentPanel;
	
	/**
	 * Change the panel displaying the tournament.
	 *  
	 * @param tournamentPanel
	 * 		New panel displaying the tournament.
	 */
	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	this.tournamentPanel = tournamentPanel;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void actionPerformed(ActionEvent e)
	{	// possibly interrupt any pending button-related thread first
		if(thread!=null && thread.isAlive())
			thread.interrupt();
		
		if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT))
		{	AbstractTournament tournament = null;
			tournamentConfiguration.setTournament(tournament);
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS))				
		{	setButtonsSettings();
			container.setDataPart(settingsData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT))
		{	// synch game options
			ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(playersData.getSelectedProfiles());
			tournamentConfiguration.setProfilesSelection(profilesSelection);
			
			// set the tournament
			setTournamentPlayers();
			setTournamentSettings();
			
			// save tournament options
			try
			{	TournamentConfigurationSaver.saveTournamentConfiguration(tournamentConfiguration);
			}
			catch (ParserConfigurationException e1)
			{	e1.printStackTrace();
			}
			catch (SAXException e1)
			{	e1.printStackTrace();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
			
			// synch game options
			Configuration.getGameConfiguration().setTournamentConfiguration(tournamentConfiguration);

			// get tournament
			AbstractTournament tournament = tournamentConfiguration.getTournament();
			
			// send to possible clients
			ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
			if(connexion!=null)
				connexion.startTournament(tournament);
			
			// tournament panel
			tournamentPanel.setTournament(tournament);
			tournamentPanel.autoAdvance();
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT))
		{	// set payers panel
			playersData.setTournamentConfiguration(tournamentConfiguration);
			setButtonsPlayers();
			refresh();
			if(Configuration.getAisConfiguration().getAutoAdvance()==AutoAdvance.TOURNAMENT)
			{	playersData.autoSelectPlayers();
				refreshButtons();
			}
			autoAdvance();
			container.setDataPart(playersData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH))
		{	// update buttons
			buttonPlayersPrevious.setEnabled(false);
			int index = GuiMiscTools.indexOfComponent(this,buttonPublish);
			remove(index);
			add(buttonBlockPlayers,index);
			revalidate();
			
			// set up the connexion
/*			try
			{	AbstractTournament tournament = tournamentConfiguration.getTournament();
				connexionManager = new ConfigurationServerConnexionManager(tournament);
				connexionManager.addListener(this);
				ConfigurationServerConnexionThread thread = new ConfigurationServerConnexionThread(connexionManager);
				thread.start();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}*/	
			AbstractTournament tournament = tournamentConfiguration.getTournament();
			Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
			String tournamentName = tournament.getName();
			TournamentType tournamentType = TournamentType.getType(tournament);
			List<Profile> playerProfiles = playersData.getSelectedProfiles();
			List<Double> playersScores = new ArrayList<Double>();
			RankingService glicko2 = GameStatistics.getRankingService();
			for(Profile p: playerProfiles)
			{	Double score = null;
				String id = p.getId();
				if(glicko2.getPlayers().contains(id))
				{	PlayerRating playerRating = glicko2.getPlayerRating(id);
					score = playerRating.getRating();
				}
				playersScores.add(score);
			}
			boolean direct = true; 		// TODO should be decided by a button or something
			boolean central = false;	// TODO same thing as above
			ServerGeneralConnexion connexion = new ServerGeneralConnexion(allowedPlayers,tournamentName,tournamentType,playersScores,playerProfiles,direct,central);
			Configuration.getConnexionsConfiguration().setServerConnexion(connexion);
			playersData.setConnexion();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_BLOCK_PLAYERS))
		{	// close/open players selection to client
			ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
			if(connexion!=null)
			{	connexion.switchPlayersSelection();
			}
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	// possibly interrupt any pending button-related thread first
		if(thread!=null && thread.isAlive())
			thread.interrupt();
	}

	/////////////////////////////////////////////////////////////////
	// AUTO ADVANCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Thread used for the auto-advance system */
	private Thread thread = null;

	/**
	 * Automatically clicks on the appropriate
	 * buttons in order to progress in the tournament.
	 * Used to automatically chain many tournaments,
	 * while evaluating agents.
	 */
	public void autoAdvance()
	{	if(Configuration.getAisConfiguration().getAutoAdvance()==AutoAdvance.TOURNAMENT)
		{	// go to players selection
			if(buttonSettingsNext.getParent()!=null && buttonSettingsNext.isEnabled()) //NOTE is getParent enough?
			{	thread = new Thread("TBB.autoadvance")
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
								{	buttonSettingsNext.doClick();
								}
							});				
						}
						catch (InterruptedException e)
						{	//e.printStackTrace();
						}
					}			
				};
				thread.start();
			}
			// start tournament
			else if(buttonPlayersNext.getParent()!=null && buttonPlayersNext.isEnabled()) //NOTE is getParent enough?
			{	thread = new Thread("TBB.autoadvance")
				{	@Override
					public void run()
					{	try
						{	sleep(Configuration.getAisConfiguration().getAutoAdvanceDelay());
							SwingUtilities.invokeLater(new Runnable()
							{	@Override
								public void run()
								{	buttonPlayersNext.doClick();
								}
							});				
						}
						catch (InterruptedException e)
						{	//e.printStackTrace();
						}
					}			
				};
				thread.start();
			}
		}
	}
}
