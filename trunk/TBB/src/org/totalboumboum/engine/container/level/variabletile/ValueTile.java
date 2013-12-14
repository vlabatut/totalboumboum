package org.totalboumboum.engine.container.level.variabletile;

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

/**
 * Possibl value in a tile variable
 * (see {@link VariableTile}).
 * 
 * @author Vincent Labatut
 */
public class ValueTile implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/***
	 * Creates a tile value for a tile random variable.
	 * 
	 * @param floor
	 * 		Floor name (or {@code null} for none).
	 * @param block
	 * 		Block name (or {@code null} for none).
	 * @param item
	 * 		Item name (or {@code null} for none).
	 * @param bomb
	 * 		Bomb name (or {@code null} for none).
	 * @param proba
	 * 		Value probability.
	 */
	public ValueTile(String floor, String block, String item, String bomb, float proba)
	{	this.block = block;
		this.item = item;
		this.bomb = bomb;
		this.floor = floor;
		this.proba = proba;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Block name (or {@code null} for none) */
	private String block;

	/**
	 * Returns the block name for this tile value 
	 * (or {@code null} for none).
	 * 
	 * @return
	 * 		Sprite name.
	 */
	public String getBlock()
	{	return block;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Item name (or {@code null} for none) */
	private String item;

	/**
	 * Returns the item name for this tile value 
	 * (or {@code null} for none).
	 * 
	 * @return
	 * 		Sprite name.
	 */
	public String getItem()
	{	return item;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Bomb name (or {@code null} for none) */
	private String bomb;

	/**
	 * Returns the bomb name for this tile value 
	 * (or {@code null} for none).
	 * 
	 * @return
	 * 		Sprite name.
	 */
	public String getBomb()
	{	return bomb;
	}

	/////////////////////////////////////////////////////////////////
	// FLOOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Floor name (or {@code null} for none) */
	private String floor;

	/**
	 * Returns the floor name for this tile value 
	 * (or {@code null} for none).
	 * 
	 * @return
	 * 		Sprite name.
	 */
	public String getFloor()
	{	return floor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROBA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Probability for this value to be drawn */
	private float proba;
	
	/**
	 * Returns the probability associated to this value.
	 * 
	 * @return
	 * 		A probability measure.
	 */
	public float getProba()
	{	return proba;	
	}
	
	/**
	 * Changes the probability associated to this value.
	 * 
	 * @param proba
	 * 		The new probability measure.
	 */
	public void setProba(float proba)
	{	this.proba = proba;
	}
}
