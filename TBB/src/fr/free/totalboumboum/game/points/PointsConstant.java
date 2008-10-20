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

import java.text.NumberFormat;

import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticBase;

public class PointsConstant extends PointsProcessor implements PPConstant
{	
	private float value;
	
	public PointsConstant(float value)
	{	this.value = value;	
	}
	
	public float getValue()
	{	return value;	
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	int nbr = stats.getPlayers().size();
		float result[] = new float[nbr];
		for(int i=0;i<nbr;i++)
			result[i] = value;
		return result;
	}

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
