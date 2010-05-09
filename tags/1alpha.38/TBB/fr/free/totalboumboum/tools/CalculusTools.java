package fr.free.totalboumboum.tools;

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
