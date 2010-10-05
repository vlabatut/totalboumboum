package org.totalboumboum.engine.container.theme;

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
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.engine.content.sprite.block.HollowBlockFactory;
import org.totalboumboum.engine.content.sprite.block.HollowBlockFactoryLoader;
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
public class BlocksetLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadBlockset(String folderPath, HollowTheme result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_BLOCKSET+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BLOCKSET+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		loadBlocksetElement(root,individualFolder,result);
    }
    
	private static void loadBlocksetElement(Element root, String folder, HollowTheme result) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// abstract blocks
    	HashMap<String,HollowBlockFactory> abstractBlocks = new HashMap<String,HollowBlockFactory>();
    	Element abstractBlocksElt = root.getChild(XmlNames.ABSTRACT_BLOCKS);
    	if(abstractBlocksElt!=null)
    		loadBlocksElement(abstractBlocksElt,folder,result,abstractBlocks,Type.ABSTRACT);
    	
    	// concrete blocks
    	Element concreteBlocksElt = root.getChild(XmlNames.CONCRETE_BLOCKS);
		loadBlocksElement(concreteBlocksElt,folder,result,abstractBlocks,Type.CONCRETE);
	}

	@SuppressWarnings("unchecked")
	private static void loadBlocksElement(Element root, String folder, HollowTheme result, HashMap<String,HollowBlockFactory> abstractBlocks, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// blocks
		List<Element> blcksCmpnts = root.getChildren(XmlNames.BLOCK);
		for(Element temp: blcksCmpnts)
			loadBlockElement(temp,folder,Theme.DEFAULT_GROUP,result,abstractBlocks,type);

		// groups
		List<Element> grpsCmpnts = root.getChildren(XmlNames.GROUP);
		for(Element temp: grpsCmpnts)
			loadGroupElement(temp,folder,result,abstractBlocks,type);
	}
    
    @SuppressWarnings("unchecked")
    private static void loadGroupElement(Element root, String individualFolder, HollowTheme result, HashMap<String,HollowBlockFactory> abstractBlocks, Type type) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue();
		
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// blocks
		List<Element> components = root.getChildren(XmlNames.BLOCK);
		for(Element temp: components)
			loadBlockElement(temp,localFilePath,name,result,abstractBlocks,type);
    }
    
    private static void loadBlockElement(Element root, String individualFolder, String groupName, HollowTheme result, HashMap<String,HollowBlockFactory> abstractBlocks, Type type) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue();
		
		// folder
    	String localFilePath = individualFolder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		localFilePath = localFilePath+File.separator+attribute.getValue();
		
		// components
		boolean isAbstract = type==Type.ABSTRACT;
		HollowBlockFactory blockFactory = HollowBlockFactoryLoader.loadBlockFactory(localFilePath,abstractBlocks,isAbstract);
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
