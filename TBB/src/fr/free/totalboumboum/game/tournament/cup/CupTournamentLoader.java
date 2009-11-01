package fr.free.totalboumboum.game.tournament.cup;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsProcessorLoader;
import fr.free.totalboumboum.game.points.PointsRankings;
import fr.free.totalboumboum.game.tournament.TournamentLoader;
import fr.free.totalboumboum.tools.XmlTools;

public class CupTournamentLoader
{
	public static CupTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupTournament result = new CupTournament();
		Element element;
		
		// sort players
		String sortPlayersStr = root.getAttribute(XmlTools.ATT_SORT_PLAYERS).getValue().trim();
		CupPlayerSort sortPlayers = CupPlayerSort.valueOf(sortPlayersStr);
    	result.setSortPlayers(sortPlayers);
    	
		// legs
		element = root;
		loadLegsElement(element,folder,result);

		return result;
	}

	@SuppressWarnings("unchecked")
	private static void loadLegsElement(Element root, String folder, CupTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	List<Element> legs = root.getChildren(XmlTools.ELT_LEG);
		Iterator<Element> i = legs.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupLeg leg = loadLegElement(temp,folder,result);
			result.addLeg(leg);
		}
	}

	@SuppressWarnings("unchecked")
	private static CupLeg loadLegElement(Element root, String folder, CupTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupLeg result = new CupLeg(tournament);
	
		// randomize parts
		String randomizePartsStr = root.getAttribute(XmlTools.ATT_RANDOMIZE_PARTS).getValue().trim();
		boolean randomizeParts = Boolean.valueOf(randomizePartsStr);
    	result.setRandomizeParts(randomizeParts);

		// number
		String numberStr = root.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
		int number = Integer.valueOf(numberStr);
		result.setNumber(number);
		
		// parts
		List<Element> parts = root.getChildren(XmlTools.ELT_PART);
		Iterator<Element> i = parts.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupPart part = loadPartElement(temp,folder,result);
			result.addPart(part);
		}
		
		return result;
	}
	
	private static CupPart loadPartElement(Element root, String folder, CupLeg leg) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupPart result = new CupPart(leg);
	
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);

		// number
		String numberStr = root.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
		int number = Integer.valueOf(numberStr);
		result.setNumber(number);

		// rank
		String rankStr = root.getAttribute(XmlTools.ATT_RANK).getValue().trim();
		int rank = Integer.valueOf(rankStr);
		result.setRank(rank);

		// match
		Element matchElt = root.getChild(XmlTools.ELT_MATCH);
		Match match = TournamentLoader.loadMatchElement(matchElt,folder,leg.getTournament());
		result.setMatch(match);
		
		// tie break
		Element tieBreakElt = root.getChild(XmlTools.ELT_TIE_BREAK);
		CupTieBreak tieBreak = loadTieBreakElement(tieBreakElt,folder,result);
		result.setTieBreak(tieBreak);
		
		// players
		Element playersElt = root.getChild(XmlTools.ELT_PLAYERS);
		loadPlayersElement(playersElt,folder,result);
		
		return result;
	}
	
	private static CupTieBreak loadTieBreakElement(Element root, String folder, CupPart part) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	CupTieBreak result = new CupTieBreak(part);
	
		// match
		Element matchElt = root.getChild(XmlTools.ELT_MATCH);
		Match match = TournamentLoader.loadMatchElement(matchElt,folder,part.getTournament());
		result.setMatch(match);
		
		// tie break
		Element rankingsElt = root.getChild(XmlTools.ELT_RANKINGS);
		PointsRankings rankings = new PointsRankings(new ArrayList<PointsProcessor>(),false);
		if(rankingsElt!=null)
			rankings = (PointsRankings)PointsProcessorLoader.loadGeneralPointElement(rankingsElt);
		result.setPointsRankings(rankings);
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private static void loadPlayersElement(Element root, String folder, CupPart result)
	{	List<Element> parts = root.getChildren(XmlTools.ELT_PLAYER);
		Iterator<Element> i = parts.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			CupPlayer player = loadPlayerElement(temp);
			result.addPlayer(player);
		}
	}
	
	private static CupPlayer loadPlayerElement(Element root)
	{	CupPlayer result = new CupPlayer();
	
		// number
		String partStr = root.getAttribute(XmlTools.ATT_PART).getValue().trim();
		int part = Integer.valueOf(partStr);
		result.setPart(part);

		// rank
		String rankStr = root.getAttribute(XmlTools.ATT_RANK).getValue().trim();
		int rank = Integer.valueOf(rankStr);
		result.setRank(rank);

		return result;
	}
}
