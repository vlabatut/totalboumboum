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
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This limit is based on the number of points calculated by its PointProcessor.
 * For example, a match can be stopped as soon as a player scores more than 50 points.
 * 
 * @author Vincent Labatut
 */
public class LimitPoints implements TournamentLimit, MatchLimit, RoundLimit
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
	 * @param thresholdPointProcessor
	 * 		Point processor used to compute the thresholded value.
	 */
	public LimitPoints(float threshold, Comparisons comparatorCode, AbstractPointsProcessor pointProcessor, AbstractPointsProcessor thresholdPointProcessor)
	{	this.threshold = threshold;
		this.comparatorCode = comparatorCode;
		this.thresholdPointProcessor = thresholdPointProcessor;
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITED VALUE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Point processor used by this limit */
	private AbstractPointsProcessor thresholdPointProcessor;

	/**
	 * Returns the point processor used by this limit.
	 * 
	 * @return
	 * 		Point processor used by this limit
	 */
	public AbstractPointsProcessor getThresholdPointProcessor()
	{	return thresholdPointProcessor;
	}
	
	/**
	 * Changes the point processor used by this limit.
	 * 
	 * @param thresholdPointProcessor
	 * 		New point processor used by this limit
	 */
	public void setThresholdPointProcessor(AbstractPointsProcessor thresholdPointProcessor)
	{	this.thresholdPointProcessor = thresholdPointProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Threshold of this limit object */
	private float threshold;
	/** Type of the performed comparison */ 
	private Comparisons comparatorCode;

	/**
	 * Returns the threshold value of this limit object.
	 * 
	 * @return
	 * 		Threshold value.
	 */
	public float getThreshold()
	{	return threshold;
	}

	/**
	 * Changes the threshold value of this limit object.
	 * 
	 * @param threshold
	 * 		New threshold value.
	 */
	public void setThreshold(float threshold)
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
		float points[] = pointProcessor.process(holder);
		int i=0;
		switch(comparatorCode)
		{	case EQUAL:
				while(i<points.length && !result)
				{	result = points[i]==threshold;
					i++;
				}
				break;
			case GREATER:
				while(i<points.length && !result)
				{	result = points[i]>threshold;
					i++;
				}
				break;
			case GREATEREQ:
				while(i<points.length && !result)
				{	result = points[i]>=threshold;
					i++;
				}
				break;
			case LESS:
				while(i<points.length && !result)
				{	result = points[i]<threshold;
					i++;
				}
				break;
			case LESSEQ:
				while(i<points.length && !result)
				{	result = points[i]<=threshold;
					i++;
				}
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
	public AbstractPointsProcessor getPointsProcessor()
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
