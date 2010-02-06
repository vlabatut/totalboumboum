package org.totalboumboum.engine.container.theme;

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

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.engine.EngineConfiguration;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;

public class ThemeLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Theme loadTheme(String folderPath) throws SAXException, IOException, ParserConfigurationException, ClassNotFoundException
	{	// init
		String schemaFolder = FilePaths.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileNames.FILE_THEME+FileNames.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileNames.FILE_THEME+FileNames.EXTENSION_XML);
		Theme result = null;
		
		// caching
		String cachePath = FilePaths.getCacheThemesPath()+ File.separator;
		File objectFile = dataFile.getParentFile();
		File packFile = objectFile.getParentFile().getParentFile();
		String cacheName = packFile.getName()+"_"+objectFile.getName();
		cachePath = cachePath + cacheName +FileNames.EXTENSION_DATA;
		File cacheFile = new File(cachePath);
		EngineConfiguration engineConfiguration = Configuration.getEngineConfiguration();
		Object o = engineConfiguration.getMemoryCache(cacheName);
		if(engineConfiguration.getMemoryCache() && o!=null)
		{	double zoomFactor = RoundVariables.zoomFactor;
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

    private static Theme loadThemeElement(Element root, String individualFolder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
    	Theme result = new Theme();
    	String folder;

    	// general data
    	loadGeneralElement(root,result);
    	loadAuthorElement(root,result);
    	loadSourceElement(root,result);
    	
		// blocks
    	folder = individualFolder + File.separator + FileNames.FOLDER_BLOCKS;
    	BlocksetLoader.loadBlockset(folder,result);
    	
		// floors
    	folder = individualFolder + File.separator + FileNames.FOLDER_FLOORS;
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
