package org.totalboumboum.engine.player;

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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.control.player.RemotePlayersControl;
import org.xml.sax.SAXException;

public class RemotePlayer extends AbstractPlayer
{	
	public RemotePlayer(Profile profile, HollowHeroFactory base, Tile tile, ControlSettings controlSettings, RemotePlayersControl spriteControl) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	super(profile,base,tile);
		
		// set controls
		this.controlSettings = controlSettings;
		sprite.setControlSettings(controlSettings);
		this.spriteControl = spriteControl;
		spriteControl.addHero(sprite);
		
		// stream
		startThread();
	}

	/////////////////////////////////////////////////////////////////
	// THREAD			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	Thread thread;
	
	private void startThread()
	{	thread = new Thread(spriteControl);
		thread.start();
	}
	
	//TODO to be moved
	public void stopThread()
	{	thread.interrupt();
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** control */
	private RemotePlayersControl spriteControl;
	/** current controls */
	private ControlSettings controlSettings;

	public ControlSettings getControlSettings()
	{	return controlSettings;
	}

	public RemotePlayersControl getSpriteControl()
	{	return spriteControl;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
		
			// control
			spriteControl = null;
			controlSettings = null;
		}
	}
}
