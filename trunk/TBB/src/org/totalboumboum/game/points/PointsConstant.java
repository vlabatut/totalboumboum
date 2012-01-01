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

import java.text.NumberFormat;

import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticHolder;


/**
 * This PointsProcessor always send the same real value as a result
 * 
 * For example, if there was 5 players and the parameter was 7 
 * then the result would be {7,7,7} 
 * 
 * @author Vincent Labatut
 *
 */
public class PointsConstant extends PointsProcessor implements PPConstant
{	private static final long serialVersionUID = 1L;

	public PointsConstant(float value)
	{	this.value = value;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PARAMETER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float value;
	
	public float getValue()
	{	return value;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public float[] process(StatisticHolder holder)
	{	StatisticBase stats = holder.getStats();
		int nbr = stats.getPlayersIds().size();
		float result[] = new float[nbr];
		for(int i=0;i<nbr;i++)
			result[i] = value;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// value
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		String val = nf.format(value);
		result.append(val);
		// result
		return result.toString();
	}
}
