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

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;

/**
 * By opposition to the interactive type
 * of loops, replayed loops do not let the user
 * affect how the game goes.
 * 
 * @author Vincent Labatut
 */
public interface ReplayedLoop
{	
	/**
	 * Fetch the next game event,
	 * to update the loop.
	 * <br/>
	 * Must always returns an event.
	 * If the list is empty, the thread is blocked until an event arrives
	 * 
	 * @return
	 * 		Next game event.
	 */
	public ReplayEvent retrieveEvent();
}
