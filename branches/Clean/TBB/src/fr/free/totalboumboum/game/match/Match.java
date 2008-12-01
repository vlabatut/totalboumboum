package fr.free.totalboumboum.game.match;

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
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.game.statistics.StatisticHolder;
import fr.free.totalboumboum.game.statistics.StatisticMatch;
import fr.free.totalboumboum.game.statistics.StatisticRound;
import fr.free.totalboumboum.game.tournament.AbstractTournament;

public class Match implements StatisticHolder
{	
	public Match(AbstractTournament tournament)
	{	this.tournament = tournament;
	}
	
	/////////////////////////////////////////////////////////////////
	// TOURNAMENT		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private AbstractTournament tournament;
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean matchOver = false;
	
	public void init(ArrayList<Profile> profiles)
	{	// are rounds in random order ?
    	if(randomOrder)
    		randomizeRounds();
		// 
		// profiles
    	int i = 1;
    	this.profiles.addAll(profiles);
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
		// rounds
		iterator = rounds.iterator();
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
		{	Round round = iterator.next();
			currentRound = round.copy();
			currentRound.init();
		}
	}

	public boolean isOver()
	{	return matchOver;
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
	public ArrayList<Round> getRound()
	{	return rounds;	
	}
	public void setRounds(ArrayList<Round> rounds)
	{	this.rounds.addAll(rounds);			
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
		for(int i=1;i<=GameConstants.MAX_PROFILES_COUNT;i++)
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

	public int[] getRanks(float[] pts)
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
	
	public int[] getOrderedPlayers()
	{	float[] pts;
		if(isOver())
			pts = stats.getPoints();
		else
			pts = stats.getTotal();
		int[] result = new int[getProfiles().size()];
		int[] ranks = getRanks(pts);
		int done = 0;
		for(int i=1;i<=result.length;i++)
		{	for(int j=0;j<ranks.length;j++)
			{	if(ranks[j]==i)
				{	result[done] = j;
					done++;
				}
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// ROUNDS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Round currentRound;
	private Iterator<Round> iterator;
	
	public Round getCurrentRound()
	{	return currentRound;	
	}
	public void roundOver()
	{	// stats
		StatisticRound statsRound = currentRound.getStats();
		stats.addStatisticRound(statsRound);
		// iterator
		if(!iterator.hasNext())
		{	if(randomOrder)
				randomizeRounds();
			iterator = rounds.iterator();		
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
		iterator = null;
		rounds.clear();
		// limits
		limits.finish();
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
	private MatchRenderPanel panel;
	
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
