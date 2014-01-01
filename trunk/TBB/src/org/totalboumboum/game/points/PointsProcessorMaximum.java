package org.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This {@code PointsProcessor} calculate the maximal value
 * in the results coming from source {@code PointsProcessor}.
 * <br/>
 * For example, if the source was {12,2,5} then the result would be {12,12,12}.
 * 
 * @author Vincent Labatut
 */
public class PointsProcessorMaximum extends AbstractPointsProcessor implements InterfacePointsProcessorFunction
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new {@code PointsProcessor}.
	 * 
	 * @param source
	 * 		Operand (another {@code PointsProcessor}).
	 */
	public PointsProcessorMaximum(AbstractPointsProcessor source)
	{	this.source = source;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Unique operand */
	private AbstractPointsProcessor source;

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
		float max = Float.MIN_VALUE;
		for(int i=0;i<temp.length;i++)
		{	if(temp[i]>max)
				max = temp[i];
		}
		for(int i=0;i<result.length;i++)
			result[i] = max;
		//
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
		result.append("Max");
		// argument
		result.append("(");
		result.append(source.toString());
		result.append(")");
		// result
		return result.toString();
	}
}
