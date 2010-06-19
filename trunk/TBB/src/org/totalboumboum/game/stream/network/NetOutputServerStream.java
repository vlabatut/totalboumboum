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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.stream.OutputServerStream;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.xml.sax.SAXException;

public class NetOutputServerStream extends OutputServerStream
{	
	public NetOutputServerStream(Round round, List<Socket> sockets) throws IOException
	{	super(round);
	
		// init net-related stuff
		
		// init round
		initRound(sockets);
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	private void initRound(List<Socket> sockets) throws IOException
	{	// init streams and threads
		for(Socket socket: sockets)
		{	OutputStream o = socket.getOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(o);
			outs.add(oo);
			RunnableWriter w = new RunnableWriter(oo);
			writers.add(w);
			w.start();
		}
		
		initRound();
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	@Override
	public void finishRound(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	super.finishRound(stats);
		
		finishWriters();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void write(Object object) throws IOException
	{	for(RunnableWriter w: writers)
			w.addObject(object);
	}

	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<RunnableWriter> writers = new ArrayList<RunnableWriter>();

	private void finishWriters() throws IOException
	{	for(RunnableWriter w: writers)
			w.interrupt();
	}
}
