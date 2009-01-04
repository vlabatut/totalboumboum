package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;

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
	}

	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void init()
	{	initProfiles();
		currentMatch = match;
		currentMatch.init(profiles);
	}
	
	public void progress()
	{	currentMatch = tieBreak.getMatch();
//TODO only tied profiles, not necesserally all of them		
		currentMatch.init(profiles);
	}
	
	public void finish()
	{	// misc
		leg = null;
		match = null;
		tieBreak = null;
		players.clear();
	}

	public void matchOver()
	{	if(currentMatch==match)
		{	if(noTie())
				setOver(true);
		}
		else
			setOver(true);
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
	// RANKING			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int rank;

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
	private ArrayList<Profile> profiles;
	private final ArrayList<CupPlayer> players = new ArrayList<CupPlayer>();
	private Set<Integer> allowedPlayerNumbers = null;
	
	public ArrayList<CupPlayer> getPlayers()
	{	return players;
	}
	
	public void addPlayer(CupPlayer player)
	{	players.add(player);
	}
	
	private void initProfiles()
	{	profiles = new ArrayList<Profile>();
		int previousLegIndex = leg.getNumber()-1;
		if(previousLegIndex<0)
		{
			
		}
		else
		{	CupLeg previousLeg = getTournament().getLeg(previousLegIndex);
			for(CupPlayer p: players)
			{	int previousPartIndex = p.getPart();
				CupPart previousPart = previousLeg.getPart(previousPartIndex);
				int playerRank = p.getRank();
				Profile profile = previousPart.getRankedPlayer(playerRank);
				profiles.add(profile);
			}
		}		
	}
	
	public boolean isPlayerNeeded(int number)
	{	boolean result;
		if(allowedPlayerNumbers==null)
			getAllowedPlayerNumbers();
		
		
		return result;
	}
	
	public Set<Integer> getAllowedPlayerNumbers()
	{	if(allowedPlayerNumbers==null)
		{	allowedPlayerNumbers = new TreeSet<Integer>();
			int legNumber = leg.getNumber();
			int legCount = getTournament().getLegs().size();
			// last leg
			if(legNumber==legCount)
			{	Set<Integer> temp = match.getAllowedPlayerNumbers();
				int max = players.size();
				for(int i=GameConstants.MAX_PROFILES_COUNT;i>max;i--)
					temp.remove(i);				
			}
			// other leg
			else
			{	
				
			}
		
		}
		return allowedPlayerNumbers;
	
	
	
	
		TreeSet<Integer> result = new TreeSet<Integer>();
		for(int i=1;i<=GameConstants.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Match m:matches)
		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
			result.retainAll(temp);			
		}
		return result;			
	}

}
