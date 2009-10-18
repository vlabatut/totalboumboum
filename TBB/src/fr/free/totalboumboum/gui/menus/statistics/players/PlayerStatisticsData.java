package fr.free.totalboumboum.gui.menus.statistics.players;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.profile.ProfileLoader;
import fr.free.totalboumboum.gui.common.content.subpanel.statistics.PlayerStatisticBrowserSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;

public class PlayerStatisticsData extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINES = 16;
	private PlayerStatisticBrowserSubPanel mainPanel;
	
	public PlayerStatisticsData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_STATISTICS_PLAYER_TITLE);
		
		// data
		{	// create panel
			mainPanel = new PlayerStatisticBrowserSubPanel(dataWidth,dataHeight);
			setDataPart(mainPanel);
			
			// set players ids
			setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2);
			List<Integer> playersIds = ProfileLoader.getIdsList();
			mainPanel.setPlayersIds(playersIds,LINES);
		}
	}

	@Override
	public void refresh()
	{	//
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU INTERACTION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setView(String view)
	{	if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2))
		{	mainPanel.setShowPortrait(true);
			mainPanel.setShowType(true);
			mainPanel.setShowMean(true);
			mainPanel.setShowStdev(true);
			mainPanel.setShowVolatility(true);
			mainPanel.setShowRoundcount(true);
			mainPanel.setShowRoundsPlayed(false);
			mainPanel.setShowRoundsWon(false);
			mainPanel.setShowRoundsDrawn(false);
			mainPanel.setShowRoundsLost(false);
			mainPanel.setShowTimePlayed(false);
			mainPanel.setShowScores(false);			
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES))
		{	mainPanel.setShowPortrait(true);
			mainPanel.setShowType(true);
			mainPanel.setShowMean(false);
			mainPanel.setShowStdev(false);
			mainPanel.setShowVolatility(false);
			mainPanel.setShowRoundcount(false);
			mainPanel.setShowRoundsPlayed(false);
			mainPanel.setShowRoundsWon(false);
			mainPanel.setShowRoundsDrawn(false);
			mainPanel.setShowRoundsLost(false);
			mainPanel.setShowTimePlayed(false);
			mainPanel.setShowScores(true);		
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS))
		{	mainPanel.setShowPortrait(true);
			mainPanel.setShowType(true);
			mainPanel.setShowMean(false);
			mainPanel.setShowStdev(false);
			mainPanel.setShowVolatility(false);
			mainPanel.setShowRoundcount(false);
			mainPanel.setShowRoundsPlayed(true);
			mainPanel.setShowRoundsWon(true);
			mainPanel.setShowRoundsDrawn(true);
			mainPanel.setShowRoundsLost(true);
			mainPanel.setShowTimePlayed(true);
			mainPanel.setShowScores(false);		
		}
	}
}