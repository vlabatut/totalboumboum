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
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.game.tournament.league.LeagueTournament;

/**
 * Various methods designed to perform
 * combinatorics-related calculations.
 * 
 * @author Vincent Labatut
 */
@SuppressWarnings("unused")
public class CombinatoricsTools
{	
	/**
	 * Methods used to process combination maps
	 * for the {@link LeagueTournament}.
	 * 
	 * @param args
	 * 		None needed.
	 */
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
		
		int n = 10;
		int k = 7;
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
}
