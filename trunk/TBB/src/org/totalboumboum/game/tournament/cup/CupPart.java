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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.tools.GameData;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class CupPart implements Serializable
{	private static final long serialVersionUID = 1L;

	public CupPart(CupLeg leg)
	{	this.leg = leg;
		ranks = new Ranks();
	}

	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int problematicTie = -1;
	
	public int getProblematicTie()
	{	return problematicTie;
	}
	
	public void init()
	{	// match
		currentMatch = match;
		// profiles
		List<Profile> profiles = new ArrayList<Profile>();
		for(int i=0;i<players.size();i++)
		{	Profile p = getProfileForIndex(i);
			if(p!=null)
			profiles.add(p);
		}
		currentMatch.init(profiles);
	}
	
	public void progress()
	{	currentMatch = tieBreak.initMatch();
	}
	
	public void finish()
	{	// misc
		leg = null;
		match = null;
		tieBreak = null;
		players.clear();
	}

	public void matchOver()
	{	// init the players rankings according to the match results
		if(ranks.isEmpty())
			ranks = match.getOrderedPlayers().copy();
		else
			updateRankings();
		
		// process the ranks needed for the coming leg (or final ranking)
		List<Integer> neededRanks = getNeededRanks();
		
		// identify the first tie conflicting with these needed ranks
		problematicTie = getProblematicTie(neededRanks);
		
		// try to break the tie with points
		while(problematicTie!=-1 && tieBreak.breakTie())
			problematicTie = getProblematicTie(neededRanks);

		if(problematicTie==-1)
			setOver(true);
	}

	private List<Integer> getNeededRanks()
	{	List<Integer> result = new ArrayList<Integer>();
		int nextLegNumber = leg.getNumber()+1;
		List<CupLeg> legs = getTournament().getLegs();
		
		// this was the last leg
		if(nextLegNumber>=legs.size())
		{	// this part ranked : all ranks count 
			if(rank!=-1)
			{	for(int i=1;i<=players.size();i++)
					result.add(i);
			}
			// and else: no rank counts
		}
		
		// there is another leg coming
		else
		{	CupLeg nextLeg = legs.get(nextLegNumber);
			for(CupPart part: nextLeg.getParts())
			{	for(CupPlayer player: part.getPlayers())
				{	if(player.getPrevPart()==number)
						result.add(player.getPrevRank());
				}
			}
		}
		
		// result
		Collections.sort(result);
		return result;
	}
	
	private void updateRankings()
	{	// process ranks
		Ranks matchRanks = currentMatch.getOrderedPlayers();
		List<Profile> tie = ranks.getProfilesFromRank(problematicTie);
		ranks.remove(problematicTie);
		
		// update rankings
		for(int i=0;i<tie.size();i++)
		{	Profile profile = tie.get(i);
			int relativeRank = matchRanks.getRankForProfile(profile);
			int newRank = problematicTie-1 + relativeRank;
			List<Profile> list = ranks.getProfilesFromRank(newRank);
			if(list==null)
				ranks.addProfile(newRank,profile);
			else
				list.add(profile);
		}
	}
	
	private int getProblematicTie(List<Integer> neededRanks)
	{	// keep only (meaninful) ties
		int result = -1;
		int i = 1;
		while(i<=GameData.CONTROL_COUNT && result<0)
		{	List<Profile> list = ranks.getProfilesFromRank(i);
			if(list!=null && list.size()>1)
			{	int j = 0;
				while(j<list.size() && result<0)
				{	int r = i+j;
					if(neededRanks.contains(r))
						result = i;
					else
						j++;
				}
			}
			if(result<0)
				i++;
		}
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANKS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Ranks ranks;
	
	public Ranks getOrderedPlayers()
	{	return ranks;
	}
	
	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean partOver = false;

	public boolean isOver()
	{	return partOver;
	}
	public void setOver(boolean partOver)
	{	this.partOver = partOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// LEG				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupLeg leg;
	
	public CupLeg getLeg()
	{	return leg;
	}
	
	public void setLeg(CupLeg leg)
	{	this.leg = leg;
	}
	
	public CupTournament getTournament()
	{	return leg.getTournament();		
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	private Match currentMatch;
	
	public void setMatch(Match match)
	{	this.match = match;
	}	
	
	public Match getMatch()
	{	return match;
	}
	
	public Match getCurrentMatch()
	{	return currentMatch;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIE BREAK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupTieBreak tieBreak;
	public void setTieBreak(CupTieBreak tieBreak)
	{	this.tieBreak = tieBreak;
	}	
	
	public CupTieBreak getTieBreak()
	{	return tieBreak;
	}
	
	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int number;
	
	public void setNumber(int number)
	{	this.number = number;
	}
	
	public int getNumber()
	{	return number;
	}

	/////////////////////////////////////////////////////////////////
	// RANK				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int rank = -1;

	public void setRank(int rank)
	{	this.rank = rank;
	}

	public int getRank()
	{	return rank;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public void setName(String name)
	{	this.name = name;
	}
	
	public String getName()
	{	return name;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<CupPlayer> players = new ArrayList<CupPlayer>();
	
	public List<CupPlayer> getPlayers()
	{	return players;
	}
	
	public CupPlayer getPlayer(int index)
	{	return players.get(index);
	}
	
	public void addPlayer(CupPlayer player)
	{	players.add(player);
	}
	
	public List<CupPlayer> getUsedPlayers()
	{	List<CupPlayer> result = new ArrayList<CupPlayer>();
		for(CupPlayer player: players)
		{	if(player.getUsed())
				result.add(player);			
		}
		return result;
	}
		
	/////////////////////////////////////////////////////////////////
	// PROFILES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	public CupPlayer getPlayerForProfile(Profile profile)
	{	CupPlayer result = null;
		int i = 0;
		while(i<players.size() && result==null)
		{	CupPlayer player = players.get(i);
			if(getProfileForIndex(i)==profile)
				result = player;
			else
				i++;
		}
		return result;
	}
	
	/**
	 * get the profile for the player whose index is indicated in parameter
	 */
	public Profile getProfileForIndex(int index)
	{	Profile result = null;
		CupPlayer player = players.get(index);
		int previousRank = player.getPrevRank();
		int previousPartNumber = player.getPrevPart();
		// not the first leg
		if(previousPartNumber>=0)
		{	CupLeg previousLeg = leg.getPreviousLeg();
			CupPart previousPart = previousLeg.getPart(previousPartNumber);
			Ranks previousRanks = previousPart.getOrderedPlayers();
			List<Profile> list = previousRanks.getProfilesFromRank(previousRank);
			if(list!=null && list.size()==1)
				result = list.get(0);
		}
		// first leg
		else
		{	List<Integer> firstLegPlayersdistribution = getTournament().getFirstLegPlayersdistribution();
			if(firstLegPlayersdistribution.get(number)>index)
			{	int count = 0;
				for(int i=0;i<number;i++)
				{	int legCount = firstLegPlayersdistribution.get(i);
					count = count + legCount;				
				}
				List<Profile> profiles = getTournament().getProfiles();
				result = profiles.get(count+index);
			}
		}

		return result;	
	}	
	
	/**
	 * process the next part for the player ranked at the indicated position
	 * @param rank
	 * @return
	 */
	public CupPart getNextPartForRank(int rank)
	{	CupPart result = null;
		CupLeg nextLeg = leg.getNextLeg();
	
		// there is another leg coming
		if(nextLeg!=null)
		{	Iterator<CupPart> itPart = nextLeg.getParts().iterator();
			while(itPart.hasNext() && result==null)
			{	CupPart part = itPart.next();
				Iterator<CupPlayer> itPlr = part.getPlayers().iterator();
				while(itPlr.hasNext() && result==null)
				{	CupPlayer player = itPlr.next();
					if(player.getPrevPart()==number && player.getPrevRank()==rank)
						result = part;
				}
			}
		}

		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int simulatedCount;
	
	public int getSimulatedCount()
	{	return simulatedCount;		
	}
	
	/** 
	 * simulate player progression in a part from first leg
	 * receiving a number qualified of players
	 */
	public boolean simulatePlayerProgression(int qualified)
	{	// init
		boolean result = true;
		Set<Integer> matchAllowed = match.getAllowedPlayerNumbers();
		int qualifiedAllowed = players.size();
		simulatedCount = 0;
		
		if(!matchAllowed.contains(qualified))
			result = false;
		else if(qualified>qualifiedAllowed)
			result = false;
		else
		{	// mark players
			for(int i=0;i<players.size();i++)
			{	CupPlayer player = players.get(i);
				if(i<qualified)
				{	player.setUsed(true);
					simulatedCount++;
					player.setSimulatedRank(simulatedCount);
				}
				else
				{	player.setUsed(false);
					player.setSimulatedRank(0);				
				}
			}
		}
		return result;
	}

	/** 
	 * simulate player progression in a part from a leg which is not the first one 
	 */
	public boolean simulatePlayerProgression()
	{	// init
		boolean result = true;
		Set<Integer> matchAllowed = match.getAllowedPlayerNumbers();
		
		simulatedCount = 0;
		CupLeg previousLeg = leg.getPreviousLeg();
		for(CupPlayer player: players)
		{	int prevPartNbr = player.getPrevPart();
			int prevRank = player.getPrevRank();
			CupPlayer prevPlayer = previousLeg.getPart(prevPartNbr).getPlayerSimulatedRank(prevRank);
			if(prevPlayer==null)
			{	player.setUsed(false);
				player.setSimulatedRank(0);				
			}
			else
			{	player.setUsed(true);
				simulatedCount++;
				player.setSimulatedRank(simulatedCount);
			}
		}
		
		if(!matchAllowed.contains(simulatedCount))
			result = false;

		return result;
	}
	
	/**
	 * returns the player corresponding to the specified local rank
	 * in the simulated cup (or null if no player has this rank in this part)
	 * @param rank
	 * @return
	 */
	public CupPlayer getPlayerSimulatedRank(int rank)
	{	CupPlayer result = null;
		Iterator<CupPlayer> it = players.iterator();
		while(result==null && it.hasNext())
		{	CupPlayer player = it.next();
			if(player.getSimulatedRank()==rank)
				result = player;
		}
		return result;
	}
	
	/**
	 * process the simulated final rank (ie rank for the whole cup)
	 * for each used players in this part
	 * @param localRank
	 * @param finalRank
	 * @return
	 */
	public boolean simulatePlayerFinalRank(int localRank, int finalRank)
	{	boolean result = false;
		
		CupPlayer player = getPlayerSimulatedRank(localRank);
		if(player!=null && player.getSimulatedFinalRank()==0)
		{	result = true;
			player.setSimulatedFinalRank(finalRank);
		}
		
		return result;
	}
	
	/**
	 * returns the final rank of the last used player
	 * or Integer.MAX_VALUE if all players are used  
	 * @return
	 */
	public int getSimulatedFinalRankMax()
	{	int result = 0;
		
		if(simulatedCount!=players.size())
		{	for(CupPlayer player: players)
			{	if(player.getUsed())
				{	int finalRank = player.getSimulatedFinalRank();
					if(finalRank>result)
						result = finalRank;
				}
			}
		}
		else
			result = Integer.MAX_VALUE;
		
		return result;
	}

	public int processPlayerFinalRank(int localRank, int finalRank)
	{	int result = 0;
		
		List<Profile> prfls = ranks.getProfilesFromRank(localRank);
		if(prfls!=null)
		{	// process each player with the specified local rank
			for(Profile profile: prfls)
			{	CupPlayer player = getPlayerForProfile(profile);
				// only if the player was not already ranked in a higher level match 
				if(player.getActualFinalRank()==0)
				{	CupPart nextPart = getNextPartForRank(localRank);
					// and only if the player has no other match coming
					if(nextPart==null)
					{	result ++;
						player.setActualFinalRank(finalRank);
					}
				}
			}
		}
		
		return result;
	}

	public void reinitPlayersActualFinalRanks()
	{	for(CupPlayer player: players)
			player.reinitActualFinalRank();
	}

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public String toString()
	{	String result = "";
		result = result + "-- part " + number + "\n";
		for(CupPlayer player: players)
			result = result + "  " + player + "\n";
		return result;
	}
}
