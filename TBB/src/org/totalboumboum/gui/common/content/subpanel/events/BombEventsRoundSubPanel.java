package org.totalboumboum.gui.common.content.subpanel.events;

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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.MyLabel;
import org.totalboumboum.gui.common.structure.subpanel.container.EmptySubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.SubPanel;
import org.totalboumboum.gui.common.structure.subpanel.container.TableSubPanel;
import org.totalboumboum.gui.common.structure.subpanel.content.EmptyContentPanel;
import org.totalboumboum.gui.tools.GuiKeys;
import org.totalboumboum.gui.tools.GuiTools;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.statistics.glicko2.jrs.PlayerRating;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.xml.sax.SAXException;

import com.sun.org.glassfish.external.statistics.Statistic;

import de.erichseifert.gral.data.DataTable;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class BombEventsRoundSubPanel extends EmptySubPanel implements MouseListener
{	private static final long serialVersionUID = 1L;

	public BombEventsRoundSubPanel(int width, int height)
	{	super(width,height,SubPanel.Mode.BORDER);

		// set panel
		EmptyContentPanel dataPanel = getDataPanel();
		dataPanel.setOpaque(false);
		
		// background
		{	Color bg = GuiTools.COLOR_COMMON_BACKGROUND;
			setBackground(bg);
		}
		
		// layout
		{	BoxLayout layout = new BoxLayout(dataPanel,BoxLayout.PAGE_AXIS); 
			dataPanel.setLayout(layout);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// PAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TableSubPanel plotPanel;
	private Round round;
	
	public void setRound(Round round)
	{	this.round = round;
		AbstractTournament tournament;
	
		// init player series
		StatisticRound statisticRound = round.getStats();
		List<String> ids = statisticRound.getPlayersIds();
		Map<Integer,String> series = new HashMap<Integer, String>();
		for(int i=0;i<ids.size();i++)
			series.put(i+1, value)
		
		// setting up data model
		DataTable dataTable = new DataTable(Integer.class, Integer.class);
		List<StatisticEvent> events = statisticRound.getStatisticEvents();
		
		for(StatisticEvent event: events)
		{	String id = event.getActorId();
			Profile profile = tournament.getProfileById(id);
			
		}
		// main panel
		{	plotPanel = 
			dataPanel.add(plotPanel);
		}

	}
	
	public void refresh()
	{	setRound(round);
	}

	/////////////////////////////////////////////////////////////////
	// MOUSE LISTENER	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mouseClicked(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseExited(MouseEvent e)
	{	
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{	
	}
	
	@Override
	public void mouseReleased(MouseEvent e)
	{	
	}
}
