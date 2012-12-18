package org.totalboumboum.game.points;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;


/**
 * This PointsProcessor calculates some rankings in function of the results 
 * coming from the source PointsProcessor objects. The position of the source
 * in the list determines its priority while evaluating the rankings.
 * <br/>
 * For example, if the sources were {12,5,5} and {0,4,6} then the rankings would be {1,3,2} 
 * <br/>
 * <b>Note:</b> the order can be normal (ie. increasing) or inverted (ie. decreasing).
 * 
 * @author Vincent Labatut
 */
public class PointsRankings extends PointsProcessor implements PPFunction
{	private static final long serialVersionUID = 1L;

	public PointsRankings(List<PointsProcessor> sources, boolean inverted)
	{	this.sources.addAll(sources);
		this.inverted = inverted;
	}
	
	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean inverted;
	
	public boolean isInverted()
	{	return inverted;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<PointsProcessor> sources = new ArrayList<PointsProcessor>();

	public List<PointsProcessor> getSources()
	{	return sources;		
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	// source
		StatisticBase stats = holder.getStats();
		List<float[]> values = new ArrayList<float[]>();
		Iterator<PointsProcessor> it = sources.iterator();
		while (it.hasNext())
		{	PointsProcessor source = it.next();
			values.add(source.process(holder));
		}
		
		// result
		List<String> playersIds = stats.getPlayersIds();
		float[] result = new float[playersIds.size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;
		
		// process		
		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	int cpr = comparePoints(j,i,values,inverted);
				if(cpr>0)
					result[i] = result[i] + 1;
				else if(cpr<0)
					result[j] = result[j] + 1;
			}
		}	
		
		return result;
	}
	
	/**
	 * Compares two players according to their points.
	 * The various sources are considered according to their order in the list. 
	 */
	public int comparePoints(int i, int j, List<float[]> values, boolean inverted)
	{	// inverted ?
		if(inverted)
		{	int temp = i;
			i = j;
			j = temp;
		}
		// process
		int result = 0;
		Iterator<float[]> it = values.iterator();
		while (it.hasNext() && result==0)
		{	float[] temp = it.next();
			if(temp[i]<temp[j])
				result = -1;
			else if(temp[i]>temp[j])
				result = +1;
		}
		return result;
	}		

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// function
		result.append("Ranks");
		result.append("(");
		// inverted
		if(inverted)
			result.append("<inverted> ; ");
		// argument
		result.append("<");
		Iterator<PointsProcessor> i = sources.iterator();
		while (i.hasNext())
		{	PointsProcessor temp = i.next();
			result.append(temp.toString());
			result.append(";");
		}
		result.deleteCharAt(result.length()-1);
		result.append(">");
		// result
		result.append(")");
		return result.toString();
	}
}
