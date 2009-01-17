package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;

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
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		for(int i=0;i<players.size();i++)
		{	Profile p = getProfileForIndex(i);
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
		ArrayList<Integer> neededRanks = getNeededRanks();
		
		// identify the first tie conflicting with these needed ranks
		problematicTie = getProblematicTie(neededRanks);
		
		// try to break the tie with points
		while(problematicTie!=-1 && tieBreak.breakTie())
			problematicTie = getProblematicTie(neededRanks);

		if(problematicTie==-1)
			setOver(true);
	}

	private ArrayList<Integer> getNeededRanks()
	{	ArrayList<Integer> result = new ArrayList<Integer>();
		int nextLegNumber = leg.getNumber()+1;
		ArrayList<CupLeg> legs = getTournament().getLegs();
		
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
				{	if(player.getPart()==number)
						result.add(player.getRank());
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
		ArrayList<Profile> tie = ranks.getProfilesFromRank(problematicTie);
		ranks.remove(problematicTie);
		
		// update rankings
		for(int i=0;i<tie.size();i++)
		{	Profile profile = tie.get(i);
			int relativeRank = matchRanks.getRankFromProfile(profile);
			int newRank = problematicTie-1 + relativeRank;
			ArrayList<Profile> list = ranks.getProfilesFromRank(newRank);
			if(list==null)
				ranks.addProfile(newRank,profile);
			else
				list.add(profile);
		}
	}
	
	private int getProblematicTie(ArrayList<Integer> neededRanks)
	{	// keep only (meaninful) ties
		int result = -1;
		int i = 1;
		while(i<=GameConstants.CONTROL_COUNT && result<0)
		{	ArrayList<Profile> list = ranks.getProfilesFromRank(i);
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
	private final ArrayList<CupPlayer> players = new ArrayList<CupPlayer>();
	
	public ArrayList<CupPlayer> getPlayers()
	{	return players;
	}
	
	public void addPlayer(CupPlayer player)
	{	players.add(player);
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
		int previousRank = player.getRank();
		int previousPartNumber = player.getPart();
		// not the first leg
		if(previousPartNumber>=0)
		{	CupLeg previousLeg = leg.getPreviousLeg();
			CupPart previousPart = previousLeg.getPart(previousPartNumber);
			Ranks previousRanks = previousPart.getOrderedPlayers();
			ArrayList<Profile> list = previousRanks.getProfilesFromRank(previousRank);
			if(list!=null && list.size()==1)
				result = list.get(0);
		}
		// first leg
		else
		{	ArrayList<Integer> firstLegPlayersdistribution = getTournament().getFirstLegPlayersdistribution();
			int count = 0;
			for(int i=0;i<number;i++)
			{	int legCount = firstLegPlayersdistribution.get(i);
				count = count + legCount;				
			}
			ArrayList<Profile> profiles = getTournament().getProfiles();
			result = profiles.get(count+index);
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
					if(player.getPart()==number && player.getRank()==rank)
						result = part;
				}
			}
		}

		return result;
	}
}
