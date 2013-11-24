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
 * Set of methods common to all statistical objects.
 * 
 * @author Vincent Labatut
 */
public abstract class StatisticBase implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new statistical object.
	 * 
	 * @param holder
	 * 		Game object described by this stat object.
	 */
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
	/** The current scores */
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	/**
	 * Gets the current values for
	 * the specified score.
	 * 
	 * @param score 
	 * 		Score of interest.
	 * @return 
	 * 		Values associated to this score, for each player.
	 */
	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Points scored by the players */
	private float[] points;
	/** Total points scored by the players in the various sub-parts of the considered game object */
	private float[] total;
	
	/**
	 * Points processed after the end of the confrontation
	 * (for tournaments, matches and rounds).
	 * 
	 * @return
	 * 		Points scored by the players.
	 */
	public float[] getPoints()
	{	return points;
	}

	/**
	 * Changes the points associated to
	 * this stat object.
	 * 
	 * @param points
	 * 		New point values.
	 */
	public void setPoints(float[] points)
	{	for(int i=0;i<points.length;i++)
			this.points[i] = points[i];
	}
	
	/**
	 * Sum of the points for all sub-confrontations
	 * (i.e. matches for a tournament and rounds for a match. 
	 * Should not be used for rounds).
	 * 
	 * @return
	 * 		Total of the points scored in the sub-confrontations.
	 */
	public float[] getTotal()
	{	return total;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Returns the stat events associated to this stat object.
	 * 
	 * @return 
	 * 		List of stat events.
	 */
	public abstract List<StatisticEvent> getStatisticEvents();
	
	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Returns the number of confrontations 
	 * (played up to now).
	 * 
	 * @return 
	 * 		Number of past confrontations.
	 */
	public abstract int getConfrontationCount();
	/** 
	 * Returns the stats of each confrontation.
	 *  
	 * @return 
	 * 		List of stat objects.
	 */
	public abstract List<StatisticBase> getConfrontationStats();

	/////////////////////////////////////////////////////////////////
	// TIME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Total time played */
	private long totalTime;

	/**
	 * Returns the total time played.
	 * 
	 * @return
	 * 		Total time.
	 */
	public long getTotalTime()
	{	return totalTime;	
	}
	
	/**
	 * Changes the total time played.
	 * 
	 * @param totalTime
	 * 		New total time.
	 */
	public void setTotalTime(long totalTime)
	{	this.totalTime = totalTime;
	}

	/////////////////////////////////////////////////////////////////
	// DATE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Start date of the associated game object */
	private Date startDate;
	/** End date of the associated game object */
	private Date endDate;

	/**
	 * Returns the start date of the associated game object.
	 * 
	 * @return
	 * 		Start date of the associated game object.
	 */
	public Date getStartDate()
	{	return startDate;
	}
	
	/**
	 * Initializes the start date of the associated game object.
	 */
	public void initStartDate()
	{	Calendar cal = new GregorianCalendar();
		this.startDate = cal.getTime();
	}
	
	/**
	 * Returns the end date of the associated game object.
	 * 
	 * @return
	 * 		End date of the associated game object.
	 */
	public Date getEndDate()
	{	return endDate;
	}
	
	/**
	 * Initializes the end date of the associated game object.
	 */
	public void initEndDate()
	{	Calendar cal = new GregorianCalendar();
		this.endDate = cal.getTime();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Ids of the involved players */
	private final List<String> playersIds = new ArrayList<String>();

	/**
	 * Gets the list of players involved in
	 * the associated game object.
	 * 
	 * @return
	 * 		List of player ids.
	 */
	public List<String> getPlayersIds()
	{	return playersIds;
	}

	/**
	 * Adds a player to the list
	 * of ids.
	 * 
	 * @param playerId
	 * 		New player to add.
	 */
	public void addPlayerId(String playerId)
	{	playersIds.add(playerId);
	}
}
