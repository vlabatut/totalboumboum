package org.totalboumboum.game.tournament.single;

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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.tournament.TournamentLoader;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class SingleTournamentLoader
{
	public static SingleTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	SingleTournament result = new SingleTournament();
		Element element;
		
		// match
		element = root.getChild(XmlNames.MATCH);
		loadMatchElement(element,folder,result);
		return result;
	}
	
	private static void loadMatchElement(Element root, String folder, SingleTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Match match = TournamentLoader.loadMatchElement(root,folder,result);
		result.setMatch(match);
	}
}
