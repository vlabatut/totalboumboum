package org.totalboumboum.gui.game.tournament.statistics;

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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This abstract class was designed to display
 * plots describing the evolution of
 * a tournament.
 * 
 * @param <T>
 * 		Type of tournament handled by this panel. 
 * 
 * @author Vincent Labatut
 */
public abstract class TournamentStatistics<T extends AbstractTournament> extends EntitledDataPanel
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
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The tournament displayed in this panel */
	protected T tournament;

	/**
	 * Changes the tournament displayed in this
	 * panel, and updates all concerned GUI
	 * components.
	 * 
	 * @param tournament
	 * 		The new tournament to display.
	 */
	public abstract void setTournament(T tournament);
	
	/**
	 * Returns the tournament currently
	 * displayed.
	 * 
	 * @return
	 * 		The current tournament.
	 */
	public T getTournament()
	{	return tournament;	
	}
}
