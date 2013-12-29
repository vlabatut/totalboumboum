package org.totalboumboum.game.rank;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.game.profile.Profile;

/**
 * This class is used to process and represents player
 * rankings for a given confrontation (be it a round,
 * match or tournament).
 * 
 * @author Vincent Labatut
 */
public class Ranks implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Makes a copy of this object.
	 * 
	 * @return
	 * 		A copy of this object.
	 */
	public Ranks copy()
	{	Ranks result = new Ranks();
		for(Entry<Integer,List<Profile>> entry: ranks.entrySet())
		{	List<Profile> list = entry.getValue();
			int rank = entry.getKey();
			for(Profile profile: list)
				result.addProfile(rank, profile);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// HASHMAP			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map containing all ranks and ranked players */
	private final HashMap<Integer,List<Profile>> ranks = new HashMap<Integer, List<Profile>>();
	
	/**
	 * Returns the map of all ranks.
	 * 
	 * @return
	 * 		Map of all ranks.
	 */
	public Map<Integer,List<Profile>> getRanks()
	{	return ranks;
	}
	
	/**
	 * Adds a player to the specified rank.
	 * 
	 * @param rank
	 * 		Rank of interest.
	 * @param profile
	 * 		Player to be added to this rank.
	 */
	public void addProfile(int rank, Profile profile)
	{	List<Profile> list = ranks.get(rank);
		if(list==null)
		{	list = new ArrayList<Profile>();
			ranks.put(rank,list);
		}
		list.add(profile);
		
	}
	
	/**
	 * Checks if this object contains at least one rank.
	 * 
	 * @return
	 * 		{@code true} iff this object contains one rank or more.
	 */
	public boolean isEmpty()
	{	return ranks.isEmpty();		
	}
	
	/**
	 * Removes a rank (and all the concerned players).
	 * 
	 * @param rank
	 * 		Rank to be removed.
	 */
	public void remove(int rank)
	{	ranks.remove(rank);		
	}
	
	/**
	 * Returns the number of players ranked 
	 * in this object.
	 *  
	 * @return
	 * 		Number of ranked players.
	 */
	public int size()
	{	int result = 0;
		for(Entry<Integer,List<Profile>> entry: ranks.entrySet())
			result = result + entry.getValue().size();
		return result;		
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*	public Profile getProfileFromAbsoluteRank(int absoluteRank)
	{	Profile result = null;
		
		Iterator<Entry<Integer,ArrayList<Profile>>> it = ranks.entrySet().iterator();
		int cpt = 0;
		while(cpt<absoluteRank && it.hasNext())
		{	Entry<Integer,ArrayList<Profile>> entry = it.next();
			ArrayList<Profile> list = entry.getValue();
			Iterator<Profile> it2 = list.iterator();
			while(cpt<absoluteRank && it2.hasNext())
			{	cpt ++;
				result = it2.next();
			}
		}
		if(cpt<absoluteRank)
			result = null;
		return result;
	}
*/
	/**
	 * Returns the ordered list of players.
	 * 
	 * @return
	 * 		List of players ordered depending on their rank.
	 */
	public List<Profile> getAbsoluteOrderList()
	{	// init
		List<Profile> result = new ArrayList<Profile>();
		List<Integer> keys = new ArrayList<Integer>(ranks.keySet());
		Collections.sort(keys);
		
		if(keys.size()>0)
		{	// dealing with unranked profiles first
			List<Profile> unranked = new ArrayList<Profile>();
			if(keys.get(0)==0)
			{	List<Profile> profiles = ranks.get(0);
				for(Profile profile: profiles)
					unranked.add(profile);
				keys.remove(0);
			}
			
			// populate list
			for(int rank: keys)
			{	List<Profile> profiles = ranks.get(rank);
				for(Profile profile: profiles)
					result.add(profile);
			}
			
			result.addAll(unranked);
		}
		
		return result;
	}

	/**
	 * Returns the list of players corresponding
	 * to the specified rank.
	 * 
	 * @param rank
	 * 		Rank of interest.
	 * @return
	 * 		Players having obtained this rank.
	 */
	public List<Profile> getProfilesFromRank(int rank)
	{	List<Profile> result = ranks.get(rank);
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the rank of the specified profile, without considering
	 * ex-aequos. In other terms, if two players are both ranked
	 * 3rd, then this method returns 3 for one of them and 4 for the other.
	 * 
	 * @param profile
	 * 		Player of interest.
	 * @return
	 * 		Absolute rank of the player.
	 */
	public int getAbsoluteRankForProfile(Profile profile)
	{	int result = -1;
		Iterator<Entry<Integer,List<Profile>>> it = ranks.entrySet().iterator();
		int cpt = 1;
		while(it.hasNext() && result==-1)
		{	Entry<Integer,List<Profile>> entry = it.next();
			List<Profile> list = entry.getValue();
			Iterator<Profile> it2 = list.iterator();
			while(it2.hasNext() && result==-1)
			{	Profile p = it2.next();
				if(profile.equals(p))
					result = cpt;
				else
					cpt ++;
			}
		}
		return result;
	}
	
	/**
	 * Returns the rank of the specified profile, considering
	 * ex-aequos. In other terms, if two players are both ranked
	 * 3rd, then this method returns 3 for both of them.
	 * 
	 * @param profile
	 * 		Player of interest.
	 * @return
	 * 		Rank of the player.
	 */
	public int getRankForProfile(Profile profile)
	{	int result = -1;
		Iterator<Entry<Integer,List<Profile>>> it = ranks.entrySet().iterator();
		while(it.hasNext() && result==-1)
		{	Entry<Integer,List<Profile>> entry = it.next();
			int rank = entry.getKey();
			List<Profile> list = entry.getValue();
			if(list.contains(profile))
				result = rank;
		}
		return result;
	}
}
