package fr.free.totalboumboum.engine.container.theme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_DATA);
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
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// blocks
		element = XmlTools.getChildElement(root,XmlTools.ELT_BLOCKS);
		HashMap<String,BlockFactory> blocks = loadBlocksElement(element,localFilePath,level);
		// floors
		element = XmlTools.getChildElement(root,XmlTools.ELT_FLOORS);
		HashMap<String,FloorFactory> floors = loadFloorsElement(element,localFilePath,level);
		// theme
		result = new Theme(blocks,floors);
		return result;
	}

    private static HashMap<String,BlockFactory> loadBlocksElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,BlockFactory> result = new HashMap<String,BlockFactory>();
    	// folder
    	String localFilePath = individualFolder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// groups
		{	ArrayList<Element> components = XmlTools.getChildElements(root,XmlTools.ELT_GROUP);
			Iterator<Element> i = components.iterator();
			while(i.hasNext())
			{	Element temp = i.next();	
				HashMap<String,BlockFactory> factories = loadGroupElement(temp,localFilePath,level);
				result.putAll(factories);
			}
		}
		// blocks
		{	ArrayList<Element> components = XmlTools.getChildElements(root,XmlTools.ELT_BLOCK);
			Iterator<Element> i = components.iterator();
			while(i.hasNext())
			{	Element temp = i.next();	
				loadBlockElement(temp,localFilePath,Theme.DEFAULT_GROUP,result,level);
			}
		}
		return result;
    }
    
    private static HashMap<String,BlockFactory> loadGroupElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,BlockFactory> result = new HashMap<String,BlockFactory>();
		// folder
    	String localFilePath = individualFolder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME);
		// blocks
		ArrayList<Element> components = XmlTools.getChildElements(root,XmlTools.ELT_BLOCK);
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
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// components
		//String content = root.getTextContent().trim();
		BlockFactory blockFactory = BlockFactoryLoader.loadBlockFactory(localFilePath,level);
		// name
		String name = groupName+Theme.GROUP_SEPARATOR+root.getAttribute(XmlTools.ATT_NAME);
		blockFactories.put(name,blockFactory);
    }

    private static HashMap<String,FloorFactory> loadFloorsElement(Element root, String individualFolder, Level level) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	HashMap<String,FloorFactory> result = new HashMap<String,FloorFactory>();
		// folder
    	String localFilePath = individualFolder;
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// floors
		ArrayList<Element> components = XmlTools.getChildElements(root,XmlTools.ELT_FLOOR);
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
		if(root.hasAttribute(XmlTools.ATT_FOLDER))
			localFilePath = localFilePath+File.separator+root.getAttribute(XmlTools.ATT_FOLDER);
		// components
		//String content = root.getTextContent().trim();
		FloorFactory floorFactory = FloorFactoryLoader.loadFloorFactory(localFilePath,level);
		// name
		String name = root.getAttribute(XmlTools.ATT_NAME);
		floorFactories.put(name,floorFactory);
    }
}
