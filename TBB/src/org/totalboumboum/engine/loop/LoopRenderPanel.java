package org.totalboumboum.engine.loop;

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

import java.awt.Font;
import java.awt.event.KeyListener;

/**
 * Interface an object displaying the game must implement
 * (only for actual rounds, not for menu browsing and such).
 * 
 * @author Vincent Labatut
 */
public interface LoopRenderPanel
{	
	/////////////////////////////////////////////////////////////////
	// GRAPHICS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Refresh the game screen.
	 */
	public void paintScreen();
	/**
	 * Perform a screen capture while playing.
	 */
	public void captureScreen();
	
	/////////////////////////////////////////////////////////////////
	// ROUND			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indicates the round is over.
	 */
	public void loopOver();
	/**
	 * Indicates a player was eliminated.
	 * 
	 * @param index
	 * 		Index of the player.
	 */
	public void playerOut(int index);
	
	/////////////////////////////////////////////////////////////////
	// CONTROLS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Adds a key listener to this panel.
	 * 
	 * @param listener
	 * 		New listener.
	 */
	public void addKeyListener(KeyListener listener);
	/** 
	 * Removes a key listener from this panel.
	 * 
	 * @param listener
	 * 		Lisener to remove.
	 */
	public void removeKeyListener(KeyListener listener);
	
	/////////////////////////////////////////////////////////////////
	// MESSAGES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** 
	 * Retrieves one of the message displayed at the begining of the round.
	 * 
	 * @return
	 * 		The required text message.
	 */
	public String getMessageTextReady();
	/** 
	 * Retrieves one of the message displayed at the begining of the round.
	 * 
	 * @return
	 * 		The required text message.
	 */
	public String getMessageTextSet();
	/** 
	 * Retrieves one of the message displayed at the begining of the round.
	 * 
	 * @return
	 * 		The required text message.
	 */
	public String getMessageTextGo();
	/** 
	 * Retrieves the font used to display messages at the begining of the round.
	 * 
	 * @param width
	 * 		Width of the message.
	 * @param height
	 * 		Height of the message.
	 * @return
	 * 		The appropriate font for the message.
	 */
	public Font getMessageFont(double width, double height);
}
