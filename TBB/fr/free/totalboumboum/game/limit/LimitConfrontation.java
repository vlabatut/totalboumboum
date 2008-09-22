package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class LimitConfrontation extends Limit
{
	private int limit;

	public LimitConfrontation(int limit)
	{	this.limit = limit;	
	}
	
	public int getLimit()
	{	return limit;
	}

	public void setLimit(int limit)
	{	this.limit = limit;
	}

	@Override
	public int testLimit(StatisticBase stats)
	{	int result = -1;
		int nbr = stats.getConfrontationCount();
		if(nbr>=limit)
			result = stats.getPlayers().size();
		return result;
	}
}
