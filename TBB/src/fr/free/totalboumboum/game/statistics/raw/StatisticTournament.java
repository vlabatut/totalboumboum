package fr.free.totalboumboum.game.statistics.raw;

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
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class StatisticTournament extends StatisticBase
{
	private static final long serialVersionUID = 1L;
	
	public StatisticTournament(AbstractTournament tournament)
	{	super(tournament);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC MATCHES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	
	public ArrayList<StatisticMatch> getStatisticMatches()
	{	return matches;
	}

	public void addStatisticMatch(StatisticMatch match)
	{	// matches stats
		matches.add(match);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = getScores(score);
			long[] matchScores = match.getScores(score);
			for(int i=0;i<matchScores.length;i++)
			{	String playerName = match.getPlayers().get(i);
				int index = getPlayers().indexOf(playerName);
				currentScores[index] = currentScores[index] + matchScores[i];			
			}
		}
		// total
		float[] matchPoints = match.getPoints();
		for(int i=0;i<matchPoints.length;i++)
		{	String playerName = match.getPlayers().get(i);
			int index = getPlayers().indexOf(playerName);
			getTotal()[index] = getTotal()[index] + matchPoints[i];
		}
		// time
		long time = getTotalTime() + match.getTotalTime();
		setTotalTime(time);
	}

	@Override
	public int getConfrontationCount()
	{	int result = matches.size();
		return result;
	}

	@Override
	public ArrayList<StatisticBase> getConfrontationStats()
	{	ArrayList<StatisticBase> result = new ArrayList<StatisticBase>();
		for(StatisticMatch r: matches)
			result.add(r);
		return result;
	}
}
