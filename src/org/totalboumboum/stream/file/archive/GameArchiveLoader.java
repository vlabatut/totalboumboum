package org.totalboumboum.stream.file.archive;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Loads a {@link GameArchive} object from an XML file.
 * 
 * @author Vincent Labatut
 */
public class GameArchiveLoader
{
	/**
	 * Loads a game archive object from the specified folder.
	 * 
	 * @param folderName
	 * 		Name of the folder.
	 * @return
	 * 		Loaded {@link GameArchive} object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading the file.
	 * @throws SAXException
	 * 		Problem while loading the file.
	 * @throws IOException
	 * 		Problem while loading the file.
	 * @throws IllegalArgumentException
	 * 		Problem while loading the file.
	 * @throws SecurityException
	 * 		Problem while loading the file.
	 * @throws IllegalAccessException
	 * 		Problem while loading the file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading the file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the file.
	 */
	public static GameArchive loadGameArchive(String folderName) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String individualFolder = FilePaths.getSavesPath()+File.separator+folderName;
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_ARCHIVE+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ARCHIVE+FileNames.EXTENSION_SCHEMA);
		
		GameArchive result = new GameArchive();
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadArchiveElement(root,result);
		result.setFolder(folderName);
		
		return result;
	}
	
	/**
	 * Processes the main XML element of the game archive XML file.
	 * 
	 * @param root
	 * 		Main element.
	 * @param result
	 * 		Corresponding game archive object.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading the file.
	 * @throws SecurityException
	 * 		Problem while loading the file.
	 * @throws ParserConfigurationException
	 * 		Problem while loading the file.
	 * @throws SAXException
	 * 		Problem while loading the file.
	 * @throws IOException
	 * 		Problem while loading the file.
	 * @throws IllegalAccessException
	 * 		Problem while loading the file.
	 * @throws NoSuchFieldException
	 * 		Problem while loading the file.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the file.
	 */
	private static void loadArchiveElement(Element root, GameArchive result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// tournament
		Element tournamentElement = root.getChild(XmlNames.TOURNAMENT);
		loadTournamentElement(tournamentElement,result);
		
		// played
		Element playedElement = root.getChild(XmlNames.PLAYED);
		loadPlayedConfrontationsElement(playedElement,result);
		
		// total
		Element totalElement = root.getChild(XmlNames.TOTAL);
		loadTotalConfrontationsElement(totalElement,result);
		
		// dates
		Element datesElement = root.getChild(XmlNames.DATES);
		loadDatesElement(datesElement,result);
		
		// players
		Element playersElement = root.getChild(XmlNames.PLAYERS);
		loadPlayersElement(playersElement,result);		
	}
	
	/**
	 * Processes the tournament-related element of the XML file.
	 * 
	 * @param root
	 * 		Tournament-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	private static void loadTournamentElement(Element root, GameArchive result)
	{	// name
		String name = root.getAttributeValue(XmlNames.NAME);
		result.setName(name);
		
		// type
		String typeStr = root.getAttributeValue(XmlNames.TYPE).toUpperCase(Locale.ENGLISH);
		TournamentType type = TournamentType.valueOf(typeStr);
		result.setType(type);
	}
	
	/**
	 * Processes the played confrontations-related element of the XML file.
	 * 
	 * @param root
	 * 		Played confrontations-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	private static void loadPlayedConfrontationsElement(Element root, GameArchive result)
	{	// matches
		String matchesStr = root.getAttributeValue(XmlNames.MATCHES);
		int matches = Integer.parseInt(matchesStr);
		result.setPlayedMatches(matches);
		
		// rounds
		String roundsStr = root.getAttributeValue(XmlNames.ROUNDS);
		int rounds = Integer.parseInt(roundsStr);
		result.setPlayedRounds(rounds);
	}
	
	/**
	 * Processes the total confrontations-related element of the XML file.
	 * 
	 * @param root
	 * 		Total confrontations-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	private static void loadTotalConfrontationsElement(Element root, GameArchive result)
	{	// matches
		String matchesStr = root.getAttributeValue(XmlNames.MATCHES);
		if(matchesStr!=null)
		{	int matches = Integer.parseInt(matchesStr);
			result.setTotalMatches(matches);
		}
		
		// rounds
		String roundsStr = root.getAttributeValue(XmlNames.ROUNDS);
		int rounds = Integer.parseInt(roundsStr);
		result.setTotalRounds(rounds);
	}
	
	/**
	 * Processes the dates-related element of the XML file.
	 * 
	 * @param root
	 * 		Dates-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	private static void loadDatesElement(Element root, GameArchive result)
	{	// start
		String startStr = root.getAttributeValue(XmlNames.START);
		Date start = TimeTools.dateXmlToJava(startStr);
		result.setStartDate(start);
		
		// save
		String saveStr = root.getAttributeValue(XmlNames.SAVE);
		Date save = TimeTools.dateXmlToJava(saveStr);
		result.setSaveDate(save);
	}

	/**
	 * Processes the players-related element of the XML file.
	 * 
	 * @param root
	 * 		Players-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	@SuppressWarnings("unchecked")
	private static void loadPlayersElement(Element root, GameArchive result)
	{	List<Element> playerList = root.getChildren(XmlNames.PLAYER);
		for(Element playerElement: playerList)
			loadPlayerElement(playerElement,result);
	}

	/**
	 * Processes the player-related element of the XML file.
	 * 
	 * @param root
	 * 		Player-related XML element.
	 * @param result
	 * 		Resulting game archive object (updated).
	 */
	private static void loadPlayerElement(Element root, GameArchive result)
	{	// name
		String name = root.getAttributeValue(XmlNames.NAME);
		result.addPlayer(name);
	}
}
