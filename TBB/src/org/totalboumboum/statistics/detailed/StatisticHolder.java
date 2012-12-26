package org.totalboumboum.statistics.detailed;

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

import java.util.List;

import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;

/**
 * Implemented by objects able to generate
 * data to process statistics.
 * 
 * @author Vincent Labatut
 */
public interface StatisticHolder
{
	/**
	 * Returns the statistics for the associated game.
	 * 
	 * @return
	 * 		Some statistics.
	 */
	public StatisticBase getStats();
	
	/**
	 * Returns the list of involved players .
	 * 
	 * @return
	 * 		List of profiles.
	 */
	public List<Profile> getProfiles();
	
	/**
	 * Returns the statuses of the players.
	 * 
	 * @return
	 * 		The statistics of this tournament.
	 */
	public List<Boolean> getPlayersStatus();
	
	public Ranks getOrderedPlayers();
}
