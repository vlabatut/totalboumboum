package org.totalboumboum.engine.container.level.players;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.engine.player.PlayerLocation;
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
public class PlayersSaver
{	
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

	private static Element savePlayersElement(Players players)
	{	Element result = new Element(XmlNames.PLAYERS);
		
		// locations
		HashMap<Integer,PlayerLocation[]> locations = players.getLocations();
		Element locationsElement = saveLocationsElement(locations);
		result.addContent(locationsElement);

		// items
		HashMap<String,Integer> items = players.getInitialItems();
		Element itemsElement = saveItemsElement(items);
		result.addContent(itemsElement);
	
		return result;
	}
 
	private static Element saveLocationsElement(HashMap<Integer,PlayerLocation[]> locations)
	{	Element result = new Element(XmlNames.LOCATIONS);
		
		for(PlayerLocation[] playerLocation: locations.values())
		{	Element caseElement = saveCaseElement(playerLocation);
			result.addContent(caseElement);
		}
	
		return result;
	}
    
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
    
	private static Element saveLocationElement(PlayerLocation location)
	{	Element result = new Element(XmlNames.LOCATION);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
			
		// number of players
		int player = location.getNumber();
		result.setAttribute(XmlNames.PLAYER,nf.format(player));
		
		// line
		int line = location.getLine();
		result.setAttribute(XmlNames.LINE,nf.format(line));

		// column
		int col = location.getCol();
		result.setAttribute(XmlNames.COL,nf.format(col));
	
		return result;
	}

	private static Element saveItemsElement(HashMap<String,Integer> items)
	{	Element result = new Element(XmlNames.ITEMS);
		
		for(Entry<String,Integer> entry: items.entrySet())
		{	String name = entry.getKey();
			int nbr = entry.getValue();
			Element itemElement = saveItemElement(name,nbr);
			result.addContent(itemElement);
		}
	
		return result;
	}
    
	private static Element saveItemElement(String item, int nbr)
	{	Element result = new Element(XmlNames.ITEM);
		
		// name
		result.setAttribute(XmlNames.NAME,item);
	
		// number
		result.setAttribute(XmlNames.NUMBER,Integer.toString(nbr));

		return result;
	}
}
