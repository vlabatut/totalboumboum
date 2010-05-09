package fr.free.totalboumboum.configuration.game.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class TournamentConfigurationLoader
{	
	public static TournamentConfiguration loadTournamentConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	TournamentConfiguration result = new TournamentConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME_TOURNAMENT+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_TOURNAMENT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameTournamentElement(root,result);
		return result;
	}

	private static void loadGameTournamentElement(Element root, TournamentConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// options
		Element optionsElement = root.getChild(XmlTools.ELT_OPTIONS);
		loadOptionsElement(optionsElement,result);
		
		// name
		Element tournamentElement = root.getChild(XmlTools.ELT_TOURNAMENT);
		String tournamentName = tournamentElement.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setTournamentName(new StringBuffer(tournamentName));
		
		// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection tournamentSelected = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(tournamentSelected);
	}
	
	private static void loadOptionsElement(Element root, TournamentConfiguration result)
	{	// use last players
		String useLastPlayersStr = root.getAttributeValue(XmlTools.ATT_USE_LAST_PLAYERS);
		boolean useLastPlayers = Boolean.parseBoolean(useLastPlayersStr);
		result.setUseLastPlayers(useLastPlayers);
		
		// use last tournament
		String useLastTournamentStr = root.getAttributeValue(XmlTools.ATT_USE_LAST_TOURNAMENT);
		boolean useLastTournament = Boolean.parseBoolean(useLastTournamentStr);
		result.setUseLastTournament(useLastTournament);
		
		// autoload
		String autoLoadStr = root.getAttributeValue(XmlTools.ATT_AUTOLOAD);
		boolean autoLoad = Boolean.parseBoolean(autoLoadStr);
		result.setAutoLoad(autoLoad);
		
		// autosave
		String autoSaveStr = root.getAttributeValue(XmlTools.ATT_AUTOSAVE);
		boolean autoSave = Boolean.parseBoolean(autoSaveStr);
		result.setAutoSave(autoSave);
	}
}
