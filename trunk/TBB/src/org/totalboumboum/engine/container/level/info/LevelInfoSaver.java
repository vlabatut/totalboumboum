package org.totalboumboum.engine.container.level.info;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class LevelInfoSaver
{	
	public static void saveLevelInfo(String folder, LevelInfo levelInfo) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException
	{	// build document
		Element root = saveLevelElement(levelInfo);	
		
		// save file
		String individualFolder = folder;
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_LEVEL+FileNames.EXTENSION_XML);
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_LEVEL+FileNames.EXTENSION_SCHEMA);
		XmlTools.makeFileFromRoot(dataFile,schemaFile,root);
	}

	private static Element saveLevelElement(LevelInfo levelInfo)
	{	Element result = new Element(XmlTools.LEVEL);
		
		// title
		Element titleElement = new Element(XmlTools.TITLE);
		titleElement.setAttribute(XmlTools.VALUE,levelInfo.getTitle());
		result.addContent(titleElement);
		// author
		Element authorElement = new Element(XmlTools.AUTHOR);
		authorElement.setAttribute(XmlTools.VALUE,levelInfo.getAuthor());
		result.addContent(authorElement);
		// source
		Element sourceElement = new Element(XmlTools.SOURCE);
		sourceElement.setAttribute(XmlTools.VALUE,levelInfo.getSource());
		result.addContent(sourceElement);
		// preview
		Element previewElement = new Element(XmlTools.PREVIEW);
		previewElement.setAttribute(XmlTools.FILE,levelInfo.getPreview());
		result.addContent(previewElement);
	
		// instance
		Element instanceElement = new Element(XmlTools.INSTANCE);
		instanceElement.setAttribute(XmlTools.NAME,levelInfo.getInstance());
		result.addContent(instanceElement);
		// theme
		Element themeElement = new Element(XmlTools.THEME);
		themeElement.setAttribute(XmlTools.NAME,levelInfo.getTheme());
		result.addContent(themeElement);
	
		// global dimension
		Element globaldimElement = new Element(XmlTools.GLOBAL_DIMENSION);
		globaldimElement.setAttribute(XmlTools.HEIGHT,Integer.toString(levelInfo.getGlobalHeight()));
		globaldimElement.setAttribute(XmlTools.WIDTH,Integer.toString(levelInfo.getGlobalWidth()));
		result.addContent(globaldimElement);
		// visible dimension
		Element visibledimElement = new Element(XmlTools.VISIBLE_DIMENSION);
		visibledimElement.setAttribute(XmlTools.HEIGHT,Integer.toString(levelInfo.getVisibleHeight()));
		visibledimElement.setAttribute(XmlTools.WIDTH,Integer.toString(levelInfo.getVisibleWidth()));
		result.addContent(visibledimElement);
		// visible position
		Element visibleposeElement = new Element(XmlTools.VISIBLE_POSITION);
		visibleposeElement.setAttribute(XmlTools.UPLINE,Integer.toString(levelInfo.getVisiblePositionUpLine()));
		visibleposeElement.setAttribute(XmlTools.LEFTCOL,Integer.toString(levelInfo.getVisiblePositionLeftCol()));
		result.addContent(visibleposeElement);
		// display
		Element displayElement = new Element(XmlTools.DISPLAY);
		displayElement.setAttribute(XmlTools.FORCE_ALL,Boolean.toString(levelInfo.getForceAll()));
		displayElement.setAttribute(XmlTools.MAXIMIZE,Boolean.toString(levelInfo.getMaximize()));
		result.addContent(displayElement);

		return result;
	}
}
