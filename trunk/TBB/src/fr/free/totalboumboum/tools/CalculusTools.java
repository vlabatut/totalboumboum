package fr.free.totalboumboum.tools;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.data.configuration.GameConstants;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.round.Round;

public class CalculusTools
{	
	public static boolean isRelativelySmallerThan(double a, double b, Loop loop)
	{	boolean result = false;
		double temp = b-a;
		result = temp>loop.getZoomFactor()*GameConstants.TOLERANCE;
		return result;
	}

	public static boolean isRelativelyGreaterThan(double a, double b, Loop loop)
	{	boolean result = false;
		double temp = a-b;
		result = temp>loop.getZoomFactor()*GameConstants.TOLERANCE;
		return result;
	}
	
	public static boolean isRelativelyEqualTo(double a, double b, Loop loop)
	{	boolean result = false;
		double temp = Math.abs(b-a);
		result = temp<=loop.getZoomFactor()*GameConstants.TOLERANCE;
		return result;
	}
	
	public static boolean isRelativelyGreaterOrEqualThan(double a, double b, Loop loop)
	{	boolean result;
		result = isRelativelyGreaterThan(a,b,loop) || isRelativelyEqualTo(a,b,loop);
		return result;
	}

	public static boolean isRelativelySmallerOrEqualThan(double a, double b, Loop loop)
	{	boolean result;
		result = isRelativelySmallerThan(a,b,loop) || isRelativelyEqualTo(a,b,loop);
		return result;
	}

	public static double round(double a, Loop loop)
	{	double result;
/*	
		result = a/(configuration.getZoomFactor()*configuration.getTolerance());
		result = Math.round(result);
		result = result*(configuration.getZoomFactor()*configuration.getTolerance());
*/	
		double temp = Math.round(a);
		if(isRelativelyEqualTo(a,temp,loop))
			result = temp;
		else
			result = a;
		return result;
	}

}
