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
	
	private String actor;
	private StatisticAction action;
	private String target;
	private long time;
	
	public StatisticEvent(String actor, StatisticAction action, String target, long time)
	{	this.actor = actor;
		this.action = action;
		this.target = target;
		this.time = time;
	}

	public String getActor()
	{	return actor;
	}
	public void setActor(String actor)
	{	this.actor = actor;
	}

	public StatisticAction getAction()
	{	return action;
	}
	public void setAction(StatisticAction action)
	{	this.action = action;
	}

	public String getTarget()
	{	return target;
	}
	public void setTarget(String target)
	{	this.target = target;
	}
	
	public long getTime()
	{	return time;
	}
	public void setTime(long time)
	{	this.time = time;
	}

}
