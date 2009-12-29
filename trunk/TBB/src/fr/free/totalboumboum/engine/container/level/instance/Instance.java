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
import java.io.Serializable;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.fireset.FiresetMap;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.tools.FileTools;

public class Instance implements Serializable
{	private static final long serialVersionUID = 1L;

	public Instance(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;

	public String getName()
    {	return name;
    }
    
	/////////////////////////////////////////////////////////////////
	// BOMBSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BombsetMap bombsetMap;

	public void loadBombsetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init bombset map
		String individualFolder = FileTools.getInstancesPath()+File.separator+name+File.separator+FileTools.FOLDER_BOMBS;
		bombsetMap = new BombsetMap();
    	bombsetMap.initBombset(individualFolder);
		
    	// load level bombset
    	bombsetMap.completeBombset(individualFolder,null);
    }
	
	public BombsetMap getBombsetMap()
	{	return bombsetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private FiresetMap firesetMap;
	
	public void loadFiresetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FileTools.getInstancesPath()+File.separator+name+File.separator+FileTools.FOLDER_FIRES;
		firesetMap = FiresetLoader.loadFiresetMap(individualFolder);
    }
		
	public FiresetMap getFiresetMap()
	{	return firesetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Itemset itemset;

	public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FileTools.getInstancesPath()+File.separator+name+File.separator+FileTools.FOLDER_ITEMS;
    	itemset = ItemsetLoader.loadItemset(individualFolder);
    }
		
	public Itemset getItemset()
	{	return itemset;	
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
/*    public Instance copy()
    {	Instance result = new Instance();
    	result.instancePath = instancePath;
    	result.itemPath = itemPath;
    	result.bombsetPath = bombsetPath;
    	result.firePath = firePath;
      	result.instanceName = instanceName;
    	return result;
    }
 */   
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private boolean finished = false;
	
	public void finish()
    {	if(!finished)
	    {	name = null;
	    	bombsetMap = null;
	    	firesetMap = null;
	    	itemset = null;
	    }
    }
}
