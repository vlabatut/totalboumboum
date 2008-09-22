package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticBase;

public class ScoreLimit extends Limit
{
	private long limit;
	private Score score;
	private boolean maxLimit;

	public long getLimit()
	{	return limit;
	}

	public void setLimit(int limit)
	{	this.limit = limit;
	}

	public void setMaxLimit(boolean maxLimit)
	{	this.maxLimit = maxLimit;
	}

	@Override
	public boolean testLimit(StatisticBase stats)
	{	boolean result = false;
		long scores[] = stats.getScores(score);
		int i=0;
		if(maxLimit)
		{	while(i<scores.length && !result)
				result = scores[i]>=limit;
		}
		else
		{	while(i<scores.length && !result)
				result = scores[i]<=limit;
		}
		return result;
	}
}
