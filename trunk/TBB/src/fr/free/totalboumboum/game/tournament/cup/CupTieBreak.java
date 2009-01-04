package fr.free.totalboumboum.game.tournament.cup;

import java.io.Serializable;

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
	
	/////////////////////////////////////////////////////////////////
	// RANKINGS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsRankings rankings;
	
	public void setRankings(PointsRankings rankings)
	{	this.rankings = rankings;
	}	
	
	public PointsRankings getRankings()
	{	return rankings;
	}	
}
