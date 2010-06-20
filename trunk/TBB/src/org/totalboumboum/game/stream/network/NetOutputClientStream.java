package org.totalboumboum.game.stream.network;

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
import org.totalboumboum.game.stream.OutputClientStream;

public class NetOutputClientStream extends OutputClientStream
{	
	public NetOutputClientStream(Socket socket, List<ControlSettings> controlSettings) throws IOException
	{	super(controlSettings);
	
		this.socket = socket;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void initRound() throws IOException
	{	super.initRound();

		writer = new RunnableWriter(out);
		writer.start();
	}
	
	public void finishRound()
	{	finishWriter();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Socket socket = null;
	
	@Override
	public void initStreams() throws IOException
	{	OutputStream o = socket.getOutputStream();
		out = new ObjectOutputStream(o);
	}

	@Override
	protected void write(Object object) throws IOException
	{	writer.addObject(object);
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
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			socket = null;
			writer = null;
		}
	}
}
