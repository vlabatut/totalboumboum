package fr.free.totalboumboum.engine.container.level;

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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class PlayersLoader
{	
    public static Players loadPlayers(String folder) throws ParserConfigurationException, SAXException, IOException
	{	
    	// init
		Element root;
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PLAYERS+FileTools.EXTENSION_SCHEMA);
		root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// reading
		Players result = loadPlayersElement(root);
		return result;
    }
    
    private static Players loadPlayersElement(Element root)
    {	// init
    	Players result = new Players();
    	Element element;
    	// locations
    	element = root.getChild(XmlTools.ELT_LOCATIONS);
    	loadLocationsElement(element,result);
    	// items
    	element = root.getChild(XmlTools.ELT_ITEMS);
    	loadItemsElement(element,result);
    	// result
    	return result;
    }
    
    @SuppressWarnings("unchecked")
	private static void loadLocationsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlTools.ELT_CASE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadCaseElement(temp,result);
		}
    }
    
    @SuppressWarnings("unchecked")
    private static void loadCaseElement(Element root, Players result)
    {	String valStr = root.getAttribute(XmlTools.ATT_PLAYERS).getValue().trim();
		int value = Integer.valueOf(valStr);
		PlayerLocation[] locations = new PlayerLocation[value];
		List<Element> elements = root.getChildren(XmlTools.ELT_LOCATION);
		Iterator<Element> i = elements.iterator();
		int index = 0;
		while(i.hasNext())
		{	Element temp = i.next();
			PlayerLocation pl = new PlayerLocation();
			loadLocationElement(temp,pl);
			locations[index] = pl;
			index++;
		}
		result.addLocation(value,locations);
    }
    	
    private static void loadLocationElement(Element root, PlayerLocation result)
    {	String str = root.getAttribute(XmlTools.ATT_PLAYER).getValue().trim();
		int number = Integer.valueOf(str);
		result.setNumber(number);
		str = root.getAttribute(XmlTools.ATT_COL).getValue().trim();
		int col = Integer.valueOf(str);
		result.setCol(col);
		str = root.getAttribute(XmlTools.ATT_LINE).getValue().trim();
		int line = Integer.valueOf(str);
		result.setLine(line);
    }
    
    @SuppressWarnings("unchecked")
    private static void loadItemsElement(Element root, Players result)
    {	List<Element> elements = root.getChildren(XmlTools.ELT_ITEM);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadItemElement(temp,result);
		}
    }
    
    private static void loadItemElement(Element root, Players result)
    {	String str = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    	String nbrStr = root.getAttribute(XmlTools.ATT_NUMBER).getValue().trim();
    	int number = Integer.valueOf(nbrStr);
    	for(int i=0;i<number;i++)
    		result.addInitialItem(str);
    }
    
}
