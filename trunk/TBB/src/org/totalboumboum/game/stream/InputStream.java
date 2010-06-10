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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.statistics.detailed.StatisticRound;

public abstract class InputStream
{	private static final boolean VERBOSE = false;
	
	/////////////////////////////////////////////////////////////////
	// INPUT				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ObjectInputStream in = null;
	private Double readZoomCoef = null;
	private List<Profile> readProfiles = null;
	private LevelInfo readLevelInfo = null;
	private Limits<RoundLimit> readRoundLimits = null;
	private HashMap<String,Integer> readItemCounts = null;
	private StatisticRound readRoundStats = null;
	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	@SuppressWarnings("unchecked")
	public void initReplaying() throws IOException, ClassNotFoundException
	{	readProfiles = (List<Profile>) in.readObject();
		readLevelInfo = (LevelInfo) in.readObject();
		readRoundLimits = (Limits<RoundLimit>) in.readObject();		
		readItemCounts = (HashMap<String,Integer>) in.readObject();		
		readZoomCoef = (Double) in.readObject();
	}
	
	public double getReadZoomCoef()
	{	return readZoomCoef;
	}
	
	public List<Profile> getReadProfiles()
	{	return readProfiles;
	}
	
	public LevelInfo getReadLevelInfo()
	{	return readLevelInfo;
	}
	
	public Limits<RoundLimit> getReadRoundLimits()
	{	return readRoundLimits;
	}
	
	public HashMap<String,Integer> getReadItemCounts()
	{	return readItemCounts;
	}

	public StatisticRound getReadRoundStats()
	{	return readRoundStats;
	}

	/**
	 * reads an event in the currently open stream.
	 */
	public ReplayEvent readEvent()
	{	ReplayEvent result = null;
		
		try
		{	Object object = in.readObject();
			if(object instanceof ReplayEvent)
			{	result = (ReplayEvent) object;
				if(VERBOSE)
					System.out.println("reading: "+result);
			}
		}
		catch (EOFException e) 
		{	//
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{	e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 * @throws ClassNotFoundException 
	 */
	public void finishReading() throws IOException, ClassNotFoundException
	{	if(VERBOSE)
			System.out.println("reading: stats");
		readRoundStats = (StatisticRound) in.readObject();
		in.close();
	}

	/////////////////////////////////////////////////////////////////
	// LEVEL				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String levelName;
	private String levelPack;
	
	public void setLevelName(String name)
	{	this.levelName = name;
	}
	public String getLevelName()
	{	return levelName;
	}
	
	public void setLevelPack(String levelPack)
	{	this.levelPack = levelPack;
	}
	public String getLevelPack()
	{	return levelPack;
	}
	
	/////////////////////////////////////////////////////////////////
	// DATE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Date saveDate;
	
	public void setSaveDate(Date save)
	{	this.saveDate = save;
	}
	public Date getSaveDate()
	{	return saveDate;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<String> players = new ArrayList<String>();
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	public List<String> getPlayers()
	{	return players;
	}
}
