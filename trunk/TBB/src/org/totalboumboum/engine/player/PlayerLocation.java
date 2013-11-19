package org.totalboumboum.engine.player;

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
 * Initial location of the player
 * (when the round starts).
 * 
 * @author Vincent Labatut
 */
public class PlayerLocation implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of the location */
	private int number;
	
	/**
	 * Returns the location number.
	 * 
	 * @return
	 * 		Location number.
	 */
	public int getNumber()
	{	return number;
	}
	
	/**
	 * Changes the location number.
	 * 
	 * @param number
	 * 		New location number.
	 */
	public void setNumber(int number)
	{	this.number = number;
	}

	/////////////////////////////////////////////////////////////////
	// COLUMN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column of the starting location */
	private int col;

	/**
	 * Returns the column of the starting location.
	 * 
	 * @return
	 * 		Column of the starting location.
	 */
	public int getCol()
	{	return col;
	}
	
	/**
	 * Changes the column of the starting location.
	 * 
	 * @param col
	 * 		New column of the starting location.
	 */
	public void setCol(int col)
	{	this.col = col;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Row of the starting location */
	private int row;

	/**
	 * Returns the row of the starting location.
	 * 
	 * @return
	 * 		Row of the starting location.
	 */
	public int getRow()
	{	return row;
	}
	
	/**
	 * Changes the row of the starting location.
	 * 
	 * @param row
	 * 		New row of the starting location.
	 */
	public void setRow(int row)
	{	this.row = row;
	}
}
