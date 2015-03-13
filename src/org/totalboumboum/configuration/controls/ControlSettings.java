package org.totalboumboum.configuration.controls;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Map associating keys to events, and used
 * to control sprites.
 * 
 * @author Vincent Labatut
 */
public class ControlSettings implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// KEYS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Adds a key, associated to the specified event and occupying
	 * the specified position.
	 * 
	 * @param key
	 * 		Key to add.
	 * @param event
	 * 		Associated event.
	 * @param on
	 * 		Key position (on or off).
	 */
	private void addKey(int key, String event, boolean on)
	{	// init
		Map<Integer,List<String>> keys;
		Map<String,Integer> events;
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
		{	List<String> evts = keys.get(key);
			evts.add(event);
		}
		else
		{	List<String> evts = new ArrayList<String>();
			evts.add(event);
			keys.put(key,evts);		
		}
		events.put(event,key);
	}
	
	/**
	 * Returns the key associated to the specific event,
	 * from the specified list (on or off).
	 * 
	 * @param event
	 * 		Event of interest.
	 * @param on
	 * 		{@code true} for on, {@code false} for off.
	 * @return
	 * 		Code of the associated key.
	 */
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
	
	/**
	 * Returns the list of events associated to the specified
	 * key in the specified position (on or off).
	 * 
	 * @param keyCode
	 * 		Code of the key of interest.
	 * @param on
	 * 		{@code true} for on, {@code false} for off.
	 * @return
	 * 		List of associated events.
	 */
	private List<String> getEventsFromKey(int keyCode, boolean on)
	{	List<String> result;
		if(on)
			result = onKeys.get(keyCode);
		else
			result = offKeys.get(keyCode);
		if(result==null)
			result = new ArrayList<String>();
		return result;
	}
	
	/**
	 * Removes the specified event from the specified list
	 * (on or off).
	 * 
	 * @param event
	 * 		Event of interest.
	 * @param on
	 * 		{@code true} for on, {@code false} for off.
	 */
	private void removeEvent(String event, boolean on)
	{	// init
		Map<Integer,List<String>> keys;
		Map<String,Integer> events;
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
			List<String> list = keys.get(oldKey);
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
	/** Map of keys currently in on position */
	private final Map<Integer,List<String>> onKeys = new HashMap<Integer,List<String>>();
	/** Map of events currently in on position */
	private final Map<String,Integer> onEvents = new HashMap<String,Integer>();
	
	/**
	 * Checks if the specified key is currently on.
	 * 
	 * @param key
	 * 		Key of interest.
	 * @return
	 * 		{@code true} iff it is currently on.
	 */
	public boolean containsOnKey(int key)
	{	return onKeys.containsKey(key);
	}
	
	/**
	 * Sets a key to the on position.
	 * 
	 * @param key
	 * 		Concerned key.
	 * @param event
	 * 		Associated event.
	 */
	public void addOnKey(int key, String event)
	{	addKey(key,event,true);
	}
	
	/**
	 * Indicates if the key associated to the specified
	 * event is currently on.
	 * 
	 * @param event
	 * 		Event of interest.
	 * @return
	 * 		{@code true} iff the key is on.
	 */
	public int getOnKeyFromEvent(String event)
	{	return getKeyFromEvent(event,true);
	}
	
	/**
	 * Returns the events associated to the specified on key.
	 * 
	 * @param keyCode
	 * 		Key of interest.
	 * @return
	 * 		List of events.
	 */
	public List<String> getEventsFromOnKey(int keyCode)
	{	return getEventsFromKey(keyCode,true);
	}
	
	/**
	 * Gets the map of on events.
	 * 
	 * @return
	 * 		Map of on events.
	 */
	public Map<String,Integer> getOnEvents()
	{	return onEvents;	
	}
	
	/////////////////////////////////////////////////////////////////
	// OFF				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of keys currently in off position */
	private final Map<Integer,List<String>> offKeys = new HashMap<Integer,List<String>>();
	/** Map of events currently in off position */
	private final Map<String,Integer> offEvents = new HashMap<String,Integer>();

	/**
	 * Checks if the specified key is currently off.
	 * 
	 * @param key
	 * 		Key of interest.
	 * @return
	 * 		{@code true} iff it is currently off.
	 */
	public boolean containsOffKey(int key)
	{	return offKeys.containsKey(key);
	}
	
	/**
	 * Sets a key to the off position.
	 * 
	 * @param key
	 * 		Concerned key.
	 * @param event
	 * 		Associated event.
	 */
	public void addOffKey(int key, String event)
	{	addKey(key,event,false);
	}
	
	/**
	 * Indicates if the key associated to the specified
	 * event is currently off.
	 * 
	 * @param event
	 * 		Event of interest.
	 * @return
	 * 		{@code true} iff the key is off.
	 */
	public int getOffKeyFromEvent(String event)
	{	return getKeyFromEvent(event,false);
	}
	
	/**
	 * Returns the events associated to the specified off key.
	 * 
	 * @param keyCode
	 * 		Key of interest.
	 * @return
	 * 		List of events.
	 */
	public List<String> getEventsFromOffKey(int keyCode)
	{	return getEventsFromKey(keyCode,false);
	}

	/**
	 * Gets the map of off events.
	 * 
	 * @return
	 * 		Map of off events.
	 */
	public Map<String,Integer> getOffEvents()
	{	return offEvents;	
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTOFIRE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of the events set to autofire */
	private final List<String> autofires = new ArrayList<String>();

	/**
	 * Checks if the specified event is set to autofire.
	 *  
	 * @param event
	 * 		Event of interest.
	 * @return
	 * 		{@code true} iff it is set to autofire.
	 */
	public boolean isAutofire(String event)
	{	return autofires.contains(event);		
	}
	
	/**
	 * Sets an event to autofire.
	 * 
	 * @param event
	 * 		Event of interest.
	 */
	public void addAutofire(String event)
	{	autofires.add(event);		
	}
	
	/**
	 * Unsets an event to autofire.
	 * 
	 * @param event
	 * 		Event of interest.
	 */
	public void removeAutofire(String event)
	{	autofires.remove(event);		
	}

	/////////////////////////////////////////////////////////////////
	// MISC				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Makes a copy of these settings.
	 * 
	 * @return
	 * 		Copy of these settings.
	 */
	public ControlSettings copy()
	{	ControlSettings result = new ControlSettings();
		// on keys
		{	Iterator<Entry<Integer,List<String>>> it = onKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,List<String>> entry = it.next();
				Integer key = entry.getKey();
				List<String> values = entry.getValue();
				Iterator<String> it2 = values.iterator();
				while(it2.hasNext())
				{	String temp = it2.next();
					result.addOnKey(key,temp);
				}
			}
		}
		// off keys
		{	Iterator<Entry<Integer,List<String>>> it = offKeys.entrySet().iterator();
			while(it.hasNext())
			{	Entry<Integer,List<String>> entry = it.next();
				Integer key = entry.getKey();
				List<String> values = entry.getValue();
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
