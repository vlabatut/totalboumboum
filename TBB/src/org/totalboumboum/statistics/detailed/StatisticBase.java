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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.game.profile.Profile;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class StatisticBase implements Serializable
{	private static final long serialVersionUID = 1L;
	
	public StatisticBase(StatisticHolder holder)
	{	totalTime = 0;
		// players
		for(Profile p: holder.getProfiles())
		{	String playerId = p.getId();
			playersIds.add(playerId);
		}
		// points
		points = new float[playersIds.size()];
		Arrays.fill(points,0);
		// total
		total = new float[playersIds.size()];
		Arrays.fill(total,0);
		// scores
		for(Score score : Score.values())
		{	long[] sc = new long[playersIds.size()];
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

	public void setPoints(float[] points)
	{	for(int i=0;i<points.length;i++)
			this.points[i] = points[i];
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
	public abstract List<StatisticBase> getConfrontationStats();

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
	
	public void initStartDate()
	{	Calendar cal = new GregorianCalendar();
		this.startDate = cal.getTime();
	}
	
	public Date getEndDate()
	{	return endDate;
	}
	
	public void initEndDate()
	{	Calendar cal = new GregorianCalendar();
		this.endDate = cal.getTime();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> playersIds = new ArrayList<String>();

	public List<String> getPlayersIds()
	{	return playersIds;
	}

	public void addPlayerId(String playerId)
	{	playersIds.add(playerId);
	}
}
