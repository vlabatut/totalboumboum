package org.totalboumboum.network.newstream.client;

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
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.network.game.GameInfo;
import org.totalboumboum.network.host.HostInfo;
import org.totalboumboum.network.newstream.AbstractConnection;
import org.totalboumboum.network.newstream.event.ConfigurationNetworkMessage;
import org.totalboumboum.network.newstream.event.MatchNetworkMessage;
import org.totalboumboum.network.newstream.event.NetworkInfo;
import org.totalboumboum.network.newstream.event.NetworkMessage;
import org.totalboumboum.network.newstream.event.RoundNetworkMessage;
import org.totalboumboum.network.newstream.event.TournamentNetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientIndividualConnection extends AbstractConnection implements Runnable
{
	public ClientIndividualConnection(ClientGeneralConnection generalConnection, GameInfo gameInfo)
	{	this.generalConnection = generalConnection;
		this.gameInfo = gameInfo;
		
		initSocket();
	}
	
	/////////////////////////////////////////////////////////////////
	// RUNNABLE		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	HostInfo hostInfo = gameInfo.getHostInfo();
		InetAddress address = hostInfo.getLastIp();
		int port = hostInfo.getLastPort();
		try
		{	Socket socket = new Socket(address,port);
			initConnection(socket);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// SOCKET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	private void initSocket()
	{	// TODO : manage time-out
		Thread thread = new Thread(this);
		thread.start();
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL CONNECTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientGeneralConnection generalConnection;

	/////////////////////////////////////////////////////////////////
	// GAME INFO	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private GameInfo gameInfo = null;
	
	public GameInfo getGameInfo()
	{	return gameInfo;
	}

	/////////////////////////////////////////////////////////////////
	// PROFILES		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Profile> playerProfiles = new ArrayList<Profile>();
	
	public List<Profile> getPlayerProfiles()
	{	return playerProfiles;
	}
	
	/////////////////////////////////////////////////////////////////
	// MODE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean inGame = false;
	
	public boolean getMode()
	{	return inGame;
	}
	
	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	@Override
	public void messageRead(NetworkMessage message)
	{	if(message instanceof ConfigurationNetworkMessage)
		{	if(message.getInfo().equals(NetworkInfo.REQUEST_GAME_INFO))
				gameInfoReceived((GameInfo)message.getData());
			else if(message.getInfo().equals(NetworkInfo.REQUEST_PLAYERS_LIST))
				playersListReceived((List<Profile>)message.getData());
			// TODO
		}
		else if(message instanceof TournamentNetworkMessage)
		{
			// TODO
		}
		else if(message instanceof MatchNetworkMessage)
		{
			// TODO
		}
		else if(message instanceof RoundNetworkMessage)
		{
			// TODO
		}
	}
	
	public void writeMessage(NetworkMessage message)
	{	writer.addMessage(message);
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION MESSAGES	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void gameInfoReceived(GameInfo gameInfo)
	{	// complete host info
		HostInfo oldHostInfo = this.gameInfo.getHostInfo();
		HostInfo hostInfo = gameInfo.getHostInfo();
		hostInfo.setLastIp(oldHostInfo.getLastIp());
		hostInfo.setLastPort(oldHostInfo.getLastPort());
		hostInfo.setPreferred(oldHostInfo.isPreferred());
		hostInfo.setUses(oldHostInfo.getUses());

		// update game info
		this.gameInfo = gameInfo;
		
		// propagate modifications
		generalConnection.gameInfoChanged(this);
	}

	/**
	 * TODO
	 * when a time out occurs, the host state is set to unknown
	 * and modification is propagated
	 */
	
	private void playersListReceived(List<Profile> playerProfiles)
	{	// update profile list
		this.playerProfiles = playerProfiles;
		
		// propagate modification
		generalConnection.profilesChanged(this);
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT MESSAGES	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			gameInfo = null;
			generalConnection = null;
		}
	}
}
