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

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.control.ControlCode;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.engine.player.RemotePlayer;

public class RemotePlayersControl extends PlayerControl<RemotePlayer> implements Runnable
{	
	public RemotePlayersControl(RemotePlayer player)
	{	super(player);
		in = player.getInputStream();
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in;
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	Sprite sprite = getSprite();
		try
		{	while(!Thread.interrupted())
			{	// read the control event in the network stream
				RemotePlayerControlEvent event = (RemotePlayerControlEvent)in.readObject();
				// get the control code
				ControlCode controlCode = event.getControlCode();
				// send it to the sprite like a local control code
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
	private boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			super.finish();
			
			in = null;
		}
	}
}
