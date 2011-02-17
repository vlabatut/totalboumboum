package org.totalboumboum.gui.game.match.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import org.totalboumboum.gui.common.structure.panel.SplitMenuPanel;
import org.totalboumboum.gui.common.structure.panel.data.EntitledDataPanel;
import org.totalboumboum.gui.tools.GuiKeys;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MatchStatistics extends EntitledDataPanel
{	
	private static final long serialVersionUID = 1L;

	public MatchStatistics(SplitMenuPanel container)
	{	super(container);

		// title
		String key = GuiKeys.GAME_MATCH_STATISTICS_TITLE;
		setTitleKey(key);

	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;

	public void setMatch(Match match)
	{	this.match = match;
	}
	
	public Match getMatch()
	{	return match;	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTENT PANEL	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void refresh()
	{	// nothing to do here
	}
}
