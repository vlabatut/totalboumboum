package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class LimitTime implements RoundLimit
{
	private long limit;
	
	public LimitTime(long limit)
	{	this.limit = limit;	
	}
	
	public long getLimit()
	{	return limit;
	}

	public void setLimit(long limit)
	{	this.limit = limit;
	}

	@Override
	public int testLimit(StatisticBase stats)
	{	int result = -1;
		if(stats.getTime()>=limit)
			result = stats.getPlayers().size(); 
		return result;
	}

}
