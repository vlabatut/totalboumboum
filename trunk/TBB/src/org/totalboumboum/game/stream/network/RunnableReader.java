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
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;

public class RunnableReader extends Thread
{
	public RunnableReader(ObjectInputStream in)
	{	this.in = in;
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	while(!Thread.interrupted())
		{	try
			{	Object object = in.readObject();
				ReplayEvent event = (ReplayEvent) object;
				addEvent(event);
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in;

	/////////////////////////////////////////////////////////////////
	// DATA					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Queue<ReplayEvent> data = new LinkedList<ReplayEvent>();
	
	public synchronized List<ReplayEvent> getEvents()
	{	List<ReplayEvent> result = new ArrayList<ReplayEvent>(data);
		data.clear();
		return result;
	}
	
	private synchronized void addEvent(ReplayEvent event)
	{	data.offer(event);
	}
}
