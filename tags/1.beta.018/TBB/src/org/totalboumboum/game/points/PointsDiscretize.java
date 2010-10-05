package org.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This PointsProcessor discretizes the points definition set and associates 
 * a given number points to every resulting interval. Points are calculated
 * form the PointsProcessor source according to the corresponding category.
 *  
 * for example, if the source was {3,12,0} and the parameters :
 * 		thresholds:     | 2 | 5 | 12 | 33 |
 * 		values:       | 1 | 2 | 4  |  6 | 10 |
 * the the result would be {2,4,0}
 * 
 * @author Vincent Labatut
 *
 */
public class PointsDiscretize extends PointsProcessor implements PPFunction
{	private static final long serialVersionUID = 1L;

	public PointsDiscretize(PointsProcessor source, float[] thresholds, float[] values)
	{	this.source = source;
		this.thresholds = thresholds;
		this.values = values;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor source;
	
	public PointsProcessor getSource()
	{	return source;	
	}

	/////////////////////////////////////////////////////////////////
	// PARAMETERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float[] thresholds;
	private float[] values;
	
	public float[] getValues()
	{	return values;	
	}
	public float[] getThresholds()
	{	return thresholds;	
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
		
		// process
		for(int i=0;i<temp.length;i++)
		{	int j=0;
			boolean found = false;
			do
			{	float threshold = thresholds[j];
				if(temp[i]<=threshold)
				{	result[i] = values[j];
					found = true;
				}
				else
					j++;
			}
			while(!found && j<thresholds.length);
			if(!found)
				result[i] = values[j];
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
		result.append("Discretize");
		result.append("(");
		// values
		result.append("<"); 
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		String thresholds2[] = new String[thresholds.length+2];
		thresholds2[0] = new Character('\u2212').toString()+new Character('\u221E').toString();
		for(int i=0;i<thresholds.length;i++)
			thresholds2[i+1] = nf.format(thresholds[i]);
		thresholds2[thresholds2.length-1] = new Character('\u002B').toString()+new Character('\u221E').toString();
		for(int i=0;i<values.length;i++)
		{	result.append("]");
			result.append(thresholds2[i]);
			result.append(";");
			result.append(thresholds2[i+1]);
			result.append("]->");
			result.append(nf.format(values[i]));
			result.append("; "); 
		}
		result.deleteCharAt(result.length()-1);
		result.append("> ; "); 
		// argument
		result.append("<"); 
		result.append(source.toString());
		result.append(">"); 
		// result
		result.append(")");
		return result.toString();
	}
}
