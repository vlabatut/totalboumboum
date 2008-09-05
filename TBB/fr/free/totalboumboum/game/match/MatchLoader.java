package fr.free.totalboumboum.game.match;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.point.PointProcessor;
import fr.free.totalboumboum.game.point.PointProcessorLoader;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class MatchLoader
{	
	public static Match loadMatchFromFolderPath(String folderPath) throws ParserConfigurationException, SAXException, IOException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(folderPath+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_MATCH+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Match result = loadMatchElement(root,folderPath);
		return result;
    }
    
	public static Match loadMatchFromName(String name) throws ParserConfigurationException, SAXException, IOException
	{	String individualFolder = FileTools.getMatchesPath()+File.separator+name;
		Match result = loadMatchFromFolderPath(individualFolder);
		return result;
    }

    private static Match loadMatchElement(Element root, String folderPath) throws ParserConfigurationException, SAXException, IOException
	{	// init
    	Match result = new Match();
		Element element;
		// options
		element = XmlTools.getChildElement(root,XmlTools.ELT_OPTIONS);
		loadOptionsElement(element,result);
		// levels
		element = XmlTools.getChildElement(root,XmlTools.ELT_LEVELS);
		loadLevelsElement(element,folderPath,result);
		return result;
	}		
		
    private static void loadOptionsElement(Element root, Match result)
    {	// init
    	String str;
    	// limit
    	str = root.getAttribute(XmlTools.ATT_MATCH_LIMIT).toUpperCase();
    	MatchLimit matchLimit = MatchLimit.getValueFromString(str);
    	result.setMatchLimit(matchLimit);
    	// limit value
    	str = root.getAttribute(XmlTools.ATT_MATCH_LIMIT_VALUE);
    	int matchLimitValue = Integer.valueOf(str);
    	result.setMatchLimitValue(matchLimitValue);
    }

    private static void loadLevelsElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException
    {	// level order
    	String str = root.getAttribute(XmlTools.ATT_RANDOM_ORDER).trim();
    	boolean randomOrder = Boolean.valueOf(str);
    	result.setRandomOrder(randomOrder);
    	// levels
		ArrayList<Element> elements = XmlTools.getChildElements(root,XmlTools.ELT_LEVEL);
    	Iterator<Element> i = elements.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadLevelElement(temp,folder,result);
    	}
    }
    
    private static void loadLevelElement(Element root, String folder, Match result) throws ParserConfigurationException, SAXException, IOException
    {	LevelDescription levelDescription = new LevelDescription();
    	Element element;
    	// location
    	element = XmlTools.getChildElement(root,XmlTools.ELT_LOCATION);
    	loadLocationElement(element,levelDescription);
    	// game
    	element = XmlTools.getChildElement(root,XmlTools.ELT_GAME);
    	loadGameElement(element,levelDescription);
    	// points
    	element = XmlTools.getChildElement(root,XmlTools.ELT_POINTS);
    	loadPointsElement(element,folder,levelDescription);
    	// result
    	result.addLevelDescription(levelDescription);
    }
    
    private static void loadLocationElement(Element root, LevelDescription result)
    {	String name = root.getAttribute(XmlTools.ATT_NAME).trim();
		result.setName(name);
    	String packname = root.getAttribute(XmlTools.ATT_PACKNAME).trim();
    	result.setPackname(packname);
    }
    
    private static void loadGameElement(Element root, LevelDescription result)
    {	String playModeStr = root.getAttribute(XmlTools.ATT_PLAY_MODE).toUpperCase();
    	PlayMode playMode = PlayMode.valueOf(playModeStr);
    	result.setPlayMode(playMode);
    }
    
	private static void loadPointsElement(Element root, String folder, LevelDescription result) throws ParserConfigurationException, SAXException, IOException
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
