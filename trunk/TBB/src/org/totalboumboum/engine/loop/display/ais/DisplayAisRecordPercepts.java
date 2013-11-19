package org.totalboumboum.engine.loop.display.ais;

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

import java.awt.Graphics;
import java.util.List;

import org.totalboumboum.engine.loop.InteractiveLoop;
import org.totalboumboum.engine.loop.display.Display;
import org.totalboumboum.engine.loop.event.control.SystemControlEvent;
import org.totalboumboum.engine.player.AbstractPlayer;
import org.totalboumboum.engine.player.AiPlayer;

/**
 * Displays a message indicating the
 * percepts of an agent were just recorded.
 * 
 * @author Vincent Labatut
 */
public class DisplayAisRecordPercepts extends Display
{
	/**
	 * Builds a standard display object.
	 * 
	 * @param loop
	 * 		Object used for displaying.
	 */
	public DisplayAisRecordPercepts(InteractiveLoop loop)
	{	this.players = loop.getPlayers();

		eventNames.add(SystemControlEvent.REQUIRE_RECORD_AI_PERCEPTS);
	}
	
	/////////////////////////////////////////////////////////////////
	// DATA				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of players involved in the game */
	private List<AbstractPlayer> players;
	
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void switchShow(SystemControlEvent event)
	{	int index = event.getIndex();
		if(index<players.size())
		{	AbstractPlayer player = players.get(index);
			if(player instanceof AiPlayer)
				message = MESSAGE_NORMAL + (index+1);
			else
				message = null;
		}
	}

	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Capture message */
	private final String MESSAGE_NORMAL = "Percepts recorded for player #";
	/** Message to be displayed as feedback */
	private String message = null;

	@Override
	public String getMessage(SystemControlEvent event)
	{	return message;
	}
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void draw(Graphics g)
	{	// nothing to draw
	}
}
