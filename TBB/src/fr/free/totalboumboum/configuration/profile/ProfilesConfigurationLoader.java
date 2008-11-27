package fr.free.totalboumboum.configuration.profile;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ProfilesConfigurationLoader
{	
	public static ProfilesConfiguration loadProfilesConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	ProfilesConfiguration result = new ProfilesConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_PROFILES+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadProfilesElement(root,result);
		return result;
	}

	private static void loadProfilesElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// general
		Element generalElement = root.getChild(XmlTools.ELT_GENERAL);
		loadGeneralElement(generalElement,result);
		// list
		Element listElement = root.getChild(XmlTools.ELT_LIST);
		loadListElement(listElement,result);
		// selected
		Element selectedElement = root.getChild(XmlTools.ELT_SELECTED);
		loadSelectedElement(selectedElement,result);
	}

	@SuppressWarnings("unchecked")
	private static void loadListElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlTools.ELT_PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp,result);
		}
/*		
result.addSelected("4");
result.addSelected("8");		
result.addSelected("14");		
result.addSelected("18");		
result.addSelected("1");
*/		
	}
		
	private static void loadProfileElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String file = root.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.addProfile(file,name);	
	}

	private static void loadGeneralElement(Element root, ProfilesConfiguration result)
	{	String lastProfileStr = root.getAttribute(XmlTools.ATT_LAST).getValue().trim();
		int lastProfile = Integer.parseInt(lastProfileStr);
		result.setLastProfileIndex(lastProfile);
	}

	private static void loadSelectedElement(Element root, ProfilesConfiguration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// quickstart
		Element quickstartElement = root.getChild(XmlTools.ELT_QUICKSTART);
		ArrayList<String> quickStartSelected = loadPlayersElement(quickstartElement);
		for(String s: quickStartSelected)
			result.addQuickStartSelected(s);
		// quickmatch
		Element quickmatchElement = root.getChild(XmlTools.ELT_QUICKMATCH);
		ArrayList<String> quickMatchSelected = loadPlayersElement(quickmatchElement);
		for(String s: quickMatchSelected)
			result.addQuickMatchSelected(s);
		// tournament
		Element tournamentElement = root.getChild(XmlTools.ELT_TOURNAMENT);
		ArrayList<String> tournamentSelected = loadPlayersElement(tournamentElement);
		for(String s: tournamentSelected)
			result.addTournamentSelected(s);
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<String> loadPlayersElement(Element root)
	{	ArrayList<String> result = new ArrayList<String>();
		List<Element> playersElt = root.getChildren(XmlTools.ELT_PLAYER);
		for(Element elt: playersElt)
			loadPlayerElement(elt,result);
		return result;
	}
	
	private static void loadPlayerElement(Element root, ArrayList<String> result)
	{	String fileName = root.getAttributeValue(XmlTools.ATT_FILE);
		result.add(fileName);
	}
}
