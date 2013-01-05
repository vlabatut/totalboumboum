package org.totalboumboum.game.tournament.cup;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
 * Represents a special match, played
 * in extra when needed, in order to
 * break ties.
 * 
 * @author Vincent Labatut
 */
public class CupTieBreak implements Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard tie breaker.
	 * 
	 * @param part
	 * 		The part containing this tie break.
	 */
	public CupTieBreak(CupPart part)
	{	this.part = part;
	}

	/////////////////////////////////////////////////////////////////
	// PART			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** The part containing this tie break */
	private CupPart part;
	
	/**
	 * Changes the part containing this tie break.
	 * 
	 * @param part
	 * 		New part for this tie break.
	 */
	public void setPart(CupPart part)
	{	this.part = part;
	}	
	
	/**
	 * Returns the part containing this
	 * tie break.
	 * 
	 * @return
	 * 		The part containing this tie break.
	 */
	public CupPart getPart()
	{	return part;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Match used to break the tie */
	private Match match;
	
	/**
	 * Changes the match used to break the tie.
	 * 
	 * @param match
	 * 		New match used to break the tie.
	 */
	public void setMatch(Match match)
	{	this.match = match;
	}	
	
	/**
	 * Returns the match used to break the tie.
	 * 
	 * @return
	 * 		Match used to break the tie.
	 */
	public Match getMatch()
	{	return match;
	}
	
	/**
	 * Initializes the match used to break the tie.
	 * 
	 * @return
	 * 		The resulting initialized match object. 
	 */
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
	/** Points processor used to break the tie */
	private PointsRankings pointsRankings;
	
	/**
	 * Changes the points processor used to break the tie.
	 * 
	 * @param rankings
	 * 		New points processor used to break the tie
	 */
	public void setPointsRankings(PointsRankings rankings)
	{	this.pointsRankings = rankings;
	}	
	
	/**
	 * Returns the points processor used to break the tie
	 * 
	 * @return
	 * 		Points processor used to break the tie.
	 */
	public PointsRankings getPointsRankings()
	{	return pointsRankings;
	}
	
	/**
	 * Tries breaking the tie.
	 *  
	 * @return
	 * 		{@code true} iff at least one player could
	 * 		be removed from this tie situation (and it
	 * 		therefore might require a new tie break for 
	 * 		the remaining player to settle the score).
	 */
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
