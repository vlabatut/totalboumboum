package org.totalboumboum.gui.menus.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.configuration.game.tournament.TournamentConfigurationSaver;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.configuration.profile.ProfilesConfiguration;
import org.totalboumboum.configuration.profile.ProfilesSelection;
import org.totalboumboum.configuration.profile.SpriteInfo;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.network.stream.network.configuration.ConfigurationServerConnectionListener;
import org.totalboumboum.network.stream.network.configuration.ConfigurationServerConnectionManager;
import org.totalboumboum.network.stream.network.configuration.ConfigurationServerConnectionThread;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class TournamenuMenu extends InnerMenuPanel implements DataPanelListener, ConfigurationServerConnectionListener
{	private static final long serialVersionUID = 1L;
	
	public TournamenuMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

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
	private JButton buttonQuit;
	private JButton buttonPlayersPrevious;
	private JButton buttonPlayersNext;
	private JButton buttonSettingsPrevious;
	private JButton buttonSettingsNext;
	private JButton buttonPublish;
	private JToggleButton buttonBlockPlayers;
	private int buttonWidth;
	private int buttonHeight;

	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();

		buttonQuit = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersNext = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonSettingsPrevious = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonSettingsNext = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonPublish = GuiTools.createButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH,buttonWidth,buttonHeight,1,this);
		buttonBlockPlayers = GuiTools.createToggleButton(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_BLOCK_PLAYERS,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	private void setButtonsPlayers()
	{	removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonPlayersPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(buttonPublish);
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
	
	private void refreshButtons()
	{	AbstractTournament tournament = tournamentConfiguration.getTournament();
		if(tournament==null || !tournament.getAllowedPlayerNumbers().contains(playersData.getSelectedProfiles().size()))
			buttonPlayersNext.setEnabled(false);
		else
			buttonPlayersNext.setEnabled(true);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentConfiguration tournamentConfiguration;
	
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

	private void setTournamentPlayers()
	{	List<Profile> selectedProfiles = playersData.getSelectedProfiles();
		AbstractTournament tournament = tournamentConfiguration.getTournament();
		tournament.setProfiles(selectedProfiles);
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
	
	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	this.tournamentPanel = tournamentPanel;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_BUTTON_QUIT))
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
			
			// tournament panel
			AbstractTournament tournament = tournamentConfiguration.getTournament();
			tournamentPanel.setTournament(tournament);
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_NEXT))
		{	// set payers panel
			playersData.setTournamentConfiguration(tournamentConfiguration);
			setButtonsPlayers();
			refresh();
			container.setDataPart(playersData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_TOURNAMENT_SETTINGS_BUTTON_PUBLISH))
		{	// update buttons
			buttonPlayersPrevious.setEnabled(false);
			int index = GuiTools.indexOfComponent(this,buttonPublish);
			remove(index);
			add(buttonBlockPlayers,index);
			revalidate();
			
			// set up the connection
			try
			{	AbstractTournament tournament = tournamentConfiguration.getTournament();
				connectionManager = new ConfigurationServerConnectionManager(tournament);
				connectionManager.addListener(this);
				ConfigurationServerConnectionThread thread = new ConfigurationServerConnectionThread(connectionManager);
				thread.start();
			}
			catch (IOException e1)
			{	e1.printStackTrace();
			}
	    }
	} 
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void refresh()
	{	refreshButtons();
	} 

	/////////////////////////////////////////////////////////////////
	// DATA PANEL LISTENER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void dataPanelSelectionChanged()
	{	refreshButtons();
	}

	/////////////////////////////////////////////////////////////////
	// CONNECTION MANAGER	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ConfigurationServerConnectionManager connectionManager = null;

	@Override
	public synchronized void profileAdded(Profile profile)
	{	// possibly add the profile to the local ones
		try
		{	Configuration.getProfilesConfiguration().insertProfile(profile);
		}
		catch (IllegalArgumentException e)
		{	e.printStackTrace();
		}
		catch (SecurityException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{	e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}

		// add the profile to the selection
		ProfilesSelection profilesSelection = tournamentConfiguration.getProfilesSelection();
		if(!profilesSelection.containsProfile(profile))
			profilesSelection.addProfile(profile);
		
		// update the GUI
		playersData.refresh();
	}

	@Override
	public synchronized void profileRemoved(String id)
	{	
		playersData.refresh();
	}

	@Override
	public synchronized void spriteChanged(String id, SpriteInfo sprite)
	{	
		playersData.refresh();
	}
}
