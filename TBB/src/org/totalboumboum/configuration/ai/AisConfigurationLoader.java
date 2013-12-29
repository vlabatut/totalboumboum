package org.totalboumboum.configuration.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import org.totalboumboum.configuration.ai.AisConfiguration.AutoAdvance;
import org.totalboumboum.configuration.ai.AisConfiguration.TournamentAutoAdvance;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * Load the AI-related
 * settings from an XML file.
 * 
 * @author Vincent Labatut
 */
public class AisConfigurationLoader
{	
	/**
	 * Loads the AI settings from the XML file.
	 * 
	 * @return
	 * 		AI settings.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the AI settings XML file.
	 * @throws SAXException
	 * 		Problem while accessing the AI settings XML file.
	 * @throws IOException
	 * 		Problem while accessing the AI settings XML file.
	 */
	public static AisConfiguration loadAisConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	AisConfiguration result = new AisConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_AIS+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_AIS+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadAisElement(root,result);
		return result;
	}

	/**
	 * Load the root element.
	 * 
	 * @param root
	 * 		Root element.
	 * @param result
	 * 		Loaded settings.
	 */
	private static void loadAisElement(Element root, AisConfiguration result)
	{	Element element; 
	
		// ups
		element = root.getChild(XmlNames.UPS);
		loadUpsElement(element,result);
		
		// auto advance
		element = root.getChild(XmlNames.AUTO_ADVANCE);
		loadAutoAdvanceElement(element,result);
		
		// hide all-ais rounds
		element = root.getChild(XmlNames.HIDE_ALLAIS);
		loadHideAllAisElement(element,result);
		
		// hide all-ais rounds
		element = root.getChild(XmlNames.BOMB_USELESS_AIS);
		loadBombUselessAisElement(element,result);
		
		// display exceptions onscreen during game
		element = root.getChild(XmlNames.DISPLAY_EXCEPTIONS);
		loadDisplayExceptionsElement(element,result);

		// log exceptions during game
		element = root.getChild(XmlNames.LOG_EXCEPTIONS);
		loadLogExceptionsElement(element,result);
		
		// record stats automatically
		element = root.getChild(XmlNames.RECORD_STATS);
		loadRecordStatsElement(element, result);
	}
	
	/**
	 * Loads UPS related settings.
	 * 
	 * @param root
	 * 		UPS element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadUpsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		int ups = Integer.valueOf(value);
		result.setAiUps(ups);
	}
	
	/**
	 * Loads auto-advance related settings.
	 * 
	 * @param root
	 * 		Auto advance element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadAutoAdvanceElement(Element root, AisConfiguration result)
	{	// mode
		{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
			AutoAdvance autoAdvance = AutoAdvance.valueOf(value);
			result.setAutoAdvance(autoAdvance);
		}
		// delay
		{	String value = root.getAttribute(XmlNames.DELAY).getValue().trim();
			long autoAdvanceDelay = Long.parseLong(value);
			result.setAutoAdvanceDelay(autoAdvanceDelay);
		}
		// mode
		{	String value = root.getAttribute(XmlNames.TOURNAMENT).getValue().trim();
			TournamentAutoAdvance tournamentAutoAdvance = TournamentAutoAdvance.valueOf(value);
			result.setTournamentAutoAdvanceMode(tournamentAutoAdvance);
		}
		// delay
		{	String pack = root.getAttribute(XmlNames.PACK).getValue().trim();
			result.setTournamentAutoAdvancePack(pack);
		}
	}
	
	/**
	 * Loads hide/show AI round related settings.
	 * 
	 * @param root
	 * 		Hide/show element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadHideAllAisElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean hideAllAis = Boolean.valueOf(value);
		result.setHideAllAis(hideAllAis);
	}

	/**
	 * Loads threatening option related settings.
	 * 
	 * @param root
	 * 		Threatening element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadBombUselessAisElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		long bombUselessAis = Long.valueOf(value);
		result.setBombUselessAis(bombUselessAis);
	}

	/**
	 * Loads display exception related settings.
	 * 
	 * @param root
	 * 		Display exception element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadDisplayExceptionsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean displayExceptions = Boolean.valueOf(value);
		result.setDisplayExceptions(displayExceptions);
	}

	/**
	 * Loads exception logging related settings.
	 * 
	 * @param root
	 * 		Exception logging element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadLogExceptionsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean logExceptions = Boolean.valueOf(value);
		result.setLogExceptions(logExceptions);
	}
	
	/**
	 * Loads the stat recording flag.
	 * 
	 * @param root
	 * 		Stat recording element.
	 * @param result
	 * 		Settings object to be completed.
	 */
	private static void loadRecordStatsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean recordStats = Boolean.valueOf(value);
		result.setRecordStats(recordStats);
	}
}
