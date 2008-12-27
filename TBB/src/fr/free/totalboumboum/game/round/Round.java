package fr.free.totalboumboum.game.round;

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
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.player.PlayerLocation;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.statistics.StatisticEvent;
import fr.free.totalboumboum.game.statistics.StatisticHolder;
import fr.free.totalboumboum.game.statistics.StatisticRound;

public class Round implements StatisticHolder
{
	public Round(Match match)
	{	this.match = match;
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
	// GAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean roundOver = false;
	
	public void loadStepOver()
	{	if(panel!=null)
			panel.loadStepOver();		
	}
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	stats = new StatisticRound(this);
		stats.initStartDate();
		remainingPlayers = getProfiles().size();
		for(int i=0;i<remainingPlayers;i++)
			playersStatus.add(new Boolean(true));
		hollowLevel.getZone().makeMatrix();
	}
	
	public boolean isOver()
	{	return roundOver;
	}
	
	public void cancel()
	{	// TODO à compléter
		match.cancel();
	}
	
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	loop = new Loop(this);
			Thread animator = new Thread(loop);
			animator.start();
//			loop.init();
		}
	}
	
	public void finish()
	{	// loop
		loop.finish();
		loop = null;
		// level description
		hollowLevel.finish();
		hollowLevel = null;
		// misc
		match = null;
		panel = null;
		playersStatus.clear();
		stats = null;
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}

	/////////////////////////////////////////////////////////////////
	// LOOP 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Loop loop = null;
	
	public Loop getLoop()
	{	return loop;
	}	
	
	public void loopOver()
	{	match.roundOver();
		if(panel!=null)
		{	panel.roundOver();
			stats.initEndDate();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats;

	public StatisticRound getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int remainingPlayers;
	private final ArrayList<Boolean> playersStatus = new ArrayList<Boolean>();
	private boolean randomLocations;

	public boolean getRandomLocations()
	{	return randomLocations;
	}
	public void setRandomLocations(boolean randomLocations)
	{	this.randomLocations = randomLocations;
	}

	public ArrayList<Boolean> getPlayersStatus()
	{	return playersStatus;		
	}
	
	public Set<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		Iterator<Entry<Integer,PlayerLocation[]>> it = hollowLevel.getPlayers().getLocations().entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,PlayerLocation[]> entry = it.next();
			result.add(entry.getKey());
		}
		return result;			
	}
	
	public ArrayList<Profile> getProfiles()
	{	return match.getProfiles();
	}	
	
	public void playerOut(int index)
	{	if(!roundOver)
		{	remainingPlayers --;
			playersStatus.set(index,new Boolean(false));
		}
	}
	public void updateTime(long time)
	{	if(!roundOver)
		{	stats.updateTime(time,this);			
			if(getLimits().testLimit(this))
				closeGame();
//			else TODO à voir quand on s'occupera d'afficher les points en temps réel dans la GUI
//				stats.computePoints(getPointProcessor());
		}
	}
	
	public void cancelGame()
	{	getLimits().selectLimit(0);
		closeGame();
	}
	
	public void closeGame()
	{	roundOver = true;
		stats.finalizeTime(this);
		float[] points = limits.processPoints(this);
		stats.setPoints(points);
		celebrate();		
	}
	private void celebrate()
	{	loop.initCelebrationDelay();
		ArrayList<Integer> winners = getWinners();
		// celebration time !
		for(int i=0;i<getProfiles().size();i++)
		{	if(winners.contains(new Integer(i)))	
				loop.reportVictory(i);
			else
				loop.reportDefeat(i);
		}
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
	
	public ArrayList<Integer> getWinners()
	{	ArrayList<Integer> result = new ArrayList<Integer>();
		float[] points = stats.getPoints();
		int[] ranks = getRanks(points);
		for(int i=0;i<ranks.length;i++)
			if(ranks[i]==1 && points[i]>0)
				result.add(i);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private RoundRenderPanel panel;
	
	public void setPanel(RoundRenderPanel panel)
	{	this.panel = panel;
	}
	public RoundRenderPanel getPanel()
	{	return panel;	
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	if(!isOver())
		{	stats.addStatisticEvent(event);
			//stats.computePoints(getPointProcessor());
		}
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

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Limits<RoundLimit> limits;

	public Limits<RoundLimit> getLimits()
	{	return limits;
	}
	public void setLimits(Limits<RoundLimit> limits)
	{	this.limits = limits;
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
	// HOLLOW LEVEL			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private HollowLevel hollowLevel;
	
	public HollowLevel getHollowLevel()
	{	return hollowLevel;	
	}
	public void setHollowLevel(HollowLevel hollowLevel)
	{	this.hollowLevel = hollowLevel;	
	}

	
	public Round copy()
	{	Round result = new Round(match);
		result.setNotes(notes);
		result.setLimits(limits);
		result.setHollowLevel(hollowLevel.copy());
		result.setAuthor(author);
		result.setName(name);
		result.setRandomLocations(randomLocations);
		return result;
	}
	
	public void setMatch(Match match)
	{	this.match = match;	
	}
}
