package fr.free.totalboumboum.game.points;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsPoints extends PointsProcessor
{	
	@Override
	public float[] process(StatisticBase stats)
	{	float result[] = stats.getPartialPoints();
		return result;
	}
}
