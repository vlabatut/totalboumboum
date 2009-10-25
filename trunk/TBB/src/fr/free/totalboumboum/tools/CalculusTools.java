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

import fr.free.totalboumboum.game.round.RoundVariables;

public class CalculusTools
{	
	/////////////////////////////////////////////////////////////////
	// APPROXIMATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static boolean isRelativelySmallerThan(double a, double b)
	{	boolean result = false;
		double temp = b-a;
		result = temp>RoundVariables.toleranceCoefficient;
		return result;
	}

	public static boolean isRelativelyGreaterThan(double a, double b)
	{	boolean result = false;
		double temp = a-b;
		result = temp>RoundVariables.toleranceCoefficient;
		return result;
	}
	
	public static boolean isRelativelyEqualTo(double a, double b)
	{	boolean result = false;
		double temp = Math.abs(b-a);
		result = temp<=RoundVariables.toleranceCoefficient;
		return result;
	}
	
	public static boolean isRelativelyGreaterOrEqualTo(double a, double b)
	{	boolean result;
		result = isRelativelyGreaterThan(a,b) || isRelativelyEqualTo(a,b);
		return result;
	}

	public static boolean isRelativelySmallerOrEqualTo(double a, double b)
	{	boolean result;
		result = isRelativelySmallerThan(a,b) || isRelativelyEqualTo(a,b);
		return result;
	}

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
	 * according to Johnson's algorithm. 
	 * cf http://en.wikipedia.org/wiki/Steinhaus-Johnson-Trotter_algorithm
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

	/////////////////////////////////////////////////////////////////
	// ORDERING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * process an array of ranks, each one corresponding to a player,
	 * according to the points in input.
	 * eg: {1,3,2} means the player 0 came first, player 1 came 
	 * third and player 2 came second
	 */
	public static int[] getRanks(float[] pts)
	{	// init
		int[] result = new int[pts.length];
		for(int i=0;i<result.length;i++)
			result[i] = 1;
		// process ranks
		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(pts[i]<pts[j])
					result[i] = result[i] + 1;
				else if(pts[i]>pts[j])
					result[j] = result[j] + 1;
			}
		}	
		//
		return result;
	}

	public static ArrayList<Integer> getWinners(float[] points)
	{	ArrayList<Integer> result = new ArrayList<Integer>();
		int[] ranks = CalculusTools.getRanks(points);
		for(int i=0;i<ranks.length;i++)
			if(ranks[i]==1 && points[i]>0)
				result.add(i);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// FUNCTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * process the value of a sigmoid function for x.
	 * The lambda parameter defines the slope (the higher, the sloping)
	 * and the theta parameter defines the center of symmetry. 
	 */
	public static double sigmoid(double lambda, double theta, double x)
	{	double result;
		result = 1/(1+Math.exp(-lambda*(x-theta)));
		return result;
	}
}
