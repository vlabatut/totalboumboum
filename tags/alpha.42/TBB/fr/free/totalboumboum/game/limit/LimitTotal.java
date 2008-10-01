package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class LimitTotal implements TournamentLimit, MatchLimit
{
	private float limit;
	
	public LimitTotal(float limit)
	{	this.limit = limit;	
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
		float points[] = stats.getPartialPoints();
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
