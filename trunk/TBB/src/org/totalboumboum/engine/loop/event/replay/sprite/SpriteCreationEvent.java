package org.totalboumboum.engine.loop.event.replay.sprite;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SpriteCreationEvent extends SpriteEvent
{	private static final long serialVersionUID = 1L;

/*
	public SpriteCreationEvent(int id, long time, String name, Role role, int line, int col)
	{	this.id = id;
		this.time = time;
		this.name = name;
		this.role = role;
		this.line = line;
		this.col = col;
	}
*/
	public SpriteCreationEvent(Sprite sprite, String name)
	{	super(sprite);
	
		// name
		this.name = name;
		
		// role
		role = sprite.getRole();
		
		// color
		color = sprite.getColor();
		
		// location
		Tile tile = sprite.getTile();
		this.line = tile.getLine();
		this.col = tile.getCol();
		
		// send event
		sendEvent = true;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;	
	}

	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Role role;
	
	public Role getRole()
	{	return role;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PredefinedColor color;
	
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int line;
	private int col;
	
	public int getLine()
	{	return line;
	}

	public int getCol()
	{	return col;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "SpriteCreationEvent("+time+":"+spriteId+"): " + getSpriteInfo() + " ";
		result = result + name + " [" + color + "," + role + "] ";
		result = result + "@(" + line + "," + col + ")";
		return result;
	}
}
