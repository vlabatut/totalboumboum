package org.totalboumboum.gui.common.content.subpanel.game;

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

import org.totalboumboum.stream.network.data.game.GameInfo;

/**
 * Interface for objects listening to the associated panel.
 *  
 * @author Vincent Labatut
 */
public interface GameListSubPanelListener
{
	/**
	 * Called when the game selection has changed.
	 * 
	 * @param gameId 
	 * 		Id of the selected game.
	 */
	public void gameSelectionChanged(String gameId);

	/**
	 * Called when the line has changed.
	 * 
	 * @param gameInfo 
	 * 		Info of the changed game.
	 */
	public void gameLineModified(GameInfo gameInfo);

	/**
	 * Called when the requested game has changed.
	 * 
	 * @param gameInfo 
	 * 		Info of the requested game.
	 */
	public void refreshGameRequested(GameInfo gameInfo);

	/**
	 * Called when the previous game is required.
	 */
	public void gameBeforeClicked();

	/**
	 * Called when the next game is required.
	 */
	public void gameAfterClicked();

	/**
	 * Called when a game is added.
	 */
	public void gameAddClicked();
}
