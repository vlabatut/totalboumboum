package org.totalboumboum.engine.control.player;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;

/**
 * Server side class: fetch the actions of a remote player
 * 
 * @author Vincent Labatut
 *
 */
public class RemotePlayerControl
{	
	public RemotePlayerControl()
	{	connection = Configuration.getConnectionsConfiguration().getServerConnection();
		connection.setRemotePlayerControl(this);
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerGeneralConnection connection = null;
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer,Hero> sprites = new HashMap<Integer,Hero>();
	private HashMap<Hero,Integer> indices = new HashMap<Hero,Integer>();
	
	public void addSprite(Hero sprite)
	{	sprites.put(sprite.getId(),sprite);
		int index = sprites.size() - 1;
		indices.put(sprite,index);
		
//		ControlSettings controlSettings = connection.getControlSettings(index);
//		sprite.setControlSettings(controlSettings);
	}
	
	public void setControlSettings(List<ControlSettings> controlSettings)
	{	for(Hero sprite: indices.keySet())
		{	int index = indices.get(sprite);
			ControlSettings cs = controlSettings.get(index);
			sprite.setControlSettings(cs);
//TODO pb sur la numérotation des sprites dans controlsettings
//voir comment c'est fait dans le cas local...
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<RemotePlayerControlEvent> eventList = new ArrayList<RemotePlayerControlEvent>();
	private Lock eventLock = new ReentrantLock();
	
	public void addEvent(RemotePlayerControlEvent event)
	{	eventLock.lock();
		
	eventList.add(event);
	
		eventLock.unlock();
	}
	
	public void update()
	{	long totalTime = RoundVariables.loop.getTotalEngineTime();
		List<RemotePlayerControlEvent> events = new ArrayList<RemotePlayerControlEvent>();
		
		eventLock.lock();
		Iterator<RemotePlayerControlEvent> it = eventList.iterator();
		while(it.hasNext())
		{	RemotePlayerControlEvent event = it.next();
			if(event.getTime()<totalTime)
			{	events.add(event);
				it.remove();
//				if(VERBOSE)
//					System.out.print("["+currentEvent.getTime()+"<"+getTotalEngineTime()+"]");		
			}
		}
		eventLock.unlock();
		
		// process events
		for(RemotePlayerControlEvent event: events)
		{	// get the control code
			ControlCode controlCode = event.getControlCode();
			// get the appropriate sprite
			int id = event.getSpriteId();
			Hero sprite = sprites.get(id);
			// send the event to the sprite like a local control code
			sprite.putControlCode(controlCode);
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		
			sprites.clear();
			connection = null;
		}
	}
}
