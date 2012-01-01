package org.totalboumboum.game.limit;

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

import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * this limit is based on the number of points calculated by its PointProcessor.
 * for example, a match can be stopped as soon as a player scores more than 50 points.
 * 
 * @author Vincent Labatut
 *
 */
public class LimitPoints implements TournamentLimit, MatchLimit, RoundLimit
{	private static final long serialVersionUID = 1L;

	public LimitPoints(float threshold, Comparisons comparatorCode, PointsProcessor pointProcessor, PointsProcessor thresholdPointProcessor)
	{	this.threshold = threshold;
		this.comparatorCode = comparatorCode;
		this.thresholdPointProcessor = thresholdPointProcessor;
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITED VALUE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor thresholdPointProcessor;

	public PointsProcessor getThresholdPointProcessor()
	{	return thresholdPointProcessor;
	}
	public void setThresholdPointProcessor(PointsProcessor thresholdPointProcessor)
	{	this.thresholdPointProcessor = thresholdPointProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float threshold;
	private Comparisons comparatorCode;

	public float getThreshold()
	{	return threshold;
	}

	public void setThreshold(float threshold)
	{	this.threshold = threshold;
	}

	public Comparisons getComparatorCode()
	{	return comparatorCode;
	}
	
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
	private PointsProcessor pointProcessor;
	
	public PointsProcessor getPointProcessor()
	{	return pointProcessor;
	}

	public void setPointProcessor(PointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}

	@Override
	public float[] processPoints(StatisticHolder holder)
	{	return pointProcessor.process(holder);		
	}
}
