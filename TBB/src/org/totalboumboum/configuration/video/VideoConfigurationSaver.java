package org.totalboumboum.configuration.video;

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

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.FileTools;
import org.totalboumboum.tools.XmlTools;
import org.xml.sax.SAXException;


public class VideoConfigurationSaver
{	
	public static void saveVideoConfiguration(VideoConfiguration videoConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveVideoElement(videoConfiguration);	
		// save file
		String engineFile = FileTools.getConfigurationPath()+File.separator+FileTools.FILE_VIDEO+FileTools.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_VIDEO+FileTools.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveVideoElement(VideoConfiguration videoConfiguration)
	{	Element result = new Element(XmlTools.VIDEO); 
		// full screen
		Element fullScreenElement = saveFullScreenElement(videoConfiguration);
		result.addContent(fullScreenElement);
		// smoothing
		Element smoothingElement = saveSmoothGraphicsElement(videoConfiguration);
		result.addContent(smoothingElement);
		// panel dimension
		Element panelElement = savePanelDimensionElement(videoConfiguration);
		result.addContent(panelElement);
		// border color
		Element borderElement = saveBorderElement(videoConfiguration);
		result.addContent(borderElement);
		//
		return result;
	}
	
	private static Element saveFullScreenElement(VideoConfiguration videoConfiguration)
	{	Element result = new Element(XmlTools.FULL_SCREEN);
		String fullScreen = Boolean.toString(videoConfiguration.getFullScreen());
		result.setAttribute(XmlTools.VALUE,fullScreen);
		return result;
	}
	
	private static Element saveSmoothGraphicsElement(VideoConfiguration videoConfiguration)
	{	Element result = new Element(XmlTools.SMOOTH_GRAPHICS);
		String smoothing = Boolean.toString(videoConfiguration.getSmoothGraphics());
		result.setAttribute(XmlTools.VALUE,smoothing);
		return result;
	}
	
	private static Element saveBorderElement(VideoConfiguration videoConfiguration)
	{	// init
		Element result = new Element(XmlTools.BORDER);
		Color color = videoConfiguration.getBorderColor();
/*		
		// red
		String red = Integer.toString(color.getRed());
		result.setAttribute(XmlTools.ATT_RED,red);
		// green
		String green = Integer.toString(color.getGreen());
		result.setAttribute(XmlTools.ATT_GREEN,green);
		// blue
		String blue = Integer.toString(color.getBlue());
		result.setAttribute(XmlTools.ATT_BLUE,blue);
*/
		if(color!=null)
			result.setAttribute(XmlTools.VALUE,"black");
		//
		return result;
	}

	private static Element savePanelDimensionElement(VideoConfiguration videoConfiguration)
	{	// init
		Element result = new Element(XmlTools.PANEL_DIMENSION);
		Dimension dim = videoConfiguration.getPanelDimension();
		// width
		String width = Integer.toString(dim.width);
		result.setAttribute(XmlTools.WIDTH,width);
		// height
		String height = Integer.toString(dim.height);
		result.setAttribute(XmlTools.HEIGHT,height);
		//
		return result;
	}
}
