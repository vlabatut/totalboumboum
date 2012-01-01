package org.totalboumboum.stream.file.replay;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ReplaySaver
{	
	public static void saveReplay(FileServerStream replay) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveReplayElement(replay);	
		
		// save file
		String folder = FilePaths.getReplaysPath() + File.separator + replay.getFolder();
		String path = folder + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_XML;
		File dataFile = new File(path);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder + File.separator + FileNames.FILE_REPLAY + FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveReplayElement(FileServerStream replay)
	{	Element result = new Element(XmlNames.REPLAY); 
		
		// level
		Element tournamentElement = saveLevelElement(replay);
		result.addContent(tournamentElement);
	
		// date
		Element datesElement = saveDateElement(replay);
		result.addContent(datesElement);
	
		// players
		Element playersElement = savePlayersElement(replay);
		result.addContent(playersElement);

		return result;
	}

	private static Element saveLevelElement(FileServerStream replay)
	{	Element result = new Element(XmlNames.LEVEL);
		
		// name
		String name = replay.getLevelName();
		result.setAttribute(XmlNames.NAME,name);

		// pack
		String pack = replay.getLevelPack();
		result.setAttribute(XmlNames.PACK,pack);
		
		return result;
	}

	private static Element saveDateElement(FileServerStream replay)
	{	Element result = new Element(XmlNames.DATE);
		
		// save
		Date save = replay.getSaveDate();
		String saveStr = TimeTools.dateJavaToXml(save);
		result.setAttribute(XmlNames.SAVE,saveStr);
		
		return result;
	}

	private static Element savePlayersElement(FileServerStream replay)
	{	Element result = new Element(XmlNames.PLAYERS);
		List<String> players = replay.getPlayers();
		for(String player: players)
		{	Element playerElement = savePlayerElement(player);
			result.addContent(playerElement);
		}
		return result;
	}
	
	private static Element savePlayerElement(String player)
	{	Element result = new Element(XmlNames.PLAYER);
		
		// name
		result.setAttribute(XmlNames.NAME,player);
		
		return result;
	}
}
