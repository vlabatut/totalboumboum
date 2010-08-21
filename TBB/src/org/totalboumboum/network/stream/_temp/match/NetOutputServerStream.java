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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.network.stream.network.thread.RunnableWriter;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetOutputServerStream
{	private final boolean verbose = false;

	public NetOutputServerStream(Round round, List<Socket> sockets)
	{	this.round = round;
		this.sockets = sockets;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZOOM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void writeZoomCoef(double zoomCoef) throws IOException
	{	write(zoomCoef);
	}
	
	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeProfiles() throws IOException
	{	List<Profile> profiles = round.getProfiles();
		write(profiles);
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeLevelInfo() throws IOException
	{	LevelInfo leveInfo = round.getHollowLevel().getLevelInfo();
		for(ObjectOutputStream o: outs)
			o.writeObject(leveInfo);
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeLimits() throws IOException
	{	Limits<RoundLimit> limits = round.getLimits();
		write(limits);
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeItems() throws IOException
	{	HashMap<String,Integer> itemsCounts = round.getHollowLevel().getItemCount();
		write(itemsCounts);
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void writeStats() throws IOException
	{	StatisticRound stats = round.getStats();
		write(stats);
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void writeEvent(StreamedEvent event)
	{	try
		{	for(ObjectOutputStream o: outs)
				o.writeObject(event);
			if(verbose)
				System.out.println("recording: "+event);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}

	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round round;

	public void initRound() throws IOException
	{	// start threads
		for(ObjectOutputStream oo: outs)
		{	RunnableWriter w = new RunnableWriter(oo);
			writers.add(w);
			w.start();
		}
		
		writeProfiles();
		writeLevelInfo();
		writeLimits();
		writeItems();
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public void finishRound(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	// put a stop event
		StopReplayEvent event = new StopReplayEvent();
		writeEvent(event);
		
		// record the stats
		writeStats();
		
		if(verbose)
			System.out.println("recording: stats");
		
		finishWriters();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ObjectOutputStream> outs = new ArrayList<ObjectOutputStream>();
	private List<Socket> sockets = null;
	
	public void initStreams() throws IOException
	{	// init streams and threads
		for(Socket socket: sockets)
		{	OutputStream o = socket.getOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(o);
			outs.add(oo);
		}
	}

	protected void write(Object object) throws IOException
	{	for(RunnableWriter w: writers)
			w.addObject(object);
	}

	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<RunnableWriter> writers = new ArrayList<RunnableWriter>();

	private void finishWriters() throws IOException
	{	for(RunnableWriter w: writers)
			w.interrupt();
	}

	/////////////////////////////////////////////////////////////////
	// FILTER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean filterEvents = true;

	public void setFilterEvents(boolean flag)
	{	filterEvents = flag;		
	}
	
	public boolean getFilterEvents()
	{	return filterEvents;		
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			outs.clear();
			round = null;
	
			sockets.clear();
			writers.clear();
		}
	}
}
