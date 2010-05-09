package fr.free.totalboumboum.game.point;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointMultiplication implements PointProcessor
{
	private PointProcessor leftSource;
	private PointProcessor rightSource;
	
	public PointMultiplication(PointProcessor leftSource, PointProcessor rightSource)
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
			result[i] = leftTemp[i] * rightTemp[i];
		//
		return result;
	}
	
}
