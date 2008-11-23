package fr.free.totalboumboum.game.statistics;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.game.round.Round;

public class StatisticRound extends StatisticBase implements Serializable
{
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticEvent> events = new ArrayList<StatisticEvent>();
	
	public ArrayList<StatisticEvent> getStatisticEvents()
	{	return events;
	}

	public void addStatisticEvent(StatisticEvent event)
	{	// events
		events.add(event);
		// scores
		Iterator<Score> i = scores.keySet().iterator();
		while(i.hasNext())
		{	Score temp = i.next();
			temp.process(this, event);
		}
	}

	@Override
	public int getConfrontationCount()
	{	// useless for round
		int result = 0;
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// STATISTIC EVENTS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	
	
	
	public void init(Round round)
	{	totalTime = 0;
		// players
		ArrayList<Profile> profiles = round.getProfiles();
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile temp = it.next();
			String name = temp.getName();
			players.add(name);
		}
		// points
		points = new float[players.size()];
		Arrays.fill(points,0);
		// partial points
		total = new float[players.size()];
		Arrays.fill(total,0);
		// scores
		for (Score score : Score.values())
		{	long[] sc = new long[players.size()];
			for(int i=0;i<sc.length;i++)
				sc[i] = 0;
			scores.put(score,sc);
		}
	}

	public void computePoints(PointsProcessor pointProcessor)
	{	points = pointProcessor.process(this);
	}



	public TreeSet<PlayerPoints> getOrderedPlayers()
	{	TreeSet<PlayerPoints> result = new TreeSet<PlayerPoints>();
		for(int i=0;i<points.length;i++)
		{	PlayerPoints pp = new PlayerPoints(players.get(i),i);
			pp.addPoint(points[i]);
			result.add(pp);
		}
		return result;
	}
	public ArrayList<PlayerPoints> getWinners()
	{	ArrayList<PlayerPoints> result = new ArrayList<PlayerPoints>();
		TreeSet<PlayerPoints> orderedPlayers = getOrderedPlayers();
		Iterator<PlayerPoints> i = orderedPlayers.descendingIterator();
		PlayerPoints first = orderedPlayers.last();
		boolean goOn = true;
		while(i.hasNext() && goOn)	
		{	PlayerPoints temp = i.next();
			if(temp.equalsPoints(first))
				result.add(temp);
			else
				goOn = false;
		}
		return result;
	}
		
	public void updateTime(long time)
	{	totalTime = time;
		long[] sc = scores.get(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(sc[i]>=0)
				sc[i] = time;
		}
	}
	public void finish(long time)
	{	updateTime(time+1);
		long[] sc = scores.get(Score.TIME);
		for(int i=0;i<sc.length;i++)
		{	if(sc[i]<0)
				sc[i] = -sc[i];
		}
	}

}
