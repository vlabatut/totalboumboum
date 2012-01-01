package org.totalboumboum.statistics.detailed;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.tools.calculus.CombinatoricsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StatisticTournament extends StatisticBase
{
	private static final long serialVersionUID = 1L;
	
	public StatisticTournament(AbstractTournament tournament)
	{	super(tournament);
		// confrontations
		played = new int[getPlayersIds().size()];
		Arrays.fill(played,0);
		won = new int[getPlayersIds().size()];
		Arrays.fill(won,0);
		drawn = new int[getPlayersIds().size()];
		Arrays.fill(drawn,0);
		lost = new int[getPlayersIds().size()];
		Arrays.fill(lost,0);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC MATCHES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	private int played[];
	private int won[];
	private int drawn[];
	private int lost[];
		
	public int[] getPlayed()
	{	return played;
	}
	public int[] getWon()
	{	return won;
	}
	public int[] getDrawn()
	{	return drawn;
	}
	public int[] getLost()
	{	return lost;
	}

	public List<StatisticMatch> getStatisticMatches()
	{	return matches;
	}

	public void addStatisticMatch(StatisticMatch match)
	{	// matches stats
		matches.add(match);
		
		// confrontations
		int[] matchRanks = CombinatoricsTools.getRanks(match.getPoints());
		int count = 0;
		for(int i=0;i<matchRanks.length;i++)
			if(matchRanks[i]==1)
				count++;
		boolean draw = count==1;
		for(int i=0;i<matchRanks.length;i++)
		{	String playerId = match.getPlayersIds().get(i);
			int index = getPlayersIds().indexOf(playerId);
			played[index]++;
			if(matchRanks[i]>1)
				lost[index]++;
			else if(draw)
				drawn[index]++;
			else
				won[index]++;
		}
		
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = getScores(score);
			long[] matchScores = match.getScores(score);
			for(int i=0;i<matchScores.length;i++)
			{	String playerId = match.getPlayersIds().get(i);
				int index = getPlayersIds().indexOf(playerId);
				currentScores[index] = currentScores[index] + matchScores[i];			
			}
		}
		
		// total
		float[] matchPoints = match.getPoints();
		for(int i=0;i<matchPoints.length;i++)
		{	String playerId = match.getPlayersIds().get(i);
			int index = getPlayersIds().indexOf(playerId);
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
	public List<StatisticBase> getConfrontationStats()
	{	List<StatisticBase> result = new ArrayList<StatisticBase>();
		for(StatisticMatch r: matches)
			result.add(r);
		return result;
	}
}
