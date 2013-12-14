package org.totalboumboum.tools.computing;

import java.util.ArrayList;
import java.util.List;

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
 * Methods related to processing orders, ordering stuff, etc.
 * 
 * @author Vincent Labatut
 */
public class RankingTools
{
	/**
	 * Processes an array of ranks, each one corresponding to a player,
	 * according to the specified points.
	 * <br/>
	 * ex: {1,3,2} means the player 0 came first, player 1 came 
	 * third and player 2 came second.
	 * 		
	 * @param pts
	 * 		Points scored by the players. 
	 * @return 
	 * 		Ranks of the players.
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

	/**
	 * Return the list of winners for the specifed points.
	 * 
	 * @param points
	 * 		Points scored by the players.
	 * @return
	 * 		Players with highest scores.
	 */
	public static List<Integer> getWinners(float[] points)
	{	List<Integer> result = new ArrayList<Integer>();
		int[] ranks = getRanks(points);
		for(int i=0;i<ranks.length;i++)
			if(ranks[i]==1 && points[i]>0)
				result.add(i);
		return result;
	}
}
