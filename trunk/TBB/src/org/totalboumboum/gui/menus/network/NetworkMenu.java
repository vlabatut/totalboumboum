package org.totalboumboum.gui.menus.network;

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
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.DataPanelListener;
import org.totalboumboum.gui.common.structure.panel.menu.InnerMenuPanel;
import org.totalboumboum.gui.common.structure.panel.menu.MenuPanel;
import org.totalboumboum.gui.game.tournament.TournamentSplitPanel;
import org.totalboumboum.gui.tools.GuiButtonTools;
import org.totalboumboum.gui.tools.GuiColorTools;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiSizeTools;
import org.totalboumboum.gui.tools.GuiImageTools;
import org.totalboumboum.stream.network.client.ClientGeneralConnexion;
import org.totalboumboum.stream.network.client.ClientIndividualConnexion;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostState;
import org.xml.sax.SAXException;

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
		setBackground(GuiColorTools.COLOR_COMMON_BACKGROUND);

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
	private JToggleButton buttonPlayersValidate;
	private int buttonWidth;
	private int buttonHeight;

	private void initButtons()
	{	buttonWidth = getHeight();
		buttonHeight = getHeight();

		buttonQuit = GuiButtonTools.createButton(GuiKeys.MENU_NETWORK_BUTTON_QUIT,buttonWidth,buttonHeight,1,this);
		buttonGamesPrevious = GuiButtonTools.createButton(GuiKeys.MENU_NETWORK_GAMES_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonGamesNext = GuiButtonTools.createButton(GuiKeys.MENU_NETWORK_GAMES_BUTTON_NEXT,buttonWidth,buttonHeight,1,this);
		buttonPlayersPrevious = GuiButtonTools.createButton(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS,buttonWidth,buttonHeight,1,this);
		buttonPlayersValidate = GuiButtonTools.createToggleButton(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_VALIDATE,buttonWidth,buttonHeight,1,this);
		removeAll();
	}
	
	private void setButtonsGames()
	{	refreshButtons();
		removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonGamesPrevious);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(buttonGamesNext);
	}
	
	private void setButtonsPlayers()
	{	refreshButtons();
		removeAll();
		add(buttonQuit);
		add(Box.createHorizontalGlue());
		add(buttonPlayersPrevious);
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(buttonWidth,buttonHeight)));
		add(Box.createRigidArea(new Dimension(GuiSizeTools.buttonHorizontalSpace,0)));
		add(buttonPlayersValidate);
	}
	
	private void refreshButtons()
	{	// games
		GameInfo gameInfo = gamesData.getSelectedGame();
		if(gameInfo==null || gameInfo.getHostInfo().getState()!=HostState.OPEN)
			buttonGamesNext.setEnabled(false);
		else
			buttonGamesNext.setEnabled(true);
		
		// players
		ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
		ClientIndividualConnexion activeConnexion = connexion.getActiveConnexion();
		if(activeConnexion==null)
			buttonPlayersValidate.setEnabled(false);
		else
			buttonPlayersValidate.setEnabled(true);
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
		{	ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
			connexion.exitGame();
			getFrame().setMainMenuPanel();
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_GAMES_BUTTON_PREVIOUS))				
		{	replaceWith(parent);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_PREVIOUS))				
		{	ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
			connexion.exitPlayersSelection();

			setButtonsGames();
			container.setDataPart(gamesData);
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_PLAYERS_BUTTON_VALIDATE))
		{	ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
			connexion.confirmPlayersSelection(buttonPlayersValidate.isSelected());
	    }
		else if(e.getActionCommand().equals(GuiKeys.MENU_NETWORK_GAMES_BUTTON_NEXT))
		{	// set payers panel
			GameInfo gameInfo = gamesData.getSelectedGame();
			ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
			connexion.entersPlayerSelection(gameInfo);
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
	public void dataPanelSelectionChanged(Object object)
	{	refreshButtons();
		
		if(getDataPart()==playersData)
		{	ClientGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getClientConnexion();
			ClientIndividualConnexion activeConnexion = connexion.getActiveConnexion();
			// active connexion lost >> exit player selection screen
			if(object instanceof ClientIndividualConnexion && 
					(activeConnexion.getState()==ClientState.SELECTING_PLAYERS || activeConnexion.getState()==ClientState.WAITING_TOURNAMENT))
			{	connexion.exitPlayersSelection();

				setButtonsGames();
				container.setDataPart(gamesData);
			}
			
			// tournament has just started >> enter tournament screen or get back to game selection
			else if(object instanceof AbstractTournament)
			{	// enter the tournament
				if(activeConnexion.getState()==ClientState.WAITING_TOURNAMENT)
				{	tournament = activeConnexion.getTournament();
					
					// synch game options
					List<Profile> profiles = tournament.getProfiles();
					for(Profile profile: profiles)
					{	try
						{	ProfileLoader.reloadPortraits(profile);
						}
						catch(ParserConfigurationException e)
						{	e.printStackTrace();
						}
						catch(SAXException e)
						{	e.printStackTrace();
						}
						catch(IOException e)
						{	e.printStackTrace();
						}
						catch(ClassNotFoundException e)
						{	e.printStackTrace();
						}
					}
					ProfilesSelection profilesSelection = ProfilesConfiguration.getSelection(profiles);
					TournamentConfiguration tournamentConfiguration = Configuration.getGameConfiguration().getTournamentConfiguration().copy();
					tournamentConfiguration.setProfilesSelection(profilesSelection);
					tournamentConfiguration.setTournament(tournament);
					
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
					tournamentPanel.setTournament(tournament);
					replaceWith(tournamentPanel);
				}
				// leave the tournament
				else
				{	connexion.exitPlayersSelection();
	
					setButtonsGames();
					container.setDataPart(gamesData);
				}
			}
		}
	}
}
