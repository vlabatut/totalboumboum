package fr.free.totalboumboum.game.tournament.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.point.PointProcessorLoader;
import fr.free.totalboumboum.tools.XmlTools;



public class SequenceTournamentLoader
{	
	public static SequenceTournament loadTournamentElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException
	{	SequenceTournament result = new SequenceTournament();
		Element element;
		// matches
		element = XmlTools.getChildElement(root,XmlTools.ELT_MATCHES);
		loadMatchesElement(element,folder,result);
		return result;
	}
	
	private static void loadMatchesElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException
	{	ArrayList<Element> matches = XmlTools.getChildElements(root, XmlTools.ELT_MATCH);
		Iterator<Element> i = matches.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadMatchElement(temp,folder,result);
		}
	}
		
	private static void loadMatchElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException
    {	Match match;
		Element element;
		// location
		element = XmlTools.getChildElement(root,XmlTools.ELT_LOCATION);
		match = loadLocationElement(element,folder);
		// points
		element = XmlTools.getChildElement(root,XmlTools.ELT_POINTS);
		loadPointsElement(element,folder,match);
		// result
		result.addMatch(match);
	}

	private static Match loadLocationElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Match result;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME);
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			result = MatchLoader.loadMatchFromFolderPath(folder);
		}
		else
			result = MatchLoader.loadMatchFromName(name);
		return result;
	}
	
	private static void loadPointsElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException
	{	PointProcessor pp;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME);
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			pp = PointProcessorLoader.loadPointProcessorFromFilePath(folder);
		}
		else
			pp = PointProcessorLoader.loadPointProcessorFromName(name);
		result.setPointProcessor(pp);
	}
}
