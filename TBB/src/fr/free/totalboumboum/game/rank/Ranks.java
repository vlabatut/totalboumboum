package fr.free.totalboumboum.game.rank;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.profile.Profile;

public class Ranks implements Serializable
{	private static final long serialVersionUID = 1L;

	public Ranks copy()
	{	Ranks result = new Ranks();
		for(Entry<Integer,ArrayList<Profile>> entry: ranks.entrySet())
		{	ArrayList<Profile> list = entry.getValue();
			int rank = entry.getKey();
			for(Profile profile: list)
				result.addProfile(rank, profile);
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// HASHMAP			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Integer,ArrayList<Profile>> ranks = new HashMap<Integer, ArrayList<Profile>>();
	
	public HashMap<Integer,ArrayList<Profile>> getRanks()
	{	return ranks;
	}
		
	public void addProfile(int rank, Profile profile)
	{	ArrayList<Profile> list = ranks.get(rank);
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
	
	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Profile getProfileFromAbsoluteRank(int absoluteRank)
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
	
	public ArrayList<Profile> getProfilesFromRank(int rank)
	{	ArrayList<Profile> result = ranks.get(rank);
		return result;		
	}

	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getAbsoluteRankForProfile(Profile profile)
	{	int result = -1;
		Iterator<Entry<Integer,ArrayList<Profile>>> it = ranks.entrySet().iterator();
		int cpt = 1;
		while(it.hasNext() && result==-1)
		{	Entry<Integer,ArrayList<Profile>> entry = it.next();
			ArrayList<Profile> list = entry.getValue();
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
	
	public int getRankFromProfile(Profile profile)
	{	int result = -1;
		Iterator<Entry<Integer,ArrayList<Profile>>> it = ranks.entrySet().iterator();
		while(it.hasNext() && result==-1)
		{	Entry<Integer,ArrayList<Profile>> entry = it.next();
			int rank = entry.getKey();
			ArrayList<Profile> list = entry.getValue();
			if(list.contains(profile))
				result = rank;
		}
		return result;
	}
}
