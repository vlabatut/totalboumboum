package org.totalboumboum.engine.content.feature.gesture.anime.color;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ColorMapLoader
{
	public static ColorMap loadColormap(String individualFolder, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
	{	// opening
		File dataFile = new File(individualFolder+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_COLORMAP+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		ColorMap result = new ColorMap(color);
		loadColorsElement(root, result);
		return result;
	}
    
    @SuppressWarnings("unchecked")
	private static void loadColorsElement(Element root, ColorMap colormap) throws IOException
    {	// colors
    	List<Element> colorsList = root.getChildren(XmlNames.COLOR);
		for(int i=0;i<colorsList.size();i++)
			loadColorElement(colorsList.get(i),colormap);    	
    }
	
    private static void loadColorElement(Element root, ColorMap colormap) throws IOException
    {	// index
		int index = Integer.parseInt(root.getAttribute(XmlNames.INDEX).getValue());
		// RGB
		byte colors[] = new byte[3];
		colors[0] = (byte)Integer.parseInt(root.getAttribute(XmlNames.RED).getValue());
		// green
		colors[1] = (byte)Integer.parseInt(root.getAttribute(XmlNames.GREEN).getValue());
		// blue
		colors[2] = (byte)Integer.parseInt(root.getAttribute(XmlNames.BLUE).getValue());
		// colormap
		colormap.addColor(index,colors);
    }
}
