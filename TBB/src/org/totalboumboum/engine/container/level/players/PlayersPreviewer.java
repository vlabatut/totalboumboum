package org.totalboumboum.engine.container.level.players;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.tools.FileTools;
import org.totalboumboum.tools.XmlTools;
import org.xml.sax.SAXException;


public class PlayersPreviewer
{	
	private static boolean onlyAllowedPlayers;
	
    public static void loadPlayers(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// parameters
    	onlyAllowedPlayers = false;
    	// load
		loadPlayersCommon(folder,result);
    }

    public static void loadPlayersAllowed(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// parameters
    	onlyAllowedPlayers = true;
    	// load
		loadPlayersCommon(folder,result);
    }

    private static void loadPlayersCommon(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// init
		Element root;
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// reading
		loadPlayersElement(root,result);
    }

    private static void loadPlayersElement(Element root, LevelPreview result)
    {	// init
    	Element element;
    	// locations
    	element = root.getChild(XmlTools.LOCATIONS);
    	loadLocationsElement(element,result);
    	// items
    	if(!onlyAllowedPlayers)
    	{	element = root.getChild(XmlTools.ITEMS);
    		loadItemsElement(element,result);
    	}
    }
    
    @SuppressWarnings("unchecked")
	private static void loadLocationsElement(Element root, LevelPreview result)
    {	Set<Integer> allowedPlayersNumber = new TreeSet<Integer>();
    	List<Element> elements = root.getChildren(XmlTools.CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String valStr = temp.getAttribute(XmlTools.PLAYERS).getValue().trim();
			int value = Integer.valueOf(valStr);
			allowedPlayersNumber.add(value);
		}
		result.setAllowedPlayerNumbers(allowedPlayersNumber);
	}
    
    @SuppressWarnings("unchecked")
    private static void loadItemsElement(Element root, LevelPreview result)
    {	HashMap<String,Integer> initialItems = new HashMap<String, Integer>();
    	List<Element> elements = root.getChildren(XmlTools.ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String str = temp.getAttribute(XmlTools.NAME).getValue().trim();
			String nbrStr = temp.getAttribute(XmlTools.NUMBER).getValue().trim();
			int number = Integer.valueOf(nbrStr);
			initialItems.put(str,number);
		}
		result.setInitialItems(initialItems);
    }
}
