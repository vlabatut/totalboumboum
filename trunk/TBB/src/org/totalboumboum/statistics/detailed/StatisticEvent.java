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

/**
 * Event happening during a round,
 * and conveying a statistical meaning.
 * 
 * @author Vincent Labatut
 */
public class StatisticEvent implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new stat event for the specified context.
	 * 
	 * @param actorId
	 * 		Id of the player performing the action.
	 * @param action
	 * 		Nature of the action.
	 * @param targetId
	 * 		Id of the player undergoing the action ({@code null} if the action is intransitive).
	 * @param time
	 * 		Time the action was performed.
	 */
	public StatisticEvent(String actorId, StatisticAction action, String targetId, long time)
	{	this(actorId,action,targetId,time,false);
		
	}
	
	/**
	 * Builds a new stat event for the specified context.
	 * 
	 * @param actorId
	 * 		Id of the player performing the action.
	 * @param action
	 * 		Nature of the action.
	 * @param targetId
	 * 		Id of the player undergoing the action.
	 * @param time
	 * 		Time the action was performed.
	 * @param simulated
	 * 		Whether the event was simulated or real.
	 */
	public StatisticEvent(String actorId, StatisticAction action, String targetId, long time, boolean simulated)
	{	this.actorId = actorId;
		this.action = action;
		this.targetId = targetId;
		this.time = time;
		this.simulated = simulated;
	}

	/////////////////////////////////////////////////////////////////
	// ACTOR				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Id of the player performing the action */
	private String actorId;

	/**
	 * Returns the id of the player performing the action.
	 * 
	 * @return
	 * 		Id of the player performing the action.
	 */
	public String getActorId()
	{	return actorId;
	}
	
	/**
	 * Changes the id of the player performing the action.
	 * 
	 * @param actorId
	 * 		New id of the player performing the action.
	 */
	public void setActorId(String actorId)
	{	this.actorId = actorId;
	}

	/////////////////////////////////////////////////////////////////
	// ACTION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Nature of the action */
	private StatisticAction action;
	
	/**
	 * Returns the nature of the action.
	 * 
	 * @return
	 * 		Nature of the action.
	 */
	public StatisticAction getAction()
	{	return action;
	}
	
	/**
	 * Changes the nature of the action.
	 * 
	 * @param action
	 * 		Nature of the action.
	 */
	public void setAction(StatisticAction action)
	{	this.action = action;
	}

	/////////////////////////////////////////////////////////////////
	// TARGET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Time the action was performed */
	private String targetId;
	
	/**
	 * Returns the time the action was performed.
	 * 
	 * @return
	 * 		Time the action was performed.
	 */
	public String getTargetId()
	{	return targetId;
	}
	
	/**
	 * Changes the time the action was performed.
	 * 
	 * @param targetId
	 * 		New time the action was performedn.
	 */
	public void setTargetId(String targetId)
	{	this.targetId = targetId;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	private long time;

	/**
	 * Returns the id of the player performing the action.
	 * 
	 * @return
	 * 		Id of the player performing the action.
	 */
	public long getTime()
	{	return time;
	}
	
	/**
	 * Changes the id of the player performing the action.
	 * 
	 * @param time
	 * 		New id of the player performing the action.
	 */
	public void setTime(long time)
	{	this.time = time;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATED			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the event was simulated or real */
	private boolean simulated;

	/**
	 * Returns the simulation state of this event.
	 * 
	 * @return
	 * 		{@code true} iff this event is simulated.
	 */
	public boolean getSimulated()
	{	return simulated;
	}

	/**
	 * Changes the simulation state of this event.
	 * 
	 * @param simulated
	 * 		New simulation state of this event.
	 */
	public void setSimulated(boolean simulated)
	{	this.simulated = simulated;
	}
}
