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
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
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
	{	Element result = new Element(XmlNames.LEVEL);
		
		// title
		Element titleElement = new Element(XmlNames.TITLE);
		titleElement.setAttribute(XmlNames.VALUE,levelInfo.getTitle());
		result.addContent(titleElement);
		// author
		Element authorElement = new Element(XmlNames.AUTHOR);
		authorElement.setAttribute(XmlNames.VALUE,levelInfo.getAuthor());
		result.addContent(authorElement);
		// source
		Element sourceElement = new Element(XmlNames.SOURCE);
		sourceElement.setAttribute(XmlNames.VALUE,levelInfo.getSource());
		result.addContent(sourceElement);
		// preview
		Element previewElement = new Element(XmlNames.PREVIEW);
		previewElement.setAttribute(XmlNames.FILE,levelInfo.getPreview());
		result.addContent(previewElement);
	
		// instance
		Element instanceElement = new Element(XmlNames.INSTANCE);
		instanceElement.setAttribute(XmlNames.NAME,levelInfo.getInstanceName());
		result.addContent(instanceElement);
		// theme
		Element themeElement = new Element(XmlNames.THEME);
		themeElement.setAttribute(XmlNames.NAME,levelInfo.getThemeName());
		result.addContent(themeElement);
	
		// global dimension
		Element globaldimElement = new Element(XmlNames.GLOBAL_DIMENSION);
		globaldimElement.setAttribute(XmlNames.HEIGHT,Integer.toString(levelInfo.getGlobalHeight()));
		globaldimElement.setAttribute(XmlNames.WIDTH,Integer.toString(levelInfo.getGlobalWidth()));
		result.addContent(globaldimElement);
		// visible dimension
		Element visibledimElement = new Element(XmlNames.VISIBLE_DIMENSION);
		visibledimElement.setAttribute(XmlNames.HEIGHT,Integer.toString(levelInfo.getVisibleHeight()));
		visibledimElement.setAttribute(XmlNames.WIDTH,Integer.toString(levelInfo.getVisibleWidth()));
		result.addContent(visibledimElement);
		// visible position
		Element visibleposeElement = new Element(XmlNames.VISIBLE_POSITION);
		visibleposeElement.setAttribute(XmlNames.UPLINE,Integer.toString(levelInfo.getVisiblePositionUpLine()));
		visibleposeElement.setAttribute(XmlNames.LEFTCOL,Integer.toString(levelInfo.getVisiblePositionLeftCol()));
		result.addContent(visibleposeElement);
		// display
		Element displayElement = new Element(XmlNames.DISPLAY);
		displayElement.setAttribute(XmlNames.FORCE_ALL,Boolean.toString(levelInfo.getForceAll()));
		displayElement.setAttribute(XmlNames.MAXIMIZE,Boolean.toString(levelInfo.getMaximize()));
		result.addContent(displayElement);

		return result;
	}
}
