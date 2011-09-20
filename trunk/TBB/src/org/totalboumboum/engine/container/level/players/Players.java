package org.totalboumboum.engine.container.level.players;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.tools.GameData;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Players implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// LOCATIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer, PlayerLocation[]> locations = new HashMap<Integer, PlayerLocation[]>();

	public void addLocation(PlayerLocation[] loc)
	{	locations.put(loc.length,loc);		
	}

	public HashMap<Integer, PlayerLocation[]> getLocations()
	{	return locations;	
	}
	
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

	/////////////////////////////////////////////////////////////////
	// INITIAL ITEMS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Integer> initialItems = new HashMap<String,Integer>();
		
	public void addInitialItem(String item)
	{	Integer nbr = initialItems.get(item);
		if(nbr==null)
			nbr = 1;
		else
			nbr++;
		initialItems.put(item,nbr);		
	}

	public HashMap<String,Integer> getInitialItems()
	{	return initialItems;	
	}

	/////////////////////////////////////////////////////////////////
	// FORMAT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
