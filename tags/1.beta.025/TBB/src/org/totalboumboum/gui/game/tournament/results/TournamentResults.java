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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * This class handles the display of the
 * result of a match, during a game.
 * 
 * @param <T>
 * 		Type or tournament displayed by this class.
 *  
 * @author Vincent Labatut
 */
public abstract class TournamentResults<T extends AbstractTournament> extends EntitledDataPanel
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard panel.
	 * 
	 * @param container
	 * 		Container of the panel.
	 */
	public TournamentResults(SplitMenuPanel container)
	{	super(container);
		
		// title
		String key = GuiKeys.GAME_TOURNAMENT_RESULTS_TITLE;
		setTitleKey(key);	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament displayed by this panel */
	protected T tournament;

	/**
	 * Changes the tournament displayed
	 * in this panel.
	 * 
	 * @param tournament
	 * 		New tournament.
	 */
	public abstract void setTournament(T tournament);
	
	/**
	 * Returns the tournament currently
	 * displayed in this panel.
	 * 
	 * @return
	 * 		Current tournament.
	 */
	public AbstractTournament getTournament()
	{	return tournament;	
	}
}
