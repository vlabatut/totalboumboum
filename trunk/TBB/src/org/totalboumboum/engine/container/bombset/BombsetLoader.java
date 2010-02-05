package org.totalboumboum.engine.container.bombset;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.sprite.bomb.BombFactory;
import org.totalboumboum.engine.content.sprite.bomb.BombFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class BombsetLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD ALL BUT ANIMES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Bombset initBombset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_XML);
		Bombset result = null;
		
		// caching
		String cachePath = FilePaths.getCacheBombsPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName();
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getMemoryCache(cacheName);
		if(engineConfiguration.getMemoryCache() && o!=null)
		{	double zoomFactor = RoundVariables.zoomFactor;
			result = ((Bombset)o).cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.getFileCache() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (Bombset)oIn.readObject();
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
			result = loadBombsetElement(root,individualFolder);
			// caching
			boolean cached = false;
			if(engineConfiguration.getMemoryCache())
			{	engineConfiguration.addMemoryCache(cacheName,result);
				cached = true;
			}
			if(engineConfiguration.getFileCache())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(result);
				oOut.close();
				cached = true;
			}
			if(cached)
			{	double zoomFactor = RoundVariables.zoomFactor;
				result = result.cacheCopy(zoomFactor);
			}
		}

		return result;
	}
	
    private static Bombset loadBombsetElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init
    	Bombset result = new Bombset();

    	// abstract bombs
    	HashMap<String,BombFactory> abstractBombs = new HashMap<String,BombFactory>();
    	Element abstractBombsElt = root.getChild(XmlTools.ABSTRACT_BOMBS);
    	if(abstractBombsElt!=null)
    		loadBombsElement(abstractBombsElt,folder,result,abstractBombs,Type.ABSTRACT);
    	
    	// concrete bombs
    	Element concreteBombsElt = root.getChild(XmlTools.CONCRETE_BOMBS);
		loadBombsElement(concreteBombsElt,folder,result,abstractBombs,Type.CONCRETE);
    	
    	return result;
    }
    
	@SuppressWarnings("unchecked")
	private static void loadBombsElement(Element root, String folder, Bombset result, HashMap<String,BombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlTools.BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,result,abstractBombs,type);
    	}
	}

	private static void loadBombElement(Element root, String folder, Bombset bombset, HashMap<String,BombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// required abilities
		if(type==Type.CONCRETE)
		{	ArrayList<AbstractAbility> requiredAbilities = AbilityLoader.loadAbilitiesElement(root);
			Iterator<AbstractAbility> i = requiredAbilities.iterator();
			ArrayList<StateAbility> abilities = new ArrayList<StateAbility>();
			while(i.hasNext())
			{	AbstractAbility ablt = i.next();
				if(ablt instanceof StateAbility)
					abilities.add((StateAbility)ablt);
			}
		
			// result
			BombFactory bombFactory = BombFactoryLoader.loadBombFactory(individualFolder,name,abstractBombs);
			bombset.addBombFactory(bombFactory,abilities);
		}
		else
		{	BombFactory bombFactory = BombFactoryLoader.loadBombFactory(individualFolder,name,abstractBombs);
			abstractBombs.put(name,bombFactory);
		}
    }
    
	/////////////////////////////////////////////////////////////////
	// LOAD ONLY ANIMES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Bombset completeBombset(String folderPath, PredefinedColor color, Bombset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;
		
		// loading components
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		loadBombsetElement(root,individualFolder,color,result);
		return result;
	}
	
	private static void loadBombsetElement(Element root, String folder, PredefinedColor color, Bombset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// abstract bombs
    	HashMap<String,String> abstractBombs = new HashMap<String,String>();
    	Element abstractBombsElt = root.getChild(XmlTools.ABSTRACT_BOMBS);
    	if(abstractBombsElt!=null)
    		loadBombsElement(abstractBombsElt,folder,color,result,abstractBombs,Type.ABSTRACT);
    	
    	// concrete bombs
    	Element concreteBombsElt = root.getChild(XmlTools.CONCRETE_BOMBS);
		loadBombsElement(concreteBombsElt,folder,color,result,abstractBombs,Type.CONCRETE);
	}
    
    @SuppressWarnings("unchecked")
	private static void loadBombsElement(Element root, String folder, PredefinedColor color, Bombset result, HashMap<String,String> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlTools.BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,color,result,abstractBombs,type);
    	}
    }
    
	private static void loadBombElement(Element root, String folder, PredefinedColor color, Bombset bombset, HashMap<String,String> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// required abilities
		BombFactory bombFactory;
		if(type==Type.CONCRETE)
		{	bombFactory = bombset.getBombFactory(name);
			BombFactoryLoader.completeBombFactory(bombFactory,individualFolder,color,bombset,abstractBombs);
		}
		else
			abstractBombs.put(name,individualFolder);
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