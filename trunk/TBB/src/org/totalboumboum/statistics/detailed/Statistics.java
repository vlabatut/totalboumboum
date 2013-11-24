package org.totalboumboum.statistics.detailed;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stats as defined at the level of the whole game.
 * 
 * @author Vincent Labatut
 */
public class Statistics implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/** List of tournament stats */
	private final List<StatisticTournament> tournaments = new ArrayList<StatisticTournament>();
	
	/**
	 * Adds a new tournament stat to the current list.
	 * 
	 * @param tournament
	 * 		Stats to be added to the list.
	 */
	public void addGame(StatisticTournament tournament)
	{	tournaments.add(tournament);
	}
}
