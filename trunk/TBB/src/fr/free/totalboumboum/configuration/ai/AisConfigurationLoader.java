package fr.free.totalboumboum.configuration.ai;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class AisConfigurationLoader
{	
	public static AisConfiguration loadAisConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	AisConfiguration result = new AisConfiguration();
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_AIS+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_AIS+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadAisElement(root,result);
		return result;
	}

	private static void loadAisElement(Element root, AisConfiguration result)
	{	Element element; 
	
		// ups
		element = root.getChild(XmlTools.UPS);
		loadUpsElement(element,result);
		
		// auto advance
		element = root.getChild(XmlTools.AUTO_ADVANCE);
		loadAutoAdvanceElement(element,result);
		
		// hide all-ais rounds
		element = root.getChild(XmlTools.HIDE_ALLAIS);
		loadHideAllAisElement(element,result);

		// display exceptions onscreen during game
		element = root.getChild(XmlTools.DISPLAY_EXCEPTIONS);
		loadDisplayExceptionsElement(element,result);
}
	
	private static void loadUpsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlTools.VALUE).getValue().trim();
		int ups = Integer.valueOf(value);
		result.setAiUps(ups);
	}
	
	private static void loadAutoAdvanceElement(Element root, AisConfiguration result)
	{	// switch
		{	String value = root.getAttribute(XmlTools.VALUE).getValue().trim();
			boolean autoAdvance = Boolean.valueOf(value);
			result.setAutoAdvance(autoAdvance);
		}
		// delay
		{	String value = root.getAttribute(XmlTools.DELAY).getValue().trim();
			long autoAdvanceDelay = Long.parseLong(value);
			result.setAutoAdvanceDelay(autoAdvanceDelay);
		}		
	}
	
	private static void loadHideAllAisElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlTools.VALUE).getValue().trim();
		boolean hideAllAis = Boolean.valueOf(value);
		result.setHideAllAis(hideAllAis);
	}

	private static void loadDisplayExceptionsElement(Element root, AisConfiguration result)
	{	String value = root.getAttribute(XmlTools.VALUE).getValue().trim();
		boolean displayExceptions = Boolean.valueOf(value);
		result.setDisplayExceptions(displayExceptions);
	}
}
