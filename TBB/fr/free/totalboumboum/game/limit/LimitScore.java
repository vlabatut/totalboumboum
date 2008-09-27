package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticBase;

public class LimitScore implements TournamentLimit, MatchLimit, RoundLimit
{
	private long limit;
	private Score score;
	// is the limit a superior limit (or an inferior)
	private boolean supLimit;
	// if the limit is crossed, does the player win ?
	private boolean win;

	public LimitScore(long limit, Score score, boolean supLimit, boolean win)
	{	this.limit = limit;
		this.score = score;
		this.supLimit = supLimit;
		this.win = win;
	}

	public long getLimit()
	{	return limit;
	}
	
	public Score getScore()
	{	return score;
	}

	public void setLimit(int limit)
	{	this.limit = limit;
	}

	public void setsupLimit(boolean supLimit)
	{	this.supLimit = supLimit;
	}

	@Override
	public int testLimit(StatisticBase stats)
	{	int result = -1;
		long scores[] = stats.getScores(score);
		int i=0;
		if(supLimit)
		{	while(i<scores.length && result<0)
			{	if(scores[i]>=limit)
					result = i;
				else
					i++;
			}
		}
		else
		{	while(i<scores.length && result<0)
			{	if(scores[i]<=limit)
					result = i;
				else
					i++;
			}
		}
		return result;
	}
}
