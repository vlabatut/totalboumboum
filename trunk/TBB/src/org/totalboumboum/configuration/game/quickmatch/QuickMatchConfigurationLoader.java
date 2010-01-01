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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.profile.ProfilesSelection;
import org.totalboumboum.configuration.profile.ProfilesSelectionLoader;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class QuickMatchConfigurationLoader
{	
	public static QuickMatchConfiguration loadQuickMatchConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameQuickMatchElement(root,result);
		return result;
	}

	private static void loadGameQuickMatchElement(Element root, QuickMatchConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// options
		Element optionsElement = root.getChild(XmlTools.OPTIONS);
		// use last players
		String att = optionsElement.getAttributeValue(XmlTools.USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(att);
		result.setUseLastPlayers(useLastPlayers);
		// use last levels
		att = optionsElement.getAttributeValue(XmlTools.USE_LAST_LEVELS);
		boolean useLastLevels = Boolean.parseBoolean(att);
		result.setUseLastLevels(useLastLevels);
		// use last settings
		att = optionsElement.getAttributeValue(XmlTools.USE_LAST_SETTINGS);
		boolean useLastSettings = Boolean.parseBoolean(att);
		result.setUseLastSettings(useLastSettings);
		
		// settings
		Element settingsElement = root.getChild(XmlTools.SETTINGS);
		loadSettingsElement(settingsElement,result);
		
		// players
		Element playersElement = root.getChild(XmlTools.PLAYERS);
		ProfilesSelection quickMatchProfiles = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(quickMatchProfiles);

		// levels
		Element levelsElement = root.getChild(XmlTools.LEVELS);
		LevelsSelection quickMatchLevels = LevelsSelectionLoader.loadLevelsSelection(levelsElement);
		result.setLevelsSelection(quickMatchLevels);
	}

	private static void loadSettingsElement(Element root, QuickMatchConfiguration result)
	{	// levels 
		Element levelsElement = root.getChild(XmlTools.LEVELS);
		loadLevelsElement(levelsElement,result);
		// players 
		Element playersElement = root.getChild(XmlTools.PLAYERS);
		loadPlayersElement(playersElement,result);
		// limits 
		Element limitsElement = root.getChild(XmlTools.LIMITS);
		loadLimitsElement(limitsElement,result);
		// points
		Element pointsElement = root.getChild(XmlTools.POINTS);
		loadPointsElement(pointsElement,result);
	}

	private static void loadLevelsElement(Element root, QuickMatchConfiguration result)
	{	// random order
		String att = root.getAttributeValue(XmlTools.RANDOM_ORDER);
		boolean levelsRandomOrder = Boolean.parseBoolean(att);
		result.setLevelsRandomOrder(levelsRandomOrder);
	}
	
	private static void loadPlayersElement(Element root, QuickMatchConfiguration result)
	{	// random location
		String att = root.getAttributeValue(XmlTools.RANDOM_LOCATION);
		boolean playersRandomLocation = Boolean.parseBoolean(att);
		result.setPlayersRandomLocation(playersRandomLocation);
	}
	
	private static void loadLimitsElement(Element root, QuickMatchConfiguration result)
	{	// points limit
		String att = root.getAttributeValue(XmlTools.POINTS);
		int limitPoints = Integer.parseInt(att);
		result.setLimitPoints(limitPoints);
		// rounds limit
		att = root.getAttributeValue(XmlTools.ROUNDS);
		int limitRounds = Integer.parseInt(att);
		result.setLimitRounds(limitRounds);
		// time limit
		att = root.getAttributeValue(XmlTools.TIME);
		int limitTime = Integer.parseInt(att);
		result.setLimitTime(limitTime);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadPointsElement(Element root, QuickMatchConfiguration result)
	{	// values
		ArrayList<Integer> points = new ArrayList<Integer>();
		for(int i=0;i<GameData.CONTROL_COUNT;i++)
			points.add(0);
		List<Element> list = root.getChildren(XmlTools.VALUE);
		for(Element e:list)
		{	String rankStr = e.getAttributeValue(XmlTools.RANK);
			int rank = Integer.parseInt(rankStr);
			String pointsStr = e.getAttributeValue(XmlTools.POINTS);
			int pts = Integer.parseInt(pointsStr);
			points.set(rank-1,pts);
		}
		result.setPoints(points);
		// share
		String att = root.getAttributeValue(XmlTools.SHARE);
		boolean pointsShare = Boolean.parseBoolean(att);
		result.setPointsShare(pointsShare);
		// points draw
		att = root.getAttributeValue(XmlTools.DRAW);
		QuickMatchDraw pointsDraw = QuickMatchDraw.valueOf(att.toUpperCase(Locale.ENGLISH));
		result.setPointsDraw(pointsDraw);		
	}
}
