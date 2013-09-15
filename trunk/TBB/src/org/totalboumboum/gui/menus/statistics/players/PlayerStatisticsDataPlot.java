package org.totalboumboum.gui.menus.statistics.players;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.gui.common.content.subpanel.statistics.PlayerEvolutionSubPanel;
import org.totalboumboum.gui.common.content.subpanel.statistics.PlayerStatisticSubPanel;
import org.totalboumboum.gui.common.content.subpanel.statistics.StatisticColumn;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.statistics.overall.PlayerStats.Value;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class PlayerStatisticsDataPlot extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;
	
	private static final int LINES = 16;
	private PlayerEvolutionSubPanel mainPanel;
	
	public PlayerStatisticsDataPlot(SplitMenuPanel container)
	{	super(container);

		// title
		setTitleKey(GuiKeys.MENU_STATISTICS_PLAYER_TITLE);
		
		// data
		{	// create panel
			mainPanel = new PlayerEvolutionSubPanel(dataWidth,dataHeight);
			setDataPart(mainPanel);
			
			// set players content
			try
			{	List<String> playersIds = ProfileLoader.getIdsList();
				HashMap<String, Profile> profiles = ProfileLoader.loadProfiles(playersIds);
				mainPanel.setProfiles(profiles);
				setView(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2);
				mainPanel.updateSelectedPlayers();
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
	{	mainPanel.updateSelectedPlayers();
	}
	
	/////////////////////////////////////////////////////////////////
	// MENU INTERACTION				/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setView(String view)
	{	List<Value> values = new ArrayList<Value>();
		if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_GLICKO2))
		{	values.add(Value.RANK);
			values.add(Value.MEAN);
			values.add(Value.STDEV);
			mainPanel.setAvailableValues(values);
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_SCORES))
		{	values.add(Value.BOMBEDS);
			values.add(Value.SELF_BOMBINGS);
			values.add(Value.BOMBINGS);
			values.add(Value.ITEMS);
			values.add(Value.BOMBS);
			values.add(Value.PAINTINGS);
			values.add(Value.CROWNS);
			mainPanel.setAvailableValues(values);
		}
		else if(view.equals(GuiKeys.MENU_STATISTICS_PLAYER_BUTTON_CONFRONTATIONS))
		{	values.add(Value.CONFR_TOTAL);
			values.add(Value.CONFR_WON);
			values.add(Value.CONFR_DRAW);
			values.add(Value.CONFR_LOST);
			values.add(Value.TIME);
			mainPanel.setAvailableValues(values);
			mainPanel.setValue(Value.CONFR_WON);
		}
	}
}