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

import fr.free.totalboumboum.configuration.GameConstants;

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
	
	public void removeProfile(String file)
	{	profiles.remove(file);		
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

	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int lastProfile = 0;

	public int getLastProfile()
	{	return lastProfile;
	}

	public void setLastProfile(int lastProfile)
	{	this.lastProfile = lastProfile;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getNextFreeControls(ArrayList<Profile> profiles, int start)
	{	/// init
		Iterator<Profile> it = profiles.iterator();
		ArrayList<Integer> occupied = new ArrayList<Integer>();
		while(it.hasNext())
		{	Profile profile = it.next();
			int index = profile.getControlSettingsIndex();
			if(index>0)
				occupied.add(index);
		}
		// next free index
		boolean found = false;
		int result = 0;
		int test = 1;
		while(!found && test<=GameConstants.CONTROL_COUNT)
		{	int temp = (start+test)%(GameConstants.CONTROL_COUNT+1);
			if(occupied.contains(temp))
				test++;
			else
			{	result = temp;
				found = true;
			}
		}
		if(!found)
			result = start;
		return result;
	}

	public PredefinedColor getNextFreeColor(ArrayList<Profile> profiles, Profile profile, PredefinedColor color)
	{	PredefinedColor result = null;
		// used colors
		ArrayList<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteSelectedColor();
			usedColors.add(clr);
		}
		// preferred colors
		ArrayList<PredefinedColor> preferredColors = new ArrayList<PredefinedColor>();
		for(PredefinedColor c: profile.getSpriteColors())
		{	if(c!=null && (c==color || !usedColors.contains(c)))
					preferredColors.add(c);
		}
		for(PredefinedColor c: PredefinedColor.values())
		{	if(c==color || (!usedColors.contains(c) && !preferredColors.contains(c)))
				preferredColors.add(c);
		}
		// select a color
		int currentColorIndex = preferredColors.indexOf(color);
		int index = (currentColorIndex+1) % preferredColors.size();
		if(index<preferredColors.size())
			result = preferredColors.get(index);
		return result;
	}

	public boolean isFreeColor(ArrayList<Profile> profiles, PredefinedColor color)
	{	// used colors
		ArrayList<PredefinedColor> usedColors = new ArrayList<PredefinedColor>();
		for(Profile p: profiles)
		{	PredefinedColor clr = p.getSpriteSelectedColor();
			usedColors.add(clr);
		}
		boolean result = !usedColors.contains(color);
		return result;
	}
}
