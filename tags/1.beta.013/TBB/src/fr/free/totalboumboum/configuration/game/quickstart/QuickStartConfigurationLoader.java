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
import fr.free.totalboumboum.configuration.profile.ProfilesSelectionLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class QuickStartConfigurationLoader
{	
	public static QuickStartConfiguration loadQuickStartConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	QuickStartConfiguration result = new QuickStartConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_GAME_QUICKSTART+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGameQuickStartElement(root,result);
		return result;
	}

	private static void loadGameQuickStartElement(Element root, QuickStartConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// round
		Element roundElement = root.getChild(XmlTools.ELT_ROUND);
		loadRoundElement(roundElement,result);
		
		// players
		Element playersElement = root.getChild(XmlTools.ELT_PLAYERS);
		ProfilesSelection quickStartSelected = ProfilesSelectionLoader.loadProfilesSelection(playersElement);
		result.setProfilesSelection(quickStartSelected);
	}
	
	private static void loadRoundElement(Element root, QuickStartConfiguration result)
	{	// name
		String quickStartName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setRoundName(new StringBuffer(quickStartName));
		
		// allowed players
		TreeSet<Integer> allowedPlayers = new TreeSet<Integer>();
		String apStr = root.getAttributeValue(XmlTools.ATT_ALLOWED_PLAYERS);
		String[] split = apStr.split(" ");
		for(String s: split)
		{	int value = Integer.parseInt(s);
			allowedPlayers.add(value);
		}
		result.setAllowedPlayers(allowedPlayers);
	}
}