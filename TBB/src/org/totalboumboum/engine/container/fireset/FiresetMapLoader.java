package org.totalboumboum.engine.container.fireset;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.engine.content.sprite.fire.FireFactory;
import org.totalboumboum.engine.content.sprite.fire.FireFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class FiresetMapLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static FiresetMap loadFiresetMap(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = folderPath;
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_FIRESETMAP+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_FIRESETMAP+FileNames.EXTENSION_XML);
		FiresetMap result = null;
		
		// caching
		String cachePath = FilePaths.getCacheFiresPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName();
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getMemoryCache(cacheName);
		if(engineConfiguration.getMemoryCache() && o!=null)
		{	double zoomFactor = RoundVariables.zoomFactor;
			result = ((FiresetMap)o).cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.getFileCache() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (FiresetMap)oIn.readObject(); //TODO fonction à surcharger
				//result.setInstance(instance); 
				oIn.close();
			}
			catch (FileNotFoundException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
		}
		
		if(result==null)
		{	// opening
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			result = loadFiresetmapElement(individualFolder,root);
			// caching
			if(engineConfiguration.getMemoryCache())
			{	engineConfiguration.addMemoryCache(cacheName,result);
			}
			if(engineConfiguration.getFileCache())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(result);
				oOut.close();
			}
		}

		// set final size
		double zoomFactor = RoundVariables.zoomFactor;
		result = result.cacheCopy(zoomFactor);

		return result;
	}

	private static FiresetMap loadFiresetmapElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	FiresetMap result = new FiresetMap();		
		HashMap<String,FireFactory> abstractFires = new HashMap<String, FireFactory>();
		
    	// abstract fires
    	Element abstractFiresElt = root.getChild(XmlNames.ABSTRACT_FIRES);
    	if(abstractFiresElt!=null)
    		loadFiresElement(folder,abstractFiresElt,null,abstractFires,Type.ABSTRACT);

    	// firesets
    	Element firesetsElt = root.getChild(XmlNames.FIRESETS);
    	loadFiresetsElement(folder,firesetsElt,result,abstractFires);
    	
    	return result;
	}
	
	@SuppressWarnings("unchecked")
	private static void loadFiresetsElement(String folder, Element root, FiresetMap result, HashMap<String,FireFactory> abstractFires) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	List<Element> elts = root.getChildren(XmlNames.FIRESET);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		String name = temp.getAttribute(XmlNames.NAME).getValue().trim();
			Attribute attribute = temp.getAttribute(XmlNames.FOLDER);
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
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_FIRESET+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_FIRESET+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		HashMap<String,FireFactory> abstractFiresCpy = (HashMap<String,FireFactory>) abstractFires.clone(); 
		Fireset result = loadFiresetElement(individualFolder,root,abstractFiresCpy);
		return result;
	}
	
	private static Fireset loadFiresetElement(String folder, Element root, HashMap<String,FireFactory> abstractFires) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	Fireset result = new Fireset();
		
    	// abstract fires
    	Element abstractFiresElt = root.getChild(XmlNames.ABSTRACT_FIRES);
    	if(abstractFiresElt!=null)
    		loadFiresElement(folder,abstractFiresElt,result,abstractFires,Type.ABSTRACT);

    	// concrete fires
    	Element concreteFiresElt = root.getChild(XmlNames.CONCRETE_FIRES);
		loadFiresElement(folder,concreteFiresElt,result,abstractFires,Type.CONCRETE);
    	
    	return result;
    }

	@SuppressWarnings("unchecked")
	private static void loadFiresElement(String folder, Element root, Fireset result, HashMap<String,FireFactory> abstractFires, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
		List<Element> elts = root.getChildren(XmlNames.FIRE);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
			loadFireElement(individualFolder,temp,result,abstractFires,type);
    	}
    }
    
    private static void loadFireElement(String folder, Element root, Fireset result, HashMap<String,FireFactory> abstractFires, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
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
