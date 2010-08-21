package org.totalboumboum.network.stream._temp;

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
import java.util.LinkedList;
import java.util.Queue;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RunnableWriter extends Thread
{
	public RunnableWriter(ObjectOutputStream out)
	{	this.out = out;
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	while(!Thread.interrupted())
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
			{	Object object = getObject();
				out.writeObject(object);
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
/*	
		try
		{	out.close();
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
*/
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectOutputStream out;

	/////////////////////////////////////////////////////////////////
	// DATA					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Queue<Object> data = new LinkedList<Object>();
	
	private synchronized boolean isEmpty()
	{	boolean result = data.isEmpty();
		return result;
	}
	
	private synchronized Object getObject()
	{	Object result = data.poll();
		return result;
	}
	
	public synchronized void addObject(Object object)
	{	data.offer(object);
	}
}
