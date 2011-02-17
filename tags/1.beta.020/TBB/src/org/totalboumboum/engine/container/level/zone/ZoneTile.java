package org.totalboumboum.engine.container.level.zone;

import java.io.Serializable;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ZoneTile implements Serializable
{
	private static final long serialVersionUID = 1L;

	public ZoneTile(int line, int col)
	{	this.line = line;
		this.col = col;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int col;
	
	public int getCol()
	{	return col;
	}
	public void setCol(int col)
	{	this.col = col;
	}
	
	/////////////////////////////////////////////////////////////////
	// LINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int line;
	
	public int getLine()
	{	return line;
	}
	public void setLine(int line)
	{	this.line = line;
	}

	/////////////////////////////////////////////////////////////////
	// LINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String variable = null;

	public String getVariable()
	{	return variable;
	}
	public void setVariable(String variable)
	{	this.variable = variable;
	}
	
	/////////////////////////////////////////////////////////////////
	// BLOCK			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String block = null;

	public String getBlock()
	{	return block;
	}
	public void setBlock(String block)
	{	this.block = block;
	}

	/////////////////////////////////////////////////////////////////
	// ITEM				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String item = null;

	public String getItem()
	{	return item;
	}
	public void setItem(String item)
	{	this.item = item;
	}
	
	/////////////////////////////////////////////////////////////////
	// BOMB				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String bomb = null;

	public String getBomb()
	{	return bomb;
	}
	public void setBomb(String bomb)
	{	this.bomb = bomb;
	}
	
	/////////////////////////////////////////////////////////////////
	// FLOOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String floor = null;

	public String getFloor()
	{	return floor;
	}
	public void setFloor(String floor)
	{	this.floor = floor;
	}
}
