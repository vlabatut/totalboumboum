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

public class ProfilesSelection
{
	public ProfilesSelection copy()
	{	ProfilesSelection result = new ProfilesSelection();
		for(int i=0;i<fileNames.size();i++)
			result.addProfile(fileNames.get(i),colors.get(i),controls.get(i));
		return result;
	}
	
	public boolean equals(Object object)
	{	boolean result;
		if(object instanceof ProfilesSelection)
		{	ProfilesSelection profilesSelection = (ProfilesSelection)object;
			int i=0;
			result = true;
			while(i<fileNames.size() && result)
			{	result = colors.get(i)==profilesSelection.getColor(i);
				result = result && controls.get(i).equals(profilesSelection.getControlsIndex(i));
				result = result && fileNames.get(i).equals(profilesSelection.getFileName(i));
				i++;
			}				
		}
		else
			result = false;
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getProfileCount()
	{	return fileNames.size();
	}

	public void addProfile(String file, PredefinedColor color, int controlsIndex)
	{	fileNames.add(file);
		colors.add(color);
		controls.add(controlsIndex);
	}

	/////////////////////////////////////////////////////////////////
	// FILENAMES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> fileNames = new ArrayList<String>();
	
	public String getFileName(int index)
	{	return fileNames.get(index);	
	}	

	/////////////////////////////////////////////////////////////////
	// COLORS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<PredefinedColor> colors = new ArrayList<PredefinedColor>();
	
	public PredefinedColor getColor(int index)
	{	return colors.get(index);	
	}	

	/////////////////////////////////////////////////////////////////
	// CONTROLS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Integer> controls = new ArrayList<Integer>();
	
	public int getControlsIndex(int index)
	{	return controls.get(index);	
	}	
}
