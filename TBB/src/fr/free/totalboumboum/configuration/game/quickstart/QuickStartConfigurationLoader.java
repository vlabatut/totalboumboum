package fr.free.totalboumboum.configuration.game.quickstart;

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

public class QuickStartConfigurationLoader
{	
	public static QuickStartConfiguration loadGameConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	QuickStartConfiguration result = new QuickStartConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameElement(root,result);
		return result;
	}

	private static void loadGameElement(Element root, QuickStartConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// tournament
		{	Element element = root.getChild(XmlTools.ELT_TOURNAMENT);
			loadTournamentElement(element,result);
		}
		// quick match
		{	Element element = root.getChild(XmlTools.ELT_QUICKMATCH);
			loadQuickMatchElement(element,result);
		}
		// quick start
		{	Element element = root.getChild(XmlTools.ELT_QUICKSTART);
			loadQuickStartElement(element,result);
		}
	}
	
	private static void loadTournamentElement(Element root, QuickStartConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection tournamentSelected = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setTournamentSelected(tournamentSelected);
	}

	private static void loadQuickMatchElement(Element root, QuickStartConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// name
		Element matchElement = root.getChild(XmlTools.ELT_MATCH);
		String quickMatchName = matchElement.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setQuickMatchName(quickMatchName);
		
		// options
		Element optionsElement = root.getChild(XmlTools.ELT_OPTIONS);
		// use last players
		String att = optionsElement.getAttributeValue(XmlTools.ATT_USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(att);
		result.setQuickMatchUseLastPlayers(useLastPlayers);
		// use last levels
		att = optionsElement.getAttributeValue(XmlTools.ATT_USE_LAST_LEVELS);
		boolean useLastLevels = Boolean.parseBoolean(att);
		result.setQuickMatchUseLastLevels(useLastLevels);
		
		// settings
		Element settingsElement = root.getChild(XmlTools.ELT_SETTINGS);
		loadSettingsElement(settingsElement,result);
		
		// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickMatchProfiles = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setQuickMatchSelectedProfiles(quickMatchProfiles);

		// levels
		Element levelsElement = root.getChild(XmlTools.ELT_LEVELS);
		LevelsSelection quickMatchLevels = LevelsSelectionLoader.loadLevelsSelection(levelsElement);
		result.setQuickMatchSelectedLevels(quickMatchLevels);
	}

	@SuppressWarnings("unchecked")
	private static void loadSettingsElement(Element root, QuickStartConfiguration result)
	{	// levels 
		{	Element levelsElement = root.getChild(XmlTools.ELT_LEVELS);
			// random order
			String att = levelsElement.getAttributeValue(XmlTools.ATT_RANDOM_ORDER);
			boolean levelsRandomOrder = Boolean.parseBoolean(att);
			result.setQuickMatchLevelsRandomOrder(levelsRandomOrder);
		}		
		// players 
		{	Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
			// random location
			String att = playersElement.getAttributeValue(XmlTools.ATT_RANDOM_LOCATION);
			boolean playersRandomLocation = Boolean.parseBoolean(att);
			result.setQuickMatchPlayersRandomLocation(playersRandomLocation);
		}		
		// limits 
		{	Element limitsElement = root.getChild(XmlTools.ELT_LIMITS);
			// points limit
			String att = limitsElement.getAttributeValue(XmlTools.ATT_POINTS);
			int limitPoints = Integer.parseInt(att);
			result.setQuickMatchLimitPoints(limitPoints);
			// rounds limit
			att = limitsElement.getAttributeValue(XmlTools.ATT_ROUNDS);
			int limitRounds = Integer.parseInt(att);
			result.setQuickMatchLimitRounds(limitRounds);
			// time limit
			att = limitsElement.getAttributeValue(XmlTools.ATT_TIME);
			int limitTime = Integer.parseInt(att);
			result.setQuickMatchLimitTime(limitTime);
		}		
		// points
		{	Element pointsElement = root.getChild(XmlTools.ELT_POINTS);
			// values
			ArrayList<Integer> points = new ArrayList<Integer>();
			for(int i=0;i<GameConstants.CONTROL_COUNT;i++)
				points.add(0);
			List<Element> list = pointsElement.getChildren(XmlTools.ELT_VALUE);
			for(Element e:list)
			{	String rankStr = e.getAttributeValue(XmlTools.ATT_RANK);
				int rank = Integer.parseInt(rankStr);
				String pointsStr = e.getAttributeValue(XmlTools.ATT_POINTS);
				int pts = Integer.parseInt(pointsStr);
				points.set(rank-1,pts);
			}
			result.setQuickMatchPoints(points);
			// share
			String att = pointsElement.getAttributeValue(XmlTools.ATT_SHARE);
			boolean pointsShare = Boolean.parseBoolean(att);
			result.setQuickMatchPointsShare(pointsShare);
			// points draw
			att = pointsElement.getAttributeValue(XmlTools.ATT_DRAW);
			QuickMatchDraw pointsDraw = QuickMatchDraw.valueOf(att.toUpperCase(Locale.ENGLISH));
			result.setQuickMatchPointsDraw(pointsDraw);
		}	
	}
	
	private static void loadQuickStartElement(Element root, QuickStartConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// name
		Element roundElement = root.getChild(XmlTools.ELT_ROUND);
		String quickStartName = roundElement.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setRoundName(quickStartName);
		
		// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickStartSelected = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setQuickStartSelected(quickStartSelected);
	}
}
