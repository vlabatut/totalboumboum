package org.totalboumboum.game.replay;

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
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.xml.sax.SAXException;

public abstract class OutputStream
{	private static final boolean VERBOSE = false;
		
	/////////////////////////////////////////////////////////////////
	// OUTPUT				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean filterEvents = true;
	private ObjectOutputStream out = null;
		
	/**
	 * records an event in the currently open stream.
	 */
	public void writeEvent(ReplayEvent event)
	{	try
		{	out.writeObject(event);
			if(VERBOSE)
				System.out.println("recording: "+event);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * close the replay output stream
	 */
	public void finishWriting(StatisticRound stats) throws IOException, ParserConfigurationException, SAXException
	{	// record the stats
		StopReplayEvent event = new StopReplayEvent();
		writeEvent(event);
		out.writeObject(stats);
		if(VERBOSE)
			System.out.println("recording: stats");
		// close the object stream
		out.close();
	}

	public void setFilterEvents(boolean flag)
	{	filterEvents = flag;		
	}
	
	public boolean getFilterEvents()
	{	return filterEvents;		
	}
	
	public void writeZoomCoef(double zoomCoef) throws IOException
	{	//Double zoomCoef = RoundVariables.zoomFactor;
		out.writeObject(zoomCoef);
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
