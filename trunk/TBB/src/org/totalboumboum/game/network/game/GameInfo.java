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
import java.net.InetAddress;
import java.util.List;

import org.totalboumboum.game.tournament.AbstractTournament;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class GameInfo implements Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private List<Double> glickoScores;

	public void addPlayer(String id)
	{	this.id = id;
	}
	
	/*
	 * TODO
	 * player count
	 * allowed counts
	 * player g-level
	 * tournament type
	 * tournament name
	 */
	
	/////////////////////////////////////////////////////////////////
	// USE					/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int uses;

	public int getUses()
	{	return uses;
	}

	public void setUses(int uses)
	{	this.uses = uses;
	}

	public void incrementUses()
	{	uses++;
	}
	
	/////////////////////////////////////////////////////////////////
	// ADDRESS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private InetAddress lastIp;

	public InetAddress getLastIp()
	{	return lastIp;
	}

	public void setLastIp(InetAddress lastIp)
	{	this.lastIp = lastIp;
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;

	public AbstractTournament getTournament()
	{	return tournament;
	}

	public void setTournament(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
}
