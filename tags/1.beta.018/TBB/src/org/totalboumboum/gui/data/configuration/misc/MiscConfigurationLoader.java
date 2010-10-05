package org.totalboumboum.gui.data.configuration.misc;

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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.gui.data.language.Language;
import org.totalboumboum.gui.data.language.LanguageLoader;
import org.totalboumboum.gui.tools.GuiFileTools;
import org.totalboumboum.gui.tools.GuiXmlTools;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.ImageTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class MiscConfigurationLoader
{	
	public static MiscConfiguration loadMiscConfiguration() throws ParserConfigurationException, SAXException, IOException
	{	MiscConfiguration result = new MiscConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+GuiFileTools.FILE_GUI+GuiFileTools.EXTENSION_DATA);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+GuiFileTools.FILE_GUI+GuiFileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadGuiElement(root,result);
		return result;
	}

	private static void loadGuiElement(Element root, MiscConfiguration result) throws IOException, ParserConfigurationException, SAXException
	{	// language
		{	Element element = root.getChild(GuiXmlTools.ELT_LANGUAGE);
			loadLanguageElement(element,result);
		}
		// font
		{	Element element = root.getChild(GuiXmlTools.ELT_FONT);
			loadFontElement(element,result);
		}
		// background
		{	Element element = root.getChild(GuiXmlTools.ELT_BACKGROUND);
			loadBackgroundElement(element,result);
		}
	}

	public static void loadLanguageElement(Element root, MiscConfiguration result) throws ParserConfigurationException, SAXException, IOException
	{	String value = root.getAttribute(GuiXmlTools.ATT_VALUE).getValue().trim();
		Language language;
		language = LanguageLoader.loadLanguage(value);			
		result.setLanguage(value,language);
	}

	public static void loadFontElement(Element root, MiscConfiguration result)
	{	String filename = root.getAttribute(GuiXmlTools.ATT_VALUE).getValue().trim();
		String path = GuiFileTools.getFontsPath()+File.separator+filename+FileNames.EXTENSION_FONT;
		Font font;
		try
		{	InputStream is = new FileInputStream(path);
			font = Font.createFont(Font.TRUETYPE_FONT,is);
		}
		catch (Exception ex)
		{	font = new Font("serif",Font.PLAIN,1);
		}		
		result.setFont(filename,font);
	}

	public static void loadBackgroundElement(Element root, MiscConfiguration result) throws IOException
	{	// folder
		String filename = root.getAttribute(GuiXmlTools.ATT_FILE).getValue().trim();
		String path = GuiFileTools.getBackgroundsPath()+File.separator+filename;
		// image
		BufferedImage image = ImageTools.loadImage(path,null);
		image = ImageTools.getCompatibleImage(image);
		// resize
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		double zoomY = dim.getHeight()/(double)image.getHeight();
		double zoomX = dim.getWidth()/(double)image.getWidth();
		double zoom = Math.max(zoomX,zoomY);
		image = ImageTools.getResizedImage(image,zoom,true);
		image = ImageTools.getCompatibleImage(image);
		result.setBackground(filename,image);
	}
}
