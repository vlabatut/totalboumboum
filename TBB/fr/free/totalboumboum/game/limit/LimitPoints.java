package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;
import fr.free.totalboumboum.game.points.PointProcessor;

public class LimitPoints implements TournamentLimit, MatchLimit, RoundLimit
{
	private float limit;
	private PointProcessor pointProcessor;

	public LimitPoints(float limit)
	{	this.limit = limit;
	}
	
	public PointProcessor getPointProcessor()
	{	return pointProcessor;
	}
	public void setPointProcessor(PointProcessor pointProcessor)
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
