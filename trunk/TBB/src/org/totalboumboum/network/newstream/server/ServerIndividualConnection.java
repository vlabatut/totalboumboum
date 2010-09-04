package org.totalboumboum.network.newstream.server;

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

import org.totalboumboum.network.game.GameInfo;
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
public class ServerIndividualConnection extends AbstractConnection
{
	public ServerIndividualConnection(ServerGeneralConnection generalConnection, Socket socket) throws IOException
	{	this.generalConnection = generalConnection;
	
		initConnection(socket);
	}
	
	/////////////////////////////////////////////////////////////////
	// GENERAL CONNECTION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ServerGeneralConnection generalConnection;

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
	@Override
	public void messageRead(NetworkMessage message)
	{	if(message instanceof ConfigurationNetworkMessage)
		{	if(message.getInfo().equals(NetworkInfo.REQUEST_GAME_INFO))
				gameInfoRequested();
			else if(message.getInfo().equals(NetworkInfo.REQUEST_PLAYERS_LIST))
				playersListRequested();
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
	private void gameInfoRequested()
	{	GameInfo gameInfo = null;
		ConfigurationNetworkMessage message = new ConfigurationNetworkMessage(NetworkInfo.REQUEST_GAME_INFO,gameInfo);
		writer.addMessage(message);
	}
	
	private void playersListRequested()
	{	
		// TODO write the players list
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
			
			generalConnection = null;
		}
	}
}
