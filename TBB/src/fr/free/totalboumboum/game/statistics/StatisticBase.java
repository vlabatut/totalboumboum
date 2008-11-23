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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import fr.free.totalboumboum.configuration.profile.Profile;


public abstract class StatisticBase
{
	public StatisticBase(StatisticHolder holder)
	{	totalTime = 0;
		// players
		for(Profile p: holder.getProfiles())
		{	String player = p.getFileName();
			players.add(player);
		}
		// points
		points = new float[players.size()];
		Arrays.fill(points,0);
		// total
		total = new float[players.size()];
		Arrays.fill(total,0);
		// scores
		for(Score score : Score.values())
		{	long[] sc = new long[players.size()];
			Arrays.fill(sc,0);
			scores.put(score,sc);
		}		
	}
	
	/////////////////////////////////////////////////////////////////
	// SCORES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** the current scores */
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	/**
	 * get the current scores
	 */
	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float[] points;
	private float[] total;
	
	/**
	 * points processed after the end of the confrontation
	 * (for tournaments, matches and rounds)
	 * @return
	 */
	public float[] getPoints()
	{	return points;
	}

	/**
	 * sum of the points for all sub-confrontations
	 * (ie matches for a tournament and rounds for a match. 
	 * should not be used for rounds).
	 * @return
	 */
	public float[] getTotal()
	{	return total;
	}

	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract int getConfrontationCount();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long totalTime;

	public long getTotalTime()
	{	return totalTime;	
	}
	
	public void setTotalTime(long totalTime)
	{	this.totalTime = totalTime;
	}

	/////////////////////////////////////////////////////////////////
	// DATE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Date startDate;
	private Date endDate;

	public Date getStartDate()
	{	return startDate;
	}
	
	public void setStartDate(Date startDate)
	{	this.startDate = startDate;
	}
	
	public Date getEndDate()
	{	return endDate;
	}
	
	public void setEndDate(Date endDate)
	{	this.endDate = endDate;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> players = new ArrayList<String>();

	public ArrayList<String> getPlayers()
	{	return players;
	}

	public void addPlayer(String player)
	{	players.add(player);
	}
}
