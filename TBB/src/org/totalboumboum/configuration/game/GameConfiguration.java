package org.totalboumboum.configuration.game;

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

import org.totalboumboum.configuration.game.quickmatch.QuickMatchConfiguration;
import org.totalboumboum.configuration.game.quickstart.QuickStartConfiguration;
import org.totalboumboum.configuration.game.tournament.TournamentConfiguration;

/**
 * This class handles all options regarding
 * the game and gameplay aspects.
 * 
 * @author Vincent Labatut
 */
public class GameConfiguration
{
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Settings related to tournaments */
	private TournamentConfiguration tournamentConfiguration;
	
	/**
	 * Changes the settings related to tournaments.
	 * 
	 * @param tournamentConfiguration
	 * 		New settings.
	 */
	public void setTournamentConfiguration(TournamentConfiguration tournamentConfiguration)
	{	this.tournamentConfiguration = tournamentConfiguration;
	}
	
	/**
	 * Returns the settings related to tournaments.
	 * 
	 * @return
	 * 		Current settings.
	 */
	public TournamentConfiguration getTournamentConfiguration()
	{	return tournamentConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// QUICK MATCH			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Settings related to the quick match mode */
	private QuickMatchConfiguration quickMatchConfiguration;
	
	/**
	 * Changes the settings related to the quick match mode.
	 * 
	 * @param quickMatchConfiguration
	 * 		New settings.
	 */
	public void setQuickMatchConfiguration(QuickMatchConfiguration quickMatchConfiguration)
	{	this.quickMatchConfiguration = quickMatchConfiguration;
	}
	
	/**
	 * Returns the settings related to the quick match mode.
	 * 
	 * @return
	 * 		Current settings.
	 */
	public QuickMatchConfiguration getQuickMatchConfiguration()
	{	return quickMatchConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// QUICK START			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Settings related to the quick start mode */
	private QuickStartConfiguration quickStartConfiguration;
	
	/**
	 * Changes the settings related to quick start mode.
	 * 
	 * @param quickStartConfiguration
	 * 		New settings.
	 */
	public void setQuickStartConfiguration(QuickStartConfiguration quickStartConfiguration)
	{	this.quickStartConfiguration = quickStartConfiguration;
	}
	
	/**
	 * Returns the settings related to the quick start mode.
	 * 
	 * @return
	 * 		Current settings.
	 */
	public QuickStartConfiguration getQuickStartConfiguration()
	{	return quickStartConfiguration;
	}
}
