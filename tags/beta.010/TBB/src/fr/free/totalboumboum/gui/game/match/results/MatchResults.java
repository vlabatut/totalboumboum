package fr.free.totalboumboum.gui.game.match.results;

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

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.gui.common.content.subpanel.results.ResultsSubPanel;
import fr.free.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import fr.free.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import fr.free.totalboumboum.gui.tools.GuiKeys;

public class MatchResults extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	private ResultsSubPanel resultsPanel;
	
	public MatchResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_MATCH_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	resultsPanel = new ResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowTime(false);
			setDataPart(resultsPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;

	public void setMatch(Match match)
	{	this.match = match;
		resultsPanel.setStatisticHolder(match);
	}
	
	public Match getMatch()
	{	return match;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	setMatch(match);
	}
}
