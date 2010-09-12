package org.totalboumboum.stream.newstream;

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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.totalboumboum.stream.newstream.message.NetworkMessage;
import org.totalboumboum.stream.newstream.thread.RunnableReader;
import org.totalboumboum.stream.newstream.thread.RunnableWriter;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractConnection
{	
	public AbstractConnection()
	{	writer = new RunnableWriter();
		reader = new RunnableReader();
	}
	
//	public AbstractConnection(Socket socket) throws IOException
//	{	initConnection(socket);
//	}

	protected void initConnection(Socket socket, boolean order) throws IOException
	{	// init streams
		ObjectInputStream in;
		ObjectOutputStream out;
		if(order)
		{	InputStream is = socket.getInputStream();
			in = new ObjectInputStream(is);
			OutputStream os = socket.getOutputStream();
			out = new ObjectOutputStream(os);
		}
		else
		{	OutputStream os = socket.getOutputStream();
			out = new ObjectOutputStream(os);
			InputStream is = socket.getInputStream();
			in = new ObjectInputStream(is);
		}
		
		// init writer
		writer.setStream(out);
		Thread writeThread = new Thread(writer);
		writeThread.start();
	
		// init reader
		reader.setStream(in);
		reader.setConnection(this);
		Thread readThread = new Thread(reader);
		readThread.start();
	}
	
	/////////////////////////////////////////////////////////////////
	// READER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected RunnableReader reader = null;

/*	public RunnableReader getReader()
	{	return reader;
	}
*/	
	public abstract void messageRead(NetworkMessage message);

	public void pauseReader(boolean pause)
	{	reader.pause(pause);		
	}

	/////////////////////////////////////////////////////////////////
	// WRITER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected RunnableWriter writer = null;
	
/*	public RunnableWriter getWriter()
	{	return writer;
	}
*/	
	public void writeMessage(NetworkMessage message) throws IOException
	{	writer.addMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			// reader
			reader.finish();
			reader = null;
			
			// writer
			writer.finish();
			writer = null;
		}
	}
}
