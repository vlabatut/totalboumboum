package fr.free.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.statistics.StatisticBase;
import fr.free.totalboumboum.game.statistics.StatisticHolder;

/**
 * this limit is based on time. In a round, a similar limit could be defined 
 * whith a ScoreLimit, but not in a tournament or match. Moreover, if there is no
 * player left in the round (which is theoretically possible), then the ScoreLimit 
 * based on time is no longer equivalent to the TimeLimit.
 * For example, a tournament can be stopped when one player's cumulated time is
 * greater than 10 minutes.   
 * 
 * @author Vincent
 *
 */
public class LimitTime implements TournamentLimit, MatchLimit, RoundLimit
{	private static final long serialVersionUID = 1L;

	public LimitTime(long threshold, ComparatorCode comparatorCode, PointsProcessor pointProcessor)
	{	this.threshold = threshold;	
		this.comparatorCode = comparatorCode;
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long threshold;
	private ComparatorCode comparatorCode;

	public long getThreshold()
	{	return threshold;
	}

	public void setThreshold(long threshold)
	{	this.threshold = threshold;
	}

	public ComparatorCode getSupLimit()
	{	return comparatorCode;
	}
	
	public void setsupLimit(ComparatorCode comparatorCode)
	{	this.comparatorCode = comparatorCode;
	}

	@Override
	public boolean testThreshold(StatisticHolder holder)
	{	boolean result = false;
		StatisticBase stats = holder.getStats();
		switch(comparatorCode)
		{	case EQUAL:
			result = stats.getTotalTime()==threshold;
				break;
			case GREATER:
				result = stats.getTotalTime()>threshold;
				break;
			case GREATEREQ:
				result = stats.getTotalTime()>=threshold;
				break;
			case LESS:
				result = stats.getTotalTime()<threshold;
				break;
			case LESSEQ:
				result = stats.getTotalTime()<=threshold;
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
