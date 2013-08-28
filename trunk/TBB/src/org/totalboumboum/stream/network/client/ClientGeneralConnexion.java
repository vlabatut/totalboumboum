package org.totalboumboum.stream.network.client;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
public class ClientGeneralConnexion
{
	public ClientGeneralConnexion(List<HostInfo> hosts)
	{	// init direct connexions
		//List<GameInfo> gameInfos = Configuration.getConnexionsConfiguration().getDirectConnexions();
		for(HostInfo hostInfo: hosts)
			createConnexion(hostInfo);
		
		// TODO for central connexion, a special connexion will be defined for the configuration stage
	}
	
	/////////////////////////////////////////////////////////////////
	// CONNECTIONS			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<ClientIndividualConnexion> individualConnexions = new ArrayList<ClientIndividualConnexion>();
	private ClientIndividualConnexion activeConnexion;
	private Lock connexionsLock = new ReentrantLock();
	
	public void terminateConnexion()
	{	connexionsLock.lock();
		for(ClientIndividualConnexion connexion: individualConnexions)
			connexion.finish();
		connexionsLock.unlock();
	}
	
	public ClientIndividualConnexion getActiveConnexion()
	{	return activeConnexion;
	}
	
	public void createConnexion(HostInfo hostInfo)
	{	ClientIndividualConnexion individualConnexion;
		int index;
	
		if(hostInfo.getId()==null)
		{	UUID id = UUID.randomUUID();
			hostInfo.setId("temp"+id.toString());
			hostInfo.setDirect(true);
			hostInfo.setPreferred(false);
			hostInfo.setUses(0);
		}
			
		connexionsLock.lock();
		{	individualConnexion = new ClientIndividualConnexion(this,hostInfo);
			individualConnexions.add(individualConnexion);
			index = individualConnexions.indexOf(individualConnexion);
		}
		connexionsLock.unlock();

		fireConnexionAdded(individualConnexion,index);
		individualConnexion.initSocket();
	}
	
	public void removeConnexion(ClientIndividualConnexion connexion)
	{	int index;
	
		connexionsLock.lock();
		{	index = individualConnexions.indexOf(connexion);
			connexion.finish();
			individualConnexions.remove(connexion);
		}
		connexionsLock.unlock();
		
		fireConnexionRemoved(connexion,index);
	}
	
	public void removeAllConnexions()
	{	connexionsLock.lock();
		{	Iterator<ClientIndividualConnexion> it = individualConnexions.iterator();
			while(it.hasNext())
			{	ClientIndividualConnexion connexion = it.next();
				connexion.finish();
				it.remove();
			}
		}
		connexionsLock.unlock();
	}
	
	public List<GameInfo> getGameList()
	{	List<GameInfo> result = new ArrayList<GameInfo>();

		connexionsLock.lock();
		{	for(ClientIndividualConnexion connexion: individualConnexions)
				result.add(connexion.getGameInfo());
		}
		connexionsLock.unlock();
		
		return result;
	}
	
	public void refreshConnexion(GameInfo gameInfo)
	{	ClientIndividualConnexion cx = null;
		
		connexionsLock.lock();
		{	Iterator<ClientIndividualConnexion> it = individualConnexions.iterator();
			while(cx==null && it.hasNext())
			{	ClientIndividualConnexion connexion = it.next();
				GameInfo gi = connexion.getGameInfo();
				if(gi==gameInfo)
					cx = connexion;
			}
		}
		connexionsLock.unlock();
		
		if(cx!=null)
		{	HostState state = cx.getGameInfo().getHostInfo().getState();
			if(state==HostState.UNKOWN)
				cx.retryConnexion();
		}
	}
	
	protected void connexionLost(ClientIndividualConnexion connexion)
	{	int index;
		connexionsLock.lock();
		{	index = individualConnexions.indexOf(connexion);
		}
		connexionsLock.unlock();
		
		fireConnexionGameInfoChanged(connexion,index,null);
		if(activeConnexion==connexion)
			fireConnexionActiveConnexionLost(connexion,index);
	}
	
	/////////////////////////////////////////////////////////////////
	// RECEIVED MESSAGES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// NOTE don't remember what this lock is for...
	private Lock updateLock = new ReentrantLock();

	public void gameInfoChanged(ClientIndividualConnexion connexion, String oldId)
	{	int index;
		
		connexionsLock.lock();
		{	index = individualConnexions.indexOf(connexion);
			if(oldId!=null)
			{	// look for an existing connexion with the same new id
				List<ClientIndividualConnexion> list = new ArrayList<ClientIndividualConnexion>(individualConnexions);
				Iterator<ClientIndividualConnexion> it = list.iterator();
				HostInfo hi1 = connexion.getGameInfo().getHostInfo();
				String id1 = hi1.getId();
				boolean found = false;
				while(!found && it.hasNext())
				{	ClientIndividualConnexion cx = it.next();
					HostInfo hi2 = cx.getGameInfo().getHostInfo();
					String id2 = hi2.getId();
					if(id1.equals(id2) && cx!=connexion)
					{	found = true;
						hi1.setPreferred(hi2.isPreferred());
						hi1.setUses(hi2.getUses());
						removeConnexion(cx);
					}
				}
			}
		}
		connexionsLock.unlock();

		updateLock.lock();
		{	fireConnexionGameInfoChanged(connexion,index,oldId);
		}		
		updateLock.unlock();
	}

	public void profilesChanged(ClientIndividualConnexion connexion)
	{	int index;
		
		connexionsLock.lock();
		{	index = individualConnexions.indexOf(connexion);
		}
		connexionsLock.unlock();
		
		updateLock.lock();
		{	fireConnexionProfilesChanged(connexion,index);
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
		activeConnexion.writeMessage(message);
	}
	
	public void requestGameInfos()
	{	String id = Configuration.getConnexionsConfiguration().getHostId();
		NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_GAME_INFO,id);
		List<ClientIndividualConnexion> list;
		
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
			connexion.writeMessage(message);
	}
	
