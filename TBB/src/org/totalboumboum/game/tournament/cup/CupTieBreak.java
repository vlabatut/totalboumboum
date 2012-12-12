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
import java.util.Arrays;
import java.util.List;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.PointsRankings;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;


/**
 * 
 * @author Vincent Labatut
 */
public class CupTieBreak implements Serializable
{	/** Id */
	private static final long serialVersionUID = 1L;

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
		Ranks ranks = part.getOrderedPlayers();
		int problematicTie = part.getProblematicTie();
		List<Profile> tie = ranks.getProfilesFromRank(problematicTie);
		List<Profile> profiles = new ArrayList<Profile>(tie);
		
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
			List<Profile> tie = ranks.getProfilesFromRank(problematicTie);
			int tiedPlayersCount = tie.size();
			List<Profile> tiedProfiles = (ArrayList<Profile>)tie;
			
			// tournament-relative numbers of the tied players
			CupTournament tournament = part.getTournament();
			List<Profile> tournamentProfiles = tournament.getProfiles();
			List<Integer> numbers = new ArrayList<Integer>();
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
				{	// get profile
					Profile profile = tie.get(i);
					tie.set(i,null);
					// insert new rank
					int newRank = relativeRanks[i];
					ranks.addProfile(newRank,profile);
				}				
			}
			
			// delete old rank
			int i=0;
			while(i<tie.size())
			{	if(tie.get(i)==null)
					tie.remove(i);
				else
					i++;
			}
			
			// if at least one tie was removed, then the tie-break was successful
			result = tie.size() != tiedPlayersCount;
			
		}
		return result;
	}
}
