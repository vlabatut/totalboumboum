package fr.free.totalboumboum.engine.content.feature.gesture.anime;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ColormapLoader
{
	public static Colormap loadColormap(String individualFolder) throws IOException, ParserConfigurationException, SAXException
	{	// opening
		File dataFile = new File(individualFolder+FileTools.EXTENSION_XML);
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_COLORMAP+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Colormap result = new Colormap();
		loadColorsElement(root, result);
		return result;
	}
    
    @SuppressWarnings("unchecked")
	private static void loadColorsElement(Element root, Colormap colormap) throws IOException
    {	// colors
    	List<Element> colorsList = root.getChildren(XmlTools.COLOR);
		for(int i=0;i<colorsList.size();i++)
			loadColorElement(colorsList.get(i),colormap);    	
    }
	
    private static void loadColorElement(Element root, Colormap colormap) throws IOException
    {	// index
		int index = Integer.parseInt(root.getAttribute(XmlTools.INDEX).getValue());
		// RGB
		byte colors[] = new byte[3];
		colors[0] = (byte)Integer.parseInt(root.getAttribute(XmlTools.RED).getValue());
		// green
		colors[1] = (byte)Integer.parseInt(root.getAttribute(XmlTools.GREEN).getValue());
		// blue
		colors[2] = (byte)Integer.parseInt(root.getAttribute(XmlTools.BLUE).getValue());
		// colormap
		colormap.put(index,colors);
    }
}
