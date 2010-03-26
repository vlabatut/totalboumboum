package org.totalboumboum.engine.content.sprite.hero;

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

import javax.xml.parsers.ParserConfigurationException;


import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.content.feature.Role;
import org.totalboumboum.engine.content.feature.ability.AbilityLoader;
import org.totalboumboum.engine.content.feature.ability.AbstractAbility;
import org.totalboumboum.engine.content.feature.gesture.GesturePack;
import org.totalboumboum.engine.content.feature.gesture.anime.HollowAnimesLoader;
import org.totalboumboum.engine.content.feature.gesture.modulation.ModulationsLoader;
import org.totalboumboum.engine.content.feature.gesture.trajectory.HollowTrajectoriesLoader;
import org.totalboumboum.engine.content.sprite.HollowSpriteFactoryLoader;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

public class HeroFactoryLoader extends HollowSpriteFactoryLoader
{	
	/* 
	 * load the base HeroFactory (i.e. no graphics nor bombset)
	 */
	public static HeroFactory loadHeroFactory(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		//double zoomFactor = RoundVariables.zoomFactor;
		HeroFactory result = null;
		
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
		{	result = ((HeroFactory)o);
			//result = result.cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (HeroFactory)oIn.readObject();
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
			result = init(folderPath);
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
		
	private static HeroFactory init(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,new HashMap<String,HeroFactory>());
		GesturePack gesturePack = result.getGesturePack();

		// ABILITIES
		ArrayList<AbstractAbility> abilities = result.getAbilities();
		folder = folderPath + File.separator+FileNames.FOLDER_ABILITIES;
		AbilityLoader.loadAbilityPack(folder,abilities);

		// EXPLOSION
		String explosionName = loadExplosionElement(root);
		if(explosionName!=null)
			result.setExplosionName(explosionName);
		
		// MODULATIONS
		folder = folderPath+File.separator+FileNames.FOLDER_MODULATIONS;
		ModulationsLoader.loadModulations(folder,gesturePack,Role.HERO);
		
		// TRAJECTORIES
		folder = folderPath+File.separator+FileNames.FOLDER_TRAJECTORIES;
		HollowTrajectoriesLoader.loadTrajectories(folder,gesturePack);
		
		// result
		return result;
	}

	/*
	 * complete the base HeroFactory with graphics and bombset
	 */
	public static HeroFactory completeHeroFactory(String folderPath, PredefinedColor color, HeroFactory base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		double zoomFactor = RoundVariables.zoomFactor;
		HeroFactory result = null;
		
		// caching
		String cachePath = FilePaths.getCacheHeroesPath()+ File.separator;
		File spriteFile = new File(folderPath);
		File packFile = spriteFile.getParentFile();
		String c = color.toString();
		String cacheName = packFile.getName()+"_"+spriteFile.getName()+"_"+c;
		cachePath = cachePath + cacheName + FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cacheName);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	result = ((HeroFactory)o).deepCopy(zoomFactor);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (HeroFactory)oIn.readObject();
				oIn.close();
				result = result.deepCopy(zoomFactor);
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
			result = complete(folderPath,color,base);
			// caching
			if(engineConfiguration.isSpriteMemoryCached())
			{	engineConfiguration.addToSpriteCache(cacheName,result);
				result = result.deepCopy(zoomFactor);
			}
			if(engineConfiguration.isSpriteFileCached())
			{	FileOutputStream out = new FileOutputStream(cacheFile);
				BufferedOutputStream outBuff = new BufferedOutputStream(out);
				ObjectOutputStream oOut = new ObjectOutputStream(outBuff);
				oOut.writeObject(result);
				oOut.close();
				result = result.deepCopy(zoomFactor);
			}
		}
		
		return result;
	}
	
	private static HeroFactory complete(String folderPath, PredefinedColor color, HeroFactory base) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		HeroFactory result = new HeroFactory();
		Element root = HollowSpriteFactoryLoader.openFile(folderPath);
		String folder;
		
		// GENERAL
		loadGeneralElement(root,result,base);
		GesturePack gesturePack = result.getGesturePack();
		
		// ANIMES
		folder = folderPath+File.separator+FileNames.FOLDER_ANIMES;
		HollowAnimesLoader.loadAnimes(folder,gesturePack,color,HeroFactory.getAnimeReplacements());
		
		// BOMBSET
		result.setBombsetColor(color);

		// result
		initDefaultGestures(gesturePack,Role.HERO);
		return result;
	}
}
