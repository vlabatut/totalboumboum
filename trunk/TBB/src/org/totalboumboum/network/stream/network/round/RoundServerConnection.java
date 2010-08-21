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
public class RoundServerConnection extends AbstractConnection<RoundServerConnectionListener>
{	
	public RoundServerConnection(Socket socket) throws IOException
	{	super(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateRound(Round match) throws IOException
	{	write(match);
	}
	
	public void updateStats(StatisticRound stats) throws IOException
	{	write(stats);
	}
	
	public void setZoomCoeff(Double zoomCoeff) throws IOException
	{	write(zoomCoeff);		
	}
	
	public void setProfiles(List<Profile> profiles) throws IOException
	{	write(profiles);		
	}
	
	public void setLevelInfo(LevelInfo levelInfo) throws IOException
	{	write(levelInfo);		
	}

	public void setLimits(Limits<RoundLimit> limits) throws IOException
	{	write(limits);		
	}

	public void setItemCounts(HashMap<String,Integer> itemCounts) throws IOException
	{	write(itemCounts);		
	}
	
	public void sendEvent(ReplayEvent event) throws IOException
	{	write(event);		
	}
	
	/////////////////////////////////////////////////////////////////
	// INPUT STREAM			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	@Override
	public void dataRead(Object data)
	{	if(data instanceof RemotePlayerControlEvent)
		{	RemotePlayerControlEvent event = (RemotePlayerControlEvent)data;
			fireEventRead(event);
		}
		else if(data instanceof List)
		{	List<ControlSettings> controlSettings = (List<ControlSettings>)data;
			fireControlSettingsRead(controlSettings);
		}
	}

	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void fireEventRead(RemotePlayerControlEvent event)
	{	for(RoundServerConnectionListener listener: listeners)
			listener.eventRead(event);
	}
	
	private void fireControlSettingsRead(List<ControlSettings> controlSettings)
	{	for(RoundServerConnectionListener listener: listeners)
			listener.controlSettingsRead(controlSettings);
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
