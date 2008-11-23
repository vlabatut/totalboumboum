package fr.free.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
{
	public LimitTime(long limit, PointsProcessor pointProcessor)
	{	this.threshold = limit;	
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long threshold;

	public long getThreshold()
	{	return threshold;
	}

	public void setThreshold(long threshold)
	{	this.threshold = threshold;
	}

	@Override
	public boolean testThreshold(StatisticHolder holder)
	{	int result = -1;
		if(stats.getTime()>=threshold)
			result = stats.getPlayers().size(); 
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
	{	
		
	}
}
