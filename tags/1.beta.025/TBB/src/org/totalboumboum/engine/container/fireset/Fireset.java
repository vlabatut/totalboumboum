package org.totalboumboum.engine.container.fireset;

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

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.fire.Fire;
import org.totalboumboum.engine.content.sprite.fire.FireFactory;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteCreationEvent;
import org.totalboumboum.engine.loop.event.replay.sprite.SpriteInsertionEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Fireset extends AbstractFireset
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// INSTANCE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private transient Instance instance = null;
	
	public void setInstance(Instance instance) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	this.instance = instance;
		for(FireFactory fireFactory: fireFactories.values())
			fireFactory.setInstance(instance);
	}

	public Instance getInstance()
	{	return instance;	
	}

	/////////////////////////////////////////////////////////////////
	// FIRE FACTORIES	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,FireFactory> fireFactories = new HashMap<String,FireFactory>();
	
	public void addFireFactory(String name, FireFactory fireFactory)
	{	fireFactories.put(name, fireFactory);
		fireFactory.setFiresetName(name);
	}
	
	public Fire makeFire(String name, Tile tile)
	{	Fire result = null;
		if(name==null)
			name = fireFactories.keySet().iterator().next();
		FireFactory fireFactory = fireFactories.get(name);
if(fireFactory==null)
	System.out.println(name);
		result = fireFactory.makeSprite(tile);
		
		// record/transmit creation event
		String eventName = getName()+"/"+name;
		SpriteCreationEvent event = new SpriteCreationEvent(result,name);
		RoundVariables.writeEvent(event);
		
		return result;
	}
}
