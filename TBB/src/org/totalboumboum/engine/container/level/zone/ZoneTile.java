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
public class ZoneTile extends AbstractZoneTile
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a new zone with the specified position.
	 * 
	 * @param row
	 * 		Tile row number.
	 * @param col
	 * 		Tile column number.
	 */
	public ZoneTile(int row, int col)
	{	this.row = row;
		this.col = col;
	}
	
	/////////////////////////////////////////////////////////////////
	// COLUMN			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Column number of this tile */
	private int col;
	
	/**
	 * Returns the column number of this tile.
	 * 
	 * @return
	 * 		Tile column number.
	 */
	public int getCol()
	{	return col;
	}
	
	/**
	 * Changes the column number of this tile.
	 * 
	 * @param col
	 * 		New column number.
	 */
	public void setCol(int col)
	{	this.col = col;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Row number of this tile */
	private int row;
	
	/**
	 * Returns the row number of this tile.
	 * 
	 * @return
	 * 		Tile row number.
	 */
	public int getRow()
	{	return row;
	}
	
	/**
	 * Changes the row number of this tile.
	 * 
	 * @param row
	 * 		New row number.
	 */
	public void setRow(int row)
	{	this.row = row;
	}
}
