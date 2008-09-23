package fr.free.totalboumboum.game.tournament.sequence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.LimitsLoader;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.point.PointProcessorLoader;
import fr.free.totalboumboum.tools.XmlTools;



public class SequenceTournamentLoader
{	
	public static SequenceTournament loadTournamentElement(String folder, Element root, Configuration configuration) throws ParserConfigurationException, SAXException, IOException
	{	SequenceTournament result = new SequenceTournament(configuration);
		Element element;
		// options
		element = root.getChild(XmlTools.ELT_OPTIONS);
		loadOptionsElement(element,folder,result);
		// matches
		element = root.getChild(XmlTools.ELT_MATCHES);
		loadMatchesElement(element,folder,result);
		return result;
	}
	
	private static void loadOptionsElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException
	{	Element element;
		// options
		element = root.getChild(XmlTools.ELT_POINTS);
		PointProcessor pp = loadPointsElement(element,folder);
		result.setPointProcessor(pp);
		// limits
    	element = root.getChild(XmlTools.ELT_LIMITS);
    	Limits matchLimits = LimitsLoader.loadLimitsElement(element,folder);
    	result.setLimits(matchLimits);
	}
	
	private static void loadMatchesElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException
	{	List<Element> matches = root.getChildren(XmlTools.ELT_MATCH);
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
		element = root.getChild(XmlTools.ELT_LOCATION);
		match = loadLocationElement(element,folder,result);
		// points
		element = root.getChild(XmlTools.ELT_POINTS);
		PointProcessor pp = loadPointsElement(element,folder);
		match.setPointProcessor(pp);
		// result
		result.addMatch(match);
	}

	private static Match loadLocationElement(Element root, String folder, SequenceTournament tournament) throws ParserConfigurationException, SAXException, IOException
	{	Match result;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			result = MatchLoader.loadMatchFromFolderPath(folder,tournament);
		}
		else
			result = MatchLoader.loadMatchFromName(name,tournament);
		return result;
	}
	
	private static PointProcessor loadPointsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	PointProcessor result;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			result = PointProcessorLoader.loadPointProcessorFromFilePath(folder);
		}
		else
			result = PointProcessorLoader.loadPointProcessorFromName(name);
		return result;
	}
}
