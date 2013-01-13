package org.totalboumboum.game.round;

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
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.loop.ClientLoop;
import org.totalboumboum.engine.loop.ReplayLoop;
import org.totalboumboum.engine.loop.RegularLoop;
import org.totalboumboum.engine.loop.Loop;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.SimulationLoop;
import org.totalboumboum.engine.loop.event.replay.StopReplayEvent;
import org.totalboumboum.engine.player.PlayerLocation;
import org.totalboumboum.game.limit.LimitTime;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.StatisticEvent;
import org.totalboumboum.statistics.detailed.StatisticHolder;
import org.totalboumboum.statistics.detailed.StatisticRound;
import org.totalboumboum.stream.file.replay.FileClientStream;
import org.totalboumboum.stream.file.replay.FileServerStream;
import org.totalboumboum.stream.network.client.ClientGeneralConnection;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.computing.CombinatoricsTools;
import org.totalboumboum.tools.images.PredefinedColor;
import org.xml.sax.SAXException;

/**
 * This class represents a round,
 * i.e. the smalles division of a game.
 * 
 * @author Vincent Labatut
 */
public class Round implements StatisticHolder, Serializable
{	/** Class id */
	private static final long serialVersionUID = 1L;

	/**
	 * Builds a standard round.
	 * 
	 * @param match
	 * 		Match this round belongs to.
	 */
	public Round(Match match)
	{	this.match = match;
	}
	
	/////////////////////////////////////////////////////////////////
	// NAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Name given by the designer of this round */
	private String name;
	
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
	// GAME 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Indicates if the round is finished */
	private boolean roundOver = false;
	
	/**
	 * Called when one step is done,
	 * during the loading phase.
	 */
	public void loadStepOver()
	{	if(panel!=null)
			panel.loadStepOver();		
	}
	
	/**
	 * Called when one step is done,
	 * during the simulation phase.
	 */
	public void simulationStepOver()
	{	if(panel!=null)
			panel.simulationStepOver();		
	}
	
	/**
	 * Initializes this round.
	 * Sprites are not loaded yet, though.
	 * 
	 * @throws ParserConfigurationException
	 * 		Problems while loading the round data. 
	 * @throws SAXException 
	 * 		Problems while loading the round data. 
	 * @throws IOException 
	 * 		Problems while loading the round data. 
	 * @throws ClassNotFoundException 
	 * 		Problems while loading the round data. 
	 * @throws IllegalArgumentException 
	 * 		Problems while loading the round data. 
	 * @throws SecurityException 
	 * 		Problems while loading the round data. 
	 * @throws IllegalAccessException 
	 * 		Problems while loading the round data. 
	 * @throws NoSuchFieldException 
	 * 		Problems while loading the round data. 
	 */
	public void init() throws ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
	{	//stats
		stats = new StatisticRound(this);
		stats.initStartDate();
		
		// players
		remainingPlayers = getProfiles().size();
		for(int i=0;i<remainingPlayers;i++)
			playersStatus.add(new Boolean(true));
		
		// level
		long timeLimit = Long.MAX_VALUE;
		LimitTime lt = limits.getTimeLimit();
		if(lt!=null)
			timeLimit = lt.getThreshold();
		hollowLevel.makeZone(timeLimit);
		
		// current points
		currentPoints = new float[getProfiles().size()];
		Arrays.fill(currentPoints,0);
	}
	
	/**
	 * Checks if this round is over,
	 * i.e. one limit has been reached.
	 * 
	 * @return
	 * 		{@code true} iff this match is over.
	 */
	public boolean isOver()
	{	return roundOver;
	}
	
	/**
	 * Cancels this round
	 * (i.e. it is finished before
	 * its regular limit).
	 */
	public void cancel()
	{	// TODO à compléter
		match.cancel();
	}
	
