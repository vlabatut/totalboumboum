package fr.free.totalboumboum.game.point;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointTotal extends PointProcessor
{
	private PointProcessor source;
	
	public PointTotal(PointProcessor source)
	{	this.source = source;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] temp = source.process(stats);
		// process
		float sum = 0;
		for(int i=0;i<temp.length;i++)
			sum = sum + temp[i];
		for(int i=0;i<result.length;i++)
			result[i] = sum;
		//
		return result;
	}
	
}
