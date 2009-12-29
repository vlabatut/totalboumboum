package fr.free.totalboumboum.engine.container.level.instance;

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

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.fireset.FiresetMap;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.tools.FileTools;

public class InstanceLoader
{	

	public static Instance loadInstance(String name) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	// init
		String individualFolder = FileTools.getInstancesPath()+File.separator+name;
		
		// loading
		Instance result = new Instance(name);
		loadBombsetMap(individualFolder,result);
		loadFiresetMap(individualFolder,result);
		loadItemset(individualFolder,result);
		
		return result;
	}

	public static void loadBombsetMap(String folder, Instance result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init bombset map
		String individualFolder = folder + FileTools.FOLDER_BOMBS;
		BombsetMap bombsetMap = new BombsetMap();
    	bombsetMap.initBombset(individualFolder);
    	result.setBombsetMap(bombsetMap);
		
    	// load level bombset
    	bombsetMap.completeBombset(individualFolder,null);
    }

	public static void loadFiresetMap(String folder, Instance result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder + FileTools.FOLDER_FIRES;
		FiresetMap firesetMap = FiresetLoader.loadFiresetMap(individualFolder);
    	result.setFiresetMap(firesetMap);
    }

	public static void loadItemset(String folder, Instance result) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = folder + FileTools.FOLDER_ITEMS;
    	Itemset itemset = ItemsetLoader.loadItemset(individualFolder);
    	result.setItemset(itemset);
    }
}
