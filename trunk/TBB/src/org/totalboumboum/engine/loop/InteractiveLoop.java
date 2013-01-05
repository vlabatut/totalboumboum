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
 * 
 * @author Vincent Labatut
 *
 */
public interface InteractiveLoop
{	
	/////////////////////////////////////////////////////////////////
	// AIS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void switchAiPause(int index);
	public boolean getAiPause(int index);

	/////////////////////////////////////////////////////////////////
	// PLAYERS 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public List<AbstractPlayer> getPlayers();
	
	/////////////////////////////////////////////////////////////////
	// LEVEL 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public Level getLevel();
}
