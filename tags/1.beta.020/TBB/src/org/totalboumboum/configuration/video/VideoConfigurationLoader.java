package org.totalboumboum.configuration.video;

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

import java.awt.Color;
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
public class VideoConfigurationLoader
{	
	public static VideoConfiguration loadVideoConfiguration() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	VideoConfiguration result = new VideoConfiguration();
		String individualFolder = FilePaths.getConfigurationPath();
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_VIDEO+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_VIDEO+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		loadVideoElement(root,result);
		return result;
	}

	private static void loadVideoElement(Element root, VideoConfiguration result) throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException
	{	Element element; 
		// full screen
		element = root.getChild(XmlNames.FULL_SCREEN);
		loadFullScreenElement(element,result);
		// smoothing
		element = root.getChild(XmlNames.SMOOTH_GRAPHICS);
		loadSmoothGraphicsElement(element,result);
		// border
		element = root.getChild(XmlNames.BORDER);
		loadBorderElement(element,result);
		// panel
		element = root.getChild(XmlNames.PANEL_DIMENSION);
		loadPanelDimensionElement(element,result);
	}
	
	private static void loadFullScreenElement(Element root, VideoConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean fullScreen = Boolean.valueOf(value);
		result.setFullScreen(fullScreen);
	}
	
	private static void loadSmoothGraphicsElement(Element root, VideoConfiguration result)
	{	String value = root.getAttribute(XmlNames.VALUE).getValue().trim();
		boolean smoothGraphics = Boolean.valueOf(value);
		result.setSmoothGraphics(smoothGraphics);
	}
	
	private static void loadBorderElement(Element root, VideoConfiguration result)
	{	
/*		
		String value = root.getAttribute(XmlTools.ATT_RED).getValue().trim();
		int red = Integer.parseInt(value);
		value = root.getAttribute(XmlTools.ATT_GREEN).getValue().trim();
		int green = Integer.parseInt(value);
		value = root.getAttribute(XmlTools.ATT_BLUE).getValue().trim();
		int blue = Integer.parseInt(value);
		Color borderColor = new Color(red,green,blue);
		result.setBorderColor(borderColor);
*/		
		Color borderColor = null;
		String value = root.getAttributeValue(XmlNames.VALUE);
		if(value!=null)
			borderColor = Color.BLACK;
		result.setBorderColor(borderColor);
	}
	
	private static void loadPanelDimensionElement(Element root, VideoConfiguration result)
	{	String valueH = root.getAttribute(XmlNames.HEIGHT).getValue().trim();
		int height = Integer.valueOf(valueH);
		String valueW = root.getAttribute(XmlNames.WIDTH).getValue().trim();
		int width = Integer.valueOf(valueW);
		result.setPanelDimension(width, height);
	}
}
