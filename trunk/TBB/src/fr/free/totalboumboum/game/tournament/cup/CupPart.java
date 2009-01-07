package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

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
	private final HashMap<Integer,ArrayList<Integer>> rankings = new HashMap<Integer, ArrayList<Integer>>();
	private int problematicTie = -1;
	
	public int getProblematicTie()
	{	return problematicTie;
	}
	
	public HashMap<Integer,ArrayList<Integer>> getRankings()
	{	return rankings;		
	}
	
	public void init()
	{	currentMatch = match;
		currentMatch.init(profiles);
	}
	
	public void progress()
	{	currentMatch = tieBreak.initMatch();
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
	{	// init the players rankings according to the match results
		initRankings();
		
		// process the ranks needed for the coming leg (or final ranking)
		ArrayList<Integer> neededRanks = getNeededRanks();		
		
		// identify the first tie conflicting with these needed ranks
		problematicTie = getProblematicTie(neededRanks);
		
		// try to break the tie with points
		while(tieBreak.breakTie())
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
	
	private void initRankings()
	{	// process ranks
		int[] ranks = match.getRanks();
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			ArrayList<Integer> list = rankings.get(rank);
			if(list==null)
				list = new ArrayList<Integer>();
			list.add(i);			
		}
	}
	
	private int getProblematicTie(ArrayList<Integer> neededRanks)
	{	// keep only (meaninful) ties
		int result = -1;
		int i = 1;
		while(i<=GameConstants.CONTROL_COUNT && result<0)
		{	ArrayList<Integer> list = rankings.get(i);
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
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public int[] getOrderedPlayers()
	{	int[] result = new int[profiles.size()];
		for(Entry<Integer,ArrayList<Integer>> i: rankings.entrySet())
		{	Integer r = i.getKey();
			ArrayList<Integer> list = i.getValue();
			for(Integer j: list)
				result[r-1] = j; //NOTE on suppose qu'il n'y a plus de tie, à ce niveau
		}
		return result;
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
	private final ArrayList<Profile> profiles = new ArrayList<Profile>();
	private final ArrayList<CupPlayer> players = new ArrayList<CupPlayer>();
	
	public ArrayList<CupPlayer> getPlayers()
	{	return players;
	}
	
	public void addPlayer(CupPlayer player)
	{	players.add(player);
	}
	
	public void addProfile(Profile profile)
	{	profiles.add(profile);
	}	
	
	public ArrayList<Profile> getProfiles()
	{	return profiles;	
	}
}
