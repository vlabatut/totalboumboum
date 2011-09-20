package org.totalboumboum.stream.network.client;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.message.MessageName;
import org.totalboumboum.stream.network.message.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ClientGeneralConnection
{
	public ClientGeneralConnection(List<HostInfo> hosts)
	{	// init direct connections
		//List<GameInfo> gameInfos = Configuration.getConnectionsConfiguration().getDirectConnections();
		for(HostInfo hostInfo: hosts)
			createConnection(hostInfo);
		
		// TODO for central connection, a special connection will be defined for the configuration stage
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ClientIndividualConnection> individualConnections = new ArrayList<ClientIndividualConnection>();
	private ClientIndividualConnection activeConnection;
	private Lock connectionsLock = new ReentrantLock();
	
	public void terminateConnection()
	{	connectionsLock.lock();
		for(ClientIndividualConnection connection: individualConnections)
			connection.finish();
		connectionsLock.unlock();
	}
	
	public ClientIndividualConnection getActiveConnection()
	{	return activeConnection;
	}
	
	public void createConnection(HostInfo hostInfo)
	{	ClientIndividualConnection individualConnection;
		int index;
	
		if(hostInfo.getId()==null)
		{	UUID id = UUID.randomUUID();
			hostInfo.setId("temp"+id.toString());
			hostInfo.setDirect(true);
			hostInfo.setPreferred(false);
			hostInfo.setUses(0);
		}
			
		connectionsLock.lock();
		{	individualConnection = new ClientIndividualConnection(this,hostInfo);
			individualConnections.add(individualConnection);
			index = individualConnections.indexOf(individualConnection);
		}
		connectionsLock.unlock();

		fireConnectionAdded(individualConnection,index);
		individualConnection.initSocket();
	}
	
	public void removeConnection(ClientIndividualConnection connection)
	{	int index;
	
		connectionsLock.lock();
		{	index = individualConnections.indexOf(connection);
			connection.finish();
			individualConnections.remove(connection);
		}
		connectionsLock.unlock();
		
		fireConnectionRemoved(connection,index);
	}
	
	public void removeAllConnections()
	{	connectionsLock.lock();
		{	Iterator<ClientIndividualConnection> it = individualConnections.iterator();
			while(it.hasNext())
			{	ClientIndividualConnection connection = it.next();
				connection.finish();
				it.remove();
			}
		}
		connectionsLock.unlock();
	}
	
	public List<GameInfo> getGameList()
	{	List<GameInfo> result = new ArrayList<GameInfo>();

		connectionsLock.lock();
		{	for(ClientIndividualConnection connection: individualConnections)
				result.add(connection.getGameInfo());
		}
		connectionsLock.unlock();
		
		return result;
	}
	
	public void refreshConnection(GameInfo gameInfo)
	{	ClientIndividualConnection cx = null;
		
		connectionsLock.lock();
		{	Iterator<ClientIndividualConnection> it = individualConnections.iterator();
			while(cx==null && it.hasNext())
			{	ClientIndividualConnection connection = it.next();
				GameInfo gi = connection.getGameInfo();
				if(gi==gameInfo)
					cx = connection;
			}
		}
		connectionsLock.unlock();
		
		if(cx!=null)
		{	HostState state = cx.getGameInfo().getHostInfo().getState();
			if(state==HostState.UNKOWN)
				cx.retryConnection();
		}
	}
	
	protected void connectionLost(ClientIndividualConnection connection)
	{	int index;
		connectionsLock.lock();
		{	index = individualConnections.indexOf(connection);
		}
		connectionsLock.unlock();
		
		fireConnectionGameInfoChanged(connection,index,null);
		if(activeConnection==connection)
			fireConnectionActiveConnectionLost(connection,index);
	}
	
	/////////////////////////////////////////////////////////////////
	// RECEIVED MESSAGES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// NOTE don't remember what this lock is for...
	private Lock updateLock = new ReentrantLock();

	public void gameInfoChanged(ClientIndividualConnection connection, String oldId)
	{	int index;
		
		connectionsLock.lock();
		{	index = individualConnections.indexOf(connection);
			if(oldId!=null)
			{	// look for an existing connection with the same new id
				List<ClientIndividualConnection> list = new ArrayList<ClientIndividualConnection>(individualConnections);
				Iterator<ClientIndividualConnection> it = list.iterator();
				HostInfo hi1 = connection.getGameInfo().getHostInfo();
				String id1 = hi1.getId();
				boolean found = false;
				while(!found && it.hasNext())
				{	ClientIndividualConnection cx = it.next();
					HostInfo hi2 = cx.getGameInfo().getHostInfo();
					String id2 = hi2.getId();
					if(id1.equals(id2) && cx!=connection)
					{	found = true;
						hi1.setPreferred(hi2.isPreferred());
						hi1.setUses(hi2.getUses());
						removeConnection(cx);
					}
				}
			}
		}
		connectionsLock.unlock();

		updateLock.lock();
		{	fireConnectionGameInfoChanged(connection,index,oldId);
		}		
		updateLock.unlock();
	}

	public void profilesChanged(ClientIndividualConnection connection)
	{	int index;
		
		connectionsLock.lock();
		{	index = individualConnections.indexOf(connection);
		}
		connectionsLock.unlock();
		
		updateLock.lock();
		{	fireConnectionProfilesChanged(connection,index);
		}
		updateLock.unlock();
	}
	
	/////////////////////////////////////////////////////////////////
	// REPLAYS 					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ReplayEvent> eventList = new ArrayList<ReplayEvent>();
	private Lock replayLock = new ReentrantLock();
	private Condition replayCondition = replayLock.newCondition();
	
	public void replayReceived(ReplayEvent event)
	{	replayLock.lock();
		
		eventList.add(event);
		replayCondition.signal();
		
		replayLock.unlock();
	}
	
	public ReplayEvent retrieveEvent()
	{	ReplayEvent result = null;
	
		replayLock.lock();
		try
		{	while(eventList.isEmpty())
				replayCondition.await();
			result = eventList.get(0);
			eventList.remove(0);
		}
		catch(InterruptedException e)
		{	e.printStackTrace();
		}
		finally
		{	replayLock.unlock();
		}
		
		return result;
	}

	public List<ReplayEvent> retrieveEventList(long referenceTime)
	{	List<ReplayEvent> result = new ArrayList<ReplayEvent>();
		
		replayLock.lock();
		{	Iterator<ReplayEvent> it = eventList.iterator();
			while(it.hasNext())
			{	ReplayEvent event = it.next();
				if(event.getTime()<referenceTime)
				{	result.add(event);
					it.remove();
				}
			}
		}
		replayLock.unlock();
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// SENT MESSAGES		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void sendControl(StreamedEvent event)
	{	NetworkMessage message = new NetworkMessage(MessageName.INFO_PLAYER_CONTROL,event);
		activeConnection.writeMessage(message);
	}
	
	public void requestGameInfos()
	{	String id = Configuration.getConnectionsConfiguration().getHostId();
		NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_GAME_INFO,id);
		List<ClientIndividualConnection> list;
		
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
			connection.writeMessage(message);
	}
	
	public void exitGame()
	{	removeAllConnections();
		Configuration.getConnectionsConfiguration().setClientConnection(null);
	}
	
	public void entersPlayerSelection(GameInfo gameInfo)
	{	List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getGameInfo()==gameInfo)
			{	activeConnection = connection;
//TODO en fait ça devrait être une requête, à valider par le serveur...			
				connection.setState(ClientState.SELECTING_PLAYERS);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERING_PLAYERS_SELECTION,true);
				connection.writeMessage(message);
			}
			else
			{	connection.setState(ClientState.INTERESTED_ELSEWHERE);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERING_PLAYERS_SELECTION,false);
				connection.writeMessage(message);
			}
		}
	}
	
	public void exitPlayersSelection()
	{	List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	activeConnection = null;
				connection.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.EXITING_PLAYERS_SELECTION,true);
				connection.writeMessage(message);
			}
			else
			{	connection.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.EXITING_PLAYERS_SELECTION,false);
				connection.writeMessage(message);
			}
		}
	}
	
	// TODO must handle this distinction between local and remote players, one way or another...
	public void requestPlayersAdd(Profile profile)
	{	profile.setReady(false);
		List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	// TODO when profiles are sent, the portraits must be reloaded (images don't go through streams)
					NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_ADD,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersRemove(Profile profile)
	{	List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_REMOVE,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersChangeColor(Profile profile)
	{	List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_CHANGE_COLOR,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersChangeHero(Profile profile)
	{	List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_CHANGE_HERO,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersSet(int index, Profile profile)
	{	profile.setReady(false);
		List<ClientIndividualConnection> list;
	
		connectionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnection>(individualConnections);
		}
		connectionsLock.unlock();
		
		for(ClientIndividualConnection connection: list)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	Profile oldProfile = connection.getPlayerProfiles().get(index);
				if(!profile.isRemote() && !oldProfile.isRemote())
				{	List<Profile> data = new ArrayList<Profile>(Arrays.asList(oldProfile,profile));		
					NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_SET,data);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void confirmPlayersSelection(boolean confirmation)
	{	ClientState state;
		NetworkMessage message;
		if(confirmation)
		{	state = ClientState.WAITING_TOURNAMENT;
			message = new NetworkMessage(MessageName.CONFIRMING_PLAYERS_SELECTION);
		}
		else
		{	state = ClientState.SELECTING_PLAYERS;
			message = new NetworkMessage(MessageName.UNCONFIRMING_PLAYERS_SELECTION);
		}
		
		connectionsLock.lock();
		{	activeConnection.setState(state);
			activeConnection.writeMessage(message);
		}
		connectionsLock.unlock();
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void tournamentStarted(ClientIndividualConnection connection, AbstractTournament tournament)
	{	if(connection==activeConnection)
		{	fireConnectionTournamentStarted(tournament);
			if(connection.getState()==ClientState.WAITING_TOURNAMENT)
				connection.setState(ClientState.BROWSING_TOURNAMENT);
			else
				connection.setState(ClientState.SELECTING_GAME);
		}
	}

	public StatisticRound getRoundStats()
	{	StatisticRound result = activeConnection.getRoundStats();
		return result;
	}
	
	public Double getZoomCoef()
	{	return activeConnection.getZoomCoef();
	}
	
	public void loadingComplete()
	{	activeConnection.loadingComplete();
	}
	
	public void sendControlSettings(List<ControlSettings> controlSettings)
	{	activeConnection.sendControlSettings(controlSettings);
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ClientGeneralConnectionListener> listeners = new ArrayList<ClientGeneralConnectionListener>();
	private Lock listenersLock = new ReentrantLock();
	
	public void addListener(ClientGeneralConnectionListener listener)
	{	listenersLock.lock();
		{	if(!listeners.contains(listener))
				listeners.add(listener);
		}
		listenersLock.unlock();
	}
	
	public void removeListener(ClientGeneralConnectionListener listener)
	{	listenersLock.lock();
		{	listeners.remove(listener);
		}
		listenersLock.unlock();
	}
	
	private void fireConnectionAdded(ClientIndividualConnection connection, int index)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionAdded(connection,index);
	}

	private void fireConnectionRemoved(ClientIndividualConnection connection, int index)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionRemoved(connection,index);
	}

	private void fireConnectionGameInfoChanged(ClientIndividualConnection connection, int index, String oldId)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionGameInfoChanged(connection,index,oldId);
	}

	private void fireConnectionActiveConnectionLost(ClientIndividualConnection connection, int index)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionActiveConnectionLost(connection,index);
	}

	private void fireConnectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionProfilesChanged(connection,index);
	}

	private void fireConnectionTournamentStarted(AbstractTournament tournament)
	{	List<ClientGeneralConnectionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnectionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnectionListener listener: list)
			listener.connectionTournamentStarted(tournament);
	}
}
