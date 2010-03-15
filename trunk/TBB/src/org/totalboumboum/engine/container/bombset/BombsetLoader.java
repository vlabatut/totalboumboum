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
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class BombsetLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD ALL BUT ANIMES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Bombset initBombset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		//double zoomFactor = RoundVariables.zoomFactor;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_XML);
		Bombset result = null;
		
		// caching
		String cachePath = FilePaths.getCacheBombsPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName()+"_"+"abstract";
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromMemoryCache(cachePath);
		if(engineConfiguration.getMemoryCached() && o!=null)
		{	result = ((Bombset)o);
			//result = result.cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.getFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (Bombset)oIn.readObject();
				oIn.close();
				//result = result.cacheCopy(zoomFactor);
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
			if(engineConfiguration.getMemoryCached())
			{	engineConfiguration.addToMemoryCache(cachePath,result);
				//result = result.cacheCopy(zoomFactor);
			}
			if(engineConfiguration.getFileCached())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(result);
				oOut.close();
				//result = result.cacheCopy(zoomFactor);
			}
		}

		return result;
	}
	
    private static Bombset loadBombsetElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init
    	Bombset result = new Bombset();

    	// abstract bombs
    	HashMap<String,BombFactory> abstractBombs = new HashMap<String,BombFactory>();
    	Element abstractBombsElt = root.getChild(XmlNames.ABSTRACT_BOMBS);
    	if(abstractBombsElt!=null)
    		loadBombsElement(abstractBombsElt,folder,result,abstractBombs,Type.ABSTRACT);
    	
    	// concrete bombs
    	Element concreteBombsElt = root.getChild(XmlNames.CONCRETE_BOMBS);
		loadBombsElement(concreteBombsElt,folder,result,abstractBombs,Type.CONCRETE);
    	
    	return result;
    }
    
	@SuppressWarnings("unchecked")
	private static void loadBombsElement(Element root, String folder, Bombset result, HashMap<String,BombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlNames.BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,result,abstractBombs,type);
    	}
	}

	private static void loadBombElement(Element root, String folder, Bombset bombset, HashMap<String,BombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
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
	public static Bombset completeBombset(String folderPath, PredefinedColor color, Bombset base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		double zoomFactor = RoundVariables.zoomFactor;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_XML);
		Bombset result = null;
		
		// caching
		String cachePath = FilePaths.getCacheBombsPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String c = "none";
		if(color!=null)
			c = color.toString();
		String cacheName = packFile.getName()+"_"+c;
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromMemoryCache(cachePath);
		if(engineConfiguration.getMemoryCached() && o!=null)
		{	result = ((Bombset)o).deepCopy(zoomFactor,color);
		}
		else if(engineConfiguration.getFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (Bombset)oIn.readObject();
				oIn.close();
				result = result.deepCopy(zoomFactor,color);
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
		{	result = base;
			// opening
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			loadBombsetElement(root,individualFolder,color,result);
			// caching
			if(engineConfiguration.getMemoryCached())
			{	engineConfiguration.addToMemoryCache(cachePath,result);
				result = result.deepCopy(zoomFactor,color);
			}
			if(engineConfiguration.getFileCached())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(result);
				oOut.close();
				result = result.deepCopy(zoomFactor,color);
			}
		}
		
		return result;
	}
	
	private static void loadBombsetElement(Element root, String folder, PredefinedColor color, Bombset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// abstract bombs
    	HashMap<String,String> abstractBombs = new HashMap<String,String>();
    	Element abstractBombsElt = root.getChild(XmlNames.ABSTRACT_BOMBS);
    	if(abstractBombsElt!=null)
    		loadBombsElement(abstractBombsElt,folder,color,result,abstractBombs,Type.ABSTRACT);
    	
    	// concrete bombs
    	Element concreteBombsElt = root.getChild(XmlNames.CONCRETE_BOMBS);
		loadBombsElement(concreteBombsElt,folder,color,result,abstractBombs,Type.CONCRETE);
	}
    
    @SuppressWarnings("unchecked")
	private static void loadBombsElement(Element root, String folder, PredefinedColor color, Bombset result, HashMap<String,String> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlNames.BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,color,result,abstractBombs,type);
    	}
    }
    
	private static void loadBombElement(Element root, String folder, PredefinedColor color, Bombset bombset, HashMap<String,String> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
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