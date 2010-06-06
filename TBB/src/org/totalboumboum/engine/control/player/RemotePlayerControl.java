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

import java.io.ObjectInputStream;

import org.totalboumboum.engine.content.sprite.Sprite;
import org.totalboumboum.engine.player.RemotePlayer;

public class RemotePlayerControl extends PlayerControl<RemotePlayer> implements Runnable
{	
	public RemotePlayerControl(RemotePlayer player)
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
		while(!Thread.interrupted())
		{	/* TODO 
			 *	1) lire un évènement dans le flux
			 *	2) l'envoyer au sprite correspondant
			 *
			 *  c'est tout !
			 */
		
			
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
