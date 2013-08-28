package org.totalboumboum.configuration.game.tournament;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.profiles.ProfilesSelection;
import org.totalboumboum.configuration.profiles.ProfilesSelectionLoader;
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
public class TournamentConfigurationLoader
{	
	public static TournamentConfiguration loadTournamentConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	TournamentConfiguration result = new TournamentConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_GAME_TOURNAMENT+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_GAME_TOURNAMENT+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameTournamentElement(root,result);
		return result;
	}

	private static void loadGameTournamentElement(Element root, TournamentConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// options
		Element optionsElement = root.getChild(XmlNames.OPTIONS);
		loadOptionsElement(optionsElement,result);
		
		// name
		Element tournamentElement = root.getChild(XmlNames.TOURNAMENT);
		String tournamentName = tournamentElement.getAttribute(XmlNames.NAME).getValue().trim();
		result.setTournamentName(new StringBuffer(tournamentName));
		
		// players
		Element playersElement = root.getChild(XmlNames.PLAYERS);
		ProfilesSelection tournamentSelected = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(tournamentSelected);

		// auto advance
		Element aaElement = root.getChild(XmlNames.AUTO_ADVANCE);
		String aaIndexStr = aaElement.getAttribute(XmlNames.INDEX).getValue().trim();
		int aaIndex = Integer.parseInt(aaIndexStr);
		result.setAutoAdvanceIndex(aaIndex);
	}
	
	private static void loadOptionsElement(Element root, TournamentConfiguration result)
	{	// use last players
		String useLastPlayersStr = root.getAttributeValue(XmlNames.USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(useLastPlayersStr);
		result.setUseLastPlayers(useLastPlayers);
		
		// use last tournament
		String useLastTournamentStr = root.getAttributeValue(XmlNames.USE_LAST_TOURNAMENT);
		boolean useLastTournament = Boolean.parseBoolean(useLastTournamentStr);
		result.setUseLastTournament(useLastTournament);
		
		// autoload
		String autoLoadStr = root.getAttributeValue(XmlNames.AUTOLOAD);
		boolean autoLoad = Boolean.parseBoolean(autoLoadStr);
		result.setAutoLoad(autoLoad);
		
		// autosave
		String autoSaveStr = root.getAttributeValue(XmlNames.AUTOSAVE);
		boolean autoSave = Boolean.parseBoolean(autoSaveStr);
		result.setAutoSave(autoSave);
	}
}
