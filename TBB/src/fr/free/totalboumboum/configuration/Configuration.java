package fr.free.totalboumboum.configuration;

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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.controls.ControlsConfiguration;
import fr.free.totalboumboum.configuration.controls.ControlsConfigurationLoader;
import fr.free.totalboumboum.configuration.controls.ControlsConfigurationSaver;
import fr.free.totalboumboum.configuration.engine.EngineConfiguration;
import fr.free.totalboumboum.configuration.engine.EngineConfigurationLoader;
import fr.free.totalboumboum.configuration.engine.EngineConfigurationSaver;
import fr.free.totalboumboum.configuration.game.GameConfiguration;
import fr.free.totalboumboum.configuration.game.GameConfigurationLoader;
import fr.free.totalboumboum.configuration.game.GameConfigurationSaver;
import fr.free.totalboumboum.configuration.profile.ProfilesConfiguration;
import fr.free.totalboumboum.configuration.profile.ProfilesConfigurationLoader;
import fr.free.totalboumboum.configuration.profile.ProfilesConfigurationSaver;
import fr.free.totalboumboum.configuration.video.VideoConfiguration;
import fr.free.totalboumboum.configuration.video.VideoConfigurationLoader;
import fr.free.totalboumboum.configuration.video.VideoConfigurationSaver;

public class Configuration
{	

	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	controlsConfiguration = ControlsConfigurationLoader.loadControlsConfiguration();
		engineConfiguration = EngineConfigurationLoader.loadEngineConfiguration();
		gameConfiguration = GameConfigurationLoader.loadGameConfiguration();
		profilesConfiguration = ProfilesConfigurationLoader.loadProfilesConfiguration();
		videoConfiguration = VideoConfigurationLoader.loadVideoConfiguration();
	}
	
	public static void saveConfiguration() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	ControlsConfigurationSaver.saveControlsConfiguration(controlsConfiguration);
		EngineConfigurationSaver.saveEngineConfiguration(engineConfiguration);
		GameConfigurationSaver.saveGameConfiguration(gameConfiguration);
		ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfiguration);
		VideoConfigurationSaver.saveVideoConfiguration(videoConfiguration);
	}
	
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
	private static ProfilesConfiguration profilesConfiguration;

	public static void setProfilesConfiguration(ProfilesConfiguration profilesConfiguration)
	{	Configuration.profilesConfiguration = profilesConfiguration;
	}
	public static ProfilesConfiguration getProfilesConfiguration()
	{	return profilesConfiguration;
	}

}
