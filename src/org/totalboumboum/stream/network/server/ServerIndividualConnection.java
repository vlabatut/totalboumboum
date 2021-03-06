package org.totalboumboum.stream.network.server;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import org.totalboumboum.configuration.controls.ControlSettings;
import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.stream.network.AbstractConnection;
import org.totalboumboum.stream.network.client.ClientState;
import org.totalboumboum.stream.network.data.game.GameInfo;
import org.totalboumboum.stream.network.data.host.HostState;
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
	{	HostState state = generalConnection.getGameInfo().getHostInfo().getState();
		
		if(message.getInfo().equals(MessageName.REQUESTING_GAME_INFO))
			gameInfoRequested(message);
		else if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_LIST))
			playersListRequested();
		
		else if(state==HostState.OPEN)
		{	if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_ADD))
				playersAddRequested(message);
			else if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_CHANGE_COLOR))
				playersChangeRequestedColor(message);
			else if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_CHANGE_HERO))
				playersChangeRequestedHero(message);
			else if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_SET))
				playersSetRequested(message);
			else if(message.getInfo().equals(MessageName.REQUESTING_PLAYERS_REMOVE))
				playersRemoveRequested(message);
			else if(message.getInfo().equals(MessageName.ENTERING_PLAYERS_SELECTION))
				entersPlayersSelection(message);
			else if(message.getInfo().equals(MessageName.EXITING_PLAYERS_SELECTION))
				exitsPlayersSelection(message);
			else if(message.getInfo().equals(MessageName.CONFIRMING_PLAYERS_SELECTION))
				confirmsPlayersSelection(message);
			else if(message.getInfo().equals(MessageName.UNCONFIRMING_PLAYERS_SELECTION))
				unconfirmsPlayersSelection(message);
		}
		
		else if(state==HostState.CLOSED)
		{	if(message.getInfo().equals(MessageName.EXITING_PLAYERS_SELECTION))
				exitsPlayersSelection(message);
			else if(message.getInfo().equals(MessageName.CONFIRMING_PLAYERS_SELECTION))
				confirmsPlayersSelection(message);
			else if(message.getInfo().equals(MessageName.UNCONFIRMING_PLAYERS_SELECTION))
				unconfirmsPlayersSelection(message);
		}
		
		else if(state==HostState.PLAYING)
		{	if(message.getInfo().equals(MessageName.UPDATING_CONTROLS_SETTINGS))
				controlSettingsReceived(message);
			else if(message.getInfo().equals(MessageName.LOADING_COMPLETE))
				loadingComplete(message);
			else if(message.getInfo().equals(MessageName.INFO_PLAYER_CONTROL))
				controlReceived(message);
		}
	}
	
	public void writeMessage(NetworkMessage message)
	{	writer.addMessage(message);
	}

	public void propagateMessage(NetworkMessage message)
	{	MessageName info = message.getInfo();
	
		boolean send = false;
		
		// configuration
		if(info==MessageName.UPDATING_GAME_INFO)
		{	send = state==ClientState.SELECTING_GAME
					|| state==ClientState.SELECTING_PLAYERS
					|| state==ClientState.WAITING_TOURNAMENT;
		}
		else if(info==MessageName.UPDATING_PLAYERS_LIST)
		{	send = state==ClientState.SELECTING_PLAYERS
					|| state==ClientState.WAITING_TOURNAMENT;
		}
		else if(info==MessageName.STARTING_TOURNAMENT)
		{	if(state==ClientState.SELECTING_PLAYERS)
			{	state = ClientState.SELECTING_GAME;
				send = true;
			}
			else if(state==ClientState.WAITING_TOURNAMENT)
			{	state = ClientState.BROWSING_TOURNAMENT;
				send = true;
			}
			else
				send = false;
		}
		
		// actual game
		else if(info==MessageName.UPDATING_ZOOM_COEFF)
		{	if(state==ClientState.BROWSING_TOURNAMENT
					|| state==ClientState.BROWSING_MATCH
					|| state==ClientState.BROWSING_ROUND)
			{	send = true;
				state = ClientState.LOADING_ROUND;
			}
		}
		else if(info==MessageName.STARTING_ROUND)
		{	if(state==ClientState.LOADING_ROUND)
			{	send = true;
				state = ClientState.PLAYING_ROUND;
			}
		}
		else if(info==MessageName.INFO_REPLAY)
		{	send = state==ClientState.PLAYING_ROUND
				|| state==ClientState.BROWSING_TOURNAMENT
				|| state==ClientState.BROWSING_MATCH
				|| state==ClientState.BROWSING_ROUND
				|| state==ClientState.LOADING_ROUND;
		}
		else if(info==MessageName.UPDATING_ROUND_STATS)
		{	if(state==ClientState.PLAYING_ROUND)
			{	send = true;
				state = ClientState.BROWSING_ROUND;
			}
		}
		
		if(send)	
			writeMessage(message);
	}
	
	/////////////////////////////////////////////////////////////////
	// CONFIGURATION MESSAGES	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void gameInfoRequested(NetworkMessage message)
	{	hostId = (String)message.getData();
		GameInfo gameInfo = generalConnection.getGameInfo();
		NetworkMessage msg = new NetworkMessage(MessageName.UPDATING_GAME_INFO,gameInfo);
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
			NetworkMessage msg = new NetworkMessage(MessageName.UPDATING_PLAYERS_LIST,generalConnection.getPlayerProfiles());
			writeMessage(msg);
		}
		else
			state = ClientState.INTERESTED_ELSEWHERE;
	}

	private void exitsPlayersSelection(NetworkMessage message)
	{	boolean local = (Boolean) message.getData();
		state = ClientState.SELECTING_GAME;
		if(local)
		{	generalConnection.playerSelectionExited(this);
		}
	}
	
	private void confirmsPlayersSelection(NetworkMessage message)
	{	state = ClientState.WAITING_TOURNAMENT;
		generalConnection.playerSelectionConfirmed(this,true);
	}
	
	private void unconfirmsPlayersSelection(NetworkMessage message)
	{	state = ClientState.SELECTING_PLAYERS;
		generalConnection.playerSelectionConfirmed(this,false);
	}
	
	private void playersAddRequested(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		profile.setControlSettingsIndex(0);
		if(profile.getLastHost().equals(hostId))
			generalConnection.playersAddRequested(profile,this);
	}
	
	private void playersChangeRequestedColor(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		profile.setControlSettingsIndex(0);
		if(profile.getLastHost().equals(hostId))
			generalConnection.playersChangeRequestedColor(profile,this);
	}
	
	private void playersChangeRequestedHero(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		profile.setControlSettingsIndex(0);
		if(profile.getLastHost().equals(hostId))
			generalConnection.playersChangeRequestedHero(profile,this);
	}
	
	@SuppressWarnings("unchecked")
	private void playersSetRequested(NetworkMessage message)
	{	List<Profile> list = (List<Profile>)message.getData();
		Profile p1 = list.get(0);
		Profile p2 = list.get(1);
		p2.setControlSettingsIndex(0);
		String id = p1.getId();
		if(p1.getLastHost().equals(hostId) && p2.getLastHost().equals(hostId))
			generalConnection.playersSetRequested(id,p2,this);
	}
	
	private void playersRemoveRequested(NetworkMessage message)
	{	Profile profile = (Profile)message.getData();
		profile.setControlSettingsIndex(0);
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
	@SuppressWarnings("unchecked")
	private void controlSettingsReceived(NetworkMessage message)
	{	List<ControlSettings> controlSettings = (List<ControlSettings>)message.getData();
		generalConnection.controlSettingsReceived(controlSettings,this);
	}
	
	private void loadingComplete(NetworkMessage message)
	{	//state = ClientState.WAITING_ROUND;
		generalConnection.loadingComplete(this);
	}
	
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
	{	ioLock.lock();
		{	if(!ioFinished)
			{	ioFinished = true;
				
				reader.finish();
				writer.finish();
				
				//TODO à completer
				if(state==ClientState.SELECTING_GAME)
				{	generalConnection.removeConnection(this);
				}
				else if(state==ClientState.SELECTING_PLAYERS
					|| state==ClientState.WAITING_TOURNAMENT)
				{	generalConnection.playerSelectionExited(this);
					generalConnection.removeConnection(this);
				}
				
				reader = null;
				writer = null;
			}
		}
		ioLock.unlock();
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			generalConnection = null;
		}
	}
}
