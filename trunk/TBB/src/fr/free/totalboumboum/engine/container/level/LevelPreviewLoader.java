package fr.free.totalboumboum.engine.container.level;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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
{

    public static LevelPreview loadLevelPreview(String pack, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException    
    {	// init
    	String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = FileTools.getLevelsPath()+File.separator+pack+File.separator+folder;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_LEVEL+FileTools.EXTENSION_DATA);
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
	{	// title
		Element titleElement = root.getChild(XmlTools.ELT_TITLE);
		loadTitleElement(titleElement,result);
		
		// author
		Element authorElement = root.getChild(XmlTools.ELT_AUTHOR);
		loadAuthorElement(authorElement,result);
		
		// source
		Element sourceElement = root.getChild(XmlTools.ELT_SOURCE);
		loadSourceElement(sourceElement,result);
		
		// visible size
		Element visibleDimensionElement = root.getChild(XmlTools.ELT_VISIBLE_DIMENSION);
		loadVisibleDimensionElement(visibleDimensionElement,result);

		// visual preview
		Element previewElement = root.getChild(XmlTools.ELT_PREVIEW);
		loadPreviewElement(previewElement,folder,result);
		
		// instance
		Element instanceElement = root.getChild(XmlTools.ELT_INSTANCE);
		loadInstanceElement(instanceElement,result);
		
		// players stuff preview
		PlayersPreviewer.previewPlayers(folder,result);
/*
		// zone stuff preview
		element = root.getChild(XmlTools.ELT_GLOBAL_DIMENSION);
		String globalHeightStr = element.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int globalHeight = Integer.parseInt(globalHeightStr);
		String globalWidthStr = element.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int globalWidth = Integer.parseInt(globalWidthStr);
		Zone zone = ZoneLoader.loadZone(folder,globalHeight,globalWidth);
*/
		// theme
		Element themeElement = root.getChild(XmlTools.ELT_THEME);
		loadThemeElement(themeElement,result);
		
		// itemset
		String instanceFolder = FileTools.getInstancesPath()+File.separator+result.getInstanceName();		
		String itemFolder = instanceFolder + File.separator+FileTools.FOLDER_ITEMS;
		ItemsetPreview itemsetPreview = ItemsetPreviewLoader.loadItemsetPreview(itemFolder);
		result.setItemsetPreview(itemsetPreview);
	}
    
    private static void loadTitleElement(Element root, LevelPreview result)
    {	String title = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setTitle(title);    	
    }

    private static void loadAuthorElement(Element root, LevelPreview result)
    {	String author = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(author);		   	
    }
    
    private static void loadSourceElement(Element root, LevelPreview result)
    {	String source = root.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setSource(source);
    }
    
	private static void loadVisibleDimensionElement(Element root, LevelPreview result)
	{	// height
    	String visibleHeightStr = root.getAttribute(XmlTools.ATT_HEIGHT).getValue().trim();
		int visibleHeight = Integer.parseInt(visibleHeightStr);
		result.setVisibleHeight(visibleHeight);
		// width
		String visibleWidthStr = root.getAttribute(XmlTools.ATT_WIDTH).getValue().trim();
		int visibleWidth = Integer.parseInt(visibleWidthStr);
		result.setVisibleWidth(visibleWidth);
    }
	
	private static void loadPreviewElement(Element root, String folder, LevelPreview result) throws IOException
	{	String filePath = folder+File.separator+root.getAttribute(XmlTools.ATT_FILE).getValue().trim();
		BufferedImage image = ImageTools.loadImage(filePath,null);
    	result.setVisualPreview(image);				
	}
	
	private static void loadInstanceElement(Element root, LevelPreview result)
	{	String instanceName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setInstanceName(instanceName);
	}
	
	private static void loadThemeElement(Element root, LevelPreview result)
	{	String instanceFolder = FileTools.getInstancesPath()+File.separator+result.getInstanceName();		
		String themeName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setThemeName(themeName);
		String themeFolder = instanceFolder + File.separator + FileTools.FOLDER_THEMES;
		@SuppressWarnings("unused")
		String themePath = themeFolder + File.separator+themeName;
	}
}
