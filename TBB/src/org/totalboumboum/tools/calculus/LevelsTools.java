package org.totalboumboum.tools.calculus;

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

import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.engine.content.manager.trajectory.MoveZone;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelsTools
{	
    /////////////////////////////////////////////////////////////////
	// PIXEL DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * process the Manhattan distance
	 */
	public static double getPixelDistance(Sprite s1, Sprite s2, Direction direction, double pixelHeight, double pixelWidth)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		double result = processPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
		return result;
	}
	
	public static double getPixelDistance(Sprite s1, Sprite s2, double pixelHeight, double pixelWidth)
	{	return getPixelDistance(s1,s2,Direction.NONE,pixelHeight,pixelWidth);
	}
	
	/**
	 * process the Manhattan distance
	 */
	public static double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	x1 = normalizePositionX(x1,pixelLeftX,pixelWidth);
		y1 = normalizePositionY(y1,pixelTopY,pixelHeight);
		x2 = normalizePositionX(x2,pixelLeftX,pixelWidth);
		y2 = normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
		return result;
	}
	
	public static double getPixelDistance(double x1, double y1, double x2, double y2, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	return getPixelDistance(x1,y1,x2,y2,Direction.NONE,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	public static double getHorizontalPixelDistance(double x1, double x2, Direction direction, double pixelLeftX, double pixelWidth)
	{	x1 = normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = normalizePositionX(x2,pixelLeftX,pixelWidth);
		double result = processHorizontalPixelDistance(x1,x2,direction,pixelWidth);
		return result;
	}
	
	public static double getHorizontalPixelDistance(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	return getHorizontalPixelDistance(x1,x2,Direction.NONE,pixelLeftX,pixelWidth);
	}
	
	public static double getVerticalPixelDistance(double y1, double y2, Direction direction, double pixelTopY, double pixelHeight)
	{	y1 = normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processVerticalPixelDistance(y1,y2,direction,pixelHeight);
		return result;
	}
	
	public static double getVerticalPixelDistance(double y1, double y2, double pixelTopY, double pixelHeight)
	{	return getVerticalPixelDistance(y1,y2,Direction.NONE,pixelTopY,pixelHeight);
	}
	
	/**
	 * process the manhattan distance
	 */
	private static double processPixelDistance(double x1, double y1, double x2, double y2, Direction direction, double pixelHeight, double pixelWidth)
	{	double result = 0;
		result = result + processHorizontalPixelDistance(x1,x2,direction,pixelWidth);
		result = result + processVerticalPixelDistance(y1,y2,direction,pixelHeight);
		return result;
	}
	
	private static double processHorizontalPixelDistance(double x1, double x2, Direction direction, double pixelWidth)
	{	double result;
		double dx = x2 - x1;
		double direct = Math.abs(dx);
		double indirect = pixelWidth - direct;
		Direction dir = direction.getHorizontalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getHorizontalFromDouble(dx);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}
	
	private static double processVerticalPixelDistance(double y1, double y2, Direction direction, double pixelHeight)
	{	double result;
		double dy = y2 - y1;
		double direct = Math.abs(dy);
		double indirect = pixelHeight - direct;
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getVerticalFromDouble(dy);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

    /////////////////////////////////////////////////////////////////
	// TILE DISTANCES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static int getTileDistance(Sprite s1, Sprite s2, Direction direction, int globalHeight, int globalWidth)
	{	Tile tile1 = s1.getTile();
		Tile tile2 = s2.getTile();
		int result = getTileDistance(tile1,tile2,direction,globalHeight,globalWidth);
		return result;
	}
	public static int getTileDistance(Sprite s1, Sprite s2, int globalHeight, int globalWidth)
	{	return getTileDistance(s1,s2,Direction.NONE,globalHeight,globalWidth);
	}
	public static int getTileDistance(Tile tile1, Tile tile2, Direction direction, int globalHeight, int globalWidth)
	{	int row1 = tile1.getRow();
		int col1 = tile1.getCol();
		int row2 = tile2.getRow();
		int col2 = tile2.getCol();
		int result = processTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
		return result;
	}
	public static int getTileDistance(Tile tile1, Tile tile2, int globalHeight, int globalWidth)
	{	return getTileDistance(tile1,tile2,Direction.NONE,globalHeight,globalWidth);
	}

	public static int getTileDistance(int row1, int col1, int row2, int col2, Direction direction, int globalHeight, int globalWidth)
	{	row1 = normalizePositionRow(row1,globalHeight);
		col1 = normalizePositionCol(col1,globalWidth);
		row2 = normalizePositionRow(row2,globalHeight);
		col2 = normalizePositionCol(col2,globalWidth);
		int result = processTileDistance(row1,col1,row2,col2,direction,globalHeight,globalWidth);
		return result;
	}
	public static int getTileDistance(int row1, int col1, int row2, int col2, int globalHeight, int globalWidth)
	{	return getTileDistance(row1,col1,row2,col2,Direction.NONE,globalHeight,globalWidth);
	}

	public static int getHorizontalTileDistance(int col1, int col2, Direction direction, int globalWidth)
	{	col1 = normalizePositionCol(col1,globalWidth);
		col2 = normalizePositionCol(col2,globalWidth);
		int result = processHorizontalTileDistance(col1,col2,direction,globalWidth);
		return result;
	}
	public static int getHorizontalTileDistance(int col1, int col2, int globalWidth)
	{	return getHorizontalTileDistance(col1,col2,Direction.NONE,globalWidth);
	}
	
	public static double getVerticalTileDistance(int row1, int row2, Direction direction, int globalHeight)
	{	row1 = normalizePositionRow(row1,globalHeight);
		row2 = normalizePositionRow(row2,globalHeight);
		double result = processVerticalTileDistance(row1,row2,direction,globalHeight);
		return result;
	}
	public static double getVerticalTileDistance(int row1, int row2, int globalHeight)
	{	return getVerticalTileDistance(row1,row2,Direction.NONE,globalHeight);
	}
	
	private static int processTileDistance(int row1, int col1, int row2, int col2, Direction direction, int globalHeight, int globalWidth)
	{	int result = 0;
		result = result + processHorizontalTileDistance(col1,col2,direction,globalWidth);
		result = result + processVerticalTileDistance(row1,row2,direction,globalHeight);
		return result;
	}
	
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
	
	private static int processVerticalTileDistance(int row1, int row2, Direction direction, int globalHeight)
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

	/////////////////////////////////////////////////////////////////
	// DIRECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * processes the direction from the sprite s1 to the sprite s2, considering the 
	 * level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getDirection(Sprite s1, Sprite s2, double pixelHeight, double pixelWidth)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		Direction result = processDirection(x1,y1,x2,y2,pixelHeight,pixelWidth);
		return result;
	}
	
	/**
	 * processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getDirection(double x1, double y1, double x2, double y2, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	x1 = normalizePositionX(x1,pixelLeftX,pixelWidth);
		y1 = normalizePositionY(y1,pixelTopY,pixelHeight);
		x2 = normalizePositionX(x2,pixelLeftX,pixelWidth);
		y2 = normalizePositionY(y2,pixelTopY,pixelHeight);
		Direction result = processDirection(x1,y1,x2,y2,pixelHeight,pixelWidth);
		return result;
	}

	/**
	 * processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getHorizontalDirection(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	x1 = normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = normalizePositionX(x2,pixelLeftX,pixelWidth);
		Direction result = processHorizontalDirection(x1,x2,pixelWidth);
		return result;
	}

	/**
	 * processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getVerticalDirection(double y1, double y2, double pixelTopY, double pixelHeight)
	{	y1 = normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = normalizePositionY(y2,pixelTopY,pixelHeight);
		Direction result = processVerticalDirection(y1,y2,pixelHeight);
		return result;
	}

	/**
	 * processes the overall direction from locations (x1,y1) to (x2,y2),
	 * considering the level is cyclic. In other words, several
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 */
	private static Direction processDirection(double x1, double y1, double x2, double y2, double pixelHeight, double pixelWidth)
	{	Direction temp;
		Direction result = Direction.NONE;	
		temp = processHorizontalDirection(x1,x2,pixelWidth);
		result = result.put(temp);
		temp = processVerticalDirection(y1,y2,pixelHeight);
		result = result.put(temp);
		return result;
	}
	
	/**
	 * processes the horizontal direction from locations x1 to x2,
	 * considering the level is cyclic. In other words, two opposite
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 * The locations are supposed to have been normalized (i.e. they're in the level)
	 */
	private static Direction processHorizontalDirection(double x1, double x2, double pixelWidth)
	{	Direction result;
		double dx = x2 - x1;
		result = Direction.getHorizontalFromDouble(dx);
		double hemi = ((double)pixelWidth)/2;
		if(Math.abs(dx)>hemi)
			result = result.getOpposite();
		return result;
	}
	
	/**
	 * processes the vertical direction from locations y1 to y2,
	 * considering the level is cyclic. In other words, two opposite
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 * The locations are supposed to have been normalized (i.e. they're in the level)
	 */
	private static Direction processVerticalDirection(double y1, double y2, double pixelHeight)
	{	Direction result;
		double dy = y2 - y1;
		result = Direction.getVerticalFromDouble(dy);
		double hemi = ((double)pixelHeight)/2;
		if(Math.abs(dy)>hemi)
			result = result.getOpposite();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// APPROXIMATE DIRECTIONS	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * processes the direction from s1 to s2, considering approximate 
	 * distance (i.e. it will be NONE even if s1 and s2 are just relatively close).
	 * It also considers the level circularity, i.e. it will choose the
	 * directions corresponding to the shortest distances. 
	 */
	public static Direction getCompositeFromSprites(Sprite s1, Sprite s2)
	{	Direction result;
		if(s1==null || s2==null)
			result = Direction.NONE;
		else
		{	double x1 = s1.getCurrentPosX();
			double y1 = s1.getCurrentPosY();
			double x2 = s2.getCurrentPosX();
			double y2 = s2.getCurrentPosY();
			result = getCompositeFromLocations(x1,y1,x2,y2);
		}
		return result;
	}

	/**
	 * processes the direction from the sprite in the specified MoveZone to the specified obstacle.
	 * Approximate distance is considered (i.e. it will be NONE even if s1 and s2 
	 * are just relatively close). It also considers the level circularity, 
	 * i.e. it will choose the directions corresponding to the shortest distances 
	 */
	public static Direction getCompositeFromSprites(MoveZone mz, Sprite obstacle)
	{	Direction result;
		if(obstacle==null)
			result = Direction.NONE;
		else
		{	double x1 = mz.getCurrentX();
			double y1 = mz.getCurrentY();
			double x2 = obstacle.getCurrentPosX();
			double y2 = obstacle.getCurrentPosY();
			result = getCompositeFromLocations(x1,y1,x2,y2);
		}
		return result;
	}
	
	/**
	 * returns the direction from the (x1,y1) location to the (x2,y2) location.
	 * Warning: consider approximate distance, i.e. will be NONE
	 * if the locations are relatively close.
	 * Also: considers the level circularity, i.e. will choose the
	 * directions corresponding to the shortest distances 
	 */
	private static Direction getCompositeFromLocations(double x1, double y1, double x2, double y2)
	{	double dx = RoundVariables.level.getDeltaX(x1,x2);
		double dy = RoundVariables.level.getDeltaY(y1,y2);
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// DELTAS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public static double getDeltaX(double x1, double x2, Direction direction, double pixelLeftX, double pixelWidth)
	{	x1 = normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = normalizePositionX(x2,pixelLeftX,pixelWidth);
		double result = processDeltaX(x1,x2,direction,pixelWidth);
		return result;
	}
	
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public static double getDeltaX(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	double result = getDeltaX(x1,x2,Direction.NONE,pixelLeftX,pixelWidth);
		return result;
	}

	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public static double getDeltaY(double y1, double y2, Direction direction, double pixelTopY, double pixelHeight)
	{	y1 = normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processDeltaY(y1,y2,direction,pixelHeight);
		return result;
	}
	
	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public static double getDeltaY(double y1, double y2, double pixelTopY, double pixelHeight)
	{	double result = getDeltaY(y1,y2,Direction.NONE,pixelTopY,pixelHeight);
		return result;
	}

	/**
	 * returns the difference between x1 and x2, using the specified
	 * direction. Consequently, it may not be the delta with the smallest
	 * absolute value (i.e. not necessarily the shortest path).
	 * The locations are supposed to have been normalized (i.e. they're inside the level)
	 */
	private static double processDeltaX(double x1, double x2, Direction direction, double pixelWidth)
	{	double result;
		double direct = x2 - x1;
		double absDirect = Math.abs(direct);
		double indirect = -1*Math.signum(direct)*(pixelWidth - absDirect);
		double absIndirect = Math.abs(indirect);
		Direction dir = direction.getHorizontalPrimary();
		if(dir==Direction.NONE)
		{	if(absDirect<=absIndirect)
				result = direct;
			else 
				result = indirect;		
		}
		else
		{	Direction d = Direction.getHorizontalFromDouble(direct);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

	/**
	 * returns the difference between y1 and y2, using the specified
	 * direction. Consequently, it may not be the delta with the smallest
	 * absolute value (i.e. not necessarily the shortest path)
	 * The locations are supposed to have been normalized (i.e. they're inside the level)
	 */
	private static double processDeltaY(double y1, double y2, Direction direction, double pixelHeight)
	{	double result;
		double direct = y2 - y1;
		double absDirect = Math.abs(direct);
		double indirect = -1*Math.signum(direct)*(pixelHeight - absDirect);
		double absIndirect = Math.abs(indirect);
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
		{	if(absDirect<=absIndirect)
				result = direct;
			else 
				result = indirect;		
		}
		else
		{	Direction d = Direction.getVerticalFromDouble(direct);
			if(dir==d)
				result = direct;
			else
				result = indirect;
		}		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// TILE LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static int[] getTile(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth, int globalHeight, int globalWidth)
	{	double temp[] = normalizePosition(x,y,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
		x = CombinatoricsTools.round(temp[0]);
		y = CombinatoricsTools.round(temp[1]);
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

	public static int[] normalizePosition(int row, int col, int globalHeight, int globalWidth)
	{	int result[] = new int[2];
		result[0] = normalizePositionRow(row,globalHeight);
		result[1] = normalizePositionCol(col,globalWidth);
		//
		return result;
	}
	public static int normalizePositionRow(int row, int globalHeight)
	{	int result = row;
		while(result<globalHeight)
			result = result + globalHeight;
		while(result>globalHeight)
			result = result - globalHeight;
		return result;
	}
	public static int normalizePositionCol(int col, int globalWidth)
	{	int result = col;
		while(result<globalWidth)
			result = result + globalWidth;
		while(result>globalWidth)
			result = result - globalWidth;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PIXEL LOCATION	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static double[] normalizePosition(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	double result[] = new double[2];
		result[0] = normalizePositionX(x,pixelLeftX,pixelWidth);
		result[1] = normalizePositionY(y,pixelTopY,pixelHeight);
		//
		return result;
	}
	public static double normalizePositionX(double x, double pixelLeftX, double pixelWidth)
	{	double result = x;
		while(result<pixelLeftX)
			result = result + pixelWidth;
		while(result>pixelLeftX+pixelWidth)
			result = result - pixelWidth;
		return result;
	}
	public static double normalizePositionY(double y, double pixelTopY, double pixelHeight)
	{	double result = y;
		while(result<pixelTopY)
			result = result + pixelHeight;
		while(result>pixelTopY+pixelHeight)
			result = result - pixelHeight;
		return result;
	}
	public static boolean isInsidePosition(double x, double y, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	return isInsidePositionX(x,pixelLeftX,pixelWidth) && isInsidePositionY(y,pixelTopY,pixelHeight);		
	}
	public static boolean isInsidePositionX(double x, double pixelLeftX, double pixelWidth)
	{	//NOTE comparaison relative?
		return x>=pixelLeftX && x<=pixelLeftX+pixelWidth;
	}
	public static boolean isInsidePositionY(double y, double pixelTopY, double pixelHeight)
	{	//NOTE comparaison relative?
		return y>=pixelTopY && y<=pixelTopY+pixelHeight;
	}
}