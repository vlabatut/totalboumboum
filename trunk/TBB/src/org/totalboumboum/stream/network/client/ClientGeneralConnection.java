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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.loop.ClientLoop;
import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.replay.ReplayEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostInfo;
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
	
	public void createConnection(HostInfo hostInfo)
	{	ClientIndividualConnection individualConnection = new ClientIndividualConnection(this,hostInfo);
		individualConnections.add(individualConnection);
	}
	
	public void removeConnection(ClientIndividualConnection connection)
	{	connection.finish();
		individualConnections.remove(connection);
	}
	
	public List<GameInfo> getGameList()
	{	List<GameInfo> result = new ArrayList<GameInfo>();
		for(ClientIndividualConnection connection: individualConnections)
			result.add(connection.getGameInfo());
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CLIENT LOOP			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientLoop loop = null;
	
	public void setLoop(ClientLoop loop)
	{	this.loop = loop;
	}
	
	/////////////////////////////////////////////////////////////////
	// RECEIVED MESSAGES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Lock updateLock = new ReentrantLock();

	public void gameInfoChanged(ClientIndividualConnection connection)
	{	updateLock.lock();
		
		int index = individualConnections.indexOf(connection);
		fireConnectionGameInfoChanged(connection,index);
		
		updateLock.unlock();
	}

	public void profilesChanged(ClientIndividualConnection connection)
	{	updateLock.lock();
	
		int index = individualConnections.indexOf(connection);
		fireConnectionProfilesChanged(connection,index);
		
		updateLock.unlock();
	}
	
	public void replayReceived(ReplayEvent event)
	{	// the method invoked is already synchronized
		loop.addEvent(event);
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
		NetworkMessage message = new NetworkMessage(MessageName.REQUEST_GAME_INFO,id);
		for(ClientIndividualConnection connection: individualConnections)
			connection.writeMessage(message);
	}
	
	public void enterPlayerSelection(GameInfo gameInfo)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getGameInfo()==gameInfo)
			{	connection.setState(ClientState.SELECTING_PLAYERS);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERS_PLAYERS_SELECTION,true);
				connection.writeMessage(message);
			}
			else
			{	connection.setState(ClientState.INTERESTED_ELSEWHERE);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERS_PLAYERS_SELECTION,false);
				connection.writeMessage(message);
			}
		}
	}
	
	public void exitPlayerSelection()
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	connection.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERS_PLAYERS_SELECTION,true);
				connection.writeMessage(message);
			}
			else
			{	connection.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERS_PLAYERS_SELECTION,false);
				connection.writeMessage(message);
			}
		}
	}
	
	// TODO must handle this distinction between local and remote players, one way or another...
	public void requestPlayersAdd(Profile profile)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	// TODO when profiles are sent, the portraits must be reloaded (images don't go through streams)
					NetworkMessage message = new NetworkMessage(MessageName.REQUEST_PLAYERS_ADD,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersRemove(Profile profile)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUEST_PLAYERS_REMOVE,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersChange(Profile profile)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUEST_PLAYERS_CHANGE,profile);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersSet(int index, Profile profile)
	{	for(ClientIndividualConnection connection: individualConnections)
		{	if(connection.getState()==ClientState.SELECTING_PLAYERS)
			{	Profile oldProfile = connection.getPlayerProfiles().get(index);
				if(!profile.isRemote() && !oldProfile.isRemote())
				{	List<Profile> data = new ArrayList<Profile>(Arrays.asList(oldProfile,profile));		
					NetworkMessage message = new NetworkMessage(MessageName.REQUEST_PLAYERS_SET,data);
					connection.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// ZOOM COEFF		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Double zoomCoeff = null;
	
	// TODO not supposed to be synch since it should be defined long before being needed (right?)
	public Double getZoomCoeff()
	{	return zoomCoeff;
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ClientGeneralConnectionListener> listeners = new ArrayList<ClientGeneralConnectionListener>();
	
	public void addListener(ClientGeneralConnectionListener listener)
	{	if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void removeListener(ClientGeneralConnectionListener listener)
	{	listeners.remove(listener);
	}
	
//	private void fireConnectionAdded(ClientIndividualConnection connection, int index)
//	{	for(ClientGeneralConnectionListener listener: listeners)
//			listener.connectionAdded(connection,index);
//	}

//	private void fireConnectionRemoved(ClientIndividualConnection connection, int index)
//	{	for(ClientGeneralConnectionListener listener: listeners)
//			listener.connectionRemoved(connection,index);
//	}

	private void fireConnectionGameInfoChanged(ClientIndividualConnection connection, int index)
	{	for(ClientGeneralConnectionListener listener: listeners)
			listener.connectionGameInfoChanged(connection,index);
	}

	private void fireConnectionProfilesChanged(ClientIndividualConnection connection, int index)
	{	for(ClientGeneralConnectionListener listener: listeners)
			listener.connectionProfilesChanged(connection,index);
	}
}
