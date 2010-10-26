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
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.stream.network.AbstractConnection;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostState;
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
System.out.println(state);	
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
			socketCondition.signalAll();
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
					hostInfo.setState(HostState.RETRIEVING);
					generalConnection.gameInfoChanged(this,null);
					String address = hostInfo.getLastIp();
					int port = hostInfo.getLastPort();
					try
					{	socket = new Socket(address,port);
					}
					catch(ConnectException e)
					{	System.err.println("ConnectException: address "+address+" doesn't respond");
						hostInfo.setState(HostState.UNKOWN);
						generalConnection.gameInfoChanged(this,null);
					}
//					catch(UnknownHostException e)
//					{	System.err.println("UnknownHostException: address "+address+" doesn't respond");
//					}
					catch(IOException e)
					{	e.printStackTrace();
						hostInfo.setState(HostState.UNKOWN);
						generalConnection.gameInfoChanged(this,null);
					}
				}
			}
			
			if(socket==null)
			{	try
				{	socketCondition.await();
				}
				catch (InterruptedException e)
				{	e.printStackTrace();
				}
			}
			socketLock.unlock();
		}
		
		try
		{	initConnection(socket,false);
		}
		catch (IOException e)
		{	e.printStackTrace();
		}
		
		// ask for the game info
		String id = Configuration.getConnectionsConfiguration().getHostId();
		NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_GAME_INFO,id);
		writeMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// SOCKET				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	protected void initSocket()
	{	Thread thread = new Thread(this);
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
	{	if(message.getInfo().equals(MessageName.UPDATING_PLAYERS_LIST))
			playersListReceived((List<Profile>)message.getData());
		else if(message.getInfo().equals(MessageName.UPDATING_GAME_INFO))
			gameInfoReceived((GameInfo)message.getData());
		else if(message.getInfo().equals(MessageName.STARTING_TOURNAMENT))
			tournamentStarted((AbstractTournament)message.getData());
		else if(message.getInfo().equals(MessageName.UPDATING_ZOOM_COEFF))
			zoomCoeffUpdated((Double)message.getData());
		else if(message.getInfo().equals(MessageName.STARTING_ROUND))
			roundStarted();
		else if(message.getInfo().equals(MessageName.INFO_REPLAY))
			replayReceived((ReplayEvent)message.getData());
		else if(message.getInfo().equals(MessageName.UPDATING_ROUND_STATS))
			roundStatsUpdated((StatisticRound)message.getData());
	}
	
	public void writeMessage(NetworkMessage message)
	{	if(writer!=null)
			writer.addMessage(message);
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
		HostInfo hi = gameInfo.getHostInfo();
		hostInfo.setState(hi.getState());
		if(hostInfo.getId().startsWith("temp"))
		{	String oldId = hostInfo.getId();
			hostInfo.setId(hi.getId());
			hostInfo.setName(hi.getName());
			//hostInfo.setLastIp(hi.getLastIp());
			//hostInfo.setLastPort(hi.getLastPort());
			hostInfo.setCentral(hi.isCentral());
			hostInfo.setDirect(hi.isDirect());
			Configuration.getConnectionsConfiguration().synchronizeHost(hostInfo);
			// propagate new connection
			generalConnection.gameInfoChanged(this,oldId);
		}
		else
		{	// propagate modifications
			generalConnection.gameInfoChanged(this,null);
		}
	}

	private void playersListReceived(List<Profile> playerProfiles)
	{	// update controls
		for(Profile p2: playerProfiles)
		{	String id2 = p2.getId();
			int cs = 0;
			Iterator<Profile> it = this.playerProfiles.iterator();
			while(cs!=0 && it.hasNext())
			{	Profile p1 = it.next();
				String id1 = p1.getId();
				if(id1.equals(id2))
					cs = p1.getControlSettingsIndex();
			}
			p2.setControlSettingsIndex(cs);
		}
		
		// update profile list
		this.playerProfiles = playerProfiles;
		
		// images must be loaded because they did not pass the stream	
		for(Profile profile: playerProfiles)
		{	try
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
	private AbstractTournament tournament = null;

	private void tournamentStarted(AbstractTournament tournament)
	{	this.tournament = tournament;
		generalConnection.tournamentStarted(this,tournament);
	}

	public AbstractTournament getTournament()
	{	return tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCH 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// ROUND 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean canStart;
	private Lock loadingLock = new ReentrantLock();
	private Condition loadingCondition = loadingLock.newCondition();
	
	public void sendControlSettings(List<ControlSettings> controlSettings)
	{	NetworkMessage message = new NetworkMessage(MessageName.UPDATING_CONTROLS_SETTINGS,controlSettings);
		writeMessage(message);
	}
	
	public void loadingComplete()
	{	NetworkMessage message = new NetworkMessage(MessageName.LOADING_COMPLETE);
		writeMessage(message);
		
		loadingLock.lock();
		{	while(!canStart)
			{	try
				{	loadingCondition.await();
				}
				catch (InterruptedException e)
				{	e.printStackTrace();
				}
			}
		
			// reset variable for next time
			canStart = false;
		}
		loadingLock.unlock();
		
		state = ClientState.PLAYING_ROUND;
	}
	
	public void roundStarted()
	{	loadingLock.lock();
		{	canStart = true;
			loadingCondition.signal();
		}
		loadingLock.unlock();
	}
	
	private void replayReceived(ReplayEvent event)
	{	generalConnection.replayReceived(event);
	}
	
	/////////////////////////////////////////////////////////////////
	// STATS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats = null;
	private Lock statsLock = new ReentrantLock();
	private Condition statsCondition = statsLock.newCondition();
	
	private void roundStatsUpdated(StatisticRound stats)
	{	if(state==ClientState.PLAYING_ROUND)
		{	statsLock.lock();
			{	this.stats = stats;
				statsCondition.signal();
			}
			statsLock.unlock();
		}
	}
	
	public StatisticRound getRoundStats()
	{	StatisticRound result = null;
		
		statsLock.lock();
		{	// wait for the server to send the round stats
			try
			{	while(stats==null)
					statsCondition.await();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			
			// update stats
			result = stats;
			// reset variable for next time
			stats = null;
			
			// update state
			state = ClientState.BROWSING_ROUND;
//			NetworkMessage message = new NetworkMessage(MessageName.UPDATING_STATE,state);
//			writeMessage(message);
		}
		statsLock.unlock();
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// ZOOM COEFF				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double zoomCoeff = null;
	private Lock zoomCoeffLock = new ReentrantLock();
	private Condition zoomCoeffCondition = zoomCoeffLock.newCondition();
	
	private void zoomCoeffUpdated(double zoomCoeff)
	{	if(state==ClientState.BROWSING_TOURNAMENT
			|| state==ClientState.BROWSING_MATCH
			|| state==ClientState.BROWSING_ROUND)
		{	zoomCoeffLock.lock();
			{	this.zoomCoeff = zoomCoeff;
				zoomCoeffCondition.signal();
			}
			zoomCoeffLock.unlock();
		}
	}
	
	public double getZoomCoef()
	{	double result;
		
		zoomCoeffLock.lock();
		{	try
			{	while(zoomCoeff==null)
					zoomCoeffCondition.await();
			}
			catch (InterruptedException e)
			{	e.printStackTrace();
			}
			
			// set result
			result = zoomCoeff;
			// reset variable for next time
			zoomCoeff = null;
			// update state
			state = ClientState.LOADING_ROUND;
		}
		zoomCoeffLock.unlock();
		
		return result;
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
		
		generalConnection.connectionLost(this);
		//gameInfo.getHostInfo().setState(HostState.UNKOWN);
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
