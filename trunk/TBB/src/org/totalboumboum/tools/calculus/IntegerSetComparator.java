package org.totalboumboum.tools.calculus;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class IntegerSetComparator implements Comparator<Set<Integer>>
{	@Override
	public int compare(Set<Integer> s1, Set<Integer> s2)
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
