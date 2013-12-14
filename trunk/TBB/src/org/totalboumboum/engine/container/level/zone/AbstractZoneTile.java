package org.totalboumboum.engine.container.level.zone;

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
import java.util.logging.Level;

import org.totalboumboum.engine.container.tile.Tile;

/**
 * This class represents a single tile in
 * a zone. By opposition to {@link Tile},
 * which represents a tile in a {@link Level}
 * (an instancied zone).
 * 
 * @author Vincent Labatut
 */
public class AbstractZoneTile implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// BLOCK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Block name, or {@code null} if no block */
	private String block = null;

	/**
	 * Returns the block name for this tile.
	 * 
	 * @return
	 * 		Block name.
	 */
	public String getBlock()
	{	return block;
	}
	
	/**
	 * Changes the block name for this tile.
	 * 
	 * @param block
	 * 		New block name.
	 */
	public void setBlock(String block)
	{	this.block = block;
	}

	/////////////////////////////////////////////////////////////////
	// ITEM				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Item name, or {@code null} if no block */
	private String item = null;

	/**
	 * Returns the item name for this tile.
	 * 
	 * @return
	 * 		Item name.
	 */
	public String getItem()
	{	return item;
	}

	/**
	 * Changes the item name for this tile.
	 * 
	 * @param item
	 * 		New item name.
	 */
	public void setItem(String item)
	{	this.item = item;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Bomb name, or {@code null} if no block */
	private String bomb = null;

	/**
	 * Returns the bomb name for this tile.
	 * 
	 * @return
	 * 		Bomb name.
	 */
	public String getBomb()
	{	return bomb;
	}
	
	/**
	 * Changes the bomb name for this tile.
	 * 
	 * @param bomb
	 * 		New bomb name.
	 */
	public void setBomb(String bomb)
	{	this.bomb = bomb;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Floor name, or {@code null} if no block */
	private String floor = null;

	/**
	 * Returns the floor name for this tile.
	 * 
	 * @return
	 * 		Floor name.
	 */
	public String getFloor()
	{	return floor;
	}
	
	/**
	 * Changes the floor name for this tile.
	 * 
	 * @param floor
	 * 		New floor name.
	 */
	public void setFloor(String floor)
	{	this.floor = floor;
	}
}
