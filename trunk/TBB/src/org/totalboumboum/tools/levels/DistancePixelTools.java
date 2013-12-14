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
import org.totalboumboum.engine.content.sprite.Sprite;

/**
 * Set of methods related to level-based
 * calculations, more precisely distances expressed in pixel.
 * 
 * @author Vincent Labatut
 */
public class DistancePixelTools
{
	/////////////////////////////////////////////////////////////////
	// COMPOSITE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static double getPixelDistance(Sprite s1, Sprite s2, Direction direction, double pixelHeight, double pixelWidth)
	{	double x1 = s1.getCurrentPosX();
		double y1 = s1.getCurrentPosY();
		double x2 = s2.getCurrentPosX();
		double y2 = s2.getCurrentPosY();
		double result = processPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
		return result;
	}

	/**
	 * Processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in pixels.
	 * 
	 * @param s1 
	 * 		First sprite.
	 * @param s2 
	 * 		Second sprite.
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the sprites.
	 */
	public static double getPixelDistance(Sprite s1, Sprite s2, double pixelHeight, double pixelWidth)
	{	return getPixelDistance(s1,s2,Direction.NONE,pixelHeight,pixelWidth);
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelLeftX 
	 * 		x coordinate of the left pixels.
	 * @param pixelTopY 
	 * 		y coordinate of the top pixels.
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getPixelDistance(double x1, double y1, double x2, double y2, Direction direction, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	x1 = PositionTools.normalizePositionX(x1,pixelLeftX,pixelWidth);
		y1 = PositionTools.normalizePositionY(y1,pixelTopY,pixelHeight);
		x2 = PositionTools.normalizePositionX(x2,pixelLeftX,pixelWidth);
		y2 = PositionTools.normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processPixelDistance(x1,y1,x2,y2,direction,pixelHeight,pixelWidth);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum.
	 * The distance is expressed in pixels.
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
	 * 		x coordinate of the left pixels.
	 * @param pixelTopY 
	 * 		y coordinate of the top pixels.
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getPixelDistance(double x1, double y1, double x2, double y2, double pixelLeftX, double pixelTopY, double pixelHeight, double pixelWidth)
	{	return getPixelDistance(x1,y1,x2,y2,Direction.NONE,pixelLeftX,pixelTopY,pixelHeight,pixelWidth);
	}

	/**
	 * Processes the Manhattan distance, taking the
	 * specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	private static double processPixelDistance(double x1, double y1, double x2, double y2, Direction direction, double pixelHeight, double pixelWidth)
	{	double result = 0;
		result = result + processHorizontalPixelDistance(x1,x2,direction,pixelWidth);
		result = result + processVerticalPixelDistance(y1,y2,direction,pixelHeight);
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
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelLeftX 
	 * 		x coordinate of the left pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getHorizontalPixelDistance(double x1, double x2, Direction direction, double pixelLeftX, double pixelWidth)
	{	x1 = PositionTools.normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = PositionTools.normalizePositionX(x2,pixelLeftX,pixelWidth);
		double result = processHorizontalPixelDistance(x1,x2,direction,pixelWidth);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the horizontal axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param pixelLeftX 
	 * 		x coordinate of the left pixels.
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getHorizontalPixelDistance(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	return getHorizontalPixelDistance(x1,x2,Direction.NONE,pixelLeftX,pixelWidth);
	}

	/**
	 * Processes the Manhattan distance along the horizontal axis,
	 * taking the specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelWidth 
	 * 		Width of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
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

	/////////////////////////////////////////////////////////////////
	// VERTICAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, taking the
	 * specified direction into account, along the
	 * vertical axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelTopY 
	 * 		y coordinate of the top pixels.
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getVerticalPixelDistance(double y1, double y2, Direction direction, double pixelTopY, double pixelHeight)
	{	y1 = PositionTools.normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = PositionTools.normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processVerticalPixelDistance(y1,y2,direction,pixelHeight);
		return result;
	}

	/**
	 * Normalizes the specified coordinates, then
	 * processes the Manhattan distance, considering
	 * all possible directions and taking the minimum, 
	 * along the vertical axis.
	 * The distance is expressed in pixels.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param pixelTopY 
	 * 		y coordinate of the top pixels.
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	public static double getVerticalPixelDistance(double y1, double y2, double pixelTopY, double pixelHeight)
	{	return getVerticalPixelDistance(y1,y2,Direction.NONE,pixelTopY,pixelHeight);
	}

	/**
	 * Processes the Manhattan distance along the vertical axis,
	 * taking the specified direction into account.
	 * The distance is expressed in pixels.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction
	 * 		Direction used to process the distance. 
	 * @param pixelHeight 
	 * 		Height of the level, expressed in pixels.
	 * @return 
	 * 		Distance between the points.
	 */
	static double processVerticalPixelDistance(double y1, double y2, Direction direction, double pixelHeight)
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
}