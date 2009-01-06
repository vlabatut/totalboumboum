package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsRankings;

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


public class CupTieBreak implements Serializable
{	private static final long serialVersionUID = 1L;

	public CupTieBreak(CupPart part)
	{	this.part = part;
	}

	/////////////////////////////////////////////////////////////////
	// PART			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private CupPart part;
	
	public void setPart(CupPart part)
	{	this.part = part;
	}	
	
	public CupPart getPart()
	{	return part;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	
	public void setMatch(Match match)
	{	this.match = match;
	}	
	
	public Match getMatch()
	{	return match;
	}
	
	public Match initMatch()
	{	// involved players
		ArrayList<Profile> partProfiles = part.getProfiles();
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		HashMap<Integer,ArrayList<Integer>> rankings = part.getRankings();
		int problematicTie = part.getProblematicTie();
		ArrayList<Integer> tie = rankings.get(problematicTie);
		for(Integer i: tie)
		{	Profile p = partProfiles.get(i);
			profiles.add(p);			
		}
		
		// init match 
		Match result = match.copy();
		result.init(profiles);
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// RANKINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsRankings pointsRankings;
	
	public void setPointsRankings(PointsRankings rankings)
	{	this.pointsRankings = rankings;
	}	
	
	public PointsRankings getPointsRankings()
	{	return pointsRankings;
	}
	
	public boolean breakTie()
	{	boolean result;
		int problematicTie = part.getProblematicTie();
		if(problematicTie<0)
			result = true;
		else
		{	// profiles of the tied players
			ArrayList<Profile> partProfiles = part.getProfiles();
			HashMap<Integer,ArrayList<Integer>> rankings = part.getRankings();
			ArrayList<Integer> tie = rankings.get(problematicTie);
			int tiedPlayersCount = tie.size();
			ArrayList<Profile> tiedProfiles = new ArrayList<Profile>();
			for(Integer i: tie)
				tiedProfiles.add(partProfiles.get(i));
			
			// tournament-relative numbers of the tied players
			CupTournament tournament = part.getTournament();
			ArrayList<Profile> tournamentProfiles = tournament.getProfiles();
			ArrayList<Integer> numbers = new ArrayList<Integer>();
			for(Profile p: tiedProfiles)
				numbers.add(tournamentProfiles.indexOf(p));
			
			// PP-relative ranks of the tied players
			float[] tournamentRanks = pointsRankings.process(tournament);
			int relativeRanks[] = new int[tiedPlayersCount];
			Arrays.fill(relativeRanks,problematicTie);
			for(int i=0;i<numbers.size()-1;i++)
			{	for(int j=i+1;j<numbers.size();j++)
				{	if(tournamentRanks[i]>tournamentRanks[j])
						relativeRanks[i]++;
					else if(tournamentRanks[i]<tournamentRanks[j])
						relativeRanks[j]++;
				}
			}		
				
			// update rankings in part
			for(int i=0;i<relativeRanks.length;i++)
			{	if(relativeRanks[i]!=problematicTie)
				{	// delete old rank
					int playerNumber = tie.get(i);
					tie.remove(playerNumber);
					// insert new rank
					int newRank = relativeRanks[i];
					ArrayList<Integer> list = new ArrayList<Integer>();
					list.add(playerNumber);
					rankings.put(newRank,list);
				}				
			}
			
			// if at least one tie was remove, then the tie-break was successful
			result = tie.size() != tiedPlayersCount;
			
		}
		return result;
	}
}
