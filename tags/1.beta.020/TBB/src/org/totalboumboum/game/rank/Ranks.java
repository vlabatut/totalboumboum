package org.totalboumboum.game.rank;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.Map.Entry;

import org.totalboumboum.game.profile.Profile;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Ranks implements Serializable
{	private static final long serialVersionUID = 1L;

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
	private final HashMap<Integer,List<Profile>> ranks = new HashMap<Integer, List<Profile>>();
	
	public HashMap<Integer,List<Profile>> getRanks()
	{	return ranks;
	}
		
	public void addProfile(int rank, Profile profile)
	{	List<Profile> list = ranks.get(rank);
		if(list==null)
		{	list = new ArrayList<Profile>();
			ranks.put(rank,list);
		}
		list.add(profile);
		
	}
	
	public boolean isEmpty()
	{	return ranks.isEmpty();		
	}
	
	public void remove(int rank)
	{	ranks.remove(rank);		
	}
	
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

	public List<Profile> getProfilesFromRank(int rank)
	{	List<Profile> result = ranks.get(rank);
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
