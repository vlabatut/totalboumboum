package org.totalboumboum.engine.loop.event.replay;

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

import org.totalboumboum.engine.loop.event.StreamedEvent;

/**
 * Event used when recording a round to a file,
 * or when sending through network.
 * 
 * @author Vincent Labatut
 */
public abstract class ReplayEvent extends StreamedEvent
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a new replay event.
	 */
	protected ReplayEvent()
	{	super();
	}
	
	/////////////////////////////////////////////////////////////////
	// SEND EVENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the event should be sent or not */
	protected transient boolean sendEvent = true;

	/**
	 * Indicates whether the event should be sent or not.
	 * 
	 * @return
	 * 		{@code true} iff the event should be sent.
	 */
	public boolean getSendEvent()
	{	return sendEvent;	
	}
}
