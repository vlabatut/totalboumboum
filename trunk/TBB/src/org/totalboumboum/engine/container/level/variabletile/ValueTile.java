package org.totalboumboum.engine.container.level.variabletile;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
 * 
 * @author Vincent Labatut
 *
 */
public class ValueTile implements Serializable
{	private static final long serialVersionUID = 1L;

	
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
	private String block;

	public String getBlock()
	{	return block;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEM				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String item;

	public String getItem()
	{	return item;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String bomb;

	public String getBomb()
	{	return bomb;
	}

	/////////////////////////////////////////////////////////////////
	// FLOOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String floor;

	public String getFloor()
	{	return floor;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROBA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private float proba;

	public float getProba()
	{	return proba;	
	}
	
	public void setProba(float proba)
	{	this.proba = proba;
	}
}
