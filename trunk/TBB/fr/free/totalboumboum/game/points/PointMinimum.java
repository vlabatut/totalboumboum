package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointMinimum extends PointProcessor
{
	private PointProcessor source;
	
	public PointMinimum(PointProcessor source)
	{	this.source = source;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] temp = source.process(stats);
		// process
		float min = Float.MAX_VALUE;
		for(int i=0;i<temp.length;i++)
		{	if(temp[i]<min)
				min = temp[i];
		}
		for(int i=0;i<result.length;i++)
			result[i] = min;
		//
		return result;
	}
}
