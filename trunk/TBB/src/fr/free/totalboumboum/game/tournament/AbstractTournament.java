package fr.free.totalboumboum.game.tournament;

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

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.statistics.StatisticHolder;
import fr.free.totalboumboum.game.statistics.StatisticTournament;

public abstract class AbstractTournament implements StatisticHolder
{	
	/////////////////////////////////////////////////////////////////
	// GAME		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean begun = false;
	
	public abstract void init(ArrayList<Profile> selected) throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, IllegalAccessException, NoSuchFieldException, ClassNotFoundException;
	public abstract void progress();
	public abstract void finish();
	public abstract boolean isOver();
	
	public abstract boolean isReady();
	
	public boolean hasBegun()
	{	return begun;	
	}

	/////////////////////////////////////////////////////////////////
	// MATCHES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract Match getCurrentMatch();
	public abstract void matchOver();

	/////////////////////////////////////////////////////////////////
	// PLAYERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected ArrayList<Profile> profiles = new ArrayList<Profile>();
	protected int minPlayerNumber;
	protected int maxPlayerNumber;

	public void addProfiles(ArrayList<Profile> profiles)
	{	profiles.addAll(profiles);		
	}
	
	public ArrayList<Profile> getProfiles()
	{	return profiles;	
	}

	public abstract void updatePlayerNumber();
	
	public int getMinPlayerNumber()
	{	return minPlayerNumber;			
	}
	public int getMaxPlayerNumber()
	{	return maxPlayerNumber;			
	}

	@Override
	public ArrayList<Boolean> getPlayersStatus()
	{	// useless here
		return null;
	}

	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected String name = "N/A";

	public String getName()
	{	return name;
	}
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected StatisticTournament stats;

	public StatisticTournament getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected TournamentRenderPanel panel;
	
	public void setPanel(TournamentRenderPanel panel)
	{	this.panel = panel;
	}
	public TournamentRenderPanel getPanel()
	{	return panel;	
	}

	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String author;
	
	public String getAuthor()
	{	return author;
	}
	
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<String> notes = new ArrayList<String>();

	public void setNotes(ArrayList<String> notes)
	{	this.notes.addAll(notes);
	}
	public ArrayList<String> getNotes()
	{	return notes;
	}
}
