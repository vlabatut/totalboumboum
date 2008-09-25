package fr.free.totalboumboum.game.point;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointPoints extends PointProcessor
{	
	@Override
	public float[] process(StatisticBase stats)
	{	float result[] = stats.getPartialPoints();
		return result;
	}
}
