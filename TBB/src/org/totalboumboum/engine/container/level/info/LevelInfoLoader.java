package org.totalboumboum.engine.container.level.info;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.tools.FileTools;
import org.totalboumboum.tools.XmlTools;
import org.xml.sax.SAXException;


public class LevelInfoLoader
{	
	public static LevelInfo loadLevelInfo(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
   		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+pack+File.separator+folder;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		LevelInfo result = new LevelInfo();
		result.setPack(pack);
		result.setFolder(folder);
		loadLevelElement(individualFolder,root,result);
		
		return result;
	}
   
	private static void loadLevelElement(String folder, Element root, LevelInfo result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// title
		Element titleElement = root.getChild(XmlTools.TITLE);
		loadTitleElement(titleElement,result);
		
		// author
		Element authorElement = root.getChild(XmlTools.AUTHOR);
		loadAuthorElement(authorElement,result);
		
		// source
		Element sourceElement = root.getChild(XmlTools.SOURCE);
		loadSourceElement(sourceElement,result);
		
		// preview
		Element previewElement = root.getChild(XmlTools.PREVIEW);
		loadPreviewElement(previewElement,folder,result);

		// instance
		Element instanceElement = root.getChild(XmlTools.INSTANCE);
		loadInstanceElement(instanceElement,result);

		// theme
		Element themeElement = root.getChild(XmlTools.THEME);
		loadThemeElement(themeElement,result);

		// global size
		Element globalDimensionElement = root.getChild(XmlTools.GLOBAL_DIMENSION);
		loadGlobalDimensionElement(globalDimensionElement,result);

		// visible size
		Element visibleDimensionElement = root.getChild(XmlTools.VISIBLE_DIMENSION);
		loadVisibleDimensionElement(visibleDimensionElement,result);

		// visible position
		Element visiblePositionElement = root.getChild(XmlTools.VISIBLE_POSITION);
		loadVisiblePositionElement(visiblePositionElement,result);

		// display options
		Element displayElement = root.getChild(XmlTools.DISPLAY);
		loadDisplayElement(displayElement,result);
	}
    
    private static void loadTitleElement(Element root, LevelInfo result)
    {	String title = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setTitle(title);    	
    }

    private static void loadAuthorElement(Element root, LevelInfo result)
    {	String author = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setAuthor(author);		   	
    }
    
    private static void loadSourceElement(Element root, LevelInfo result)
    {	String source = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setSource(source);
    }
    
	private static void loadPreviewElement(Element root, String folder, LevelInfo result) throws IOException
	{	String preview = root.getAttribute(XmlTools.FILE).getValue().trim();
    	result.setPreview(preview);
	}
	
	private static void loadInstanceElement(Element root, LevelInfo result)
	{	String instance = root.getAttribute(XmlTools.NAME).getValue().trim();
		result.setInstance(instance);
	}
	
	private static void loadThemeElement(Element root, LevelInfo result)
	{	String theme = root.getAttribute(XmlTools.NAME).getValue().trim();
		result.setTheme(theme);
	}

	private static void loadGlobalDimensionElement(Element root, LevelInfo result)
	{	// height
    	String globalHeightStr = root.getAttribute(XmlTools.HEIGHT).getValue().trim();
		int globalHeight = Integer.parseInt(globalHeightStr);
		result.setGlobalHeight(globalHeight);
		// width
		String globalWidthStr = root.getAttribute(XmlTools.WIDTH).getValue().trim();
		int globalWidth = Integer.parseInt(globalWidthStr);
		result.setGlobalWidth(globalWidth);
    }
    
	private static void loadVisibleDimensionElement(Element root, LevelInfo result)
	{	// height
    	String visibleHeightStr = root.getAttribute(XmlTools.HEIGHT).getValue().trim();
		int visibleHeight = Integer.parseInt(visibleHeightStr);
		result.setVisibleHeight(visibleHeight);
		// width
		String visibleWidthStr = root.getAttribute(XmlTools.WIDTH).getValue().trim();
		int visibleWidth = Integer.parseInt(visibleWidthStr);
		result.setVisibleWidth(visibleWidth);
    }
	
	private static void loadVisiblePositionElement(Element root, LevelInfo result)
	{	// up line
    	String upLineStr = root.getAttribute(XmlTools.UPLINE).getValue().trim();
		int upLine = Integer.parseInt(upLineStr);
		result.setVisiblePositionUpLine(upLine);
		// left column
		String leftColStr = root.getAttribute(XmlTools.LEFTCOL).getValue().trim();
		int leftCol = Integer.parseInt(leftColStr);
		result.setVisiblePositionLeftCol(leftCol);
    }
	
	private static void loadDisplayElement(Element root, LevelInfo result)
	{	// force all
    	String forceAllStr = root.getAttribute(XmlTools.FORCE_ALL).getValue().trim();
		boolean forceAll = Boolean.parseBoolean(forceAllStr);
		result.setForceAll(forceAll);
		// maximize
		String maximizeStr = root.getAttribute(XmlTools.MAXIMIZE).getValue().trim();
		boolean maximize = Boolean.parseBoolean(maximizeStr);
		result.setMaximize(maximize);
    }
}
