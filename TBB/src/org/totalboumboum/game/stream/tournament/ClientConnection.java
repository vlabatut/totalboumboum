package org.totalboumboum.game.stream.tournament;

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
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.game.stream.RunnableReader;
import org.totalboumboum.game.stream.RunnableWriter;

public class ClientConnection
{	
	public ClientConnection(Socket socket)
	{	this.socket = socket;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> profiles = new ArrayList<Profile>();

	public List<Profile> getProfiles()
	{	return profiles;
	}

	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ControlSettings> controlSettings = new ArrayList<ControlSettings>();

	public List<ControlSettings> getControlSettings()
	{	return controlSettings;
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	
	public void initStreams() throws IOException
	{	// create streams
		InputStream is = socket.getInputStream();
		in = new ObjectInputStream(is);
		OutputStream os = socket.getOutputStream();
		out = new ObjectOutputStream(os);

		//TODO lire les trucs d'init (profils/cs)

		// create threads
		reader = new RunnableReader<Object>(in);
		reader.start();
		writer = new RunnableWriter(out);
		writer.start();
	}

	public void close() throws IOException
	{	reader.interrupt();
		in.close();
		
	}
	
	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<Object> reader = null;
	private RunnableWriter writer = null;

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			profiles.clear();
			controlSettings.clear();
	
			in = null;
			out = null;

			socket = null;
			reader = null;
			writer = null;
		}
	}
}
