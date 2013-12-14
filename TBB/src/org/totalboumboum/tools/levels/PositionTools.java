package org.totalboumboum.tools.levels;

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

import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.computing.ApproximationTools;

/**
 * Set of methods related to level-based
 * calculations, mainly distance and delta processing.
 * 
 * @author Vincent Labatut
 */
public class PositionTools
{	
    /////////////////////////////////////////////////////////////////
	// PIXEL TO TILE	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Get the tile corresponding to the specified position.
	 * 
	 * @param x
	 * 		x coordinate of the considered position.
	 * @param y
	 * 		y coordinate of the considered position.
	 * @param pixelLeftX
	 * 		Coordinate of the level left pixels.
	 * @param pixelTopY
	 * 		Coordinate of the level top pixels.
	 * @param pixelHeight
	 * 		Height of the level, in pixels.
	 * @param pixelWidth
	 * 		Width of the level, in pixels.
	 * @param globalHeight
	 * 		Height of the level, in tiles.
	 * @param globalWidth
	 * 		Width of the level, in tiles.
	 * @return
	 * 		The tile at the specified location.
	 */
	public static int[] getTile(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth, int globalHeight, int globalWidth)
	{	double temp[] = normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		x = ApproximationTools.round(temp[0]);
		y = ApproximationTools.round(temp[1]);
		double difX = x-pixelLeftX;
		double difY = y-pixelTopY;
		double rX = difX/RoundVariables.scaledTileDimension;
		double rY = difY/RoundVariables.scaledTileDimension;
		int rdX = (int)rX;//(int)Math.round(rX);
		int rdY = (int)rY;//(int)Math.round(rY);
		int c = rdX%globalWidth;
		int l = rdY%globalHeight;
		
		int result[] = {l,c};
		return result;
	}	
	
    /////////////////////////////////////////////////////////////////
	// NEIGHBORS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Get the neighbor tile for the specified direction.
	 * 
	 * @param row
	 * 		Row of the original tile.
	 * @param col
	 * 		Col of the original tile.
	 * @param direction
	 * 		Direction of the neighbor.
	 * @param globalHeight
	 * 		Height of the level, in tiles.
	 * @param globalWidth
	 * 		Width of the level, in tiles.
	 * @return
	 * 		Neighbor tile.
	 */
	public static int[] getNeighborTile(int row, int col, Direction direction, int globalHeight, int globalWidth)
	{	int c,l;
		Direction p[] = direction.getPrimaries(); 

		if(p[0]==Direction.LEFT)
			c = (col+globalWidth-1)%globalWidth;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%globalWidth;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (row+globalHeight-1)%globalHeight;
		else if(p[1]==Direction.DOWN)
			l = (row+1)%globalHeight;
		else
			l = row;

		int result[] = {l,c};
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// NORMALIZE TILE POSITION	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified position, in order to get some coordinates
	 * actually contained in the level.
	 * 
	 * @param row
	 * 		Row number.
	 * @param col
	 * 		Col number.
	 * @param globalHeight
	 * 		Height of the level, in tiles.
	 * @param globalWidth
	 * 		Width of the level, in tiles.
	 * @return
	 * 		Normalized coordinates.
	 */
	public static int[] normalizePosition(int row, int col, int globalHeight, int globalWidth)
	{	int result[] = new int[2];
		result[0] = normalizePositionRow(row,globalHeight);
		result[1] = normalizePositionCol(col,globalWidth);
		//
		return result;
	}
	
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param row
	 * 		Row number.
	 * @param globalHeight
	 * 		Height of the level, in tiles.
	 * @return
	 * 		Normalized coordinate.
	 */
	public static int normalizePositionRow(int row, int globalHeight)
	{	int result = row;
		while(result<globalHeight)
			result = result + globalHeight;
		while(result>globalHeight)
			result = result - globalHeight;
		return result;
	}
	
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param col
	 * 		Col number.
	 * @param globalWidth
	 * 		Width of the level, in tiles.
	 * @return
	 * 		Normalized coordinate.
	 */
	public static int normalizePositionCol(int col, int globalWidth)
	{	int result = col;
		while(result<globalWidth)
			result = result + globalWidth;
		while(result>globalWidth)
			result = result - globalWidth;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// NORMALIZE PIXEL LOCATION		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified position, in order to get some coordinates
	 * actually contained in the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param y
	 * 		Position on the y axis.
	 * @param pixelLeftX
	 * 		Position of the level left pixels.
	 * @param pixelTopY
	 * 		Position of the level top pixels.
	 * @param pixelHeight
	 * 		Height of the level, in pixels.
	 * @param pixelWidth
	 * 		Width of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static double[] normalizePosition(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	double result[] = new double[2];
		result[0] = normalizePositionX(x,pixelLeftX,pixelWidth);
		result[1] = normalizePositionY(y,pixelTopY,pixelHeight);
		//
		return result;
	}
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param pixelLeftX
	 * 		Position of the level left pixels.
	 * @param pixelWidth
	 * 		Width of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static double normalizePositionX(double x, double pixelLeftX, double pixelWidth)
	{	double result = x;
		while(result<pixelLeftX)
			result = result + pixelWidth;
		while(result>pixelLeftX+pixelWidth)
			result = result - pixelWidth;
		return result;
	}
	
	/**
	 * Normalizes the specified position, in order to get a coordinate
	 * actually contained in the level.
	 * 
	 * @param y
	 * 		Position on the y axis.
	 * @param pixelTopY
	 * 		Position of the level top pixels.
	 * @param pixelHeight
	 * 		Height of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static double normalizePositionY(double y, double pixelTopY, double pixelHeight)
	{	double result = y;
		while(result<pixelTopY)
			result = result + pixelHeight;
		while(result>pixelTopY+pixelHeight)
			result = result - pixelHeight;
		return result;
	}
	
	/**
	 * Checks if the specified position is inside the level.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param y
	 * 		Position on the y axis.
	 * @param pixelLeftX
	 * 		Position of the level left pixels.
	 * @param pixelTopY
	 * 		Position of the level left pixels.
	 * @param pixelHeight
	 * 		Height of the level, in pixels.
	 * @param pixelWidth
	 * 		Width of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static boolean isInsidePosition(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	return isInsidePositionX(x,pixelLeftX,pixelWidth) && isInsidePositionY(y,pixelTopY,pixelHeight);		
	}
	
    /////////////////////////////////////////////////////////////////
	// INSIDE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Checks if the specified position is inside the level,
	 * in terms of x axis.
	 * 
	 * @param x
	 * 		Position on the x axis.
	 * @param pixelLeftX
	 * 		Position of the level left pixels.
	 * @param pixelWidth
	 * 		Width of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static boolean isInsidePositionX(double x, double pixelLeftX, double pixelWidth)
	{	//NOTE plutot comparaison relative?
		
		return x>=pixelLeftX && x<=pixelLeftX+pixelWidth;
	}
	/**
	 * Checks if the specified position is inside the level,
	 * in terms of x axis.
	 * 
	 * @param y
	 * 		Position on the y axis.
	 * @param pixelTopY
	 * 		Position of the level top pixels.
	 * @param pixelHeight
	 * 		Height of the level, in pixels.
	 * @return
	 * 		{@code true} iff the position is inside the level.
	 */
	public static boolean isInsidePositionY(double y, double pixelTopY, double pixelHeight)
	{	//NOTE plutot comparaison relative?
		return y>=pixelTopY && y<=pixelTopY+pixelHeight;
	}
}