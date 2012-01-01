package org.totalboumboum.engine.container.itemset;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import org.totalboumboum.engine.content.sprite.item.HollowItemFactory;
import org.totalboumboum.engine.content.sprite.item.HollowItemFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
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
public class ItemsetLoader
{	
	/////////////////////////////////////////////////////////////////
	// LOAD ALL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Itemset loadItemset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		double zoomFactor = RoundVariables.zoomFactor;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_ITEMSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_ITEMSET+FileNames.EXTENSION_XML);
		HollowItemset original = null;
		
		// caching
		String cachePath = FilePaths.getCacheItemsPath()+ File.separator;
		File cacheFolder = new File(cachePath);
		cacheFolder.mkdirs();
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName();
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cachePath);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	original = ((HollowItemset)o);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				original = (HollowItemset)oIn.readObject();
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
		
		if(original==null)
		{	// opening
			Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
			// loading
			original = loadItemsetElement(root,individualFolder);
			// caching
			if(engineConfiguration.isSpriteMemoryCached())
			{	engineConfiguration.addToSpriteCache(cachePath,original);
			}
			if(engineConfiguration.isSpriteFileCached())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(original);
				oOut.close();
			}
		}
		
		Itemset result = original.fill(zoomFactor);
		return result;
    }
    
	private static HollowItemset loadItemsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
		HollowItemset result = new HollowItemset();

		// abstract items
    	HashMap<String,HollowItemFactory> abstractItems = new HashMap<String,HollowItemFactory>();
    	Element abstractItemsElt = root.getChild(XmlNames.ABSTRACT_ITEMS);
    	if(abstractItemsElt!=null)
    		loadItemsElement(abstractItemsElt,folder,result,abstractItems,Type.ABSTRACT);
    	
    	// concrete items
    	Element concreteItemsElt = root.getChild(XmlNames.CONCRETE_ITEMS);
		loadItemsElement(concreteItemsElt,folder,result,abstractItems,Type.CONCRETE);
		
		return result;
	}
    
	@SuppressWarnings("unchecked")
	private static void loadItemsElement(Element root, String folder, HollowItemset result, HashMap<String,HollowItemFactory> abstractItems, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> items = root.getChildren(XmlNames.ITEM);
		for(Element temp: items)
    		loadItemElement(temp,individualFolder,result,abstractItems,type);
	}
    
	@SuppressWarnings("unchecked")
	private static void loadItemElement(Element root, String folder, HollowItemset result, HashMap<String,HollowItemFactory> abstractItems, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// abilities / itemref
		List<Float> probabilities = new ArrayList<Float>();
		List<List<AbstractAbility>> abilities = new ArrayList<List<AbstractAbility>>();
		List<String> itemrefs = new ArrayList<String>();
		List<Element> elements = root.getChildren(XmlNames.ABILITIES);
		if(!elements.isEmpty())
		{	for(Element e: elements)
				loadAbilitiesElement(e,abilities,probabilities);
		}
		else
		{	elements = root.getChildren(XmlNames.ITEM);
			for(Element e: elements)
				loadItemrefElement(e,itemrefs,probabilities);
		}
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
		HollowItemFactory itemFactory = HollowItemFactoryLoader.loadItemFactory(individualFolder,name,itemrefs,abilities,probabilities,abstractItems,isAbstract);
		if(isAbstract)
			abstractItems.put(name,itemFactory);
		else
			result.addItemFactory(name,itemFactory);
    }
	
	private static void loadAbilitiesElement(Element root, List<List<AbstractAbility>> abilities, List<Float> probabilities) throws ClassNotFoundException
	{	// abilities
		List<AbstractAbility> list = AbilityLoader.loadAbilitiesElement(root);
		abilities.add(list);
		
		// probabilities
		String probaStr = root.getAttributeValue(XmlNames.PROBA).trim();
		float proba = Float.parseFloat(probaStr);
		probabilities.add(proba);
	}

	private static void loadItemrefElement(Element root, List<String> items, List<Float> probabilities)
	{	// item
		String itemName = root.getAttributeValue(XmlNames.NAME).trim();
		items.add(itemName);
		
		// probabilities
		String probaStr = root.getAttributeValue(XmlNames.PROBA).trim();
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
