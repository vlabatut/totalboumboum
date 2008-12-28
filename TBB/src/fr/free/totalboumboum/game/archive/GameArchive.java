package fr.free.totalboumboum.game.archive;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.sql.Date;
import java.util.ArrayList;

public class GameArchive
{
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	private TournamentType type;
	
	public void setName(String name)
	{	this.name = name;
	}
	public String getName()
	{	return name;
	}
	
	public void setType(TournamentType type)
	{	this.type = type;
	}
	public TournamentType getType()
	{	return type;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYED			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int matches;
	private int rounds;
	
	public void setPlayedMatches(int matches)
	{	this.matches = matches;
	}
	public int getPlayedMatches()
	{	return matches;
	}
	
	public void setPlayedRounds(int rounds)
	{	this.rounds = rounds;
	}
	public int getPlayedRounds()
	{	return rounds;
	}

	/////////////////////////////////////////////////////////////////
	// DATES			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Date start;
	private Date save;
	
	public void setStartDate(Date start)
	{	this.start = start;
	}
	public Date getStartDate()
	{	return start;
	}
	
	public void setSaveDate(Date save)
	{	this.save = save;
	}
	public Date getSaveDate()
	{	return save;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS				/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private ArrayList<String> players;
	
	public void setPlayers(ArrayList<String> players)
	{	this.players = players;
	}
	public ArrayList<String> getPlayers()
	{	return players;
	}
}
