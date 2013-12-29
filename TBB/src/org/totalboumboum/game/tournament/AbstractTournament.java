package org.totalboumboum.game.tournament;

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
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
 * General representation of a tournament.
 * 
 * @author Vincent Labatut
 */
public abstract class AbstractTournament implements StatisticHolder, Serializable
{	/** Id */
	private static final long serialVersionUID = 1L;

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the tournament has begun */
	protected boolean begun = false;
	
	/**
	 * Initializes this tournament,
	 * before actually starting it.
	 */
	public abstract void init();
	
	/**
	 * Goes to the next stage in
	 * this tournament.
	 */
	public abstract void progress();
	
	/**
	 * Terminates this tournament.
	 */
	public abstract void finish();
	
	/**
	 * Cancels an ongoing tournament.
	 */
	public void cancel()
	{	// TODO à compléter (sauver stats?)
		tournamentOver = true;
	}
	
	/**
	 * Returns {@code true} iff
	 * this tournament has begun.
	 * 
	 * @return
	 * 		{@code true} iff the tournament has begun.
	 */
	public boolean hasBegun()
	{	return begun;	
	}

	/**
	 * Used to come back to the first match,
	 * when browsing statistics (<i>a posteriori</i>).
	 */
	public void rewind()
	{	currentIndex = 0;
		for(Match match: playedMatches)
			match.rewind();
		if(playedMatches.isEmpty())
			currentMatch = null;
		else
			currentMatch = playedMatches.get(0);
	}
	
	/**
	 * Goes to the previous match in
	 * this tournament. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void regressStat()
	{	currentIndex--;
		currentMatch = playedMatches.get(currentIndex);
	}

	/**
	 * Goes to the next match in
	 * this tournament. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void progressStat()
	{	currentIndex++;
		currentMatch = playedMatches.get(currentIndex);
	}
	
	/////////////////////////////////////////////////////////////////
	// OVER				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the tournament is finished */
	private boolean tournamentOver = false;

	/**
	 * Indicates if the tournament is finished.
	 * 
	 * @return
	 * 		{@code true} iff the tournament is finished.
	 */
	public boolean isOver()
	{	return tournamentOver;
	}
	
	/**
	 * Sets the tournament to finished.
	 * 
	 * @param tournamentOver
	 * 		Indicates if the tournament is finished.
	 */
	public void setOver(boolean tournamentOver)
	{	this.tournamentOver = tournamentOver;
	}
	
	/////////////////////////////////////////////////////////////////
	// MATCHES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Match currently played */
	protected Match currentMatch;
	/** Position of the match currently played (or leg, for the cup tournament) */
	protected int currentIndex;
	/** List of matches already played */
	protected final List<Match> playedMatches = new ArrayList<Match>();
	
	/**
	 * Returns the currently ongoing match.
	 * 
	 * @return
	 * 		The current match for this tournament.
	 */
	public Match getCurrentMatch()
	{	return currentMatch;
	}
	
	/**
	 * Returns the index of the
	 * match currently played.
	 * 
	 * @return
	 * 		Index of the current match.
	 */
	public int getCurrentIndex()
	{	return currentIndex;	
	}

	/**
	 * Method called when a match is
	 * finished.
	 */
	public abstract void matchOver();
	
	/**
	 * Method called when a round is
	 * finished.
	 */
	public abstract void roundOver();

	/**
	 * Indicates if the specified match
	 * corresponds to the first match played
	 * in this tournament.
	 * 
	 * @param match
	 * 		Match to be checked.
	 * @return
	 * 		{@code true} iff the specified match is the first one of the tournament.
	 */
	public final boolean isFirstMatch(Match match)
	{	Match firstMatch = playedMatches.get(0);
		boolean result = firstMatch == match;
		
		return result;
	}
	
	/**
	 * Indicates if the specified match
	 * corresponds to the last match played
	 * in this tournament.
	 * 
	 * @param match
	 * 		Match to be checked.
	 * @return
	 * 		{@code true} iff the specified match is the last one played during the tournament.
	 */
	public final boolean isLastPlayedMatch(Match match)
	{	Match lastMatch = playedMatches.get(playedMatches.size()-1);
		boolean result = lastMatch==match;
		
		return result;
	}

