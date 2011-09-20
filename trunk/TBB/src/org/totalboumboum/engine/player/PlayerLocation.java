package org.totalboumboum.engine.player;

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
public class PlayerLocation implements Serializable
{	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int number;

	public int getNumber()
	{	return number;
	}
	
	public void setNumber(int number)
	{	this.number = number;
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
	// ROW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int row;

	public int getRow()
	{	return row;
	}
	
	public void setRow(int row)
	{	this.row = row;
	}
}
