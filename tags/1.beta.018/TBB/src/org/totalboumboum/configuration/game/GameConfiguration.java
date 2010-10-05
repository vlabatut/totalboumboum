package org.totalboumboum.configuration.game;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameConfiguration
{
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentConfiguration tournamentConfiguration;
	
	public void setTournamentConfiguration(TournamentConfiguration tournamentConfiguration)
	{	this.tournamentConfiguration = tournamentConfiguration;
	}
	public TournamentConfiguration getTournamentConfiguration()
	{	return tournamentConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// QUICKMATCH			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickMatchConfiguration quickMatchConfiguration;
	
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
	}
	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private QuickStartConfiguration quickStartConfiguration;
	
	public void setQuickStartConfiguration(QuickStartConfiguration quickStartConfiguration)
	{	this.quickStartConfiguration = quickStartConfiguration;
	}
	public QuickStartConfiguration getQuickStartConfiguration()
	{	return quickStartConfiguration;
	}
}
