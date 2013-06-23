package org.totalboumboum.gui.game.round.statistics;

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
import org.totalboumboum.gui.common.content.subpanel.events.RoundEvolutionSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class was designed to display
 * plots describing the evolution of
 * a round.
 * 
 * @author Vincent Labatut
 */
public class RoundStatistics extends EntitledDataPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel to display
	 * plots describing the evolution of
	 * a round.
	 * 
	 * @param container
	 * 		The GUI component containing this panel.
	 */
	public RoundStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_ROUND_STATISTICS_TITLE;
		setTitleKey(key);
		
		// data
		{	evolutionPanel = new RoundEvolutionSubPanel(dataWidth,dataHeight);
			setDataPart(evolutionPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The round displayed in this panel */
	private Round round;
	/** Number of the round currently displayed */
	private int number;
	
	/**
	 * Changes the round displayed in this
	 * panel, and updates all concerned GUI
	 * components.
	 * 
	 * @param round
	 * 		The new round to display.
	 * @param number
	 * 		Number of the round in the current match.
	 */
	public void setRound(Round round, Integer number)
	{	this.round = round;
		evolutionPanel.setRound(round);
		
		// title
		this.number = number;
		String key = GuiKeys.GAME_ROUND_STATISTICS_TITLE;
		String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		if(number!=null)
		{	text = text + " " + number;
			tooltip = tooltip + " " + number;
		}
		setTitleText(text,tooltip);
	}
	
	/**
	 * Returns the round currently
	 * displayed.
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
	/** Panel used to display the plot */
	private RoundEvolutionSubPanel evolutionPanel;

	@Override
	public void refresh()
	{	setRound(round,number);
	}
}
