package org.totalboumboum.game.stream;

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
import java.io.ObjectOutputStream;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.round.Round;

public abstract class OutputClientStream
{	private static final boolean VERBOSE = false;
		
	public OutputClientStream()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void writeControlSettings(ControlSettings controlSettings) throws IOException
	{	write(controlSettings);
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * records an event in the currently open stream.
	 */
	public void writeEvent(RemotePlayerControlEvent event)
	{	try
		{	out.writeObject(event);
			if(VERBOSE)
				System.out.println("recording: "+event);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * writes player-related information in the open stream
	 */
	protected void initRound() throws IOException
	{	writeControlSettings(controlSettings);
	}
	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ObjectOutputStream out = null;
		
	protected abstract void write(Object object) throws IOException;
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;
	
		out = null;
	}		
}
