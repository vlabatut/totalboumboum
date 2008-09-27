package fr.free.totalboumboum.game.round;

import java.io.File;
import java.util.ArrayList;

import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.points.PointsProcessor;

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


	public void finish()
	{	
	}
}
