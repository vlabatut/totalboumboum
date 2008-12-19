package fr.free.totalboumboum.configuration.game.tournament;

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
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class TournamentConfigurationSaver
{	
	public static void saveGameConfiguration(TournamentConfiguration gameConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameElement(gameConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_GAME); 
		
		// tournament
		Element tournamentElement = saveTournamentElement(gameConfiguration);
		result.addContent(tournamentElement);
		
		// quick match
		Element matchElement = saveQuickMatchElement(gameConfiguration);
		result.addContent(matchElement);
		
		// quick start round
		Element roundElement = saveQuickStartElement(gameConfiguration);
		result.addContent(roundElement);

		return result;
	}
	
	private static Element saveTournamentElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_TOURNAMENT);
		
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection tournamentSelected = gameConfiguration.getTournamentSelected();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,tournamentSelected);
		result.addContent(playersElement);
		
		return result;
	}

	private static Element saveQuickMatchElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_QUICKMATCH);
		
		// name
		Element matchElement = new Element(XmlTools.ELT_MATCH);
		String quickMatch = gameConfiguration.getQuickMatchName();
		matchElement.setAttribute(XmlTools.ATT_NAME,quickMatch);
		result.addContent(matchElement);
		
		// settings
		Element optionssElement = saveQuickMatchOptionsElement(gameConfiguration);
		result.addContent(optionssElement);

		// settings
		Element settingsElement = saveQuickMatchSettingsElement(gameConfiguration);
		result.addContent(settingsElement);

		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickMatchSelected = gameConfiguration.getQuickMatchSelectedProfiles();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickMatchSelected);
		result.addContent(playersElement);

		return result;
	}
	
	private static Element saveQuickMatchOptionsElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_OPTIONS);
		
	// use last players
		String useLastPlayers = Boolean.toString(gameConfiguration.getQuickMatchUseLastPlayers());
		result.setAttribute(XmlTools.ATT_USE_LAST_PLAYERS,useLastPlayers);

		// use last levels
		String useLastLevels = Boolean.toString(gameConfiguration.getQuickMatchUseLastLevels());
		result.setAttribute(XmlTools.ATT_USE_LAST_LEVELS,useLastLevels);
		
		return result;
	}
	
	private static Element saveQuickMatchSettingsElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_SETTINGS);

		// levels
		{	Element levelsElement = new Element(XmlTools.ELT_LEVELS);	
			// random order
			String randomOrder = Boolean.toString(gameConfiguration.getQuickMatchLevelsRandomOrder());
			levelsElement.setAttribute(XmlTools.ATT_RANDOM_ORDER,randomOrder);
		}
		// players
		{	Element playersElement = new Element(XmlTools.ELT_PLAYERS);
			// random location
			String randomLocation = Boolean.toString(gameConfiguration.getQuickMatchPlayersRandomLocation());
			playersElement.setAttribute(XmlTools.ATT_RANDOM_LOCATION,randomLocation);
		}
		// limits
		{	Element limitsElement = new Element(XmlTools.ELT_LIMITS);
			// limit points
			String points = Integer.toString(gameConfiguration.getQuickMatchLimitPoints());
			limitsElement.setAttribute(XmlTools.ATT_POINTS,points);
			// limit rounds
			String rounds = Integer.toString(gameConfiguration.getQuickMatchLimitRounds());
			limitsElement.setAttribute(XmlTools.ATT_ROUNDS,rounds);
			// limit time
			String time = Integer.toString(gameConfiguration.getQuickMatchLimitTime());
			limitsElement.setAttribute(XmlTools.ATT_TIME,time);
		}
		// points
		{	Element pointsElement = new Element(XmlTools.ELT_POINTS);
			// share
			String share = Boolean.toString(gameConfiguration.getQuickMatchPointsShare());
			pointsElement.setAttribute(XmlTools.ATT_SHARE,share);
			// draw
			String draw = gameConfiguration.getQuickMatchPointsDraw().toString().toLowerCase(Locale.ENGLISH);
			pointsElement.setAttribute(XmlTools.ATT_DRAW,draw);
			// values
			ArrayList<Integer> values = gameConfiguration.getQuickMatchPoints();
			for(int r=0;r<values.size();r++)
			{	Element valueElement = new Element(XmlTools.ELT_VALUE);
				// limit rounds
				String rank = Integer.toString(r+1);
				valueElement.setAttribute(XmlTools.ATT_RANK,rank);
				// limit time
				String points = Integer.toString(values.get(r));
				valueElement.setAttribute(XmlTools.ATT_POINTS,points);
			}
		}

		return result;
	}
	
	private static Element saveQuickStartElement(TournamentConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_QUICKSTART);
	
		// name
		Element roundElement = new Element(XmlTools.ELT_ROUND);
		String quickStart = gameConfiguration.getQuickStartName();
		roundElement.setAttribute(XmlTools.ATT_NAME,quickStart);
		result.addContent(roundElement);
		
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickStartSelected = gameConfiguration.getQuickStartSelected();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickStartSelected);
		result.addContent(playersElement);
		
		return result;
	}
}
