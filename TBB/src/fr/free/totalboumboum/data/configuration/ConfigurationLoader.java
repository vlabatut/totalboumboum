package fr.free.totalboumboum.data.configuration;

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
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.controls.ControlSettings;
import fr.free.totalboumboum.data.configuration.controls.ControlSettingsLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ConfigurationLoader
{	
	public static Configuration quickloadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	return loadConfiguration(true);
	}
	public static Configuration loadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	return loadConfiguration(false);
	}
	private static Configuration loadConfiguration(boolean quickStart) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Configuration result = new Configuration();
		String individualFolder = FileTools.getSettingsPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_CONFIGURATION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadConfigurationElement(root,result,quickStart);
		return result;
	}

	private static void loadConfigurationElement(Element root, Configuration result, boolean quickStart) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Element element; 
		// engine
		element = root.getChild(XmlTools.ELT_FPS);
		loadFpsElement(element,result);
		element = root.getChild(XmlTools.ELT_SPEED);
		loadSpeedElement(element,result);
		// display
		element = root.getChild(XmlTools.ELT_SMOOTH_GRAPHICS);
		loadSmoothGraphicsElement(element,result);
		// panel
		element = root.getChild(XmlTools.ELT_PANEL_DIMENSION);
		loadPanelDimensionElement(element,result);
		// profiles
		element = root.getChild(XmlTools.ELT_PROFILES);
		loadProfilesElement(element,result);
		// controls
		loadControlSettings(result);
		// round for quick start
		element = root.getChild(XmlTools.ELT_QUICKSTART);
		if(quickStart && element!=null)
			loadQuickstartElement(element,result);
		else
		{	// last tournament
			element = root.getChild(XmlTools.ELT_TOURNAMENT);
			if(element!=null)
				loadTournamentElement(element,result);
			// quick match
			element = root.getChild(XmlTools.ELT_QUICKMATCH);
			if(element!=null)
				loadQuickmatchElement(element,result);
		}
	}
	
	private static void loadFpsElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		int fps = Integer.valueOf(value);
		result.setFps(fps);
	}
	
	private static void loadSpeedElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		float speedCoeff = Float.valueOf(value);
		result.setSpeedCoeff(speedCoeff);
	}
	
	private static void loadSmoothGraphicsElement(Element root, Configuration result)
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		boolean smoothGraphics = Boolean.valueOf(value);
		result.setSmoothGraphics(smoothGraphics);
	}
	
	private static void loadPanelDimensionElement(Element root, Configuration result)
	{	String valueH = root.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int height = Integer.valueOf(valueH);
		String valueW = root.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int width = Integer.valueOf(valueW);
		result.setPanelDimension(width, height);
	}
	
	@SuppressWarnings("unchecked")
	private static void loadProfilesElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	List<Element> elements = root.getChildren(XmlTools.ELT_PROFILE);
		Iterator<Element> i = elements.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadProfileElement(temp,result);
		}
	}
	
	private static void loadProfileElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	String value = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
//		Profile profile = ProfileLoader.loadProfile(value);			
//		result.addProfile(profile);
result.addProfile(value);	
	}

	private static void loadTournamentElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String name = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setLastTournamentName(name);
	}

	private static void loadQuickmatchElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String quickmatchName = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setQuickmatchName(quickmatchName);
	}

	private static void loadQuickstartElement(Element root, Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	String quickstartName = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setQuickstartName(quickstartName);
	}
	
	public static void loadControlSettings(Configuration result) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		for(int i=1;i<=GameConstants.CONTROL_COUNT;i++)
		{	String fileName = nf.format(i);
			ControlSettings controlSettings = ControlSettingsLoader.loadControlSettings(fileName);
			result.putControlSettings(i,controlSettings);
		}
	}
}
