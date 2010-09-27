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
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
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
	private boolean retry = true;
	private Lock socketLock = new ReentrantLock();
	private Condition socketCondition = socketLock.newCondition();
	
	/**
	 * for the GUI to ask for a manual retry regarding the connection to this server
	 */
	public void retryConnection()
	{	socketLock.lock();
		{	retry = true;
			socketCondition.signal();
		}
		socketLock.unlock();
	}
	
	@Override
	public void run()
	{	Socket socket = null;
		while(socket==null)
		{	socketLock.lock();
			{	if(retry)
				{	retry = false;
					HostInfo hostInfo = gameInfo.getHostInfo();
					String address = hostInfo.getLastIp();
					int port = hostInfo.getLastPort();
					try
					{	socket = new Socket(address,port);
					}
					catch(ConnectException e)
					{	System.err.println("ConnectException: address "+address+" doesn't respond");
					}
					catch(IOException e)
					{	e.printStackTrace();
					}
				}
			}
			
			try
			{	socketCondition.await();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			socketLock.unlock();
		}
		
		try
		{	initConnection(socket,false);
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
		
		// update host info
		HostInfo hostInfo = this.gameInfo.getHostInfo();
		hostInfo.setState(gameInfo.getHostInfo().getState());
		if(hostInfo.getId()==null)
		{	hostInfo.setId(gameInfo.getHostInfo().getId());
			hostInfo.setLastIp(gameInfo.getHostInfo().getLastIp());
			hostInfo.setLastPort(gameInfo.getHostInfo().getLastPort());
			Configuration.getConnectionsConfiguration().synchronizeHost(hostInfo);
			// propagate new connection
			generalConnection.gameInfoChanged(this,true);
		}
		else
		{	// propagate modifications
			generalConnection.gameInfoChanged(this,false);
		}
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
		{	// images must be loaded because they did not pass the stream	
			try
			{	ProfileLoader.reloadPortraits(profile);
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
			// if profile new, must be inserted in the local DB	
			try
			{	Configuration.getProfilesConfiguration().insertProfile(profile);
			}
			catch (IllegalArgumentException e)
			{	e.printStackTrace();
			}
			catch (SecurityException e)
			{	e.printStackTrace();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{	e.printStackTrace();
			}
			catch (NoSuchFieldException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
		}
		
		if(state==ClientState.SELECTING_GAME)
			state = ClientState.SELECTING_PLAYERS;
		
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
	{	ioLock.lock();
		{	if(!ioFinished)
			{	ioFinished = true;
			
				reader.finish();
				writer.finish();

				//TODO à completer

				reader = null;
				writer = null;
			}
		}
		ioLock.unlock();		
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
