package fr.free.totalboumboum.game.archive;

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
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.StringTools;
import fr.free.totalboumboum.tools.XmlTools;

public class GameArchiveSaver
{	
	public static void saveGameArchive(GameArchive gameArchive) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveArchiveElement(gameArchive);	
		// save file
		String folder = FileTools.getSavesPath()+File.separator+gameArchive.getFolder();
		String path = folder + File.separator+FileTools.FILE_ARCHIVE+FileTools.EXTENSION_XML;
		File dataFile = new File(path);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ARCHIVE+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveArchiveElement(GameArchive gameArchive)
	{	Element result = new Element(XmlTools.ELT_ARCHIVE); 
		
		// tournament
		Element tournamentElement = saveTournamentElement(gameArchive);
		result.addContent(tournamentElement);
	
		// played
		Element playedElement = savePlayedElement(gameArchive);
		result.addContent(playedElement);
	
		// dates
		Element datesElement = saveDatesElement(gameArchive);
		result.addContent(datesElement);
	
		// players
		Element playersElement = savePlayersElement(gameArchive);
		result.addContent(playersElement);

		return result;
	}

	private static Element saveTournamentElement(GameArchive gameArchive)
	{	Element result = new Element(XmlTools.ELT_TOURNAMENT);
		
		// name
		String name = gameArchive.getName();
		result.setAttribute(XmlTools.ATT_NAME,name);

		// type
		TournamentType type = gameArchive.getType();
		String typeStr = type.toString();
		result.setAttribute(XmlTools.ATT_TYPE,typeStr);
		
		return result;
	}

	private static Element savePlayedElement(GameArchive gameArchive)
	{	Element result = new Element(XmlTools.ELT_PLAYED);
		
		// matches
		String matches = Integer.toString(gameArchive.getPlayedMatches());
		result.setAttribute(XmlTools.ATT_MATCHES,matches);

		// rounds
		String rounds = Integer.toString(gameArchive.getPlayedRounds());
		result.setAttribute(XmlTools.ATT_ROUNDS,rounds);
		
		return result;
	}

	private static Element saveDatesElement(GameArchive gameArchive)
	{	Element result = new Element(XmlTools.ELT_DATES);
		
		// start
		Date start = gameArchive.getStartDate();
		String startStr = StringTools.dateJavaToXml(start);
		result.setAttribute(XmlTools.ATT_START,startStr);

		// save
		Date save = gameArchive.getSaveDate();
		String saveStr = StringTools.dateJavaToXml(save);
		result.setAttribute(XmlTools.ATT_SAVE,saveStr);
		
		return result;
	}

	private static Element savePlayersElement(GameArchive gameArchive)
	{	Element result = new Element(XmlTools.ELT_PLAYERS);
		ArrayList<String> players = gameArchive.getPlayers();
		for(String player: players)
		{	Element playerElement = savePlayerElement(player);
			result.addContent(playerElement);
		}
		return result;
	}
	
	private static Element savePlayerElement(String player)
	{	Element result = new Element(XmlTools.ELT_PLAYER);
		
		// name
		result.setAttribute(XmlTools.ATT_NAME,player);
		
		return result;
	}
}