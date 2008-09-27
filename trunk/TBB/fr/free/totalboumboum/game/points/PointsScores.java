package fr.free.totalboumboum.game.points;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsScores extends PointsProcessor
{	
	private Score score;
	
	public PointsScores(Score score)
	{	this.score = score;	
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	long[] temp = stats.getScores(score);
		float result[] = new float[stats.getPlayers().size()];
		for(int i=0;i<result.length;i++)
			result[i] = temp[i];
		return result;
	}
}
