package org.totalboumboum.stream.network.thread;

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
import java.net.SocketException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.stream.network.message.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RunnableWriter implements Runnable
{
	public RunnableWriter(OwnerInterface owner)
	{	this.owner = owner;
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private OwnerInterface owner;
	
	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectOutputStream out;

	public void setStream(ObjectOutputStream out)
	{	this.out = out;
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public synchronized void run()
	{	while(!isFinished() || !isEmpty())
		{	// wait for some objects to write
			while(isEmpty())
			{	try
				{	wait();
				}
				catch (InterruptedException e)
				{	//e.printStackTrace();
				}
			}
			// write the first object
			try
			{	NetworkMessage message = getMessage();
				out.writeObject(message);
				out.flush();out.reset();
System.out.println("<<"+message);
			}
			catch(SocketException e)
			{	// stream broken
				System.err.println("SocketException: connection lost");
				owner.connectionLost();
			}
			catch(IOException e)
			{	e.printStackTrace();
			}
		}
//TODO should be in a finally(?)
		// close stream
		try
		{	out.close();
			out = null;
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// DATA					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Queue<NetworkMessage> data = new LinkedList<NetworkMessage>();
	
	private synchronized boolean isEmpty()
	{	boolean result = data.isEmpty();
		return result;
	}
	
	private synchronized NetworkMessage getMessage()
	{	NetworkMessage result = data.poll();
		return result;
	}
	
	public synchronized void addMessage(NetworkMessage message)
	{	data.offer(message);
		notify();
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
	private Lock finishLock = new ReentrantLock();
	
	public boolean isFinished()
	{	boolean result;
		finishLock.lock();
		{	result = finished;
		}
		finishLock.lock();
		return result;
	}
	
	public void finish()
	{	finishLock.lock();
		{	finished = true;
			try
			{	out.close();
			}
			catch (IOException e)
			{	//e.printStackTrace();
			}
		}
	}
}
