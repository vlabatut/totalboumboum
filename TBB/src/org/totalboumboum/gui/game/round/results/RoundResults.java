package org.totalboumboum.gui.game.round.results;

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

import org.totalboumboum.game.round.Round;
import org.totalboumboum.gui.common.content.subpanel.results.HomogenResultsSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class handles the display of the
 * results of a round, during a game.
 * 
 * @author Vincent Labatut
 */
public class RoundResults extends EntitledDataPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public RoundResults(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_ROUND_RESULTS_TITLE;
		setTitleKey(key);
		
		// data
		{	resultsPanel = new HomogenResultsSubPanel(dataWidth,dataHeight);
			resultsPanel.setShowConfrontations(false);
			resultsPanel.setShowTotal(false);
			setDataPart(resultsPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Round displayed in this panel */
	private Round round;
	/** Number of the round currently displayed */
	private int number;

	/**
	 * Changes the round displayed in this panel.
	 * 
	 * @param round
	 * 		The new round to display.
	 * @param number
	 * 		Number of the round in the current match.
	 */
	public void setRound(Round round, Integer number)
	{	this.round = round;
		resultsPanel.setStatisticHolder(round);
		
		// title
		this.number = number;
		String key = GuiKeys.GAME_ROUND_RESULTS_TITLE;
		String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		if(number!=null)
		{	text = text + " " + number;
			tooltip = tooltip + " " + number;
		}
		setTitleText(text,tooltip);
	}
	
	/**
	 * Returns the round displayed
	 * in this panel.
	 * 
	 * @return
	 * 		The current round.
	 */
	public Round getRound()
	{	return round;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying the results */
	private HomogenResultsSubPanel resultsPanel;
	
	@Override
	public void refresh()
	{	setRound(round,number);
	}
}
