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

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.network.stream.network.AbstractConnection;
import org.totalboumboum.statistics.detailed.StatisticRound;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundClientConnection extends AbstractConnection<RoundClientConnectionListener>
{	
	public RoundClientConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void sendEvent(RemotePlayerControlEvent event) throws IOException
	{	write(event);
	}

	public void sendControlSettings(List<ControlSettings> controlSettings) throws IOException
	{	write(controlSettings);
	}

	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	@Override
	public void dataRead(Object data)
	{	if(data instanceof Round)
		{	Round round = (Round)data;
			fireRoundUpdated(round);
		}
		else if(data instanceof StatisticRound)
		{	StatisticRound stats = (StatisticRound)data;
			fireStatsUpdated(stats);
		}
		else if(data instanceof Boolean)
		{	Boolean next = (Boolean) data;
			fireRoundStarted(next);
		}
		else if(data instanceof Double)
		{	Double zoomCoeff = (Double) data;
			fireZoomCoeffRead(zoomCoeff);
		}
		else if(data instanceof List)
		{	List<Profile> profiles = (List<Profile>) data;
			fireProfilesRead(profiles);
		}
		else if(data instanceof LevelInfo)
		{	LevelInfo levelInfo = (LevelInfo) data;
			fireLevelInfoRead(levelInfo);
		}
		else if(data instanceof Limits)
		{	Limits<RoundLimit> limits = (Limits<RoundLimit>) data;
			fireLimitsRead(limits);
		}
		else if(data instanceof HashMap)
		{	HashMap<String,Integer> itemCounts = (HashMap<String,Integer>) data;
			fireItemCountsRead(itemCounts);
		}
		else if(data instanceof ReplayEvent)
		{	ReplayEvent event = (ReplayEvent) data;
			fireEventRead(event);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireRoundUpdated(Round round)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.roundUpdated(round);
	}

	private void fireStatsUpdated(StatisticRound stats)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.statsUpdated(stats);
	}

	private void fireRoundStarted(Boolean start)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.roundStarted(start);
	}

	private void fireZoomCoeffRead(Double zoomCoeff)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.zoomCoeffRead(zoomCoeff);
	}

	private void fireProfilesRead(List<Profile> profiles)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.profilesRead(profiles);
	}

	private void fireLevelInfoRead(LevelInfo levelInfo)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.levelInfoRead(levelInfo);
	}

	private void fireLimitsRead(Limits<RoundLimit> limits)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.limitsRead(limits);
	}

	private void fireItemCountsRead(HashMap<String,Integer> itemCounts)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.itemCountsRead(itemCounts);
	}

	private void fireEventRead(ReplayEvent event)
	{	for(RoundClientConnectionListener listener: listeners)
			listener.eventRead(event);
	}

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
		}
	}
}
