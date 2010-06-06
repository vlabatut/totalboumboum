package org.totalboumboum.game.round;

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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.configuration.profile.PredefinedColor;
import org.totalboumboum.configuration.profile.Profile;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.SimulationLoop;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.limit.LimitTime;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.replay.Replay;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.calculus.CalculusTools;
import org.xml.sax.SAXException;

public class Round implements StatisticHolder, Serializable
{	private static final long serialVersionUID = 1L;

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
	
	public void simulationStepOver()
	{	if(panel!=null)
			panel.simulationStepOver();		
	}
	
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	//stats
		stats = new StatisticRound(this);
		stats.initStartDate();
		
		// players
		remainingPlayers = getProfiles().size();
		for(int i=0;i<remainingPlayers;i++)
			playersStatus.add(new Boolean(true));
		
		// level
		hollowLevel.makeZone();
		
		// current points
		currentPoints = new float[getProfiles().size()];
		Arrays.fill(currentPoints,0);
	}
	
	public boolean isOver()
	{	return roundOver;
	}
	
	public void cancel()
	{	// TODO � compl�ter
		match.cancel();
	}
	
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	if(replayed)
				loop = new ReplayLoop(this);
			else
				loop = new ServerLoop(this);
		
			// recording
			if(Configuration.getEngineConfiguration().isRecordRounds() && !replayed)
			{	replay = new Replay(this);
				RoundVariables.replay = replay;
			}
		
			if(replayed)
				RoundVariables.replay = replay;
			
			Thread animator = new Thread(loop);
			animator.start();
//			loop.init();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// REPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Replay replay = null;
	
	public void setReplay(Replay replay)
	{	this.replay = replay;
	}
	
	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
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
		RoundVariables.level = null;
		RoundVariables.loop = null;
		RoundVariables.instance = null;
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}

	/////////////////////////////////////////////////////////////////
	// REPLAY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private boolean replayed = false;
	
	public boolean isReplayed()
	{	return replayed;
	}

	public void setReplayed(boolean replayed)
	{	this.replayed = replayed;		
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private Loop loop = null;
	
	public Loop getLoop()
	{	return loop;
	}	
	
	public void loopOver()
	{	// read stats from replay if replayed
		if(replayed)
		{	try
			{	replay.finishReading();
				StatisticRound stats = replay.getReadRoundStats();
				setStats(stats);
				roundOver = true; // ou close?
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
		}
		// init stats date only if not replayed
		else
			stats.initEndDate();
	
		// possibly not record simulated stats
		if((!(loop instanceof SimulationLoop) || Configuration.getStatisticsConfiguration().getIncludeSimulations())
		// possibly not record quick mode stats
			&& (!GameData.quickMode || Configuration.getStatisticsConfiguration().getIncludeQuickStarts())
		// don't record replay stats	
			&& !replayed)
			GameStatistics.update(stats);
		
		// possibly end replay recording
		if(Configuration.getEngineConfiguration().isRecordRounds())
		{	try
			{	RoundVariables.replay.finishWriting(stats);
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ParserConfigurationException e)
			{	e.printStackTrace();
			}
			catch (SAXException e)
			{	e.printStackTrace();
			}
		}
		
		match.roundOver();
		if(panel!=null)
		{	panel.roundOver();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats;

	public StatisticRound getStats()
	{	return stats;
	}

	public void setStats(StatisticRound stats)
	{	this.stats = stats;	
	}
	
	public void addStatisticEvent(StatisticEvent event)
	{	if(!isOver())
		{	stats.addStatisticEvent(event);
			//stats.computePoints(getPointProcessor());
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	
	public Match getMatch()
	{	return match;	
	}
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int remainingPlayers;
	private final ArrayList<Boolean> playersStatus = new ArrayList<Boolean>();
	private boolean randomLocation;

	public boolean getRandomLocation()
	{	return randomLocation;
	}
	public void setRandomLocation(boolean randomLocations)
	{	this.randomLocation = randomLocations;
	}

	public ArrayList<Boolean> getPlayersStatus()
	{	return playersStatus;		
	}
	
	public TreeSet<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		Iterator<Entry<Integer,PlayerLocation[]>> it = hollowLevel.getPlayers().getLocations().entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,PlayerLocation[]> entry = it.next();
			result.add(entry.getKey());
		}
		return result;			
	}
	
	public List<Profile> getProfiles()
	{	return match.getProfiles();
	}	
	public List<PredefinedColor> getProfilesColors()
	{	List<PredefinedColor> result = new ArrayList<PredefinedColor>();
		for(Profile p: match.getProfiles())
			result.add(p.getSpriteColor());
		return result;
	}	
	
	public void playerOut(int index)
	{	if(!roundOver)
		{	remainingPlayers --;
			playersStatus.set(index,new Boolean(false));
//System.out.println("player out "+index+"("+loop.getTotalTime()+")");			
		}
	}
	public void updateTime(long time)
	{	if(!roundOver)
		{	stats.updateTime(time,this);			
			if(getLimits().testLimit(this))
				closeGame();
			else
			{	LimitTime limit = limits.getTimeLimit();
				if(limit!=null)
					currentPoints = limit.processPoints(this);
				else
					limits.getLimit(0).processPoints(this);			
			}
		}
	}
	
	public void cancelGame()
	{	if(roundOver!=true)
		{	getLimits().selectLimit(0);
			closeGame();
		}
	}
	
	private void closeGame()
	{	roundOver = true;
		stats.finalizeTime(this);
		float[] points = limits.processPoints(this);
		stats.setPoints(points);
		celebrate();		
	}
	
	private void celebrate()
	{	loop.initCelebration();
		List<Integer> winners = getWinners();
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
	private float[] currentPoints;
	
	public float[] getCurrentPoints()
	{	return currentPoints;		
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
		{	ranks = CalculusTools.getRanks(points);
			ranks2 = CalculusTools.getRanks(total);
		}
		else
		{	ranks = CalculusTools.getRanks(currentPoints);
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
	
	public ArrayList<Integer> getWinners()
	{	float[] points = stats.getPoints();
		ArrayList<Integer> result = CalculusTools.getWinners(points);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	transient private RoundRenderPanel panel;
	
	public void setPanel(RoundRenderPanel panel)
	{	this.panel = panel;
	}
	public RoundRenderPanel getPanel()
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
		result.setRandomLocation(randomLocation);
		
		result.setReplayed(replayed);
		result.replay = replay;
		
		return result;
	}
	
	public void setMatch(Match match)
	{	this.match = match;	
	}

	/////////////////////////////////////////////////////////////////
	// SIMULATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public boolean isSimulated()
	{	AisConfiguration aisConfiguration = Configuration.getAisConfiguration();
		boolean result = aisConfiguration.getHideAllAis();
		Iterator<Profile> it = getProfiles().iterator();
		while(it.hasNext() && result)
		{	Profile profile = it.next();
			result = profile.hasAi();
		}
		return result;
	}
	
	public void simulate()
	{	if(!isOver())
		{	loop = new SimulationLoop(this);
			Thread animator = new Thread(loop);
			animator.start();
		}
	}
}
