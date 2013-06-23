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
import java.awt.Dimension;
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
public class VideoConfigurationSaver
{	
	public static void saveVideoConfiguration(VideoConfiguration videoConfiguration) throws ParserConfigurationException, SAXException, IOException
	{	// build document
		Element root = saveVideoElement(videoConfiguration);	
		// save file
		String engineFile = FilePaths.getConfigurationPath()+File.separator+FileNames.FILE_VIDEO+FileNames.EXTENSION_XML;
		File dataFile = new File(engineFile);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_VIDEO+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveVideoElement(VideoConfiguration videoConfiguration)
	{	Element result = new Element(XmlNames.VIDEO); 
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
	{	Element result = new Element(XmlNames.FULL_SCREEN);
		String fullScreen = Boolean.toString(videoConfiguration.getFullScreen());
		result.setAttribute(XmlNames.VALUE,fullScreen);
		return result;
	}
	
	private static Element saveSmoothGraphicsElement(VideoConfiguration videoConfiguration)
	{	Element result = new Element(XmlNames.SMOOTH_GRAPHICS);
		String smoothing = Boolean.toString(videoConfiguration.getSmoothGraphics());
		result.setAttribute(XmlNames.VALUE,smoothing);
		return result;
	}
	
	private static Element saveBorderElement(VideoConfiguration videoConfiguration)
	{	// init
		Element result = new Element(XmlNames.BORDER);
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
			result.setAttribute(XmlNames.VALUE,"black");
		//
		return result;
	}

	private static Element savePanelDimensionElement(VideoConfiguration videoConfiguration)
	{	// init
		Element result = new Element(XmlNames.PANEL_DIMENSION);
		Dimension dim = videoConfiguration.getPanelDimension();
		// width
		String width = Integer.toString(dim.width);
		result.setAttribute(XmlNames.WIDTH,width);
		// height
		String height = Integer.toString(dim.height);
		result.setAttribute(XmlNames.HEIGHT,height);
		//
		return result;
	}
}
