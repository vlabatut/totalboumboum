package org.totalboumboum.configuration.game.quickmatch;

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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelsSelection
{
	public LevelsSelection copy()
	{	LevelsSelection result = new LevelsSelection();
	
		result.packNames.addAll(packNames);
		result.folderNames.addAll(folderNames);
		result.allowedPlayers.addAll(allowedPlayers);
		for(Set<Integer> ap: allowedPlayersList)
		{	Set<Integer> tmp = new TreeSet<Integer>(ap);
			result.allowedPlayersList.add(tmp);
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getLevelCount()
	{	return packNames.size();
	}

	public void addLevel(String packName, String folderName, Set<Integer> allowedPlayers)
	{	addLevel(getLevelCount(),packName,folderName,allowedPlayers);
	}
	
	public void addLevel(int index, String packName, String folderName, Set<Integer> allowedPlayers)
	{	// file stuff
		folderNames.add(index,folderName);
		packNames.add(index,packName);
		
		// allowed players
		allowedPlayersList.add(index,allowedPlayers);
		if(this.allowedPlayers.isEmpty())
			this.allowedPlayers.addAll(allowedPlayers);
		else
			this.allowedPlayers.retainAll(allowedPlayers);
	}
	
	public void removeLevel(int index)
	{	folderNames.remove(index);
		packNames.remove(index);
		allowedPlayersList.remove(index);
		updateAllowedPlayers();
	}

	/////////////////////////////////////////////////////////////////
	// ALLOWED PLAYERS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Set<Integer> allowedPlayers = new TreeSet<Integer>();
	private List<Set<Integer>> allowedPlayersList = new ArrayList<Set<Integer>>();
	
	public Set<Integer> getAllowedPlayerNumbers()
	{	return allowedPlayers;	
	}
	
	public Set<Integer> getAllowedPlayerNumbers(int index)
	{	return allowedPlayersList.get(index);	
	}
	
	private void updateAllowedPlayers()
	{	allowedPlayers.clear();
		Iterator<Set<Integer>> it = allowedPlayersList.iterator();
		if(it.hasNext())
		{	Set<Integer> ap = it.next();
			allowedPlayers.addAll(ap);
			while(it.hasNext())
			{	ap = it.next();
				allowedPlayers.retainAll(ap);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FOLDER NAMES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> folderNames = new ArrayList<String>();
	
	public String getFolderName(int index)
	{	return folderNames.get(index);	
	}	

	/////////////////////////////////////////////////////////////////
	// PACK NAMES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> packNames = new ArrayList<String>();
	
	public String getPackName(int index)
	{	return packNames.get(index);	
	}	
}
