package fr.free.totalboumboum.data.statistics;

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
