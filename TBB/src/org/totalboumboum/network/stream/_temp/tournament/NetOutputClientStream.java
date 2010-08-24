package org.totalboumboum.network.stream._temp.tournament;

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
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.network.stream.network.thread.RunnableWriter;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetOutputClientStream
{	private final boolean verbose = false;

	public NetOutputClientStream(Socket socket, List<ControlSettings> controlSettings)
	{	this.controlSettings = controlSettings;
		this.socket = socket;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ControlSettings> controlSettings = null;
	
	public void writeControlSettings() throws IOException
	{	write(controlSettings);
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
	public void initRound() throws IOException
	{	writeControlSettings();

		writer = new RunnableWriter(out);
		writer.start();
	}
	
	public void finishRound()
	{	finishWriter();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectOutputStream out = null;
	private Socket socket = null;
	
	public void initStreams() throws IOException
	{	OutputStream o = socket.getOutputStream();
		out = new ObjectOutputStream(o);
	}

	private void write(Object object) throws IOException
	{	writer.addMessage(object);
	}

	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableWriter writer = null;

	private void finishWriter()
	{	writer.interrupt();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	if(!finished)
		{	finished = true;
			
			out = null;
			socket = null;
			writer = null;
		}
	}
}
