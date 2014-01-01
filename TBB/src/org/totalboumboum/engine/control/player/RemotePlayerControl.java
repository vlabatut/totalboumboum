package org.totalboumboum.engine.control.player;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;

/**
 * Server side class in charge
 * of fetching the actions 
 * of <i>all</i> remote players.
 * 
 * @author Vincent Labatut
 */
public class RemotePlayerControl
{	
	/**
	 * Builds a monitoring object
	 * for <i>all</i> remote players.
	 */
	public RemotePlayerControl()
	{	connection = Configuration.getConnectionsConfiguration().getServerConnection();
		connection.setRemotePlayerControl(this);
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Server connection */
	private ServerGeneralConnection connection = null;
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map of player sprites */
	private HashMap<Integer,Hero> sprites = new HashMap<Integer,Hero>();
	/** List of player sprites */
	private List<Hero> orderedSprites = new ArrayList<Hero>();
	
	/**
	 * Adds a new sprite to this monitor.
	 * 
	 * @param sprite
	 * 		New sprite to monitor.
	 */
	public void addSprite(Hero sprite)
	{	sprites.put(sprite.getId(),sprite);
System.out.println(sprite+" "+sprite.getId());	
		orderedSprites.add(sprite);
		
//		ControlSettings controlSettings = connection.getControlSettings(index);
//		sprite.setControlSettings(controlSettings);
	}
	
	/**
	 * Sets new control settings.
	 *  
	 * @param controlSettings
	 * 		New control settings.
	 */
	public void setControlSettings(List<ControlSettings> controlSettings)
	{	int index = 0;
		for(ControlSettings cs: controlSettings)
		{	if(controlSettings!=null)
			{	Hero sprite = orderedSprites.get(index);
				sprite.setControlSettings(cs);
			}
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Events related to control */
	private final List<RemotePlayerControlEvent> eventList = new ArrayList<RemotePlayerControlEvent>();
	/** Event lock */
	private Lock eventLock = new ReentrantLock();
	
	/**
	 * Adds a new event to this monitor,
	 * will be processed later.
	 * 
	 * @param event
	 * 		New event.
	 */
	public void addEvent(RemotePlayerControlEvent event)
	{	eventLock.lock();
		
		eventList.add(event);
	
		eventLock.unlock();
	}
	
	/**
	 * Process the events waiting
	 * in the event list. 'Transforms'
	 * them in 'local events', under
	 * the form of control codes (i.e.
	 * values generated by keyboard strokes
	 * when playing a local game).
	 */
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
	/** Whether this object has been deleted or not */
	protected boolean finished = false;
	
	/**
	 * Cleanly finishes this object.
	 */
	public void finish()
	{	if(!finished)
		{	finished = true;
		
			sprites.clear();
			connection = null;
		}
	}
}
