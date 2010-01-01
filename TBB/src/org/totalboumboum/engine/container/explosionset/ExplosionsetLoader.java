package org.totalboumboum.engine.container.explosionset;

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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.totalboumboum.tools.files.FileTools;
import org.totalboumboum.tools.xml.XmlTools;
import org.xml.sax.SAXException;


public class ExplosionsetLoader
{	
	public static Explosionset loadExplosionset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String schemaFolder = FileTools.getSchemasPath();
		String individualFolder = folderPath;
		File schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_EXPLOSIONSET+FileTools.EXTENSION_SCHEMA);
		File dataFile = new File(individualFolder+File.separator+FileTools.FILE_EXPLOSIONSET+FileTools.EXTENSION_XML);
		Explosionset result = null;
		
		// opening
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		result = loadExplosionsetElement(root,individualFolder);
		
		return result;
    }
    
	private static Explosionset loadExplosionsetElement(Element root, String folder) throws IOException, ParserConfigurationException, SAXException, ClassNotFoundException
	{	// init
		Explosionset result = new Explosionset();

    	Element explosionsElt = root.getChild(XmlTools.EXPLOSIONS);
		loadExplosionsElement(explosionsElt,folder,result);
		
		return result;
	}
    
	@SuppressWarnings("unchecked")
	private static void loadExplosionsElement(Element root, String folder, Explosionset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	String individualFolder = folder;
    	List<Element> items = root.getChildren(XmlTools.EXPLOSION);
		for(Element temp: items)
    		loadExplosionElement(temp,individualFolder,result);
	}
    
	private static void loadExplosionElement(Element root, String folder, Explosionset result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.NAME).getValue().trim();
		
		// folder
		Attribute attribute = root.getAttribute(XmlTools.FOLDER);
		String individualFolder = folder+File.separator+attribute.getValue().trim();

		Explosion explosion = loadExplosion(individualFolder);
		explosion.setName(name);
		result.addExplosion(name,explosion);
    }
	
	private static Explosion loadExplosion(String pathFolder) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = pathFolder;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		
		// loading
		Explosion result = loadExplosionElement(root);
		return result;
	}
	
    private static Explosion loadExplosionElement(Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Explosion result = new Explosion();
    	
    	Element elt = root.getChild(XmlTools.FIRESET);
    	String firesetName = elt.getAttribute(XmlTools.NAME).getValue().trim();
    	result.setFiresetName(firesetName);
    	
    	return result;
    }
}
