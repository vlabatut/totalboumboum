package org.totalboumboum.gui.menus.network;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import java.util.List;
import java.util.Set;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanel;
import org.totalboumboum.gui.common.content.subpanel.players.PlayersSelectionSubPanelListener;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.menus.network.hero.SelectHeroSplitPanel;
import org.totalboumboum.gui.menus.network.profile.SelectProfileSplitPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.client.ClientGeneralConnectionListener;
import org.totalboumboum.stream.network.client.ClientIndividualConnection;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayersData extends EntitledDataPanel implements PlayersSelectionSubPanelListener, ClientGeneralConnectionListener
{	
	private static final long serialVersionUID = 1L;
	
	private PlayersSelectionSubPanel playersPanel;
	
	public PlayersData(SplitMenuPanel container)
	{	super(container);
		
		connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.addListener(this);
		
		// title
		String key = GuiKeys.MENU_NETWORK_PLAYERS_TITLE;
		setTitleKey(key);
		
		// data
		playersPanel = new PlayersSelectionSubPanel(dataWidth,dataHeight);
		playersPanel.addListener(this);
		setDataPart(playersPanel);
	}

	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void refresh()
	{	//AbstractTournament tournament = tournamentConfiguration.getTournament();
		//Set<Integer> allowedPlayers = tournament.getAllowedPlayerNumbers();
		//playersPanel.setAllowedPlayers(allowedPlayers);
		//playersPanel.refresh();
	}
		
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT CONFIGURATION		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setTournamentConfiguration()
	{	// nothing to do
	}

	private List<Profile> getSelectedProfiles()
	{	return playersPanel.getPlayers();	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER SELECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void playerSelectionHeroSet(int index)
	{	// supposedly for a local player
		Profile profile = playersPanel.getPlayer(index);
		SelectHeroSplitPanel selectHeroPanel = new SelectHeroSplitPanel(container.getMenuContainer(),container,profile);
		getMenuContainer().replaceWith(selectHeroPanel);	
	}

	@Override
	public void playerSelectionPlayerAdded(int index)
	{	// supposedly for a local player
		SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayerRemoved(int index)
	{	// supposedly for a local player
		Profile profile = playersPanel.getPlayer(index);
		ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.requestPlayersRemove(profile);
		//fireDataPanelSelectionChange();
	}

	@Override
	public void playerSelectionProfileSet(int index)
	{	// supposedly for a local player
		SelectProfileSplitPanel selectProfilePanel = new SelectProfileSplitPanel(container.getMenuContainer(),container,index,getSelectedProfiles());
		getMenuContainer().replaceWith(selectProfilePanel);
	}

	@Override
	public void playerSelectionPlayersAdded()
	{	// should not be possible
	}

	@Override
	public void playerSelectionColorSet(int index)
	{	Profile profile = playersPanel.getPlayer(index);
		ClientGeneralConnection connection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(connection!=null)
			connection.requestPlayersChangeColor(profile);
	}

	@Override
	public void playerSelectionControlsSet(int index)
	{	// nothing to do here
	}

	/////////////////////////////////////////////////////////////////
	// CLIENT CONNECTION LISTENER	/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientGeneralConnection connection = null;

	@Override
	public void connectionAdded(ClientIndividualConnection connection, int index)
	{	// nothing to do here
	}

	@Override
	public void connectionRemoved(ClientIndividualConnection connection,int index)
	{	// nothing to do here
	}

	@Override
	public void connectionGameInfoChanged(ClientIndividualConnection connection, int index, String oldId)
	{	// nothing to do here
	}

	@Override
	public void connectionActiveConnectionLost(ClientIndividualConnection connection, int index)
	{	fireDataPanelSelectionChange(connection);
	}

	@Override
	public void connectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	Set<Integer> allowedPlayers = connection.getGameInfo().getAllowedPlayers();
		List<Profile> selectedProfiles = connection.getPlayerProfiles();
		playersPanel.setPlayers(selectedProfiles,allowedPlayers);
	}

	@Override
	public void connectionTournamentStarted(AbstractTournament tournament)
	{	fireDataPanelSelectionChange(tournament);
	}
}
