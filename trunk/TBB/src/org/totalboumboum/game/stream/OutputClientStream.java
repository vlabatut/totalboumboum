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
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;

public abstract class OutputClientStream
{	protected final boolean verbose = false;
		
	public OutputClientStream(List<ControlSettings> controlSettings)
	{	this.controlSettings = controlSettings;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<ControlSettings> controlSettings = null;
	
	public void writeControlSettings() throws IOException
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
			if(verbose)
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
	{	writeControlSettings();
	}
	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ObjectOutputStream out = null;
		
	public abstract void initStreams() throws IOException;

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
