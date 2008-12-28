package fr.free.totalboumboum.configuration.game.quickstart;

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

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickStartConfigurationSaver
{	
	public static void saveQuickStartConfiguration(QuickStartConfiguration gameConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameQuickStartElement(gameConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameQuickStartElement(QuickStartConfiguration gameConfiguration)
	{	Element result = new Element(XmlTools.ELT_GAME_QUICKSTART); 
			
		// name
		Element roundElement = new Element(XmlTools.ELT_ROUND);
		String quickStart = gameConfiguration.getRoundName().toString();
		roundElement.setAttribute(XmlTools.ATT_NAME,quickStart);
		result.addContent(roundElement);
		
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickStartSelected = gameConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickStartSelected);
		result.addContent(playersElement);
		
		return result;
	}
}
