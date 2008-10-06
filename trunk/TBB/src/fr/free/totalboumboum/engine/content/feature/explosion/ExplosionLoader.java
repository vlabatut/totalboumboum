package fr.free.totalboumboum.engine.content.feature.explosion;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import org.jdom.Attribute;
import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.container.fireset.Fireset;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.level.Level;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.XmlTools;


public class ExplosionLoader
{	
	public static Explosion loadExplosion(String pathFolder, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = pathFolder;
		String schemaFolder = FileTools.getSchemasPath();
		File schemaFile,dataFile;
		// opening
		dataFile = new File(individualFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_DATA);
		schemaFile = new File(schemaFolder+File.separator+FileTools.FILE_EXPLOSION+FileTools.EXTENSION_SCHEMA);
		Element root = XmlTools.getRootFromFile(dataFile,schemaFile);
		// loading
		Explosion result = loadExplosionElement(root,level);
		return result;
	}
	
    private static Explosion loadExplosionElement(Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	Explosion result = new Explosion();
    	// fire
    	Element elt = root.getChild(XmlTools.ELT_FIRESET);
    	Fireset fireset = loadFiresetElement(elt,level);
    	//
    	result.setFireset(fireset);
    	return result;
    }
    
    private static Fireset loadFiresetElement(Element root, Level level) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String name = root.getAttribute(XmlTools.ATT_NAME).getValue().trim();
		String folder = level.getInstancePath()+File.separator+FileTools.FOLDER_FIRES;
		folder = folder + File.separator+name;
    	Fireset result = FiresetLoader.loadFireset(folder,level);
    	return result;
    }
}
