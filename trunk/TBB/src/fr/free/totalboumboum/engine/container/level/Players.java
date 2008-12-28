package fr.free.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import fr.free.totalboumboum.engine.player.PlayerLocation;

public class Players implements Serializable
{	private static final long serialVersionUID = 1L;

	private HashMap<Integer, PlayerLocation[]> locations = new HashMap<Integer, PlayerLocation[]>();
	private ArrayList<String> initialItems = new ArrayList<String>();
		
	public void addLocation(Integer key, PlayerLocation[] value)
	{	locations.put(key, value);		
	}
	public HashMap<Integer, PlayerLocation[]> getLocations()
	{	return locations;	
	}

	public void addInitialItem(String value)
	{	initialItems.add(value);		
	}
	public ArrayList<String> getInitialItems()
	{	return initialItems;	
	}
}
