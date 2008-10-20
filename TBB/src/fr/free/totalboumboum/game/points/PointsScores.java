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

import fr.free.totalboumboum.game.statistics.Score;
import fr.free.totalboumboum.game.statistics.StatisticBase;

public class PointsScores extends PointsProcessor implements PPConstant
{	
	private Score score;
	
	public PointsScores(Score score)
	{	this.score = score;	
	}
	
	public Score getScore()
	{	return score;	
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	long[] temp = stats.getScores(score);
		float result[] = new float[stats.getPlayers().size()];
		for(int i=0;i<result.length;i++)
			result[i] = temp[i];
		return result;
	}

	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// value
		result.append(score.stringFormat());
		// result
		return result.toString();
	}
}
