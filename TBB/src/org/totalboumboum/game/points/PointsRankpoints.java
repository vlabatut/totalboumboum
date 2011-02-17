package org.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This PointsProcessor process the rankings in function of  the results coming 
 * from the source PointProcessor, and then gives points according to this
 * ranking.
 * 
 * note: the same result can be obtained by combing a Rankings and a Discretize
 * PointsProcessor objects, but PointsRankpoints allows to specify a special
 * behaviour for draws (share points or not).
 * 
 * For example, if the source was {12,2,5} and we have the values :
 * 		- 10 pts for the first place
 * 		- 5 pts for the second place
 * then the result would be {10,0,5} 
 * 
 * @author Vincent Labatut
 *
 */
public class PointsRankpoints extends PointsProcessor implements PPFunction
{	private static final long serialVersionUID = 1L;

	public PointsRankpoints(List<PointsProcessor> sources, float[] values, boolean inverted, boolean exaequoShare)
	{	this.source = new PointsRankings(sources,inverted);
		this.values = values;
		this.exaequoShare = exaequoShare;
	}
	
	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float[] values;
	private boolean exaequoShare;

	public float[] getValues()
	{	return values;	
	}
	public boolean getExaequoShare()
	{	return exaequoShare;	
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsRankings source;
	
	public PointsRankings getSource()
	{	return source;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	// init
		StatisticBase stats = holder.getStats();
		List<String> playersIds = stats.getPlayersIds();
		float[] result = new float[playersIds.size()];
		float[] temp = source.process(holder);
		float[] values2 = new float[values.length];
		
		// count
		if(exaequoShare)
		{	// init
			int[] count = new int[values.length];
			for(int i=0;i<count.length;i++)
				count[i] = 0;
			// process the rankings
			for(int i=0;i<temp.length;i++)
			{	int index = (int)temp[i]-1;
				if(index<count.length)
					count[index]++;
//				else
//					count[count.length-1]++;			
			}
			// process the points
			for(int i=0;i<count.length;i++)
			{	float pts = 0;
				if(count[i]>0)
				{	for(int j=0;j<count[i] && (i+j)<count.length;j++)
						pts = pts + values[i+j];
					pts = pts / count[i];
				}
				values2[i] = pts;
			}
		}
		else
			values2 = values;
		
		// process
		for(int i=0;i<temp.length;i++)
		{	int index = (int)temp[i]-1;
			if(index<values2.length)
				result[i] = values2[index];
			else
				result[i] = 0;
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
		result.append("Rankpoints");
		result.append("(");
		// parameters
		result.append("<");
		if(source.isInverted())
			result.append("inverted;");
		if(exaequoShare)
			result.append("share");
		result.append("> ; ");
		// values
		result.append("<");
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		for(int i=0;i<values.length;i++)
		{	result.append("#"+(i+1)+"->");
			result.append(values[i]);
			result.append(";");
		}
		result.deleteCharAt(result.length()-1);
		result.append("> ; ");
		// arguments
		result.append("<");
		Iterator<PointsProcessor> i = source.getSources().iterator();
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
