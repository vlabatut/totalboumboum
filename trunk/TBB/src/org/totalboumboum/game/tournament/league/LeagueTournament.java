package org.totalboumboum.game.tournament.league;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.PointsProcessor;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.CombinatoricsTools;

/**
 * This class represents a tournament in which
 * all players must play against each other at
 * some point, possibly several time. It is
 * based on the league model used in most sports.
 * 
 * @author Vincent Labatut
 */
public class LeagueTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// players
		if(randomizePlayers)
			Collections.shuffle(profiles);
	
		// matches
		initMatches();
		
		// stats
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	// confrontations
			Set<Integer> players = confrontations.get(matchCount);
			matchCount++;
			List<Profile> prof = new ArrayList<Profile>();
			for(Integer idx: players)
			{	Profile profile = profiles.get(idx);
				prof.add(profile);
			}
			
			// match
			Match match = matches.get(currentIndex);
			currentIndex++;
			currentMatch = match.copy();
			currentMatch.init(prof);
			playedMatches.add(currentMatch);
		}
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
		if(matchCount==confrontations.size()-1)
		{	float[] points;
			if(pointsProcessor!=null)
				points = pointsProcessor.process(this);
			else
				points = stats.getTotal();
			stats.setPoints(points);
			setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
			
			// server connection
			ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
			if(serverConnection!=null)
				serverConnection.updateHostState(HostState.FINISHED);
		}
		else
		{	panel.matchOver();		
		}
	}
	
	@Override
	public void roundOver()
	{	panel.roundOver();
	}

	@Override
	public void finish()
	{	// points
//		pointsProcessor = null;
	}

	/////////////////////////////////////////////////////////////////
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Used to process points for this tournament */
	private PointsProcessor pointsProcessor;
		
	/**
	 * Returns the point processor of this tournament.
	 * 
	 * @return
	 * 		Point processor of this tournament.
	 */
	public PointsProcessor getPointsProcessor()
	{	return pointsProcessor;
	}

	/**
	 * Changes the point processor of this tournament.
	 * 
	 * @param pointsProcessor
	 * 		New point processor of this tournament.
	 */
	public void setPointsProcessor(PointsProcessor pointsProcessor)
	{	this.pointsProcessor = pointsProcessor;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the players should be used in a random order */ 
	private boolean randomizePlayers;
	
	/**
	 * Indicates if the player order
	 * should be random or not.
	 * 
	 * @return
	 * 		{@code true} iff players should be used in a random order.
	 */
	public boolean getRandomizePlayers()
	{	return randomizePlayers;
	}

	/**
	 * Changes the flag indicating if the player order
	 * should be random or not.
	 * 
	 * @param randomizePlayers
	 * 		If {@code true}, players should be used in a random order.
	 */
	public void setRandomizePlayers(boolean randomizePlayers)
	{	this.randomizePlayers = randomizePlayers;
	}

	/**
	 * Returns the allowed number of players
	 * for the matches of this tournament.
	 * 
	 * @return
	 * 		Set of allowed number of players.
	 */
	public Set<Integer> getMatchesAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		for(int i=2;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Match m:matches)
		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
			result.retainAll(temp);			
		}
		return result;			
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = getMatchesAllowedPlayerNumbers();
		int min = Collections.min(result);
		min = Math.max(min,2);
		result = new TreeSet<Integer>();
		for(int i=min;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		return result;			
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the ranks for this tournament.
	 * 
	 * @param pts
	 * 		Points scored by the players.
	 * @return
	 * 		Corresponding player ranks.
	 */
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

	@Override
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
			List<Profile> list = result.getProfilesFromRank(rank);
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

	/////////////////////////////////////////////////////////////////
	// MATCH ORDER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the matches should be played in a random order */ 
	private boolean randomizeMatches;

	/**
	 * Indicates if the match order
	 * should be random or not.
	 * 
	 * @return
	 * 		{@code true} iff matches should be played in a random order.
	 */
	public boolean getRandomizeMatches()
	{	return randomizeMatches;
	}
	
	/**
	 * Changes the flag indicating if the match order
	 * should be random or not.
	 * 
	 * @param randomizeMatches
	 * 		If {@code true}, matches should be played in a random order.
	 */
	public void setRandomizeMatches(boolean randomizeMatches)
	{	this.randomizeMatches = randomizeMatches;
	}

	/**
	 * Set matches in a random order.
	 */
	private void randomizeMatches()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(matches,random);
	}

	/////////////////////////////////////////////////////////////////
	// MATCHES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Prototype matches for this tournament */ 
	private List<Match> matches = new ArrayList<Match>();
	/** Number of match played until now */
	private int matchCount;

	/**
	 * Initializes the matches of this tournament.
	 */
	private void initMatches()
	{	// matches
		if(randomizeMatches)
			randomizeMatches();
		currentIndex = 0;
		playedMatches.clear();
		
		// confrontations
		int n = profiles.size();
		List<Integer> ks = new ArrayList<Integer>(getMatchesAllowedPlayerNumbers());
		confrontations = null;
		// try to minimize the number of matches
		if(minimizeConfrontations)
		{	for(Integer k: ks)
			{	List<Set<Integer>> conf = new ArrayList<Set<Integer>>(CombinatoricsTools.processCombinationsRec/*processMinimalCombinations*/(n,k));
				if(confrontations==null || conf.size()<confrontations.size())
					confrontations = conf;
			}			
		}
		// or choose all possible combinations (might be quite long)
		else
		{	for(Integer k: ks)
			{	List<Set<Integer>> conf = new ArrayList<Set<Integer>>(CombinatoricsTools.processCombinationsRec(n,k));
				if(confrontations==null || conf.size()<confrontations.size())
					confrontations = conf;
			}
		}
		if(confrontationOrder==ConfrontationOrder.RANDOM)
			randomizeConfrontations();
		else if(confrontationOrder==ConfrontationOrder.HOMOGENEOUS)
			homogenizeConfrontations();
		else if(confrontationOrder==ConfrontationOrder.HETEROGENEOUS)
			heterogenizeConfrontations();
		matchCount = 0;
	}

	/**
	 * Adds a match to this tournament.
	 * 
	 * @param match
	 * 		The additional match.
	 */
	public void addMatch(Match match)
	{	matches.add(match);
	}

	/////////////////////////////////////////////////////////////////
	// CONFRONTATIONS	/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** How player confrontations affect match order */
	private ConfrontationOrder confrontationOrder;
	/** Whether we should try to minimize the number of times two players meet */
	private boolean minimizeConfrontations;
	/** List of confrontations */
	private List<Set<Integer>> confrontations;

	/**
	 * Returns the way confrontations
	 * affect match order.
	 * 
	 * @return
	 * 		Confrontation ordering type.
	 */
	public ConfrontationOrder getConfrontationOrder()
	{	return confrontationOrder;
	}
	
	/**
	 * Changes the way confrontations
	 * affect match order.
	 * 
	 * @param confrontationOrder
	 * 		New confrontation ordering type.
	 */
	public void setConfrontationOrder(ConfrontationOrder confrontationOrder)
	{	this.confrontationOrder = confrontationOrder;
	}

	/**
	 * Indicates whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @return
	 * 		{@code true} iff the minization flag is on.
	 */
	public boolean getMinimizeConfrontations()
	{	return minimizeConfrontations;
	}
	
	/**
	 * Changes the flag indicating whether we should try to minimize 
	 * the number of times two players meet.
	 * 
	 * @param minimizeConfrontations
	 * 		New minimization flag value.
	 */
	public void setMinimizeConfrontations(boolean minimizeConfrontations)
	{	this.minimizeConfrontations = minimizeConfrontations;
	}

	/**
	 * Makes confrontations randomly distributed
	 * (consecutive match may or may not involve
	 * the same players).
	 */
	private void randomizeConfrontations()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(confrontations,random);
	}

	/**
	 * Makes confrontations distributed
	 * in an heterogeneous way (consecutive
	 * matches do not involve the same players).
	 */
	private void heterogenizeConfrontations()
	{	// TODO		
	}
	
	/**
	 * Makes confrontations distributed
	 * in an homogeneous way (consecutive
	 * matches involve roughly the same players).
	 */
	private void homogenizeConfrontations()
	{	// TODO		
	}
	
	/**
	 * Represents the order of the confrontations.
	 * 
	 * @author Vincent Labatut
	 */
	public enum ConfrontationOrder
	{	/** Keeps the order as defined by the designer */
		AS_IS,
		/** Randomizes the order */
		RANDOM,
		/** (Tries to) distribute confrontations homogeneously over matches */
		HOMOGENEOUS,
		/** (Tries to) distribute confrontations heterogeneously over matches */
		HETEROGENEOUS;
	}
}
