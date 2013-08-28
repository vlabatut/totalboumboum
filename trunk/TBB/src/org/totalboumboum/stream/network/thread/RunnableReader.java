package org.totalboumboum.stream.network.thread;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.stream.network.AbstractConnexion;
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
	protected AbstractConnexion connexion;
	
	public void setConnexion(AbstractConnexion connexion)
	{	this.connexion = connexion;
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
		{	pausedLock.lock();
			{	if(paused)
				{	try
					{	pausedCondition.await();
					}
					catch (InterruptedException e)
					{	// stream broken
						System.err.println("SocketException: connexion lost");
						owner.connexionLost();
					}
				}
			}
			pausedLock.unlock();
		
			try
			{	Object object = in.readObject();
				NetworkMessage message = (NetworkMessage) object;
System.out.println(">>"+message);
				connexion.messageRead(message);
			}
			catch(SocketException e)
			{	// stream broken
				System.err.println("SocketException: connexion lost");
				owner.connexionLost();
			}
			catch(EOFException e)
			{	// stream broken
				System.err.println("EOFException: connexion lost");
				owner.connexionLost();
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
			connexion = null;
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// PAUSE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean paused = false;
	private Lock pausedLock = new ReentrantLock();
	private Condition pausedCondition = pausedLock.newCondition();
	
	public void pause(boolean pause)
	{	pausedLock.lock();
		{	if(pause!=paused)
			{	paused = pause;
				if(!pause)
					pausedCondition.signal();
			}
		}
		pausedLock.unlock();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISHED				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether this object has been deleted or not */
	private boolean finished = false;
	private Lock finishLock = new ReentrantLock();
		
	public boolean isFinished()
	{	boolean result;
		finishLock.lock();
		{	result = finished;
		}
		finishLock.unlock();
		return result;
	}
	
	/**
	 * Cleanly finishes this object,
	 * possibly freeing some memory.
	 */
	public void finish()
	{	finishLock.lock();
		{	finished = true;
			try
			{	if(in!=null)
					in.close();
			}
			catch (IOException e)
			{	//e.printStackTrace();
			}
		}
		finishLock.unlock();		
	}
}
