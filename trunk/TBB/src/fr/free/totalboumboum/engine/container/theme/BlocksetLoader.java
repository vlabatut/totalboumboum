package fr.free.totalboumboum.engine.container.theme;

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
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.block.BlockFactory;
import fr.free.totalboumboum.engine.content.sprite.block.BlockFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class BlocksetLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadBlockset(String folderPath, Theme result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_BLOCKSET+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_BLOCKSET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		loadBlocksetElement(root,individualFolder,result);
    }
    
	private static void loadBlocksetElement(Element root, String folder, Theme result) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// abstract blocks
    	HashMap<String,BlockFactory> abstractItems = new HashMap<String,BlockFactory>();
    	Element abstractBlocksElt = root.getChild(XmlTools.ELT_ABSTRACT_BLOCKS);
    	if(abstractBlocksElt!=null)
    		loadBlocksElement(abstractBlocksElt,folder,result,abstractItems,Type.ABSTRACT);
    	
    	// concrete blocks
    	Element concreteBlocksElt = root.getChild(XmlTools.ELT_CONCRETE_BLOCKS);
		loadBlocksElement(concreteBlocksElt,folder,result,abstractItems,Type.CONCRETE);
	}

	@SuppressWarnings("unchecked")
	private static void loadBlocksElement(Element root, String folder, Theme result, HashMap<String,BlockFactory> abstractBlocks, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// blocks
		List<Element> blcksCmpnts = root.getChildren(XmlTools.ELT_BLOCK);
		for(Element temp: blcksCmpnts)
			loadBlockElement(temp,folder,Theme.DEFAULT_GROUP,result,abstractBlocks,type);

		// groups
		List<Element> grpsCmpnts = root.getChildren(XmlTools.ELT_GROUP);
		for(Element temp: grpsCmpnts)
			loadGroupElement(temp,folder,result,abstractBlocks,type);
	}
    
    @SuppressWarnings("unchecked")
    private static void loadGroupElement(Element root, String individualFolder, Theme result, HashMap<String,BlockFactory> abstractBlocks, Type type) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// blocks
		List<Element> components = root.getChildren(XmlTools.ELT_BLOCK);
		for(Element temp: components)
			loadBlockElement(temp,localFilePath,name,result,abstractBlocks,type);
    }
    
    private static void loadBlockElement(Element root, String individualFolder, String groupName, Theme result, HashMap<String,BlockFactory> abstractBlocks, Type type) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue();
		
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// components
		boolean isAbstract = type==Type.ABSTRACT;
		BlockFactory blockFactory = BlockFactoryLoader.loadBlockFactory(localFilePath,abstractBlocks,isAbstract);
		if(isAbstract)
			abstractBlocks.put(name,blockFactory);
		else
		{	String fullname = groupName+Theme.GROUP_SEPARATOR+name;
			result.addBlockFactory(fullname,blockFactory);		
		}
    }

	/////////////////////////////////////////////////////////////////
	// LOADING TYPE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private enum Type
    {
    	ABSTRACT,
    	CONCRETE;
    }
}