	/**
	 * Actually starts the round.
	 * 
	 * @throws IllegalArgumentException
	 * 		Problem while loading the round.
	 * @throws SecurityException
	 * 		Problem while loading the round.
	 * @throws ParserConfigurationException
	 * 		Problem while loading the round.
	 * @throws SAXException
	 * 		Problem while loading the round.
	 * @throws IOException
	 * 		Problem while loading the round.
	 * @throws ClassNotFoundException
	 * 		Problem while loading the round.
	 * @throws IllegalAccessException
	 * 		Problem while loading the round.
	 * @throws NoSuchFieldException
	 * 		Problem while loading the round.
	 */
	public void progress() throws IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException, IllegalAccessException, NoSuchFieldException
	{	if(!isOver())
		{	ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
			ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
			
			// replay
			if(fileIn!=null)
			{	loop = new ReplayLoop(this);
				//TODO plus logique d'initialiser stream & round ici non ?
			}
			// client
			else if(clientConnection!=null)
			{	loop = new ClientLoop(this);
				// TODO
			}
			// server
			else if(serverConnection!=null)
			{	loop = new ServerLoop(this);
				// TODO
			}
			// regular (local)
			else
				loop = new RegularLoop(this);
		
			// recording
			if(Configuration.getEngineConfiguration().isRecordRounds() && fileIn==null)
			{	fileOut = new FileServerStream(this);
				RoundVariables.fileOut = fileOut;
				fileOut.initStream();
				fileOut.initRound();
			}
		
			RoundVariables.fileIn = fileIn;
			RoundVariables.fileOut = fileOut;
			
			Thread animator = new Thread(loop);
			animator.setName("TBB.main");
			animator.start();
//			loop.init();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT GAME STREAM	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	// TODO maybe should better be handled at a configuration level, like for the network connections
	// TODO maybe all the round ingame stuff should be over there too
	/** Server stream, for the network mode */
	private FileServerStream fileOut = null;
	/** Client stream, for the network mode */
	private FileClientStream fileIn = null;
/*	
	public void setNetOutputGameStream(NetOutputServerStream netOut)
	{	this.netOut = netOut;
	}
*/	
	/**
	 * Changes the input stream,
	 * for the network mode.
	 * 
	 * @param in
	 * 		New input stream.
	 */
	public void setInputStream(FileClientStream in)
	{	fileIn = in;
	}

	/////////////////////////////////////////////////////////////////
	// FINISH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Cleanly terminates this object.
	 */
	public void finish()
	{	
		// level description
		hollowLevel.finish();
		hollowLevel = null;
		
		// misc
		match = null;
		author = null;
		name = null;
		notes.clear();
		currentPoints = null;
		limits = null;
		stats = null;
		
		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	/**
	 * Remove some uneeded resources
	 * from memory, so that they do not
	 * accumulate in memory and block the game. 
	 */
	public void clean()
	{	// loop
		loop.finish();
		loop = null;

		// round variables
		RoundVariables.level = null;
		RoundVariables.loop = null;
		RoundVariables.instance = null;
		
		// misc
		hollowLevel.clean();
		playersStatus.clear();
		panel = null;

		// garbage collect
		Runtime rt = Runtime.getRuntime();
		rt.gc(); 
	}
	
	/////////////////////////////////////////////////////////////////
	// LOOP 			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Game engine */
	transient private Loop loop = null;
	
	/**
	 * Returns the current game engine.
	 * 
	 * @return
	 * 		Current game engine.
	 */
	public Loop getLoop()
	{	return loop;
	}	
	
	/**
	 * Called when the round is over.
	 */
	public void loopOver()
	{	ClientGeneralConnection clientConnection = Configuration.getConnectionsConfiguration().getClientConnection();
		ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
		
		// read stats from replay if replayed
		if(fileIn!=null)
		{	try
			{	fileIn.finishRound();
				StatisticRound stats = fileIn.getRoundStats();
				setStats(stats);
				fileIn.close();
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
			roundOver = true;
		}
		// read stats from server if network game
		else if(clientConnection!=null)
		{	// NOTE uggly blocking here
			StatisticRound stats = clientConnection.getRoundStats();
			setStats(stats);
			roundOver = true;
		}
		// else : init stats date
		else
		{	stats.initEndDate();		
		}
	
		// possibly not record simulated stats
		if((!(loop instanceof SimulationLoop) || Configuration.getStatisticsConfiguration().getIncludeSimulations())
		// possibly not record quick mode stats
			&& (!GameData.quickMode || Configuration.getStatisticsConfiguration().getIncludeQuickStarts())
		// don't record replay stats	
			&& fileIn==null)
			GameStatistics.update(stats);
		
		// possibly end replay recording
		if(fileOut!=null)
		{	try
			{	fileOut.finishRound(stats);
				fileOut.close();
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

		// possibly end network game
		if(serverConnection!=null)
		{	// send a stop event
			StopReplayEvent event = new StopReplayEvent();
			serverConnection.sendReplay(event);
			// and send the stats
			serverConnection.finishRound(stats);
		}

		match.roundOver();
		if(panel!=null)
		{	panel.roundOver();
		}
	}
	
	/////////////////////////////////////////////////////////////////
	// STATISTICS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Statistics of this round */
	private StatisticRound stats;

	@Override
	public StatisticRound getStats()
	{	return stats;
	}

	/**
	 * Changes the statistics of this round.
	 * 
	 * @param stats
	 * 		New statistics of this round.
	 */
	public void setStats(StatisticRound stats)
	{	this.stats = stats;	
	}
	
	/**
	 * Adds a new event to the statistics
	 * of this round.
	 * 
	 * @param event
	 * 		New statistical event.
	 */
	public void addStatisticEvent(StatisticEvent event)
	{	if(!isOver())
		{	stats.addStatisticEvent(event);
			//stats.computePoints(getPointProcessor());
		}
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Match containing this round */
	private Match match;
	
	/**
	 * Returns the match containing this round.
	 *  
	 * @return
	 * 		Match containing this round.
	 */
	public Match getMatch()
	{	return match;	
	}
	
	/**
	 * Changes the match containing this round.
	 *  
	 * @param match
	 * 		New match for this round.
	 */
	public void setMatch(Match match)
	{	this.match = match;	
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Players still participating to the round */
	private int remainingPlayers;
	/** Status of all players */
	private final List<Boolean> playersStatus = new ArrayList<Boolean>();
	/** Whether players should start the round at random slots */ 
	private boolean randomLocation;

	/**
	 * Indicates if players should start 
	 * the round at random slots.
	 * 
	 * @return
	 * 		{@code true} iff players should start the round at random slots.
	 */
	public boolean getRandomLocation()
	{	return randomLocation;
	}
	
	/**
	 * Changes the flag indicating if players 
	 * should start the round at random slots.
	 * 
	 * @param randomLocations
	 * 		If {@code true}, players start the round at random slots.
	 */
	public void setRandomLocation(boolean randomLocations)
	{	this.randomLocation = randomLocations;
	}
	
	@Override 
	public List<Boolean> getPlayersStatus()
	{	return playersStatus;		
	}
	
	/**
	 * Returns the allowed numbers of players
	 * for this round.
	 * 
	 * @return
	 * 		Set of allowed numbers of players.
	 */
	public TreeSet<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		Iterator<Entry<Integer,PlayerLocation[]>> it = hollowLevel.getPlayers().getLocations().entrySet().iterator();
		while(it.hasNext())
		{	Entry<Integer,PlayerLocation[]> entry = it.next();
			result.add(entry.getKey());
		}
		return result;			
	}
	
	@Override
	public List<Profile> getProfiles()
	{	return match.getProfiles();
	}	
	
	/**
	 * Returns the list of the colors,
	 * for all players involved in this
	 * round.
	 * 
	 * @return
	 * 		List of the player colors.
	 */
	public List<PredefinedColor> getProfilesColors()
	{	List<PredefinedColor> result = new ArrayList<PredefinedColor>();
		for(Profile p: match.getProfiles())
			result.add(p.getSpriteColor());
		return result;
	}	
	
	/**
	 * Eliminates one player from the round.
	 * 
	 * @param index
	 * 		Index of the eliminated player. 
	 */
	public void playerOut(int index)
	{	if(!roundOver)
		{	remainingPlayers --;
			playersStatus.set(index,new Boolean(false));
//System.out.println("player out "+index+"("+loop.getTotalTime()+")");			
		}
	}
	
	/**
	 * Updates the current time of this round.
	 * 
	 * @param time
	 * 		New current time.
	 */
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
	
	/**
	 * Cancels this round.
	 */
	public void cancelGame()
	{	if(roundOver!=true)
		{	getLimits().selectLimit(0);
			closeGame();
		}
	}
	
	/**
	 * Finializes the game
	 * (process the points scored,
	 * etc.).
	 */
	private void closeGame()
	{	roundOver = true;
		stats.finalizeTime(this);
		float[] points = limits.processPoints(this);
		stats.setPoints(points);
		celebrate();		
	}
	
	/**
	 * Starts the celebration phase
	 * (when players express joy or sadness
	 * depending on their results) to
	 * conclude the round.
	 */
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
	/** Number of points scored by the players */
	private float[] currentPoints;
	
	/**
	 * Returns the current number
	 * of points for each player.
	 * 
	 * @return
	 * 		Points scored by the players.
	 */
	public float[] getCurrentPoints()
	{	return currentPoints;		
	}
	
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
		{	ranks = CombinatoricsTools.getRanks(points);
			ranks2 = CombinatoricsTools.getRanks(total);
		}
		else
		{	ranks = CombinatoricsTools.getRanks(currentPoints);
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
	
	
	/**
	 * Returns the winner(s) of this round.
	 * 
	 * @return
	 * 		A list of indices corresponding to the winners.
	 */
	public List<Integer> getWinners()
	{	float[] points = stats.getPoints();
		List<Integer> result = CombinatoricsTools.getWinners(points);
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PANEL			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Panel used to display this round (not the game itself, but the round info) */
	transient private RoundRenderPanel panel;

	/**
	 * Changes the panel used to
	 * display this round.
	 * 
	 * @param panel
	 * 		New panel displaying this round info.
	 */
	public void setPanel(RoundRenderPanel panel)
	{	this.panel = panel;
	}
	
	/**
	 * Returns the panel used to
	 * display this round.
	 * 
	 * @return
	 * 		Panel displaying this round info.
	 */
	public RoundRenderPanel getPanel()
	{	return panel;	
	}
	
	/////////////////////////////////////////////////////////////////
	// NOTES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Text describing this round informally */
	private final List<String> notes = new ArrayList<String>();

	/**
	 * Changes the text describing this round.
	 * 
	 * @param notes
	 * 		New round description.
	 */
	public void setNotes(List<String> notes)
	{	this.notes.addAll(notes);
	}
	
	/**
	 * Returns the text describing this round.
	 * 
	 * @return
	 * 		Author's notes describing this round. 
	 */
	public List<String> getNotes()
	{	return notes;
	}

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Limits for this round */
	private Limits<RoundLimit> limits;

	/**
	 * Returns the limits for this round.
	 * 
	 * @return
	 * 		Limits for this round.
	 */
	public Limits<RoundLimit> getLimits()
	{	return limits;
	}
	
	/**
	 * Changes the limits for this round.
	 * 
	 * @param limits
	 * 		New limits for this round.
	 */
	public void setLimits(Limits<RoundLimit> limits)
	{	this.limits = limits;
	}
			
	/////////////////////////////////////////////////////////////////
	// AUTHOR			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Designer of this round */
	private String author;
	
	/**
	 * Returns the author of this round.
	 * 
	 * @return
	 * 		Designer of the round.
	 */
	public String getAuthor()
	{	return author;
	}
	
	/**
	 * Changes the author of this round.
	 * 
	 * @param author
	 * 		New designer of the round.
	 */
	public void setAuthor(String author)
	{	this.author = author;
	}
	
	/////////////////////////////////////////////////////////////////
	// HOLLOW LEVEL			/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Structure storing all the data necessary to build the level */ 
	private HollowLevel hollowLevel;
	
	/**
	 * Returns the hollow level this
	 * round will use when loading the zone.
	 * 
	 * @return
	 * 		This round hollow level.
	 */
	public HollowLevel getHollowLevel()
	{	return hollowLevel;	
	}
	
	/**
	 * Changes the hollow level this
	 * round will use when loading the zone.
	 * 
	 * @param hollowLevel
	 * 		New hollow level for this round.
	 */
	public void setHollowLevel(HollowLevel hollowLevel)
	{	this.hollowLevel = hollowLevel;	
	}

	/**
	 * Creates a new round, which is a copy of this one.
	 * This method is used to keep an untouched version
	 * of the original roud, in match objects, in case
	 * the same round should be played several times.
	 * 
	 * @return
	 * 		A copy of this round.
	 */
	public Round copy()
	{	Round result = new Round(match);
		result.setNotes(notes);
		result.setLimits(limits);
		result.setHollowLevel(hollowLevel.copy());
		result.setAuthor(author);
		result.setName(name);
		result.setRandomLocation(randomLocation);
		
//		result.fileOut = fileOut;
		result.fileIn = fileIn;
//		result.netClientOut = netClientOut;
//		result.netClientIn = netClientIn;
//		result.netServerOut = netServerOut;
//		result.netServerIn = netServerIn;
		
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// SIMULATION		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Indicates if this round should be actually
	 * played, or just simulated.
	 * 
	 * @return
	 * 		{@code true} iff the round must be simulated.
	 */
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
	
	/**
	 * Launch the simulation of this round.
	 */
	public void simulate()
	{	if(!isOver())
		{	loop = new SimulationLoop(this);
			Thread animator = new Thread(loop);
			animator.setName("TBB.simulation");
			animator.start();
		}
	}
}
