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
import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticBase;
import fr.free.totalboumboum.game.statistics.StatisticHolder;

/**
 * this limit is based on a given score (time, bombings, items...).
 * for example, a round can be stopped as soon as a player collects 30 items, or has 4 bombings
 * 
 * @author Vincent
 *
 */
public class LimitScore implements TournamentLimit, MatchLimit, RoundLimit
{
	public LimitScore(long limit, Score score, boolean supLimit, PointsProcessor pointProcessor)
	{	this.threshold = limit;
		this.score = score;
		this.supLimit = supLimit;
		this.pointProcessor = pointProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// LIMITED VALUE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Score score;

	public Score getScore()
	{	return score;
	}

	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long threshold;
	// is the limit a superior limit (or an inferior)
	private boolean supLimit;
	
	public long getThreshold()
	{	return threshold;
	}
	
	public void setThreshold(int threshold)
	{	this.threshold = threshold;
	}

	public void setsupLimit(boolean supLimit)
	{	this.supLimit = supLimit;
	}

	@Override
	public boolean testThreshold(StatisticHolder holder)
	{	boolean result = false;
		StatisticBase stats = holder.getStats();
		long scores[] = stats.getScores(score);
		int i=0;
		if(supLimit)
		{	while(i<scores.length && !result)
			{	result = scores[i]>=threshold;
				i++;
			}
		}
		else
		{	while(i<scores.length && !result)
			{	result = scores[i]<=threshold;
				i++;
			}
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
