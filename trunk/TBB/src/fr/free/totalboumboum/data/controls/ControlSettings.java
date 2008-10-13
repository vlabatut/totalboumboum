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

public class ControlSettings
{
	private HashMap<Integer,String> onKeys;
	private HashMap<Integer,String> offKeys;
	private ArrayList<String> autofires;
	
	public ControlSettings()
	{	onKeys = new HashMap<Integer,String>();
		offKeys = new HashMap<Integer,String>();
		autofires = new ArrayList<String>(); 
	}
	
	public boolean containsOnKey(int key)
	{	return onKeys.containsKey(key);
	}
	public void addOnKey(int key, String event)
	{	onKeys.put(key,event);		
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
}
