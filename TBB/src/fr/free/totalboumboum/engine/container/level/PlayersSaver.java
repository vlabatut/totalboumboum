package fr.free.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class PlayersSaver
{	
	public static void savePlayers(String folder, Players players) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = savePlayersElement(players);	
		// save file
		String individualFolder = folder;
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element savePlayersElement(Players players)
	{	Element result = new Element(XmlTools.PLAYERS);
		
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
	{	Element result = new Element(XmlTools.LOCATIONS);
		
		for(PlayerLocation[] playerLocation: locations.values())
		{	Element caseElement = saveCaseElement(playerLocation);
			result.addContent(caseElement);
		}
	
		return result;
	}
    
	private static Element saveCaseElement(PlayerLocation[] playerLocation)
	{	Element result = new Element(XmlTools.CASE);
		
		// number of players
		int nbr = playerLocation.length;
		result.setAttribute(XmlTools.PLAYERS,Integer.toString(nbr));
		
		for(int i=0;i<playerLocation.length;i++)
		{	PlayerLocation location = playerLocation[i];
			Element locationElement = saveLocationElement(location);
			result.addContent(locationElement);
		}
	
		return result;
	}
    
	private static Element saveLocationElement(PlayerLocation location)
	{	Element result = new Element(XmlTools.LOCATION);
		
		// number of players
		int player = location.getNumber();
		result.setAttribute(XmlTools.PLAYER,Integer.toString(player));
		
		// line
		int line = location.getLine();
		result.setAttribute(XmlTools.LINE,Integer.toString(line));

		// column
		int col = location.getCol();
		result.setAttribute(XmlTools.COL,Integer.toString(col));
	
		return result;
	}

	private static Element saveItemsElement(HashMap<String,Integer> items)
	{	Element result = new Element(XmlTools.ITEMS);
		
		for(Entry<String,Integer> entry: items.entrySet())
		{	String name = entry.getKey();
			int nbr = entry.getValue();
			Element itemElement = saveItemElement(name,nbr);
			result.addContent(itemElement);
		}
	
		return result;
	}
    
	private static Element saveItemElement(String item, int nbr)
	{	Element result = new Element(XmlTools.ITEM);
		
		// name
		result.setAttribute(XmlTools.NAME,item);
	
		// number
		result.setAttribute(XmlTools.NUMBER,Integer.toString(nbr));

		return result;
	}
}
