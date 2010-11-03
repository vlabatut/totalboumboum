package org.totalboumboum.tools.calculus;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
	public static double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction, double pixelHeight, double pixelWidth)
	{	x1 = normalizePositionX(x1);
		y1 = normalizePositionY(y1);
		x2 = normalizePositionX(x2);
		y2 = normalizePositionY(y2);
		double result = processPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
		return result;
	}
	
	public static double getPixelDistance(double x1, double y1, double x2, double y2, double pixelHeight, double pixelWidth)
	{	return getPixelDistance(x1,y1,x2,y2,Direction.NONE,pixelHeight,pixelWidth);
	}

	public static double getHorizontalPixelDistance(double x1, double x2, Direction direction, double pixelWidth)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		double result = processHorizontalPixelDistance(x1,x2,direction,pixelWidth);
		return result;
	}
	
	public static double getHorizontalPixelDistance(double x1, double x2, double pixelWidth)
	{	return getHorizontalPixelDistance(x1,x2,Direction.NONE,pixelWidth);
	}
	
	public static double getVerticalPixelDistance(double y1, double y2, Direction direction, double pixelHeight)
	{	y1 = normalizePositionY(y1);
		y2 = normalizePositionY(y2);
		double result = processVerticalPixelDistance(y1,y2,direction,pixelHeight);
		return result;
	}
	
	public static double getVerticalPixelDistance(double y1, double y2, double pixelHeight)
	{	return getVerticalPixelDistance(y1,y2,Direction.NONE,pixelHeight);
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
	public static int getTileDistance(Sprite s1, Sprite s2, Direction direction)
	{	Tile tile1 = s1.getTile();
		Tile tile2 = s2.getTile();
		int result = getTileDistance(tile1,tile2,direction);
		return result;
	}
	public static int getTileDistance(Sprite s1, Sprite s2)
	{	return getTileDistance(s1,s2,Direction.NONE);
	}
	public static int getTileDistance(Tile tile1, Tile tile2, Direction direction)
	{	int line1 = tile1.getLine();
		int col1 = tile1.getCol();
		int line2 = tile2.getLine();
		int col2 = tile2.getCol();
		int result = processTileDistance(line1,col1,line2,col2,direction);
		return result;
	}
	public static int getTileDistance(Tile tile1, Tile tile2)
	{	return getTileDistance(tile1,tile2,Direction.NONE);
	}

	public static int getTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	line1 = normalizePositionLine(line1);
		col1 = normalizePositionCol(col1);
		line2 = normalizePositionLine(line2);
		col2 = normalizePositionCol(col2);
		int result = processTileDistance(line1,col1,line2,col2,direction);
		return result;
	}
	public static int getTileDistance(int line1, int col1, int line2, int col2)
	{	return getTileDistance(line1,col1,line2,col2,Direction.NONE);
	}

	public static int getHorizontalTileDistance(int col1, int col2, Direction direction)
	{	col1 = normalizePositionCol(col1);
		col2 = normalizePositionCol(col2);
		int result = processHorizontalTileDistance(col1,col2,direction);
		return result;
	}
	public static int getHorizontalTileDistance(int col1, int col2)
	{	return getHorizontalTileDistance(col1,col2,Direction.NONE);
	}
	
	public static double getVerticalTileDistance(int line1, int line2, Direction direction)
	{	line1 = normalizePositionLine(line1);
		line2 = normalizePositionLine(line2);
		double result = processVerticalTileDistance(line1,line2,direction);
		return result;
	}
	public static double getVerticalTileDistance(int line1, int line2)
	{	return getVerticalTileDistance(line1,line2,Direction.NONE);
	}
	
	private static int processTileDistance(int line1, int col1, int line2, int col2, Direction direction)
	{	int result = 0;
		result = result + processHorizontalTileDistance(col1,col2,direction);
		result = result + processVerticalTileDistance(line1,line2,direction);
		return result;
	}
	
	private static int processHorizontalTileDistance(int col1, int col2, Direction direction)
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
	
	private static int processVerticalTileDistance(int line1, int line2, Direction direction)
	{	int result;
		int dLine = line2 - line1;
		int direct = Math.abs(dLine);
		int indirect = globalHeight - direct;
		Direction dir = direction.getVerticalPrimary();
		if(dir==Direction.NONE)
			result = Math.min(direct,indirect);
		else
		{	Direction d = Direction.getVerticalFromDouble(dLine);
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
	public static Direction getDirection(Sprite s1, Sprite s2)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		Direction result = processDirection(x1,y1,x2,y2);
		return result;
	}
	
	/**
	 * processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getDirection(double x1, double y1, double x2, double y2)
	{	x1 = normalizePositionX(x1);
		y1 = normalizePositionY(y1);
		x2 = normalizePositionX(x2);
		y2 = normalizePositionY(y2);
		Direction result = processDirection(x1,y1,x2,y2);
		return result;
	}

	/**
	 * processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getHorizontalDirection(double x1, double x2)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		Direction result = processHorizontalDirection(x1,x2);
		return result;
	}

	/**
	 * processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 */
	public static Direction getVerticalDirection(double x1, double x2)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		Direction result = processHorizontalDirection(x1,x2);
		return result;
	}

	/**
	 * processes the overall direction from locations (x1,y1) to (x2,y2),
	 * considering the level is cyclic. In other words, several
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 */
	private static Direction processDirection(double x1, double y1, double x2, double y2)
	{	Direction temp;
		Direction result = Direction.NONE;	
		temp = processHorizontalDirection(x1,x2);
		result = result.put(temp);
		temp = processVerticalDirection(y1,y2);
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
	private static Direction processHorizontalDirection(double x1, double x2)
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
	private static Direction processVerticalDirection(double y1, double y2)
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
	public static double getDeltaX(double x1, double x2, Direction direction)
	{	x1 = normalizePositionX(x1);
		x2 = normalizePositionX(x2);
		double result = processDeltaX(x1,x2,direction);
		return result;
	}
	
	/**
	 * returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public static double getDeltaX(double x1, double x2)
	{	double result = getDeltaX(x1,x2,Direction.NONE);
		return result;
	}

	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 */
	public static double getDeltaY(double y1, double y2, Direction direction)
	{	y1 = normalizePositionY(y1);
		y2 = normalizePositionY(y2);
		double result = processDeltaY(y1,y2,direction);
		return result;
	}
	
	/**
	 * returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
	 */
	public static double getDeltaY(double y1, double y2)
	{	double result = getDeltaY(y1,y2,Direction.NONE);
		return result;
	}

	/**
	 * returns the difference between x1 and x2, using the specified
	 * direction. Consequently, it may not be the delta with the smallest
	 * absolute value (i.e. not necessarily the shortest path).
	 * The locations are supposed to have been normalized (i.e. they're inside the level)
	 */
	private static double processDeltaX(double x1, double x2, Direction direction)
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
	private static double processDeltaY(double y1, double y2, Direction direction)
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
	public static Tile getTile(double x, double y)
	{	Tile result = null;
		x = CombinatoricsTools.round(x);
		y = CombinatoricsTools.round(y);
		double difX = x-pixelLeftX;
		double difY = y-pixelTopY;
		double rX = difX/RoundVariables.scaledTileDimension;
		double rY = difY/RoundVariables.scaledTileDimension;
		int rdX = (int)rX;//(int)Math.round(rX);
		int rdY = (int)rY;//(int)Math.round(rY);
		int c = rdX%globalWidth;
		int l = rdY%globalHeight;
		result = matrix[l][c];
		return result;
	}	
	public static Tile getNeighborTile(int line, int col, Direction direction)
	{	Tile result;
		int c,l;
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+globalWidth-1)%globalWidth;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%globalWidth;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+globalHeight-1)%globalHeight;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%globalHeight;
		else
			l = line;
		//
		result = matrix[l][c];
		return result;
	}
	public static List<Tile> getNeighborTiles(int line, int col)
	{	List<Tile> resultat = new ArrayList<Tile>();
		resultat.add(getNeighborTile(line, col, Direction.LEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWNLEFT));
		resultat.add(getNeighborTile(line, col, Direction.DOWN));
		resultat.add(getNeighborTile(line, col, Direction.DOWNRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.RIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPRIGHT));
		resultat.add(getNeighborTile(line, col, Direction.UPLEFT));
		resultat.add(getNeighborTile(line, col, Direction.UP));		
		return resultat;
	}	

	public static int[] normalizePosition(int line, int col)
	{	int result[] = new int[2];
		result[0] = normalizePositionLine(line);
		result[1] = normalizePositionCol(col);
		//
		return result;
	}
	public static int normalizePositionLine(int line)
	{	int result = line;
		while(result<globalHeight)
			result = result + globalHeight;
		while(result>globalHeight)
			result = result - globalHeight;
		return result;
	}
	public static int normalizePositionCol(int col)
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
	public static double[] normalizePosition(double x, double y)
	{	double result[] = new double[2];
		result[0] = normalizePositionX(x);
		result[1] = normalizePositionY(y);
		//
		return result;
	}
	public static double normalizePositionX(double x)
	{	double result = x;
		while(result<pixelLeftX)
			result = result + pixelWidth;
		while(result>pixelLeftX+pixelWidth)
			result = result - pixelWidth;
		return result;
	}
	public static double normalizePositionY(double y)
	{	double result = y;
		while(result<pixelTopY)
			result = result + pixelHeight;
		while(result>pixelTopY+pixelHeight)
			result = result - pixelHeight;
		return result;
	}
	public static boolean isInsidePosition(double x, double y)
	{	return isInsidePositionX(x) && isInsidePositionY(y);		
	}
	public static boolean isInsidePositionX(double x)
	{	//NOTE comparaison relative?
		return x>=pixelLeftX && x<=pixelLeftX+pixelWidth;
	}
	public static boolean isInsidePositionY(double y)
	{	//NOTE comparaison relative?
		return y>=pixelTopY && y<=pixelTopY+pixelHeight;
	}
}