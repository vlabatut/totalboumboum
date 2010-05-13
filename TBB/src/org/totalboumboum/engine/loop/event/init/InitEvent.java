package org.totalboumboum.engine.loop.event.init;

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

import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.ReplayEvent;

public class InitEvent extends ReplayEvent
{	private static final long serialVersionUID = 1L;

	public InitEvent(LevelInfo levelInfo, List<Profile> profiles)
	{	super();
		this.levelInfo = levelInfo;
		this.profiles = profiles;
	}
		
	/////////////////////////////////////////////////////////////////
	// LEVEL INFO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelInfo levelInfo;
	
	public LevelInfo getLevelInfo()
	{	return levelInfo;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS PROFILES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Profile> profiles;
	
	public List<Profile> getProfiles()
	{	return profiles;
	}
	
	/////////////////////////////////////////////////////////////////
	// TO STRING			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "SpriteInitEvent: ";
		result = result + "level=" + levelInfo.getPackName() + "/" + levelInfo.getFolder() + " ";
		result = result + "players=";
		for(int i=0;i<profiles.size();i++)
		{	Profile p = profiles.get(i);
			result = result + i + ": " + p.getName() + "(" + p.getSpriteColor() + ") ";
		}
		return result;
	}
}
