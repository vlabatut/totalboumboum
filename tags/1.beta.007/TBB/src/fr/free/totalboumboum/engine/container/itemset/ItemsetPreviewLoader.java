package fr.free.totalboumboum.engine.container.itemset;

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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.SpritePreview;
import fr.free.totalboumboum.engine.content.sprite.SpritePreviewLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ItemsetPreviewLoader
{	
	public static ItemsetPreview loadItemsetPreview(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_ITEMSET+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_ITEMSET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		ItemsetPreview result = loadItemsetElement(root,individualFolder);
		return result;
    }
    
    @SuppressWarnings("unchecked")
    private static ItemsetPreview loadItemsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	ItemsetPreview result = new ItemsetPreview();
    	String individualFolder = folder;
		List<Element> items = root.getChildren(XmlTools.ELT_ITEM);	
		Iterator<Element> i = items.iterator();
		while(i.hasNext())
		{	Element temp = i.next();
			loadItemElement(temp,individualFolder,result);
		}
		return result;
	}
    
    private static void loadItemElement(Element root, String folder, ItemsetPreview result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		// preview
		SpritePreview itemPreview = SpritePreviewLoader.loadSpritePreview(individualFolder);
		result.putItemPreview(name,itemPreview);
    }     
}