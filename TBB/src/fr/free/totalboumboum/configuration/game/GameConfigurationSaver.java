package fr.free.totalboumboum.configuration.game;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class GameConfigurationSaver
{	
	public static void saveGameConfiguration(GameConfiguration gameConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameElement(gameConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameElement(GameConfiguration gameConfiguration)
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
	
	private static Element saveTournamentElement(GameConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_TOURNAMENT);
		
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection tournamentSelected = gameConfiguration.getTournamentSelected();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,tournamentSelected);
		result.addContent(playersElement);
		
		return result;
	}

	private static Element saveQuickMatchElement(GameConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_QUICKMATCH);
		
		// name
		Element matchElement = new Element(XmlTools.ELT_MATCH);
		String quickMatch = gameConfiguration.getQuickMatchName();
		matchElement.setAttribute(XmlTools.ATT_NAME,quickMatch);
		result.addContent(matchElement);
		
		// use last players
		Element optionsElement = new Element(XmlTools.ELT_OPTIONS);
		String useLastPlayers = Boolean.toString(gameConfiguration.getQuickMatchUseLastPlayers());
		optionsElement.setAttribute(XmlTools.ATT_USE_LAST_PLAYERS,useLastPlayers);
		result.addContent(optionsElement);
		// use last levels
		String useLastLevels = Boolean.toString(gameConfiguration.getQuickMatchUseLastLevels());
		optionsElement.setAttribute(XmlTools.ATT_USE_LAST_LEVELS,useLastLevels);
		result.addContent(optionsElement);
		
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickMatchSelected = gameConfiguration.getQuickMatchSelectedProfiles();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickMatchSelected);
		result.addContent(playersElement);

		return result;
	}
	
	private static Element saveQuickStartElement(GameConfiguration gameConfiguration)
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
