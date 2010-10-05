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
 * 
 * @author Vincent Labatut
 *
 */
public abstract class ControlledPlayer extends AbstractPlayer
{	
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
	/** control */
	private PlayerControl spriteControl;
	/** current controls */
	private ControlSettings controlSettings;

	public ControlSettings getControlSettings()
	{	return controlSettings;
	}
	public PlayerControl getSpriteControl()
	{	return spriteControl;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
