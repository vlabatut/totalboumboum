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

import java.util.Set;
import java.util.TreeSet;
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
public class ZoneHollowTile extends AbstractZoneTile
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a deterministic tile
	 * (non-random).
	 * 
	 * @param row
	 * 		Tile row number.
	 * @param col
	 * 		Tile column number.
	 */
	public ZoneHollowTile(int row, int col)
	{	rows.add(row);
		nRows = 1;
		cols.add(col);
		nCols = 1;
	}
	
	/**
	 * Creates a tile whose position is random.
	 * 
	 * @param rows
	 * 		Possible rows.
	 * @param nRows
	 * 		Number of rows to be drawn.
	 * @param cols
	 * 		Possible columns.
	 * @param nCols
	 * 		Number of columns to be drawn.
	 */
	public ZoneHollowTile(Set<Integer> rows, int nRows, Set<Integer> cols, int nCols)
	{	this.rows.addAll(rows);
		this.nRows = nRows;
		this.cols.addAll(cols);
		this.nCols = nCols;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROWS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of rows to be drawn, or zero for independent drawing */
	private int nRows = 0;
	/** Possible row values */
	private final Set<Integer> rows = new TreeSet<Integer>();

	/**
	 * Returns the set of all possible rows for this tile.
	 * 
	 * @return
	 * 		Set of integers.
	 */
	public Set<Integer> getRows()
	{	return rows;
	}

	/**
	 * Returns the number of rows to be drawn.
	 * 
	 * @return
	 * 		Number of rows.
	 */
	public int getRowNumber()
	{	return nRows;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROWS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of columns to be drawn, no independent drawing possible */
	private int nCols = 0;
	/** Possible column values */
	private final Set<Integer> cols = new TreeSet<Integer>();
	
	/**
	 * Returns the set of all possible columns for this tile.
	 * 
	 * @return
	 * 		Set of integers.
	 */
	public Set<Integer> getCols()
	{	return cols;
	}
	
	/**
	 * Returns the number of columns to be drawn.
	 * 
	 * @return
	 * 		Number of columns.
	 */
	public int getColNumber()
	{	return nCols;
	}
	
	/////////////////////////////////////////////////////////////////
	// VARIABLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tile variable possibly associated to this tile */
	private String variable = null;

	/**
	 * Returns the variable name.
	 * 
	 * @return
	 * 		Variable associated to this tile.
	 */
	public String getVariable()
	{	return variable;
	}
	
	/**
	 * Changes the name of the tile variable associated
	 * to this tile.
	 * 
	 * @param variable
	 * 		New name of the variable.
	 */
	public void setVariable(String variable)
	{	this.variable = variable;
	}
}
