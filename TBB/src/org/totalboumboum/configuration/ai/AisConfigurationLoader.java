package org.totalboumboum.configuration.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class AisConfigurationLoader
{	
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
	}
	
	private static void loadUpsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		int ups = Integer.valueOf(value);
		result.setAiUps(ups);
	}
	
	private static void loadAutoAdvanceElement(Element root, AisConfiguration result)
	{	// switch
		{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
			boolean autoAdvance = Boolean.valueOf(value);
			result.setAutoAdvance(autoAdvance);
		}
		// delay
		{	String value = root.getAttribute(XmlNames.DELAY).getValue().trim();
			long autoAdvanceDelay = Long.parseLong(value);
			result.setAutoAdvanceDelay(autoAdvanceDelay);
		}		
	}
	
	private static void loadHideAllAisElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean hideAllAis = Boolean.valueOf(value);
		result.setHideAllAis(hideAllAis);
	}

	private static void loadBombUselessAisElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		long bombUselessAis = Long.valueOf(value);
		result.setBombUselessAis(bombUselessAis);
	}

	private static void loadDisplayExceptionsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean displayExceptions = Boolean.valueOf(value);
		result.setDisplayExceptions(displayExceptions);
	}

	private static void loadLogExceptionsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean logExceptions = Boolean.valueOf(value);
		result.setLogExceptions(logExceptions);
	}
}
