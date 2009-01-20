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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticTournament;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.tools.CalculusTools;

public class CupTournament extends AbstractTournament
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
	
		// are players in random order ?
		if(randomizePlayers)
			randomizePlayers();
		
		// players distribution
		int playerCount = profiles.size();
		HashMap<Integer,ArrayList<ArrayList<Integer>>> distris = processPlayerDistribution(playerCount);
		int mx = Collections.max(distris.keySet());
		ArrayList<ArrayList<Integer>> distri = distris.get(mx);
		int index = (int)(Math.random()*distri.size());
		firstLegPlayersdistribution = distri.get(index);
//firstLegPlayersdistribution = distri.get(0);			
		
		// 
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
	private boolean randomizePlayers;
	private ArrayList<Integer> firstLegPlayersdistribution;

	public boolean isRandomizePlayers()
	{	return randomizePlayers;
	}

	public void setRandomizePlayers(boolean randomizePlayers)
	{	this.randomizePlayers = randomizePlayers;
	}

	private void randomizePlayers()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(profiles,random);
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameConstants.MAX_PROFILES_COUNT;i++)
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
	 * the method result is -1. If it is, it's the maximal rank
	 * of a non-player at the end of the tournament. This value
	 * allows ranking all the possible distributions in order
	 * to peak the best one (i.e. the one with lower value)
	 * @param distribution
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private int checkPlayerDistribution(ArrayList<Integer> distribution)
	{	int result = Integer.MAX_VALUE;
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		temp.add((ArrayList<Integer>)distribution.clone());
		HashMap<Integer,int[]> finalRanking = new HashMap<Integer, int[]>();
		
		// check compatibility with matches
		{	Iterator<CupLeg> itLeg = legs.iterator();
			while(itLeg.hasNext() && result>=0)
			{	CupLeg leg = itLeg.next();
				int legNumber = leg.getNumber();
//				int prevLeg = legNumber-1;
				ArrayList<Integer> list = new ArrayList<Integer>();
				temp.add(list);
				Iterator<CupPart> itPart = leg.getParts().iterator();
				while(itPart.hasNext() && result>=0)
				{	list.add(new Integer(0));
					CupPart part = itPart.next();
					int partNumber = part.getNumber();
					Set<Integer> matchAllowed = part.getMatch().getAllowedPlayerNumbers();
					int qualifiedCount = 0;
					ArrayList<CupPlayer> players = part.getPlayers();
					if(legNumber==0)
					{	int prevPart = partNumber;
						qualifiedCount = temp.get(legNumber).get(prevPart);
						if(!matchAllowed.contains(qualifiedCount))
							result = -1;
						else
						{	list.set(partNumber,qualifiedCount);
							//
							int partRank = part.getRank();
							if(partRank>0)
							{	int tp[] = {qualifiedCount,players.size()};
								finalRanking.put(partRank,tp);
							}
						}
					}
					else
					{	for(CupPlayer player: players)
						{	int prevPart = player.getPart();
							int prevInvolved = temp.get(legNumber).get(prevPart);
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
							{	int tp[] = {qualifiedCount,players.size()};
								finalRanking.put(partRank,tp);
							}
						}
					}
				}	
			}
		}
		
		// process the highest non-player ranking
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
}
