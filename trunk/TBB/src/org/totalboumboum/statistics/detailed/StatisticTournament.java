package org.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
	/** List of match stats */
	private final List<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	/** Number of rounds played */
	private int played[];
	/** Number of rounds won */
	private int won[];
	/** Number of rounds drawn*/
	private int drawn[];
	/** Number of rounds lost*/
	private int lost[];
		
	/**
	 * Returns the numbers of rounds played.
	 * 
	 * @return
	 * 		Numbers of rounds played.
	 */
	public int[] getPlayed()
	{	return played;
	}
	
	/**
	 * Returns the numbers of rounds won.
	 * 
	 * @return
	 * 		Numbers of rounds won.
	 */
	public int[] getWon()
	{	return won;
	}

	/**
	 * Returns the numbers of rounds drawn.
	 * 
	 * @return
	 * 		Numbers of rounds drawn.
	 */
	public int[] getDrawn()
	{	return drawn;
	}

	/**
	 * Returns the numbers of rounds lost.
	 * 
	 * @return
	 * 		Numbers of rounds lost.
	 */
	public int[] getLost()
	{	return lost;
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
		
		// confrontations
		int[] matchRanks = RankingTools.getRanks(match.getPoints());
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
