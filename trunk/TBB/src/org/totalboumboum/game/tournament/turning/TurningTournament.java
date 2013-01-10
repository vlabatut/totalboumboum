package org.totalboumboum.game.tournament.turning;

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

import java.util.Set;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;

/**
 * This tournament is based on a round-robin system,
 * in the sense every few rounds/matches, some of the
 * playing players are put to rest, whereas some of the
 * resting players enter game. The winner is the one
 * with the most points at the end, or reaching a point
 * limit first.
 * 
 * @author Vincent Labatut
 */
public class TurningTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard tournament.
	 */
	public TurningTournament()
	{	
	}

	@Override
	public Ranks getOrderedPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		playedMatches.clear();
		
		// TODO Auto-generated method stub
		
	}

	@Override
	public void progress() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Match getCurrentMatch() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void matchOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void roundOver() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Integer> getAllowedPlayerNumbers() {
		// TODO Auto-generated method stub
		return null;
	}
}
