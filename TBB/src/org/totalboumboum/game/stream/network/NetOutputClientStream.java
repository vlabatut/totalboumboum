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

import org.totalboumboum.game.stream.OutputClientStream;

public class NetOutputClientStream extends OutputClientStream
{	
	public NetOutputClientStream(Socket socket) throws IOException
	{	// init net-related stuff
		OutputStream o = socket.getOutputStream();
		out = new ObjectOutputStream(o);
	
		// init round
		initRound();
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	private void initRound() throws IOException
	{	writer = new RunnableWriter(out);
		writer.start();
	}
	
	public void finishRound()
	{	finishWriter();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
			
			writer = null;
		}
	}
}
