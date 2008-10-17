package fr.free.totalboumboum.data.controls;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ControlSettings
{
	private final HashMap<Integer,String> onKeys = new HashMap<Integer,String>();
	private final HashMap<Integer,String> offKeys = new HashMap<Integer,String>();
	private final ArrayList<String> autofires = new ArrayList<String>();
	
	public boolean containsOnKey(int key)
	{	return onKeys.containsKey(key);
	}
	public void addOnKey(int key, String event)
	{	onKeys.put(key,event);		
	}
	public int getKeyFromEvent(String event)
	{	int result = -1;
		Iterator<Entry<Integer,String>> it = onKeys.entrySet().iterator();
		while(it.hasNext() && result<0)
		{	Entry<Integer,String> entry = it.next();
			int key = entry.getKey();
			String value = entry.getValue();
			if(value.equalsIgnoreCase(event))
				result = key;
		}
		return result;
	}
	public String getEventFromKey(int keyCode)
	{	String result = onKeys.get(keyCode);
		if(result==null)
			result = offKeys.get(keyCode);
		return result;
	}
	
	public boolean containsOffKey(int key)
	{	return offKeys.containsKey(key);
	}
	public void addOffKey(int key, String event)
	{	offKeys.put(key,event);		
	}
	
	public boolean isAutofire(String event)
	{	return autofires.contains(event);		
	}
	public void addAutofire(String event)
	{	autofires.add(event);		
	}
	public void removeAutofire(String event)
	{	autofires.remove(event);		
	}
	
	public ControlSettings copy()
	{	ControlSettings result = new ControlSettings();
		// on keys
		{	Iterator<Entry<Integer,String>> it = onKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,String> entry = it.next();
				Integer key = entry.getKey();
				String value = entry.getValue();
				result.addOnKey(key,value);
			}
		}
		// off keys
		{	Iterator<Entry<Integer,String>> it = offKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,String> entry = it.next();
				Integer key = entry.getKey();
				String value = entry.getValue();
				result.addOffKey(key,value);
			}
		}
		// autofires
		{	Iterator<String> it = autofires.iterator();
			while(it.hasNext())
			{	String value = it.next();
				result.addAutofire(value);
			}
		}
		return result;
	}
}
