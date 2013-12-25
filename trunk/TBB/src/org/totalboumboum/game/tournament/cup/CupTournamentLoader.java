package org.totalboumboum.game.tournament.cup;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.game.points.PointsProcessorLoader;
import org.totalboumboum.game.points.PointsProcessorRankings;
import org.totalboumboum.game.tournament.TournamentLoader;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * Loads an XML file representing a cup tournament.
 * 
 * @author Vincent Labatut
 */
public class CupTournamentLoader
{	
	/**
	 * Processes the main element of an XML
	 * file representing a cup tournament.
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
	public static CupTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupTournament result = new CupTournament();
		Element element;
		
		// randomize players
		String sortPlayersStr = root.getAttribute(XmlNames.SORT_PLAYERS).getValue().trim();
		CupPlayerSort sortPlayers = CupPlayerSort.valueOf(sortPlayersStr);
    	result.setSortPlayers(sortPlayers);
    	
		// legs
		element = root;
		loadLegsElement(element,folder,result);

		return result;
	}

	/**
	 * Reads all the XML elements representing cup legs.
	 * 
	 * @param root
	 * 		The main element of the list of legs.
	 * @param folder
	 * 		The folder containing the XML file.
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
	@SuppressWarnings("unchecked")
	private static void loadLegsElement(Element root, String folder, CupTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	List<Element> legs = root.getChildren(XmlNames.LEG);
		Iterator<Element> i = legs.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupLeg leg = loadLegElement(temp,folder,result);
			result.addLeg(leg);
		}
	}

	/**
	 * Reads an XML element representing a cup leg.
	 * 
	 * @param root
	 * 		The main leg element.
	 * @param folder
	 * 		The folder containing the XML file.
	 * @param tournament
	 * 		The concerned resulting tournament.
	 * @return
	 * 		The read leg.
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
	private static CupLeg loadLegElement(Element root, String folder, CupTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupLeg result = new CupLeg(tournament);
	
		// randomize parts
		String randomizePartsStr = root.getAttribute(XmlNames.RANDOMIZE_PARTS).getValue().trim();
		boolean randomizeParts = Boolean.valueOf(randomizePartsStr);
    	result.setRandomizeParts(randomizeParts);

		// number
		String numberStr = root.getAttribute(XmlNames.NUMBER).getValue().trim();
		int number = Integer.valueOf(numberStr);
		result.setNumber(number);
		
		// parts
		List<Element> parts = root.getChildren(XmlNames.PART);
		Iterator<Element> i = parts.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupPart part = loadPartElement(temp,folder,result);
			result.addPart(part);
		}
		
		return result;
	}
	
	/**
	 * Reads an XML element representing a cup part.
	 * 
	 * @param root
	 * 		The main leg element.
	 * @param folder
	 * 		The folder containing the XML file.
	 * @param leg
	 * 		The leg containing the read part.
	 * @return
	 * 		The read part.
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
	private static CupPart loadPartElement(Element root, String folder, CupLeg leg) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupPart result = new CupPart(leg);
	
		// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);

		// number
		String numberStr = root.getAttribute(XmlNames.NUMBER).getValue().trim();
		int number = Integer.valueOf(numberStr);
		result.setNumber(number);

		// rank
		String rankStr = root.getAttribute(XmlNames.RANK).getValue().trim();
		int rank = Integer.valueOf(rankStr);
		result.setRank(rank);

		// match
		Element matchElt = root.getChild(XmlNames.MATCH);
		Match match = TournamentLoader.loadMatchElement(matchElt,folder,leg.getTournament());
		result.setMatch(match);
		if(name!=null)
			match.setName(name);
		
		// tie break
		Element tieBreakElt = root.getChild(XmlNames.TIE_BREAK);
		CupTieBreak tieBreak = loadTieBreakElement(tieBreakElt,folder,result);
		result.setTieBreak(tieBreak);
		
		// players
		Element playersElt = root.getChild(XmlNames.PLAYERS);
		loadPlayersElement(playersElt,folder,result);
		
		return result;
	}
	
	/**
	 * Reads an XML element representing a cup tie break.
	 * 
	 * @param root
	 * 		The main leg element.
	 * @param folder
	 * 		The folder containing the XML file.
	 * @param part
	 * 		The part containing the read part.
	 * @return
	 * 		The read tie break.
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
	private static CupTieBreak loadTieBreakElement(Element root, String folder, CupPart part) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupTieBreak result = new CupTieBreak(part);
	
		// match
		Element matchElt = root.getChild(XmlNames.MATCH);
		Match match = TournamentLoader.loadMatchElement(matchElt,folder,part.getTournament());
		result.setMatch(match);
		
		// tie break
		Element rankingsElt = root.getChild(XmlNames.RANKINGS);
		PointsProcessorRankings rankings = new PointsProcessorRankings(new ArrayList<AbstractPointsProcessor>(),false);
		if(rankingsElt!=null)
			rankings = (PointsProcessorRankings)PointsProcessorLoader.loadGeneralPointElement(rankingsElt);
		result.setPointsRankings(rankings);
		
		return result;
	}
	
	/**
	 * Reads an XML element representing a list of cup players.
	 * 
	 * @param root
	 * 		The main leg element.
	 * @param folder
	 * 		The folder containing the XML file.
	 * @param result
	 * 		The part containing the read players.
	 */
	@SuppressWarnings("unchecked")
	private static void loadPlayersElement(Element root, String folder, CupPart result)
	{	List<Element> parts = root.getChildren(XmlNames.PLAYER);
		Iterator<Element> i = parts.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupPlayer player = loadPlayerElement(temp, result);
			result.addPlayer(player);
		}
	}

	/**
	 * Reads an XML element representing a cup player.
	 * 
	 * @param root
	 * 		The main leg element.
	 * @param part
	 * 		The part containing the read player.
	 * @return
	 * 		The read player.
	 */
	private static CupPlayer loadPlayerElement(Element root, CupPart part)
	{	CupPlayer result = new CupPlayer(part);
	
		// rank
		String rankStr = root.getAttribute(XmlNames.RANK).getValue().trim();
		int prevRank = Integer.valueOf(rankStr);
		result.setPrevRank(prevRank);

		// part
		String partStr = root.getAttribute(XmlNames.PART).getValue().trim();
		int prevPart = Integer.valueOf(partStr);
		result.setPrevPart(prevPart);

		// leg
		int prevLeg;
		Attribute legAttr = root.getAttribute(XmlNames.LEG);
		if(legAttr!=null)
		{	String legStr = legAttr.getValue().trim();
			prevLeg = Integer.valueOf(legStr);
		}
		else
		{	CupLeg previousLeg = part.getLeg().getPreviousLeg();
			if(previousLeg==null)
				prevLeg = -1;
			else
				prevLeg = previousLeg.getNumber();
		}
		result.setPrevLeg(prevLeg);
		
		return result;
	}
}
