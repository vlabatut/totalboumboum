package org.totalboumboum.game.match;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.limit.LimitLoader;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Loads a match object.
 * 
 * @author Vincent Labatut
 */
public class MatchLoader
{	
	/**
	 * Loads a match object.
	 * 
	 * @param folderPath
	 * 		Location of the match object.
	 * @param tournament
	 * 		Tournament containing the match.
	 * @return
	 * 		The loaded match.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading the match.
	 * @throws SAXException
	 * 		Problem while loading the match.
	 * @throws IOException
	 * 		Problem while loading the match.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the match.
	 */
	public static Match loadMatchFromFolderPath(String folderPath, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(folderPath+File.separator+FileNames.FILE_MATCH+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_MATCH+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		Match result = new Match(tournament);
		File temp = new File(folderPath);
		String name = temp.getName();
		result.setName(name);
		loadMatchElement(root,folderPath,tournament,result);
		
		return result;
    }
    
	/**
	 * Loads a match just using its name.
	 * 
	 * @param name
	 * 		Name of the match.
	 * @param tournament
	 * 		Tournament containing the match.
	 * @return
	 * 		Loaded match.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading the match.
	 * @throws SAXException
	 * 		Problem while loading the match.
	 * @throws IOException
	 * 		Problem while loading the match.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the match.
	 */
	public static Match loadMatchFromName(String name, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FilePaths.getMatchesPath()+File.separator+name;
		Match result = loadMatchFromFolderPath(individualFolder,tournament);
		return result;
    }

	/**
	 * Loads the {@code match} element.
	 * 
	 * @param root
	 * 		Root element.
	 * @param folderPath
	 * 		Folder containing the file.
	 * @param tournament
	 * 		Tournament containing the match.
	 * @param result
	 * 		Loaded match. 
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading the match.
	 * @throws SAXException
	 * 		Problem while loading the match.
	 * @throws IOException
	 * 		Problem while loading the match.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the match.
	 */
    private static void loadMatchElement(Element root, String folderPath, AbstractTournament tournament, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
    	Element element;
		
		// notes
		element = root.getChild(XmlNames.NOTES);
		List<String> notes = loadNotesElement(element);
		result.setNotes(notes);
		
		// author
		element = root.getChild(XmlNames.AUTHOR);
		if(element!=null)
		{	String author = element.getAttributeValue(XmlNames.VALUE);
			result.setAuthor(author);
		}
		
		// limits
		element = root.getChild(XmlNames.LIMITS);
		Limits<MatchLimit> limits = loadLimitsElement(element,folderPath);
		result.setLimits(limits);
		
		// rounds
		element = root.getChild(XmlNames.ROUNDS);
		loadRoundsElement(element,folderPath,result);
		
		// players
		element = root.getChild(XmlNames.PLAYERS);
		if(element!=null)
			loadPlayersElement(element,result);
	}		
		
    /**
 	 * Loads the {@code notes} element.
     * 
     * @param root
 	 * 		Root element.
     * @return
     * 		Notes under the form of a list of strings.
     */
    @SuppressWarnings("unchecked")
	public static List<String> loadNotesElement(Element root)
    {	List<String> result = new ArrayList<String>();
    	if(root!=null)
    	{	List<Element> lines = root.getChildren(XmlNames.LINE);
	    	Iterator<Element> i = lines.iterator();
	    	while(i.hasNext())
	    	{	Element temp = i.next();
	    		String line = temp.getAttributeValue(XmlNames.VALUE).trim();
	    		result.add(line);
	    	}
    	}
    	return result;
    }

    /**
 	 * Loads the {@code rounds} element.
     * 
     * @param root
 	 * 		Root element.
     * @param folder
 	 * 		Folder containing the file.
    * @param result
 	 * 		Loaded match. 
    * 
     * @throws ParserConfigurationException
	 * 		Problem while loading the match.
     * @throws SAXException
	 * 		Problem while loading the match.
     * @throws IOException
	 * 		Problem while loading the match.
     * @throws ClassNotFoundException
	 * 		Problem while loading the match.
     */
    @SuppressWarnings("unchecked")
	private static void loadRoundsElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// rounds order
    	String str = root.getAttribute(XmlNames.RANDOM_ORDER).getValue().trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomOrder(randomOrder);
    	
    	// rounds
		List<Element> elements = root.getChildren(XmlNames.ROUND);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadRoundElement(temp,folder,result);
    	}
    }
    
    /**
 	 * Loads the {@code round} element.
     * 
     * @param root
 	 * 		Root element.
     * @param folder
 	 * 		Folder containing the file.
     * @param result
 	 * 		Loaded match. 
     * 
     * @throws ParserConfigurationException
	 * 		Problem while loading the match.
     * @throws SAXException
	 * 		Problem while loading the match.
     * @throws IOException
	 * 		Problem while loading the match.
     * @throws ClassNotFoundException
	 * 		Problem while loading the match.
     */
    private static void loadRoundElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Round round;
		
		// local
		String localStr = root.getAttribute(XmlNames.LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		
		// name
		String name = root.getAttribute(XmlNames.NAME).getValue();
		
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			round = RoundLoader.loadRoundFromFolderPath(folder,result);
		}
		else
			round = RoundLoader.loadRoundFromName(name,result);
		result.addRound(round);
	}
    
    /**
 	 * Loads the {@code limits} element.
     * 
     * @param root
 	 * 		Root element.
     * @param folder
 	 * 		Folder containing the file.
     * @return
 	 * 		Loaded match. 
     * 
     * @throws ParserConfigurationException
	 * 		Problem while loading the match.
     * @throws SAXException
	 * 		Problem while loading the match.
     * @throws IOException
	 * 		Problem while loading the match.
     */
	@SuppressWarnings("unchecked")
	public static Limits<MatchLimit> loadLimitsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limits<MatchLimit> result = new Limits<MatchLimit>();

		List<Element> elements = root.getChildren();
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			MatchLimit limit = (MatchLimit)LimitLoader.loadLimitElement(temp,folder);
			result.addLimit(limit);
		}
		
		return result;
	}


	/**
	 * Loads the {@code players} element.
	 * 
	 * @param root
	 * 		Root element.
	 * @param result
	 * 		Loaded match. 
	 */
    private static void loadPlayersElement(Element root, Match result)
    {	// forbidden player numbers
    	String forbidPlayerNumbers = root.getAttributeValue(XmlNames.FORBID_PLAYER_NUMBERS);
    	if(forbidPlayerNumbers!=null)
    	{	Set<Integer> set = XmlTools.parseMultipleInteger(forbidPlayerNumbers);
    		result.setForbiddenPlayerNumbers(set);
    	}
     }
}
