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
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.statistics.detailed.StatisticRound;

public abstract class InputClientStream
{	protected final boolean verbose = false;

	public InputClientStream()
	{
	}

	/////////////////////////////////////////////////////////////////
	// ZOOM					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Double zoomCoef = null;

	public double getZoomCoef()
	{	return zoomCoef;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<Profile> profiles = null;

	public List<Profile> getProfiles()
	{	return profiles;
	}

	/////////////////////////////////////////////////////////////////
	// INFO					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected LevelInfo levelInfo = null;

	public LevelInfo getLevelInfo()
	{	return levelInfo;
	}
	
	/////////////////////////////////////////////////////////////////
	// LIMITS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected Limits<RoundLimit> roundLimits = null;

	public Limits<RoundLimit> getRoundLimits()
	{	return roundLimits;
	}
	
	/////////////////////////////////////////////////////////////////
	// ITEMS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected HashMap<String,Integer> itemCounts = null;

	public HashMap<String,Integer> getItemCounts()
	{	return itemCounts;
	}

	/////////////////////////////////////////////////////////////////
	// STATS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected StatisticRound roundStats = null;
	
	public StatisticRound getRoundStats()
	{	return roundStats;
	}

	/////////////////////////////////////////////////////////////////
	// EVENTS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads an event in the currently open stream.
	 */
	public abstract ReplayEvent readEvent();
	
	/////////////////////////////////////////////////////////////////
	// ROUND				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * reads round-related information in the open stream
	 */
	@SuppressWarnings("unchecked")
	public void initRound() throws IOException, ClassNotFoundException
	{	profiles = (List<Profile>) in.readObject();
		levelInfo = (LevelInfo) in.readObject();
		roundLimits = (Limits<RoundLimit>) in.readObject();		
		itemCounts = (HashMap<String,Integer>) in.readObject();		
		zoomCoef = (Double) in.readObject();
	}

	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public void finishRound() throws IOException, ClassNotFoundException
	{	if(verbose)
			System.out.println("reading: stats");
		roundStats = (StatisticRound) in.readObject();
	}

	/////////////////////////////////////////////////////////////////
	// STREAM				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ObjectInputStream in = null;

	public abstract void initStreams() throws IOException;

	public void close() throws IOException
	{	in.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean finished = false;
	
	public void finish()
	{	finished = true;
		
		in = null;
		
		itemCounts = null;
		levelInfo = null;
		profiles = null;
		roundLimits = null;
		roundStats = null;
		zoomCoef = null;
	}
}
