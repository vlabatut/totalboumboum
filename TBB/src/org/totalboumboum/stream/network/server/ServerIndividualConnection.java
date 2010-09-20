package org.totalboumboum.stream.network.server;

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
import java.util.List;

import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.stream.network.AbstractConnection;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.message.MessageName;
import org.totalboumboum.stream.network.message.NetworkMessage;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class ServerIndividualConnection extends AbstractConnection
{
	public ServerIndividualConnection(ServerGeneralConnection generalConnection, Socket socket) throws IOException
	{	this.generalConnection = generalConnection;
	
		initConnection(socket,true);
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL CONNECTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerGeneralConnection generalConnection;

	/////////////////////////////////////////////////////////////////
	// CLIENT STATE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientState state = ClientState.SELECTING_GAME;
	
	public ClientState getState()
	{	return state;
	}
	
	public void setState(ClientState state)
	{	this.state = state;
	}

	/////////////////////////////////////////////////////////////////
	// CLIENT STATE			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String hostId = null;
	
	public String getClientId()
	{	return hostId;
	}
	
	public void setClientId(String hostId)
	{	this.hostId = hostId;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void messageRead(NetworkMessage message)
	{	if(message.getInfo().equals(MessageName.REQUEST_GAME_INFO))
			gameInfoRequested(message);
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_LIST))
			playersListRequested();
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_ADD))
			playersAddRequested(message);
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_CHANGE))
			playersChangeRequested(message);
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_SET))
			playersSetRequested(message);
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_REMOVE))
			playersRemoveRequested(message);
		else if(message.getInfo().equals(MessageName.ENTERS_PLAYERS_SELECTION))
			entersPlayersSelection(message);
		else if(message.getInfo().equals(MessageName.EXITS_PLAYERS_SELECTION))
			exitsPlayersSelection(message);
		
		else if(message.getInfo().equals(MessageName.INFO_PLAYER_CONTROL))
				controlReceived(message);
	}
	
	public void writeMessage(NetworkMessage message)
	{	writer.addMessage(message);
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION MESSAGES	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void gameInfoRequested(NetworkMessage message)
	{	hostId = (String)message.getData();
		GameInfo gameInfo = generalConnection.getGameInfo();
		NetworkMessage msg = new NetworkMessage(MessageName.UPDATE_GAME_INFO,gameInfo);
		writer.addMessage(msg);
	}
	
	private void playersListRequested()
	{	
		// TODO write the players list
	}
	
	private void entersPlayersSelection(NetworkMessage message)
	{	boolean local = (Boolean) message.getData();
		if(local)
		{	// update state
			state = ClientState.SELECTING_PLAYERS;
			// send back players list
			NetworkMessage msg = new NetworkMessage(MessageName.UPDATE_PLAYERS_LIST,generalConnection.getPlayerProfiles());
			writeMessage(msg);
		}
		else
			state = ClientState.INTERESTED_ELSEWHERE;
	}

	private void exitsPlayersSelection(NetworkMessage message)
	{	boolean local = (Boolean) message.getData();
		state = ClientState.SELECTING_GAME;
		if(local)
		{	// TODO must remove all the players from this connection
			generalConnection.playerSelectionExited(this);
		}
	}
	
	private void playersAddRequested(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		if(profile.getLastHost().equals(hostId))
			generalConnection.playersAddRequested(profile,this);
	}
	
	private void playersChangeRequested(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		if(profile.getLastHost().equals(hostId))
			generalConnection.playersChangeRequested(profile,this);
	}
	
	@SuppressWarnings("unchecked")
	private void playersSetRequested(NetworkMessage message)
	{	List<Profile> list = (List<Profile>)message.getData();
		Profile p1 = list.get(0);
		Profile p2 = list.get(1);
		String id = p1.getId();
		if(p1.getLastHost().equals(hostId) && p2.getLastHost().equals(hostId))
			generalConnection.playersSetRequested(id,p2,this);
	}
	
	private void playersRemoveRequested(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		if(profile.getLastHost().equals(hostId))
		{	String id = profile.getId();
			generalConnection.playersRemoveRequested(id,this);
		}
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT MESSAGES		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// MATCH MESSAGES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////
	// ROUND MESSAGES			/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void controlReceived(NetworkMessage message)
	{	StreamedEvent event = (StreamedEvent) message.getData();
		if(event instanceof RemotePlayerControlEvent)
		{	RemotePlayerControlEvent evt = (RemotePlayerControlEvent) event;
			generalConnection.controlReceived(evt);
		}
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
		if(state==ClientState.SELECTING_GAME)
		{	generalConnection.removeConnection(this);
			
		}
		else if(state==ClientState.SELECTING_GAME)
		{	generalConnection.removeConnection(this);
			
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			generalConnection = null;
		}
	}
}
