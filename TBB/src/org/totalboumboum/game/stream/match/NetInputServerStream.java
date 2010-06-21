package org.totalboumboum.game.stream.match;

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
import java.util.List;

import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.stream.InputServerStream;

public class NetInputServerStream extends InputServerStream
{	
	public NetInputServerStream(List<Socket> sockets) throws IOException
	{	super();
		
		this.sockets = sockets;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public RemotePlayerControlEvent readEvent(int index)
	{	RemotePlayerControlEvent result = readers[index].getData();
		return result;
	}
/*
	public List<RemotePlayerControlEvent> readEvents()
	{	List<RemotePlayerControlEvent> result = new ArrayList<RemotePlayerControlEvent>();
		for(RunnableReader<RemotePlayerControlEvent> reader: readers)
		{	RemotePlayerControlEvent event = reader.getData();
			if(event!=null)
				result.add(event);
		}
		return result;
	}
*/
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	@Override
	public void initRound() throws IOException, ClassNotFoundException
	{	super.initRound();
	
		// start threads
		readers = new RunnableReader[ins.length];
		for(int i=0;i<ins.length;i++)
		{	readers[i] = new RunnableReader<RemotePlayerControlEvent>(ins[i]);
			readers[i].start();
		}
	}
	
	public void finishRound()
	{	finishReaders();
	}

	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Socket> sockets = null;

	@Override
	public void initStreams() throws IOException
	{	for(int i=0;i<sockets.size();i++)
		{	Socket socket = sockets.get(i);
			InputStream in = socket.getInputStream();
			ins[i] = new ObjectInputStream(in);
		}
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
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			sockets.clear();
			readers = null;
		}
	}
}
