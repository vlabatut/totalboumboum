package org.totalboumboum.engine.content.feature.gesture.anime.color;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ColorRulesMapLoader
{
	@SuppressWarnings("unchecked")
	public static ColorRulesMap loadColorsElement(Element root, String individualFolder) throws IOException, ParserConfigurationException, SAXException
	{	ColorRulesMap result = new ColorRulesMap();
		
		// folder
		result.setLocalPath(individualFolder);
		String localFilePath = individualFolder;
		String folderName = null;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		if(attribute!=null)
		{	folderName = attribute.getValue();
			localFilePath = localFilePath+File.separator+folderName;
		}
		
		// colormaps
		List<Element> clrs = root.getChildren();
		for(Element temp: clrs)
		{	String name = temp.getAttribute(XmlNames.NAME).getValue().trim();
			PredefinedColor color;
			if(name.equalsIgnoreCase(XmlNames.NONE))
				color = null;
			else 
				color = PredefinedColor.valueOf(name);
			ColorRule colorRule = null;
			// colormap
			if(temp.getName().equals(XmlNames.COLORMAP))
				colorRule = loadColormapElement(temp,localFilePath,color);
			// colorsprite
			else if(temp.getName().equals(XmlNames.COLORSPRITE))
				colorRule = loadColorspriteElement(temp,folderName,color);
			result.setColorRule(colorRule);
		}
		
		return result;
	}
	
    @SuppressWarnings("unchecked")
    public static ColorRule loadColorsElement(Element root, String individualFolder, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
    {	ColorRule result=null;
    	// folder
    	String localFilePath = individualFolder;
    	Attribute attribute = root.getAttribute(XmlNames.FOLDER);
    	if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		
    	// colormaps
    	List<Element> clrs = root.getChildren();
    	int i=0;
		while(result==null && i<clrs.size())
    	{	Element temp = clrs.get(i);
    		String name = temp.getAttribute(XmlNames.NAME).getValue().trim();
    		if(color==null && name.equals(XmlNames.NONE) 
    			|| name.equalsIgnoreCase(color.toString()))
    		{	// colormap
    			if(temp.getName().equals(XmlNames.COLORMAP))
    				result = loadColormapElement(temp,localFilePath,color);
    			// colorsprite
    			else if(temp.getName().equals(XmlNames.COLORSPRITE))
    				result = loadColorspriteElement(temp,localFilePath,color);
    		}
    		else
    			i++;
    	}
		if(result==null)
			;// erreur
		return result;
    }

    private static ColorMap loadColormapElement(Element root, String individualFolder, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
	{	// file
		String localPath = individualFolder+File.separator;
		localPath = localPath + root.getAttribute(XmlNames.FILE).getValue().trim();
		
		// colormap
		ColorMap colormap = ColorMapLoader.loadColormap(localPath,color);
		return colormap;
	}
	
	private static ColorFolder loadColorspriteElement(Element root, String folderName, PredefinedColor color) throws IOException, ParserConfigurationException, SAXException
	{	// folder
		String colorFolder = root.getAttribute(XmlNames.FOLDER).getValue().trim();
		if(folderName!=null)
			colorFolder = folderName+File.separator+colorFolder;
		ColorFolder result = new ColorFolder(color,colorFolder);
		return result;
	} 
}
