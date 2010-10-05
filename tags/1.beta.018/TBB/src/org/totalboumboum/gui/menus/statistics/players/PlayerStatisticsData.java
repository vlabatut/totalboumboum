package org.totalboumboum.gui.menus.statistics.players;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.gui.common.content.subpanel.statistics.PlayerStatisticSubPanel;
import org.totalboumboum.gui.common.content.subpanel.statistics.StatisticColumn;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayerStatisticsData extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINES = 16;
	private PlayerStatisticSubPanel mainPanel;
	
	public PlayerStatisticsData(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_STATISTICS_PLAYER_TITLE);
		
		// data
		{	// create panel
			mainPanel = new PlayerStatisticSubPanel(dataWidth,dataHeight);
			setDataPart(mainPanel);
			
			// set players ids
			setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2);
			List<String> playersIds = ProfileLoader.getIdsList();
			HashMap<String, Profile> profilesMap;
			try
			{	profilesMap = ProfileLoader.loadProfiles(playersIds);
				mainPanel.setPlayersIds(profilesMap,LINES);
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
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
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
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
	{	List<StatisticColumn> columns = new ArrayList<StatisticColumn>();
		if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2))
		{	columns.add(StatisticColumn.GENERAL_BUTTON);
			columns.add(StatisticColumn.GENERAL_RANK);
			columns.add(StatisticColumn.GENERAL_EVOLUTION);
			columns.add(StatisticColumn.GENERAL_TYPE);
			columns.add(StatisticColumn.GENERAL_NAME);
			columns.add(StatisticColumn.GLICKO_MEAN);
			columns.add(StatisticColumn.GLICKO_DEVIATION);
			columns.add(StatisticColumn.GLICKO_ROUNDCOUNT);
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES))
		{	columns.add(StatisticColumn.GENERAL_BUTTON);
			columns.add(StatisticColumn.GENERAL_RANK);
			columns.add(StatisticColumn.GENERAL_TYPE);
			columns.add(StatisticColumn.GENERAL_NAME);
			columns.add(StatisticColumn.SCORE_BOMBS);
			columns.add(StatisticColumn.SCORE_ITEMS);
			columns.add(StatisticColumn.SCORE_BOMBEDS);
			columns.add(StatisticColumn.SCORE_SELF_BOMBINGS);
			columns.add(StatisticColumn.SCORE_BOMBINGS);
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS))
		{	columns.add(StatisticColumn.GENERAL_BUTTON);
			columns.add(StatisticColumn.GENERAL_RANK);
			columns.add(StatisticColumn.GENERAL_TYPE);
			columns.add(StatisticColumn.GENERAL_NAME);
			columns.add(StatisticColumn.ROUNDS_PLAYED);
			columns.add(StatisticColumn.ROUNDS_WON);
			columns.add(StatisticColumn.ROUNDS_DRAWN);
			columns.add(StatisticColumn.ROUNDS_LOST);
			columns.add(StatisticColumn.SCORE_TIME);
		}
		mainPanel.setColumns(columns);
	}
}