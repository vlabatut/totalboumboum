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
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.stream.InputClientStream;

public class NetInputClientStream extends InputClientStream
{	
	public NetInputClientStream(Socket socket) throws IOException
	{	super();
	
		this.socket = socket;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public void initRound() throws IOException, ClassNotFoundException
	{	super.initRound();
	
		reader = new RunnableReader<ReplayEvent>(in);
		reader.start();
	}

	@Override
	public void finishRound() throws IOException, ClassNotFoundException
	{	super.finishRound();
		
		finishReader();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Socket socket = null;
	
	@Override
	public void initStreams() throws IOException
	{	InputStream i = socket.getInputStream();
		in = new ObjectInputStream(i);
	}

	@Override
	public List<ReplayEvent> readEvents()
	{	List<ReplayEvent> result = reader.getData();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<ReplayEvent> reader = null;

	private void finishReader()
	{	reader.interrupt();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			socket = null;
			reader = null;
		}
	}
}
