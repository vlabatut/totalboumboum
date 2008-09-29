package fr.free.totalboumboum.engine.container.level;

import java.util.ArrayList;
import java.util.HashMap;

import fr.free.totalboumboum.engine.player.PlayerLocation;

public class Players
{
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
