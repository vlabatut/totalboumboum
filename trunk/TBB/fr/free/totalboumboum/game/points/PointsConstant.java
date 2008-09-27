package fr.free.totalboumboum.game.points;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsConstant extends PointsProcessor
{	
	private float value;
	
	public PointsConstant(float value)
	{	this.value = value;	
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	int nbr = stats.getPlayers().size();
		float result[] = new float[nbr];
		for(int i=0;i<nbr;i++)
			result[i] = value;
		return result;
	}
}
