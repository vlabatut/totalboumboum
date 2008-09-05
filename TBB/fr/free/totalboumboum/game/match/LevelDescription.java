package fr.free.totalboumboum.game.match;

import java.io.File;

import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.round.PlayMode;

public class LevelDescription
{
	private String name;
	private String packname;
	private PlayMode playMode;
	private PointProcessor pointProcessor;
	
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

	public PlayMode getPlayMode()
	{	return playMode;	
	}
	public void setPlayMode(PlayMode playMode)
	{	this.playMode = playMode;
	}
	
	public PointProcessor getPointProcessor()
	{	return pointProcessor;	
	}
	public void setPointProcessor(PointProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}
}
