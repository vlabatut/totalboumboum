package org.totalboumboum.engine.container.level.instance;

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
import java.io.Serializable;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.engine.container.bombset.BombsetMap;
import org.totalboumboum.engine.container.explosionset.Explosionset;
import org.totalboumboum.engine.container.explosionset.ExplosionsetLoader;
import org.totalboumboum.engine.container.fireset.FiresetMap;
import org.totalboumboum.engine.container.fireset.FiresetMapLoader;
import org.totalboumboum.engine.container.itemset.Itemset;
import org.totalboumboum.engine.container.itemset.ItemsetLoader;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;
import org.xml.sax.SAXException;

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
	// BOMBSET MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient BombsetMap bombsetMap;

	public void loadBombsetMaps(List<PredefinedColor> playersColors) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// init bombset map
		String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FOLDER_BOMBS;
		bombsetMap = new BombsetMap();
    	bombsetMap.initBombset(individualFolder);
		
    	// load level bombset
    	bombsetMap.completeBombset(null);
    	for(PredefinedColor color: playersColors)
    		bombsetMap.completeBombset(color);
    }
	
	public BombsetMap getBombsetMap()
	{	return bombsetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET MAP		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient FiresetMap firesetMap;
	
	public void loadFiresetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FOLDER_FIRES;
		firesetMap = FiresetMapLoader.loadFiresetMap(individualFolder);
    }
		
	public FiresetMap getFiresetMap()
	{	return firesetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Itemset itemset;

	public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FOLDER_ITEMS;
    	itemset = ItemsetLoader.loadItemset(individualFolder);
    }
		
	public Itemset getItemset()
	{	return itemset;	
	}

	/////////////////////////////////////////////////////////////////
	// EXPLOSIONSET		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Explosionset explosionset;

	public void loadExplosionSet() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	String individualFolder = FilePaths.getInstancesPath()+File.separator+name+File.separator+FileNames.FOLDER_EXPLOSIONS;
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
