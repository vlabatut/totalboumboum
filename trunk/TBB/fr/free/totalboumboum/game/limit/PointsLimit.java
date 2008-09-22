package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsLimit extends Limit
{
	private float limit;

	public float getLimit()
	{	return limit;
	}

	public void setLimit(float limit)
	{	this.limit = limit;
	}

	@Override
	public boolean testLimit(StatisticBase stats)
	{	boolean result = false;
		float points[] = stats.getPartialPoints();
		int i=0;
		while(i<points.length && !result)
			result = points[i]>=limit;
		return result;
	}

}
