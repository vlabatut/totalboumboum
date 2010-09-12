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
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.configuration.profiles.ProfilesSelectionSaver;
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
public class TournamentConfigurationSaver
{	
	public static void saveTournamentConfiguration(TournamentConfiguration tournamentConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameTournamentElement(tournamentConfiguration);	
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_GAME_TOURNAMENT+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_GAME_TOURNAMENT+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameTournamentElement(TournamentConfiguration tournamentConfiguration)
	{	Element result = new Element(XmlNames.GAME_TOURNAMENT); 
		
		// options
		Element optionsElement = saveTournamentOptionsElement(tournamentConfiguration);
		result.addContent(optionsElement);
	
		// name
		Element tournamentElement = new Element(XmlNames.TOURNAMENT);
		String tournament = tournamentConfiguration.getTournamentName().toString();
		tournamentElement.setAttribute(XmlNames.NAME,tournament);
		result.addContent(tournamentElement);
		
		// players
		Element playersElement = new Element(XmlNames.PLAYERS);
		ProfilesSelection tournamentSelected = tournamentConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,tournamentSelected);
		result.addContent(playersElement);
		
		return result;
	}

	private static Element saveTournamentOptionsElement(TournamentConfiguration tournamentConfiguration)
	{	Element result = new Element(XmlNames.OPTIONS);
		
		// use last players
		String useLastPlayers = Boolean.toString(tournamentConfiguration.getUseLastPlayers());
		result.setAttribute(XmlNames.USE_LAST_PLAYERS,useLastPlayers);

		// use last settings
		String useLastTournament = Boolean.toString(tournamentConfiguration.getUseLastTournament());
		result.setAttribute(XmlNames.USE_LAST_TOURNAMENT,useLastTournament);

		// auto load
		String autoLoad = Boolean.toString(tournamentConfiguration.getAutoLoad());
		result.setAttribute(XmlNames.AUTOLOAD,autoLoad);

		// auto save
		String autoSave = Boolean.toString(tournamentConfiguration.getAutoSave());
		result.setAttribute(XmlNames.AUTOSAVE,autoSave);
		
		return result;
	}
	
}
