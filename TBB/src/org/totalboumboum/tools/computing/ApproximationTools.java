package org.totalboumboum.tools.computing;

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

import org.totalboumboum.game.round.RoundVariables;

/**
 * Methods used when performing approximate calculations,
 * especially on sprite positions.
 * 
 * @author Vincent Labatut
 */
public class ApproximationTools
{	
	/////////////////////////////////////////////////////////////////
	// COMPARISON		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Checks if the first value is strictly smaller than
	 * the second one, with some tolerance.
	 * 
	 * @param a
	 * 		First value.
	 * @param b
	 * 		Second value.
	 * @return
	 * 		(a<b) with some tolerance.
	 */
	public static boolean isRelativelySmallerThan(double a, double b)
	{	boolean result = false;
		double temp = b-a;
		result = temp>RoundVariables.toleranceCoefficient;
		return result;
	}

	/**
	 * Checks if the first value is strictly greater than
	 * the second one, with some tolerance.
	 * 
	 * @param a
	 * 		First value.
	 * @param b
	 * 		Second value.
	 * @return
	 * 		(a>b) with some tolerance.
	 */
	public static boolean isRelativelyGreaterThan(double a, double b)
	{	boolean result = false;
		double temp = a-b;
		result = temp>RoundVariables.toleranceCoefficient;
		return result;
	}

	/**
	 * Checks if the first value is equal to
	 * the second one, with some tolerance.
	 * 
	 * @param a
	 * 		First value.
	 * @param b
	 * 		Second value.
	 * @return
	 * 		(a==b) with some tolerance.
	 */
	public static boolean isRelativelyEqualTo(double a, double b)
	{	boolean result = false;
		double temp = Math.abs(b-a);
		result = temp<=RoundVariables.toleranceCoefficient;
		return result;
	}

	/**
	 * Checks if the first value is greater or equal to
	 * the second one, with some tolerance.
	 * 
	 * @param a
	 * 		First value.
	 * @param b
	 * 		Second value.
	 * @return
	 * 		(a>=b) with some tolerance.
	 */
	public static boolean isRelativelyGreaterOrEqualTo(double a, double b)
	{	boolean result;
		result = isRelativelyGreaterThan(a,b) || isRelativelyEqualTo(a,b);
		return result;
	}

	/**
	 * Checks if the first value is smaller or equal to
	 * the second one, with some tolerance.
	 * 
	 * @param a
	 * 		First value.
	 * @param b
	 * 		Second value.
	 * @return
	 * 		(a<=b) with some tolerance.
	 */
	public static boolean isRelativelySmallerOrEqualTo(double a, double b)
	{	boolean result;
		result = isRelativelySmallerThan(a,b) || isRelativelyEqualTo(a,b);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Rounds the specified value if it is relatively
	 * equal to the closest integer.
	 * 
	 * @param a
	 * 		Value to process.
	 * @return
	 * 		Either {@code a} or the closest integer.
	 */
	public static double round(double a)
	{	double result;
/*	
		result = a/(configuration.getZoomFactor()*configuration.getTolerance());
		result = Math.round(result);
		result = result*(configuration.getZoomFactor()*configuration.getTolerance());
*/	
		double temp = Math.round(a);
		if(isRelativelyEqualTo(a,temp))
			result = temp;
		else
			result = a;
		return result;
	}


	/////////////////////////////////////////////////////////////////
	// SIGN				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns an integer representing the sing
	 * of the specified value, with a tolerance.
	 * 
	 * @param a
	 * 		Value whose sign is required.
	 * @return
	 * 		-1 for negative, 0 for zero and +1 for a positive. 
	 */
	public static int relativeSignum(double a)
	{	int result;
		if(isRelativelyEqualTo(a,0))
			result = 0;
		else if(isRelativelyGreaterThan(a,0))
			result = +1;
		else //if(isRelativelySmallerThan(a,0,loop))
			result = -1;
		return result;
	}	
}
