package org.totalboumboum.gui.menus.network;

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
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostState;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetworkMenu extends InnerMenuPanel implements DataPanelListener
{	private static final long serialVersionUID = 1L;
	
	public NetworkMenu(SplitMenuPanel container, MenuPanel parent)
	{	super(container, parent);
		
		// layout
		BoxLayout layout = new BoxLayout(this,BoxLayout.LINE_AXIS); 
		setLayout(layout);
		
		// background
		setBackground(GuiTools.COLOR_COMMON_BACKGROUND);

		// buttons
		initButtons();	
		
		// panels
		gamesData = new GamesData(container);
		gamesData.addListener(this);
		playersData = new PlayersData(container);
		playersData.addListener(this);
	}
	
	/////////////////////////////////////////////////////////////////
	// BUTTONS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private JButton buttonQuit;
	private JButton buttonGamesPrevious;
	private JButton buttonGamesNext;
	private JButton buttonPlayersPrevious;
	private JButton buttonPlayersNext;
	private int buttonWidth;
	private int buttonHeight;

	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();

		buttonQuit = GuiTools.createButton(GuiKeys.MENU_NETWORK_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonGamesPrevious = GuiTools.createButton(GuiKeys.MENU_NETWORK_GAMES_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonGamesNext = GuiTools.createButton(GuiKeys.MENU_NETWORK_GAMES_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiTools.createButton(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersNext = GuiTools.createButton(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	private void setButtonsGames()
	{	refreshButtons();
		removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonGamesPrevious);
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiTools.buttonHorizontalSpace,0)));
		add(buttonGamesNext);
	}
	
	private void setButtonsPlayers()
	{	refreshButtons();
		removeAll();
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
	
	private void refreshButtons()
	{	// games
		GameInfo gameInfo = gamesData.getSelectedGame();
		if(gameInfo==null || gameInfo.getHostInfo().getState()!=HostState.OPEN)
			buttonGamesNext.setEnabled(false);
		else
			buttonGamesNext.setEnabled(true);
		
		// players
		if(tournament==null) //TODO à compléter
			buttonPlayersNext.setEnabled(false);
		else
			buttonPlayersNext.setEnabled(true);
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT					/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament = null;
	
	public void initTournament()
	{	// set panel
		//gamesData.setTournamentConfiguration(tournamentConfiguration);
		container.setDataPart(gamesData);
		setButtonsGames();
	}

	private void setTournamentPlayers()
	{	
//NOTE NET		List<Profile> selectedProfiles = playersData.getSelectedProfiles();
//NOTE NET		AbstractTournament tournament = tournamentConfiguration.getTournament();
//NOTE NET		tournament.setProfiles(selectedProfiles);
	}
	
	private void setTournamentSettings()
	{				
	}
	
	/////////////////////////////////////////////////////////////////
	// PANELS						/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentSplitPanel tournamentPanel;
	private GamesData gamesData;
	private PlayersData playersData;
	
	public void setTournamentPanel(TournamentSplitPanel tournamentPanel)
	{	this.tournamentPanel = tournamentPanel;
	}
	
	/////////////////////////////////////////////////////////////////
	// ACTION LISTENER				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{	if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_BUTTON_QUIT))
		{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			connection.exitGame();
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_GAMES_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS))				
		{	ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			connection.exitPlayerSelection();

			setButtonsGames();
			container.setDataPart(gamesData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_NEXT))
		{	// synch game options
//			ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(playersData.getSelectedProfiles());
//TODO			tournamentConfiguration.setProfilesSelection(profilesSelection);
			
			// set the tournament
			setTournamentPlayers();
			setTournamentSettings();
			
			// save tournament options
/*			try
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
*/			
			// synch game options
//TODO			Configuration.getGameConfiguration().setTournamentConfiguration(tournamentConfiguration);
			
			// tournament panel
//TODO			AbstractTournament tournament = tournamentConfiguration.getTournament();
			tournamentPanel.setTournament(tournament);
			replaceWith(tournamentPanel);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_GAMES_BUTTON_NEXT))
		{	// set payers panel
			GameInfo gameInfo = gamesData.getSelectedGame();
			ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
			connection.enterPlayerSelection(gameInfo);
			playersData.setTournamentConfiguration();
			
			setButtonsPlayers();
			refresh();
			container.setDataPart(playersData);
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
}
