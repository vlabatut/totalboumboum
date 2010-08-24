package org.totalboumboum.network.stream._temp.tournament;

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
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.network.stream.network.thread.RunnableReader;
import org.totalboumboum.network.stream.network.thread.RunnableWriter;

/**
 * represents a client-side connection with a server
 * @author Vincent Labatut
 *
 */
public class ServerConnection
{	
	public ServerConnection(Socket socket)
	{	this.socket = socket;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * NOTE to be done after starting the thread or it won't work...
	 */
	public void initTournament(List<Profile> profiles, List<ControlSettings> controlSettings) throws IOException, ClassNotFoundException
	{	writer.addMessage(profiles);
		writer.addMessage(controlSettings);
	}
	
	public void updateTournament()
	{
		//TODO
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initMatch()
	{
		//TODO
	}

	public void updateMatch()
	{
		//TODO
	}

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void initRound()
	{
		//TODO
	}

	public void updateRound()
	{
		//TODO
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public void initStreams() throws IOException
	{	InputStream is = socket.getInputStream();
		in = new ObjectInputStream(is);
		OutputStream os = socket.getOutputStream();
		out = new ObjectOutputStream(os);
	}

	public void close() throws IOException
	{	reader.interrupt();
		writer.interrupt();
		in.close();
		out.close();
	}
	
	public void write(Object object) throws IOException
	{	writer.addMessage(object);
	}
	
	public Object readObject()
	{	Object result = reader.getData();
		return result;
	}

	public List<Object> readObjects()
	{	List<Object> result = reader.getAllData();
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<Object> reader = null;
	private RunnableWriter writer = null;
	
	public void initThreads()
	{	reader = new ConfigurationServerConnectionThread<Object>(in);
		reader.start();
		writer = new RunnableWriter(out);
		writer.start();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			in = null;
			out = null;

			socket = null;
			reader = null;
			writer = null;
		}
	}
}
