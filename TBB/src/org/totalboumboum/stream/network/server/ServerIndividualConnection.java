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

import org.totalboumboum.engine.loop.event.StreamedEvent;
import org.totalboumboum.engine.loop.event.control.RemotePlayerControlEvent;
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
	// MODE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ClientState state = ClientState.SELECTING_GAME;
	
	public ClientState getState()
	{	return state;
	}
	
	public void setState(ClientState state)
	{	this.state = state;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO virer les différentes classes de messages : c'est redondant avec les info incluses dans les msgs
	@Override
	public void messageRead(NetworkMessage message)
	{	// TODO maybe those should be filtered? or a standard reply is always provided by the corresponding function?
		if(message.getInfo().equals(MessageName.REQUEST_GAME_INFO))
			gameInfoRequested();
		else if(message.getInfo().equals(MessageName.REQUEST_PLAYERS_LIST))
			playersListRequested();
		else if(message.getInfo().equals(MessageName.INFO_PLAYER_CONTROL))
				controlReceived(message);
	}
	
	public void writeMessage(NetworkMessage message)
	{	writer.addMessage(message);
	}

	/////////////////////////////////////////////////////////////////
	// CONFIGURATION MESSAGES	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private void gameInfoRequested()
	{	GameInfo gameInfo = generalConnection.getGameInfo();
		NetworkMessage message = new NetworkMessage(MessageName.UPDATE_GAME_INFO,gameInfo);
		writer.addMessage(message);
	}
	
	private void playersListRequested()
	{	
		// TODO write the players list
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
	// FINISH				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void finish()
	{	if(!finished)
		{	super.finish();
			
			generalConnection = null;
		}
	}
}
