package fr.free.totalboumboum.configuration.profile;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

public class SpriteInfo
{
	public SpriteInfo copy()
	{	SpriteInfo result = new SpriteInfo();
		result.setPack(pack);
		result.setFolder(folder);
		result.setName(name);
		result.setColor(color);
		return result;
	}
	
	public boolean hasChanged(SpriteInfo spriteInfo)
	{	boolean result = false;
		if(!result)
			result = pack.equals(spriteInfo.getPack());
		if(!result)
			result = folder.equals(spriteInfo.getFolder());
//		if(!result)
//			result = name.equals(spriteInfo.getName());
		if(!result)
			result = color==spriteInfo.getColor();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PICTURES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String pack;
	private String folder;

	public String getPack()
	{	return pack;
	}

	public void setPack(String pack)
	{	this.pack = pack;
	}

	public String getFolder()
	{	return folder;
	}

	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;

	public String getName()
	{	return name;
	}

	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;

	public PredefinedColor getColor()
	{	return color;
	}

	public void setColor(PredefinedColor color)
	{	this.color = color;
	}
}

