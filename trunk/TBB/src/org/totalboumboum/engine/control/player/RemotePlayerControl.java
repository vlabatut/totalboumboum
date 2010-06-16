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

import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.stream.network.NetInputGameStream;

public class RemotePlayerControl implements Runnable
{	
	public RemotePlayerControl(Hero sprite, NetInputGameStream in)
	{	this.in = in;
		this.sprite = sprite;
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private NetInputGameStream in = null;
	
	/////////////////////////////////////////////////////////////////
	// SPRITE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Hero sprite;
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	while(!Thread.interrupted())
		{	// read the control event in the network stream
			RemotePlayerControlEvent event = (RemotePlayerControlEvent)in.readEvent();
			// get the control code
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
