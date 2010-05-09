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

public class LimitPoints implements TournamentLimit, MatchLimit, RoundLimit
{
	private float limit;
	private PointsProcessor pointProcessor;

	public LimitPoints(float limit)
	{	this.limit = limit;
	}
	
	public PointsProcessor getPointProcessor()
	{	return pointProcessor;
	}
	public void setPointProcessor(PointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}

	public float getLimit()
	{	return limit;
	}

	public void setLimit(float limit)
	{	this.limit = limit;
	}

	@Override
	public int testLimit(StatisticBase stats)
	{	int result = -1;
		float points[] = pointProcessor.process(stats);
		int i=0;
		while(i<points.length && result<0)
		{	if(points[i]>=limit)
				result = i;
			else
				i++;
		}
		return result;
	}

}
