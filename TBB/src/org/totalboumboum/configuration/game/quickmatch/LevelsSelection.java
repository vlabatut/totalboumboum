package org.totalboumboum.configuration.game.quickmatch;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.List;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class LevelsSelection
{
	public LevelsSelection copy()
	{	LevelsSelection result = new LevelsSelection();
		for(int i=0;i<packNames.size();i++)
			result.addLevel(packNames.get(i),folderNames.get(i));
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getLevelCount()
	{	return packNames.size();
	}

	public void addLevel(String packName, String folderName)
	{	addLevel(getLevelCount(),packName,folderName);
	}
	
	public void addLevel(int index, String packName, String folderName)
	{	folderNames.add(index,folderName);
		packNames.add(index,packName);
	}
	
	public void removeLevel(int index)
	{	folderNames.remove(index);
		packNames.remove(index);		
	}

	/////////////////////////////////////////////////////////////////
	// FOLDER NAMES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> folderNames = new ArrayList<String>();
	
	public String getFolderName(int index)
	{	return folderNames.get(index);	
	}	

	/////////////////////////////////////////////////////////////////
	// PACK NAMES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> packNames = new ArrayList<String>();
	
	public String getPackName(int index)
	{	return packNames.get(index);	
	}	
}
