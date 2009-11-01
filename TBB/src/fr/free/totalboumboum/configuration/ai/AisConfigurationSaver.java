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

public class AisConfigurationSaver
{	
	public static void saveAisConfiguration(AisConfiguration aisConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveAisElement(aisConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_AIS+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_AIS+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveAisElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlTools.AIS); 
		// ups
		Element upsElement = saveUpsElement(aisConfiguration);
		result.addContent(upsElement);
		// auto advance
		Element autoAdvanceElement = saveAutoAdvanceElement(aisConfiguration);
		result.addContent(autoAdvanceElement);
		// hide all-ais
		Element hideAllAisElement = saveHideAllAisElement(aisConfiguration);
		result.addContent(hideAllAisElement);
		//
		return result;
	}
	
	private static Element saveUpsElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlTools.ELT_UPS);
		String ups = Integer.toString(aisConfiguration.getAiUps());
		result.setAttribute(XmlTools.ATT_VALUE,ups);
		return result;
	}

	private static Element saveAutoAdvanceElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlTools.ELT_AUTO_ADVANCE);
		// switch
		String autoAdvance = Boolean.toString(aisConfiguration.getAutoAdvance());
		result.setAttribute(XmlTools.ATT_VALUE,autoAdvance);
		// delay
		String autoAdvanceDelay = Long.toString(aisConfiguration.getAutoAdvanceDelay());
		result.setAttribute(XmlTools.ATT_DELAY,autoAdvanceDelay);
		//
		return result;
	}
	
	private static Element saveHideAllAisElement(AisConfiguration aisConfiguration)
	{	Element result = new Element(XmlTools.ELT_HIDE_ALLAIS);
		String hideAllAis = Boolean.toString(aisConfiguration.getHideAllAis());
		result.setAttribute(XmlTools.ATT_VALUE,hideAllAis);
		return result;
	}
}
