package fr.free.totalboumboum.engine.container.theme;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.content.sprite.block.BlockFactory;
import fr.free.totalboumboum.engine.content.sprite.block.BlockFactoryLoader;
import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactory;
import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ThemeLoader
{	
	public static Theme loadTheme(String folderPath, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;	
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// theme
		Theme result = loadThemeElement(root,individualFolder,level);
		return result;
    }
    
    private static Theme loadThemeElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	Theme result;
    	Element element;
		// folder
		String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// blocks
		element = root.getChild(XmlTools.ELT_BLOCKS);
		HashMap<String,BlockFactory> blocks = loadBlocksElement(element,localFilePath,level);
		// floors
		element = root.getChild(XmlTools.ELT_FLOORS);
		HashMap<String,FloorFactory> floors = loadFloorsElement(element,localFilePath,level);
		// theme
		result = new Theme(blocks,floors);
		return result;
	}

    @SuppressWarnings("unchecked")
    private static HashMap<String,BlockFactory> loadBlocksElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,BlockFactory> result = new HashMap<String,BlockFactory>();
    	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// groups
		{	List<Element> components = root.getChildren(XmlTools.ELT_GROUP);
			Iterator<Element> i = components.iterator();
			while(i.hasNext())
			{	Element temp = i.next();	
				HashMap<String,BlockFactory> factories = loadGroupElement(temp,localFilePath,level);
				result.putAll(factories);
			}
		}
		// blocks
		{	List<Element> components = root.getChildren(XmlTools.ELT_BLOCK);
			Iterator<Element> i = components.iterator();
			while(i.hasNext())
			{	Element temp = i.next();	
				loadBlockElement(temp,localFilePath,Theme.DEFAULT_GROUP,result,level);
			}
		}
		return result;
    }
    
    @SuppressWarnings("unchecked")
    private static HashMap<String,BlockFactory> loadGroupElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,BlockFactory> result = new HashMap<String,BlockFactory>();
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		// blocks
		List<Element> components = root.getChildren(XmlTools.ELT_BLOCK);
		Iterator<Element> i = components.iterator();
		while(i.hasNext())
		{	Element temp = i.next();	
			loadBlockElement(temp,localFilePath,name,result,level);
		}
		//
		return result;
    }
    
    private static void loadBlockElement(Element root, String individualFolder, String groupName, HashMap<String,BlockFactory> blockFactories, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// components
		//String content = root.getTextContent().trim();
		BlockFactory blockFactory = BlockFactoryLoader.loadBlockFactory(localFilePath,level);
		// name
		String name = groupName+Theme.GROUP_SEPARATOR+root.getAttribute(XmlTools.ATT_NAME).getValue();
		blockFactories.put(name,blockFactory);
    }

    @SuppressWarnings("unchecked")
    private static HashMap<String,FloorFactory> loadFloorsElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,FloorFactory> result = new HashMap<String,FloorFactory>();
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// floors
		List<Element> components = root.getChildren(XmlTools.ELT_FLOOR);
		Iterator<Element> i = components.iterator();
		while(i.hasNext())
		{	Element temp = i.next();	
			loadFloorElement(temp,localFilePath,result,level);
		}
		//
		return result;
    }
    
    private static void loadFloorElement(Element root, String individualFolder, HashMap<String,FloorFactory> floorFactories, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
			localFilePath = localFilePath+File.separator+attribute.getValue();
		// components
		//String content = root.getTextContent().trim();
		FloorFactory floorFactory = FloorFactoryLoader.loadFloorFactory(localFilePath,level);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		floorFactories.put(name,floorFactory);
    }
}
