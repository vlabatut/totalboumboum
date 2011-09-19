package org.totalboumboum.stream.network;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.stream.network.message.NetworkMessage;
import org.totalboumboum.stream.network.thread.OwnerInterface;
import org.totalboumboum.stream.network.thread.RunnableReader;
import org.totalboumboum.stream.network.thread.RunnableWriter;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractConnection implements OwnerInterface
{	
	public AbstractConnection()
	{	writer = new RunnableWriter(this);
		reader = new RunnableReader(this);
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
		writeThread.setName("TBB.netwriter");
		writeThread.start();
	
		// init reader
		reader.setStream(in);
		reader.setConnection(this);
		Thread readThread = new Thread(reader);
		readThread.setName("TBB.netreader");
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
	protected Lock ioLock = new ReentrantLock();
	protected boolean ioFinished = false;

	public void finish()
	{	ioLock.lock();
		{	if(!finished)
			{	finished = true;
		
				if(!ioFinished)
				{	ioFinished = true;
				
					reader.finish();
					writer.finish();
		
					//TODO à completer
		
					reader = null;
					writer = null;
				}
			}
			ioLock.unlock();		
		}
	}
}
