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
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.network.stream.network.thread.RunnableReader;
import org.totalboumboum.statistics.detailed.StatisticRound;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class NetInputClientStream
{	private final boolean verbose = false;

	public NetInputClientStream(Socket socket)
	{	this.socket = socket;
	}

	/////////////////////////////////////////////////////////////////
	// ZOOM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double zoomCoef = null;

	public double getZoomCoef()
	{	return zoomCoef;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Profile> profiles = null;

	public List<Profile> getProfiles()
	{	return profiles;
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private LevelInfo levelInfo = null;

	public LevelInfo getLevelInfo()
	{	return levelInfo;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<RoundLimit> roundLimits = null;

	public Limits<RoundLimit> getRoundLimits()
	{	return roundLimits;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HashMap<String,Integer> itemCounts = null;

	public HashMap<String,Integer> getItemCounts()
	{	return itemCounts;
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound roundStats = null;
	
	public StatisticRound getRoundStats()
	{	return roundStats;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public ReplayEvent readEvent()
	{	ReplayEvent result = reader.getData();
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public void initRound() throws IOException, ClassNotFoundException
	{	profiles = (List<Profile>) in.readObject();
		levelInfo = (LevelInfo) in.readObject();
		roundLimits = (Limits<RoundLimit>) in.readObject();		
		itemCounts = (HashMap<String,Integer>) in.readObject();		
		zoomCoef = (Double) in.readObject();
	
		reader = new ConfigurationServerConnectionThread<ReplayEvent>(in);
		reader.start();
	}

	public void finishRound() throws IOException, ClassNotFoundException
	{	if(verbose)
			System.out.println("reading: stats");
		roundStats = (StatisticRound) in.readObject();
		finishReader();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Socket socket = null;
	private ObjectInputStream in = null;

	public void initStreams() throws IOException
	{	InputStream i = socket.getInputStream();
		in = new ObjectInputStream(i);
	}

	public void close() throws IOException
	{	in.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// THREADS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RunnableReader<ReplayEvent> reader = null;

	private void finishReader()
	{	reader.interrupt();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean finished = false;

	public void finish()
	{	if(!finished)
		{	finished = true;
			
			in = null;
		
			itemCounts = null;
			levelInfo = null;
			profiles = null;
			roundLimits = null;
			roundStats = null;
			zoomCoef = null;
	
			socket = null;
			reader = null;
		}
	}
}
