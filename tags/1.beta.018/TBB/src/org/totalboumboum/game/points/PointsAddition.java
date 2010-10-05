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

import java.util.List;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This PointsProcessor adds the results coming from two other PointProcessor objects.
 * 
 * For example, if the sources were {12,2,5} and {3,2,1}
 * then the result would be {15,4,6} 
 * 
 * @author Vincent Labatut
 *
 */
public class PointsAddition extends PointsProcessor implements PPSecondaryOperator
{	private static final long serialVersionUID = 1L;

	public PointsAddition(PointsProcessor leftSource, PointsProcessor rightSource)
	{	this.leftSource = leftSource;
		this.rightSource = rightSource;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor leftSource;
	private PointsProcessor rightSource;
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	// init
		StatisticBase stats = holder.getStats();
		List<String> playersIds = stats.getPlayersIds();
		float[] result = new float[playersIds.size()];
		float[] leftTemp = leftSource.process(holder);
		float[] rightTemp = rightSource.process(holder);
		// process
		for(int i=0;i<result.length;i++)
			result[i] = leftTemp[i] + rightTemp[i];
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
		// left operand
		result.append(leftSource.toString());
		// operator
		result.append(new Character('\u002B').toString());
		// right operand
		result.append(rightSource.toString());
		// result
		return result.toString();
	}
}
