package fr.free.totalboumboum.game.tournament.cup;

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
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.GameData;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.statistics.GameStatistics;
import fr.free.totalboumboum.statistics.detailed.StatisticMatch;
import fr.free.totalboumboum.statistics.detailed.StatisticTournament;
import fr.free.totalboumboum.statistics.glicko2.jrs.RankingService;
import fr.free.totalboumboum.tools.CalculusTools;

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
		HashMap<Integer,ArrayList<ArrayList<Integer>>> distris = processPlayerDistribution(playerCount);
		highestEmptyRank = Collections.max(distris.keySet());
		ArrayList<ArrayList<Integer>> distri = distris.get(highestEmptyRank);
		int index = (int)(Math.random()*distri.size());
		firstLegPlayersdistribution = distri.get(index);
//firstLegPlayersdistribution = distri.get(0);			
		
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
	private ArrayList<Integer> firstLegPlayersdistribution;
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
		{	// init
			List<List<Integer>> progression = new ArrayList<List<Integer>>(); // list of qualified players for each part
			List<List<List<Integer>>> indivualProgression = new ArrayList<List<List<Integer>>>(); // same thing except players are distinguished, and not just counted 
			HashMap<Integer,int[]> finalRanking = new HashMap<Integer, int[]>(); //final cup rankings
			
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
			{	int indexLeg = progression.size()-1;
				List<List<Integer>> partsList = indivualProgression.get(indexLeg);
				// defined ranks
				int index = 1;
				int[] tp = finalRanking.get(index);
				while(tp!=null)
				{	int partIndex = tp[2];
					int playerCount = tp[0];
					List<Integer> playersList = partsList.get(partIndex);
					for(int i=0;i<playerCount;i++)
					{	playersList.set(i,generalRank);
						generalRank++;
					}
					index++;
					tp = finalRanking.get(index);
				}
				// undefined ranks
				for(List<Integer> playersList: partsList)
				{	for(int i=0;i<playersList.size();i++)
					{	if(playersList.get(i)==0)
						{	playersList.set(i,generalRank);
							generalRank++;
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
					for(int k=0;k<playersList.size();k++)
					{	Integer playerRank = playersList.get(k);
						CupPlayer player = part.getPlayer(k);
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
			}
		}
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameData.MAX_PROFILES_COUNT;i++)
		{	HashMap<Integer,ArrayList<ArrayList<Integer>>> distri = processPlayerDistribution(i);
			if(distri.size()>0)
				result.add(i);
		}
		return result;
	}
	
	public 	ArrayList<Integer> getFirstLegPlayersdistribution()
	{	return firstLegPlayersdistribution;		
	}

	
	private HashMap<Integer,ArrayList<ArrayList<Integer>>> processPlayerDistribution(int playerCount)
	{	int matches = legs.get(0).getParts().size();

		// get the distributions
		ArrayList<ArrayList<Integer>> distributions = CalculusTools.processDistributions(playerCount,matches);
		
		// permute them
		TreeSet<ArrayList<Integer>> permutations = new TreeSet<ArrayList<Integer>>(new Comparator<ArrayList<Integer>>()
		{	@Override
			public int compare(ArrayList<Integer> o1, ArrayList<Integer> o2)
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
		for(ArrayList<Integer> distrib: distributions)
		{	ArrayList<ArrayList<Integer>> perms = CalculusTools.processPermutations(distrib);
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
		HashMap<Integer,ArrayList<ArrayList<Integer>>> result = new HashMap<Integer,ArrayList<ArrayList<Integer>>>();
		for(ArrayList<Integer> list: permutations)
		{	int value = checkPlayerDistribution(list);
			if(value>-1)
			{	ArrayList<ArrayList<Integer>> l = result.get(value);
				if(l==null)
				{	l = new ArrayList<ArrayList<Integer>>();
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
	private int checkPlayerDistribution(ArrayList<Integer> distribution)
	{	// init
		List<List<Integer>> progression = new ArrayList<List<Integer>>(); // list of qualified players for each part
		HashMap<Integer,int[]> finalRanking = new HashMap<Integer, int[]>();
		
		// check compatibility with matches
		int result = simulatePlayerProgression(distribution,progression,finalRanking);
		
		// process the highest position for which a player is missing
		// (we want no hole in the final rankings, or at least we want it to be the downest)
		if(result!=-1)
		{	boolean goOn = true;
			int i=1;
			int count = 1;
			while(goOn)
			{	int[] tp = finalRanking.get(i);
				if(tp==null)
					goOn = false;
				else
				{	count = count + tp[0];
					if(tp[0]<tp[1])
					{	result = count;
						goOn = false;
					}
					i++;
				}
			}
		}
		
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
	private int simulatePlayerProgression(ArrayList<Integer> distribution, List<List<Integer>> progression, HashMap<Integer,int[]> finalRanking)
	{	int result = profiles.size();
		
		// check compatibility with matches
		{	Iterator<CupLeg> itLeg = legs.iterator();
			while(itLeg.hasNext() && result>=0)
			{	CupLeg leg = itLeg.next();
				int legNumber = leg.getNumber();
//				int prevLeg = legNumber-1;
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
					{	qualifiedCount = distribution.get(partNumber); //use the initial distribution
						if(!matchAllowed.contains(qualifiedCount))
							result = -1;
						else
						{	list.set(partNumber,qualifiedCount);
							//
							int partRank = part.getRank(); // is the part final?
							if(partRank>0)
							{	int tp[] = {qualifiedCount,players.size(),partNumber};
								finalRanking.put(partRank,tp);
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
								qualifiedCount++;						
						}
						if(!matchAllowed.contains(qualifiedCount))
							result = -1;					
						else
						{	list.set(partNumber,qualifiedCount);
							//
							int partRank = part.getRank();
							if(partRank>0)
							{	int tp[] = {qualifiedCount,players.size(),partNumber};
								finalRanking.put(partRank,tp);
							}
						}
					}
				}	
			}
		}
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();		
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
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// LEGS				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<CupLeg> legs = new ArrayList<CupLeg>();
	private int currentIndex;
	private CupLeg currentLeg;
	
	public ArrayList<CupLeg> getLegs()
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
		}
		else
		{	panel.matchOver();		
		}
	}

	public void roundOver()
	{	panel.roundOver();
	}
}
