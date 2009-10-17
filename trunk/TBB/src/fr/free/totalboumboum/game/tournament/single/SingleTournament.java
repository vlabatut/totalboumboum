package fr.free.totalboumboum.game.tournament.single;

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
import java.util.Arrays;
import java.util.Set;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.statistics.raw.StatisticMatch;
import fr.free.totalboumboum.statistics.raw.StatisticTournament;

public class SingleTournament extends AbstractTournament
{	private static final long serialVersionUID = 1L;
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	@Override
	public void init()
	{	begun = true;
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	match.init(profiles);
		}
	}

	@Override
	public void finish()
	{	//NOTE et les matches ? (dans SequenceTournament aussi)
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match = null;

	public void setMatch(Match match)
	{	this.match = match;
	}

	@Override
	public Match getCurrentMatch()
	{	return match;	
	}
	
	@Override
	public void matchOver()
	{	// stats
		StatisticMatch statsMatch = match.getStats();
		stats.addStatisticMatch(statsMatch);
		float[] points = stats.getTotal();
		stats.setPoints(points);
		setOver(true);
		if(panel!=null)
		{	panel.tournamentOver();
			stats.initEndDate();		
		}
//NOTE ou bien : panel.matchOver();		
	}

	public void roundOver()
	{	panel.roundOver();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	
	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = match.getAllowedPlayerNumbers();
		return result;			
	}

	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	private int[] getRanks(float[] pts)
	{	int[] result = new int[getProfiles().size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;

		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(pts[i]<pts[j])
					result[i] = result[i] + 1;
				else if(pts[i]>pts[j])
					result[j] = result[j] + 1;
			}
		}	

		return result;
	}
	
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = getRanks(points);
			ranks2 = getRanks(total);
		}
		else
		{	ranks = getRanks(total);
			ranks2 = new int[ranks.length];
			Arrays.fill(ranks2,0);
		}
		// result
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			int rank2 = ranks2[i];
			Profile profile = getProfiles().get(i);
			ArrayList<Profile> list = result.getProfilesFromRank(rank);
			int index = -1;
			// if no list yet : regular insertion
			if(list==null)
			{	result.addProfile(rank,profile);
				index = 0;
			}
			// if list : insert at right place considering total points
			else
			{	int j = 0;
				while(j<list.size() && index==-1)
				{	Profile profileB = list.get(j);
					int plrIdx = getProfiles().indexOf(profileB);
					int rank2B = ranks2[plrIdx];
					if(rank2<rank2B)
						index = j;
					else
						j++;
				}				
				if(index==-1)
					index = j;
				list.add(index,profile);
			}			
		}
			
		return result;
	}
}
