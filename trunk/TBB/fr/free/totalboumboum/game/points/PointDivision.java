package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointDivision extends PointProcessor
{
	private PointProcessor leftSource;
	private PointProcessor rightSource;
	
	public PointDivision(PointProcessor leftSource, PointProcessor rightSource)
	{	this.leftSource = leftSource;
		this.rightSource = rightSource;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] leftTemp = leftSource.process(stats);
		float[] rightTemp = rightSource.process(stats);
		// process
		for(int i=0;i<result.length;i++)
		{	if(rightTemp[i]==0) //division by zero
				result[i] = Float.MAX_VALUE;
			else
				result[i] = leftTemp[i] * rightTemp[i];
		}
		//
		return result;
	}
	
}
