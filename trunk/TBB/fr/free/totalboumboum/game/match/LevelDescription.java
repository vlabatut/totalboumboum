package fr.free.totalboumboum.game.match;

import java.io.File;

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
}
