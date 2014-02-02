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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * This class is used to record {@link GameArchive} objects
 * under the form of XML files.
 * 
 * @author Vincent Labatut
 */
public class GameArchiveSaver
{	
	/**
	 * Creates an XML file representing the specified {@link GameArchive} object.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accesing the files.
	 * @throws SAXException
	 * 		Problem while accesing the files.
	 * @throws IOException
	 * 		Problem while accesing the files.
	 */
	public static void saveGameArchive(GameArchive gameArchive) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveArchiveElement(gameArchive);
		
		// save file
		String folder = FilePaths.getSavesPath()+File.separator+gameArchive.getFolder();
		String path = folder + File.separator+FileNames.FILE_ARCHIVE+FileNames.EXTENSION_XML;
		File dataFile = new File(path);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ARCHIVE+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	/**
	 * Processes the main element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element saveArchiveElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.ARCHIVE);
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);
		
		// tournament
		Element tournamentElement = saveTournamentElement(gameArchive);
		result.addContent(tournamentElement);
	
		// played
		Element playedElement = savePlayedElement(gameArchive);
		result.addContent(playedElement);
	
		// total
		Element totalElement = saveTotalElement(gameArchive);
		result.addContent(totalElement);
	
		// dates
		Element datesElement = saveDatesElement(gameArchive);
		result.addContent(datesElement);
	
		// players
		Element playersElement = savePlayersElement(gameArchive);
		result.addContent(playersElement);

		return result;
	}

	/**
	 * Processes the tournament-related element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element saveTournamentElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.TOURNAMENT);
		
		// name
		String name = gameArchive.getName();
		result.setAttribute(XmlNames.NAME,name);

		// type
		TournamentType type = gameArchive.getType();
		String typeStr = type.toString();
		result.setAttribute(XmlNames.TYPE,typeStr);
		
		return result;
	}

	/**
	 * Processes the played confrontations-related element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element savePlayedElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.PLAYED);
		
		// matches
		String matches = Integer.toString(gameArchive.getPlayedMatches());
		result.setAttribute(XmlNames.MATCHES,matches);

		// rounds
		String rounds = Integer.toString(gameArchive.getPlayedRounds());
		result.setAttribute(XmlNames.ROUNDS,rounds);
		
		return result;
	}

	/**
	 * Processes the played confrontations-related element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element saveTotalElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.TOTAL);
		
		// matches
		Integer value = gameArchive.getTotalMatches();
		if(value!=null)
		{	String matches = Integer.toString(value);
			result.setAttribute(XmlNames.MATCHES,matches);
		}

		// rounds
		String rounds = Integer.toString(gameArchive.getTotalRounds());
		result.setAttribute(XmlNames.ROUNDS,rounds);
		
		return result;
	}

	/**
	 * Processes the dates-related element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element saveDatesElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.DATES);
		
		// start
		Date start = gameArchive.getStartDate();
		String startStr = TimeTools.dateJavaToXml(start);
		result.setAttribute(XmlNames.START,startStr);

		// save
		Date save = gameArchive.getSaveDate();
		String saveStr = TimeTools.dateJavaToXml(save);
		result.setAttribute(XmlNames.SAVE,saveStr);
		
		return result;
	}

	/**
	 * Processes the players-related element of the produced XML file.
	 * 
	 * @param gameArchive
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element savePlayersElement(GameArchive gameArchive)
	{	Element result = new Element(XmlNames.PLAYERS);
		List<String> players = gameArchive.getPlayers();
		for(String player: players)
		{	Element playerElement = savePlayerElement(player);
			result.addContent(playerElement);
		}
		return result;
	}
	
	/**
	 * Processes the player-related element of the produced XML file.
	 * 
	 * @param player
	 * 		Object to record.
	 * @return
	 * 		XML element.
	 */
	private static Element savePlayerElement(String player)
	{	Element result = new Element(XmlNames.PLAYER);
		
		// name
		result.setAttribute(XmlNames.NAME,player);
		
		return result;
	}
}
