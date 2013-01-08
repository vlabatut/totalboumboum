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
import java.util.LinkedList;
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
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.xml.sax.SAXException;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class Match implements StatisticHolder, Serializable
{	private static final long serialVersionUID = 1L;

	public Match(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private String name;
	
	public String getName()
	{	return name;
	}

	public void setName(String name)
	{	this.name = name;
	}

	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	
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
	
	public void init(List<Profile> profiles)
	{	begun = true;
		
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
		// rounds
    	currentIndex = 0;
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

	private void randomizeRounds()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(rounds,random);
	}
	
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	Round round = rounds.get(currentIndex);
			currentIndex++;
			currentRound = round.copy();
			currentRound.init();
		}
	}

	public boolean isOver()
	{	return matchOver;
	}
	
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
	}

	/**
	 * Goes to the previous match in
	 * this match. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void regressStat()
	{	currentIndex--;
	}

	/**
	 * Goes to the next match in
	 * this match. Used for stat
	 * browsing, not for actually playing
	 * the tournament.
	 */
	public void progressStat()
	{	currentIndex++;
	}

	/////////////////////////////////////////////////////////////////
	// LIMITS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<MatchLimit> limits;

	public Limits<MatchLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<MatchLimit> limits)
	{	this.limits = limits;
	}
			
	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean randomOrder;
	private List<Round> rounds = new ArrayList<Round>();

	public boolean getRandomOrder()
	{	return randomOrder;
	}
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}
	
	public void addRound(Round round)
	{	rounds.add(round);		
	}
	public List<Round> getRounds()
	{	return rounds;	
	}
	public void setRounds(List<Round> rounds)
	{	this.rounds.addAll(rounds);			
	}
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
	{	// TODO a corriger
		Round firstRound = rounds.get(0);
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
	{	// TODO a corriger
		Round lastRound = null;
		Iterator<Round> it = new LinkedList<Round>(rounds).descendingIterator();
		while(it.hasNext() && lastRound==null)
		{	Round temp = it.next();
			if(temp.isOver())
				lastRound = temp;
		}
		boolean result = round==lastRound;
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final List<Profile> profiles = new ArrayList<Profile>();

	public void addProfile(Profile profile)
	{	profiles.add(profile);
	}
	public List<Profile> getProfiles()
	{	return profiles;	
	}
	
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

	
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = CombinatoricsTools.getRanks(points);
			ranks2 = CombinatoricsTools.getRanks(total);
		}
		else
		{	ranks = CombinatoricsTools.getRanks(total);
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
	private Round currentRound;
	private int currentIndex;
	
	public Round getCurrentRound()
	{	return currentRound;	
	}
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
	private StatisticMatch stats;
	
	public StatisticMatch getStats()
	{	return stats;
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
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private MatchRenderPanel panel;
	
	public void setPanel(MatchRenderPanel panel)
	{	this.panel = panel;
	}
	public MatchRenderPanel getPanel()
	{	return panel;	
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
