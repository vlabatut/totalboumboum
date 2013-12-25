package org.totalboumboum.game.points;

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

import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This {@code PointsProcessor} sends back one score as a result.
 * <br/>
 * For example, if the first player had picked 15 items, the second none and 
 * the third 7, and if the items scores were processed, then the result would 
 * be {15,0,7} 
 * 
 * @author Vincent Labatut
 */
public class PointsProcessorScores extends AbstractPointsProcessor implements InterfacePointsProcessorConstant
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new {@code PointsProcessor}.
	 * 
	 * @param score
	 * 		Score from which the values are used by this {@code PointsProcessor}.
	 */
	public PointsProcessorScores(Score score)
	{	this.score = score;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Score concerned by this processor */
	private Score score;
	
	/**
	 * Returns the {@link Score} concerned by this processor.
	 * 
	 * @return
	 * 		Score type concerned by this processor.
	 */
	public Score getScore()
	{	return score;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	StatisticBase stats = holder.getStats();
		long[] temp = stats.getScores(score);
		float result[] = new float[stats.getPlayersIds().size()];
		for(int i=0;i<result.length;i++)
			result[i] = temp[i];
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// value
		result.append(score.stringFormat());
		// result
		return result.toString();
	}
}
