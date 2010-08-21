package org.totalboumboum.network.stream.network.tournament;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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

import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface TournamentClientConnectionListener
{	
	public void tournamentUpdated(AbstractTournament tournament);
	public void statsUpdated(StatisticTournament stats);
	public void matchStarted(Boolean next);
}
