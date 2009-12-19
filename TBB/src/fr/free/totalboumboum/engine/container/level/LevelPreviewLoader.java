package fr.free.totalboumboum.engine.container.level;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.itemset.ItemsetPreview;
import fr.free.totalboumboum.engine.container.itemset.ItemsetPreviewLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.ImageTools;
import fr.free.totalboumboum.tools.XmlTools;

public class LevelPreviewLoader
{	private static boolean previewBasics;
	private static boolean previewItemset;
	private static boolean previewPlayers;
	private static boolean previewAllowedPlayersOnly;
	private static boolean previewImage;

    public static LevelPreview loadLevelPreview(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// parameters
    	previewBasics = true;
    	previewItemset = true;
    	previewPlayers = true;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
    	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;
    }
    
    public static LevelPreview loadLevelPreviewWithoutItemset(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// parameters
    	previewBasics = true;
    	previewItemset = false;
    	previewPlayers = true;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
    	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;
    }
    
    public static LevelPreview loadLevelPreviewOnlyAllowedPlayers(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// parameters
    	previewBasics = false;
    	previewItemset = false;
    	previewPlayers = false;
    	previewAllowedPlayersOnly = true;
    	previewImage = false;
    	// load
       	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;    	
    }

    public static LevelPreview loadLevelPreviewOnlyImage(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// parameters
    	previewBasics = false;
    	previewItemset = false;
    	previewPlayers = false;
    	previewAllowedPlayersOnly = false;
    	previewImage = true;
    	// load
       	LevelPreview result = loadLevelPreviewCommon(pack,folder);
		return result;    	
    }

	private static LevelPreview loadLevelPreviewCommon(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
   		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+pack+File.separator+folder;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		LevelPreview result = new LevelPreview();
		result.setPack(pack);
		result.setFolder(folder);
		loadLevelElement(individualFolder,root,result);
		
		return result;
	}
   
	private static void loadLevelElement(String folder, Element root, LevelPreview result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	if(previewBasics)
		{	// title
			Element titleElement = root.getChild(XmlTools.TITLE);
			loadTitleElement(titleElement,result);
			
			// author
			Element authorElement = root.getChild(XmlTools.AUTHOR);
			loadAuthorElement(authorElement,result);
			
			// source
			Element sourceElement = root.getChild(XmlTools.SOURCE);
			loadSourceElement(sourceElement,result);
			
			// visible size
			Element visibleDimensionElement = root.getChild(XmlTools.VISIBLE_DIMENSION);
			loadVisibleDimensionElement(visibleDimensionElement,result);
	
			// instance
			Element instanceElement = root.getChild(XmlTools.INSTANCE);
			loadInstanceElement(instanceElement,result);

			// theme
			Element themeElement = root.getChild(XmlTools.THEME);
			loadThemeElement(themeElement,result);
		}
		
		// visual preview
		if(previewImage)
		{	Element previewElement = root.getChild(XmlTools.PREVIEW);
			loadPreviewElement(previewElement,folder,result);
		}
	
		// players stuff preview
		if(previewPlayers)
			PlayersPreviewer.loadPlayers(folder,result);		
		else if(previewAllowedPlayersOnly)
			PlayersPreviewer.loadPlayersAllowed(folder,result);		
		
/*
		// zone stuff preview
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		String globalHeightStr = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int globalHeight = Integer.parseInt(globalHeightStr);
		String globalWidthStr = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int globalWidth = Integer.parseInt(globalWidthStr);
		Zone zone = ZoneLoader.loadZone(folder,globalHeight,globalWidth);
*/
		// itemset
		if(previewItemset)
		{	String instanceFolder = FileTools.getInstancesPath()+File.separator+result.getInstanceName();		
			String itemFolder = instanceFolder + File.separator+FileTools.FOLDER_ITEMS;
			ItemsetPreview itemsetPreview = ItemsetPreviewLoader.loadItemsetPreview(itemFolder);
			result.setItemsetPreview(itemsetPreview);
		}
	}
    
    private static void loadTitleElement(Element root, LevelPreview result)
    {	String title = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setTitle(title);    	
    }

    private static void loadAuthorElement(Element root, LevelPreview result)
    {	String author = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setAuthor(author);		   	
    }
    
    private static void loadSourceElement(Element root, LevelPreview result)
    {	String source = root.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setSource(source);
    }
    
	private static void loadVisibleDimensionElement(Element root, LevelPreview result)
	{	// height
    	String visibleHeightStr = root.getAttribute(XmlTools.HEIGHT).getValue().trim();
		int visibleHeight = Integer.parseInt(visibleHeightStr);
		result.setVisibleHeight(visibleHeight);
		// width
		String visibleWidthStr = root.getAttribute(XmlTools.WIDTH).getValue().trim();
		int visibleWidth = Integer.parseInt(visibleWidthStr);
		result.setVisibleWidth(visibleWidth);
    }
	
	private static void loadPreviewElement(Element root, String folder, LevelPreview result) throws IOException
	{	// name
		String filename = root.getAttribute(XmlTools.FILE).getValue().trim();
    	result.setPreviewFile(filename);
		// image
    	String filePath = folder+File.separator+filename;
		BufferedImage image = ImageTools.loadImage(filePath,null);
    	result.setVisualPreview(image);
	}
	
	private static void loadInstanceElement(Element root, LevelPreview result)
	{	String instanceName = root.getAttribute(XmlTools.NAME).getValue().trim();
		result.setInstanceName(instanceName);
	}
	
	private static void loadThemeElement(Element root, LevelPreview result)
	{	String instanceFolder = FileTools.getInstancesPath()+File.separator+result.getInstanceName();		
		String themeName = root.getAttribute(XmlTools.NAME).getValue().trim();
		result.setThemeName(themeName);
		String themeFolder = instanceFolder + File.separator + FileTools.FOLDER_THEMES;
		@SuppressWarnings("unused")
		String themePath = themeFolder + File.separator+themeName;
	}
}
