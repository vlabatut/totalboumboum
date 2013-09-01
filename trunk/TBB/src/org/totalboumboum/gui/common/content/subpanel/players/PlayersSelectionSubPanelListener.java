package org.totalboumboum.gui.common.content.subpanel.players;

import java.awt.event.MouseEvent;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface PlayersSelectionSubPanelListener
{
	/**
	 * Event fired when a player is added to the selection.
	 * 
	 * @param index
	 * 		Position of the new player in the selection.
	 */
	public void playerSelectionPlayerAdded(int index);
	
	/**
	 * Event fired when several players are added to
	 * the selection at once.
	 */
	public void playerSelectionPlayersAdded();
	
	/**
	 * Event fired when a player is removed from
	 * the selection.
	 * 
	 * @param index
	 * 		Position of the removed player (before removal).
	 */
	public void playerSelectionPlayerRemoved(int index);
	
	/**
	 * Event fired when a player is replaced
	 * by another one.
	 * 
	 * @param index
	 * 		Position of the concerned player.
	 */
	public void playerSelectionProfileSet(int index);
	
	/**
	 * Event fired when the sprite used by
	 * a player is changed.
	 * 
	 * @param index
	 * 		Position of the concerned player.
	 */
	public void playerSelectionHeroSet(int index);
	
	/** 
	 * Event fired when the color used by
	 * a player is changed.
	 *  
	 * @param index
	 * 		Position of the concerned player.
	 */
	public void playerSelectionColorSet(int index);
	
	/**
	 * Event fired when the controls of
	 * a player are changed.
	 * 
	 * @param index
	 * 		Position of the concerned player.
	 */
	public void playerSelectionControlsSet(int index);
	
	/**
	 * Event fired when the mouse is
	 * clicked anywhere in the listened panel.
	 * 
	 * @param e
	 * 		Associated mouse event.
	 */
	public void mousePressed(MouseEvent e);
}
