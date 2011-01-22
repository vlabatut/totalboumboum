package org.totalboumboum.configuration.ai;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
public class AisConfigurationSaver
{	
	public static void saveAisConfiguration(AisConfiguration aisConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveAisElement(aisConfiguration);	
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_AIS+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_AIS+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveAisElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.AIS); 
	
		// ups
		Element upsElement = saveUpsElement(aisConfiguration);
		result.addContent(upsElement);
		
		// auto advance
		Element autoAdvanceElement = saveAutoAdvanceElement(aisConfiguration);
		result.addContent(autoAdvanceElement);
		
		// hide all-ais
		Element hideAllAisElement = saveHideAllAisElement(aisConfiguration);
		result.addContent(hideAllAisElement);
		
		// bomb useless players
		Element bombUselessAisElement = saveBombUselessAisElement(aisConfiguration);
		result.addContent(bombUselessAisElement);

		// display exceptions onscreen during game
		Element displayExceptionsElement = saveDisplayExceptionsElement(aisConfiguration);
		result.addContent(displayExceptionsElement);

		// log exceptions during game
		Element logExceptionsElement = saveLogExceptionsElement(aisConfiguration);
		result.addContent(logExceptionsElement);

		return result;
	}
	
	private static Element saveUpsElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.UPS);
		String ups = Integer.toString(aisConfiguration.getAiUps());
		result.setAttribute(XmlNames.VALUE,ups);
		return result;
	}

	private static Element saveAutoAdvanceElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.AUTO_ADVANCE);
	
		// switch
		String autoAdvance = Boolean.toString(aisConfiguration.getAutoAdvance());
		result.setAttribute(XmlNames.VALUE,autoAdvance);
		
		// delay
		String autoAdvanceDelay = Long.toString(aisConfiguration.getAutoAdvanceDelay());
		result.setAttribute(XmlNames.DELAY,autoAdvanceDelay);

		return result;
	}
	
	private static Element saveHideAllAisElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.HIDE_ALLAIS);
		String hideAllAis = Boolean.toString(aisConfiguration.getHideAllAis());
		result.setAttribute(XmlNames.VALUE,hideAllAis);
		return result;
	}

	private static Element saveBombUselessAisElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.BOMB_USELESS_AIS);
		String bombUselessAis = Long.toString(aisConfiguration.getBombUselessAis());
		result.setAttribute(XmlNames.VALUE,bombUselessAis);
		return result;
	}

	private static Element saveDisplayExceptionsElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.DISPLAY_EXCEPTIONS);
		String displayExceptions = Boolean.toString(aisConfiguration.getDisplayExceptions());
		result.setAttribute(XmlNames.VALUE,displayExceptions);
		return result;
	}

	private static Element saveLogExceptionsElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlNames.LOG_EXCEPTIONS);
		String logExceptions = Boolean.toString(aisConfiguration.getLogExceptions());
		result.setAttribute(XmlNames.VALUE,logExceptions);
		return result;
	}
}
