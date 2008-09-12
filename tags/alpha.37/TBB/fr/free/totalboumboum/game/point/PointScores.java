package fr.free.totalboumboum.game.point;

import fr.free.totalboumboum.data.statistics.StatisticBase;
import fr.free.totalboumboum.game.score.Score;

public class PointScores implements PointProcessor
{	
	private Score score;
	
	public PointScores(Score score)
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
