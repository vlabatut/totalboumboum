package org.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.List;

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.tools.computing.RankingTools;

/**
 * Represents the stats of a tournament.
 * 
 * @author Vincent Labatut
 */
public class StatisticTournament extends StatisticBase
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new stat object for
	 * the specified game object.
	 * 
	 * @param tournament
	 * 		Game object.
	 */
	public StatisticTournament(AbstractTournament tournament)
	{	super(tournament);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC MATCHES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of match stats */
	private final List<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	
	/**
	 * Returns the number of played confrontations for each player of the tournament.
	 * 
	 * @return
	 * 		Confrontation counts.
	 */
	public int[] getPlayedCounts()
	{	int size = getPlayersIds().size();
		int[] result = new int[size];
		
		for(StatisticMatch match: matches)
		{	int[] matchRanks = RankingTools.getRanks(match.getPoints());
			for(int i=0;i<matchRanks.length;i++)
			{	String playerId = match.getPlayersIds().get(i);
				int index = getPlayersIds().indexOf(playerId);
				result[index]++;
			}
		}
		
		return result;
	}

	/**
	 * Returns the number of confrontations for each player of the tournament.
	 * Each row corresponds to a different player, the columns are (in this order)
	 * the numbers of: won, drawn and lost matches.
	 * 
	 * @return
	 * 		Confrontation counts.
	 */
	public int[][] getConfrontationCounts()
	{	int size = getPlayersIds().size();
		int[][] result = new int[size][3];
		
		for(StatisticMatch match: matches)
		{	int[] matchRanks = RankingTools.getRanks(match.getPoints());
			int count = 0;
			for(int i=0;i<matchRanks.length;i++)
				if(matchRanks[i]==1)
					count++;
			boolean draw = count>1;
			for(int i=0;i<matchRanks.length;i++)
			{	String playerId = match.getPlayersIds().get(i);
				int index = getPlayersIds().indexOf(playerId);
				// lost
				if(matchRanks[i]>1)
					result[index][2]++;
				// drawn
				else if(draw)
					result[index][1]++;
				// won
				else
					result[index][0]++;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the number of ranks for each player of the tournament.
	 * Each row corresponds to a different player, the columns are (in this order)
	 * the numbers of: 1st, 2nd, 3rd, 4th and 5th (or more).
	 * 
	 * @return
	 * 		Confrontation counts.
	 */
	public int[][] getRankCounts()
	{	int size = getPlayersIds().size();
		int[][] result = new int[size][5];
		
		for(StatisticMatch match: matches)
		{	int[] matchRanks = RankingTools.getRanks(match.getPoints());
			for(int i=0;i<matchRanks.length;i++)
			{	String playerId = match.getPlayersIds().get(i);
				int index = getPlayersIds().indexOf(playerId);
				int rank = Math.min(matchRanks[i],5);
				result[index][rank-1]++;
			}
		}
		
		return result;
	}
	
	/**
	 * Returns the list of match stats.
	 * 
	 * @return
	 * 		Match stats.
	 */
	public List<StatisticMatch> getStatisticMatches()
	{	return matches;
	}

	/**
	 * Adds a new match stat object to the current list.
	 * 
	 * @param match
	 * 		New match stat object.
	 */
	public void addStatisticMatch(StatisticMatch match)
	{	// matches stats
		matches.add(match);
		
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

	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public List<StatisticEvent> getStatisticEvents()
	{	List<StatisticEvent> result = new ArrayList<StatisticEvent>();
		//for(StatisticMatch match: matches) TODO
			
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
