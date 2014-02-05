package org.totalboumboum.engine.container.level.players;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.tools.GameData;

/**
 * Represents the players-related description of a level.
 * 
 * @author Vincent Labatut
 */
public class Players implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// LOCATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of player locations allowed for this level */ 
	private Map<Integer, PlayerLocation[]> locations = new HashMap<Integer, PlayerLocation[]>();

	/**
	 * Adds a new player location to the allowed ones.
	 * 
	 * @param loc
	 * 		New location.
	 */
	public void addLocation(PlayerLocation[] loc)
	{	locations.put(loc.length,loc);		
	}

	/**
	 * Returns the map of all allowed locations for
	 * this level.
	 * 
	 * @return
	 * 		Map of all locations.
	 */
	public Map<Integer, PlayerLocation[]> getLocations()
	{	return locations;	
	}
	
	/**
	 * Checks if the specified tile is used as a location.
	 * 
	 * @param row
	 * 		Tile row.
	 * @param col
	 * 		Tile column.
	 * @return
	 * 		{@code true} iff the tile is used as a player starting location.
	 */
	public boolean isOccupied(int row, int col)
	{	boolean result = false;
		Iterator<PlayerLocation[]> it = locations.values().iterator();
		while(!result && it.hasNext())
		{	PlayerLocation[] temp = it.next();
			int i = 0;
			while(!result && i<temp.length)
			{	PlayerLocation pl = temp[i];
				result = pl.getRow()==row && pl.getCol()==col;
				i++;
			}
		}
		return result;
	}
	
	/**
	 * Removes all locations corresponding to the specified
	 * numbers of players. This allows forbidding certain
	 * player numbers from a tournament/match/round. This overrides
	 * the original locations defined in the level itself. 
	 * 
	 * @param numbers
	 * 		Forbidden numbers of players.
	 */
	public void forbidPlayers(Set<Integer> numbers)
	{	for(int n: numbers)
			locations.remove(n);
	}
	
	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of items given to players at startup */
	private Map<String,Integer> initialItems = new HashMap<String,Integer>();
		
	/**
	 * Adds a new item to the current map 
	 * of startup item.
	 * 
	 * @param item
	 * 		New item.
	 */
	public void addInitialItem(String item)
	{	Integer nbr = initialItems.get(item);
		if(nbr==null)
			nbr = 1;
		else
			nbr++;
		initialItems.put(item,nbr);		
	}

	/**
	 * Returns the map of allowed items,
	 * with the associated occurrence numbers.
	 * 
	 * @return
	 * 		Map of initial items.
	 */
	public Map<String,Integer> getInitialItems()
	{	return initialItems;	
	}

	/////////////////////////////////////////////////////////////////
	// FORMAT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Formats the numbers of allowed number as a String.
	 * 
	 * @param playerNumbers
	 * 		Set of allowed player numbers.
	 * @return
	 * 		String representation of this set.
	 */
	public static String formatAllowedPlayerNumbers(Set<Integer> playerNumbers)
	{	StringBuffer temp = new StringBuffer();
		if(playerNumbers.size()>0)
		{	int value = playerNumbers.iterator().next();
			temp.append(value);
			boolean serie = true;
			int first = value;
			for(int index=value+1;index<=GameData.MAX_PROFILES_COUNT+1;index++)
			{	if(playerNumbers.contains(index))
				{	if(!serie)
					{	serie = true;
						first = index;
						temp.append(";"+index);
					}					
				}
				else
				{	if(serie)
					{	serie = false;
						if(index-1!=first)
							temp.append("-"+(index-1));
					}
				}				
			}
		}
		else
			temp.append(0);
		return temp.toString();
	}
}
