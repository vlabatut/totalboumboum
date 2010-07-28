package org.totalboumboum.game.network.game;

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

import java.io.Serializable;
import java.util.Set;

import org.totalboumboum.game.network.host.HostInfo;
import org.totalboumboum.game.tournament.TournamentType;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameInfo implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// AVERAGE SCORE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private double averageScore;

	public void setAverageScore(double averageScore)
	{	this.averageScore = averageScore;
	}

	public double getAverageScore()
	{	return averageScore;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS COUNT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int playerCount;

	public void setPlayerCount(int playerCount)
	{	this.playerCount = playerCount;
	}

	public int getPlayerCount()
	{	return playerCount;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ALLOWED PLAYERS 		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Set<Integer> allowedPlayers;

	public void setAllowedPlayers(Set<Integer> allowedPlayers)
	{	this.allowedPlayers = allowedPlayers;
	}

	public Set<Integer> getAllowedPlayers()
	{	return allowedPlayers;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT TYPE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentType tournamentType;

	public void setTournamentType(TournamentType tournamentType)
	{	this.tournamentType = tournamentType;
	}

	public TournamentType getTournamentType()
	{	return tournamentType;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT NAME		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String tournamentName;

	public void setTournamentName(String tournamentName)
	{	this.tournamentName = tournamentName;
	}

	public String getTournamentName()
	{	return tournamentName;	
	}

	/////////////////////////////////////////////////////////////////
	// HOST INFO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostInfo hostInfo;

	public void setHostInfo(HostInfo hostInfo)
	{	this.hostInfo = hostInfo;
	}

	public HostInfo getHostInfo()
	{	return hostInfo;	
	}
}
