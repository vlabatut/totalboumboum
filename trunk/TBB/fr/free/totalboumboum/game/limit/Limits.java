package fr.free.totalboumboum.game.limit;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class Limits
{
	ArrayList<Limit> limits = new ArrayList<Limit>();
	
	public void addLimit(Limit limit)
	{	limits.add(limit);
	}
	
	public int testLimits(StatisticBase stats)
	{	int result = -1;
		Iterator<Limit> i = limits.iterator();
		while(i.hasNext() && result<0)
		{	Limit temp = i.next();
			result = temp.testLimit(stats);
		}
		return result;
	}

	public Iterator<Limit> iterator()
	{	return limits.iterator();
	}
	
	public void finish()
	{	limits.clear();		
	}
}
