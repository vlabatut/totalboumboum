package org.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Limits<T extends Limit> implements Serializable
{	private static final long serialVersionUID = 1L;

	List<T> limits = new ArrayList<T>();
	private int index;
	
	public void addLimit(T limit)
	{	limits.add(limit);
		index = -1;
	}
	
	public boolean testLimit(StatisticHolder holder)
	{	boolean result = false;
		index = -1;
		Iterator<T> it = limits.iterator();
		int i = 0;
		while(it.hasNext() && !result)
		{	T temp = it.next();
			result = temp.testThreshold(holder);
			if(result)
				index = i;
			else
				i++;
		}
		return result;
	}

	public float[] processPoints(StatisticHolder holder)
	{	Limit limit = limits.get(index);
		float[] result = limit.processPoints(holder);
		return result;
	}
	
	public LimitTime getTimeLimit()
	{	LimitTime result = null;
		Iterator<T> it = limits.iterator();
		while(it.hasNext() && result==null)
		{	T limit = it.next();
			if(limit instanceof LimitTime)
				result = (LimitTime)limit;
		}
		return result;
	}
	
	public void selectLimit(int index)
	{	this.index = index;
	}
	
	public Iterator<T> iterator()
	{	return limits.iterator();
	}
	
	public Limit getLimit(int index)
	{	return limits.get(index);		
	}
	
	public void finish()
	{	limits.clear();		
	}
	
	public int size()
	{	return limits.size();	
	}
}
