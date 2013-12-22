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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.tools.collections.IntegerCollectionComparator;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class CombinatoricsTools
{	
	public static void main(String args[])
	{	
//		int[][] combi = getCombinations(3, 5);
//		//int[][] combi = getCombinations(4, 5);
//		for(int i=0;i<combi.length;i++)
//		{	System.out.print((i+1)+".");
//			for(int j=0;j<combi[0].length;j++)
//				System.out.print(" "+combi[i][j]);
//			System.out.println();
//		}
		
		
		/*
		 *
		 * chaque match ne contient qu'un seul round
		 * tous les joueurs participent au match et jouent chacun certains rounds (possible ? >> je crois pas !)
		 * dans le xml, pas de distinction match/round
		 * 
		 */
		
		int n = 11;
		int k = 3;
		int[][] combinations = getCombinations(k, n);
		int[][] groups;
		long before = System.currentTimeMillis();
//		groups = checkGroups(combinations, n, k, 3);
//		groups = backTrackingAll(combinations, n, k);
		groups = forwardCheckingAll(combinations, n, k);
		
		long after = System.currentTimeMillis();
		long elapsed = (after - before) / 1000;
		String time = elapsed%60 + "s"; elapsed = elapsed / 60;
		time = elapsed%60 + "min " + time; elapsed = elapsed / 60;
		time = elapsed%24 + "h " + time; elapsed = elapsed / 24;
		time = elapsed + "d " + time;
		System.out.println("Duration: "+time);
		
		if(groups==null)
			System.out.println("No solution found for k="+k+" and n="+n);
		else
		{	System.out.println("Solution found for k="+k+" and n="+n);
			for(int i=0;i<groups.length;i++)
			{	print((i+1)+".");
				for(int j=0;j<groups[0].length;j++)
					System.out.print(" "+groups[i][j]);
				System.out.println();
			}
			System.out.println("Found a solution in "+groups.length+" / "+combinations.length);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// REPARTITION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes all the unique repartitions (i.e. without considering permutations)
	 * of a certain number of players in a certain number of matches.
	 * 
	 * @param players
	 * 		Number of players
	 * @param matches
	 * 		Number of matches.
	 * @return
	 * 		A list of integers, each value represents a number of players in
	 * 		the corresponding match.
	 */
	public static List<List<Integer>> processDistributions(int players, int matches)
	{	return processDistributions(players,matches,players);
		
	}
	
	/**
	 * Auxiliart function for {@link #processDistributions(int, int)}.
	 * 
	 * @param players
	 * 		Number of players
	 * @param matches
	 * 		Number of matches.
	 * @param previous
	 * 		Previous config.
	 * @return
	 * 		A list of integers, each representing a match.
	 */
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
	
	/////////////////////////////////////////////////////////////////
	// PERMUTATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Process all possible permutations for the specified integer list,
	 * according to Johnson's algorithm. 
	 * <br/>
	 * <b>Source:</b> <a href="http://en.wikipedia.org/wiki/Steinhaus-Johnson-Trotter_algorithm">Wikipedia</a>
	 * 
	 * @param values
	 * 		Values to be permuted.
	 * @return
	 * 		A list of permutations (i.e. integer lists).
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
	
	/////////////////////////////////////////////////////////////////
	// FACTORIALS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Processes and returns n!.
	 * 
	 * @param n
	 * 		Parameter of the factorial function.
	 * @return
	 * 		The value n!.
	 */
	public static long getFactorial(int n)
	{	long result = 0;
		
		if(n>0)
		{	result = 1;
			for(int i=2;i<=n;i++)
				result = result * i;
		}
	
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// COMBINATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Computes all combinations of k values in n possible values.
	 *  
	 * @param k
	 * 		Size of the combinations.
	 * @param n
	 * 		Upper value.
	 * @return
	 * 		Matrix whose rows are combinations of k integer amongst [0,n-1].
	 */
	public static int[][] getCombinations(int k, int n)
	{	// init
		int size = (int)(getFactorial(n)/getFactorial(k)/getFactorial(n-k));
		int result[][] = new int[size][k];

		// max values
		int maxVals[] = new int[k];
		for(int d=0;d<k;d++)
			maxVals[k-1-d] = n-1-d;
		print("max values: ");
		for(int d=0;d<k;d++)
			print(" "+maxVals[d]);
		println();
		
		// first combi
		int c = 0;
		for(int d=0;d<k;d++)
			result[c][d] = d;
		c++;

		// processes combinations
		do
		{	// copy
			for(int d=0;d<k;d++)
				result[c][d] = result[c-1][d];

			// increments
			boolean increment = true;
			int d = k-1;
			while(d<k)
			{	if(increment)
				{	if(result[c][d]==maxVals[d])
					{	result[c][d] = 0;
						d--;
					}
					else
					{	result[c][d] = result[c][d] + 1;
						increment = false;
						d++;
					}
				}
				else
				{	result[c][d] = result[c][d-1] + 1;
					d++;
				}
			}
			c++;
		}
		while(c<size);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// BACKTRACKING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Selects the minimum number of groups of size k amongst n players,
	 * so that each player belongs to exactly p groups. If such selection
	 * does not exist, the method returns {@code null}.
	 * 
	 * @param combinations
	 * 		All possible combinations of k amongst n.
	 * @param n
	 * 		Number of players.
	 * @param k
	 * 		Size of groups.
	 * @param p
	 * 		Number of groups a player must belong to.
	 * @return
	 * 		Selected groups (or {@code null}).
	 */
	private static int[][] backTracking(int[][] combinations, int n, int k, int p)
	{	int[][] result = null;
		println("looking for p="+p+" confrontations by player for n="+n+" and k="+k);
		
		if((p*n % k) == 0)
		{	// init result
			int m = p*n / k;
			int maxVal = p*(k-1)/(n-1); //nbr of times a player is in the same group than another
			
			// init structures
			Deque<Deque<int[]>> fringes = new LinkedList<Deque<int[]>>();
			Deque<int[]> rootFr = new LinkedList<int[]>();
//			rootFr.addAll(Arrays.asList(combinations));
			rootFr.add(combinations[0]);
			fringes.push(rootFr);
			int counts[][][] = new int[m][n][n];
			LinkedList<int[]> currentBranch = new LinkedList<int[]>();
			
			// depth-firt search (backtracking)
			int depth = 0;
			boolean found = false;
			while(!found && depth>=0)
			{	print("Current branch: ");
				for(int[] tmp: currentBranch)
					print(" "+Arrays.toString(tmp));
				println();
				
				Deque<int[]> currentFr = fringes.peek();
				if(currentFr.isEmpty())
				{	println(" Empty fringe >> Going up the research tree");
					if(!currentBranch.isEmpty())
					{	currentBranch.pop();
						fringes.pop();
					}
					depth--;
				}
				else
				{	int[] currentCombi = currentFr.poll();
					List<Integer> cc = new ArrayList<>();for(int i=0;i<currentCombi.length;i++) cc.add(currentCombi[i]);
					println(" Processing combination "+Arrays.toString(currentCombi));
					boolean consistant = true;
					for(int i=0;i<n;i++)
					{	for(int j=0;j<n;j++)
						if(j==i)
							counts[depth][i][j] = 0;
						else if(cc.contains(i) && cc.contains(j))
						{	int base = 0;
							if(depth>0)
								base = counts[depth-1][i][j];
							counts[depth][i][j] = base + 1;
							consistant = consistant && counts[depth][i][j]<=maxVal;
						}
						else
						{	if(depth>0)
								counts[depth][i][j] = counts[depth-1][i][j];
							else
								counts[depth][i][j] = 0;
						}
					}
					println("  Counts are now "+Arrays.toString(counts[depth]));
					if(consistant)
					{	println("   Which IS consistant >> going down the research tree");
						currentBranch.push(currentCombi);
						depth++;
						if(depth==m)
							found = true;
						else
						{	Deque<int[]> combis = new LinkedList<int[]>(Arrays.asList(combinations));
							combis.removeAll(currentBranch);
							fringes.push(combis);
						}
					}
					else
						println("   Which is not consistant >> trying next combination on the next level");
				}
			}
			
			// build result
			if(found)
			{	result = new int[m][k];
				for(int i=0;i<m;i++)
				{	int[] combi = currentBranch.get(m-1-i);
					for(int j=0;j<k;j++)
						result[i][j] = combi[j];
				}
			}
		}
		else
		{	println("p*n / k = "+p+"*"+n+" / "+k+" = "+( p*n / k)+" mod "+( p*n % k)+" >> done");
		}
		
		return result;
	}
	
	/**
	 * Look for a selection of groups such that each player belongs
	 * to the same number of groups.
	 * 
	 * @param combinations
	 * 		All possible combinations of k amongst n.
	 * @param n
	 * 		Number of players.
	 * @param k
	 * 		Size of groups.
	 * @return
	 * 		Selected groups (or {@code null} if no solution was found).
	 */
	private static int[][] backTrackingAll(int[][] combinations, int n, int k)
	{	int[][] result = null;
		
		int pMax = k*combinations.length/n;
		int p = 3;
		System.out.println("pmax="+pMax);
		while(result==null && p<=pMax)
		{	System.out.println("n="+n+" k="+k+" ckn="+combinations.length+" p="+p);
			result = backTracking(combinations, n, k, p);
			p++;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// FORWARD CHECKING		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Selects the minimum number of groups of size k amongst n players,
	 * so that each player belongs to exactly p groups. If such selection
	 * does not exist, the method returns {@code null}.
	 * 
	 * @param combinations
	 * 		All possible combinations of k amongst n.
	 * @param n
	 * 		Number of players.
	 * @param k
	 * 		Size of groups.
	 * @param p
	 * 		Number of groups a player must belong to.
	 * @return
	 * 		Selected groups (or {@code null}).
	 */
	private static int[][] forwardChecking(int[][] combinations, int n, int k, int p)
	{	int[][] result = null;
		println("looking for p="+p+" confrontations by player for n="+n+" and k="+k);
		
		if((p*n % k) != 0)
			println("p*n / k = "+p+"*"+n+" / "+k+" = "+(p*n/k)+" mod "+(p*n%k)+" >> done");
		else if(p*(k-1)%(n-1) != 0)
			println("p*(k-1) / (n-1) = "+p+"*"+(k-1)+" / "+(n-1)+" = "+(p*(k-1)/(n-1))+" mod "+(p*(k-1)%(n-1))+" >> done");
		
		else
		{	// init result
			int m = p*n / k;			// nbr of groups
			int maxVal = p*(k-1)/(n-1); // nbr of times a player is in the same group than another
			
			// init structures
			Deque<Deque<int[]>> fringes = new LinkedList<Deque<int[]>>();
			Deque<int[]> rootFr = new LinkedList<int[]>();
			rootFr.add(combinations[0]);
			fringes.push(rootFr);
			int counts[][][] = new int[m][n][n];
			LinkedList<int[]> currentBranch = new LinkedList<int[]>();
			
			// depth-firt search (backtracking)
			int depth = 0;
			boolean found = false;
			while(!found && depth>=0)
			{	print("Current branch: ");
				for(int[] tmp: currentBranch)
					print(" "+Arrays.toString(tmp));
				println();
				
				Deque<int[]> currentFr = fringes.peek();
				if(currentFr.isEmpty())
				{	println(" Empty fringe >> Going up the research tree");
					if(!currentBranch.isEmpty())
					{	currentBranch.pop();
						fringes.pop();
					}
					depth--;
				}
				else
				{	int[] currentCombi = currentFr.poll();
					List<Integer> cc = new ArrayList<>();for(int i=0;i<currentCombi.length;i++) cc.add(currentCombi[i]);
					println(" Processing combination "+Arrays.toString(currentCombi));
					boolean consistant = true;
					for(int i=0;i<n;i++)
					{	for(int j=0;j<n;j++)
						if(j==i)
							counts[depth][i][j] = 0;
						else if(cc.contains(i) && cc.contains(j))
						{	int base = 0;
							if(depth>0)
								base = counts[depth-1][i][j];
							counts[depth][i][j] = base + 1;
							consistant = consistant && counts[depth][i][j]<=maxVal;
						}
						else
						{	if(depth>0)
								counts[depth][i][j] = counts[depth-1][i][j];
							else
								counts[depth][i][j] = 0;
						}
					}
					println("  Counts are now "+Arrays.toString(counts[depth]));
					if(consistant)
					{	println("   Which IS consistant >> going down the research tree");
						currentBranch.push(currentCombi);
						depth++;
						if(depth==m)
							found = true;
						else
						{	Deque<int[]> combis = new LinkedList<int[]>();
							if(depth==1)
							{	combis.addAll(Arrays.asList(combinations));
								combis.removeAll(currentBranch);
							}
							else
							{	for(int fr[]: currentFr)
								{	boolean cst = true;
									int i=0;
									while(i<fr.length-1 && cst)
									{	int j=i+1;
										while(j<fr.length && cst)
										{	int row = fr[i];
											int col = fr[j];
											cst = counts[depth-1][row][col]<maxVal;
											j++;
										}
										i++;
									}
									if(cst)
										combis.add(fr);
								}
							}
							fringes.push(combis);
						}
					}
					else
						println("   Which is not consistant >> trying next combination on the next level");
				}
			}
			
			// build result
			if(found)
			{	result = new int[m][k];
				for(int i=0;i<m;i++)
				{	int[] combi = currentBranch.get(m-1-i);
					for(int j=0;j<k;j++)
						result[i][j] = combi[j];
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Look for a selection of groups such that each player belongs
	 * to the same number of groups.
	 * 
	 * @param combinations
	 * 		All possible combinations of k amongst n.
	 * @param n
	 * 		Number of players.
	 * @param k
	 * 		Size of groups.
	 * @return
	 * 		Selected groups (or {@code null} if no solution was found).
	 */
	private static int[][] forwardCheckingAll(int[][] combinations, int n, int k)
	{	int[][] result = null;
		
		int pMax = k*combinations.length/n;
		int p = 3;
		System.out.println("pmax="+pMax);
		while(result==null && p<=pMax)
		{	System.out.println("n="+n+" k="+k+" ckn="+combinations.length+" p="+p);
			result = forwardChecking(combinations, n, k, p);
			p++;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PRINT (DEBUG)	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether debug messages should be displayed */
	private static boolean verbose = false;
	
	/**
	 * Debug version of System.out.print.
	 * 
	 * @param str
	 * 		Message to print. 
	 */
	private static void print(String str)
	{	if(verbose)
			System.out.print(str);
	}

	/**
	 * Debug version of System.out.println.
	 * 
	 * @param str
	 * 		Message to print. 
	 */
	private static void println(String str)
	{	if(verbose)
			System.out.println(str);
	}

	/**
	 * Debug version of System.out.println.
	 */
	private static void println()
	{	if(verbose)
			System.out.println();
	}

	/////////////////////////////////////////////////////////////////
	// ?????????		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	{	Set<Set<Integer>> result = new TreeSet<Set<Integer>>(new IntegerCollectionComparator());
		
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
		Set<Set<Integer>> result = new TreeSet<Set<Integer>>(new IntegerCollectionComparator());
		
		if(previous!=null)
		{	result.addAll(previous);
			Set<Set<Integer>> temp = new TreeSet<Set<Integer>>(new IntegerCollectionComparator());
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
				Set<Set<Integer>> temp = new TreeSet<Set<Integer>>(new IntegerCollectionComparator());
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
}
