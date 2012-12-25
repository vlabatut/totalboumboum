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
 * Represents the settings of a match in a 
 * cup tournament. Several parts form a leg.
 * The tournament is made up of several legs.
 * 
 * @author Vincent Labatut
 */
public class CupPart implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard cup part.
	 * 
	 * @param leg
	 * 		The leg this part belongs to.
	 */
	public CupPart(CupLeg leg)
	{	this.leg = leg;
		ranks = new Ranks();
	}

	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if a tie break is necessary to finish this part */
	private int problematicTie = -1;
	
	/**
	 * Returns whether or not a tie break
	 * is needed to finish this part.
	 * 
	 * @return
	 * 		{@code true} iff a tie break is needed.
	 */
	public int getProblematicTie()
	{	return problematicTie;
	}
	
	/**
	 * Initializes this part.
	 */
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
	
	/**
	 * Advances through this part (which
	 * is likely to contain several rounds). 
	 */
	public void progress()
	{	currentMatch = tieBreak.initMatch();
	}
	
	/**
	 * Cleanly terminates this part.
	 */
	public void finish()
	{	// misc
		leg = null;
		match = null;
		tieBreak = null;
		players.clear();
	}

	/**
	 * Method called when the match corresponding
	 * to this part is over. 
	 */
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

	/**
	 * Returns a list of ranks associated
	 * to the players from this part.
	 * 
	 * @return
	 * 		A list of ranks.
	 */
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
	
	/**
	 * Updates the player ranks depending
	 * on the last results of this part.
	 */
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
	
	/**
	 * Returns the first tie needing
	 * to be broken.
	 * 
	 * @param neededRanks
	 * 		Current ranks.
	 * @return
	 * 		Problematic ties.
	 */
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
	/** Ranks of the players at the end of the tournament */
	private Ranks ranks;
	
	/**
	 * Returns the final ranks.
	 * 
	 * @return
	 * 		Final ranks at the end of the tournament.
	 */
	public Ranks getOrderedPlayers()
	{	return ranks;
	}
	
	/**
	 * Process the final ranks of the players
	 * involved in this part.
	 * 
	 * @param localRank
	 * 		Local rank of the player.
	 * @param finalRank
	 * 		Final rank of the player (overall).
	 * @return
	 * 		The number of players ranked in this match.
	 */
	public int processPlayerFinalRank(int localRank, int finalRank)
	{	int result = -1;
		
		List<Profile> prfls = ranks.getProfilesFromRank(localRank);
		if(prfls!=null)
		{	result = 0;
			// process each player with the specified local rank
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

	/**
	 * Resets the actual final ranks of each player.
	 */
	public void resetPlayersActualFinalRanks()
	{	for(CupPlayer player: players)
			player.resetActualFinalRank();
	}

	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if this part is over */ 
	private boolean partOver = false;

	/**
	 * Indicates if this part is over.
	 * 
	 * @return
	 * 		{@code true} iff this part is over.
	 */
	public boolean isOver()
	{	return partOver;
	}
	
	/**
	 * Changes the flag indicating if this part is over.
	 *  
	 * @param partOver
	 * 		New value for the flag indicating if this part is over. 
	 */
	public void setOver(boolean partOver)
	{	this.partOver = partOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// LEG				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Leg containing this part */
	private CupLeg leg;
	
	/**
	 * Returns the leg containing this part.
	 * 
	 * @return
	 * 		Leg containing this part.
	 */
	public CupLeg getLeg()
	{	return leg;
	}
	
	/**
	 * Changes the leg containing this part.
	 * 
	 * @param leg
	 * 		New leg containing this part.
	 */
	public void setLeg(CupLeg leg)
	{	this.leg = leg;
	}
	
	/**
	 * Returns the tournament containing this part.
	 * 
	 * @return
	 * 		The tournament containing this part.
	 */
	public CupTournament getTournament()
	{	return leg.getTournament();		
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Prototype of the match, kept as is and used for reset */
	private Match match;
	/** Match object, actually used when playing */
	private Match currentMatch;
	
	/**
	 * Changes the prototype match.
	 * 
	 * @param match
	 * 		New prototype match.
	 */
	public void setMatch(Match match)
	{	this.match = match;
	}	
	
	/**
	 * Returns the prototype match.
	 * 
	 * @return
	 * 		Prototype match.
	 */
	public Match getMatch()
	{	return match;
	}
	
	/**
	 * Returns the match currently
	 * played.
	 * 
	 * @return
	 * 		Currently played match.
	 */
	public Match getCurrentMatch()
	{	return currentMatch;
	}
	
	/////////////////////////////////////////////////////////////////
	// TIE BREAK		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tie breaker for this part */
	private CupTieBreak tieBreak;
	
	/**
	 * Changes the tie breaker.
	 * 
	 * @param tieBreak
	 * 		New tie breaker.
	 */
	public void setTieBreak(CupTieBreak tieBreak)
	{	this.tieBreak = tieBreak;
	}	
	
	/**
	 * Returns the tie breaker for this part.
	 * 
	 * @return
	 * 		Tie breaker of this part.
	 */
	public CupTieBreak getTieBreak()
	{	return tieBreak;
	}
	
	/////////////////////////////////////////////////////////////////
	// NUMBER			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number (position) of this part in the containing leg */ 
	private int number;
	
	/**
	 * Changes the number of this part in its leg.
	 * 
	 * @param number
	 * 		New position of this part.
	 */
	public void setNumber(int number)
	{	this.number = number;
	}
	
	/**
	 * Returns the number of this part in its leg.
	 * 
	 * @return
	 * 		Position of this part.
	 */
	public int getNumber()
	{	return number;
	}

	/////////////////////////////////////////////////////////////////
	// RANK				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Final rank of this part in the whole tournament */
	private int rank = -1;

	/**
	 * Changes the rank of this part in the tournament.
	 * 
	 * @param rank
	 * 		New rank.
	 */
	public void setRank(int rank)
	{	this.rank = rank;
	}

	/**
	 * Returns the rank of this part in the whole tournament.
	 * 
	 * @return
	 * 		Final rank of the part in the tournament.
	 */
	public int getRank()
	{	return rank;
	}

	/////////////////////////////////////////////////////////////////
	// NAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of this part */
	private String name;
	
	/**
	 * Changes the name of this part.
	 * 
	 * @param name
	 * 		New name of this part.
	 */
	public void setName(String name)
	{	this.name = name;
	}
	
	/**
	 * Returns the name of this part.
	 * 
	 * @return
	 * 		The name of this part.
	 */
	public String getName()
	{	return name;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Players involved in this part */
	private final List<CupPlayer> players = new ArrayList<CupPlayer>();
	
	/**
	 * Returns the players involved in this part.
	 *  
	 * @return
	 * 		Players involved in this part.
	 */
	public List<CupPlayer> getPlayers()
	{	return players;
	}
	
	/**
	 * Returns the player whose position is specified.
	 * 
	 * @param index
	 * 		Position of the required player.
	 * @return
	 * 		The corresponding player.
	 */
	public CupPlayer getPlayer(int index)
	{	return players.get(index);
	}
	
	/**
	 * Adds a new player to this part.
	 * 
	 * @param player
	 * 		New player.
	 */
	public void addPlayer(CupPlayer player)
	{	players.add(player);
	}
	
	/**
	 * Returns the list of players used during
	 * the last simulation.
	 * 
	 * @return
	 * 		List of used players.
	 */
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
	/**
	 * Returns the cup player object corresponding
	 * to the specified profile.
	 * 
	 * @param profile
	 * 		Profile of interest.
	 * @return
	 * 		The corresponding {@link CupPlayer} object.
	 */
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
	 * Returns the profile of the player whose position
	 * is specified as a parameter.
	 * 
	 * @param index
	 * 		Position of the player in this part.
	 * @return
	 * 		Corresponding profile.
	 */
	public Profile getProfileForIndex(int index)
	{	Profile result = null;
		CupPlayer player = players.get(index);
		int previousRank = player.getPrevRank();
		int previousPartNumber = player.getPrevPart();
		
		// not an entry player
		if(previousPartNumber>=0)
		{	CupLeg previousLeg = leg.getPreviousLeg();
			CupPart previousPart = previousLeg.getPart(previousPartNumber);
			Ranks previousRanks = previousPart.getOrderedPlayers();
			List<Profile> list = previousRanks.getProfilesFromRank(previousRank);
			if(list!=null && list.size()==1)
				result = list.get(0);
		}
		
		// entry player
		else
		{	// number of entry players in the previous parts
			int prevPlayers = getTournament().getEntryPlayerNumberBeforePart(this);
			// number of entry players in this part
			int thisPlayers = getTournament().getEntryPlayerNumberForPart(this);
			if(thisPlayers>index)
			{	// number of entry players before this index
				int entryBefore = 0;
				for(int i=0;i<index;i++)
				{	CupPlayer p = players.get(i);
					if(p.getPrevPart()==-1)
						entryBefore++;
				}
				// get the profile
				List<Profile> profiles = getTournament().getProfiles();
				int idx = prevPlayers + entryBefore;
				result = profiles.get(idx);
			}
		}

		return result;	
	}	
	
	/**
	 * Process the next part for the player 
	 * ranked at the indicated position.
	 * 
	 * @param rank
	 * 		Position of the player.
	 * @return
	 * 		The next part to be played by the player.
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
	// PLAYER DISTRIBUTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Checks if the match associated to this part
	 * includes players involved in their very
	 * first match of the tournament.
	 * 
	 * @return
	 * 		{@code true} iff there are new players
	 * 		involved in this part. 
	 */
	protected boolean isEntryMatch()
	{	boolean result = false;
		
		Iterator<CupPlayer> it = players.iterator();
		while(it.hasNext() && !result)
		{	CupPlayer player = it.next();
			result = player.getPrevLeg() == -1;
		}
		
		return result;
	}

//	/**
//	 * Counts the number of players
//	 * entering the tournament during
//	 * this part.
//	 * 
//	 * @return
//	 * 		Number of new players involved in this part.
//	 */
//	private int countNewPlayers()
//	{	int result = 0;
//		
//		for(CupPlayer player: players)
//		{	if(player.getPrevLeg() == -1)
//				result++;
//		}
//		
//		return result;
//	}
	
	/////////////////////////////////////////////////////////////////
	// SIMULATE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Number of players in this part actually used during the last simulation */
	private int simulatedCount;
	
	/**
	 * Returns the Number of players in this part 
	 * actually used during the last simulation.
	 * 
	 * @return
	 * 		Number of players actually used during the last simulation.
	 */
	public int getSimulatedCount()
	{	return simulatedCount;		
	}
	
	/** 
	 * Simulates player progression in a part.
	 * 
	 * @param distribution
	 * 		List of directly qualified players. Only used if
	 * 		the part is an entry one.
	 * @return
	 * 		{@code false} if there's a problem with the parameter.
	 */
	public boolean simulatePlayerProgression(List<Integer> distribution)
	{	// init
		boolean result = true;
		Set<Integer> matchAllowed = match.getAllowedPlayerNumbers();
		simulatedCount = 0;
		int qualified = 0; // total number of players (entry+qualified)
		
		// get the number of entry players
		int entryPlayers = 0;
		if(isEntryMatch() && !distribution.isEmpty())
		{	entryPlayers = distribution.get(0);
			distribution.remove(0);
		}
		
		// check each player depending on its previous leg (or absence of)
		for(CupPlayer player: players)
		{	int prevLegNbr = player.getPrevLeg();
			
			// entry player
			if(prevLegNbr==-1)
			{	// rejected (no slot left)
				if(entryPlayers==0)
				{	player.setUsed(false);
					player.setSimulatedRank(0);
				}
				// accepted
				else
				{	player.setUsed(true);
					simulatedCount++;
					player.setSimulatedRank(simulatedCount);
					qualified++;
					entryPlayers--;
				}
			}
			// qualified player
			else
			{	CupLeg previousLeg = getTournament().getLeg(prevLegNbr);
				int prevPartNbr = player.getPrevPart();
				CupPart previousPart = previousLeg.getPart(prevPartNbr);
				int prevRank = player.getPrevRank();
				CupPlayer prevPlayer = previousPart.getPlayerSimulatedRank(prevRank);
				// not qualified
				if(prevPlayer==null)
				{	player.setUsed(false);
					player.setSimulatedRank(0);
				}
				// qualified
				else
				{	player.setUsed(true);
					simulatedCount++;
					player.setSimulatedRank(simulatedCount);
					qualified++;
				}
			}
		}
		
		// total number of players (entry+qualified) must be consistant with the match specifications
		result = matchAllowed.contains(qualified) && entryPlayers==0;

		return result;
	}

	/**
	 * Returns the player corresponding to the specified local rank
	 * in the last simulation (or {@code null} if no player has this 
	 * rank in this part).
	 * 
	 * @param rank
	 * 		A local rank.
	 * @return
	 * 		The corresponding player.
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
	 * Changes the simulated final rank (i.e. for the 
	 * whole cup) for the specified player. This is
	 * possible only if a rank is associated to this part.
	 * 
	 * @param localRank
	 * 		Rank of the concerned player in this part. 
	 * @param finalRank
	 * 		Final rank of this player.
	 * @return
	 * 		{@code false} iff the local rank is not assigned.
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
	 * Returns the lowest final rank for the players used 
	 * in this part during the simulation, or {@link Integer#MAX_VALUE} 
	 * if all players were used.
	 *   
	 * @return
	 * 		Final rank of the last used player.
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

	/////////////////////////////////////////////////////////////////
	// STRING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public String toString()
	{	String result = "";
		result = result + "-- part " + number + "\n";
		for(CupPlayer player: players)
			result = result + "  " + player + "\n";
		return result;
	}
}
