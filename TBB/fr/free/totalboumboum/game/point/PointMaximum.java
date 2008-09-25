package fr.free.totalboumboum.game.point;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointMaximum extends PointProcessor
{
	private PointProcessor source;
	
	public PointMaximum(PointProcessor source)
	{	this.source = source;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] temp = source.process(stats);
		// process
		float max = Float.MIN_VALUE;
		for(int i=0;i<temp.length;i++)
		{	if(temp[i]>max)
				max = temp[i];
		}
		for(int i=0;i<result.length;i++)
			result[i] = max;
		//
		return result;
	}
}
