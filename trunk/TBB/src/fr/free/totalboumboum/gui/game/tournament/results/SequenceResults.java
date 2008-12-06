package fr.free.totalboumboum.gui.game.tournament.results;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.gui.common.content.subpanel.results.ResultsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;

public class SequenceResults extends TournamentResults
{	
	private static final long serialVersionUID = 1L;

	private ResultsSubPanel resultsPanel;
	
	public SequenceResults(SplitMenuPanel container)
	{	super(container);
		
		// data
		{	resultsPanel = new ResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowTime(false);
			setDataPart(resultsPanel);
			//
			SequenceTournament tournament = (SequenceTournament)Configuration.getGameConfiguration().getTournament();
			resultsPanel.setStatisticHolder(tournament);
		}
	}

	@Override
	public void refresh()
	{	SequenceTournament tournament = (SequenceTournament)Configuration.getGameConfiguration().getTournament();
		resultsPanel.setStatisticHolder(tournament);
	}
}
