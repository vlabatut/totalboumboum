package org.totalboumboum.configuration.game.quickmatch;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.configuration.profiles.ProfilesSelectionLoader;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class QuickMatchConfigurationLoader
{	
	public static QuickMatchConfiguration loadQuickMatchConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_GAME_QUICKMATCH+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_GAME_QUICKMATCH+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameQuickMatchElement(root,result);
		return result;
	}

	private static void loadGameQuickMatchElement(Element root, QuickMatchConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// options
		Element optionsElement = root.getChild(XmlNames.OPTIONS);
		// use last players
		String att = optionsElement.getAttributeValue(XmlNames.USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(att);
		result.setUseLastPlayers(useLastPlayers);
		// use last levels
		att = optionsElement.getAttributeValue(XmlNames.USE_LAST_LEVELS);
		boolean useLastLevels = Boolean.parseBoolean(att);
		result.setUseLastLevels(useLastLevels);
		// use last settings
		att = optionsElement.getAttributeValue(XmlNames.USE_LAST_SETTINGS);
		boolean useLastSettings = Boolean.parseBoolean(att);
		result.setUseLastSettings(useLastSettings);
		
		// settings
		Element settingsElement = root.getChild(XmlNames.SETTINGS);
		loadSettingsElement(settingsElement,result);
		
		// players
		Element playersElement = root.getChild(XmlNames.PLAYERS);
		ProfilesSelection quickMatchProfiles = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(quickMatchProfiles);

		// levels
		Element levelsElement = root.getChild(XmlNames.LEVELS);
		LevelsSelection quickMatchLevels = LevelsSelectionLoader.loadLevelsSelection(levelsElement);
		result.setLevelsSelection(quickMatchLevels);
	}

	private static void loadSettingsElement(Element root, QuickMatchConfiguration result)
	{	// levels 
		Element levelsElement = root.getChild(XmlNames.LEVELS);
		loadLevelsElement(levelsElement,result);
		
		// players 
		Element playersElement = root.getChild(XmlNames.PLAYERS);
		loadPlayersElement(playersElement,result);
		
		// limits 
		Element limitsElement = root.getChild(XmlNames.LIMITS);
		loadLimitsElement(limitsElement,result);
		
		// points
		Element pointsElement = root.getChild(XmlNames.POINTS);
		loadPointsElement(pointsElement,result);
		
		// sudden death
		Element suddenDeathElement = root.getChild(XmlNames.SUDDEN_DEATH);
		loadSuddenDeathElement(suddenDeathElement,result);
	}

	private static void loadLevelsElement(Element root, QuickMatchConfiguration result)
	{	// random order
		String att = root.getAttributeValue(XmlNames.RANDOM_ORDER);
		boolean levelsRandomOrder = Boolean.parseBoolean(att);
		result.setLevelsRandomOrder(levelsRandomOrder);
	}
	
	private static void loadPlayersElement(Element root, QuickMatchConfiguration result)
	{	// random location
		String att = root.getAttributeValue(XmlNames.RANDOM_LOCATION);
		boolean playersRandomLocation = Boolean.parseBoolean(att);
		result.setPlayersRandomLocation(playersRandomLocation);
	}
	
	private static void loadLimitsElement(Element root, QuickMatchConfiguration result)
	{	// points limit
		String att = root.getAttributeValue(XmlNames.POINTS);
		int limitPoints = Integer.parseInt(att);
		result.setLimitPoints(limitPoints);
		// rounds limit
		att = root.getAttributeValue(XmlNames.ROUNDS);
		int limitRounds = Integer.parseInt(att);
		result.setLimitRounds(limitRounds);
		// time limit
		att = root.getAttributeValue(XmlNames.TIME);
		int limitTime = Integer.parseInt(att);
		result.setLimitTime(limitTime);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadPointsElement(Element root, QuickMatchConfiguration result)
	{	// values
		List<Integer> points = new ArrayList<Integer>();
		for(int i=0;i<GameData.CONTROL_COUNT;i++)
			points.add(0);
		List<Element> list = root.getChildren(XmlNames.VALUE);
		for(Element e:list)
		{	String rankStr = e.getAttributeValue(XmlNames.RANK);
			int rank = Integer.parseInt(rankStr);
			String pointsStr = e.getAttributeValue(XmlNames.POINTS);
			int pts = Integer.parseInt(pointsStr);
			points.set(rank-1,pts);
		}
		result.setPoints(points);
		
		// share
		String att = root.getAttributeValue(XmlNames.SHARE);
		boolean pointsShare = Boolean.parseBoolean(att);
		result.setPointsShare(pointsShare);
		
		// points draw
		att = root.getAttributeValue(XmlNames.DRAW);
		QuickMatchDraw pointsDraw = QuickMatchDraw.valueOf(att.toUpperCase(Locale.ENGLISH));
		result.setPointsDraw(pointsDraw);		
	}

	private static void loadSuddenDeathElement(Element root, QuickMatchConfiguration result)
	{	// disabled
		String att = root.getAttributeValue(XmlNames.DISABLED);
		boolean suddenDeath = Boolean.parseBoolean(att);
		result.setSuddenDeathDisabled(suddenDeath);
	}
}
