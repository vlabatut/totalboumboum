package org.totalboumboum.stream.network.client;

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
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.stream.network.AbstractConnection;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.message.MessageName;
import org.totalboumboum.stream.network.message.NetworkMessage;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientIndividualConnection extends AbstractConnection implements Runnable
{
	public ClientIndividualConnection(ClientGeneralConnection generalConnection, HostInfo hostInfo)
	{	this.generalConnection = generalConnection;
		
		this.gameInfo = new GameInfo();
		gameInfo.setHostInfo(hostInfo);
		
		initSocket();
	}
	
	/////////////////////////////////////////////////////////////////
	// STATE				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientState state = ClientState.SELECTING_GAME;
	
	public ClientState getState()
	{	return state;
	}
	
	public void setState(ClientState state)
	{	this.state = state;
	}

	/////////////////////////////////////////////////////////////////
	// RUNNABLE		/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void run()
	{	HostInfo hostInfo = gameInfo.getHostInfo();
		String address = hostInfo.getLastIp();
		int port = hostInfo.getLastPort();
		try
		{	Socket socket = new Socket(address,port);
			initConnection(socket,false);
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
	{	if(message.getInfo().equals(MessageName.UPDATE_GAME_INFO))
			gameInfoReceived((GameInfo)message.getData());
		else if(message.getInfo().equals(MessageName.UPDATE_PLAYERS_LIST))
			playersListReceived((List<Profile>)message.getData());
		else if(message.getInfo().equals(MessageName.INFO_REPLAY))
			replayReceived((ReplayEvent)message.getData());
	}
	
	public void writeMessage(NetworkMessage message)
	{	writer.addMessage(message);
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION 			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void gameInfoReceived(GameInfo gameInfo)
	{	// update game info
		this.gameInfo.setAllowedPlayers(gameInfo.getAllowedPlayers());
		this.gameInfo.setAverageScore(gameInfo.getAverageScore());
		this.gameInfo.setPlayerCount(gameInfo.getPlayerCount());
		this.gameInfo.setTournamentName(gameInfo.getTournamentName());
		this.gameInfo.setTournamentType(gameInfo.getTournamentType());
		this.gameInfo.getHostInfo().setState(gameInfo.getHostInfo().getState());
		
		// propagate modifications
		generalConnection.gameInfoChanged(this);
	}

	/**
	 * TODO
	 * when a time out occurs, the host state is set to unknown
	 * and the modification is propagated (?)
	 */
	
	private void playersListReceived(List<Profile> playerProfiles)
	{	// update profile list
		this.playerProfiles = playerProfiles;
		for(Profile profile: playerProfiles)
		{	try
			{	// images must be loaded because they did not pass the stream	
				ProfileLoader.reloadPortraits(profile);
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
		}
		
		// propagate modification
		generalConnection.profilesChanged(this);
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT 				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// MATCH 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// ROUND 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void replayReceived(ReplayEvent event)
	{	generalConnection.replayReceived(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// OWNER INTERFACE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void connectionLost()
	{	reader.finish();
		reader = null;
		
		writer.finish();
		writer = null;
		
		//TODO à completer
	}
	
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
