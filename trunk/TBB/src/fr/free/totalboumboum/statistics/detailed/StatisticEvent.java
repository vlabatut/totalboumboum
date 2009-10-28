package fr.free.totalboumboum.statistics.detailed;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

public class StatisticEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private Integer actorId;
	private StatisticAction action;
	private Integer targetId;
	private long time;
	
	public StatisticEvent(Integer actorId, StatisticAction action, Integer targetId, long time)
	{	this.actorId = actorId;
		this.action = action;
		this.targetId = targetId;
		this.time = time;
	}

	public Integer getActorId()
	{	return actorId;
	}
	public void setActorId(Integer actorId)
	{	this.actorId = actorId;
	}

	public StatisticAction getAction()
	{	return action;
	}
	public void setAction(StatisticAction action)
	{	this.action = action;
	}

	public Integer getTargetId()
	{	return targetId;
	}
	public void setTargetId(Integer targetId)
	{	this.targetId = targetId;
	}
	
	public long getTime()
	{	return time;
	}
	public void setTime(long time)
	{	this.time = time;
	}

}
