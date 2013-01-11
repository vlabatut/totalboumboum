package org.totalboumboum.game.tournament.single;

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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.TournamentLoader;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * Loads an XML file representing a league tournament.
 * 
 * @author Vincent Labatut
 */
public class SingleTournamentLoader
{
	/**
	 * Processes the main element of an XML
	 * file representing a single tournament.
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
	public static SingleTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	SingleTournament result = new SingleTournament();
		Element element;
		
		// match
		element = root.getChild(XmlNames.MATCH);
		loadMatchElement(element,folder,result);
		return result;
	}
	
	/**
	 * Loads the XML element representing the only match
	 * in this tournament.
	 * @param root
	 * 		Root element of the tournament.
	 * @param folder
	 * 		Folder containing the XML file.
	 * @param result
	 * 		The concerned resulting tournament.
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
	private static void loadMatchElement(Element root, String folder, SingleTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Match match = TournamentLoader.loadMatchElement(root,folder,result);
		result.setMatch(match);
	}
}
