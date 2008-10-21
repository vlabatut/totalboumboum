package fr.free.totalboumboum.gui.data.configuration;

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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.gui.data.language.Language;
import fr.free.totalboumboum.gui.data.language.LanguageLoader;
import fr.free.totalboumboum.gui.tools.GuiFileTools;
import fr.free.totalboumboum.gui.tools.GuiXmlTools;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class GuiConfiguration
{	
	/////////////////////////////////////////////////////////////////
	// FILE ACCESS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	// file
		String individualFolder = FileTools.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+GuiFileTools.FILE_GUI+GuiFileTools.EXTENSION_DATA);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+GuiFileTools.FILE_GUI+GuiFileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// language
		{	Element element = root.getChild(GuiXmlTools.ELT_LANGUAGE);
			loadLanguageElement(element);
		}
		// font
		{	Element element = root.getChild(GuiXmlTools.ELT_FONT);
			loadFontElement(element);
		}
		// background
		{	Element element = root.getChild(GuiXmlTools.ELT_BACKGROUND);
			loadBackgroundElement(element);
		}
	}

	public static void saveConfiguration() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// LANGUAGE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static Language language;
		
	public static void loadLanguageElement(Element root) throws ParserConfigurationException, SAXException, IOException
	{	String value = root.getAttribute(GuiXmlTools.ATT_VALUE).getValue().trim();
		Language language;
		language = LanguageLoader.loadLanguage(value);			
		GuiConfiguration.language = language;
	}
	public static Language getLanguage()
	{	return language;	
	}
		
	/////////////////////////////////////////////////////////////////
	// FONT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static Font font;
	
	public static Font getFont()
	{	return font;	
	}
	public static void loadFontElement(Element root)
	{	String filename = root.getAttribute(GuiXmlTools.ATT_FILE).getValue().trim();
		String path = GuiFileTools.getFontsPath()+File.separator+filename;
		Font font;
		try
		{	InputStream is = new FileInputStream(path);
			font = Font.createFont(Font.TRUETYPE_FONT,is);
		}
		catch (Exception ex)
		{	font = new Font("serif",Font.PLAIN,1);
		}		
		GuiConfiguration.font = font;
	}

	/////////////////////////////////////////////////////////////////
	// BACKGROUND		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private static BufferedImage background;
	private static BufferedImage darkBackground;
	
	public static void loadBackgroundElement(Element root) throws IOException
	{	// folder
		String filename = root.getAttribute(GuiXmlTools.ATT_FILE).getValue().trim();
		String path = GuiFileTools.getImagesPath()+File.separator+filename;
		// image
		BufferedImage image = ImageTools.loadImage(path,null);
		// resize
		Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
		double zoomY = dim.getHeight()/(double)image.getHeight();
		double zoomX = dim.getWidth()/(double)image.getWidth();
		double zoom = Math.max(zoomX,zoomY);
		image = ImageTools.resize(image,zoom,true);
		GuiConfiguration.background = image;
		// dark image		
		BufferedImage darkImage = ImageTools.copyBufferedImage(image);
		Graphics2D g = (Graphics2D)darkImage.getGraphics();
		g.setComposite(AlphaComposite.Clear);
		g.setPaintMode();
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0,0,darkImage.getWidth(),darkImage.getHeight());
		g.dispose();
		GuiConfiguration.darkBackground = darkImage;
	}
	public static BufferedImage getBackground()
	{	return background;	
	}
	public static BufferedImage getDarkBackground()
	{	return darkBackground;	
	}
}
