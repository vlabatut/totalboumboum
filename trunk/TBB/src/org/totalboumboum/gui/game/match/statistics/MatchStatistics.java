package org.totalboumboum.gui.game.match.statistics;

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

import org.totalboumboum.game.match.Match;
import org.totalboumboum.gui.common.content.subpanel.events.MatchEvolutionSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class was designed to display
 * plots describing the evolution of
 * a match.
 * 
 * @author Vincent Labatut
 *
 */
public class MatchStatistics extends EntitledDataPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel to display
	 * plots describing the evolution of
	 * a match.
	 * 
	 * @param container
	 * 		The GUI component containing this panel.
	 */
	public MatchStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_MATCH_STATISTICS_TITLE;
		setTitleKey(key);

		// data
		{	evolutionPanel = new MatchEvolutionSubPanel(dataWidth,dataHeight);
			setDataPart(evolutionPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The match displayed in this panel */
	private Match match;

	/**
	 * Changes the match displayed in this
	 * panel, and updates all concerned GUI
	 * components.
	 * 
	 * @param match
	 * 		The new match to display.
	 */
	public void setMatch(Match match)
	{	this.match = match;
		evolutionPanel.setMatch(match);
	}
	
	/**
	 * Returns the match currently
	 * displayed.
	 * 
	 * @return
	 * 		The current round.
	 */
	public Match getMatch()
	{	return match;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Panel used to display the plot */
	private MatchEvolutionSubPanel evolutionPanel;

	@Override
	public void refresh()
	{	setMatch(match);
	}
}
