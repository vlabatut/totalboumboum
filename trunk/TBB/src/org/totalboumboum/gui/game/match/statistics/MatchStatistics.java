package org.totalboumboum.gui.game.match.statistics;

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
import org.totalboumboum.gui.common.content.subpanel.events.EvolutionSubPanelListener;
import org.totalboumboum.gui.common.content.subpanel.events.MatchEvolutionSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.data.configuration.GuiConfiguration;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class was designed to display
 * plots describing the evolution of
 * a match.
 * 
 * @author Vincent Labatut
 */
public class MatchStatistics extends EntitledDataPanel implements EvolutionSubPanelListener
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
			evolutionPanel.addListener(this);
			setDataPart(evolutionPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The match displayed in this panel */
	private Match match;
	/** Number of the match currently displayed */
	private int number;

	/**
	 * Changes the match displayed in this
	 * panel, and updates all concerned GUI
	 * components.
	 * 
	 * @param match
	 * 		The new match to display.
	 * @param number
	 * 		Number of the round in the current match.
	 */
	public void setMatch(Match match, Integer number)
	{	this.match = match;
		evolutionPanel.setMatch(match);
		
		// title
		this.number = number;
		String name = match.getName();
		String key = GuiKeys.GAME_MATCH_DESCRIPTION_TITLE;
		String text = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key);
		String tooltip = GuiConfiguration.getMiscConfiguration().getLanguage().getText(key+GuiKeys.TOOLTIP);
		if(number!=null)
		{	text = text + " " + number;
			tooltip = tooltip + " " + number;
		}
		if(name!=null)
			text = name;
		setTitleText(text,tooltip);
	}
	
	/**
	 * Returns the match currently
	 * displayed.
	 * 
	 * @return
	 * 		The current match.
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
	{	setMatch(match,number);
	}

	/////////////////////////////////////////////////////////////////
	// EVOLUTION SUB PANEL LISTENER		/////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void mousePressed(MouseEvent e)
	{	fireMousePressed(e);
	}
}
