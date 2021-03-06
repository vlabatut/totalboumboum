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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Class used to load Players objects from XML files.
 * 
 * @author Vincent Labatut
 */
public class PlayersLoader
{
	/**
	 * Loads the player data contained in the specified folder.
	 * 
	 * @param folder
	 * 		Location of the XML file.
	 * @return
	 * 		Loaded object.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 */
    public static Players loadPlayers(String folder) throws ParserConfigurationException, SAXException, IOException
	{	
    	/* 
		 * NOTE tester ici si le level est suffisamment grand
		 * NOTE faut qu'il y ait au moins une config, à définir dans XSD
		 * attention, le numéro des joueurs ne doit pas dépasser maxPlayer-1
		 * NOTE il faut tester qu'il y a bien autant de locations de que de players indiqué dans la situation
		 */

		/*
		 * TODO si le level ne supporte pas explicitement le nbre n de joueurs voulu,
		 * on prend la config pour la taille au dessus, et on utilise les n premières
		 * positions définies
		 */

    	// init
		Element root;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// reading
		Players result = loadPlayersElement(root);
		return result;
    }
    
    /**
     * Processes the players XML element.
     * 
     * @param root
     * 		XML element.
     * @return
     * 		Loaded Players object.
     */
    private static Players loadPlayersElement(Element root)
    {	// init
    	Players result = new Players();
    	Element element;
    	
    	// locations
    	element = root.getChild(XmlNames.LOCATIONS);
    	loadLocationsElement(element,result);
    	
    	// items
    	element = root.getChild(XmlNames.ITEMS);
    	loadItemsElement(element,result);

    	return result;
    }
    
    /**
     * Processes the locations XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    @SuppressWarnings("unchecked")
	private static void loadLocationsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlNames.CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadCaseElement(temp,result);
		}
    }
    
    /**
     * Processes the case XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    @SuppressWarnings("unchecked")
    private static void loadCaseElement(Element root, Players result)
    {	// player count
    	String valStr = root.getAttribute(XmlNames.PLAYERS).getValue().trim();
		int value = Integer.valueOf(valStr);
		PlayerLocation[] locations = new PlayerLocation[value];
		
		// location
		List<Element> elements = root.getChildren(XmlNames.LOCATION);
		Iterator<Element> i = elements.iterator();
		int index = 0;
		while(i.hasNext())
		{	Element temp = i.next();
			PlayerLocation pl = new PlayerLocation();
			loadLocationElement(temp,pl);
			locations[index] = pl;
			index++;
		}
		result.addLocation(locations);
    }
    
    /**
     * Processes the location XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    private static void loadLocationElement(Element root, PlayerLocation result)
    {	// player number
    	String str = root.getAttribute(XmlNames.PLAYER).getValue().trim();
		int number = Integer.valueOf(str);
		result.setNumber(number);
		
		// column
		str = root.getAttribute(XmlNames.COL).getValue().trim();
		int col = Integer.valueOf(str);
		result.setCol(col);
		
		// row
		str = root.getAttribute(XmlNames.LINE).getValue().trim();
		int row = Integer.valueOf(str);
		result.setRow(row);
    }
    
    /**
     * Processes the items XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    @SuppressWarnings("unchecked")
    private static void loadItemsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlNames.ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadItemElement(temp,result);
		}
    }
    
    /**
     * Processes the item XML element.
     * 
     * @param root
     * 		XML element.
     * @param result
     * 		Object to complete.
     */
    private static void loadItemElement(Element root, Players result)
    {	// name
    	String str = root.getAttribute(XmlNames.NAME).getValue().trim();
    	
    	// number
    	String nbrStr = root.getAttribute(XmlNames.NUMBER).getValue().trim();
    	int number = Integer.valueOf(nbrStr);
    	
    	// create items
    	for(int i=0;i<number;i++)
    		result.addInitialItem(str);
    }
}
