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

/**
 * Methods implementing various mathematical functions.
 * 
 * @author Vincent Labatut
 */
public class FunctionTools
{
	/**
	 * Processes the value of a sigmoid function for x.
	 * The lambda parameter defines the slope (the higher, the more slopy)
	 * and the theta parameter defines the center of symmetry. 
	 * 
	 * @param lambda
	 * 		Slope.
	 * @param theta
	 * 		Center of symmetry.
	 * @param x
	 * 		Argument of the fonction.
	 * @return
	 * 		The result of the sigmoid function when applied to x.
	 */
	public static double sigmoid(double lambda, double theta, double x)
	{	double result;
		result = 1/(1+Math.exp(-lambda*(x-theta)));
		return result;
	}	
}
