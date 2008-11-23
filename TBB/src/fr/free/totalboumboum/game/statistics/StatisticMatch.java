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

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsProcessor;

public class StatisticMatch extends StatisticBase implements Serializable
{
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// STATISTIC ROUNDS	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticRound> rounds = new ArrayList<StatisticRound>();

	public ArrayList<StatisticRound> getStatisticRounds()
	{	return rounds;
	}

	public void addStatisticRound(StatisticRound round)
	{	// round stats
		rounds.add(round);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = getScores(score);
			long[] roundScores = round.getScores(score);
			for(int i=0;i<roundScores.length;i++)
				currentScores[i] = currentScores[i] + roundScores[i];
		}
		// partial points
		float[] roundPoints = round.getPoints();
		for(int i=0;i<players.size();i++)
			total[i] = total[i] + roundPoints[i];
	}

	@Override
	public int getConfrontationCount()
	{	int result = rounds.size();
		return result;
	}
	
	
	
	
	public void init(Match match)
	{	// players
		ArrayList<Profile> profiles = match.getProfiles();
		Iterator<Profile> it = profiles.iterator();
		while(it.hasNext())
		{	Profile temp = it.next();
			String name = temp.getName();
			players.add(name);
		}
		// points
		points = new float[players.size()];
		for(int j=0;j<points.length;j++)
			points[j] = 0;
		// partial points
		total = new float[players.size()];
		for(int j=0;j<total.length;j++)
			total[j] = 0;
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
	
	
	@Override
	public long getTotalTime()
	{	long result = 0;
		for(StatisticRound r: rounds)
			result = result + r.getTotalTime();
		return 0;
	}
}