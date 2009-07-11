package fr.free.totalboumboum.engine.container.fireset;

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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.content.sprite.fire.FireFactory;
import fr.free.totalboumboum.engine.content.sprite.fire.FireFactoryLoader;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;

public class FiresetLoader
{	
	public static Fireset loadFireset(String folderPath) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = folderPath;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_XML);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_FIRESET+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Fireset result = loadFiresetElement(individualFolder,root);
		return result;
	}
	
    @SuppressWarnings("unchecked")
    private static Fireset loadFiresetElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Fireset result = new Fireset();
		// name
		String firesetName = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		result.setName(firesetName);
    	// fires
    	List<Element> elts = root.getChildren(XmlTools.ELT_FIRE);
    	Iterator<Element> i = elts.iterator();
    	while(i.hasNext())
    	{	Element temp = i.next();
			String name = temp.getAttribute(XmlTools.ATT_NAME).getValue().trim();
    		FireFactory fireFactory = loadFireElement(folder,temp);
    		result.addFireFactory(name, fireFactory);
    	}
    	return result;
    }
    
    private static FireFactory loadFireElement(String folder, Element root) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder;
		Attribute attribute = root.getAttribute(XmlTools.ATT_FOLDER);
		if(attribute!=null)
		{	String f = attribute.getValue().trim();
			individualFolder = folder+File.separator+f;
		}    	
    	//
    	FireFactory result = FireFactoryLoader.loadFireFactory(individualFolder);
    	return result;
    }
}
