package fr.free.totalboumboum.game.limit;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class Limits<T extends Limit>
{
	ArrayList<T> limits = new ArrayList<T>();
	
	public void addLimit(T limit)
	{	limits.add(limit);
	}
	
	public int testLimits(StatisticBase stats)
	{	int result = -1;
		Iterator<T> i = limits.iterator();
		while(i.hasNext() && result<0)
		{	T temp = i.next();
			result = temp.testLimit(stats);
		}
		return result;
	}

	public Iterator<T> iterator()
	{	return limits.iterator();
	}
	
	public void finish()
	{	limits.clear();		
	}
}
