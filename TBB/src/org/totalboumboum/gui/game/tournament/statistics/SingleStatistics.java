package org.totalboumboum.gui.game.tournament.statistics;

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

import java.awt.event.MouseEvent;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.content.subpanel.events.MatchEvolutionSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;

/**
 * This class was designed to display
 * plots describing the evolution of
 * a single tournament.
 * 
 * @author Vincent Labatut
 */
public class SingleStatistics extends TournamentStatistics<SingleTournament>
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel to display
	 * plots describing the evolution of
	 * a tournament.
	 * 
	 * @param container
	 * 		The GUI component containing this panel.
	 */
	public SingleStatistics(SplitMenuPanel container)
	{	super(container);
	
		// data
		{	evolutionPanel = new MatchEvolutionSubPanel(dataWidth,dataHeight);
			evolutionPanel.addListener(this);
			setDataPart(evolutionPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setTournament(SingleTournament tournament)
	{	this.tournament = tournament;
		Match match = tournament.getCurrentMatch();
		evolutionPanel.setMatch(match);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Panel used to display the plot */
	private MatchEvolutionSubPanel evolutionPanel;
	
	@Override
	public void refresh()
	{	setTournament(tournament);
	}

	/////////////////////////////////////////////////////////////////
	// EVOLUTION SUB PANEL LISTENER		/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
	}
}
