package fr.free.totalboumboum.game.tournament.single;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.util.Set;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticTournament;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class SingleTournament extends AbstractTournament
{
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean tournamentOver = false;
	
	@Override
	public void init()
	{	begun = true;
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public boolean isOver()
	{	return tournamentOver;
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

	@Override
	public boolean isReady()
	{	boolean result = true;
		return result;
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
		tournamentOver = true;
		if(panel!=null)
		{	panel.tournamentOver();
			stats.initEndDate();		
		}
//NOTE ou bien : panel.matchOver();		
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

	public int[] getRanks(float[] pts)
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
	
	public int[] getOrderedPlayers()
	{	float[] pts;
		if(isOver())
			pts = stats.getPoints();
		else
			pts = stats.getTotal();
		int[] result = new int[getProfiles().size()];
		int[] ranks = getRanks(pts);
		int done = 0;
		for(int i=1;i<=result.length;i++)
		{	for(int j=0;j<ranks.length;j++)
			{	if(ranks[j]==i)
				{	result[done] = j;
					done++;
				}
			}
		}
		return result;
	}
}
