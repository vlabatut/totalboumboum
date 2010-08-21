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
import org.totalboumboum.network.stream.network.AbstractConnectionManager;
import org.totalboumboum.statistics.detailed.StatisticRound;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class RoundServerConnectionManager extends AbstractConnectionManager<RoundServerConnectionListener,RoundServerConnection> implements RoundServerConnectionListener
{	
	public RoundServerConnectionManager() throws IOException
	{	
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void addConnection(Socket socket) throws IOException
	{	RoundServerConnection connection = new RoundServerConnection(socket);
		addConnection(connection);
	}
	
	@Override
	protected void addConnection(RoundServerConnection connection)
	{	connections.add(connection);
		connection.addListener(this);
	}

	@Override
	protected void clearConnections()
	{	for(RoundServerConnection connection: connections)
			connection.removeListener(this);
		connections.clear();
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT STREAM		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	public void updateRound(Round match) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(match);
	}
	
	public void updateStats(StatisticRound stats) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(stats);
	}
	
	public void setZoomCoeff(Double zoomCoeff) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(zoomCoeff);		
	}
	
	public void setProfiles(List<Profile> profiles) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(profiles);		
	}
	
	public void setLevelInfo(LevelInfo levelInfo) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(levelInfo);		
	}

	public void setLimits(Limits<RoundLimit> limits) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(limits);		
	}

	public void setItemCounts(HashMap<String,Integer> itemCounts) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(itemCounts);		
	}
	
	public void sendEvent(ReplayEvent event) throws IOException
	{	for(RoundServerConnection connection: connections)
			connection.write(event);		
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void eventRead(RemotePlayerControlEvent event)
	{	fireEventRead(event);
	}
	
	@Override
	public void controlSettingsRead(List<ControlSettings> controlSettings)
	{	fireControlSettingsRead(controlSettings);
	}

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
