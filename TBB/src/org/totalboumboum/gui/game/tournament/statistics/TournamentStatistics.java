package org.totalboumboum.gui.game.tournament.statistics;

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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.content.subpanel.events.TournamentEvolutionSubPanel;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class was designed to display
 * plots describing the evolution of
 * a tournament.
 * 
 * @author Vincent Labatut
 */
public class TournamentStatistics extends EntitledDataPanel
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
	public TournamentStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_TOURNAMENT_STATISTICS_TITLE;
		setTitleKey(key);
	
		// data
		{	evolutionPanel = new TournamentEvolutionSubPanel(dataWidth,dataHeight);
			setDataPart(evolutionPanel);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The tournament displayed in this panel */
	private AbstractTournament tournament;

	/**
	 * Changes the tournament displayed in this
	 * panel, and updates all concerned GUI
	 * components.
	 * 
	 * @param tournament
	 * 		The new tournament to display.
	 */
	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
		evolutionPanel.setTournament(tournament);
	}
	
	/**
	 * Returns the tournament currently
	 * displayed.
	 * 
	 * @return
	 * 		The current tournament.
	 */
	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** Panel used to display the plot */
	private TournamentEvolutionSubPanel evolutionPanel;
	
	@Override
	public void refresh()
	{	setTournament(tournament);
	}
}