	public void exitGame()
	{	removeAllConnexions();
		Configuration.getConnexionsConfiguration().setClientConnexion(null);
	}
	
	public void entersPlayerSelection(GameInfo gameInfo)
	{	List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getGameInfo()==gameInfo)
			{	activeConnexion = connexion;
//TODO en fait ça devrait être une requête, à valider par le serveur...			
				connexion.setState(ClientState.SELECTING_PLAYERS);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERING_PLAYERS_SELECTION,true);
				connexion.writeMessage(message);
			}
			else
			{	connexion.setState(ClientState.INTERESTED_ELSEWHERE);
				NetworkMessage message = new NetworkMessage(MessageName.ENTERING_PLAYERS_SELECTION,false);
				connexion.writeMessage(message);
			}
		}
	}
	
	public void exitPlayersSelection()
	{	List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	activeConnexion = null;
				connexion.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.EXITING_PLAYERS_SELECTION,true);
				connexion.writeMessage(message);
			}
			else
			{	connexion.setState(ClientState.SELECTING_GAME);
				NetworkMessage message = new NetworkMessage(MessageName.EXITING_PLAYERS_SELECTION,false);
				connexion.writeMessage(message);
			}
		}
	}
	
	// TODO must handle this distinction between local and remote players, one way or another...
	public void requestPlayersAdd(Profile profile)
	{	profile.setReady(false);
		List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	// TODO when profiles are sent, the portraits must be reloaded (images don't go through streams)
					NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_ADD,profile);
					connexion.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersRemove(Profile profile)
	{	List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_REMOVE,profile);
					connexion.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersChangeColor(Profile profile)
	{	List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_CHANGE_COLOR,profile);
					connexion.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersChangeHero(Profile profile)
	{	List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	if(!profile.isRemote())
				{	NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_CHANGE_HERO,profile);
					connexion.writeMessage(message);
				}
				else
				{	//TODO error (?)
				}
			}
		}
	}

	public void requestPlayersSet(int index, Profile profile)
	{	profile.setReady(false);
		List<ClientIndividualConnexion> list;
	
		connexionsLock.lock();
		{	list  = new ArrayList<ClientIndividualConnexion>(individualConnexions);
		}
		connexionsLock.unlock();
		
		for(ClientIndividualConnexion connexion: list)
		{	if(connexion.getState()==ClientState.SELECTING_PLAYERS)
			{	Profile oldProfile = connexion.getPlayerProfiles().get(index);
				if(!profile.isRemote() && !oldProfile.isRemote())
				{	List<Profile> data = new ArrayList<Profile>(Arrays.asList(oldProfile,profile));		
					NetworkMessage message = new NetworkMessage(MessageName.REQUESTING_PLAYERS_SET,data);
					connexion.writeMessage(message);
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
		
		connexionsLock.lock();
		{	activeConnexion.setState(state);
			activeConnexion.writeMessage(message);
		}
		connexionsLock.unlock();
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public void tournamentStarted(ClientIndividualConnexion connexion, AbstractTournament tournament)
	{	if(connexion==activeConnexion)
		{	fireConnexionTournamentStarted(tournament);
			if(connexion.getState()==ClientState.WAITING_TOURNAMENT)
				connexion.setState(ClientState.BROWSING_TOURNAMENT);
			else
				connexion.setState(ClientState.SELECTING_GAME);
		}
	}

	public StatisticRound getRoundStats()
	{	StatisticRound result = activeConnexion.getRoundStats();
		return result;
	}
	
	public Double getZoomCoef()
	{	return activeConnexion.getZoomCoef();
	}
	
	public void loadingComplete()
	{	activeConnexion.loadingComplete();
	}
	
	public void sendControlSettings(List<ControlSettings> controlSettings)
	{	activeConnexion.sendControlSettings(controlSettings);
	}
	
	/////////////////////////////////////////////////////////////////
	// LISTENERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<ClientGeneralConnexionListener> listeners = new ArrayList<ClientGeneralConnexionListener>();
	private Lock listenersLock = new ReentrantLock();
	
	public void addListener(ClientGeneralConnexionListener listener)
	{	listenersLock.lock();
		{	if(!listeners.contains(listener))
				listeners.add(listener);
		}
		listenersLock.unlock();
	}
	
	public void removeListener(ClientGeneralConnexionListener listener)
	{	listenersLock.lock();
		{	listeners.remove(listener);
		}
		listenersLock.unlock();
	}
	
	private void fireConnexionAdded(ClientIndividualConnexion connexion, int index)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionAdded(connexion,index);
	}

	private void fireConnexionRemoved(ClientIndividualConnexion connexion, int index)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionRemoved(connexion,index);
	}

	private void fireConnexionGameInfoChanged(ClientIndividualConnexion connexion, int index, String oldId)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionGameInfoChanged(connexion,index,oldId);
	}

	private void fireConnexionActiveConnexionLost(ClientIndividualConnexion connexion, int index)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionActiveConnexionLost(connexion,index);
	}

	private void fireConnexionProfilesChanged(ClientIndividualConnexion connexion, int index)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionProfilesChanged(connexion,index);
	}

	private void fireConnexionTournamentStarted(AbstractTournament tournament)
	{	List<ClientGeneralConnexionListener> list;
		listenersLock.lock();
		{	list = new ArrayList<ClientGeneralConnexionListener>(listeners);
		}
		listenersLock.unlock();
		for(ClientGeneralConnexionListener listener: list)
			listener.connexionTournamentStarted(tournament);
	}
}
