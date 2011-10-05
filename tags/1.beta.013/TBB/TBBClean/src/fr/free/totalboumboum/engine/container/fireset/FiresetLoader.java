package fr.free.totalboumboum.engine.container.fireset;

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

import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class FiresetLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static FiresetMap loadFiresetMap(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = folderPath;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_FIRESETMAP+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_FIRESETMAP+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		FiresetMap result = loadFiresetmapElement(individualFolder,root);

		return result;
	}

	private static FiresetMap loadFiresetmapElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	FiresetMap result = new FiresetMap();
		HashMap<String,FireFactory> abstractFires = new HashMap<String, FireFactory>();
		
    	// abstract fires
    	Element abstractFiresElt = root.getChild(XmlTools.ELT_ABSTRACT_FIRES);
    	if(abstractFiresElt!=null)
    		loadFiresElement(folder,abstractFiresElt,null,abstractFires,Type.ABSTRACT);

    	// firesets
    	Element firesetsElt = root.getChild(XmlTools.ELT_FIRESETS);
    	loadFiresetsElement(folder,firesetsElt,result,abstractFires);
    	
    	return result;
    }
	
	@SuppressWarnings("unchecked")
	private static void loadFiresetsElement(String folder, Element root, FiresetMap result, HashMap<String,FireFactory> abstractFires) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	List<Element> elts = root.getChildren(XmlTools.ELT_FIRESET);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
			Attribute attribute = temp.getAttribute(XmlTools.ATT_FOLDER);
			String individualFolder = folder+File.separator+attribute.getValue().trim();
			Fireset fireset = loadFireset(individualFolder,abstractFires);
			fireset.setName(name);
			result.addFireset(name,fireset);
    	}
    }

	/////////////////////////////////////////////////////////////////
	// LOAD FIRE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	private static Fireset loadFireset(String folderPath, HashMap<String,FireFactory> abstractFires) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = folderPath;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		HashMap<String,FireFactory> abstractFiresCpy = (HashMap<String,FireFactory>) abstractFires.clone(); 
		Fireset result = loadFiresetElement(individualFolder,root,abstractFiresCpy);
		return result;
	}
	
	private static Fireset loadFiresetElement(String folder, Element root, HashMap<String,FireFactory> abstractFires) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Fireset result = new Fireset();
		
    	// abstract fires
    	Element abstractFiresElt = root.getChild(XmlTools.ELT_ABSTRACT_FIRES);
    	if(abstractFiresElt!=null)
    		loadFiresElement(folder,abstractFiresElt,result,abstractFires,Type.ABSTRACT);

    	// concrete fires
    	Element concreteFiresElt = root.getChild(XmlTools.ELT_CONCRETE_FIRES);
		loadFiresElement(folder,concreteFiresElt,result,abstractFires,Type.CONCRETE);
    	
    	return result;
    }

	@SuppressWarnings("unchecked")
	private static void loadFiresElement(String folder, Element root, Fireset result, HashMap<String,FireFactory> abstractFires, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
		List<Element> elts = root.getChildren(XmlTools.ELT_FIRE);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
			loadFireElement(individualFolder,temp,result,abstractFires,type);
    	}
    }
    
    private static void loadFireElement(String folder, Element root, Fireset result, HashMap<String,FireFactory> abstractFires, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// fire factory
		boolean isAbstract = type==Type.ABSTRACT;
		FireFactory fireFactory = FireFactoryLoader.loadFireFactory(individualFolder,abstractFires,isAbstract);
		if(isAbstract)
			abstractFires.put(name,fireFactory);
		else
			result.addFireFactory(name,fireFactory);
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