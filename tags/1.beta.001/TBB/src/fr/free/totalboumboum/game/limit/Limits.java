package fr.free.totalboumboum.game.limit;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
	
	public int size()
	{	return limits.size();	
	}
}