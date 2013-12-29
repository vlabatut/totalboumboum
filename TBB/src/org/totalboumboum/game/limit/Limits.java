package org.totalboumboum.game.limit;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * Handles a list of limits for a given confrontation.
 * 
 * @param <T> 
 * 		Type of the handled limits.
 * 
 * @author Vincent Labatut
 */
public class Limits<T extends Limit> implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of limits handled by this object */
	List<T> limits = new ArrayList<T>();
	/** Currently selected limit */
	private int index;
	
	/**
	 * Adds a new limit to the list.
	 * 
	 * @param limit
	 * 		New limit to add to the list.
	 */
	public void addLimit(T limit)
	{	limits.add(limit);
		index = -1;
	}
	
	/**
	 * Selects the limit whose index is specified as a parameter.
	 * 
	 * @param index
	 * 		Index of the desired limit.
	 */
	public void selectLimit(int index)
	{	this.index = index;
	}

	/**
	 * Retursn an iterator over the limits.
	 * 
	 * @return
	 * 		Iterator object.
	 */
	public Iterator<T> iterator()
	{	return limits.iterator();
	}
	
	/**
	 * Returns the limit whose index is passed as a parameter.
	 * 
	 * @param index
	 * 		Position of the desired limit.
	 * @return
	 * 		Corresponding limit.
	 */
	public Limit getLimit(int index)
	{	return limits.get(index);		
	}
	
	/**
	 * Returns the number of limits in this object.
	 * 
	 * @return
	 * 		Number of limits.
	 */
	public int size()
	{	return limits.size();	
	}
	
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Uses the specified statistics to check if this limit has been reached.
	 * 
	 * @param holder
	 * 		Stat source.
	 * @return
	 * 		{@code true} iff the limit was reached.
	 */
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

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the points for each player, as defined in this limit object.
	 * 
	 * @param holder
	 * 		Stat source.
	 * @return
	 * 		Array of points.
	 */
	public float[] processPoints(StatisticHolder holder)
	{	Limit limit = limits.get(index);
		float[] result = limit.processPoints(holder);
		return result;
	}

	/**
	 * Returns the time-related limit contained
	 * in this object (if there's one).
	 * 
	 * @return
	 * 		A time limit object.
	 */
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
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	limits.clear();		
	}
}
