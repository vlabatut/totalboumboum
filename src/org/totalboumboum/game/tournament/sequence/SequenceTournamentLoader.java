package org.totalboumboum.game.tournament.sequence;

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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.limit.LimitLoader;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.TournamentLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.TournamentLoader;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * Loads an XML file representing a sequence tournament.
 * 
 * @author Vincent Labatut
 */
public class SequenceTournamentLoader
{	
	/**
	 * Processes the main element of an XML
	 * file representing a sequence tournament.
	 * 
	 * @param folder
	 * 		Folder containing the XML file.
	 * @param root
	 * 		Root element of the tournament.
	 * @return
	 * 		The read tournament.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the XML file.
	 */
	public static SequenceTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	SequenceTournament result = new SequenceTournament();
		Element element;
		
		// limits
		element = root.getChild(XmlNames.LIMITS);
		Limits<TournamentLimit> limits = loadLimitsElement(element,folder);
		result.setLimits(limits);
		
		// matches
		element = root.getChild(XmlNames.MATCHES);
		loadMatchesElement(element,folder,result);
		return result;
	}
	
	/**
	 * Loads the XML element representing matches.
	 * 
	 * @param root
	 * 		Root XML element.
	 * @param folder
	 * 		Folder containing the XML file.
	 * @param result
	 * 		The read tournament.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the XML file.
	 */
	@SuppressWarnings("unchecked")
	private static void loadMatchesElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// matches order
    	String str = root.getAttribute(XmlNames.RANDOM_ORDER).getValue().trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomizeMatches(randomOrder);
    	// matches
    	List<Element> matches = root.getChildren(XmlNames.MATCH);
		Iterator<Element> i = matches.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadMatchElement(temp,folder,result);
		}
	}
	
	/**
	 * Loads an XML element representing a match.
	 * 
	 * @param root
	 * 		Root XML element.
	 * @param folder
	 * 		Folder containing the XML file.
	 * @param result
	 * 		The read tournament.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the XML file.
	 */
	private static void loadMatchElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Match match = TournamentLoader.loadMatchElement(root,folder,result);
		result.addMatch(match);
	}

	/**
	 * Loads an XML element representing the tournament limits.
	 * 
	 * @param root
	 * 		Root XML element.
	 * @param folder
	 * 		Folder containing the XML file.
	 * @return
	 * 		A list of limits.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 */
	@SuppressWarnings("unchecked")
	public static Limits<TournamentLimit> loadLimitsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limits<TournamentLimit> result = new Limits<TournamentLimit>();

		List<Element> elements = root.getChildren();
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			TournamentLimit limit = (TournamentLimit)LimitLoader.loadLimitElement(temp,folder);
			result.addLimit(limit);
		}
		
		return result;
	}
}
