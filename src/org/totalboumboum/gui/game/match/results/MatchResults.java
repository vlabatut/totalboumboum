package org.totalboumboum.gui.game.match.results;

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

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.subpanel.results.HomogeneousResultsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class handles the display of the
 * results of a match, during a game.
 * 
 * @author Vincent Labatut
 */
public class MatchResults extends EntitledDataPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public MatchResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_MATCH_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	resultsPanel = new HomogeneousResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowTime(false);
			setDataPart(resultsPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Match displayed by this panel */
	private Match match;
	/** Number of the match currently displayed */
	private int number;

	/**
	 * Changes the match displayed
	 * in this panel.
	 * 
	 * @param match
	 * 		New match.
	 * @param number
	 * 		Number of the round in the current match.
	 */
	public void setMatch(Match match, Integer number)
	{	this.match = match;
		resultsPanel.setStatisticHolder(match);
		
		// original title
		this.number = number;
		String name = match.getName();
		String key = GuiKeys.GAME_MATCH_DESCRIPTION_TITLE;
		String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		
		// possibly replace by number
		if(number!=null)
		{	// match number
			text = text + " " + number;
			tooltip = tooltip + " " + number;
			// total number of matches
			AbstractTournament tournament = match.getTournament();
			Integer max = tournament.getTotalMatchNumber();
			if(max!=null)
			{	text = text + "/" + max;
				tooltip = tooltip + "/" + max;
			}
		}
		
		// possibly replace by name
		if(name!=null)
			text = name;
		
		setTitleText(text,tooltip);
	}
	
	/**
	 * Returns the match currently
	 * displayed in this panel.
	 * 
	 * @return
	 * 		Current match.
	 */
	public Match getMatch()
	{	return match;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Panel displaying the results */
	private HomogeneousResultsSubPanel resultsPanel;

	@Override
	public void refresh()
	{	setMatch(match,number);
	}
}
