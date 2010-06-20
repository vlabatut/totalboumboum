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
import java.util.List;

import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.round.RoundVariables;
import org.totalboumboum.game.stream.network.NetInputServerStream;

public class RemotePlayerControl
{	
	public RemotePlayerControl(int index, Hero sprite, NetInputServerStream in)
	{	this.index = index;
		this.in = in;
		this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER INDEX		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int index;
	
	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private NetInputServerStream in = null;
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Hero sprite;
	
	/////////////////////////////////////////////////////////////////
	// EVENTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RemotePlayerControlEvent currentEvent = null;
	
	public void update()
	{	long totalTime = RoundVariables.loop.getTotalEngineTime();
		List<RemotePlayerControlEvent> events = new ArrayList<RemotePlayerControlEvent>();
		
		// read events
		while(currentEvent!=null && currentEvent.getTime()<totalTime)
		{	events.add(currentEvent);
//			if(VERBOSE)
//				System.out.print("["+currentEvent.getTime()+"<"+getTotalEngineTime()+"]");		
			currentEvent = in.readEvent(index);
		}
		
		// process events
		for(RemotePlayerControlEvent event: events)
		{	// get the control code
			ControlCode controlCode = event.getControlCode();
			// send it to the sprite like a local control code
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
		
			sprite = null;
			in = null;
		}
	}
}
