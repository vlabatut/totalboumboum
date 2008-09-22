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
	
	public boolean testLimits(StatisticBase stats)
	{	boolean result = false;
		Iterator<Limit> i = limits.iterator();
		while(i.hasNext() && !result)
		{	Limit temp = i.next();
			result = temp.testLimit(stats);
		}
		return result;
	}
	
	public void finish()
	{	limits.clear();		
	}
}
