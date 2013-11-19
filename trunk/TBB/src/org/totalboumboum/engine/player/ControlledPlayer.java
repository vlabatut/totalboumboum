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

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.container.tile.Tile;
import org.totalboumboum.engine.content.sprite.hero.HollowHeroFactory;
import org.totalboumboum.engine.control.player.LocalPlayerControl;
import org.totalboumboum.engine.control.player.NetworkPlayerControl;
import org.totalboumboum.engine.control.player.PlayerControl;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.xml.sax.SAXException;

/**
 * Player controled locally, either by a human player
 * or an agent.
 * 
 * @author Vincent Labatut
 */
public abstract class ControlledPlayer extends AbstractPlayer
{	
	/**
	 * Builds a new player.
	 * 
	 * @param profile
	 * 		Profile associated to this player.
	 * @param base
	 * 		Factory for the appropriate sprite.
	 * @param tile
	 * 		Starting position in the zone (tile).
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
	public ControlledPlayer(Profile profile, HollowHeroFactory base, Tile tile) throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	super(profile,base,tile);
		
		// set controls settings
		int indexCtrSet = profile.getControlSettingsIndex();
		controlSettings = Configuration.getControlsConfiguration().getControlSettings().get(indexCtrSet);
		if(controlSettings == null)
			controlSettings = new ControlSettings();
		sprite.setControlSettings(controlSettings);
		
		// set controls
		ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
		if(clientConnection==null)
			spriteControl = new LocalPlayerControl(this);
		else
			spriteControl = new NetworkPlayerControl(this);
	}

	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Controls for this player */
	private PlayerControl spriteControl;
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
	public PlayerControl getSpriteControl()
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
			spriteControl.finish();
			spriteControl = null;
			controlSettings = null;
		}
	}
}
