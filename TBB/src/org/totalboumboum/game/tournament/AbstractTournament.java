package org.totalboumboum.game.tournament;

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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public abstract class AbstractTournament implements StatisticHolder, Serializable
{	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected boolean begun = false;
	
	public abstract void init();
	public abstract void progress();
	public abstract void finish();
	
	public void cancel()
	{	// TODO à compléter (sauver stats?)
		tournamentOver = true;
	}
	
	public boolean hasBegun()
	{	return begun;	
	}

	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean tournamentOver = false;

	public boolean isOver()
	{	return tournamentOver;
	}
	public void setOver(boolean tournamentOver)
	{	this.tournamentOver = tournamentOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCHES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public abstract Match getCurrentMatch();
	public abstract void matchOver();
	public abstract void roundOver();

	/////////////////////////////////////////////////////////////////
	// PLAYERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected List<Profile> profiles = new ArrayList<Profile>();

	public void setProfiles(List<Profile> profiles)
	{	this.profiles.clear();
		this.profiles.addAll(profiles);		
	}
	
	public List<Profile> getProfiles()
	{	return profiles;	
	}

	public abstract Set<Integer> getAllowedPlayerNumbers();
	
	@Override
	public List<Boolean> getPlayersStatus()
	{	// useless here
		return null;
	}
	
	public void reloadPortraits() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	for(Profile p: profiles)
			ProfileLoader.reloadPortraits(p);
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
	transient protected TournamentRenderPanel panel;
	
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
	private final List<String> notes = new ArrayList<String>();

	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	public List<String> getNotes()
	{	return notes;
	}
}
