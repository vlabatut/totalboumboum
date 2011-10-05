package fr.free.totalboumboum.game.limit;

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

import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.statistics.StatisticHolder;

/**
 * this limit is based on the number of players remaining in a round.
 * For example, a round can be stopped when there is only one player remaining.
 * 
 * @author Vincent
 *
 */
public class LimitLastStanding implements RoundLimit
{	private static final long serialVersionUID = 1L;

	public LimitLastStanding(int threshold, Comparisons comparatorCode, PointsProcessor pointProcessor)
	{	this.threshold = threshold;
		this.comparatorCode = comparatorCode;
		this.pointProcessor = pointProcessor;
	}
	
	/////////////////////////////////////////////////////////////////
	// THRESHOLD		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int threshold;
	private Comparisons comparatorCode;
	
	public int getThreshold()
	{	return threshold;
	}

	public void setThreshold(int threshold)
	{	this.threshold = threshold;
	}

	public Comparisons getSupLimit()
	{	return comparatorCode;
	}
	
	public void setsupLimit(Comparisons comparatorCode)
	{	this.comparatorCode = comparatorCode;
	}

	@Override
	public boolean testThreshold(StatisticHolder holder)
	{	boolean result = false;
		ArrayList<Boolean> playersStatus = holder.getPlayersStatus();
		int count = 0;
		for(boolean b: playersStatus)
		{	if(b)
				count++;
		}
		switch(comparatorCode)
		{	case EQUAL:
				result = count==threshold;
				break;
			case GREATER:
				result = count>threshold;
				break;
			case GREATEREQ:
				result = count>=threshold;
				break;
			case LESS:
				result = count<threshold;
				break;
			case LESSEQ:
				result = count<=threshold;
				break;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor pointProcessor;
	
	public PointsProcessor getPointProcessor()
	{	return pointProcessor;
	}

	public void setPointProcessor(PointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}

	@Override
	public float[] processPoints(StatisticHolder holder)
	{	return pointProcessor.process(holder);		
	}
}