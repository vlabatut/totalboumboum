package org.totalboumboum.engine.content.sprite.hero;

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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.HollowGesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.HollowAnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.HollowTrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.xml.XmlNames;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class HollowHeroFactoryLoader extends HollowSpriteFactoryLoader
{	
	/* 
	 * load the base HeroFactory (i.e. no graphics nor bombset)
	 */
	public static HollowHeroFactory loadBase(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowHeroFactory result = null;
		
		// caching
		String cachePath = FilePaths.getCacheHeroesPath()+ File.separator;
		File spriteFile = new File(folderPath);
		File packFile = spriteFile.getParentFile();
		String cacheName = packFile.getName()+"_"+"abstract";
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cacheName);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	result = ((HollowHeroFactory)o);
			//result = result.cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (HollowHeroFactory)oIn.readObject();
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
		{	// opening & loading
			result = initBase(folderPath);
			// caching
			if(engineConfiguration.isSpriteMemoryCached())
			{	engineConfiguration.addToSpriteCache(cacheName,result);
				//result = result.cacheCopy(zoomFactor);
			}
			if(engineConfiguration.isSpriteFileCached())
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
		
	private static HollowHeroFactory initBase(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowHeroFactory result = new HollowHeroFactory();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		Element elt = root.getChild(XmlNames.GENERAL);
		String folder;
		
		// GENERAL
		// gestures pack
		HollowGesturePack gesturePack = new HollowGesturePack();
		result.setGesturePack(gesturePack);
		// abilities
		List<AbstractAbility> abilities = new ArrayList<AbstractAbility>();
		result.setAbilities(abilities);
		// name
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);
//if(name==null)
//	System.out.println();

		// ABILITIES
		folder = folderPath + File.separator+FileNames.FILE_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);

		// EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		// MODULATIONS
		folder = folderPath+File.separator+FileNames.FILE_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.HERO);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FILE_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// result
		return result;
	}

	/*
	 * complete the base HeroFactory with graphics and bombset
	 */
	public static HeroFactory completeHeroFactory(String folderPath, PredefinedColor color, HollowHeroFactory base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowHeroFactory original = null;
		
		// caching
		String cachePath = FilePaths.getCacheHeroesPath()+ File.separator;
		File spriteFile = new File(folderPath);
		File packFile = spriteFile.getParentFile();
		String cacheName = packFile.getName()+"_"+spriteFile.getName();
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cacheName);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	original = (HollowHeroFactory)o;
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				original = (HollowHeroFactory)oIn.readObject();
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
		{	// opening & loading
			original = complete(folderPath,base);
			// caching
			if(engineConfiguration.isSpriteMemoryCached())
			{	engineConfiguration.addToSpriteCache(cacheName,original);
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
		HeroFactory result = original.fill(zoomFactor,color);
		return result;
	}
	
	private static HollowHeroFactory complete(String folderPath, HollowHeroFactory base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HollowHeroFactory result = base.copy();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		Element elt = root.getChild(XmlNames.GENERAL);
		String folder;
	
		// GENERAL
		// gestures pack
		HollowGesturePack gesturePack = result.getGesturePack();
		// name
		String name = elt.getAttribute(XmlNames.NAME).getValue().trim();
		result.setName(name);
//if(name==null)
//	System.out.println();
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FILE_ANIMES;
		HollowAnimesLoader.loadAnimes(folder,gesturePack);
		
		// result
		initDefaultGestures(gesturePack,Role.HERO);
		return result;
	}
}
