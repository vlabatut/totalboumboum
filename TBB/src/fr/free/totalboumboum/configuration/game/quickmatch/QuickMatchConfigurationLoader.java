package fr.free.totalboumboum.configuration.game.quickmatch;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickMatchConfigurationLoader
{	
	public static QuickMatchConfiguration loadQuickMatchConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	QuickMatchConfiguration result = new QuickMatchConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKMATCH+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameElement(root,result);
		return result;
	}

	private static void loadGameElement(Element root, QuickMatchConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// options
		Element optionsElement = root.getChild(XmlTools.ELT_OPTIONS);
		// use last players
		String att = optionsElement.getAttributeValue(XmlTools.ATT_USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(att);
		result.setUseLastPlayers(useLastPlayers);
		// use last levels
		att = optionsElement.getAttributeValue(XmlTools.ATT_USE_LAST_LEVELS);
		boolean useLastLevels = Boolean.parseBoolean(att);
		result.setUseLastLevels(useLastLevels);
		
		// settings
		Element settingsElement = root.getChild(XmlTools.ELT_SETTINGS);
		loadSettingsElement(settingsElement,result);
		
		// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickMatchProfiles = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(quickMatchProfiles);

		// levels
		Element levelsElement = root.getChild(XmlTools.ELT_LEVELS);
		LevelsSelection quickMatchLevels = LevelsSelectionLoader.loadLevelsSelection(levelsElement);
		result.setLevelsSelection(quickMatchLevels);
	}

	private static void loadSettingsElement(Element root, QuickMatchConfiguration result)
	{	// levels 
		Element levelsElement = root.getChild(XmlTools.ELT_LEVELS);
		loadLevelsElement(levelsElement,result);
		// players 
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		loadPlayersElement(playersElement,result);
		// limits 
		Element limitsElement = root.getChild(XmlTools.ELT_LIMITS);
		loadLimitsElement(limitsElement,result);
		// points
		Element pointsElement = root.getChild(XmlTools.ELT_POINTS);
		loadPointsElement(pointsElement,result);
	}

	private static void loadLevelsElement(Element root, QuickMatchConfiguration result)
	{	// random order
		String att = root.getAttributeValue(XmlTools.ATT_RANDOM_ORDER);
		boolean levelsRandomOrder = Boolean.parseBoolean(att);
		result.setLevelsRandomOrder(levelsRandomOrder);
	}
	
	private static void loadPlayersElement(Element root, QuickMatchConfiguration result)
	{	// random location
		String att = root.getAttributeValue(XmlTools.ATT_RANDOM_LOCATION);
		boolean playersRandomLocation = Boolean.parseBoolean(att);
		result.setPlayersRandomLocation(playersRandomLocation);
	}
	
	private static void loadLimitsElement(Element root, QuickMatchConfiguration result)
	{	// points limit
		String att = root.getAttributeValue(XmlTools.ATT_POINTS);
		int limitPoints = Integer.parseInt(att);
		result.setLimitPoints(limitPoints);
		// rounds limit
		att = root.getAttributeValue(XmlTools.ATT_ROUNDS);
		int limitRounds = Integer.parseInt(att);
		result.setLimitRounds(limitRounds);
		// time limit
		att = root.getAttributeValue(XmlTools.ATT_TIME);
		int limitTime = Integer.parseInt(att);
		result.setLimitTime(limitTime);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadPointsElement(Element root, QuickMatchConfiguration result)
	{	// values
		ArrayList<Integer> points = new ArrayList<Integer>();
		for(int i=0;i<GameConstants.CONTROL_COUNT;i++)
			points.add(0);
		List<Element> list = root.getChildren(XmlTools.ELT_VALUE);
		for(Element e:list)
		{	String rankStr = e.getAttributeValue(XmlTools.ATT_RANK);
			int rank = Integer.parseInt(rankStr);
			String pointsStr = e.getAttributeValue(XmlTools.ATT_POINTS);
			int pts = Integer.parseInt(pointsStr);
			points.set(rank-1,pts);
		}
		result.setPoints(points);
		// share
		String att = root.getAttributeValue(XmlTools.ATT_SHARE);
		boolean pointsShare = Boolean.parseBoolean(att);
		result.setPointsShare(pointsShare);
		// points draw
		att = root.getAttributeValue(XmlTools.ATT_DRAW);
		QuickMatchDraw pointsDraw = QuickMatchDraw.valueOf(att.toUpperCase(Locale.ENGLISH));
		result.setPointsDraw(pointsDraw);		
	}
}
