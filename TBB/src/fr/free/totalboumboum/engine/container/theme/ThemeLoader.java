package fr.free.totalboumboum.engine.container.theme;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.configuration.engine.EngineConfiguration;
import fr.free.totalboumboum.game.round.RoundVariables;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ThemeLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Theme loadTheme(String folderPath) throws SAXException, IOException, ParserConfigurationException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_XML);
		Theme result = null;
		
		// caching
		String cachePath = FileTools.getCacheThemesPath()+ File.separator;
		File cacheFolder = new File(cachePath);
		cacheFolder.mkdirs();
		File objectFile = dataFile.getParentFile();
		String objectName = objectFile.getName();
		File packFile = objectFile.getParentFile().getParentFile();
		String packName = packFile.getName();
		String cacheName = packName+"_"+objectName;
		cachePath = cachePath + cacheName +FileTools.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		if(engineConfiguration.getFileCache())
		{	Object o = engineConfiguration.getMemoryCache(cacheName);
			double zoomFactor = RoundVariables.zoomFactor;
			result = ((Theme)o).cacheCopy(zoomFactor);
		}
		else if(engineConfiguration.getFileCache() && cacheFile.exists())
		{	try
			{	FileInputStream in = new FileInputStream(cacheFile);
				BufferedInputStream inBuff = new BufferedInputStream(in);
				ObjectInputStream oIn = new ObjectInputStream(inBuff);
				result = (Theme)oIn.readObject();
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
			result = loadThemeElement(root,individualFolder);
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

/*
 * TODO
 * 	- renommer copy en cacheCopy
 * 	- passer tous les trucs qui ne se copient qu'une fois (bombset/feu?)
 * 
 * 	- ou alors p-� qu'il faut traiter le cache m�moire avant le loader
 * 
 *  - ou alors se comporter exactement comme le loader...	
 */
	
    private static Theme loadThemeElement(Element root, String individualFolder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
    	Theme result = new Theme();
    	String folder;

    	// general data
    	loadGeneralElement(root,result);
    	loadAuthorElement(root,result);
    	loadSourceElement(root,result);
    	
		// blocks
    	folder = individualFolder + File.separator + FileTools.FOLDER_BLOCKS;
    	BlocksetLoader.loadBlockset(folder,result);
    	
		// floors
    	folder = individualFolder + File.separator + FileTools.FOLDER_FLOORS;
    	FloorsetLoader.loadFloorset(folder,result);		
    	
		// result
		return result;
	}
    
	private static void loadGeneralElement(Element root, Theme result)
	{	// name
		Element nameElt = root.getChild(XmlTools.GENERAL);
		String name = nameElt.getAttribute(XmlTools.NAME).getValue().trim();
		result.setName(name);
		
		// version
		Element versionElt = root.getChild(XmlTools.GENERAL);
		String version = versionElt.getAttribute(XmlTools.VERSION).getValue().trim();
		result.setVersion(version);
	}
	
	private static void loadAuthorElement(Element root, Theme result)
	{	Element elt = root.getChild(XmlTools.AUTHOR);
		String name = elt.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, Theme result)
	{	Element elt = root.getChild(XmlTools.SOURCE);
		String name = elt.getAttribute(XmlTools.VALUE).getValue().trim();
		result.setSource(name);		
	}
    
}
