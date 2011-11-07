package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsSummation extends PointsProcessor implements PPFunction
{
	private PointsProcessor source;
	
	public PointsSummation(PointsProcessor source)
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
	
	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// function
		result.append("Sum");
		// argument
		result.append("(");
		result.append(source.toString());
		result.append(")");
		// result
		return result.toString();
	}
}