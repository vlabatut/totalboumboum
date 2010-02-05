package org.totalboumboum.engine.container.itemset;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.sprite.item.ItemFactory;
import org.totalboumboum.engine.content.sprite.item.ItemFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class ItemsetLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD ALL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Itemset loadItemset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ITEMSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_ITEMSET+FileNames.EXTENSION_XML);
		Itemset result = null;
		
		// caching
		String cachePath = FilePaths.getCacheItemsPath()+ File.separator;
		File cacheFolder = new File(cachePath);
		cacheFolder.mkdirs();
		File objectFile = dataFile.getParentFile();
		String objectName = objectFile.getName();
		File packFile = objectFile.getParentFile().getParentFile();
		String packName = packFile.getName();
		String cacheName = packName+"_"+objectName;
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getMemoryCache(cacheName);
		if(engineConfiguration.getMemoryCache() && o!=null)
		{	double zoomFactor = RoundVariables.zoomFactor;
			result = ((Itemset)o).cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.getFileCache() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (Itemset)oIn.readObject();
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
			result = loadItemsetElement(root,individualFolder);
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
    
	private static Itemset loadItemsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
		Itemset result = new Itemset();

		// abstract items
    	HashMap<String,ItemFactory> abstractItems = new HashMap<String,ItemFactory>();
    	Element abstractItemsElt = root.getChild(XmlTools.ABSTRACT_ITEMS);
    	if(abstractItemsElt!=null)
    		loadItemsElement(abstractItemsElt,folder,result,abstractItems,Type.ABSTRACT);
    	
    	// concrete items
    	Element concreteItemsElt = root.getChild(XmlTools.CONCRETE_ITEMS);
		loadItemsElement(concreteItemsElt,folder,result,abstractItems,Type.CONCRETE);
		
		return result;
	}
    
	@SuppressWarnings("unchecked")
	private static void loadItemsElement(Element root, String folder, Itemset result, HashMap<String,ItemFactory> abstractItems, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> items = root.getChildren(XmlTools.ITEM);
		for(Element temp: items)
    		loadItemElement(temp,individualFolder,result,abstractItems,type);
	}
    
	@SuppressWarnings("unchecked")
	private static void loadItemElement(Element root, String folder, Itemset result, HashMap<String,ItemFactory> abstractItems, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// abilities
		ArrayList<ArrayList<AbstractAbility>> abilities = new ArrayList<ArrayList<AbstractAbility>>();
		ArrayList<Float> probabilities = new ArrayList<Float>();
		List<Element> elements = root.getChildren(XmlTools.ABILITIES);
		for(Element e: elements)
			loadAbilitiesElement(e,abilities,probabilities);
		// normalize probas
		float totalProba = 0;
		for(Float f: probabilities)
			totalProba = totalProba + f;
		for(int i=0;i<probabilities.size();i++)
		{	Float p = probabilities.get(i)/totalProba;
			probabilities.set(i,p);
		}
		
		// item factory
		boolean isAbstract = type==Type.ABSTRACT;
		ItemFactory itemFactory = ItemFactoryLoader.loadItemFactory(individualFolder,name,abilities,probabilities,abstractItems,isAbstract);
		if(isAbstract)
			abstractItems.put(name,itemFactory);
		else
			result.addItemFactory(name,itemFactory);
    }
	
	private static void loadAbilitiesElement(Element root, ArrayList<ArrayList<AbstractAbility>> abilities, ArrayList<Float> probabilities) throws ClassNotFoundException
	{	// abilities
		ArrayList<AbstractAbility> list = AbilityLoader.loadAbilitiesElement(root);
		abilities.add(list);
		
		// probabilities
		String probaStr = root.getAttributeValue(XmlTools.PROBA).trim();
		float proba = Float.parseFloat(probaStr);
		probabilities.add(proba);
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
