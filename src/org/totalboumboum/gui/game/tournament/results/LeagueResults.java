package org.totalboumboum.gui.game.tournament.results;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.game.tournament.league.LeagueTournament;
import org.totalboumboum.gui.common.content.subpanel.results.HeterogeneousResultsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;

/**
 * This class handles the display of the
 * result of a league, during a game.
 * 
 * @author Vincent Labatut
 */
public class LeagueResults extends TournamentResults<LeagueTournament>
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public LeagueResults(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	resultsPanel = new HeterogeneousResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowTime(false);
			setDataPart(resultsPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void setTournament(LeagueTournament tournament)
	{	this.tournament = tournament;
		if(tournament.isOldSchool())
		{	resultsPanel.setShowConfrontations(true);
			resultsPanel.setShowRanks(false);
		}
		else
		{	resultsPanel.setShowConfrontations(false);
			resultsPanel.setShowRanks(true);
		}
		resultsPanel.setTournament(tournament);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Panel displaying the results */
	private HeterogeneousResultsSubPanel resultsPanel;
	
	@Override
	public void refresh()
	{	setTournament(tournament);
	}
}
