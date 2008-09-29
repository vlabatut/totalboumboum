package fr.free.totalboumboum.game.match;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.limit.Limit;
import fr.free.totalboumboum.game.limit.LimitLoader;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsProcessorLoader;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.round.RoundLoader;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class MatchLoader
{	
	public static Match loadMatchFromFolderPath(String folderPath, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Match result = loadMatchElement(root,folderPath,tournament);
		return result;
    }
    
	public static Match loadMatchFromName(String name, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FileTools.getMatchesPath()+File.separator+name;
		Match result = loadMatchFromFolderPath(individualFolder,tournament);
		return result;
    }

    private static Match loadMatchElement(Element root, String folderPath, AbstractTournament tournament) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
    	Match result = new Match(tournament);
		Element element;
		// notes
		element = root.getChild(XmlTools.ELT_NOTES);
		ArrayList<String> notes = loadNotesElement(element);
		result.setNotes(notes);
		// limits
		element = root.getChild(XmlTools.ELT_LIMITS);
		Limits<MatchLimit> limits = loadLimitsElement(element,folderPath);
		result.setLimits(limits);
		// points
		element = root.getChild(XmlTools.ELT_POINTS);
		PointsProcessor pp = PointsProcessorLoader.loadPointProcessorFromElement(element,folderPath);
		result.setPointProcessor(pp);
		// rounds
		element = root.getChild(XmlTools.ELT_ROUNDS);
		loadRoundsElement(element,folderPath,result);
		return result;
	}		
		
    public static ArrayList<String> loadNotesElement(Element root)
    {	ArrayList<String> result = new ArrayList<String>();
    	List<Element> lines = root.getChildren(XmlTools.ELT_LINE);
    	Iterator<Element> i = lines.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		String line = temp.getAttributeValue(XmlTools.ATT_VALUE).trim();
    		result.add(line);
    	}
    	return result;
    }

    private static void loadRoundsElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// rounds order
    	String str = root.getAttribute(XmlTools.ATT_RANDOM_ORDER).getValue().trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomOrder(randomOrder);
    	// rounds
		List<Element> elements = root.getChildren(XmlTools.ELT_ROUND);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadRoundElement(temp,folder,result);
    	}
    }
    
    private static void loadRoundElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Round round;
		// local
		String localStr = root.getAttribute(XmlTools.ATT_LOCAL).getValue().trim();
		boolean local = Boolean.valueOf(localStr);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// loading
		if(local)
		{	folder = folder+File.separator+name;
			round = RoundLoader.loadRoundFromFolderPath(folder,result);
		}
		else
			round = RoundLoader.loadRoundFromName(name,result);
		result.addRound(round);
	}
    
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
