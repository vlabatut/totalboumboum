package fr.free.totalboumboum.game.tournament;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.tournament.cup.CupTournamentLoader;
import fr.free.totalboumboum.game.tournament.league.LeagueTournamentLoader;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournamentLoader;
import fr.free.totalboumboum.game.tournament.single.SingleTournamentLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class TournamentLoader
{	
	private static final String CUP = "cup";
	private static final String LEAGUE = "league";
	private static final String SEQUENCE = "sequence";
	private static final String SINGLE = "single";

	public static AbstractTournament loadTournamentFromFolderPath(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_TOURNAMENT+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_TOURNAMENT+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		AbstractTournament result = loadTournamentElement(folderPath,root);
		return result;
	}
	public static AbstractTournament loadTournamentFromName(String name) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FileTools.getTournamentsPath()+File.separator+name;
		AbstractTournament result = loadTournamentFromFolderPath(individualFolder);
		return result;
    }
	
	@SuppressWarnings("unchecked")
	private static AbstractTournament loadTournamentElement(String path, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		AbstractTournament result = null;
		Element element;
		
		// name
		element = root.getChild(XmlTools.ELT_GENERAL);
		String name = element.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		
		// content
		List<Element> elements = root.getChildren();
		element = elements.get(elements.size()-1);
		String type = element.getName();
		if(type.equalsIgnoreCase(CUP))
			result = CupTournamentLoader.loadTournamentElement(path,element);
		else if(type.equalsIgnoreCase(LEAGUE))
			result = LeagueTournamentLoader.loadTournamentElement(path,element);
		else if(type.equalsIgnoreCase(SEQUENCE))
			result = SequenceTournamentLoader.loadTournamentElement(path,element);
		else if(type.equalsIgnoreCase(SINGLE))
			result = SingleTournamentLoader.loadTournamentElement(path,element);
		
		// notes
		element = root.getChild(XmlTools.ELT_NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		
		// author
		element = root.getChild(XmlTools.ELT_AUTHOR);
		if(element!=null)
		{	String author = element.getAttributeValue(XmlTools.ATT_VALUE);
			result.setAuthor(author);
		}
		
		//
		result.setName(name);
		return result;
	}
}
