package org.totalboumboum.engine.loop.event.replay.sprite;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Event representing the creation of a new sprite.
 * 
 * @author Vincent Labatut
 */
public class SpriteCreationEvent extends SpriteEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;

/*
	public SpriteCreationEvent(int id, long time, String name, Role role, int row, int col)
	{	this.id = id;
		this.time = time;
		this.name = name;
		this.role = role;
		this.row = row;
		this.col = col;
	}
*/
	/**
	 * Builds a new event to represent
	 * the creation of a new sprite.
	 * 
	 * @param sprite
	 * 		Concerned sprite.
	 * @param name
	 * 		Sprite name.
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
		this.row = tile.getRow();
		this.col = tile.getCol();
		
		// send event
		sendEvent = true;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of the new sprite */
	private String name;
	
	/**
	 * Returns the name of the new sprite.
	 * 
	 * @return
	 * 		The name of the new sprite.
	 */
	public String getName()
	{	return name;	
	}

	/////////////////////////////////////////////////////////////////
	// ROLE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Role of the new sprite */
	private Role role;
	
	/**
	 * Returns the role of the new sprite.
	 * 
	 * @return
	 * 		The role of the new sprite.
	 */
	public Role getRole()
	{	return role;	
	}
	
	/////////////////////////////////////////////////////////////////
	// COLOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Color of the new sprite */
	private PredefinedColor color;
	
	/**
	 * Returns the color of the new sprite.
	 * 
	 * @return
	 * 		The color of the new sprite.
	 */
	public PredefinedColor getColor()
	{	return color;	
	}
	
	/////////////////////////////////////////////////////////////////
	// LOCATION			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Row position of the new sprite */
	private int row;
	/** Col position of the new sprite */
	private int col;
	
	/**
	 * Returns the row position of the new sprite.
	 * 
	 * @return
	 * 		The row position of the new sprite.
	 */
	public int getRow()
	{	return row;
	}

	/**
	 * Returns the col position of the new sprite.
	 * 
	 * @return
	 * 		The col position of the new sprite.
	 */
	public int getCol()
	{	return col;	
	}

	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "SpriteCreationEvent("+time+":"+spriteId+"): " + getSpriteInfo() + " ";
		result = result + name + " [" + color + "," + role + "] ";
		result = result + "@(" + row + "," + col + ")";
		return result;
	}
}
