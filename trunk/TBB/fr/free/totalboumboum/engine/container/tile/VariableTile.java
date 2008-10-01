package fr.free.totalboumboum.engine.container.tile;

import java.util.ArrayList;
import java.util.Iterator;


public class VariableTile
{
	private ArrayList<ValueTile> values = new ArrayList<ValueTile>();
	private ArrayList<Integer> counts;
	private String name;//debug
	private int totalCount;
	private int totalOccurrences = 0;
	
	public VariableTile(String name)
	{	this.name = name;
	}
	
	public void incrementOccurrencesCount()
	{	totalOccurrences++;
	}
	
	public void init()
	{	counts = new ArrayList<Integer>();
		for(int i=0;i<values.size();i++)
			counts.add(0);
		totalCount = 0;
	}
	
	/**
	 * Gets the next value for this variable, according to its probability distribution.
	 * At first, I used Math.random() directly, but since the length of the series was too short,
	 * the properties of the distribution were not respected. The controlled version allows a better
	 * respect of the probability distribution, by monitoring the generated values, but the resulting 
	 * zone is always the same ! So a mixture of these two methods is used : half the values are 
	 * generated completely randomly, and the rest is generated in a controlled way. 
	 * @return	a value generated randomly
	 */
	public ValueTile getNext()
	{	ValueTile result;
		if(totalCount%2 == 0)
			result = getNextControlled();
		else
			result = getNextRandomly();
		return result;
	}
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
		ArrayList<Integer> indices = new ArrayList<Integer>();
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
	
	public ArrayList<ValueTile> getValues()
	{	return values;
	}
	public void addValue(ValueTile valueItem)
	{	values.add(valueItem);		
	}
	public void setProba(int index, float proba)
	{	values.get(index).setProba(proba);				
	}
	public float getProba(int index)
	{	return values.get(index).getProba();				
	}

	public String getName()
	{	return name;
	}
}
