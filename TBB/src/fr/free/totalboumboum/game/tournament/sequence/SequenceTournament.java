package fr.free.totalboumboum.game.tournament.sequence;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.TournamentLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.statistics.raw.StatisticMatch;
import fr.free.totalboumboum.game.statistics.raw.StatisticTournament;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class SequenceTournament extends AbstractTournament
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<TournamentLimit> limits;

	public Limits<TournamentLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<TournamentLimit> limits)
	{	this.limits = limits;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
		
		// are matches in random order ?
		if(randomizeMatches)
			randomizeMatches();
		
		// NOTE v�rifier si le nombre de joueurs s�lectionn�s correspond
		currentIndex = 0;
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	Match match = matches.get(currentIndex);
			currentIndex++;
			currentMatch = match.copy();
			currentMatch.init(profiles);
		}
	}

	@Override
	public void finish()
	{	
		// limits
		limits.finish();
		limits = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCHES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean randomizeMatches;
	private ArrayList<Match> matches = new ArrayList<Match>();
	private Match currentMatch;
	private int currentIndex;

	public boolean getRandomizeMatches()
	{	return randomizeMatches;
	}
	public void setRandomizeMatches(boolean randomOrder)
	{	this.randomizeMatches = randomOrder;
	}

	public void addMatch(Match match)
	{	matches.add(match);
	}

	private void randomizeMatches()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(matches,random);
	}
	
	@Override
	public Match getCurrentMatch()
	{	return currentMatch;	
	}
	
	@Override
	public void matchOver()
	{	// stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		// iterator
		if(currentIndex>=matches.size())
		{	if(randomizeMatches)
				randomizeMatches();
			currentIndex = 0;
		}
		// limits
		if(getLimits().testLimit(this))
		{	float[] points = limits.processPoints(this);
			stats.setPoints(points);
			setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
		}
		else
		{	panel.matchOver();		
		}
	}

	public void roundOver()
	{	panel.roundOver();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Match m:matches)
		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
			result.retainAll(temp);			
		}
		return result;			
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private int[] getRanks(float[] pts)
	{	int[] result = new int[getProfiles().size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;

		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(pts[i]<pts[j])
					result[i] = result[i] + 1;
				else if(pts[i]>pts[j])
					result[j] = result[j] + 1;
			}
		}	

		return result;
	}
	
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = getRanks(points);
			ranks2 = getRanks(total);
		}
		else
		{	ranks = getRanks(total);
			ranks2 = new int[ranks.length];
			Arrays.fill(ranks2,0);
		}
		// result
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			int rank2 = ranks2[i];
			Profile profile = getProfiles().get(i);
			ArrayList<Profile> list = result.getProfilesFromRank(rank);
			int index = -1;
			// if no list yet : regular insertion
			if(list==null)
			{	result.addProfile(rank,profile);
				index = 0;
			}
			// if list : insert at right place considering total points
			else
			{	int j = 0;
				while(j<list.size() && index==-1)
				{	Profile profileB = list.get(j);
					int plrIdx = getProfiles().indexOf(profileB);
					int rank2B = ranks2[plrIdx];
					if(rank2<rank2B)
						index = j;
					else
						j++;
				}				
				if(index==-1)
					index = j;
				list.add(index,profile);
			}			
		}
			
		return result;
	}
}
