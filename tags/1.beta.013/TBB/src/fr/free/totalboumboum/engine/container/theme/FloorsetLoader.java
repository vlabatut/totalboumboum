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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactory;
import fr.free.totalboumboum.engine.content.sprite.floor.FloorFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class FloorsetLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static void loadFloorset(String folderPath, Theme result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_FLOORSET+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_FLOORSET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		loadFloorsetElement(root,individualFolder,result);
    }

	private static void loadFloorsetElement(Element root, String folder, Theme result) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// abstract floors
    	HashMap<String,FloorFactory> abstractFloors = new HashMap<String,FloorFactory>();
    	Element abstractFloorsElt = root.getChild(XmlTools.ELT_ABSTRACT_FLOORS);
    	if(abstractFloorsElt!=null)
    		loadFloorsElement(abstractFloorsElt,folder,result,abstractFloors,Type.ABSTRACT);
    	
    	// concrete floors
    	Element concreteFloorsElt = root.getChild(XmlTools.ELT_CONCRETE_FLOORS);
		loadFloorsElement(concreteFloorsElt,folder,result,abstractFloors,Type.CONCRETE);
	}
    
	@SuppressWarnings("unchecked")
	private static void loadFloorsElement(Element root, String folder, Theme result, HashMap<String,FloorFactory> abstractFloors, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> floors = root.getChildren(XmlTools.ELT_FLOOR);
    	Iterator<Element> i = floors.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadFloorElement(temp,individualFolder,result,abstractFloors,type);
    	}
	}
    
	private static void loadFloorElement(Element root, String folder, Theme result, HashMap<String,FloorFactory> abstractFloors, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();

		// factory
		FloorFactory floorFactory = FloorFactoryLoader.loadFloorFactory(individualFolder,abstractFloors);
		if(type==Type.CONCRETE)
			result.addFloorFactory(name,floorFactory);
		else
			abstractFloors.put(name,floorFactory);
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