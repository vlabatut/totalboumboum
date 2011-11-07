package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PointsRankings;
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
	
	@SuppressWarnings("unchecked")
	public Match initMatch()
	{	// involved players
		Ranks ranks = part.getOrderedPlayers();
		int problematicTie = part.getProblematicTie();
		ArrayList<Profile> tie = ranks.getProfilesFromRank(problematicTie);
		ArrayList<Profile> profiles = (ArrayList<Profile>)tie.clone();
		
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
			Ranks ranks = part.getOrderedPlayers();
			ArrayList<Profile> tie = ranks.getProfilesFromRank(problematicTie);
			int tiedPlayersCount = tie.size();
			ArrayList<Profile> tiedProfiles = (ArrayList<Profile>)tie;
			
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
			{	int indexI = numbers.get(i);
				for(int j=i+1;j<numbers.size();j++)
				{	int indexJ = numbers.get(j);
					if(tournamentRanks[indexI]>tournamentRanks[indexJ])
						relativeRanks[i]++;
					else if(tournamentRanks[indexI]<tournamentRanks[indexJ])
						relativeRanks[j]++;
				}
			}		
				
			// update rankings in part
			for(int i=0;i<relativeRanks.length;i++)
			{	if(relativeRanks[i]!=problematicTie)
				{	// delete old rank
					Profile profile = tie.get(i);
					tie.remove(profile);
					// insert new rank
					int newRank = relativeRanks[i];
					ranks.addProfile(newRank,profile);
				}				
			}
			
			// if at least one tie was remove, then the tie-break was successful
			result = tie.size() != tiedPlayersCount;
			
		}
		return result;
	}
}