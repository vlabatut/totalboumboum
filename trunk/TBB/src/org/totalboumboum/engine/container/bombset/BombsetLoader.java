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
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.ability.StateAbility;
import org.totalboumboum.engine.content.sprite.bomb.HollowBombFactory;
import org.totalboumboum.engine.content.sprite.bomb.HollowBombFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class BombsetLoader
{	
	public static Bombset loadBombset(String folderPath, PredefinedColor color) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_BOMBSET+FileNames.EXTENSION_XML);
		HollowBombset original = null;
		
		// caching
		String cachePath = FilePaths.getCacheBombsPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName()+"_"+"abstract";
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cachePath);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	original = ((HollowBombset)o);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				original = (HollowBombset)oIn.readObject();
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
			original = loadBombsetElement(root,individualFolder);
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

		double zoomFactor = RoundVariables.zoomFactor;
		Bombset result = original.fill(zoomFactor,color);
		return result;
	}
	
    private static HollowBombset loadBombsetElement(Element root, String folder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init
    	HollowBombset result = new HollowBombset();

    	// abstract bombs
    	HashMap<String,HollowBombFactory> abstractBombs = new HashMap<String,HollowBombFactory>();
    	Element abstractBombsElt = root.getChild(XmlNames.ABSTRACT_BOMBS);
    	if(abstractBombsElt!=null)
    		loadBombsElement(abstractBombsElt,folder,result,abstractBombs,Type.ABSTRACT);
    	
    	// concrete bombs
    	Element concreteBombsElt = root.getChild(XmlNames.CONCRETE_BOMBS);
		loadBombsElement(concreteBombsElt,folder,result,abstractBombs,Type.CONCRETE);
    	
    	return result;
    }
    
	@SuppressWarnings("unchecked")
	private static void loadBombsElement(Element root, String folder, HollowBombset result, HashMap<String,HollowBombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> bombs = root.getChildren(XmlNames.BOMB);
    	Iterator<Element> i = bombs.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
    		loadBombElement(temp,individualFolder,result,abstractBombs,type);
    	}
	}

	private static void loadBombElement(Element root, String folder, HollowBombset bombset, HashMap<String,HollowBombFactory> abstractBombs, Type type) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
    	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		individualFolder = individualFolder+File.separator+attribute.getValue().trim();
		
		// required abilities
		if(type==Type.CONCRETE)
		{	List<AbstractAbility> requiredAbilities = AbilityLoader.loadAbilitiesElement(root);
			Iterator<AbstractAbility> i = requiredAbilities.iterator();
			List<StateAbility> abilities = new ArrayList<StateAbility>();
			while(i.hasNext())
			{	AbstractAbility ablt = i.next();
				if(ablt instanceof StateAbility)
					abilities.add((StateAbility)ablt);
			}
		
			// result
			HollowBombFactory bombFactory = HollowBombFactoryLoader.loadBombFactory(individualFolder,name,abstractBombs);
			bombset.addBombFactory(bombFactory,abilities);
		}
		else
		{	HollowBombFactory bombFactory = HollowBombFactoryLoader.loadBombFactory(individualFolder,name,abstractBombs);
			abstractBombs.put(name,bombFactory);
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