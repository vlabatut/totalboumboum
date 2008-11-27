package fr.free.totalboumboum.configuration.game;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class GameConfigurationLoader
{	
	public static GameConfiguration loadGameConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	GameConfiguration result = new GameConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameElement(root,result);
		return result;
	}

	private static void loadGameElement(Element root, GameConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Element element; 
		// round for quick start
		element = root.getChild(XmlTools.ELT_QUICKSTART);
		loadQuickstartElement(element,result);
		// quick match
		element = root.getChild(XmlTools.ELT_QUICKMATCH);
		loadQuickmatchElement(element,result);
	}
	
	private static void loadQuickmatchElement(Element root, GameConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// name
		String quickmatchName = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setQuickmatchName(quickmatchName);
		// use last players
		boolean useLastPlayers = Boolean.parseBoolean(root.getAttributeValue(XmlTools.ATT_USE_LAST_PLAYERS));
		result.setUseLastPlayers(useLastPlayers);
	}

	private static void loadQuickstartElement(Element root, GameConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String quickstartName = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setQuickstartName(quickstartName);
	}
}
