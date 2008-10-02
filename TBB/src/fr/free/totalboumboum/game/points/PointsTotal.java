package fr.free.totalboumboum.game.points;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsTotal extends PointsProcessor implements PPConstant
{	
	@Override
	public float[] process(StatisticBase stats)
	{	float result[] = stats.getPartialPoints();
		return result;
	}

	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// value
		result.append("Total");
		// result
		return result.toString();
	}
}