	/**
	 * Returns the list of matches already played
	 * (or at least started).
	 * 
	 * @return
	 * 		List of played matches.
	 */
	public List<Match> getPlayedMatches()
	{	return playedMatches;
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of players participating to the tournament */
	protected List<Profile> profiles = new ArrayList<Profile>();

	/**
	 * Set the list of players participating 
	 * to the tournament
	 * 
	 * @param profiles
	 * 		List of profiles.
	 */
	public void setProfiles(List<Profile> profiles)
	{	this.profiles.clear();
		this.profiles.addAll(profiles);		
	}
	
	@Override
	public List<Profile> getProfiles()
	{	return profiles;	
	}
	
	/**
	 * Checks if the current confrontation contains artificial agents.
	 * 
	 * @return
	 * 		{@code true} iff the confrontation has at least one artificial agent.
	 */
	public boolean hasAi()
	{	List<Profile> profiles = getProfiles();
		Iterator<Profile> it = profiles.iterator();
		boolean result = false;
		while(it.hasNext() && !result)
		{	Profile profile = it.next();
			result = profile.hasAi();
		}
		return result;
	}

	/**
	 * Returns the profile whose id corresponds
	 * to the specified one. If not player
	 * participating to this tournament has this
	 * id, then the method returns {@code null}.
	 * 
	 * @param id
	 * 		The id of interest.
	 * @return
	 * 		The corresponding profile, or {@code null} if
	 * 		the player is not involved in this tournament.
	 */
	public Profile getProfileById(String id)
	{	Profile result = null;
		Iterator<Profile> it = profiles.iterator();
		while(result==null && it.hasNext())
		{	Profile profile = it.next();
			String id0 = profile.getId();
			if(id0.equals(id))
				result = profile;
		}
		return result;
	}
	
	/**
	 * Returns the various numbers of players
	 * allowed for this tournament.
	 * 
	 * @return
	 * 		A set of integers, each one corresponding 
	 * 		to an allowed number of players for this tournament.
	 */
	public abstract Set<Integer> getAllowedPlayerNumbers();
	
	@Override
	public List<Boolean> getPlayersStatus()
	{	// useless here
		return null;
	}
	
	/**
	 * Loads only the portraits of the players
	 * (for GUI purposes).
	 * 
	 * @throws ParserConfigurationException
	 * 		Problem while loading the portraits.
	 * @throws SAXException
	 * 		Problem while loading the portraits.
	 * @throws IOException
	 * 		Problem while loading the portraits.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the portraits.
	 */
	public void reloadPortraits() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException
	{	for(Profile p: profiles)
			ProfileLoader.reloadPortraits(p);
	}

	/////////////////////////////////////////////////////////////////
	// NAME			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name of this tournament */
	protected String name = "N/A";

	/**
	 * Returns the Name of this tournament
	 * 
	 * @return
	 * 		The name of this tournament.
	 */
	public String getName()
	{	return name;
	}
	
	/**
	 * Changes the name of this tournament.
	 *  
	 * @param name
	 * 		New name for this tournament.
	 */
	public void setName(String name)
	{	this.name = name;
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Statistics of this tournament */
	protected StatisticTournament stats;

	@Override
	public StatisticTournament getStats()
	{	return stats;
	}

	/**
	 * Records a summary of the stats of this tournament as a text file.
	 * 
	 * @throws FileNotFoundException 
	 * 		Problem while accessing the stats file.
	 */
	protected abstract void recordStatsAsText() throws FileNotFoundException;
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel displaying this tournament data */
	transient protected TournamentRenderPanel panel;
	
	/**
	 * Change the panel displaying this tournament.
	 * 
	 * @param panel
	 * 		The new panel.
	 */
	public void setPanel(TournamentRenderPanel panel)
	{	this.panel = panel;
	}
	
	/**
	 * Returns the panel displaying this tournament.
	 * 
	 * @return
	 * 		The panel associated to this tournament.
	 */
	public TournamentRenderPanel getPanel()
	{	return panel;	
	}

	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Author of the tournament */
	private String author;
	
	/**
	 * Returns the author of the tournament.
	 * 
	 * @return
	 * 		Name of the author of the tournament.
	 */
	public String getAuthor()
	{	return author;
	}
	
	/**
	 * Changes the author of the tournament.
	 * 
	 * @param author
	 * 		New author for this tournament.
	 */
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Notes associated to this tournament */
	private final List<String> notes = new ArrayList<String>();

	/**
	 * Changes the notes associated to this tournament.
	 * 
	 * @param notes
	 * 		New notes for this tournament.
	 */
	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	
	/**
	 * Returns the notes associated to this tournament.
	 * 
	 * @return
	 * 		A list of texts.
	 */
	public List<String> getNotes()
	{	return notes;
	}
}
