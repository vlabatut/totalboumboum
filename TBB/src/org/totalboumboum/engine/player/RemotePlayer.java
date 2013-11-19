package org.totalboumboum.engine.player;

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

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.control.player.RemotePlayerControl;
import org.totalboumboum.game.profile.Profile;
import org.xml.sax.SAXException;

/**
 * Remotely controlled player.
 * 
 * @author Vincent Labatut
 */
public class RemotePlayer extends AbstractPlayer
{	
	/**
	 * 
	 * @param profile
	 * 		Profile associated to this player.
	 * @param base
	 * 		Factory for the appropriate sprite.
	 * @param tile
	 * 		Starting position in the zone (tile).
	 * @param controls 
	 * 		Controls for this player.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws SAXException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws IOException
	 * 		Problem while accessing the player profile or agent program.
	 * @throws ClassNotFoundException
	 * 		Problem while accessing the player profile or agent program.
	 */
	public RemotePlayer(Profile profile, HollowHeroFactory base, Tile tile, RemotePlayerControl controls) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	super(profile,base,tile);
		
		// set controls
		controls.addSprite(sprite);
		this.spriteControl = controls;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Controls for this player */
	private RemotePlayerControl spriteControl;
	/** Current control settings */
	private ControlSettings controlSettings;

	/**
	 * Return the current controls settings.
	 * 
	 * @return
	 * 		Current control settings.
	 */
	public ControlSettings getControlSettings()
	{	return controlSettings;
	}

	/**
	 * Returns the control for this player.
	 * 
	 * @return
	 * 		Control for this player.
	 */
	public RemotePlayerControl getSpriteControl()
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
