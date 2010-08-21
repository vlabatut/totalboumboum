package org.totalboumboum.network.stream.network.configuration;

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
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ConfigurationServerConnectionThread extends Thread
{
	public ConfigurationServerConnectionThread(ConfigurationServerConnectionManager manager)
	{	this.manager = manager;
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ConfigurationServerConnectionManager manager;
	
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
	// RUNNABLE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	while(!Thread.interrupted())
		{	// check pause
			if(isPaused())
			{	try
				{	wait();	
				}
				catch (InterruptedException e)
				{	e.printStackTrace();
				}
			}
		
			// get next connection
			try
			{	ServerSocket serverSocket = new ServerSocket(6666);
				Socket socket = serverSocket.accept();
				manager.addConnection(socket);
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}
}
