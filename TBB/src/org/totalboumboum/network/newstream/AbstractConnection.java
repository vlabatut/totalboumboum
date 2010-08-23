package org.totalboumboum.network.newstream;

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

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractConnection
{	
	public AbstractConnection(Socket socket) throws IOException
	{	// init streams
		InputStream is = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(is);
		OutputStream os = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(os);
		
		// init threads
		reader = new RunnableReader<Object>(in,this);
		reader.start();
		writer = new RunnableWriter(out);
		writer.start();
	}

	/////////////////////////////////////////////////////////////////
	// READER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<Object> reader = null;

	public abstract void dataRead(Object data);

	public void pauseReader(boolean pause)
	{	reader.pause(pause);		
	}

	/////////////////////////////////////////////////////////////////
	// WRITER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableWriter writer = null;
	
	public void write(Object object) throws IOException
	{	writer.addObject(object);
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			// reader
			reader.interrupt();
			reader = null;
			
			// writer
			writer.interrupt();
			writer = null;
		}
	}
}
