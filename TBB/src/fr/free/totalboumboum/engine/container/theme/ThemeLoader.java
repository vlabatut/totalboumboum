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

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class ThemeLoader
{	
	/////////////////////////////////////////////////////////////////
	// GENERAL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Theme loadTheme(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile,dataFile;	
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_THEME+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// theme
		Theme result = loadThemeElement(root,individualFolder);
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
		Element nameElt = root.getChild(XmlTools.ELT_GENERAL);
		String name = nameElt.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(name);
		
		// version
		Element versionElt = root.getChild(XmlTools.ELT_GENERAL);
		String version = versionElt.getAttribute(XmlTools.ATT_VERSION).getValue().trim();
		result.setVersion(version);
	}
	
	private static void loadAuthorElement(Element root, Theme result)
	{	Element elt = root.getChild(XmlTools.ELT_AUTHOR);
		String name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setAuthor(name);		
	}
	
	private static void loadSourceElement(Element root, Theme result)
	{	Element elt = root.getChild(XmlTools.ELT_SOURCE);
		String name = elt.getAttribute(XmlTools.ATT_VALUE).getValue().trim();
		result.setSource(name);		
	}
    
}
