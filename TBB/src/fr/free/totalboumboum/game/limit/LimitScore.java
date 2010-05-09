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

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticBase;

public class LimitScore implements TournamentLimit, MatchLimit, RoundLimit
{
	private long limit;
	private Score score;
	// is the limit a superior limit (or an inferior)
	private boolean supLimit;
	// if the limit is crossed, does the player win ?
	@SuppressWarnings("unused")
	private boolean win;

	public LimitScore(long limit, Score score, boolean supLimit, boolean win)
	{	this.limit = limit;
		this.score = score;
		this.supLimit = supLimit;
		this.win = win;
	}

	public long getLimit()
	{	return limit;
	}
	
	public Score getScore()
	{	return score;
	}

	public void setLimit(int limit)
	{	this.limit = limit;
	}

	public void setsupLimit(boolean supLimit)
	{	this.supLimit = supLimit;
	}

	@Override
	public int testLimit(StatisticBase stats)
	{	int result = -1;
		long scores[] = stats.getScores(score);
		int i=0;
		if(supLimit)
		{	while(i<scores.length && result<0)
			{	if(scores[i]>=limit)
					result = i;
				else
					i++;
			}
		}
		else
		{	while(i<scores.length && result<0)
			{	if(scores[i]<=limit)
					result = i;
				else
					i++;
			}
		}
		return result;
	}
}
