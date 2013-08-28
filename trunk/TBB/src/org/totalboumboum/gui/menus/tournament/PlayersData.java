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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanel;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.menus.tournament.hero.SelectHeroSplitPanel;
import org.totalboumboum.gui.menus.tournament.profile.SelectProfileSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.server.ServerGeneralConnexion;
import org.totalboumboum.stream.network.server.ServerGeneralConnexionListener;
import org.xml.sax.SAXException;

/**
 * Tihs class allows selecting players
 * for a tournament.
 * 
 * @author Vincent Labatut
 */
public class PlayersData extends EntitledDataPanel implements PlayersSelectionSubPanelListener, ServerGeneralConnexionListener
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new panel to select players for a tournament.
	 * 
	 * @param container
	 * 		Container of this panel.
	 */
	public PlayersData(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.MENU_TOURNAMENT_PLAYERS_TITLE;
		setTitleKey(key);
		
		// data
		playersPanel = new PlayersSelectionSubPanel(dataWidth,dataHeight);
		playersPanel.addListener(this);
		setDataPart(playersPanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying the player selection table */
	private PlayersSelectionSubPanel playersPanel;

	@Override
	public void refresh()
	{	AbstractTournament tournament = tournamentConfiguration.getTournament();
		Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
		playersPanel.setAllowedPlayers(allowedPlayers);
		//playersPanel.refresh();
	}
		
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT CONFIGURATION		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current tournament configuration */
	private TournamentConfiguration tournamentConfiguration;
//	private ArrayList<Profile> players;
	
	/**
	 * Setup the tournament configuration.
	 * 
	 * @param tournamentConfiguration
	 * 		Tournament configuration to set up.
	 */
	public void setTournamentConfiguration(TournamentConfiguration tournamentConfiguration)
	{	this.tournamentConfiguration = tournamentConfiguration;
		AbstractTournament tournament = tournamentConfiguration.getTournament();
		Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
		ProfilesSelection profilesSelection = tournamentConfiguration.getProfilesSelection();
		List<Profile> selectedProfiles = new ArrayList<Profile>();
		try
		{	selectedProfiles = ProfileLoader.loadProfiles(profilesSelection);
		}
		catch (IllegalArgumentException e1)
		{	e1.printStackTrace();
		}
		catch (SecurityException e1)
		{	e1.printStackTrace();
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
		catch (IllegalAccessException e1)
		{	e1.printStackTrace();
		}
		catch (NoSuchFieldException e1)
		{	e1.printStackTrace();
		}
		catch (ClassNotFoundException e1)
		{	e1.printStackTrace();
		}
		
		// remove distant profiles
		Iterator<Profile> it = selectedProfiles.iterator();
		while(it.hasNext())
		{	Profile profile = it.next();
			if(profile.isRemote())
				it.remove();
		}
		
		playersPanel.setPlayers(selectedProfiles,allowedPlayers);
	}
	
	/**
	 * Returns the players selected
	 * for this tournament.
	 * 
	 * @return
	 * 		A list of player profiles.
	 */
	public List<Profile> getSelectedProfiles()
	{	return playersPanel.getPlayers();	
	}

	/**
	 * Used to automatically select players
	 * when the auto-advance option is enabled
	 * with the "tournament" mode.
	 */
	public void autoSelectPlayers()
	{	AbstractTournament tournament = tournamentConfiguration.getTournament();
		Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
		
		List<Profile> profiles = new ArrayList<Profile>();
		try
		{	profiles = ProfilesConfiguration.autoAdvanceComplete(tournamentConfiguration);
		}
		catch (IllegalArgumentException e)
		{	e.printStackTrace();
		}
		catch (SecurityException e)
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
		catch (ParserConfigurationException e)
		{	e.printStackTrace();
		}
		catch (SAXException e)
		{	e.printStackTrace();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		
		// remove distant profiles
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile profile = it.next();
			if(profile.isRemote())
				it.remove();
		}
		
		playersPanel.setPlayers(profiles,allowedPlayers);
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER SELECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerSelectionHeroSet(int index)
	{	Profile profile = playersPanel.getPlayer(index);
		SelectHeroSplitPanel selectHeroPanel = new SelectHeroSplitPanel(container.getMenuContainer(),container,profile);
		getMenuContainer().replaceWith(selectHeroPanel);	
	}

	@Override
	public void playerSelectionPlayerAdded(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayerRemoved(int index)
	{	ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
		if(connexion!=null)
			connexion.profileRemoved(index);
		fireDataPanelSelectionChange(null);
	}

	@Override
	public void playerSelectionProfileSet(int index)
	{	SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayersAdded()
	{	// NOTE this would be so much cleaner with an events system...
		ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
		if(connexion!=null)
			connexion.profilesAdded(playersPanel.getPlayers());
	}

	@Override
	public void playerSelectionColorSet(int index)
	{	Profile profile = playersPanel.getPlayer(index);
		ServerGeneralConnexion connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
		if(connexion!=null)
			connexion.profileModified(profile);
	}

	@Override
	public void playerSelectionControlsSet(int index)
	{	// not used here
	}

	/////////////////////////////////////////////////////////////////
	// SERVER CONNECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Online connexion of this tournament */
	private ServerGeneralConnexion connexion = null;
	
	/**
	 * Set up the connexion for an online game.
	 */
	public void setConnexion()
	{	connexion = Configuration.getConnexionsConfiguration().getServerConnexion();
		if(connexion!=null)
		{	connexion.addListener(this);
			List<Profile> players = playersPanel.getPlayers();
			Set<Integer> allowedPlayers = playersPanel.getAllowedPlayers();
			playersPanel.setPlayers(players, allowedPlayers);
		}
	}
	
	@Override
	public void profileAdded(int index, Profile profile)
	{	// NOTE ugly fix
		List<Profile> profiles = connexion.getPlayerProfiles();
		GameInfo gameInfo = connexion.getGameInfo();
		playersPanel.setPlayers(profiles,gameInfo.getAllowedPlayers());
		// menu might have to update button
		fireDataPanelSelectionChange(profile);
	}

	@Override
	public void profileRemoved(Profile profile)
	{	// NOTE ugly fix
		List<Profile> profiles = connexion.getPlayerProfiles();
		GameInfo gameInfo = connexion.getGameInfo();
		playersPanel.setPlayers(profiles,gameInfo.getAllowedPlayers());
		// menu might have to update button
		fireDataPanelSelectionChange(profile);
	}

	@Override
	public void profileModified(Profile profile)
	{	// NOTE ugly fix
		List<Profile> profiles = connexion.getPlayerProfiles();
		GameInfo gameInfo = connexion.getGameInfo();
		playersPanel.setPlayers(profiles,gameInfo.getAllowedPlayers());
		// menu might have to update button
		fireDataPanelSelectionChange(profile);
	}

	@Override
	public void profileSet(int index, Profile profile)
	{	// NOTE ugly fix
		List<Profile> profiles = connexion.getPlayerProfiles();
		GameInfo gameInfo = connexion.getGameInfo();
		playersPanel.setPlayers(profiles,gameInfo.getAllowedPlayers());
		// menu might have to update button
		fireDataPanelSelectionChange(profile);
	}
}
