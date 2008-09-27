package fr.free.totalboumboum.game.round;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.limit.LimitLoader;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointProcessor;
import fr.free.totalboumboum.game.points.PointProcessorLoader;
import fr.free.totalboumboum.game.round.LevelDescription;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class RoundLoader
{	
	public static Round loadRoundFromFolderPath(String folderPath, Match match) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_ROUND+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ROUND+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Round result = loadRoundElement(root,folderPath,match);
		return result;
    }
    
	public static Round loadRoundFromName(String name, Match match) throws ParserConfigurationException, SAXException, IOException
	{	String individualFolder = FileTools.getRoundsPath()+File.separator+name;
		Round result = loadRoundFromFolderPath(individualFolder,match);
		return result;
    }

    private static Round loadRoundElement(Element root, String folderPath, Match match) throws ParserConfigurationException, SAXException, IOException
	{	// init
    	Round result = new Round(match);
		Element element;
		// notes
		element = root.getChild(XmlTools.ELT_NOTES);
		ArrayList<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		// limits
		element = root.getChild(XmlTools.ELT_LIMITS);
		Limits<RoundLimit> limits = loadLimitsElement(element,folderPath);
		result.setLimits(limits);
		// gameplay
		element = root.getChild(XmlTools.ELT_GAMEPLAY);
		loadGameplayElement(element,result);
		// points
		element = root.getChild(XmlTools.ELT_POINTS);
		PointProcessor pp = PointProcessorLoader.loadPointProcessorFromElement(element,folderPath);
		result.setPointProcessor(pp);
		// level
		element = root.getChild(XmlTools.ELT_LEVEL);
		loadLevelElement(element,result);
		//
		return result;
	}		
		
    private static void loadLevelElement(Element root, Round result)
    {	LevelDescription level = new LevelDescription();
    	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	level.setName(name);
    	String packname = root.getAttribute(XmlTools.ATT_PACKNAME).getValue().trim();
    	level.setPackname(packname);
    	result.setLevelDescription(level);
    }
    
    private static void loadGameplayElement(Element root, Round result)
    {	// play mode
    	String playModeStr = root.getAttribute(XmlTools.ATT_PLAY_MODE).getValue().toUpperCase();
    	PlayMode playMode = PlayMode.valueOf(playModeStr);
    	result.setPlayMode(playMode);
    }
    
	public static Limits<RoundLimit> loadLimitsElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException
	{	Limits<RoundLimit> result = new Limits<RoundLimit>();

		List<Element> elements = root.getChildren();
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			RoundLimit limit = (RoundLimit)LimitLoader.loadLimitElement(temp,folder);
			result.addLimit(limit);
		}
		
		return result;
	}
}
