package org.totalboumboum.configuration.controls;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class ControlSettings
{	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private void addKey(int key, String event, boolean on)
	{	// init
		HashMap<Integer,ArrayList<String>> keys;
		HashMap<String,Integer> events;
		if(on)
		{	keys = onKeys;
			events = onEvents;
		}
		else
		{	keys = offKeys;
			events = offEvents;
		}
		// remove previous value
		removeEvent(event,on);
		// put new value
		if(keys.containsKey(key))
		{	ArrayList<String> evts = keys.get(key);
			evts.add(event);
		}
		else
		{	ArrayList<String> evts = new ArrayList<String>();
			evts.add(event);
			keys.put(key,evts);		
		}
		events.put(event,key);
	}
	
	private int getKeyFromEvent(String event, boolean on)
	{	Integer result;
		if(on)
			result = onEvents.get(event);
		else
			result = offEvents.get(event);
		if(result==null)
			result = -1;
		return result;
	}
	
	private ArrayList<String> getEventsFromKey(int keyCode, boolean on)
	{	ArrayList<String> result;
		if(on)
			result = onKeys.get(keyCode);
		else
			result = offKeys.get(keyCode);
		if(result==null)
			result = new ArrayList<String>();
		return result;
	}
	
	private void removeEvent(String event, boolean on)
	{	// init
		HashMap<Integer,ArrayList<String>> keys;
		HashMap<String,Integer> events;
		if(on)
		{	keys = onKeys;
			events = onEvents;
		}
		else
		{	keys = offKeys;
			events = offEvents;
		}
		int oldKey = getKeyFromEvent(event, on);
		if(oldKey!=-1)
		{	// keys
			ArrayList<String> list = keys.get(oldKey);
			list.remove(event);
			if(list.size()==0)
				keys.remove(oldKey);
			// events
			events.remove(events);
		}				
	}

	/////////////////////////////////////////////////////////////////
	// ON				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Integer,ArrayList<String>> onKeys = new HashMap<Integer,ArrayList<String>>();
	private final HashMap<String,Integer> onEvents = new HashMap<String,Integer>();
	
	public boolean containsOnKey(int key)
	{	return onKeys.containsKey(key);
	}
	
	public void addOnKey(int key, String event)
	{	addKey(key,event,true);
	}
	
	public int getOnKeyFromEvent(String event)
	{	return getKeyFromEvent(event,true);
	}
	
	public ArrayList<String> getEventsFromOnKey(int keyCode)
	{	return getEventsFromKey(keyCode,true);
	}
	
	public HashMap<String,Integer> getOnEvents()
	{	return onEvents;	
	}
	
	/////////////////////////////////////////////////////////////////
	// OFF				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Integer,ArrayList<String>> offKeys = new HashMap<Integer,ArrayList<String>>();
	private final HashMap<String,Integer> offEvents = new HashMap<String,Integer>();

	public boolean containsOffKey(int key)
	{	return offKeys.containsKey(key);
	}
	
	public void addOffKey(int key, String event)
	{	addKey(key,event,false);
	}
	
	public int getOffKeyFromEvent(String event)
	{	return getKeyFromEvent(event,false);
	}
	
	public ArrayList<String> getEventsFromOffKey(int keyCode)
	{	return getEventsFromKey(keyCode,false);
	}

	public HashMap<String,Integer> getOffEvents()
	{	return offEvents;	
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTOFIRE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> autofires = new ArrayList<String>();

	public boolean isAutofire(String event)
	{	return autofires.contains(event);		
	}
	
	public void addAutofire(String event)
	{	autofires.add(event);		
	}
	
	public void removeAutofire(String event)
	{	autofires.remove(event);		
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	public ControlSettings copy()
	{	ControlSettings result = new ControlSettings();
		// on keys
		{	Iterator<Entry<Integer,ArrayList<String>>> it = onKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,ArrayList<String>> entry = it.next();
				Integer key = entry.getKey();
				ArrayList<String> values = entry.getValue();
				Iterator<String> it2 = values.iterator();
				while(it2.hasNext())
				{	String temp = it2.next();
					result.addOnKey(key,temp);
				}
			}
		}
		// off keys
		{	Iterator<Entry<Integer,ArrayList<String>>> it = offKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,ArrayList<String>> entry = it.next();
				Integer key = entry.getKey();
				ArrayList<String> values = entry.getValue();
				Iterator<String> it2 = values.iterator();
				while(it2.hasNext())
				{	String temp = it2.next();
					result.addOffKey(key,temp);
				}
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
