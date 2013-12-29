package org.totalboumboum.game.profile;

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

import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Contains all the details needed to load a sprite.
 *  
 * @author Vincent Labatut
 */
public class SpriteInfo implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Makes a copy of this sprite info.
	 * 
	 * @return
	 * 		Copy of this sprite info.
	 */
	public SpriteInfo copy()
	{	SpriteInfo result = new SpriteInfo();
		result.setPack(pack);
		result.setFolder(folder);
		result.setName(name);
		result.setColor(color);
		return result;
	}
	
	/**
	 * Detects if this sprite info is different from the
	 * specified one.
	 * 
	 * @param spriteInfo
	 * 		Reference sprite info.
	 * @return
	 * 		{@code true} iff they are different.
	 */
	public boolean hasChanged(SpriteInfo spriteInfo)
	{	boolean result = false;
		if(!result && pack!=null && spriteInfo.getPack()!=null)
			result = pack.equals(spriteInfo.getPack());
		if(!result && folder!=null && spriteInfo.getFolder()!=null)
			result = folder.equals(spriteInfo.getFolder());
//		if(!result && name!=null && spriteInfo.getName()!=null)
//			result = name.equals(spriteInfo.getName());
		if(!result && color!=null && spriteInfo.getColor()!=null)
			result = color==spriteInfo.getColor();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PICTURES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Pack containing the described sprite */
	private String pack;
	/** Folder containing the described sprite */
	private String folder;

	/**
	 * Returns the pack containing the described sprite.
	 * 
	 * @return
	 * 		Pack containing the described sprite.
	 */
	public String getPack()
	{	return pack;
	}

	/**
	 * Changes the pack containing the described sprite.
	 * 
	 * @param pack
	 * 		New pack containing the described sprite.
	 */
	public void setPack(String pack)
	{	this.pack = pack;
	}

	/**
	 * Returns the folder containing the described sprite.
	 * 
	 * @return
	 * 		Folder containing the described sprite.
	 */
	public String getFolder()
	{	return folder;
	}

	/**
	 * Changes the folder containing the described sprite.
	 * 
	 * @param folder
	 * 		New folder containing the described sprite.
	 */
	public void setFolder(String folder)
	{	this.folder = folder;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of the sprite */
	private String name;

	/**
	 * Returns the name of the sprite.
	 * 
	 * @return
	 * 		Name of the sprite.
	 */
	public String getName()
	{	return name;
	}

	/**
	 * Changes the name of the sprite.
	 * 
	 * @param name
	 * 		New name for the sprite.
	 */
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Color of the sprite */
	private PredefinedColor color;
	
	/**
	 * Returns the color of the sprite.
	 * 
	 * @return
	 * 		Color of the sprite.
	 */
	public PredefinedColor getColor()
	{	return color;
	}

	/**
	 * Changes the color of the sprite.
	 * 
	 * @param color
	 * 		New color for the sprite.
	 */
	public void setColor(PredefinedColor color)
	{	this.color = color;
	}
}

