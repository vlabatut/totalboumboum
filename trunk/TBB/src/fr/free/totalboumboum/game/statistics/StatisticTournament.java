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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class StatisticTournament extends StatisticBase implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// STATISTIC MATCHES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<StatisticMatch> matches = new ArrayList<StatisticMatch>();
	
	public ArrayList<StatisticMatch> getStatisticMatches()
	{	return matches;
	}

	public void addStatisticMatch(StatisticMatch match)
	{	// matches stats
		matches.add(match);
		// scores
		for (Score score : Score.values())
		{	long[] currentScores = getScores(score);
			long[] matchScores = match.getScores(score);
			for(int i=0;i<matchScores.length;i++)
				currentScores[i] = currentScores[i] + matchScores[i];
		}
		// partial points
		float[] matchPoints = match.getPoints();
		for(int i=0;i<players.size();i++)
			total[i] = total[i] + matchPoints[i];
	}

	@Override
	public int getConfrontationCount()
	{	int result = matches.size();
		return result;
	}

//	TODO à réutiliser
//		Calendar cal = new GregorianCalendar();
//		date = cal.getTime();
	
	
	
	
	
	
	public void init(AbstractTournament tournament)
	{	// players
		ArrayList<Profile> profiles = tournament.getProfiles();
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
		for(StatisticRound r: matches)
			result = result + r.getTotalTime();
		return 0;
	}
}


