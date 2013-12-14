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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Set of methods related to level-based
 * calculations, more precisely distance expressed in tiles.
 * 
 * @author Vincent Labatut
 */
public class DistanceTileTools
{
	/////////////////////////////////////////////////////////////////
	// COMPOSITE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static int getTileDistance(Sprite s1, Sprite s2, Direction direction, int globalHeight, int globalWidth)
	{	Tile tile1 = s1.getTile();
		Tile tile2 = s2.getTile();
		int result = getTileDistance(tile1,tile2,direction,globalHeight,globalWidth);
		return result;
	}

	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static int getTileDistance(Sprite s1, Sprite s2, int globalHeight, int globalWidth)
	{	return getTileDistance(s1,s2,Direction.NONE,globalHeight,globalWidth);
	}

	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param tile1 
	 * 		First tile.
	 * @param tile2 
	 * 		Second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static int getTileDistance(Tile tile1, Tile tile2, Direction direction, int globalHeight, int globalWidth)
	{	int row1 = tile1.getRow();
		int col1 = tile1.getCol();
		int row2 = tile2.getRow();
		int col2 = tile2.getCol();
		int result = processTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
		return result;
	}

	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param tile1 
	 * 		First tile.
	 * @param tile2 
	 * 		Second tile.
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static int getTileDistance(Tile tile1, Tile tile2, int globalHeight, int globalWidth)
	{	return getTileDistance(tile1,tile2,Direction.NONE,globalHeight,globalWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first point.
	 * @param col1 
	 * 		Col coordinate of the first point.
	 * @param row2 
	 * 		Row coordinate of the second point.
	 * @param col2 
	 * 		Col coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the coordinates.
	 */
	public static int getTileDistance(int row1, int col1, int row2, int col2, Direction direction, int globalHeight, int globalWidth)
	{	row1 = PositionTools.normalizePositionRow(row1,globalHeight);
		col1 = PositionTools.normalizePositionCol(col1,globalWidth);
		row2 = PositionTools.normalizePositionRow(row2,globalHeight);
		col2 = PositionTools.normalizePositionCol(col2,globalWidth);
		int result = processTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second point.
	 * @param col2 
	 * 		Col coordinate of the second point.
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the tiles.
	 */
	public static int getTileDistance(int row1, int col1, int row2, int col2, int globalHeight, int globalWidth)
	{	return getTileDistance(row1,col1,row2,col2,Direction.NONE,globalHeight,globalWidth);
	}

	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @param col2
	 * 		Col coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the tiles.
	 */
	private static int processTileDistance(int row1, int col1, int row2, int col2, Direction direction, int globalHeight, int globalWidth)
	{	int result = 0;
		result = result + processHorizontalTileDistance(col1,col2,direction,globalWidth);
		result = result + processVerticalTileDistance(row1,row2,direction,globalHeight);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// HORIZONTAL			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * horizontal axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param col2 
	 * 		Col coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the columns.
	 */
	public static int getHorizontalTileDistance(int col1, int col2, Direction direction, int globalWidth)
	{	col1 = PositionTools.normalizePositionCol(col1,globalWidth);
		col2 = PositionTools.normalizePositionCol(col2,globalWidth);
		int result = processHorizontalTileDistance(col1,col2,direction,globalWidth);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the horizontal axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param col2 
	 * 		Col coordinate of the second tile.
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the columns.
	 */
	public static int getHorizontalTileDistance(int col1, int col2, int globalWidth)
	{	return getHorizontalTileDistance(col1,col2,Direction.NONE,globalWidth);
	}

	/**
	 * Processes the Manhattan distance along the horizontal axis,
	 * taking the specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param col1 
	 * 		Col coordinate of the first tile.
	 * @param col2 
	 * 		Col coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalWidth 
	 * 		Width of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the tiles.
	 */
	private static int processHorizontalTileDistance(int col1, int col2, Direction direction, int globalWidth)
	{	int result;
		int dCol = col2 - col1;
		int direct = Math.abs(dCol);
		int indirect = globalWidth - direct;
		Direction dir = direction.getHorizontalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getHorizontalFromDouble(dCol);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// VERTICAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * vertical axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the rows.
	 */
	public static double getVerticalTileDistance(int row1, int row2, Direction direction, int globalHeight)
	{	row1 = PositionTools.normalizePositionRow(row1,globalHeight);
		row2 = PositionTools.normalizePositionRow(row2,globalHeight);
		double result = processVerticalTileDistance(row1,row2,direction,globalHeight);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the vertical axis.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the rows.
	 */
	public static double getVerticalTileDistance(int row1, int row2, int globalHeight)
	{	return getVerticalTileDistance(row1,row2,Direction.NONE,globalHeight);
	}

	/**
	 * Processes the Manhattan distance along the vertical axis,
	 * taking the specified direction into account.
	 * The distance is expressed in tiles.
	 * 
	 * @param row1 
	 * 		Row coordinate of the first tile.
	 * @param row2 
	 * 		Row coordinate of the second tile.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param globalHeight 
	 * 		Height of the level, expressed in tiles.
	 * @return 
	 * 		Distance between the tiles.
	 */
	static int processVerticalTileDistance(int row1, int row2, Direction direction, int globalHeight)
	{	int result;
		int dRow = row2 - row1;
		int direct = Math.abs(dRow);
		int indirect = globalHeight - direct;
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getVerticalFromDouble(dRow);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}	
}