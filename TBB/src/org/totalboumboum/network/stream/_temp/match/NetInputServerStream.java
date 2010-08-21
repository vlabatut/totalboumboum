package org.totalboumboum.network.stream._temp.match;

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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.network.stream.network.thread.RunnableReader;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetInputServerStream
{	private final boolean verbose = false;

	public NetInputServerStream(List<Socket> sockets)
	{	this.sockets = sockets;
	}

	/////////////////////////////////////////////////////////////////
	// TRANSLATION			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<Integer,HashMap<Integer,Integer>> translation;
	
	/////////////////////////////////////////////////////////////////
	// CONTROL SETTINGS		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<List<ControlSettings>> controlSettings = new ArrayList<List<ControlSettings>>();
	
	public ControlSettings getControlSettings(Profile profile)
	{	ControlSettings result = null;
		int tempIndex = profile.getSocketNumber();
		int localIndex = profile.getLocalNumber();
		int index = translation.get(streamIndex).get(localIndex);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public RemotePlayerControlEvent readEvent(int index)
	{	RemotePlayerControlEvent result = readers[index].getData();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public void initRound() throws IOException, ClassNotFoundException
	{	// init control settings
		for(int i=0;i<ins.length;i++)
		{	List<ControlSettings> cs = (List<ControlSettings>) ins[i].readObject();
			controlSettings.add(cs);
		}
	
		// start threads
		readers = new ConfigurationServerConnectionThread[ins.length];
		for(int i=0;i<ins.length;i++)
		{	readers[i] = new ConfigurationServerConnectionThread<RemotePlayerControlEvent>(ins[i]);
			readers[i].start();
		}
	}
	
	public void finishRound()
	{	finishReaders();
	}

	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream[] ins;
	private List<Socket> sockets = null;

	public void initStreams() throws IOException
	{	for(int i=0;i<sockets.size();i++)
		{	Socket socket = sockets.get(i);
			InputStream in = socket.getInputStream();
			ins[i] = new ObjectInputStream(in);
		}
	}

	public void close() throws IOException
	{	for(ObjectInputStream ooi: ins)
			ooi.close();
	}

	public int getSize()
	{	return ins.length;		
	}
	
	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<RemotePlayerControlEvent>[] readers = null;

	private void finishReaders()
	{	for(RunnableReader<RemotePlayerControlEvent> r: readers)
			r.interrupt();
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
		
			controlSettings.clear();
			sockets.clear();
			readers = null;
		}
	}
}
