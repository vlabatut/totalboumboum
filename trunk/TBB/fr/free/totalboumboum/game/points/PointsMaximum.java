package fr.free.totalboumboum.game.points;

import java.text.NumberFormat;
import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsMaximum extends PointsProcessor implements PPFunction
{
	private PointsProcessor source;
	
	public PointsMaximum(PointsProcessor source)
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

	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// function
		result.append("Max");
		// argument
		result.append("(");
		result.append(source.toString());
		result.append(")");
		// result
		return result.toString();
	}
}
