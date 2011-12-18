package fr.free.totalboumboum.statistics.detailed;

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
import fr.free.totalboumboum.game.match.Match;

public class StatisticMatch extends StatisticBase
{
	private static final long serialVersionUID = 1L;

	public StatisticMatch(Match match)
	{	super(match);
	}

	/////////////////////////////////////////////////////////////////
	// STATISTIC ROUNDS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticRound> rounds = new ArrayList<StatisticRound>();

	public ArrayList<StatisticRound> getStatisticRounds()
	{	return rounds;
	}

	public void addStatisticRound(StatisticRound round)
	{	// round stats
		rounds.add(round);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = getScores(score);
			long[] roundScores = round.getScores(score);
			for(int i=0;i<roundScores.length;i++)
				currentScores[i] = currentScores[i] + roundScores[i];
		}
		// partial points
		float[] roundPoints = round.getPoints();
		for(int i=0;i<getTotal().length;i++)
			getTotal()[i] = getTotal()[i] + roundPoints[i];
		// time
		long time = getTotalTime() + round.getTotalTime();
		setTotalTime(time);
	}

	@Override
	public int getConfrontationCount()
	{	int result = rounds.size();
		return result;
	}
	
	@Override
	public ArrayList<StatisticBase> getConfrontationStats()
	{	ArrayList<StatisticBase> result = new ArrayList<StatisticBase>();
		for(StatisticRound r: rounds)
			result.add(r);
		return result;
	}
}