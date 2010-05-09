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

public class GameConfigurationSaver
{	
	public static void saveGameConfiguration(GameConfiguration gameConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameElement(gameConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameElement(GameConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_GAME); 
		// quick match
		Element matchElement = saveQuickmatchElement(gameConfiguration);
		result.addContent(matchElement);
		// quick start round
		Element roundElement = saveQuickstartElement(gameConfiguration);
		result.addContent(roundElement);
		//
		return result;
	}
	
	private static Element saveQuickmatchElement(GameConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_QUICKMATCH);
		String quickmatch = gameConfiguration.getQuickmatchName();
		result.setAttribute(XmlTools.ATT_VALUE,quickmatch);
		return result;
	}
	
	private static Element saveQuickstartElement(GameConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_QUICKSTART);
		String quickround = gameConfiguration.getQuickstartName();
		result.setAttribute(XmlTools.ATT_VALUE,quickround);
		return result;
	}
}
