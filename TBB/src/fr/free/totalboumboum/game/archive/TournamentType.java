package fr.free.totalboumboum.game.archive;

import fr.free.totalboumboum.game.tournament.AbstractTournament;
import fr.free.totalboumboum.game.tournament.cup.CupTournament;
import fr.free.totalboumboum.game.tournament.league.LeagueTournament;
import fr.free.totalboumboum.game.tournament.sequence.SequenceTournament;
import fr.free.totalboumboum.game.tournament.single.SingleTournament;

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

public enum TournamentType
{
	CUP,LEAGUE,SEQUENCE,SINGLE;
	
	public static TournamentType getType(AbstractTournament tournament)
	{	TournamentType result = null;
		if(tournament instanceof CupTournament)
			result = CUP;
		else if(tournament instanceof LeagueTournament)
			result = LEAGUE;
		else if(tournament instanceof SequenceTournament)
			result = SEQUENCE;
		else if(tournament instanceof SingleTournament)
			result = SINGLE;
		return result;
	}
}
