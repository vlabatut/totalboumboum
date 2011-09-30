package fr.free.totalboumboum.configuration.game.quickstart;

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
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.ProfilesSelection;
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionSaver;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickStartConfigurationSaver
{	
	public static void saveQuickStartConfiguration(QuickStartConfiguration quickStartConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGameQuickStartElement(quickStartConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGameQuickStartElement(QuickStartConfiguration quickStartConfiguration)
	{	Element result = new Element(XmlTools.ELT_GAME_QUICKSTART); 
			
		// round
		Element roundElement = saveRoundELement(quickStartConfiguration);
		result.addContent(roundElement);
	
		// players
		Element playersElement = new Element(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickStartSelected = quickStartConfiguration.getProfilesSelection();
		ProfilesSelectionSaver.saveProfilesSelection(playersElement,quickStartSelected);
		result.addContent(playersElement);
		
		return result;
	}
	
	private static Element saveRoundELement(QuickStartConfiguration quickStartConfiguration)
	{	Element result = new Element(XmlTools.ELT_ROUND); 
	
		// name
		String roundName = quickStartConfiguration.getRoundName().toString();
		result.setAttribute(XmlTools.ATT_NAME,roundName);

		// allowed players
		TreeSet<Integer> allowedPlayers = quickStartConfiguration.getAllowedPlayers();
		String allowedPlayersStr = "";
		for(Integer i: allowedPlayers)
		{	String str = i.toString();
			allowedPlayersStr = allowedPlayersStr + str + " ";
		}
		if(allowedPlayers.size()>0)
			allowedPlayersStr = allowedPlayersStr.substring(0,allowedPlayersStr.length()-1);
		result.setAttribute(XmlTools.ATT_ALLOWED_PLAYERS,allowedPlayersStr);
		
		return result;
	}
}