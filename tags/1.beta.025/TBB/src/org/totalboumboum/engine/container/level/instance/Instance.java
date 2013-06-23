package org.totalboumboum.engine.container.level.instance;

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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.bombset.BombsetMap;
import org.totalboumboum.engine.container.explosionset.Explosionset;
import org.totalboumboum.engine.container.explosionset.ExplosionsetLoader;
import org.totalboumboum.engine.container.fireset.FiresetMap;
import org.totalboumboum.engine.container.fireset.FiresetMapLoader;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.itemset.ItemsetLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Instance
{	
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
	// BOMBSET MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private BombsetMap bombsetMap = new BombsetMap();

	public void loadBombsetMaps(List<PredefinedColor> playersColors) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
   {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FILE_BOMBS;
   		bombsetMap.loadBombsets(individualFolder,playersColors);
    }
	
	public BombsetMap getBombsetMap()
	{	return bombsetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private FiresetMap firesetMap = new FiresetMap();
	
	public void loadFiresetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FILE_FIRES;
		firesetMap = FiresetMapLoader.loadFiresetMap(individualFolder);
    }
		
	public FiresetMap getFiresetMap()
	{	return firesetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Itemset itemset;

	public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FILE_ITEMS;
    	itemset = ItemsetLoader.loadItemset(individualFolder);
    }
		
	public Itemset getItemset()
	{	return itemset;	
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSIONSET		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Explosionset explosionset;

	public void loadExplosionset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FILE_EXPLOSIONS;
    	explosionset = ExplosionsetLoader.loadExplosionset(individualFolder);
    }
		
	public Explosionset getExplosionSet()
	{	return explosionset;	
	}

	/////////////////////////////////////////////////////////////////
	// INIT LINKS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initLinks() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	firesetMap.setInstance(this);
		explosionset.setInstance(this);
		bombsetMap.setInstance(this);
		itemset.setInstance(this);
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
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
    {	name = null;
    	bombsetMap = null;
    	firesetMap = null;
    	itemset = null;
    }
}
