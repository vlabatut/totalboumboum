package fr.free.totalboumboum.gui.data.configuration.misc;

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

import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiXmlTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class MiscConfigurationSaver
{	
	public static void saveMiscConfiguration(MiscConfiguration miscConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveGuiElement(miscConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+GuiFileTools.FILE_GUI+FileTools.EXTENSION_DATA;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+GuiFileTools.FILE_GUI+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveGuiElement(MiscConfiguration engineConfiguration)
	{	Element result = new Element(GuiXmlTools.ELT_CONFIGURATION); 
		// language
		Element languageElement = saveLanguageElement(engineConfiguration);
		result.addContent(languageElement);
		// font
		Element fontElement = saveFontElement(engineConfiguration);
		result.addContent(fontElement);
		// background
		Element backgroundElement = saveBackgroundElement(engineConfiguration);
		result.addContent(backgroundElement);
		//
		return result;
	}
	
	private static Element saveLanguageElement(MiscConfiguration miscConfiguration)
	{	Element result = new Element(GuiXmlTools.ELT_LANGUAGE);
		String language = miscConfiguration.getLanguageName();
		result.setAttribute(XmlTools.ATT_VALUE,language);
		return result;
	}
	
	private static Element saveFontElement(MiscConfiguration miscConfiguration)
	{	Element result = new Element(GuiXmlTools.ELT_FONT);
		String font = miscConfiguration.getFontName();
		result.setAttribute(XmlTools.ATT_VALUE,font);
		return result;
	}
	
	private static Element saveBackgroundElement(MiscConfiguration miscConfiguration)
	{	Element result = new Element(GuiXmlTools.ELT_BACKGROUND);
		String background = miscConfiguration.getBackgroundName();
		result.setAttribute(XmlTools.ATT_FILE,background);
		return result;
	}
}