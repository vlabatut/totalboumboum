package fr.free.totalboumboum.configuration.profile;

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

public class ProfilesConfiguration
{
	public ProfilesConfiguration copy()
	{	ProfilesConfiguration result = new ProfilesConfiguration();
		// profiles
		{	Iterator<Entry<String,String>> it = profiles.entrySet().iterator();
			while(it.hasNext())
			{	Entry<String,String> entry = it.next();
				String value = entry.getValue();
				String key = entry.getKey();
				result.addProfile(key,value);			
			}
		}
		// selected
		{	Iterator<String> it = selected.iterator();
			while(it.hasNext())
			{	String temp = it.next();
				result.addSelected(temp);			
			}
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,String> profiles = new HashMap<String,String>();
	
	public HashMap<String,String> getProfiles()
	{	return profiles;	
	}
	
	public void addProfile(String file, String name)
	{	profiles.put(file,name);
	}

	/////////////////////////////////////////////////////////////////
	// SELECTED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> selected = new ArrayList<String>();
	
	public ArrayList<String> getSelected()
	{	return selected;	
	}
	
	public void addSelected(String file)
	{	selected.add(file);		
	}
}
