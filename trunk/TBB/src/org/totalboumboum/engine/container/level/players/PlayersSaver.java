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
import java.text.NumberFormat;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Comment;
import org.jdom.Element;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Class able to record a {@link Players} object
 * under the form of an XML file.
 * 
 * @author Vincent Labatut
 */
public class PlayersSaver
{	
	/**
	 * Records the specified object as an XML file.
	 * 
	 * @param folder
	 * 		Folder to contain the XML file.
	 * @param players
	 * 		Players object to record.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while accessing the XML file.
	 * @throws SecurityException
	 * 		Problem while accessing the XML file.
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the XML file.
	 * @throws SAXException
	 * 		Problem while accessing the XML file.
	 * @throws IOException
	 * 		Problem while accessing the XML file.
	 * @throws IllegalAccessException
	 * 		Problem while accessing the XML file.
	 * @throws NoSuchFieldException
	 * 		Problem while accessing the XML file.
	 */
	public static void savePlayers(String folder, Players players) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = savePlayersElement(players);	
		
		// save file
		String individualFolder = folder;
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_PLAYERS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	/**
	 * Generates the players XML element.
	 * 
	 * @param players
	 * 		Original object.
	 * @return
	 * 		XML element.
	 */
	private static Element savePlayersElement(Players players)
	{	Element result = new Element(XmlNames.PLAYERS);
		
		// GPL comment
		Comment gplComment = XmlTools.getGplComment();
		result.addContent(gplComment);

		// locations
		Map<Integer,PlayerLocation[]> locations = players.getLocations();
		Element locationsElement = saveLocationsElement(locations);
		result.addContent(locationsElement);

		// items
		Map<String,Integer> items = players.getInitialItems();
		Element itemsElement = saveItemsElement(items);
		result.addContent(itemsElement);
	
		return result;
	}
 
	/**
	 * Generates the locations XML element.
	 * 
	 * @param locations
	 * 		Original object.
	 * @return
	 * 		XML element.
	 */
	private static Element saveLocationsElement(Map<Integer,PlayerLocation[]> locations)
	{	Element result = new Element(XmlNames.LOCATIONS);
		
		for(PlayerLocation[] playerLocation: locations.values())
		{	Element caseElement = saveCaseElement(playerLocation);
			result.addContent(caseElement);
		}
	
		return result;
	}
    
	/**
	 * Generates the player location XML element.
	 * 
	 * @param playerLocation
	 * 		Original object.
	 * @return
	 * 		XML element.
	 */
	private static Element saveCaseElement(PlayerLocation[] playerLocation)
	{	Element result = new Element(XmlNames.CASE);
		
		// number of players
		int nbr = playerLocation.length;
		result.setAttribute(XmlNames.PLAYERS,Integer.toString(nbr));
		
		for(int i=0;i<playerLocation.length;i++)
		{	PlayerLocation location = playerLocation[i];
			Element locationElement = saveLocationElement(location);
			result.addContent(locationElement);
		}
	
		return result;
	}
    
	/**
	 * Generates the location XML element.
	 * 
	 * @param location
	 * 		Original object.
	 * @return
	 * 		XML element.
	 */
	private static Element saveLocationElement(PlayerLocation location)
	{	Element result = new Element(XmlNames.LOCATION);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
			
		// number of players
		int player = location.getNumber();
		result.setAttribute(XmlNames.PLAYER,nf.format(player));
		
		// row
		int row = location.getRow();
		result.setAttribute(XmlNames.LINE,nf.format(row));

		// column
		int col = location.getCol();
		result.setAttribute(XmlNames.COL,nf.format(col));
	
		return result;
	}

	/**
	 * Generates the items XML element.
	 * 
	 * @param items
	 * 		Original object.
	 * @return
	 * 		XML element.
	 */
	private static Element saveItemsElement(Map<String,Integer> items)
	{	Element result = new Element(XmlNames.ITEMS);
		
		for(Entry<String,Integer> entry: items.entrySet())
		{	String name = entry.getKey();
			int nbr = entry.getValue();
			Element itemElement = saveItemElement(name,nbr);
			result.addContent(itemElement);
		}
	
		return result;
	}
    
	/**
	 * Generates the item XML element.
	 * 
	 * @param item
	 * 		Item name.
	 * @param nbr
	 * 		Number of items.
	 * @return
	 * 		XML element.
	 */
	private static Element saveItemElement(String item, int nbr)
	{	Element result = new Element(XmlNames.ITEM);
		
		// name
		result.setAttribute(XmlNames.NAME,item);
	
		// number
		result.setAttribute(XmlNames.NUMBER,Integer.toString(nbr));

		return result;
	}
}
