package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsAddition extends PointsProcessor implements PPSecondaryOperator
{
	private PointsProcessor leftSource;
	private PointsProcessor rightSource;
	
	public PointsAddition(PointsProcessor leftSource, PointsProcessor rightSource)
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
			result[i] = leftTemp[i] + rightTemp[i];
		//
		return result;
	}
	
	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// left operand
		result.append(leftSource.toString());
		// operator
		result.append(new Character('\u002B').toString());
		// right operand
		result.append(rightSource.toString());
		// result
		return result.toString();
	}
}
