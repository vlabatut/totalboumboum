package org.totalboumboum.engine.container.explosionset;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
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
public class ExplosionsetLoader
{	
	public static Explosionset loadExplosionset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		double zoomFactor = RoundVariables.zoomFactor;
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_EXPLOSIONSET+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_EXPLOSIONSET+FileNames.EXTENSION_XML);
		HollowExplosionset original = null;
		
		// caching
		String cachePath = FilePaths.getCacheExplosionsPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile();
		String cacheName = packFile.getName();
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getFromSpriteCache(cachePath);
		if(engineConfiguration.isSpriteMemoryCached() && o!=null)
		{	original = ((HollowExplosionset)o);
		}
		else if(engineConfiguration.isSpriteFileCached() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				original = (HollowExplosionset)oIn.readObject();
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
			original = loadExplosionsetElement(root,individualFolder);
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

		Explosionset result = original.fill(zoomFactor);
		return result;
    }
    
	private static HollowExplosionset loadExplosionsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
		HollowExplosionset result = new HollowExplosionset();

    	Element explosionsElt = root.getChild(XmlNames.EXPLOSIONS);
		loadExplosionsElement(explosionsElt,folder,result);
		
		return result;
	}
    
	@SuppressWarnings("unchecked")
	private static void loadExplosionsElement(Element root, String folder, HollowExplosionset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> items = root.getChildren(XmlNames.EXPLOSION);
		for(Element temp: items)
    		loadExplosionElement(temp,individualFolder,result);
	}
    
	private static void loadExplosionElement(Element root, String folder, HollowExplosionset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlNames.NAME).getValue().trim();
		
		// folder
		Attribute attribute = root.getAttribute(XmlNames.FOLDER);
		String individualFolder = folder+File.separator+attribute.getValue().trim();

		HollowExplosion explosion = loadExplosion(individualFolder);
		explosion.setName(name);
		result.addExplosion(name,explosion);
    }
	
	private static HollowExplosion loadExplosion(String pathFolder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = pathFolder;
		String schemaFolder = FilePaths.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileNames.FILE_EXPLOSION+FileNames.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_EXPLOSION+FileNames.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		HollowExplosion result = loadExplosionElement(root);
		return result;
	}
	
    private static HollowExplosion loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	HollowExplosion result = new HollowExplosion();
    	
    	Element elt = root.getChild(XmlNames.FIRESET);
    	String firesetName = elt.getAttribute(XmlNames.NAME).getValue().trim();
    	result.setFiresetName(firesetName);
    	
    	return result;
    }
}
