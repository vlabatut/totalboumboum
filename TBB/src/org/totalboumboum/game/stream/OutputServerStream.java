package org.totalboumboum.game.stream;

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
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.xml.sax.SAXException;

public abstract class OutputServerStream
{	private static final boolean VERBOSE = false;
		
	public OutputServerStream(Round round)
	{	this.round = round;
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
	protected void writeProfiles() throws IOException
	{	List<Profile> profiles = round.getProfiles();
		write(profiles);
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void writeLevelInfo() throws IOException
	{	LevelInfo leveInfo = round.getHollowLevel().getLevelInfo();
		for(ObjectOutputStream o: outs)
			o.writeObject(leveInfo);
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void writeLimits() throws IOException
	{	Limits<RoundLimit> limits = round.getLimits();
		write(limits);
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void writeItems() throws IOException
	{	HashMap<String,Integer> itemsCounts = round.getHollowLevel().getItemCount();
		write(itemsCounts);
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void writeStats() throws IOException
	{	StatisticRound stats = round.getStats();
		write(stats);
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * records an event in the currently open stream.
	 */
	public void writeEvent(StreamedEvent event)
	{	try
		{	for(ObjectOutputStream o: outs)
				o.writeObject(event);
			if(VERBOSE)
				System.out.println("recording: "+event);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Round round;
	
	/**
	 * writes round-related information in the open streams
	 */
	protected void initRound() throws IOException
	{	writeProfiles();
		writeLevelInfo();
		writeLimits();
		writeItems();
	}

	/**
	 * close the replay output stream
	 */
	public void finishRound(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	// put a stop event
		StopReplayEvent event = new StopReplayEvent();
		writeEvent(event);
		
		// record the stats
		writeStats();
		
		if(VERBOSE)
			System.out.println("recording: stats");
	}

	/////////////////////////////////////////////////////////////////
	// STREAMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected final List<ObjectOutputStream> outs = new ArrayList<ObjectOutputStream>();

	protected abstract void write(Object object) throws IOException;
	
	/////////////////////////////////////////////////////////////////
	// FILTER				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean filterEvents = true;

	public void setFilterEvents(boolean flag)
	{	filterEvents = flag;		
	}
	
	public boolean getFilterEvents()
	{	return filterEvents;		
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;
	
		outs.clear();
		round = null;
	}		
}
