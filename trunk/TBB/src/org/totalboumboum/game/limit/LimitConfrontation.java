package org.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This limit is based on the number of confrontations during a tournament or match.
 * For example, a match can be limited to 10 rounds, or a sequence tournament to 5 matches.
 * 
 * @author Vincent Labatut
 */
public class LimitConfrontation implements TournamentLimit, MatchLimit
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new limit object.
	 * 
	 * @param threshold
	 * 		Threshold value.
	 * @param comparatorCode
	 * 		How the comparison is performed.
	 * @param pointProcessor
	 * 		Associated point processor.
	 */
	public LimitConfrontation(int threshold, Comparisons comparatorCode, AbstractPointsProcessor pointProcessor)
	{	this.threshold = threshold;	
		this.comparatorCode = comparatorCode;
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Threshold of this limit object */
	private int threshold;
	/** Type of the performed comparison */ 
	private Comparisons comparatorCode;
	
	/**
	 * Returns the threshold value of this limit object.
	 * 
	 * @return
	 * 		Threshold value.
	 */
	public int getThreshold()
	{	return threshold;
	}

	/**
	 * Changes the threshold value of this limit object.
	 * 
	 * @param threshold
	 * 		New threshold value.
	 */
	public void setThreshold(int threshold)
	{	this.threshold = threshold;
	}

	/**
	 * Returns the comparison mode of this limit object.
	 * 
	 * @return
	 * 		Comparison mode.
	 */
	public Comparisons getComparatorCode()
	{	return comparatorCode;
	}
	
	/**
	 * Changes the comparison mode of this limit object.
	 * 
	 * @param comparatorCode
	 * 		New comparison mode.
	 */
	public void setComparatorCode(Comparisons comparatorCode)
	{	this.comparatorCode = comparatorCode;
	}

	@Override
	public boolean testThreshold(StatisticHolder holder)
	{	boolean result = false;
		StatisticBase stats = holder.getStats();
		int nbr = stats.getConfrontationCount();
		switch(comparatorCode)
		{	case EQUAL:
				result = nbr==threshold;
				break;
			case GREATER:
				result = nbr>threshold;
				break;
			case GREATEREQ:
				result = nbr>=threshold;
				break;
			case LESS:
				result = nbr<threshold;
				break;
			case LESSEQ:
				result = nbr<=threshold;
				break;
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Associated point processor */
	private AbstractPointsProcessor pointProcessor;
	
	@Override
	public AbstractPointsProcessor getPointProcessor()
	{	return pointProcessor;
	}

	/**
	 * Changes the associated point processor.
	 *  
	 * @param pointProcessor
	 * 		New point processor.
	 */
	public void setPointProcessor(AbstractPointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}

	@Override
	public float[] processPoints(StatisticHolder holder)
	{	return pointProcessor.process(holder);		
	}
}
