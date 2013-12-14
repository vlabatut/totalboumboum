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

/**
 * Set of methods related to level-based
 * calculations, more precisely difference processing.
 * 
 * @author Vincent Labatut
 */
public class DeltaTools
{

	/////////////////////////////////////////////////////////////////
	// X AXIS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the difference between x1 and x2, considering the level
	 * is cyclic and using the specified direction.
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @param pixelLeftX 
	 * 		Left position, in pixels.
	 * @param pixelWidth 
	 * 		Width of the level, in pixels.
	 * @return 
	 * 		The processed difference.
	 */
	public static double getDeltaX(double x1, double x2, Direction direction, double pixelLeftX, double pixelWidth)
	{	x1 = PositionTools.normalizePositionX(x1,pixelLeftX,pixelWidth);
		x2 = PositionTools.normalizePositionX(x2,pixelLeftX,pixelWidth);
		double result = processDeltaX(x1,x2,direction,pixelWidth);
		return result;
	}

	/**
	 * Returns the difference between x1 and x2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
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
	 * 		The processed difference.
	 */
	public static double getDeltaX(double x1, double x2, double pixelLeftX, double pixelWidth)
	{	double result = getDeltaX(x1,x2,Direction.NONE,pixelLeftX,pixelWidth);
		return result;
	}

	/**
	 * Returns the difference between x1 and x2, using the specified
	 * direction. Consequently, it may not be the delta with the smallest
	 * absolute value (i.e. not necessarily the shortest path).
	 * <br/>
	 * The locations are supposed to have been normalized (i.e. they're inside the level).
	 * 
	 * @param x1 
	 * 		x coordinate of the first point.
	 * @param x2 
	 * 		x coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @param pixelWidth 
	 * 		Width of the level, in pixels.
	 * @return 
	 * 		The processed difference.
	 */
	static double processDeltaX(double x1, double x2, Direction direction, double pixelWidth)
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

	/////////////////////////////////////////////////////////////////
	// Y AXIS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the difference between y1 and y2, considering the level
	 * is cyclic and using the specified direction.
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @param pixelTopY 
	 * 		Top position, in pixels.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels.
	 * @return 
	 * 		The processed difference.
	 */
	public static double getDeltaY(double y1, double y2, Direction direction, double pixelTopY, double pixelHeight)
	{	y1 = PositionTools.normalizePositionY(y1,pixelTopY,pixelHeight);
		y2 = PositionTools.normalizePositionY(y2,pixelTopY,pixelHeight);
		double result = processDeltaY(y1,y2,direction,pixelHeight);
		return result;
	}

	/**
	 * Returns the difference between y1 and y2, considering the level
	 * is cyclic. In other words, the delta with the smallest absolute
	 * value is returned.
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
	 * 		The processed difference.
	 */
	public static double getDeltaY(double y1, double y2, double pixelTopY, double pixelHeight)
	{	double result = getDeltaY(y1,y2,Direction.NONE,pixelTopY,pixelHeight);
		return result;
	}

	/**
	 * Returns the difference between y1 and y2, using the specified
	 * direction. Consequently, it may not be the delta with the smallest
	 * absolute value (i.e. not necessarily the shortest path).
	 * <br/>
	 * The locations are supposed to have been normalized (i.e. they're inside the level).
	 * 
	 * @param y1 
	 * 		y coordinate of the first point.
	 * @param y2 
	 * 		y coordinate of the second point.
	 * @param direction 
	 * 		Forced direction.
	 * @param pixelHeight 
	 * 		Height of the level, in pixels.
	 * @return 
	 * 		The processed difference.
	 */
	static double processDeltaY(double y1, double y2, Direction direction, double pixelHeight)
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
}