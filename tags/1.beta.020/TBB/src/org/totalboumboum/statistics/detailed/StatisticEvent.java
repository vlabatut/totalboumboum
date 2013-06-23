package org.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
 * 
 * @author Vincent Labatut
 *
 */
public class StatisticEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	public StatisticEvent(String actorId, StatisticAction action, String targetId, long time)
	{	this(actorId,action,targetId,time,false);
		
	}
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
	private String actorId;

	public String getActorId()
	{	return actorId;
	}
	
	public void setActorId(String actorId)
	{	this.actorId = actorId;
	}

	/////////////////////////////////////////////////////////////////
	// ACTION				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticAction action;
	
	public StatisticAction getAction()
	{	return action;
	}
	
	public void setAction(StatisticAction action)
	{	this.action = action;
	}

	/////////////////////////////////////////////////////////////////
	// TARGET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String targetId;
	
	public String getTargetId()
	{	return targetId;
	}
	
	public void setTargetId(String targetId)
	{	this.targetId = targetId;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long time;

	public long getTime()
	{	return time;
	}
	
	public void setTime(long time)
	{	this.time = time;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATED			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean simulated;

	public boolean getSimulated()
	{	return simulated;
	}

	public void setSimulated(boolean simulated)
	{	this.simulated = simulated;
	}
}
