package fr.free.totalboumboum.tools;

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

import java.util.ArrayList;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.engine.loop.Loop;

public class CalculusTools
{	
	/////////////////////////////////////////////////////////////////
	// APPROXIMATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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

	/////////////////////////////////////////////////////////////////
	// COMBINATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	/**
	 * process all the unique distributions (i.e. without considering permutations)
	 * of a certain number of players in a certain number of matches.
	 */
	public static ArrayList<ArrayList<Integer>> processDistributions(int players, int matches)
	{	return processDistributions(players,matches,players);
		
	}
	private static ArrayList<ArrayList<Integer>> processDistributions(int players, int matches, int previous)
	{	// init
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		if(players==0)
		{	ArrayList<Integer> list = new ArrayList<Integer>();
			for(int i=0;i<matches;i++)
				list.add(0);
			result.add(list);
		}
		else if(matches==1)
		{	if(players<=previous)
			{	ArrayList<Integer> list = new ArrayList<Integer>();
				list.add(players);
				result.add(list);
			}
		}
		else
		{	int start = players;
			if(players>previous)
				start = previous;
	
			// combi
			for(int i=start;i>0;i--)
			{	ArrayList<ArrayList<Integer>> temp = processDistributions(players-i,matches-1,i);
				for(ArrayList<Integer> list: temp)
				{	list.add(0,i);
					result.add(list);
				}			
			}
		}
		
		return result;
	}
	
	/**
	 * process all possible permutations for the integer list in input,
	 * according to Johnson's algorithm. cf http://en.wikipedia.org/wiki/Steinhaus-Johnson-Trotter_algorithm
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<Integer>> processPermutations(ArrayList<Integer> values)
	{	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		result.add((ArrayList<Integer>)values.clone());
	
		// init directions
		ArrayList<Integer> directions = new ArrayList<Integer>();
		for (int i=1;i<=values.size();i++)
			directions.add(1);
		
		int gmiPos;
		do
		{	// get greatest mobile integer
			int gmi = 0;
			gmiPos = -1;
			for(int i=0;i<values.size();i++)
			{ 	int val = values.get(i);
				if(val>=gmi)
				{	int dir = directions.get(i);
					int index = i+dir;
					if(index>=0 && index<values.size())
					{	int val2 = values.get(index);
						if(val>val2) 
						{	gmiPos = i;
							gmi = val;
						}
					}
				}
			}
			
			if(gmiPos!=-1)
			{	//	interchange GMI with looked at value
				int gmiDir = directions.get(gmiPos);
				int otherPos = gmiPos + directions.get(gmiPos);	
				int other = values.get(otherPos);
				int otherDir = directions.get(otherPos);
				values.set(gmiPos,other);
				values.set(otherPos,gmi);
				directions.set(gmiPos,otherDir);
				directions.set(otherPos,gmiDir);				

				// switch directions
				for(int i=0;i<values.size();i++)
				{	int val = values.get(i);
					int dir = directions.get(i);
					if(val>gmi)
						directions.set(i,-dir);			
				}
				
				// add to result
				result.add((ArrayList<Integer>)values.clone());
			}
		}
		while(gmiPos!=-1);
		
		return result;
	}
	
}
