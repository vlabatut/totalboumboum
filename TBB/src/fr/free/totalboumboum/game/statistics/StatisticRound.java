package fr.free.totalboumboum.game.statistics;

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

import java.io.Serializable;
import java.util.ArrayList;

import fr.free.totalboumboum.game.round.Round;

public class StatisticRound extends StatisticBase implements Serializable
{
	private static final long serialVersionUID = 1L;

	public StatisticRound(Round round)
	{	super(round);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticEvent> events = new ArrayList<StatisticEvent>();
	
	public ArrayList<StatisticEvent> getStatisticEvents()
	{	return events;
	}

	public void addStatisticEvent(StatisticEvent event)
	{	// events
		events.add(event);
		// scores
		for(Score s: Score.values())
			s.process(this, event);
	}

	@Override
	public int getConfrontationCount()
	{	// useless for round
		int result = 0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void updateTime(long time, Round round)
	{	setTotalTime(time);
		long[] sc = getScores(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(round.getPlayersStatus().get(i))
				sc[i] = time;
		}
	}

	public void finalizeTime(Round round)
	{	updateTime(getTotalTime()+1, round);
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public int[] getRanks()
	{	int[] result = new int[getPlayers().size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;

		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(getPoints()[i]<getPoints()[j])
					result[i] = result[i] + 1;
				else if(getPoints()[i]>getPoints()[j])
					result[j] = result[j] + 1;
			}
		}	

		return result;
	}
	
	public int[] getOrderedPlayers()
	{	int[] result = new int[getPlayers().size()];
		int[] ranks = getRanks();
		int done = 0;
		for(int i=1;i<=result.length;i++)
		{	for(int j=0;j<ranks.length;j++)
			{	if(ranks[j]==i)
				{	result[done] = j;
					done++;
				}
			}
		}
		return result;
	}
	
	public ArrayList<Integer> getWinners()
	{	ArrayList<Integer> result = new ArrayList<Integer>();
		int[] ranks = getRanks();
		for(int i=0;i<ranks.length;i++)
			if(ranks[i]==1)
				result.add(i);
		return result;
	}
}
