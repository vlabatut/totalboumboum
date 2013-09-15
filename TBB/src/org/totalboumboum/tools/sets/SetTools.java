package org.totalboumboum.tools.sets;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.util.HashSet;
import java.util.Set;

/**
 * Methods related yo set management.
 * 
 * @author Vincent Labatut
 */
public class SetTools
{	
	/**
	 * Processes the complement of b relatively to a.
	 * In other words, if A={a,b,c,d,e} and B={a,b,e}
	 * then the result of this method is {c,d}.
	 * 
	 * @param a
	 * 		Reference set.
	 * @param b
	 * 		Set whose complement is wanted.
	 * @return
	 * 		Complement of {@code b} relatively to {@code a}.
	 */
	public static <T> Set<T> processComplement(Set<T> a, Set<T> b)
	{	Set<T> result = new HashSet<T>(a);
		for(T value: b)
			result.remove(value);
		return result;
	}
}
