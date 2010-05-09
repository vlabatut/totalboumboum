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
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.round.PlayMode;
import fr.free.totalboumboum.game.round.Round;

public class StatisticRound implements Serializable, StatisticBase
{
	private static final long serialVersionUID = 1L;

	private final ArrayList<String> players = new ArrayList<String>();
	private final ArrayList<StatisticEvent> events = new ArrayList<StatisticEvent>();
	private final HashMap<Score,long[]> scores = new HashMap<Score,long[]>();
	private PlayMode playMode;
	private float[] points;
	
	public void init(Round round)
	{	// players
		ArrayList<Profile> profiles = round.getProfiles();
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile temp = it.next();
			String name = temp.getName();
			players.add(name);
		}
		// play mode
		playMode = round.getPlayMode();
		// points
		points = new float[players.size()];
		for(int j=0;j<points.length;j++)
			points[j] = 0;
		// scores
		for (Score score : Score.values())
		{	long[] sc = new long[players.size()];
			for(int i=0;i<sc.length;i++)
				sc[i] = 0;
			scores.put(score,sc);
		}
	}
	
	public PlayMode getPlayMode()
	{	return playMode;	
	}
	
	public void addPlayer(String player)
	{	players.add(player);
	}
	public ArrayList<String> getPlayers()
	{	return players;
	}
	
	public void addEvent(StatisticEvent event)
	{	// events
		events.add(event);
		// scores
		Iterator<Score> i = scores.keySet().iterator();
		while(i.hasNext())
		{	Score temp = i.next();
			temp.process(this, event);
		}
	}

	public void computePoints(PointsProcessor pointProcessor)
	{	points = pointProcessor.process(this);
	}

	public long[] getScores(Score score)
	{	long[] result;
		result = scores.get(score);
		return result;	
	}

	public ArrayList<StatisticEvent> getEvents()
	{	return events;
	}
	
	/** renvoie les points calculés a posteriori pour ce round */
	public float[] getPoints()
	{	return points;
	}

	/** inutile ici (on renvoie zéro) */
	public float[] getPartialPoints()
	{	int size = players.size();
		float[] result = new float[size];
		for(int i=0;i<size;i++)
			result[i] = 0;
		return result; 
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
	
	private long totalTime = 0;
	
	public long getTime()
	{	return totalTime;	
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

	//useless...
	@Override
	public int getConfrontationCount()
	{	int result = 0;
		return result;
	}

	public void setWinner(int index)
	{	 //cf comment dans StatisticMatch
		
	}
}
