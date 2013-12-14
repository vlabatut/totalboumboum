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
import org.totalboumboum.engine.content.manager.trajectory.MoveZone;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.game.round.RoundVariables;

/**
 * Set of methods related to level-based
 * calculations, more precisely directions.
 * 
 * @author Vincent Labatut
 */
public class DirectionTools
{
	/////////////////////////////////////////////////////////////////
	// COMPOSITE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the direction from the sprite {@code s1} to the sprite {@code s2}, considering the 
	 * level is cyclic (i.e. using the shortest path).
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param pixelHeight
	 * 		Height of the level, in pixels. 
	 * @param pixelWidth 
	 * 		Width of the level, in pixels. 
	 * @return
	 * 		The resulting direction. 
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
	 * Processes the direction from the location (x1,y1) to the location (x2,y2),
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param pixelLeftX 
	 * 		Left position, in pixels.
	 * @param pixelTopY 
	 * 		Top position, in pixels.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels. 
	 * @param pixelWidth 
	 * 		Width of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
	 */
	public static Direction getDirection(double x1, double y1, double x2, double y2, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	x1 = PositionTools.normalizePositionX(x1,pixelLeftX,pixelWidth);
		y1 = PositionTools.normalizePositionY(y1,pixelTopY,pixelHeight);
		x2 = PositionTools.normalizePositionX(x2,pixelLeftX,pixelWidth);
		y2 = PositionTools.normalizePositionY(y2,pixelTopY,pixelHeight);
		Direction result = processDirection(x1,y1,x2,y2,pixelHeight,pixelWidth);
		return result;
	}

	/**
	 * Processes the overall direction from locations (x1,y1) to (x2,y2),
	 * considering the level is cyclic. In other words, several
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels. 
	 * @param pixelWidth 
	 * 		Width of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
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
	 * Processes the direction from s1 to s2, considering approximate 
	 * distance (i.e. it will be NONE even if s1 and s2 are just relatively close).
	 * It also considers the level circularity, i.e. it will choose the
	 * directions corresponding to the shortest distances.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @return 
	 * 		The resulting direction. 
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
	 * Processes the direction from the sprite in the specified MoveZone to the specified obstacle.
	 * Approximate distance is considered (i.e. it will be NONE even if s1 and s2 
	 * are just relatively close). It also considers the level circularity, 
	 * i.e. it will choose the directions corresponding to the shortest distances.
	 *  
	 * @param mz
	 * 		Move zone.
	 * @param obstacle 
	 * 		Considered obstacle.
	 * @return 
	 * 		The resulting direction. 
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
	 * directions corresponding to the shortest distances.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @return 
	 * 		The resulting direction. 
	 */
	private static Direction getCompositeFromLocations(double x1, double y1, double x2, double y2)
	{	double dx = RoundVariables.level.getDeltaX(x1,x2);
		double dy = RoundVariables.level.getDeltaY(y1,y2);
		Direction result = Direction.getCompositeFromRelativeDouble(dx,dy);
		return result;
	}	

	/////////////////////////////////////////////////////////////////
	// HORIZONTAL			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the direction from the location x1 to the location x2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param pixelLeftX 
	 * 		Left position, in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
	 */
	public static Direction getHorizontalDirection(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	x1 = PositionTools.normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = PositionTools.normalizePositionX(x2,pixelLeftX,pixelWidth);
		Direction result = processHorizontalDirection(x1,x2,pixelWidth);
		return result;
	}

	/**
	 * Processes the horizontal direction from locations x1 to x2,
	 * considering the level is cyclic. In other words, two opposite
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 * <br/>
	 * The locations are supposed to have been normalized (i.e. they're in the level).
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param pixelWidth 
	 * 		Width of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
	 */
	static Direction processHorizontalDirection(double x1, double x2, double pixelWidth)
	{	Direction result;
		double dx = x2 - x1;
		result = Direction.getHorizontalFromDouble(dx);
		double hemi = ((double)pixelWidth)/2;
		if(Math.abs(dx)>hemi)
			result = result.getOpposite();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// VERTICAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the direction from the location y1 to the location y2,
	 *  considering the level is cyclic (i.e. using the shortest path).
	 *  
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param pixelTopY 
	 * 		Top position, in pixels.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
	 */
	public static Direction getVerticalDirection(double y1, double y2, double pixelTopY, double pixelHeight)
	{	y1 = PositionTools.normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = PositionTools.normalizePositionY(y2,pixelTopY,pixelHeight);
		Direction result = processVerticalDirection(y1,y2,pixelHeight);
		return result;
	}

	/**
	 * Processes the vertical direction from locations y1 to y2,
	 * considering the level is cyclic. In other words, two opposite
	 * directions are considered, and the one corresponding to the 
	 * shortest distance is returned.
	 * <br/>
	 * The locations are supposed to have been normalized (i.e. they're in the level).
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels. 
	 * @return 
	 * 		The resulting direction. 
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
}