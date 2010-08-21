package org.totalboumboum.network.stream.network.round;

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

import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.statistics.detailed.StatisticRound;

/**
 * 
 * @author Vincent Labatut
 *
 */
public interface RoundClientConnectionListener
{	
	public void roundUpdated(Round round);
	public void statsUpdated(StatisticRound stats);
	public void roundStarted(Boolean next);
	public void zoomCoeffRead(Double zoomCoeff);
	public void profilesRead(List<Profile> profiles);
	public void levelInfoRead(LevelInfo levelInfo);
	public void limitsRead(Limits<RoundLimit> limits);
	public void itemCountsRead(HashMap<String,Integer> itemCounts);
	public void eventRead(ReplayEvent event);
}
