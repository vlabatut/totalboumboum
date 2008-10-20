package fr.free.totalboumboum.configuration;

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

import fr.free.totalboumboum.configuration.controls.ControlsConfiguration;
import fr.free.totalboumboum.configuration.engine.EngineConfiguration;
import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.configuration.video.VideoConfiguration;

public class Configuration
{	
	/////////////////////////////////////////////////////////////////
	// ENGINE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static EngineConfiguration engineConfiguration;

	public static void setEngineConfiguration(EngineConfiguration engineConfiguration)
	{	Configuration.engineConfiguration = engineConfiguration;
	}
	public static EngineConfiguration getEngineConfiguration()
	{	return engineConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// VIDEO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static VideoConfiguration videoConfiguration;

	public static void setVideoConfiguration(VideoConfiguration videoConfiguration)
	{	Configuration.videoConfiguration = videoConfiguration;
	}
	public static VideoConfiguration getVideoConfiguration()
	{	return videoConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static ControlsConfiguration controlsConfiguration;

	public static void setControlsConfiguration(ControlsConfiguration controlsConfiguration)
	{	Configuration.controlsConfiguration = controlsConfiguration;
	}
	public static ControlsConfiguration getControlsConfiguration()
	{	return controlsConfiguration;
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static GameConfiguration gameConfiguration;

	public static void setGameConfiguration(GameConfiguration gameConfiguration)
	{	Configuration.gameConfiguration = gameConfiguration;
	}
	public static GameConfiguration getGameConfiguration()
	{	return gameConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static ArrayList<String> profiles = new ArrayList<String>();
	
	public static ArrayList<String> getProfiles()
	{	return profiles;	
	}
	
	public static void addProfile(String profile)
	{	if(!profiles.contains(profile))
			profiles.add(profile);
	}

}
