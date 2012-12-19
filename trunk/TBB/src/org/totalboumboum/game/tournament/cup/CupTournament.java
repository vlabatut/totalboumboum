package org.totalboumboum.game.tournament.cup;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.totalboumboum.tools.computing.IntegerCollectionComparator;

/**
 * Class representing a cup tournament, i.e. with knock-out system.
 * The tournament contains several legs, e.g. quarter final, semi final, final.
 * Each leg contains several parts, e.g. semi final A, semi final B.
 * 
 * @author Vincent Labatut
 */
public class CupTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// players distribution
		initPlayerDistribution();
		
		// sort players (either: as is, random or seeds)
		sortPlayers();
		
		// NOTE check if the number of selected players fits
		currentIndex = 0;
		currentLeg = legs.get(currentIndex);
		currentLeg.init();
		
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(currentLeg.isOver())
		{	currentIndex++;
			currentLeg = legs.get(currentIndex);
			currentLeg.init();
		}
		else
			currentLeg.progress();
	}
	
	@Override
	public void finish()
	{	// legs
//		for(CupLeg leg:legs)
//			leg.finish();
		legs.clear();
		currentLeg = null;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYER ORDER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Way of sorting players before the tournament starts */
	private CupPlayerSort sortPlayers;
	
	/**
	 * Returns the way of sorting players before
	 * the tournament starts.
	 * 
	 * @return
	 * 		Player sorting method.
	 */
	public CupPlayerSort getSortPlayers()
	{	return sortPlayers;
	}

	/**
	 * Changes the method used to sort
	 * players before the begining of
	 * the tournament.
	 * 
	 * @param sortPlayers
	 * 		New sorting method.
	 */
	public void setSortPlayers(CupPlayerSort sortPlayers)
	{	this.sortPlayers = sortPlayers;
	}

	/**
	 * Order players depending on the currently selected sorting method.
	 */
	private void sortPlayers()
	{	if(sortPlayers==CupPlayerSort.NONE)
		{	// nothing special to do
		}
		
		else if(sortPlayers==CupPlayerSort.RANDOM)
		{	// just shuffle profiles
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(profiles,random);
		}
		
		else if(sortPlayers==CupPlayerSort.SEEDS)
		{	CupLeg firstLeg = legs.get(0);
			firstLeg.simulatePlayerProgression(firstLegPlayersdistribution);
			simulatePlayerFinalRank();
		
			// use first leg detailed progression to sort profiles list
			List<Profile> orderedProfile = new ArrayList<Profile>(profiles);
			Collections.sort(orderedProfile,new Comparator<Profile>()
			{	@Override
				public int compare(Profile o1, Profile o2)
				{	String id1 = o1.getId();
					String id2 = o2.getId();
					RankingService rankingService = GameStatistics.getRankingService();
					int r1 = rankingService.getPlayerRank(id1);
					if(r1<0)
						r1 = Integer.MAX_VALUE;
					int r2 = rankingService.getPlayerRank(id2);
					if(r2<0)
						r2 = Integer.MAX_VALUE;
					int result = r1-r2;
					return result;
				}
			});
			profiles.clear();
			for(CupPart part: firstLeg.getParts())
			{	for(CupPlayer player: part.getPlayers())
				{	if(player.getUsed())
					{	int index = player.getSimulatedFinalRank()-1;
//if(index==-1)
//	System.out.println();
						Profile profile = orderedProfile.get(index);
						profiles.add(profile);
					}
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER DISTRIBUTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Player distribution for the first leg */
	private List<Integer> firstLegPlayersdistribution;
	/** Highest empty rank for the above distribution */ 
	private int highestEmptyRank;
	
	/**
	 * Initializes the way players are distributed
	 * over entry matches.
	 */
	private void initPlayerDistribution()
	{	// number of players
		int playerCount = profiles.size();
		// get all possible distributions
		HashMap<Integer,List<List<Integer>>> distris = processPlayerDistribution(playerCount);
		// retrieve the best ones
		highestEmptyRank = Collections.max(distris.keySet());
		List<List<Integer>> distri = distris.get(highestEmptyRank);
		// randomly pick one of them
		int index = (int)(Math.random()*distri.size());
		firstLegPlayersdistribution = distri.get(index);
	}
	
	/**
	 * Returns the distribution of players for the first
	 * leg of this tournament.
	 * 
	 * @return
	 * 		How the players should be distributed over
	 * 		entry matches for this tournament.
	 */
	public List<Integer> getFirstLegPlayersdistribution()
	{	return firstLegPlayersdistribution;		
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameData.MAX_PROFILES_COUNT;i++)
		{	HashMap<Integer,List<List<Integer>>> distri = processPlayerDistribution(i);
			if(distri.size()>0)
				result.add(i);
		}
		
		return result;
	}
	
	/**
	 * Processes the player distribution for the specified number
	 * of players. In other words, in the case where there are less
	 * players than the maximum allowed for this tournament, this 
	 * method determines how the players should be distributed over
	 * all matches, so that the tournament can still takes place. 
	 * 
	 * @param playerCount
	 * 		Total number of players involved.
	 * @return
	 * 		A map containing all allowed distributions, the key being
	 * 		the highest empty rank. Several distributions might be associated
	 * 		to the same rank, hence the list of lists.
	 */
	private HashMap<Integer,List<List<Integer>>> processPlayerDistribution(int playerCount)
	{	// get the number of entry matches
		int matches = countEntryMatches();
		
		// process corresponding player distributions
		List<List<Integer>> distributions = CombinatoricsTools.processDistributions(playerCount,matches);
		
		// permute them
		TreeSet<List<Integer>> permutations = new TreeSet<List<Integer>>(new IntegerCollectionComparator());
/*		
System.out.println();
System.out.println("DISTRIBUTIONS");
for(ArrayList<Integer> list: distributions)
{	for(Integer i: list)
		System.out.print(i+" ");
	System.out.println();
}
*/		
		for(List<Integer> distrib: distributions)
		{	List<List<Integer>> perms = CombinatoricsTools.processPermutations(distrib);
/*		
System.out.println();
System.out.println("PERMUTATIONS");
for(ArrayList<Integer> list: perms)
{	for(Integer i: list)
		System.out.print(i+" ");
	System.out.println();
}
*/
			permutations.addAll(perms);
		}
	
/*		
System.out.println();
System.out.println("RESULTAT");
for(ArrayList<Integer> list: permutations)
{	for(Integer i: list)
		System.out.print(i+" ");
	System.out.println();
}
*/	
		// keep only the working distributions
		HashMap<Integer,List<List<Integer>>> result = new HashMap<Integer,List<List<Integer>>>();
		for(List<Integer> list: permutations)
		{	int value = checkPlayerDistribution(list);
			if(value>-1)
			{	List<List<Integer>> l = result.get(value);
				if(l==null)
				{	l = new ArrayList<List<Integer>>();
					result.put(value,l);					
				}
				l.add(list);			
			}
		}
		
/*		
		System.out.println();
		System.out.println("FILTRE");
		for(Entry<Integer,ArrayList<ArrayList<Integer>>> e: result.entrySet())
		{	System.out.println(">>"+e.getKey()+":");
			for(ArrayList<Integer> list: e.getValue())
			{	for(Integer i: list)
					System.out.print(i+" ");
				System.out.println();
			}
		}
*/		
		
		return result;
	}
	
	/**
	 * Counts the number of matches in this tournament
	 * which includes players involved in their very
	 * first match.
	 * 
	 * @return
	 * 		Number of matches in the tournament accepting
	 * 		new players. 
	 */
	private int countEntryMatches()
	{	int result = 0;
		
		for(CupLeg leg: legs)
			result = result + leg.countEntryMatches();
		
		return result;
	}
	
	/**
	 * Checks if the specified players distribution is compatible 
	 * with the matches composing this tournament. If it's not,
	 * the method result is -1. If it is, it's the highest rank
	 * (at the end of the tournament) for which the player is missing. 
	 * This value allows ranking all the possible distributions in order
	 * to pick the best one (i.e. the one with the lowest value).
	 * 
	 * @param distribution
	 * 		The distribution to be checked. Each value corresponds to 
	 * 		a number of players in one of the entry matches (i.e. matches
	 * 		involving at least one fresh player).
	 * @return
	 * 		The first final rank not assigned to any player. 
	 */
	private int checkPlayerDistribution(List<Integer> distribution)
	{	// init
		distribution = new ArrayList<Integer>(distribution);
//		List<List<Integer>> progression = new ArrayList<List<Integer>>(); // list of qualified players for each part
//		HashMap<Integer,List<int[]>> finalRanking = new HashMap<Integer,List<int[]>>();

		// check compatibility with matches
		CupLeg firstLeg = legs.get(0);
		int result = profiles.size();
		// first, try simulating players progression
		boolean res = firstLeg.simulatePlayerProgression(distribution);
		if(res)
		{	// if this works, then retrieve their final ranks
// TODO			
			simulatePlayerFinalRank();
			List<CupPart> parts = getAllParts();
			for(CupPart part: parts)
			{	int m = part.getSimulatedFinalRankMax();
				if(m<result)
					result = m;
			}
		}
		else
			result = -1;
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();	
		
		// reinit ranks
		for(CupLeg leg: legs)
			leg.reinitPlayersActualFinalRanks();
	
		// update ranks
		List<CupPart> remainingParts = getAllParts();
		int totalPartCount = remainingParts.size();
		int partRank = 1;
		int playerRank = 1;
		while(!remainingParts.isEmpty())
		{	// init list of parts with this rank
			List<CupPart> templist = new ArrayList<CupPart>();
			Iterator<CupPart> it = remainingParts.iterator();
			while(it.hasNext())
			{	CupPart part = it.next();
				if(part.getRank()==partRank)
				{	templist.add(part);
					it.remove();
				}
			}
			// process players ranks
			int count;
			int localRank = 1;
			do
			{	count = 0;
				it = templist.iterator();
				while(it.hasNext())
				{	CupPart part = it.next();
					int cnt = part.processPlayerFinalRank(localRank,playerRank);
					count = count + cnt;
				}
				playerRank = playerRank + count;
				localRank++;
			}
			while(count>0);
			
			partRank++;
			if(partRank>totalPartCount)
				partRank = 0;
		}
	
		// use first leg rankings to build final ranks
		List<CupPlayer> allPlayers = legs.get(0).getAllUsedPlayers();
		for(int i=0;i<allPlayers.size();i++)
		{	CupPlayer player = allPlayers.get(i);
			int rank = player.getActualFinalRank();
			Profile profile = profiles.get(i);
			result.addProfile(rank, profile);
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void simulatePlayerFinalRank()
	{	List<CupPart> remainingParts = getAllParts();
		int totalPartCount = remainingParts.size();
		int partRank = 1;
		int playerRank = 1;
		while(!remainingParts.isEmpty())
		{	// init list of parts with this rank
			List<CupPart> templist = new ArrayList<CupPart>();
			Iterator<CupPart> it = remainingParts.iterator();
			while(it.hasNext())
			{	CupPart part = it.next();
				if(part.getRank()==partRank)
				{	templist.add(part);
					it.remove();
				}
			}
			// process players ranks
			int count;
			int localRank = 1;
			do
			{	count = 0;
				it = templist.iterator();
				while(it.hasNext())
				{	CupPart part = it.next();
					boolean over = !part.simulatePlayerFinalRank(localRank,playerRank);
					if(over)
						it.remove();
					else
						count ++;
				}
				playerRank = playerRank + count;
				localRank++;
			}
			while(count>0);
			
			partRank++;
			if(partRank>totalPartCount)
				partRank = 0;
		}
	}

	/////////////////////////////////////////////////////////////////
	// LEGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The legs composing this cup tournament */
	private final List<CupLeg> legs = new ArrayList<CupLeg>();
	/** Index of the current leg */
	private int currentIndex;
	/** Object representing the current leg */
	private CupLeg currentLeg;
	
	/**
	 * Returns the list of legs for
	 * this tournament.
	 * 
	 * @return
	 * 		List of legs.
	 */
	public List<CupLeg> getLegs()
	{	return legs;	
	}
	
	/**
	 * Returns the leg whose index
	 * is specified.
	 * 
	 * @param index
	 * 		Index of the required leg.
	 * @return
	 * 		The required leg.
	 */
	public CupLeg getLeg(int index)
	{	return legs.get(index);	
	}
	
	/**
	 * Adds a new leg to this tournament.
	 * 
	 * @param leg
	 * 		The leg to add to this tournament.
	 */
	public void addLeg(CupLeg leg)
	{	legs.add(leg);	
	}
	
	/**
	 * Returns the current leg
	 * (the tournament must have begun).
	 * 
	 * @return
	 * 		The current leg.
	 */
	public CupLeg getCurrentLeg()
	{	return currentLeg;	
	}

	/////////////////////////////////////////////////////////////////
	// PARTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the total number of parts (over all legs)
	 * in this tournament.
	 * 
	 * @return
	 * 		The number of parts in this tournament.
	 */
	public int getTotalPartCount()
	{	int result = 0;
		for(CupLeg leg: legs)
			result = result + leg.getParts().size();
		return result;	
	}

	/**
	 * Returns the list of all parts
	 * in this tournament.
	 * 
	 * @return
	 * 		A list of parts.
	 */
	public List<CupPart> getAllParts()
	{	List<CupPart> result = new ArrayList<CupPart>();
		for(CupLeg leg: legs)
			result.addAll(leg.getParts());
		return result;	
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Match getCurrentMatch()
	{	CupPart currentPart = currentLeg.getCurrentPart();
		Match match = currentPart.getCurrentMatch();
		return match;
	}

	@Override
	public void matchOver()
	{	// stats
		Match currentMatch = getCurrentMatch();
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		currentLeg.matchOver();
		if(currentLeg.isOver() && currentIndex==legs.size()-1)
		{	setOver(true);
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
}
