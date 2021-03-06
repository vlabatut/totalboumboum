package org.totalboumboum.engine.container.level.players;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.container.level.preview.LevelPreview;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Class used to preview player-related info from a level.
 * It loads only the relevent parts of the XML file, for speed matters.
 *  
 * @author Vincent Labatut
 */
public class PlayersPreviewer
{	/** Class id */
	private static boolean onlyAllowedPlayers;
	
	/**
	 * Normally loads all the player-related data.
	 * 
	 * @param folder
	 * 		XML file folder.
	 * @param result
	 * 		Loaded object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the file.
	 * @throws SAXException
	 * 		Problem while accessing the file.
	 * @throws IOException
	 * 		Problem while accessing the file.
	 */
    public static void loadPlayers(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// parameters
    	onlyAllowedPlayers = false;
    	// load
		loadPlayersCommon(folder,result);
    }

	/**
	 * Only loads the allowed player numbers.
	 * 
	 * @param folder
	 * 		XML file folder.
	 * @param result
	 * 		Loaded object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the file.
	 * @throws SAXException
	 * 		Problem while accessing the file.
	 * @throws IOException
	 * 		Problem while accessing the file.
	 */
   public static void loadPlayersAllowed(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// parameters
    	onlyAllowedPlayers = true;
    	// load
		loadPlayersCommon(folder,result);
    }

   /**
    * Loads player-related data.
    * 
	 * @param folder
	 * 		XML file folder.
	 * @param result
	 * 		Loaded object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the file.
	 * @throws SAXException
	 * 		Problem while accessing the file.
	 * @throws IOException
	 * 		Problem while accessing the file.
    */
    private static void loadPlayersCommon(String folder, LevelPreview result) throws ParserConfigurationException, SAXException, IOException
	{	// init
		Element root;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// reading
		loadPlayersElement(root,result);
    }
    
    /**
     * Process the players XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    private static void loadPlayersElement(Element root, LevelPreview result)
    {	// init
    	Element element;
    	// locations
    	element = root.getChild(XmlNames.LOCATIONS);
    	loadLocationsElement(element,result);
    	// items
    	if(!onlyAllowedPlayers)
    	{	element = root.getChild(XmlNames.ITEMS);
    		loadItemsElement(element,result);
    	}
    }
    
    /**
     * Process the locations XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    @SuppressWarnings("unchecked")
	private static void loadLocationsElement(Element root, LevelPreview result)
    {	Set<Integer> allowedPlayersNumber = new TreeSet<Integer>();
    	List<Element> elements = root.getChildren(XmlNames.CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String valStr = temp.getAttribute(XmlNames.PLAYERS).getValue().trim();
			int value = Integer.valueOf(valStr);
			allowedPlayersNumber.add(value);
		}
		result.setAllowedPlayerNumbers(allowedPlayersNumber);
	}
    
    /**
     * Process the items XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    @SuppressWarnings("unchecked")
    private static void loadItemsElement(Element root, LevelPreview result)
    {	Map<String,Integer> initialItems = new HashMap<String, Integer>();
    	List<Element> elements = root.getChildren(XmlNames.ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			String str = temp.getAttribute(XmlNames.NAME).getValue().trim();
			String nbrStr = temp.getAttribute(XmlNames.NUMBER).getValue().trim();
			int number = Integer.valueOf(nbrStr);
			initialItems.put(str,number);
		}
		result.setInitialItems(initialItems);
    }
}
