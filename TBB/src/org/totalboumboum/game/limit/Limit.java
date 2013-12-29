package org.totalboumboum.game.limit;

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

import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * Object used to define the end of a confrontation,
 * be it a round, match or tournament.
 *  
 * @author Vincent Labatut
 */
public interface Limit extends Serializable
{
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Uses the specified statistics to check if this limit has been reached.
	 * 
	 * @param holder
	 * 		Stat source.
	 * @return
	 * 		{@code true} iff the limit was reached.
	 */
	public boolean testThreshold(StatisticHolder holder);

	/////////////////////////////////////////////////////////////////
	// POINTS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the points for each player, as defined in this limit object.
	 * 
	 * @param holder
	 * 		Stat source.
	 * @return
	 * 		Array of points.
	 */
	public float[] processPoints(StatisticHolder holder);
	
	/**
	 * Returns the point processor used to compute the points.
	 *
	 * @return
	 * 		A point processor.
	 */
	public AbstractPointsProcessor getPointProcessor();
}
