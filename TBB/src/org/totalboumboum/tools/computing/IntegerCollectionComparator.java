package org.totalboumboum.tools.computing;

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

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Compares two collections of integers.
 * First using their sizes, then their
 * content.
 * 
 * @author Vincent Labatut
 */
public class IntegerCollectionComparator implements Comparator<Collection<Integer>>
{	@Override
	public int compare(Collection<Integer> s1, Collection<Integer> s2)
	{	int size1 = s1.size();
		int size2 = s2.size();
		int result = size1-size2;
		if(result==0)
		{	Iterator<Integer> i1 = s1.iterator();
			Iterator<Integer> i2 = s2.iterator();
			boolean done;
			do
			{	int v1 = i1.next();
				int v2 = i2.next();
				result = v1-v2;
				if(result==0)
					done = !i1.hasNext();
				else
					done = true;
			}
			while(!done);
		}
		return result;
	}			
}
