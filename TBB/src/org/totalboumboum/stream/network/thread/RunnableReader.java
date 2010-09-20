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
import java.io.ObjectInputStream;
import java.net.SocketException;

import org.totalboumboum.stream.network.AbstractConnection;
import org.totalboumboum.stream.network.message.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RunnableReader implements Runnable
{
	public RunnableReader(OwnerInterface owner)
	{	this.owner = owner;
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private OwnerInterface owner;
	
	/////////////////////////////////////////////////////////////////
	// CONNECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected AbstractConnection connection;
	
	public void setConnection(AbstractConnection connection)
	{	this.connection = connection;
	}
	
	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in;

	public void setStream(ObjectInputStream in)
	{	this.in = in;
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	while(!isFinished())
		{	if(isPaused())
			{	try
				{	wait();	
				}
				catch (InterruptedException e)
				{	// stream broken
					System.err.println("SocketException: connection lost");
					owner.connectionLost();
				}
			}
		
			try
			{	Object object = in.readObject();
				NetworkMessage message = (NetworkMessage) object;
System.out.println(">>"+message);
				connection.messageRead(message);
			}
			catch(SocketException e)
			{	// stream broken
				System.err.println("SocketException: connection lost");
				owner.connectionLost();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	
		// close stream
		try
		{	in.close();
			in = null;
			connection = null;
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// PAUSE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean paused = false;
	
	public synchronized void pause(boolean pause)
	{	if(pause!=paused)
		{	paused = pause;
			if(!pause)
				notify();
		}
	}
	
	private synchronized boolean isPaused()
	{	return paused;	
	}

	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;
		
	public synchronized boolean isFinished()
	{	return finished;
	}
	
	public synchronized void finish()
	{	finished = true;
	}
}
