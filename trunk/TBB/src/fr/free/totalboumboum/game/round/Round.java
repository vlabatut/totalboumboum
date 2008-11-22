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

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.configuration.profile.Profile;
import fr.free.totalboumboum.engine.container.level.HollowLevel;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.game.limit.Limits;
import fr.free.totalboumboum.game.limit.RoundLimit;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.points.PlayerPoints;
import fr.free.totalboumboum.game.points.PointsProcessor;
import fr.free.totalboumboum.game.statistics.StatisticEvent;
import fr.free.totalboumboum.game.statistics.StatisticRound;

public class Round
{
	public Round(Match match)
	{	this.match = match;
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
	{	stats.init(this);
		remainingPlayers = getProfiles().size();
		for(int i=0;i<remainingPlayers;i++)
			playersInGame.add(new Boolean(true));
		hollowLevel.getZone().makeMatrix();
	}
	
	public boolean isOver()
	{	return roundOver;
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
		playersInGame.clear();
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
			panel.roundOver();
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private StatisticRound stats = new StatisticRound();

	public StatisticRound getStats()
	{	return stats;
	}

	/////////////////////////////////////////////////////////////////
	// PLAY MODE	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PlayMode playMode;
	
	public PlayMode getPlayMode()
	{	return playMode;	
	}
	public void setPlayMode(PlayMode playMode)
	{	this.playMode = playMode;	
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private Match match;
	
	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private int remainingPlayers;
	private final ArrayList<Boolean> playersInGame = new ArrayList<Boolean>();

	public ArrayList<Profile> getProfiles()
	{	return match.getProfiles();
	}	
	
	public void playerOut(int index)
	{	if(!roundOver)
		{	remainingPlayers --;
			playersInGame.set(index,new Boolean(false));
			if(remainingPlayers<2 /*&& getPlayMode()==PlayMode.SURVIVAL*/)
				closeGame();		
		}
	}
	public void updateTime(long time)
	{	if(!roundOver)
		{	stats.updateTime(time);			
//			if(getTimeLimit()>0 && time>=getTimeLimit()/getConfiguration().getSpeedCoeff())
			int limit = getLimits().testLimits(stats);
			if(limit>=0)
			{	// close game
				roundOver = true;
				stats.finish(loop.getTotalTime());
				stats.computePoints(getPointProcessor());
				if(limit>=0 && limit<getProfiles().size())
					stats.setWinner(limit);
				celebrate();		
			}
			else
				stats.computePoints(getPointProcessor());
		}
	}
	public void closeGame()
	{	roundOver = true;
		stats.finish(loop.getTotalTime());
		stats.computePoints(getPointProcessor());
		celebrate();		
	}
	private void celebrate()
	{	loop.initCelebrationDelay();
		ArrayList<PlayerPoints> winners = stats.getWinners();
		// indexes of the winners
		ArrayList<Integer> winnersIndex = new ArrayList<Integer>();
		{	Iterator<PlayerPoints> i = winners.iterator();
			while(i.hasNext())
			{	PlayerPoints pp = i.next();
				winnersIndex.add(new Integer(pp.getIndex()));
			}
		}
		// celebration time !
		for(int i=0;i<getProfiles().size();i++)
		{	if(winnersIndex.contains(new Integer(i)))	
				loop.reportVictory(i);
			else
				loop.reportDefeat(i);
		}
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
		{	stats.addEvent(event);
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
	// POINTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	private PointsProcessor pointProcessor;

	public void setPointProcessor(PointsProcessor pointProcessor)
	{	this.pointProcessor = pointProcessor;
	}
	public PointsProcessor getPointProcessor()
	{	return pointProcessor;
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
		result.setPlayMode(playMode);
		result.pointProcessor = pointProcessor;
		result.setHollowLevel(hollowLevel.copy());
		return result;
	}
	
	public void setMatch(Match match)
	{	this.match = match;	
	}
}
