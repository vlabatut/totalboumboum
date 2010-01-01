package org.totalboumboum.configuration.game.tournament;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.profile.ProfilesSelection;
import org.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class TournamentConfigurationSaver
{	
	public static void saveTournamentConfiguration(TournamentConfiguration tournamentConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameTournamentElement(tournamentConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME_TOURNAMENT+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_TOURNAMENT+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameTournamentElement(TournamentConfiguration tournamentConfiguration)
	{	Element result = new Element(XmlTools.GAME_TOURNAMENT); 
		
		// options
		Element optionsElement = saveTournamentOptionsElement(tournamentConfiguration);
		result.addContent(optionsElement);
	
		// name
		Element tournamentElement = new Element(XmlTools.TOURNAMENT);
		String tournament = tournamentConfiguration.getTournamentName().toString();
		tournamentElement.setAttribute(XmlTools.NAME,tournament);
		result.addContent(tournamentElement);
		
		// players
		Element playersElement = new Element(XmlTools.PLAYERS);
		ProfilesSelection tournamentSelected = tournamentConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,tournamentSelected);
		result.addContent(playersElement);
		
		return result;
	}

	private static Element saveTournamentOptionsElement(TournamentConfiguration tournamentConfiguration)
	{	Element result = new Element(XmlTools.OPTIONS);
		
		// use last players
		String useLastPlayers = Boolean.toString(tournamentConfiguration.getUseLastPlayers());
		result.setAttribute(XmlTools.USE_LAST_PLAYERS,useLastPlayers);

		// use last settings
		String useLastTournament = Boolean.toString(tournamentConfiguration.getUseLastTournament());
		result.setAttribute(XmlTools.USE_LAST_TOURNAMENT,useLastTournament);

		// auto load
		String autoLoad = Boolean.toString(tournamentConfiguration.getAutoLoad());
		result.setAttribute(XmlTools.AUTOLOAD,autoLoad);

		// auto save
		String autoSave = Boolean.toString(tournamentConfiguration.getAutoSave());
		result.setAttribute(XmlTools.AUTOSAVE,autoSave);
		
		return result;
	}
	
}
