package org.totalboumboum.game.tournament.cup;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import org.totalboumboum.tools.calculus.CombinatoricsTools;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class CupTournament extends AbstractTournament
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// players distribution
		int playerCount = profiles.size();
		HashMap<Integer,List<List<Integer>>> distris = processPlayerDistribution(playerCount);
		highestEmptyRank = Collections.max(distris.keySet());
		List<List<Integer>> distri = distris.get(highestEmptyRank);
		int index = (int)(Math.random()*distri.size());
		firstLegPlayersdistribution = distri.get(index);
		
		// sort players (as is, random or seeds)
		sortPlayers();
		
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
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
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupPlayerSort sortPlayers;
	private List<Integer> firstLegPlayersdistribution;
	private int highestEmptyRank;
	
	public CupPlayerSort getSortPlayers()
	{	return sortPlayers;
	}

	public void setSortPlayers(CupPlayerSort sortPlayers)
	{	this.sortPlayers = sortPlayers;
	}

	private void sortPlayers()
	{	if(sortPlayers==CupPlayerSort.RANDOM)
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
/*			
			// init
			List<List<Integer>> progression = new ArrayList<List<Integer>>(); // list of qualified players for each part
			List<List<List<Integer>>> indivualProgression = new ArrayList<List<List<Integer>>>(); // same thing except players are distinguished, and not just counted 
			HashMap<Integer,List<int[]>> finalRanking = new HashMap<Integer,List<int[]>>(); //final cup rankings
			
			// process players general progression (counts only, no details)
			simulatePlayerProgression(firstLegPlayersdistribution,progression,finalRanking);
			Integer generalRank = 1;
			
			// init lists and sublists
			for(List<Integer> partsList: progression)
			{	List<List<Integer>> legProg = new ArrayList<List<Integer>>();
				indivualProgression.add(legProg);
				for(Integer playerCount: partsList)
				{	List<Integer> partProg = new ArrayList<Integer>();
					legProg.add(partProg);
					for(int i=0;i<playerCount;i++)
						partProg.add(0);					
				}				
			}
			
			// affect final ranks
			{	// defined ranks
				// get the set of parts ranks (no matter if some ranks are missing or if one rank appears several time)
				List<Integer> tempList = new ArrayList<Integer>(finalRanking.keySet());
				// sort them
				Collections.sort(tempList);
				// process the parts corresponding to each rank
				for(int index: tempList)
				{	// get the parts for the considered rank
					List<int[]> tablist = finalRanking.get(index);
					// process them position by position (and not tab by tab)
					boolean done[]= new boolean[tablist.size()];
					Arrays.fill(done,false);
					boolean alldone = false;
					// idx is the current position
					int idx = 0;
					while(!alldone)
					{	int prevCount = generalRank;
						// for all tabs in tablist
						for(int tabid=0;tabid<tablist.size();tabid++)
						{	if(!done[tabid])
							{	int[] tp = tablist.get(tabid);
								// if enough players were qualified, the rank is set
								if(idx<tp[0])
								{	// set the players final ranks
									List<Integer> prog = indivualProgression.get(tp[2]).get(tp[3]);
									prog.set(idx,prevCount);
									generalRank++;
								}
								// else we stop for this part
								else
									done[tabid] = true;								
								//{qualifiedCount,players.size(),legNumber,partNumber};						
							}
						}
						// pass to the next position
						idx++;
						// update alldone
						alldone = true;
						for(int i=0;i<done.length;i++)
							alldone = alldone && done[i];
					}
				}
				
				// undefined ranks in last leg
				int lastLegNbr = indivualProgression.size()-1;
//				CupLeg lastLeg = legs.get(lastLegNbr);
				List<List<Integer>> partsList = indivualProgression.get(lastLegNbr);
				for(int i=0;i<partsList.size();i++)
				{	List<Integer> playersList = partsList.get(i);
//					CupPart part = lastLeg.getPart(i);
					if(part.getRank()==0)
					{	for(int j=0;j<playersList.size();j++)
						{	if(playersList.get(j)==0)
							{	playersList.set(j,generalRank);
								generalRank++;
							}
						}
					}
				}
			}
			
			
			// affect final ranks
			{	int prevLegRank = -1;
				// defined ranks
				int index = 0;
				while(index<finalRanking.size())
				{	int prevGeneralRank = generalRank;
					int[] tp = finalRanking.get(index);
					int partRank = tp[0];
					int playerCount = tp[1];
					int legIndex = tp[3];
					int partIndex = tp[4];
					List<List<Integer>> partsList = indivualProgression.get(legIndex);
					List<Integer> playersList = partsList.get(partIndex);
					for(int i=0;i<playerCount;i++)
					{	playersList.set(i,generalRank);
						generalRank++;
					}
					index++;
					tp = finalRanking.get(index);
				}
				// undefined ranks
				for(List<List<Integer>> partsList: indivualProgression)
				{	for(List<Integer> playersList: partsList)
					{	for(int i=0;i<playersList.size();i++)
						{	if(playersList.get(i)==0)
							{	playersList.set(i,generalRank);
								generalRank++;
							}
						}
					}
				}
			}
			
			// process detailed progression
			for(int i=indivualProgression.size()-1;i>0;i--)
			{	List<List<Integer>> partsList = indivualProgression.get(i);
				List<List<Integer>> previousPartsList = indivualProgression.get(i-1);
				CupLeg leg = legs.get(i);
				for(int j=0;j<partsList.size();j++)
				{	List<Integer> playersList = partsList.get(j);
					CupPart part = leg.getPart(j);
					int cupPlayerIndex = 0;
					for(int k=0;k<playersList.size();k++)
					{	Integer playerRank = playersList.get(k);
						CupPlayer player;
						do
						{	player = part.getPlayer(cupPlayerIndex);
							cupPlayerIndex++;
						}
						while(!player.getUsed());
						int previousPartId = player.getPart();
						int previousRank = player.getRank();
						List<Integer> previousPlayersList = previousPartsList.get(previousPartId);
						previousPlayersList.set(previousRank-1,playerRank);
					}
				}
			}
			
			// use first leg detailed progression to sort profiles list
			List<Profile> orderedProfile = new ArrayList<Profile>(profiles);
			Collections.sort(orderedProfile,new Comparator<Profile>()
			{	@Override
				public int compare(Profile o1, Profile o2)
				{	int id1 = o1.getId();
					int id2 = o2.getId();
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
			List<List<Integer>> firstLegList = indivualProgression.get(0);
			for(int i=0;i<firstLegList.size();i++)
			{	List<Integer> playersList = firstLegList.get(i);
				for(int j=0;j<playersList.size();j++)
				{	int index = playersList.get(j)-1;
					Profile profile = orderedProfile.get(index);
					profiles.add(profile);
				}
			}*/
			
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
	
	public List<Integer> getFirstLegPlayersdistribution()
	{	return firstLegPlayersdistribution;		
	}

	
	private HashMap<Integer,List<List<Integer>>> processPlayerDistribution(int playerCount)
	{	int matches = legs.get(0).getParts().size();

		// get the distributions
		List<List<Integer>> distributions = CombinatoricsTools.processDistributions(playerCount,matches);
		
		// permute them
		TreeSet<List<Integer>> permutations = new TreeSet<List<Integer>>(new Comparator<List<Integer>>()
		{	@Override
			public int compare(List<Integer> o1, List<Integer> o2)
			{	int result = 0;
				// size
				int size1 = o1.size();
				int size2 = o2.size();
				result = size1 - size2;
				// content
				int i=0;
				while(i<size1 && result==0)
				{	result = o1.get(i)-o2.get(i);
					i++;
				}
				return result;
			}				
		});
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
	 * check if the parameter players distribution is compatible 
	 * with the matches composing this tournament. If it's not,
	 * the method result is -1. If it is, it's the highest rank
	 * (at the end of the tournament) for which the player is missing. 
	 * This value allows ranking all the possible distributions in order
	 * to pick the best one (i.e. the one with the lowest value)
	 * @param distribution
	 * @return
	 */
	private int checkPlayerDistribution(List<Integer> distribution)
	{	// init
//		List<List<Integer>> progression = new ArrayList<List<Integer>>(); // list of qualified players for each part
//		HashMap<Integer,List<int[]>> finalRanking = new HashMap<Integer,List<int[]>>();

		// check compatibility with matches
		CupLeg firstLeg = legs.get(0);
		int result = profiles.size();
		boolean res = firstLeg.simulatePlayerProgression(distribution);
		if(res)
		{	simulatePlayerFinalRank();
			List<CupPart> parts = getAllParts();
			for(CupPart part: parts)
			{	int m = part.getSimulatedFinalRankMax();
				if(m<result)
					result = m;
			}
		}
		else
			result = -1;
		
/*		
		int result = simulatePlayerProgression(distribution,progression,finalRanking);
		
		// process the highest position for which a player is missing
		// (we want no hole in the final rankings, or at least we want it to be the lowest)
		if(result!=-1)
		{	int count = 1;
			// get the set of parts ranks (no matter if some ranks are missing or if one rank appears several time)
			List<Integer> tempList = new ArrayList<Integer>(finalRanking.keySet());
			// sort them
			Collections.sort(tempList);
			// process the parts corresponding to each rank
			Iterator<Integer> it = tempList.iterator();
			boolean goOn = it.hasNext();
			while(goOn)
			{	int index = it.next();
				// get the parts for the considered rank
				List<int[]> tablist = finalRanking.get(index);
				// process each one of them
				Iterator<int[]> it2 = tablist.iterator();
				while(it2.hasNext())
				{	int[] tp = it2.next();
					// add the number of qualified players
					count = count + tp[0];
					// if it is less than the number of accepted players : the main count is over for the remaining ranks
					if(tp[0]<tp[1])
						goOn = false;
					// but the count continue for the rest of this rank
				}
				// stop when all ranks are processed or when some player is missing
				goOn = goOn && it.hasNext();
			}
			result = count;
		}
*/		
		return result;
	}
	
	/**
	 * process the progression of players through the tournament legs.
	 * The parameter progression lists the different legs, each one being represented
	 * by a list of integers (orderered depending on the legs' 'number' field). 
	 * Each integer represents the number of qualified players  from the previous leg. 
	 * For the first leg, this is depends directly on the parameter
	 * distribution, which represents the initial distribution of players over matches.
	 * The finalRankings parameter is a hashmap whose key is the ranking number of the final
	 * parts (those leading to a final cup ranking) and value is an array containing
	 * three integer values : the number of qualified players for the corresponding part,
	 * the theoretical number of players this part could handle, and the id of this part in the final leg
	 *   
	 * @param distribution
	 * @param progression
	 * @param finalRanking
	 * @return
	 */
/*	private int simulatePlayerProgression(List<Integer> distribution, List<List<Integer>> progression, HashMap<Integer,List<int[]>> finalRanking)
	{	int result = profiles.size();
		
		Iterator<CupLeg> itLeg = legs.iterator();
		while(itLeg.hasNext() && result>=0)
		{	CupLeg leg = itLeg.next();
			int legNumber = leg.getNumber();
//			int prevLeg = legNumber-1;
			ArrayList<Integer> list = new ArrayList<Integer>(); //list of qualified players counts (one for each part)
			progression.add(list);
			Iterator<CupPart> itPart = leg.getParts().iterator();
			while(itPart.hasNext() && result>=0)
			{	list.add(new Integer(0));
				CupPart part = itPart.next();
				int partNumber = part.getNumber(); // id of the part in this leg
				Set<Integer> matchAllowed = part.getMatch().getAllowedPlayerNumbers(); //allowed numbers of players
				int qualifiedCount = 0;
				ArrayList<CupPlayer> players = part.getPlayers();
				
				// first leg only
				if(legNumber==0)
				{	int prevInvolved = distribution.get(partNumber); //use the initial distribution
					qualifiedCount = players.size();
					if(!matchAllowed.contains(prevInvolved))
						result = -1;
					else if(prevInvolved>qualifiedCount)
						result = -1;
					else
					{	list.set(partNumber,qualifiedCount);
						// mark players
						for(int i=0;i<players.size();i++)
							players.get(i).setUsed(i<qualifiedCount);
						// is the part final?
						int partRank = part.getRank();
						if(partRank>0)
						{	int tp[] = {qualifiedCount,players.size(),legNumber,partNumber};
							List<int[]> tempList = finalRanking.get(partRank);
							if(tempList==null)
							{	tempList = new ArrayList<int[]>();
								finalRanking.put(partRank,tempList);
							}	
							tempList.add(tp);
						}
					}
				}
				
				// other legs
				else
				{	for(CupPlayer player: players)
					{	int prevPart = player.getPart();
						int prevInvolved = progression.get(legNumber-1).get(prevPart);
						int prevRank = player.getRank();
						if(prevRank<=prevInvolved)
						{	qualifiedCount++;
							player.setUsed(true);
						}
						else
							player.setUsed(false);
					}
					if(!matchAllowed.contains(qualifiedCount))
						result = -1;					
					else
					{	list.set(partNumber,qualifiedCount);
						// is the part final?
						int partRank = part.getRank();
						if(partRank>0)
						{	int tp[] = {qualifiedCount,players.size(),legNumber,partNumber};
							List<int[]> tempList = finalRanking.get(partRank);
							if(tempList==null)
							{	tempList = new ArrayList<int[]>();
								finalRanking.put(partRank,tempList);
							}	
							tempList.add(tp);
						}
					}
				}
			}	
		}
		
		return result;
	}
*/
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
	
	
/*	
		ArrayList<Profile> ranked = new ArrayList<Profile>();
		CupLeg leg = legs.get(legs.size()-1);
		int partRank = 1;
		int countPlayers = 0;
		while(countPlayers<profiles.size())
		{	CupPart part = leg.getPartFromRank(partRank);
			// the part exists
			if(part!=null)
			{	int baseRank = countPlayers;
				Ranks orderedPlayers = part.getOrderedPlayers();
				for(Entry<Integer,ArrayList<Profile>> entry: orderedPlayers.getRanks().entrySet())
				{	ArrayList<Profile> list = entry.getValue();
					int r = entry.getKey()+baseRank;
					for(Profile p: list)
					{	result.addProfile(r,p);
						countPlayers ++;
						ranked.add(p);
					}
				}				
				partRank++;
			}
			// the part doesn't exist >> draw for all the remaining players
			else
			{	int r = countPlayers+1;
				for(Profile p: profiles)
				{	if(!ranked.contains(p))
					{	result.addProfile(r,p);
						countPlayers++;
					}
				}
			}				
		}
*/		
				
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
	private final List<CupLeg> legs = new ArrayList<CupLeg>();
	private int currentIndex;
	private CupLeg currentLeg;
	
	public List<CupLeg> getLegs()
	{	return legs;	
	}
	
	public CupLeg getLeg(int index)
	{	return legs.get(index);	
	}
	
	public void addLeg(CupLeg leg)
	{	legs.add(leg);	
	}
	
	public CupLeg getCurrentLeg()
	{	return currentLeg;	
	}

	/////////////////////////////////////////////////////////////////
	// PARTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int getTotalPartCount()
	{	int result = 0;
		for(CupLeg leg: legs)
			result = result + leg.getParts().size();
		return result;	
	}

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

	public void roundOver()
	{	panel.roundOver();
	}
}
