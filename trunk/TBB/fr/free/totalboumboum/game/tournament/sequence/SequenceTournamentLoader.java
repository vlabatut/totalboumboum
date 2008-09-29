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
import fr.free.totalboumboum.game.limit.LimitLoader;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.limit.TournamentLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsProcessorLoader;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.tools.XmlTools;



public class SequenceTournamentLoader
{	
	public static SequenceTournament loadTournamentElement(String folder, Element root, Configuration configuration) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	SequenceTournament result = new SequenceTournament(configuration);
		Element element;
		// limits
		element = root.getChild(XmlTools.ELT_LIMITS);
		Limits<TournamentLimit> limits = loadLimitsElement(element,folder);
		result.setLimits(limits);
		// points
		element = root.getChild(XmlTools.ELT_POINTS);
		PointsProcessor pp = PointsProcessorLoader.loadPointProcessorFromElement(element,folder);
		result.setPointProcessor(pp);
		// matches
		element = root.getChild(XmlTools.ELT_MATCHES);
		loadMatchesElement(element,folder,result);
		return result;
	}
	
	private static void loadMatchesElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// matches order
    	String str = root.getAttribute(XmlTools.ATT_RANDOM_ORDER).getValue().trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomOrder(randomOrder);
    	// matches
    	List<Element> matches = root.getChildren(XmlTools.ELT_MATCH);
		Iterator<Element> i = matches.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadMatchElement(temp,folder,result);
		}
	}
		
	private static void loadMatchElement(Element root, String folder, SequenceTournament result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Match match;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			match = MatchLoader.loadMatchFromFolderPath(folder,result);
		}
		else
			match = MatchLoader.loadMatchFromName(name,result);
		result.addMatch(match);
	}

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
