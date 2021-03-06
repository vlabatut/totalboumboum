package org.totalboumboum.engine.loop.display;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.engine.loop.event.control.SystemControlEvent;

/**
 * General class, in charge of displaying
 * various data during the game.
 * 
 * @author Vincent Labatut
 */
public abstract class Display
{
	/////////////////////////////////////////////////////////////////
	// SHOW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Changes the content this display
	 * is drawing on the game panel.
	 * 
	 * @param event
	 * 		Event received.
	 */
	public abstract void switchShow(SystemControlEvent event);
	
	/////////////////////////////////////////////////////////////////
	// TEXT				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the current message to be displayed.
	 * 
	 * @param event
	 * 		Event used to update the message.
	 * @return
	 * 		Message to be displayed.
	 */
	public abstract String getMessage(SystemControlEvent event);
	
	/////////////////////////////////////////////////////////////////
	// DRAW				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Updates the game panel with the
	 * data associated to this display.
	 * 
	 * @param g
	 * 		Graphical object used while drawing.
	 */
	public abstract void draw(Graphics g);

	/////////////////////////////////////////////////////////////////
	// EVENT NAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Events this display reacts to */
	protected final List<String> eventNames = new ArrayList<String>();
	
	/**
	 * Returns the events this display 
	 * reacts to.
	 * 
	 * @return
	 * 		List of the names of the concerned events.
	 */
	public List<String> getEventNames()
	{	return eventNames;
	}
}
