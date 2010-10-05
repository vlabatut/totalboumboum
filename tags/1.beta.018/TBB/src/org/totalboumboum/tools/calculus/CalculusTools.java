package org.totalboumboum.tools.calculus;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.game.round.RoundVariables;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
	public static List<List<Integer>> processDistributions(int players, int matches)
	{	return processDistributions(players,matches,players);
		
	}
	private static List<List<Integer>> processDistributions(int players, int matches, int previous)
	{	// init
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		if(players==0)
		{	List<Integer> list = new ArrayList<Integer>();
			for(int i=0;i<matches;i++)
				list.add(0);
			result.add(list);
		}
		else if(matches==1)
		{	if(players<=previous)
			{	List<Integer> list = new ArrayList<Integer>();
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
			{	List<List<Integer>> temp = processDistributions(players-i,matches-1,i);
				for(List<Integer> list: temp)
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
	public static List<List<Integer>> processPermutations(List<Integer> values)
	{	List<List<Integer>> result = new ArrayList<List<Integer>>();
		List<Integer> clone = new ArrayList<Integer>(values);
		result.add(clone);
	
		// init directions
		List<Integer> directions = new ArrayList<Integer>();
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
				clone = new ArrayList<Integer>(values);
				result.add(clone);
			}
		}
		while(gmiPos!=-1);
		
		return result;
	}

/*	public static void processChampionship(int totalPlayers, int matchPlayers)
	{	List<Set<Integer>> matches = new ArrayList<Set<Integer>>();
		Set<Set<Integer>> uniqueMatches = new TreeSet<Set<Integer>>(new Comparator<Set<Integer>>()
		{	@Override
			public int compare(Set<Integer> s1, Set<Integer> s2)
			{	Iterator<Integer> i1 = s1.iterator();
				Iterator<Integer> i2 = s2.iterator();
				int result = 0;
				boolean done;
				do
				{	int v1 = i1.next();
					int v2 = i2.next();
					result = v1-v2;
					if(result==0)
						done = !i1.hasNext();
					else
						done = true;
				}
				while(!done);
				return result;
			}			
		});
		int mod = (totalPlayers-1)%(matchPlayers-1);
		if(mod!=0)
			System.out.println("incompatibilité entre le nombre total de joueurs et la capacité du match");
		else
		{	int rep = (totalPlayers-1)/(matchPlayers-1);
			for(int i=0;i<totalPlayers;i++)
			{	int index = i;
				for(int j=0;j<rep;j++)
				{	Set<Integer> match = new TreeSet<Integer>();
					match.add(i);
					for(int k=0;k<(matchPlayers-1);k++)
					{	index = (index+1)%totalPlayers;
						match.add(index);
					}
					matches.add(match);
					uniqueMatches.add(match);
				}
			}
			// display matches
			System.out.println("total number of matches: "+matches.size());
			for(Set<Integer> match: matches)
			{	System.out.print("[ ");
				for(Integer player: match)
					System.out.print(player+" ");
				System.out.println("]");
			}
			System.out.println();
	
			// display unique matches
			System.out.println("total number of unique matches: "+uniqueMatches.size());
			for(Set<Integer> match: uniqueMatches)
			{	System.out.print("[ ");
				for(Integer player: match)
					System.out.print(player+" ");
				System.out.println("]");
			}
			System.out.println();
	
			// counts for player 0
			int counts[] = new int[totalPlayers];
			for(Set<Integer> match: matches)
			{	if(match.contains(0))
				{	for(Integer player: match)
						counts[player]++;
				}
			}
			System.out.println("Counts for player 0:");
			for(int i=0;i<counts.length;i++)
			{	System.out.println(i+"\t: "+counts[i]);
			}
			System.out.println();
	
			// unique counts for player 0
			Arrays.fill(counts,0);
			for(Set<Integer> match: uniqueMatches)
			{	if(match.contains(0))
				{	for(Integer player: match)
						counts[player]++;
				}
			}
			System.out.println("Unique counts for player 0:");
			for(int i=0;i<counts.length;i++)
			{	System.out.println(i+"\t: "+counts[i]);
			}
			System.out.println();
		}
	}*/
	public static void processChampionship(int totalPlayers, int matchPlayers)
	{	
		// set patterns
		int centralPlayer = 0;//(totalPlayers-1)/2;
		int matchOpponents = matchPlayers - 1;
		int totalOpponents = totalPlayers - 1;
		int combinationCount = totalOpponents/matchOpponents;
		List<Set<Integer>> patterns = new ArrayList<Set<Integer>>();
/*		Set<Integer> set;
		set = new TreeSet<Integer>(Arrays.asList(0,1,2,7,8));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,2,3,6,7));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,3,4,5,6));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,1,3,6,8));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,2,4,5,7));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,1,4,5,8));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,2,4,6,8));patterns.add(set);
		set = new TreeSet<Integer>(Arrays.asList(0,1,3,5,7));patterns.add(set);*/
		for(int i=0;i<combinationCount;i++)
		{	TreeSet<Integer> pattern = new TreeSet<Integer>();
			pattern.add(centralPlayer);
			int leftBase = (centralPlayer - i*matchOpponents + totalPlayers) % totalPlayers;
			int rightBase = (centralPlayer + i*matchOpponents) % totalPlayers;
			for(int j=1;j<=(matchOpponents/2);j++)
			{	int leftIndex = (leftBase-j+totalPlayers) % totalPlayers;
if(pattern.contains(leftIndex))
	System.out.println("pattern already contains "+leftIndex);
else
				pattern.add(leftIndex);
				int rightIndex = (rightBase+j) % totalPlayers;
if(pattern.contains(rightIndex))
	System.out.println("pattern already contains "+rightIndex);
else
				pattern.add(rightIndex);				
			}
			patterns.add(pattern);
		}
		
/*		
System.out.println("------------");
for(Set<Integer> set: patterns)
{	System.out.print("[ ");
	for(Integer integer: set)
		System.out.print(integer+" ");
	System.out.println("]");
}
*/		
		// process matches
		List<List<Set<Integer>>> matches = new ArrayList<List<Set<Integer>>>();
		for(Set<Integer> pattern: patterns)
		{	List<Set<Integer>> list = new ArrayList<Set<Integer>>();
			for(int i=0;i<totalPlayers;i++)
			{	Set<Integer> match = new TreeSet<Integer>();
				for(Integer p: pattern)
					match.add((p+i)%totalPlayers);
				list.add(match);
			}
			matches.add(list);
		}
		
System.out.println("------------");
for(int i=0;i<patterns.size();i++)
{	// pattern
	Set<Integer> pattern = patterns.get(i);
	System.out.print("[ ");
	for(Integer integer: pattern)
		System.out.print(integer+" ");
	System.out.println("]");
	// matches for this pattern
	List<Set<Integer>> list = matches.get(i);
	for(Set<Integer> match: list)
	{	System.out.print("\t[ ");
		for(Integer integer: match)
			System.out.print(integer+" ");
		System.out.println("]");
	}
	System.out.println();
}

System.out.println("------------");
centralPlayer = (totalPlayers-1)/2;
for(int i=0;i<totalPlayers;i++)
	System.out.print("\t"+i);
System.out.println();
System.out.println("--------------------------------------------------------------------------------");
for(List<Set<Integer>> list: matches)
{	int count[] = new int[totalPlayers];
	Arrays.fill(count,0);
	for(Set<Integer> match: list)
	{	if(match.contains(centralPlayer))
		{	for(Integer p: match)
				count[p]++;
		}
	}
	for(int i=0;i<count.length;i++)
		System.out.print("\t"+count[i]);
	System.out.println();
}
	
	
	}
	
	public static Set<Set<Integer>> processCombinationsRec(int n, int k)
	{	Set<Set<Integer>> result = new TreeSet<Set<Integer>>(new IntegerSetComparator());
		
		if(k==1)
		{	for(int i=0;i<n;i++)
			{	Set<Integer> set = new TreeSet<Integer>();
				set.add(new Integer(i));
				result.add(set);
			}			
		}
		else
		{	Set<Set<Integer>> combis = processCombinationsRec(n,k-1);
			for(int i=0;i<n;i++)
			{	for(Set<Integer> set: combis)
				{	if(!set.contains(i))
					{	Set<Integer> copy = new TreeSet<Integer>(set);
						copy.add(i);
						if(!result.contains(copy))
							result.add(copy);
					}
				}
			}
		}
		return result;
	}

	public static Set<Set<Integer>> processCombinationsIter(int n, int k, List<Set<Integer>> previous)
	{	System.out.println("\tprocess C("+n+","+k+")");
		Set<Set<Integer>> result = new TreeSet<Set<Integer>>(new IntegerSetComparator());
		
		if(previous!=null)
		{	result.addAll(previous);
			Set<Set<Integer>> temp = new TreeSet<Set<Integer>>(new IntegerSetComparator());
			for(int i=0;i<n;i++)
			{	for(Set<Integer> set: result)
				{	boolean test;
					int l = 0;
					do
					{	test = !set.contains(l);
						l++;
					}
					while(test && l<=i);
					if(test)
					{	Set<Integer> tempSet = new TreeSet<Integer>(set);
						tempSet.add(i);
						temp.add(tempSet);
					}
				}
			}
			result = temp;
		}
		else
		{	// init
			System.out.println("\t\tprocess C("+n+",1)");
			for(int i=0;i<n;i++)
			{	Set<Integer> set = new TreeSet<Integer>();
				set.add(new Integer(i));
				result.add(set);
			}
			System.out.println("\t\tdone");
			
			for(int m=2;m<=k;m++)
			{	System.out.println("\t\tprocess C("+n+","+m+")");
				Set<Set<Integer>> temp = new TreeSet<Set<Integer>>(new IntegerSetComparator());
				for(int i=0;i<n;i++)
				{	for(Set<Integer> set: result)
					{	boolean test;
						int l = 0;
						do
						{	test = !set.contains(l);
							l++;
						}
						while(test && l<=i);
						if(test)
						{	Set<Integer> tempSet = new TreeSet<Integer>(set);
							tempSet.add(i);
							temp.add(tempSet);
						}
					}
				}
				System.out.println("\t\tdone");
				result = temp;
			}
		}		
		
		System.out.println("\tdone");
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

	public static List<Integer> getWinners(float[] points)
	{	List<Integer> result = new ArrayList<Integer>();
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
