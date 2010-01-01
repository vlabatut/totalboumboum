package org.totalboumboum.game.match;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.limit.LimitLoader;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.round.RoundLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class MatchLoader
{	
	public static Match loadMatchFromFolderPath(String folderPath, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Match result = new Match(tournament);
		File temp = new File(folderPath);
		String name = temp.getName();
		result.setName(name);
		loadMatchElement(root,folderPath,tournament,result);
		return result;
    }
    
	public static Match loadMatchFromName(String name, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FileTools.getMatchesPath()+File.separator+name;
		Match result = loadMatchFromFolderPath(individualFolder,tournament);
		return result;
    }

    private static void loadMatchElement(Element root, String folderPath, AbstractTournament tournament, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
    	Element element;
		
		// notes
		element = root.getChild(XmlTools.NOTES);
		ArrayList<String> notes = loadNotesElement(element);
		result.setNotes(notes);
		
		// author
		element = root.getChild(XmlTools.AUTHOR);
		if(element!=null)
		{	String author = element.getAttributeValue(XmlTools.VALUE);
			result.setAuthor(author);
		}
		
		// limits
		element = root.getChild(XmlTools.LIMITS);
		Limits<MatchLimit> limits = loadLimitsElement(element,folderPath);
		result.setLimits(limits);
		
		// rounds
		element = root.getChild(XmlTools.ROUNDS);
		loadRoundsElement(element,folderPath,result);
	}		
		
    @SuppressWarnings("unchecked")
	public static ArrayList<String> loadNotesElement(Element root)
    {	ArrayList<String> result = new ArrayList<String>();
    	if(root!=null)
    	{	List<Element> lines = root.getChildren(XmlTools.LINE);
	    	Iterator<Element> i = lines.iterator();
	    	while(i.hasNext())
	    	{	Element temp = i.next();
	    		String line = temp.getAttributeValue(XmlTools.VALUE).trim();
	    		result.add(line);
	    	}
    	}
    	return result;
    }

    @SuppressWarnings("unchecked")
	private static void loadRoundsElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// rounds order
    	String str = root.getAttribute(XmlTools.RANDOM_ORDER).getValue().trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomOrder(randomOrder);
    	// rounds
		List<Element> elements = root.getChildren(XmlTools.ROUND);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadRoundElement(temp,folder,result);
    	}
    }
    
    private static void loadRoundElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Round round;
		// local
		String localStr = root.getAttribute(XmlTools.LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			round = RoundLoader.loadRoundFromFolderPath(folder,result);
		}
		else
			round = RoundLoader.loadRoundFromName(name,result);
		result.addRound(round);
	}
    
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
}
