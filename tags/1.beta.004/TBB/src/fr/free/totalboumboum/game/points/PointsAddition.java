package fr.free.totalboumboum.game.points;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsAddition extends PointsProcessor implements PPSecondaryOperator
{
	private PointsProcessor leftSource;
	private PointsProcessor rightSource;
	
	public PointsAddition(PointsProcessor leftSource, PointsProcessor rightSource)
	{	this.leftSource = leftSource;
		this.rightSource = rightSource;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] leftTemp = leftSource.process(stats);
		float[] rightTemp = rightSource.process(stats);
		// process
		for(int i=0;i<result.length;i++)
			result[i] = leftTemp[i] + rightTemp[i];
		//
		return result;
	}
	
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