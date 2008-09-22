package fr.free.totalboumboum.game.limit;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class ConfrontationLimit extends Limit
{
	private int limit;

	public int getLimit()
	{	return limit;
	}

	public void setLimit(int limit)
	{	this.limit = limit;
	}

	@Override
	public boolean testLimit(StatisticBase stats)
	{	boolean result;
		int nbr = stats.getConfrontationCount();
		result = nbr>=limit;
		return result;
	}
}
