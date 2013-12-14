package org.totalboumboum.engine.container.level.variabletile;

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

/**
 * Represents a random variable whose values are tiles.
 * It is used to introduce random element when defining zones.
 * 
 * @author Vincent Labatut
 */
public class VariableTile implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new random tile variable
	 * with the specified name.
	 * 
	 * @param name
	 * 		Variable name (used for later reference).
	 */
	public VariableTile(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Variable name */
	private String name;//debug

	/**
	 * Returns the name of this variable.
	 * 
	 * @return
	 * 		The variable name.
	 */
	public String getName()
	{	return name;
	}

	/////////////////////////////////////////////////////////////////
	// VALUES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of possible values for this variable */
	private List<ValueTile> values = new ArrayList<ValueTile>();

	/**
	 * Returns the list of possible values for this variable.
	 * 
	 * @return
	 * 		List of tile values.
	 */
	public List<ValueTile> getValues()
	{	return values;
	}
	
	/**
	 * Adds a new value to the list of possible
	 * values of this variable.
	 * 
	 * @param value
	 * 		New tile value to add.
	 */
	public void addValue(ValueTile value)
	{	values.add(value);		
	}
	
	/////////////////////////////////////////////////////////////////
	// PROBAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Change the probability for the tile value of specified
	 * index to be drawn. 
	 * 
	 * @param index 
	 * 		Index of the value.
	 * @param proba 
	 * 		Probability for the value to be drawn.
	 */
	public void setProba(int index, float proba)
	{	values.get(index).setProba(proba);				
	}
	
	/**
	 * Returns the probability of the value
	 * whose index is specified, to be drawn.
	 * 
	 * @param index
	 * 		Index of the value of interest.
	 * @return
	 * 		The associated probability measure.
	 */
	public float getProba(int index)
	{	return values.get(index).getProba();				
	}

	/////////////////////////////////////////////////////////////////
	// OCCURRENCES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of occurrence of each value */ 
	private List<Integer> counts;
	/** Total number of drawn values */
	private int totalCount;
	/** Total number of occurrences (as specified in the zone) */
	@SuppressWarnings("unused")
	private int totalOccurrences = 0;

	/**
	 * Counts one more occurrence.
	 */
	public void incrementOccurrencesCount()
	{	totalOccurrences++;
	}
	
	/**
	 * Initializes all variables used to count
	 * values. Those are used to keep controle
	 * of the drawing process.
	 */
	public void init()
	{	counts = new ArrayList<Integer>();
		for(int i=0;i<values.size();i++)
			counts.add(0);
		totalCount = 0;
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Gets the next value for this variable, according to its probability distribution.
	 * At first, I used {@code Math.random()} directly, but since the length of the series was too short,
	 * the properties of the distribution were not respected. The controlled version allows a better
	 * respect of the probability distribution, by monitoring the generated values, but the resulting 
	 * zone is always the same ! So a mixture of these two methods is used : half the values are 
	 * generated completely randomly, and the rest is generated in a controlled way. 
	 * 
	 * @return
	 * 		A value generated randomly.
	 */
	public ValueTile getNext()
	{	ValueTile result;
		if(totalCount%2 == 0)
			result = getNextControlled();
		else
			result = getNextRandomly();
		return result;
	}
	
	/**
	 * Select a value for the next draw,
	 * using the distribution of already
	 * drawn values.
	 * 
	 * @return
	 * 		A tile value.
	 */
	private ValueTile getNextControlled()
	{	ValueTile result;
		// count frequencies
		double[] def = new double[values.size()];
		Iterator<ValueTile> i = values.iterator();
		Iterator<Integer> j = counts.iterator();
		int k = 0;
		while(i.hasNext())
		{	ValueTile vt = i.next();
			int count = j.next();
			double the = vt.getProba();
			double act;
			if(totalCount==0)
				act = 0;
			else
				act = count/(float)totalCount;
			double diff = act-the;
			def[k] = diff;
			k++;
		}
		// calculate minima
		int indexMin = 0;
		List<Integer> indices = new ArrayList<Integer>();
		for(k=0;k<def.length;k++)
		{	if(def[k]<def[indexMin])
			{	indices.clear();
				indices.add(k);
				indexMin = k;
			}
			else if(def[k]==def[indexMin])
			{	indices.add(k);				
			}
		}
		// result
		if(indices.size()>1)
		{	int proba = (int)(Math.random()*indices.size());
			indexMin = indices.get(proba);
		}
		result = values.get(indexMin);
		int count = counts.get(indexMin);
		count ++;
		counts.set(indexMin,new Integer(count));
		totalCount ++;
		return result;
	}
	
	/**
	 * Draws the next tile value completely randomly.
	 * 
	 * @return
	 * 		Tile value.
	 */
	private ValueTile getNextRandomly()
	{	ValueTile result = null;
		totalCount++;
		double proba = Math.random();
		float p = 0;
		Iterator<ValueTile> i = values.iterator();
		int j = 0;
		while(i.hasNext() && result==null)
		{	ValueTile vit = i.next();
			Float f = vit.getProba();
			p = p + f;
			if(proba<=p)
			{	result = vit;
				int count = counts.get(j);
				count ++;
				counts.set(j,new Integer(count));
			}
			else
				j++;
		}
		return result;
	}
}
