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

import org.totalboumboum.game.round.Round;

/**
 * Represents the stats of a round.
 * 
 * @author Vincent Labatut
 */
public class StatisticRound extends StatisticBase
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new stat object for
	 * the specified game object.
	 * 
	 * @param round
	 * 		Game object.
	 */
	public StatisticRound(Round round)
	{	super(round);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of event stats */
	private final List<StatisticEvent> events = new ArrayList<StatisticEvent>();
	
	@Override
	public List<StatisticEvent> getStatisticEvents()
	{	return events;
	}

	/**
	 * Adds a new event stat object to the current list.
	 * 
	 * @param event
	 * 		New event stat object.
	 */
	public void addStatisticEvent(StatisticEvent event)
	{	// events
		events.add(event);
		// scores
		for(Score s: Score.values())
			s.process(this, event);
	}

	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public int getConfrontationCount()
	{	// useless for round
		int result = 0;
		return result;
	}

	@Override
	public List<StatisticBase> getConfrontationStats()
	{	List<StatisticBase> result = new ArrayList<StatisticBase>();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME		/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates the current time of this round.
	 * 
	 * @param time
	 * 		Current time.
	 * @param round
	 * 		Associated game object.
	 */
	public void updateTime(long time, Round round)
	{	setTotalTime(time);
		long[] sc = getScores(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(round.getPlayersStatus().get(i))
				sc[i] = time;
		}
	}

	/**
	 * Sets the end time of this round.
	 * 
	 * @param round
	 * 		Associated game object.
	 */
	public void finalizeTime(Round round)
	{	updateTime(getTotalTime()+1,round);
	}
}
