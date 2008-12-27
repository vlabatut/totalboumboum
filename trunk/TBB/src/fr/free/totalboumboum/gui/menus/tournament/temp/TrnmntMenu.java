package fr.free.totalboumboum.gui.menus.tournament.temp;

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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import fr.free.totalboumboum.configuration.game.tournament.TournamentConfigurationSaver;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import fr.free.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;
import fr.free.totalboumboum.gui.tools.GuiTools;

public class TrnmntMenu extends InnerMenuPanel
{	private static final long serialVersionUID = 1L;
	
	public TrnmntMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		initButtons();	

		// panels
		playersData = new PlayersData(container);
		settingsData = new SettingsData(container);
		tournamentPanel = new TournamentSplitPanel(container.getContainer(),getMenuParent()/*container*/);
	}
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonQuit;
	private JButton buttonPlayersPrevious;
	private JButton buttonPlayersNext;
	private JButton buttonSettingsPrevious;
	private JButton buttonSettingsNext;
	private int buttonWidth;
	private int buttonHeight;

	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();
		//
		buttonQuit = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersNext = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonSettingsPrevious = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonSettingsNext = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	private void setButtonsPlayers()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonPlayersPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonPlayersNext);
	}
	
	private void setButtonsSettings()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonSettingsPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonSettingsNext);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	private TournamentConfiguration tournamentConfiguration;
	
	public boolean initTournament()
	{	boolean result = false;
		// init tournament
		if(tournament==null || tournament.isOver())
		{	// init configuration
			tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration().copy();
			loadTournament();
			if(!tournamentConfiguration.getUseLastPlayers())
				tournamentConfiguration.reinitPlayers();		
			if(!tournamentConfiguration.getUseLastTournament())
				tournamentConfiguration.reinitTournament();
			// set panel
			playersData.setTournamentConfiguration(tournamentConfiguration);
			container.setDataPart(playersData);
			setButtonsPlayers();
			result = true;
		}		
		// existing (unfinished) tournament
		else if(tournament.hasBegun())
			//replaceWith(matchPanel);
			result = false;
		return result;
	}

	private void setTournamentPlayers()
	{	ArrayList<Profile> selectedProfiles = playersData.getSelectedProfiles();
		tournament.setProfiles(selectedProfiles);
	}
	
	private void loadTournament()
	{	try
		{	tournament = tournamentConfiguration.loadLastTournament();
			tournamentConfiguration.setTournament(tournament);
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
	}
	
	private void setTournamentSettings()
	{				
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentSplitPanel tournamentPanel;
	private PlayersData playersData;
	private SettingsData settingsData;
	
	public TournamentSplitPanel getTournamentPanel()
	{	return tournamentPanel;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT))
		{	tournament = null;
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS))				
		{	setButtonsPlayers();
			container.setDataPart(playersData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT))
		{	// synch game options
			ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(playersData.getSelectedProfiles());
			tournamentConfiguration.setProfilesSelection(profilesSelection);
			// set settings panel
			settingsData.setTournamentConfiguration(tournamentConfiguration);
			setButtonsSettings();
			refresh();
			container.setDataPart(settingsData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT))
		{	// implements settings in tournament
			tournament = tournamentConfiguration.getTournament();
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
			// match panel
			tournamentPanel.setTournament(tournament);
			replaceWith(tournamentPanel);
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	if(tournament==null || 
			!tournament.getAllowedPlayerNumbers().contains(tournamentConfiguration.getProfilesSelection().getProfileCount()))
			buttonSettingsNext.setEnabled(false);
		else
			buttonSettingsNext.setEnabled(true);
	}
}
