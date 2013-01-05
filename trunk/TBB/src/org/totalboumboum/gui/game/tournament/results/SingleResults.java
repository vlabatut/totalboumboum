package org.totalboumboum.gui.game.tournament.results;

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

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.single.SingleTournament;
import org.totalboumboum.gui.common.content.subpanel.results.HomogenResultsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SingleResults extends TournamentResults<SingleTournament>
{	
	private static final long serialVersionUID = 1L;

	private HomogenResultsSubPanel resultsPanel;
	
	public SingleResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_MATCH_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	resultsPanel = new HomogenResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowTime(false);
			setDataPart(resultsPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void setTournament(SingleTournament tournament)
	{	this.tournament = tournament;
		Match match = tournament.getCurrentMatch();
		resultsPanel.setStatisticHolder(match);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	setTournament(tournament);
	}
}
