package org.totalboumboum.game.round;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.hollow.HollowLevelLoader;
import org.totalboumboum.game.limit.LimitLoader;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.match.MatchLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundLoader
{	
	public static Round loadRoundFromFolderPath(String folderPath, Match match) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(folderPath+File.separator+FileNames.FILE_ROUND+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ROUND+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		Round result = new Round(match);
		File temp = new File(folderPath);
		String name = temp.getName();
		result.setName(name);
		loadRoundElement(root,folderPath,result);
		
		return result;
    }
    
	public static Round loadRoundFromName(String name, Match match) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = FilePaths.getRoundsPath()+File.separator+name;
		Round result = loadRoundFromFolderPath(individualFolder,match);
		return result;
    }

    private static void loadRoundElement(Element root, String folderPath, Round result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
    	Element element;
		
		// notes
		element = root.getChild(XmlNames.NOTES);
		List<String> notes = MatchLoader.loadNotesElement(element);
		result.setNotes(notes);
		
		// author
		element = root.getChild(XmlNames.AUTHOR);
		if(element!=null)
		{	String author = element.getAttributeValue(XmlNames.VALUE);
			result.setAuthor(author);
		}
		
		// limits
		element = root.getChild(XmlNames.LIMITS);
		Limits<RoundLimit> limits = loadLimitsElement(element,folderPath);
		result.setLimits(limits);
		
		// level
		element = root.getChild(XmlNames.LEVEL);
		loadLevelElement(element,result);
	}		
		
    private static void loadLevelElement(Element root, Round result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// random locations
    	String randomLocationStr = root.getAttributeValue(XmlNames.RANDOM_LOCATION).trim();
    	boolean randomLocation = Boolean.parseBoolean(randomLocationStr);
    	result.setRandomLocation(randomLocation);
    	
    	// name
    	String name = root.getAttribute(XmlNames.NAME).getValue().trim();
    	// packname
    	String packname = root.getAttribute(XmlNames.PACKNAME).getValue().trim();
    	// load
    	HollowLevel hollowLevel = HollowLevelLoader.loadHollowLevel(packname,name); 
    	result.setHollowLevel(hollowLevel);
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
