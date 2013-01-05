package org.totalboumboum.configuration;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.ai.AisConfigurationLoader;
import org.totalboumboum.configuration.ai.AisConfigurationSaver;
import org.totalboumboum.configuration.connections.ConnectionsConfiguration;
import org.totalboumboum.configuration.connections.ConnectionsConfigurationLoader;
import org.totalboumboum.configuration.connections.ConnectionsConfigurationSaver;
import org.totalboumboum.configuration.controls.ControlsConfiguration;
import org.totalboumboum.configuration.controls.ControlsConfigurationLoader;
import org.totalboumboum.configuration.controls.ControlsConfigurationSaver;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.configuration.engine.EngineConfigurationLoader;
import org.totalboumboum.configuration.engine.EngineConfigurationSaver;
import org.totalboumboum.configuration.game.GameConfiguration;
import org.totalboumboum.configuration.game.GameConfigurationLoader;
import org.totalboumboum.configuration.game.GameConfigurationSaver;
import org.totalboumboum.configuration.profiles.ProfilesConfiguration;
import org.totalboumboum.configuration.profiles.ProfilesConfigurationLoader;
import org.totalboumboum.configuration.profiles.ProfilesConfigurationSaver;
import org.totalboumboum.configuration.statistics.StatisticsConfiguration;
import org.totalboumboum.configuration.statistics.StatisticsConfigurationLoader;
import org.totalboumboum.configuration.statistics.StatisticsConfigurationSaver;
import org.totalboumboum.configuration.video.VideoConfiguration;
import org.totalboumboum.configuration.video.VideoConfigurationLoader;
import org.totalboumboum.configuration.video.VideoConfigurationSaver;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
		aisConfiguration = AisConfigurationLoader.loadAisConfiguration();
		statisticsConfiguration = StatisticsConfigurationLoader.loadStatisticsConfiguration();
		connectionsConfiguration = ConnectionsConfigurationLoader.loadConnectionsConfiguration();
	}
	
	public static void saveConfiguration() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	ControlsConfigurationSaver.saveControlsConfiguration(controlsConfiguration);
		EngineConfigurationSaver.saveEngineConfiguration(engineConfiguration);
		GameConfigurationSaver.saveGameConfiguration(gameConfiguration);
		ProfilesConfigurationSaver.saveProfilesConfiguration(profilesConfiguration);
		VideoConfigurationSaver.saveVideoConfiguration(videoConfiguration);
		AisConfigurationSaver.saveAisConfiguration(aisConfiguration);
		StatisticsConfigurationSaver.saveStatisticsConfiguration(statisticsConfiguration);
		ConnectionsConfigurationSaver.saveConnectionsConfiguration(connectionsConfiguration);
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

	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static AisConfiguration aisConfiguration;

	public static void setAisConfiguration(AisConfiguration aisConfiguration)
	{	Configuration.aisConfiguration = aisConfiguration;
	}
	public static AisConfiguration getAisConfiguration()
	{	return aisConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static StatisticsConfiguration statisticsConfiguration;

	public static void setStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration)
	{	Configuration.statisticsConfiguration = statisticsConfiguration;
	}
	public static StatisticsConfiguration getStatisticsConfiguration()
	{	return statisticsConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// CONNECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static ConnectionsConfiguration connectionsConfiguration;

	public static void setConnectionsConfiguration(ConnectionsConfiguration connectionsConfiguration)
	{	Configuration.connectionsConfiguration = connectionsConfiguration;
	}
	public static ConnectionsConfiguration getConnectionsConfiguration()
	{	return connectionsConfiguration;
	}
}
