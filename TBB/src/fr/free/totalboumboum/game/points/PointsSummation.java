package fr.free.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import fr.free.totalboumboum.statistics.detailed.StatisticBase;
import fr.free.totalboumboum.statistics.detailed.StatisticHolder;

/**
 * This PointsProcessor calculates its result by summing all the 
 * the results coming from the source PointProcessor.
 * 
 * For example, for 5 players and a {1,3,4,0,3} points vector coming
 * from the source, the result would be {11,11,11,11,11}. 
 * 
 * @author Vincent
 *
 */

public class PointsSummation extends PointsProcessor implements PPFunction
{	private static final long serialVersionUID = 1L;

	public PointsSummation(PointsProcessor source)
	{	this.source = source;
	}
	
	/////////////////////////////////////////////////////////////////
	// SOURCES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor source;

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	// init
		StatisticBase stats = holder.getStats();
		ArrayList<Integer> playersIds = stats.getPlayersIds();
		float[] result = new float[playersIds.size()];
		float[] temp = source.process(holder);
		// process
		float sum = 0;
		for(int i=0;i<temp.length;i++)
			sum = sum + temp[i];
		for(int i=0;i<result.length;i++)
			result[i] = sum;
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
		result.append("Sum");
		// argument
		result.append("(");
		result.append(source.toString());
		result.append(")");
		// result
		return result.toString();
	}
}
