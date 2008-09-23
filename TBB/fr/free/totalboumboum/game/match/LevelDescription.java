package fr.free.totalboumboum.game.match;

import java.io.File;
import java.util.ArrayList;

import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.round.PlayMode;

public class LevelDescription
{
	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	private String packname;

	public String getName()
	{	return name;	
	}
	public void setName(String name)
	{	this.name = name;
	}
	
	public String getPackName()
	{	return packname;	
	}
	public void setPackname(String packname)
	{	this.packname = packname;
	}
	
	public String getPath()
	{	String result = packname+File.separator+name;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PLAY MODE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PlayMode playMode;

	public PlayMode getPlayMode()
	{	return playMode;	
	}
	public void setPlayMode(PlayMode playMode)
	{	this.playMode = playMode;
	}
	
	/////////////////////////////////////////////////////////////////
	// POINT PROCESSOR	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointProcessor pointProcessor;
	
	public PointProcessor getPointProcessor()
	{	return pointProcessor;	
	}
	public void setPointProcessor(PointProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// TIME LIMIT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private long timeLimit;
	
	public long getTimeLimit()
	{	return timeLimit;	
	}
	public void setTimeLimit(long timeLimit)
	{	this.timeLimit = timeLimit;
	}

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits limits;

	public Limits getLimits()
	{	return limits;
	}
	public void setLimits(Limits limits)
	{	this.limits = limits;
	}
	
	
	
	public void finish()
	{	// limits
		limits.finish();
		limits = null;
		// misc
		playMode = null;
		pointProcessor = null;
	}
}
