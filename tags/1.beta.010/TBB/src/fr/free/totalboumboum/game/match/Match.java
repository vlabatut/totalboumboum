package fr.free.totalboumboum.game.match;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
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
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.GameConstants;
import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.MatchLimit;
import fr.free.totalboumboum.game.rank.Ranks;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.StatisticHolder;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticRound;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

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
	private boolean matchOver = false;
	
	public void init(ArrayList<Profile> profiles)
	{	// are rounds in random order ?
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
	private ArrayList<Round> rounds = new ArrayList<Round>();

	public boolean getRandomOrder()
	{	return randomOrder;
	}
	public void setRandomOrder(boolean randomOrder)
	{	this.randomOrder = randomOrder;
	}
	
	public void addRound(Round round)
	{	rounds.add(round);		
	}
	public ArrayList<Round> getRounds()
	{	return rounds;	
	}
	public void setRounds(ArrayList<Round> rounds)
	{	this.rounds.addAll(rounds);			
	}
	public void clearRounds()
	{	rounds.clear();	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private final ArrayList<Profile> profiles = new ArrayList<Profile>();

	public void addProfile(Profile profile)
	{	profiles.add(profile);
	}
	public ArrayList<Profile> getProfiles()
	{	return profiles;	
	}
	
	public Set<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameConstants.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Round r:rounds)
		{	Set<Integer> temp = r.getAllowedPlayerNumbers();
			result.retainAll(temp);
		}
		return result;			
	}
	
	@Override
	public ArrayList<Boolean> getPlayersStatus()
	{	// useless here
		return null;
	}
	
	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////

	/**
	 * process an array of ranks, each one corresponding to a player,
	 * according to the points in input.
	 * eg: {1,3,2} means the player 0 came first, player 1 came 
	 * third and player 2 came second
	 */
	private int[] getRanks(float[] pts)
	{	int[] result = new int[getProfiles().size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;

		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	if(pts[i]<pts[j])
					result[i] = result[i] + 1;
				else if(pts[i]>pts[j])
					result[j] = result[j] + 1;
			}
		}	

		return result;
	}
	
	public Ranks getOrderedPlayers()
	{	Ranks result = new Ranks();
		// points
		float[] points = stats.getPoints();
		float[] total = stats.getTotal();
		// ranks
		int ranks[];
		int ranks2[];
		if(isOver())
		{	ranks = getRanks(points);
			ranks2 = getRanks(total);
		}
		else
		{	ranks = getRanks(total);
			ranks2 = new int[ranks.length];
			Arrays.fill(ranks2,0);
		}
		// result
		for(int i=0;i<ranks.length;i++)
		{	int rank = ranks[i];
			int rank2 = ranks2[i];
			Profile profile = getProfiles().get(i);
			ArrayList<Profile> list = result.getProfilesFromRank(rank);
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
		{	//tournament.roundOver();
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
	private final ArrayList<String> notes = new ArrayList<String>();

	public void setNotes(ArrayList<String> notes)
	{	this.notes.addAll(notes);
	}
	public ArrayList<String> getNotes()
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
		result.setNotes(notes);
		result.setLimits(limits);
		return result;
	}
}
