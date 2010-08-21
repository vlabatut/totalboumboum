package org.totalboumboum.network.game;

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

import org.totalboumboum.game.tournament.TournamentType;
import org.totalboumboum.network.host.HostInfo;

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
	private Double averageScore = null;

	public void setAverageScore(Double averageScore)
	{	this.averageScore = averageScore;
	}

	public Double getAverageScore()
	{	return averageScore;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS COUNT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Integer playerCount = null;

	public void setPlayerCount(Integer playerCount)
	{	this.playerCount = playerCount;
	}

	public Integer getPlayerCount()
	{	return playerCount;	
	}
	
	/////////////////////////////////////////////////////////////////
	// ALLOWED PLAYERS 		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Set<Integer> allowedPlayers = null;

	public void setAllowedPlayers(Set<Integer> allowedPlayers)
	{	this.allowedPlayers = allowedPlayers;
	}

	public Set<Integer> getAllowedPlayers()
	{	return allowedPlayers;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT TYPE		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private TournamentType tournamentType = null;

	public void setTournamentType(TournamentType tournamentType)
	{	this.tournamentType = tournamentType;
	}

	public TournamentType getTournamentType()
	{	return tournamentType;	
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT NAME		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String tournamentName = null;

	public void setTournamentName(String tournamentName)
	{	this.tournamentName = tournamentName;
	}

	public String getTournamentName()
	{	return tournamentName;	
	}

	/////////////////////////////////////////////////////////////////
	// HOST INFO			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HostInfo hostInfo = null;

	public void setHostInfo(HostInfo hostInfo)
	{	this.hostInfo = hostInfo;
	}

	public HostInfo getHostInfo()
	{	return hostInfo;	
	}
}
