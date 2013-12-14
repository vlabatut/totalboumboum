package org.totalboumboum.game.match;

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

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.RankingTools;
import org.xml.sax.SAXException;

/**
 * This class represents a match,
 * i.e. a set of rounds.
 * 
 * @author Vincent Labatut
 */
public class Match implements StatisticHolder, Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard match.
	 * 
	 * @param tournament
	 * 		Tournament this match belongs to.
	 */
	public Match(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name given by the designer of this match */
	private String name = null;
	
	/**
	 * Returns the name given by the 
	 * designer of this match.
	 * 
	 * @return
	 * 		Name of this match.
	 */
	public String getName()
	{	return name;
	}

	/**
	 * Changes the name given by the 
	 * designer of this match.
	 * 
	 * @param name
	 * 		New name of this match.
	 */
	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament containing this match */
	private AbstractTournament tournament;
	
	/**
	 * Returns the tournament 
	 * containing this match.
	 * 
	 * @return
	 * 		Tournament containing this match.
	 */
	public AbstractTournament getTournament()
	{	return tournament;	
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the match has begun */
	protected boolean begun = false;
	/** Indicates if the match is finished */
	private boolean matchOver = false;
	
	/**
	 * Initializes this match with the
	 * specified profiles.
	 * 
	 * @param profiles
	 * 		Players to participate in this match.
	 */
	public void init(List<Profile> profiles)
	{	begun = true;
		
		// rounds
		playedRounds.clear();
		currentIndex = 0;
		// are rounds in random order ?
    	if(randomOrder)
    		randomizeRounds();
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
    	
		// profiles
    	this.profiles.addAll(profiles);
/*    	
    	int i = 1;
		Iterator<Profile> it = this.profiles.iterator();
		while(it.hasNext())
		{	Profile p = it.next();
			if(p.hasAi())
				p.setControlSettingsIndex(0);
			else
			{	p.setControlSettingsIndex(i);
				i++;
			}
		}
*/		
		// stats
		stats = new StatisticMatch(this);
		stats.initStartDate();
	}
	
	/**
	 * Returns {@code true} iff
	 * this match has begun.
	 * 
	 * @return
	 * 		{@code true} iff the match has begun.
	 */
	public boolean hasBegun()
	{	return begun;	
	}

	/**
	 * Goes to the next round in this match.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while initializing the next round.
	 * @throws SecurityException
	 * 		Problem while initializing the next round.
	 * @throws ParserConfigurationException
	 * 		Problem while initializing the next round.
	 * @throws SAXException
	 * 		Problem while initializing the next round.
	 * @throws IOException
	 * 		Problem while initializing the next round.
	 * @throws ClassNotFoundException
	 * 		Problem while initializing the next round.
	 * @throws IllegalAccessException
	 * 		Problem while initializing the next round.
	 * @throws NoSuchFieldException
	 * 		Problem while initializing the next round.
	 */
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	Round round = rounds.get(currentIndex);
			currentIndex++;
			currentRound = round.copy();
			currentRound.init();
			playedRounds.add(currentRound);
		}
	}

	/**
	 * Checks if this match is over,
	 * i.e. all rounds have been played.
	 * 
	 * @return
	 * 		{@code true} iff this match is over.
	 */
	public boolean isOver()
	{	return matchOver;
	}
	
	/**
	 * Cancels the whole tournament.
	 */
	public void cancel()
	{	// TODO à compléter (stats)
		tournament.cancel();
	}
	
	/**
	 * Used to come back to the first round,
	 * when browsing statistics (<i>a posteriori</i>).
	 */
	public void rewind()
	{	currentIndex = 0;
		if(playedRounds.isEmpty())
			currentRound = null;
		else
			currentRound = playedRounds.get(0);
	}

	/**
	 * Goes to the previous round in
	 * this match. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void regressStat()
	{	currentIndex--;
		currentRound = playedRounds.get(currentIndex);
	}

	/**
	 * Goes to the next round in
	 * this match. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void progressStat()
	{	currentIndex++;
		currentRound = playedRounds.get(currentIndex);
	}

	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Limits for this match */
	private Limits<MatchLimit> limits;

	/**
	 * Gets the limits 
	 * for this match.
	 * 
	 * @return
	 * 		Limits of this match.
	 */
	public Limits<MatchLimit> getLimits()
	{	return limits;
	}

	/**
	 * Changes the limits 
	 * for this match.
	 * 
	 * @param limits
	 * 		New limits of this match.
	 */
	public void setLimits(Limits<MatchLimit> limits)
	{	this.limits = limits;
	}
			
	/////////////////////////////////////////////////////////////////
	// ROUND ORDER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the rounds should be played in a random order or not */
	private boolean randomOrder;
	
	/**
	 * Checks if the rounds should
	 * ne played in random order.
	 * 
	 * @return
	 * 		{@code true} iff the rounds must be played in random order.
	 */
	public boolean getRandomOrder()
	{	return randomOrder;
	}
	
	/**
	 * Changes the flag indicating if the 
	 * rounds should ne played in random order.
	 * 
	 * @param randomOrder
	 * 		If {@code true}, then the rounds must be played in random order.
	 */
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}
	
	/**
	 * Randomly changes the order of the rounds.
	 */
	private void randomizeRounds()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(rounds,random);
	}
	
	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of the round models (never instantiated) */
	private List<Round> rounds = new ArrayList<Round>();
	/** List of the rounds already played */
	private final List<Round> playedRounds = new ArrayList<Round>();
	
	/**
	 * Adds a round to this match.
	 * 
	 * @param round
	 * 		New round belonging to this match.
	 */
	public void addRound(Round round)
	{	rounds.add(round);		
	}
	
	/**
	 * Gets the list of original rounds
	 * constituting this match.
	 * 
	 * @return
	 * 		Original rounds of this match.
	 */
	public List<Round> getRounds()
	{	return rounds;	
	}
	
	/**
	 * Sets up the list of original rounds
	 * for this match.
	 * 
	 * @param rounds
	 * 		New rounds of this match.
	 */
	public void setRounds(List<Round> rounds)
	{	this.rounds.addAll(rounds);			
	}
	
	/**
	 * Removes all the rounds from this
	 * match.
	 */
	public void clearRounds()
	{	rounds.clear();	
	}

	/**
	 * Indicates if the specified round
	 * corresponds to the first one played
	 * in this match.
	 * 
	 * @param round
	 * 		Round to be checked.
	 * @return
	 * 		{@code true} iff the specified round is the first one of the match.
	 */
	public boolean isFirstRound(Round round)
	{	Round firstRound = playedRounds.get(0);
		boolean result = round==firstRound;
		
		return result;
	}
	
	/**
	 * Indicates if the specified round
	 * corresponds to the last one played
	 * in this match.
	 * 
	 * @param round
	 * 		Round to be checked.
	 * @return
	 * 		{@code true} iff the specified round is the last one played during this match.
	 */
	public boolean isLastPlayedRound(Round round)
	{	Round lastRound = playedRounds.get(playedRounds.size()-1);
		boolean result = round==lastRound;
		
		return result;
	}

	/**
	 * Returns the list of rounds already played
	 * (or at least started).
	 * 
	 * @return
	 * 		List of played rounds.
	 */
	public List<Round> getPlayedRounds()
	{	return playedRounds;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of profiles participating in this match */
	private final List<Profile> profiles = new ArrayList<Profile>();
	
	/**
	 * Adds a new player to this match.
	 * 
	 * @param profile
	 * 		Profile of the additional player.
	 */
	public void addProfile(Profile profile)
	{	profiles.add(profile);
	}
	
	@Override
	public List<Profile> getProfiles()
	{	return profiles;	
	}
	
	/**
	 * Returns the various numbers of players
	 * allowed for this match (which depend on
	 * the rounds contained in this match).
	 * 
	 * @return
	 * 		A set of integer values, each one representing an allowed number of players.
	 */
	public Set<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Round r:rounds)
		{	Set<Integer> temp = r.getAllowedPlayerNumbers();
			result.retainAll(temp);
		}
		return result;			
	}
	
	@Override
	public List<Boolean> getPlayersStatus()
	{	// useless here
		return null;
	}
	
	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = RankingTools.getRanks(points);
			ranks2 = RankingTools.getRanks(total);
		}
		else
		{	ranks = RankingTools.getRanks(total);
			ranks2 = new int[ranks.length];
			Arrays.fill(ranks2,0);
		}
		// result
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			int rank2 = ranks2[i];
			Profile profile = getProfiles().get(i);
			List<Profile> list = result.getProfilesFromRank(rank);
			int index = -1;
			// if no list yet : regular insertion
			if(list==null)
			{	result.addProfile(rank,profile);
				index = 0;
			}
			// if list : insert at right place considering total points
			else
			{	int j = 0;
				while(j<list.size() && index==-1)
				{	Profile profileB = list.get(j);
					int plrIdx = getProfiles().indexOf(profileB);
					int rank2B = ranks2[plrIdx];
					if(rank2<rank2B)
						index = j;
					else
						j++;
				}
				if(index==-1)
					index = j;
				list.add(index,profile);
			}			
		}
			
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Current round */
	private Round currentRound;
	/** Position of the current round in the list of rounds */
	private int currentIndex;
	
	/**
	 * Returns the round currently
	 * played.
	 * 
	 * @return
	 * 		The current round.
	 */
	public Round getCurrentRound()
	{	return currentRound;	
	}
	
	/**
	 * Returns the index of the
	 * round currently played.
	 * 
	 * @return
	 * 		Index of the current round.
	 */
	public int getCurrentIndex()
	{	return currentIndex;	
	}
	
	/**
	 * Called when the current round is over.
	 */
	public void roundOver()
	{	// stats
		StatisticRound statsRound = currentRound.getStats();
		stats.addStatisticRound(statsRound);
		// iterator
		if(currentIndex>=rounds.size())
		{	if(randomOrder)
				randomizeRounds();
			currentIndex = 0;		
		}
		// limits
		if(limits.testLimit(this))
		{	float[] points = limits.processPoints(this);
			stats.setPoints(points);
			matchOver = true;
			tournament.matchOver();
			if(panel!=null)
			{	panel.matchOver();
				stats.initEndDate();
			}
		}
		else
		{	tournament.roundOver();
			if(panel!=null)
				panel.roundOver();
		}
	}
	
	/**
	 * Allows terminating this object
	 * appropriately (memory-wise).
	 */
	public void finish()
	{	// rounds
		currentRound = null;
		rounds.clear();
		
		// limits
//		limits.finish();
		limits = null;
		
		// misc
		panel = null;
		profiles.clear();
		stats = null;
		tournament = null;
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Stats associated to this match */
	private StatisticMatch stats;
	
	@Override
	public StatisticMatch getStats()
	{	return stats;
	}
	
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Author of this match */
	private String author;
	
	/**
	 * Returns the name of the designer
	 * of this match.
	 * 
	 * @return
	 * 		Author(s) of this match.
	 */
	public String getAuthor()
	{	return author;
	}
	
	/**
	 * Changes the author of this match.
	 * 
	 * @param author
	 * 		New author of this match.
	 */
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel used to display the match */
	transient private MatchRenderPanel panel;
	
	/**
	 * Changes the panel used to
	 * display this match.
	 * 
	 * @param panel
	 * 		New panel.
	 */
	public void setPanel(MatchRenderPanel panel)
	{	this.panel = panel;
	}
	
	/**
	 * Returns the panel used to
	 * display this match.
	 * 
	 * @return
	 * 		The panel used to display this match.
	 */
	public MatchRenderPanel getPanel()
	{	return panel;	
	}

	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Notes describing this match */
	private final List<String> notes = new ArrayList<String>();

	/**
	 * Changes the notes describing this match.
	 * 
	 * @param notes
	 * 		New description.
	 */
	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	
	/**
	 * Returns the notes describing this match.
	 * 
	 * @return
	 * 		Description of this match.
	 */
	public List<String> getNotes()
	{	return notes;
	}
	
	/**
	 * Makes a copy of this match,
	 * used to clone the match and use
	 * the clone to perform the actual game.
	 * The original object is kept in case
	 * the same match needs to be played again
	 * during the same tournament.
	 * 
	 * @return
	 * 		A copy of this match.
	 */
	public Match copy()
	{	Match result = new Match(tournament);
		// rounds
		Iterator<Round> i = rounds.iterator();
		while (i.hasNext())
		{	Round round = i.next();
			Round copy = round.copy();
			copy.setMatch(result);
			result.addRound(copy);
		}
		// misc
		result.setAuthor(author);
		result.setName(name);
//		result.currentIndex = currentIndex);
		result.setNotes(notes);
		result.setLimits(limits);
		result.setRandomOrder(randomOrder);
		return result;
	}
}
