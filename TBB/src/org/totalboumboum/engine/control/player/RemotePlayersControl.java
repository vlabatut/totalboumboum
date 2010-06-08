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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import org.totalboumboum.engine.content.sprite.hero.Hero;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;

public class RemotePlayersControl implements Runnable
{	
	public RemotePlayersControl(ObjectInputStream in)
	{	this.in = in;
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in = null;
	
	/////////////////////////////////////////////////////////////////
	// SPRITES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final HashMap<Integer,Hero> heroes = new HashMap<Integer,Hero>();
	
	public void addHero(Hero hero)
	{	int id = hero.getId();
		heroes.put(id,hero);
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	try
		{	while(!Thread.interrupted())
			{	// read the control event in the network stream
				RemotePlayerControlEvent event = (RemotePlayerControlEvent)in.readObject();
				// get the control code
				ControlCode controlCode = event.getControlCode();
				// get the sprite id
				int id = event.getSpriteId();
				// send it to the sprite like a local control code
				Hero sprite = heroes.get(id);
				sprite.putControlCode(controlCode);
			}
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
		
			heroes.clear();
			in = null;
		}
	}
}
