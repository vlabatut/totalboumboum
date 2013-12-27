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
 * This class gives access to all configuration-related objects.
 * 
 * @author Vincent Labatut
 */
public class Configuration
{	
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Loads all configuration-related objects from XML files.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws SAXException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws IOException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws IllegalArgumentException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws SecurityException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws IllegalAccessException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws NoSuchFieldException
	 * 		Problem while loading one of the configuration objects.
	 * @throws ClassNotFoundException
	 * 		Problem while loading one of the configuration objects.
	 */
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
	
	/**
	 * Records all configuration-related objects in XML files.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws SecurityException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws SAXException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws IOException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws IllegalAccessException
	 * 		Problem while accessing one of the XML configuration files.
	 * @throws NoSuchFieldException
	 * 		Problem while accessing one of the XML configuration files.
	 */
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
	/** Game engine-related settings */
	private static EngineConfiguration engineConfiguration;

	/**
	 * Changes the engine-related settings.
	 * 
	 * @param engineConfiguration
	 * 		New engine-related settings.
	 */
	public static void setEngineConfiguration(EngineConfiguration engineConfiguration)
	{	Configuration.engineConfiguration = engineConfiguration;
	}
	
	/**
	 * Returns the engine-related settings.
	 * 
	 * @return
	 * 		Current engine-related settings.
	 */
	public static EngineConfiguration getEngineConfiguration()
	{	return engineConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// VIDEO			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Video-related settings */
	private static VideoConfiguration videoConfiguration;

	/**
	 * Changes the video-related settings.
	 * 
	 * @param videoConfiguration
	 * 		New video-related settings.
	 */
	public static void setVideoConfiguration(VideoConfiguration videoConfiguration)
	{	Configuration.videoConfiguration = videoConfiguration;
	}
	
	/**
	 * Returns the vide-related settings.
	 * 
	 * @return
	 * 		Current vide-related settings.
	 */
	public static VideoConfiguration getVideoConfiguration()
	{	return videoConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Controls-related settings */
	private static ControlsConfiguration controlsConfiguration;

	/**
	 * Changes the controls-related settings.
	 * 
	 * @param controlsConfiguration
	 * 		New controls-related settings.
	 */
	public static void setControlsConfiguration(ControlsConfiguration controlsConfiguration)
	{	Configuration.controlsConfiguration = controlsConfiguration;
	}
	
	/**
	 * Returns the controls-related settings.
	 * 
	 * @return
	 * 		Current controls-related settings.
	 */
	public static ControlsConfiguration getControlsConfiguration()
	{	return controlsConfiguration;
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Game-related settings */
	private static GameConfiguration gameConfiguration;

	/**
	 * Changes the game-related settings.
	 * 
	 * @param gameConfiguration
	 * 		New game-related settings.
	 */
	public static void setGameConfiguration(GameConfiguration gameConfiguration)
	{	Configuration.gameConfiguration = gameConfiguration;
	}
	
	/**
	 * Returns the game-related settings.
	 * 
	 * @return
	 * 		Current game-related settings.
	 */
	public static GameConfiguration getGameConfiguration()
	{	return gameConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Profiles-related settings */
	private static ProfilesConfiguration profilesConfiguration;

	/**
	 * Changes the profiles-related settings.
	 * 
	 * @param profilesConfiguration
	 * 		New profiles-related settings.
	 */
	public static void setProfilesConfiguration(ProfilesConfiguration profilesConfiguration)
	{	Configuration.profilesConfiguration = profilesConfiguration;
	}
	
	/**
	 * Returns the profiles-related settings.
	 * 
	 * @return
	 * 		Current profiles-related settings.
	 */
	public static ProfilesConfiguration getProfilesConfiguration()
	{	return profilesConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// AI				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Agents-related settings */
	private static AisConfiguration aisConfiguration;

	/**
	 * Changes the agents-related settings.
	 * 
	 * @param aisConfiguration
	 * 		New agents-related settings.
	 */
	public static void setAisConfiguration(AisConfiguration aisConfiguration)
	{	Configuration.aisConfiguration = aisConfiguration;
	}
	
	/**
	 * Returns the agents-related settings.
	 * 
	 * @return
	 * 		Current agents-related settings.
	 */
	public static AisConfiguration getAisConfiguration()
	{	return aisConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Stats-related settings */
	private static StatisticsConfiguration statisticsConfiguration;

	/**
	 * Changes the stats-related settings.
	 * 
	 * @param statisticsConfiguration
	 * 		New stats-related settings.
	 */
	public static void setStatisticsConfiguration(StatisticsConfiguration statisticsConfiguration)
	{	Configuration.statisticsConfiguration = statisticsConfiguration;
	}
	
	/**
	 * Returns the stats-related settings.
	 * 
	 * @return
	 * 		Current stats-related settings.
	 */
	public static StatisticsConfiguration getStatisticsConfiguration()
	{	return statisticsConfiguration;
	}

	/////////////////////////////////////////////////////////////////
	// CONNECTIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Connection-related settings */
	private static ConnectionsConfiguration connectionsConfiguration;

	/**
	 * Changes the connection-related settings.
	 * 
	 * @param connectionsConfiguration
	 * 		New connection-related settings.
	 */
	public static void setConnectionsConfiguration(ConnectionsConfiguration connectionsConfiguration)
	{	Configuration.connectionsConfiguration = connectionsConfiguration;
	}
	
	/**
	 * Returns the connection-related settings.
	 * 
	 * @return
	 * 		Current connection-related settings.
	 */
	public static ConnectionsConfiguration getConnectionsConfiguration()
	{	return connectionsConfiguration;
	}
}
