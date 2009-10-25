package fr.free.totalboumboum.statistics.glicko2.jrs;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MathException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.special.Erf;

/**
 * Processes and combines the cumulative distribution functions for the specified player rating,
 * considering the rating and deviation are the mean and standard-deviation of a normal random variable.
 * The implemented calculus is described in Wikipedia: http://en.wikipedia.org/wiki/Normal_distribution<br/>
 * The combination takes the form F1(t)(1-F2(t)) since we want to process P(P1<=t)P(P2>t), that is the probability
 * for player 1 to have a rating smaller than player's 2 for one round.
 */
public class CumulativeDistributionFunction implements UnivariateRealFunction
{
	CumulativeDistributionFunction(PlayerRating playerRating1, PlayerRating playerRating2)
	{	double sqrt2 = Math.sqrt(2);
		// first player
		mean1 = playerRating1.getRating();
		stdev1 = playerRating1.getRatingDeviation();
		stdevSqrt21 = stdev1*sqrt2;
		// second player
		mean2 = playerRating2.getRating();
		stdev2 = playerRating2.getRatingDeviation();
		stdevSqrt22 = stdev2*sqrt2;
	}

	// first player
	private double mean1;
	private double stdev1;
	private double stdevSqrt21;
	// second player
	private double mean2;
	private double stdev2;
	private double stdevSqrt22;
	
	/////////////////////////////////////////////////////////////////
	// UNIVARIATE REAL FUNCTION		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public double value(double x) throws FunctionEvaluationException
	{	double result = 0;
			// player 1
			double erfArg1 = (x - mean1)/stdevSqrt21;
			double erfRes1 = erf(erfArg1);
			double f1 = 0.5*(1+erfRes1);
			// player 2
			double erfArg2 = (x - mean2)/stdevSqrt22;
			double erfRes2 = erf(erfArg2);
			double q2 = 1 - 0.5*(1+erfRes2);
			// result
			result = f1*q2;
System.out.println(f1+"*"+q2+"="+result);			
		return result;
	}
	
    // fractional error in math formula less than 1.2 * 10 ^ -7.
    // although subject to catastrophic cancellation when z in very close to 0
    // from Chebyshev fitting formula for erf(z) from Numerical Recipes, 6.2
	public static double erf(double z)
	{	double t = 1.0 / (1.0 + 0.5 * Math.abs(z));

        // use Horner's method
        double ans = 1 - t * Math.exp( -z*z   -   1.26551223 +
                                            t * ( 1.00002368 +
                                            t * ( 0.37409196 + 
                                            t * ( 0.09678418 + 
                                            t * (-0.18628806 + 
                                            t * ( 0.27886807 + 
                                            t * (-1.13520398 + 
                                            t * ( 1.48851587 + 
                                            t * (-0.82215223 + 
                                            t * ( 0.17087277))))))))));
        if (z >= 0) return  ans;
        else        return -ans;
    }

    // fractional error less than x.xx * 10 ^ -4.
    // Algorithm 26.2.17 in Abromowitz and Stegun, Handbook of Mathematical.
    public static double erf2(double z)
    {
        double t = 1.0 / (1.0 + 0.47047 * Math.abs(z));
        double poly = t * (0.3480242 + t * (-0.0958798 + t * (0.7478556)));
        double ans = 1.0 - poly * Math.exp(-z*z);
        if (z >= 0) return  ans;
        else        return -ans;
    }


}
