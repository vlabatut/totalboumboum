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

import java.util.List;

import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.player.AbstractPlayer;

/**
 * This interface must be implemented by all loops
 * allowing the user to interact with the game.
 * This concerns {@link ClientLoop} and {@link LocalLoop}
 * including {@link RegularLoop} and {@link ServerLoop},
 * but not {@link ReplayedLoop}, which is not interactive,
 * nor {@link SimulationLoop} which is not even visible.
 * 
 * @author Vincent Labatut
 */
public interface InteractiveLoop
{	
	/////////////////////////////////////////////////////////////////
	// AIS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Changes the pause state of the agent
	 * player whose index is specified as
	 * a parameter.
	 * 
	 * @param index
	 * 		Index of the agent to be paused/unpaused.
	 */
	public void switchAiPause(int index);
	
	/**
	 * Returns the pause state of the
	 * agent whose index is specified as
	 * a parameter.
	 * 
	 * @param index
	 * 		Index of the agent of interest.
	 * @return
	 * 		{@code true} iff it is currently paused. 
	 */
	public boolean getAiPause(int index);
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the list of all players
	 * involved in this loop.
	 * 
	 * @return
	 * 		List of players.
	 */
	public List<AbstractPlayer> getPlayers();
	
	/////////////////////////////////////////////////////////////////
	// LEVEL 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the level object played
	 * during the game displayed by
	 * this loop.
	 * 
	 * @return
	 * 		The level used by this loop.
	 */
	public Level getLevel();
}
