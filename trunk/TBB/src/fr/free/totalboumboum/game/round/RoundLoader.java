package fr.free.totalboumboum.game.round;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.game.limit.LimitLoader;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.match.MatchLoader;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.points.PointsProcessorLoader;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class RoundLoader
{	
	public static Round loadRoundFromFolderPath(String folderPath, Match match) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
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
    
	public static Round loadRoundFromName(String name, Match match) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FileTools.getRoundsPath()+File.separator+name;
		Round result = loadRoundFromFolderPath(individualFolder,match);
		return result;
    }

    private static Round loadRoundElement(Element root, String folderPath, Match match) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
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
		PointsProcessor pp = PointsProcessorLoader.loadPointProcessorFromElement(element,folderPath);
		result.setPointProcessor(pp);
		// level
		element = root.getChild(XmlTools.ELT_LEVEL);
		loadLevelElement(element,result);
		//
		return result;
	}		
		
    private static void loadLevelElement(Element root, Round result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	String packname = root.getAttribute(XmlTools.ATT_PACKNAME).getValue().trim();
    	String folder = packname+File.separator+name;
    	HollowLevel hollowLevel = new HollowLevel(folder); 
    	result.setHollowLevel(hollowLevel);
    }
    
    private static void loadGameplayElement(Element root, Round result)
    {	// play mode
    	String playModeStr = root.getAttribute(XmlTools.ATT_PLAY_MODE).getValue().toUpperCase();
    	PlayMode playMode = PlayMode.valueOf(playModeStr);
    	result.setPlayMode(playMode);
    }
    
	@SuppressWarnings("unchecked")
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
