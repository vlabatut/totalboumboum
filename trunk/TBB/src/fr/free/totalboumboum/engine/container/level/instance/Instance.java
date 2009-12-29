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

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.Configuration;
import fr.free.totalboumboum.engine.container.bombset.Bombset;
import fr.free.totalboumboum.engine.container.bombset.BombsetMap;
import fr.free.totalboumboum.engine.container.fireset.FiresetLoader;
import fr.free.totalboumboum.engine.container.fireset.FiresetMap;
import fr.free.totalboumboum.engine.container.itemset.Itemset;
import fr.free.totalboumboum.engine.container.itemset.ItemsetLoader;
import fr.free.totalboumboum.engine.container.level.players.Players;
import fr.free.totalboumboum.engine.container.level.players.PlayersLoader;
import fr.free.totalboumboum.engine.container.level.zone.Zone;
import fr.free.totalboumboum.engine.container.level.zone.ZoneLoader;
import fr.free.totalboumboum.engine.container.theme.Theme;
import fr.free.totalboumboum.engine.container.theme.ThemeLoader;
import fr.free.totalboumboum.engine.container.tile.Tile;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.LocalLoop;
import fr.free.totalboumboum.game.round.RoundVariables;
import fr.free.totalboumboum.tools.FileTools;
import fr.free.totalboumboum.tools.GameData;
import fr.free.totalboumboum.tools.XmlTools;

public class Instance implements Serializable
{	private static final long serialVersionUID = 1L;

	private Instance(String name)
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
	private String bombsetPath;
	private BombsetMap bombsetMap;
	private Bombset bombset;

	public void loadBombsets() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	// bombsets map
		bombsetMap = new BombsetMap();
    	bombsetMap.loadBombset(bombsetPath);
		
    	// level bombset
    	bombset = bombsetMap.loadBombset(bombsetPath,null);
		level.setBombset(bombset);
    }
	
	public BombsetMap getBombsetMap()
	{	return bombsetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRESET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String firePath;
	private FiresetMap firesetMap;
	
	public void loadFiresetMap() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	firesetMap = FiresetLoader.loadFiresetMap(firePath);
    }
	
	public FiresetMap getFiresetMap()
	{	return firesetMap;	
	}

	/////////////////////////////////////////////////////////////////
	// ITEMSET			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String itemPath;
	private Itemset itemset;

	public void loadItemset() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
    {	itemset = ItemsetLoader.loadItemset(itemPath);
    }
	
	public Itemset getItemset()
	{	return itemset;	
	}

	/////////////////////////////////////////////////////////////////
	// COPY				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    public Instance copy()
    {	Instance result = new Instance();
    	result.instancePath = instancePath;
    	result.itemPath = itemPath;
    	result.bombsetPath = bombsetPath;
    	result.firePath = firePath;
      	result.instanceName = instanceName;
    	return result;
    }
    
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
    private boolean finished = false;
	
	public void finish()
    {	if(!finished)
	    {	instancePath = null;
	    	itemPath = null;
	    	bombsetPath = null;
	    	firePath = null;
	      	instanceName = null;
	    }
    }
}
