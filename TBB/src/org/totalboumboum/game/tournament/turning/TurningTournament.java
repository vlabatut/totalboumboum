package org.totalboumboum.game.tournament.turning;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.game.limit.LimitConfrontation;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.TournamentLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.GameStatistics;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * This tournament is based on a round-robin system,
 * in the sense every few rounds/matches, some of the
 * playing players are put to rest, whereas some of the
 * resting players enter game. The winner is the one
 * with the most points at the end, or reaching a point
 * limit first.
 * 
 * @author Vincent Labatut
 */
public class TurningTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard tournament.
	 */
	public TurningTournament()
	{	
	}

	/////////////////////////////////////////////////////////////////
	// LIMIT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Tournament limits */
	private Limits<TournamentLimit> limits;

	/**
	 * Returns the tournament limits.
	 *  
	 * @return
	 * 		Tournament limits.
	 */
	public Limits<TournamentLimit> getLimits()
	{	return limits;
	}
	
	/**
	 * Changes the tournament limits.
	 *  
	 * @param limits
	 * 		New tournament limits.
	 */
	public void setLimits(Limits<TournamentLimit> limits)
	{	this.limits = limits;
	}

	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
		
		// matches and tbeir repetitions
		initMatches();
		playedMatches.clear();
		
		// init and sort players (either: as is, random or seeds)
		initPlayers();
				
		// stats
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	// update players (between active and benched players)
			updateCurrentPlayers();
			
			// create new match instance
			Match match = matches.get(currentIndex);
			currentIndex++;
			currentMatch = match.copy();
			currentMatch.init(activePlayers);
			playedMatches.add(currentMatch);
		}
	}
	
	@Override
	public void matchOver()
	{	// update stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		// iterate over matches
		if(currentIndex>=matches.size())
		{	initMatches();
		}

		// check limits
		if(getLimits().testLimit(this))
		{	float[] points = limits.processPoints(this);
			stats.setPoints(points);
			setOver(true);
			panel.tournamentOver();
			stats.initEndDate();
			
			// possibly record stats as text file
			if(hasAi())
			{	AisConfiguration config = Configuration.getAisConfiguration();
				if(config.getRecordStats())
				try
				{	recordStatsAsText();
				}
				catch (FileNotFoundException e)
				{	e.printStackTrace();
				}
				catch (UnsupportedEncodingException e)
				{	e.printStackTrace();
				}
			}

			// server connection
			ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
			if(serverConnection!=null)
				serverConnection.updateHostState(HostState.FINISHED);
		}
		else
		{	panel.matchOver();		
		}
	}

	@Override
	public void finish()
	{	// limits
		limits.finish();
		limits = null;
	}

	/////////////////////////////////////////////////////////////////
	// MATCH ORDER		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Whether the matches should be played in a random order */ 
	private boolean randomizeMatches;

	/**
	 * Indicates if the match order
	 * should be random or not.
	 * 
	 * @return
	 * 		{@code true} iff matches should be played in a random order.
	 */
	public boolean getRandomizeMatches()
	{	return randomizeMatches;
	}
	
	/**
	 * Changes the flag indicating if the match order
	 * should be random or not.
	 * 
	 * @param randomOrder
	 * 		If {@code true}, matches should be played in a random order.
	 */
	public void setRandomizeMatches(boolean randomOrder)
	{	this.randomizeMatches = randomOrder;
	}

	/**
	 * Set matches in a random order.
	 */
	private void randomizeMatches()
	{	Calendar cal = new GregorianCalendar();
		long seed = cal.getTimeInMillis();
		Random random = new Random(seed);
		Collections.shuffle(matches,random);
	}

	/////////////////////////////////////////////////////////////////
	// MATCHES			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of prototype matches */
	private List<Match> matches = new ArrayList<Match>();
	
	/**
	 * Returns the list of prototype matches
	 * for this tournament.
	 * 
	 * @return
	 * 		List of prototype matches.
	 */
	public List<Match> getMatches()
	{	return matches;
	}
	
	/**
	 * Initializes the matches to
	 * be played in this tournament.
	 */
	private void initMatches()
	{	
		// are matches in random order ?
		if(randomizeMatches)
			randomizeMatches();
		
		currentIndex = 0;
	}

	/**
	 * Adds a new match to this tournament.
	 * 
	 * @param match
	 * 		Match to add to this tournament.
	 */
	public void addMatch(Match match)
	{	matches.add(match);
	}

	@Override
	public void roundOver()
	{	panel.roundOver();
	}
	
	@Override
	public Integer getTotalMatchNumber()
	{	Integer result = null;
		
		LimitConfrontation confLim = limits.getConfrontationLimit();
		if(confLim!=null && limits.size()==1)
			result = confLim.getThreshold();
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// PLAYER INITIAL ORDER		/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Way of sorting players before the tournament starts */
	private TurningPlayerSort sortPlayers;
	
	/**
	 * Returns the way of sorting players before
	 * the tournament starts.
	 * 
	 * @return
	 * 		Player sorting method.
	 */
	public TurningPlayerSort getSortPlayers()
	{	return sortPlayers;
	}

	/**
	 * Changes the method used to sort
	 * players before the begining of
	 * the tournament.
	 * 
	 * @param sortPlayers
	 * 		New sorting method.
	 */
	public void setSortPlayers(TurningPlayerSort sortPlayers)
	{	this.sortPlayers = sortPlayers;
	}

	/**
	 * Order players depending on the currently selected sorting method.
	 */
	private void sortPlayers()
	{	// user-defined order is kept
		if(sortPlayers==TurningPlayerSort.NONE)
		{	// nothing special to do
		}
		
		// randomize user-define order
		else if(sortPlayers==TurningPlayerSort.RANDOM)
		{	// just shuffle profiles
			Calendar cal = new GregorianCalendar();
			long seed = cal.getTimeInMillis();
			Random random = new Random(seed);
			Collections.shuffle(profiles,random);
		}
		
		// order players by decreasing rank (no increasing!)
		else if(sortPlayers==TurningPlayerSort.SEEDS)
		{	Collections.sort(profiles,new Comparator<Profile>()
			{	@Override
				public int compare(Profile o1, Profile o2)
				{	String id1 = o1.getId();
					String id2 = o2.getId();
					RankingService rankingService = GameStatistics.getRankingService();
					int r1 = rankingService.getPlayerRank(id1);
					int r2 = rankingService.getPlayerRank(id2);
					int result = r2-r1;
					return result;
				}
			});
		}
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** List of players involved in the current match */
	private final List<Profile> activePlayers = new ArrayList<Profile>();
	/** List of players waiting to play */
	private final Queue<Profile> benchedPlayers = new LinkedList<Profile>();
	/** Number of players kept after each match */
	private int numberKept; 
	/** Number of players involved in a match */
	private int numberActive; 
	
	/**
	 * Sets up the initial order of players as well
	 * as the number of players involved in the first match.
	 */
	private void initPlayers()
	{	// possibly sort players first
		sortPlayers();
		
		// select players for first match
		activePlayers.addAll(profiles.subList(0, numberActive));
		
		// init bench
		benchedPlayers.addAll(profiles.subList(numberActive,profiles.size()));
		
		// possibly update the number of players kept: 
		// is is decreased if the selected player are too few to allow implementing it
		numberKept = Math.min(numberKept, profiles.size()-numberActive);
	}
		
	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	Set<Integer> result = new TreeSet<Integer>();
		for(int i=numberActive+1;i<GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		return result;
	}
	
	/**
	 * Updates the list of players for the next match.
	 * We retain the first {@link #activePlayers} from
	 * the previous match, and complete with the oldest
	 * benched players.
	 */
	private void updateCurrentPlayers()
	{	Ranks ranks = currentMatch.getOrderedPlayers();
	
		// remove newly benched players
		Map<Integer,List<Profile>> all = ranks.getRanks();
		TreeSet<Integer> rankVals = new TreeSet<Integer>(all.keySet());
		Iterator<Integer> it =  rankVals.descendingIterator();
		int c = 0;
		while(it.hasNext() && c<numberActive-numberKept)
		{	int r = it.next();
			List<Profile> p = new ArrayList<Profile>(all.get(r));
			Collections.shuffle(p);
			Iterator<Profile> it2 = p.iterator();
			while(it.hasNext() && c<numberActive-numberKept)
			{	Profile pr = it2.next();
				activePlayers.remove(pr);
				benchedPlayers.offer(pr);
				c++;
			}
		}
		
		// add previously benched players
		for(c=0;c<numberActive-numberKept;c++)
		{	Profile p = benchedPlayers.poll();
			activePlayers.add(p);
		}
	}
	
	/**
	 * Returns the number of players involved
	 * in each match.
	 * 
	 * @return
	 * 		Number of players in a match.
	 */
	public int getNumberActive()
	{	return numberActive;
	}
	
	/**
	 * Changes the number of players involved
	 * in each match.
	 * 
	 * @param numberActive
	 * 		New number of players in a match.
	 */
	public void setNumberActive(int numberActive)
	{	this.numberActive = numberActive;
	}
	
	/**
	 * Returns the number of players kepts
	 * after each match.
	 * 
	 * @return
	 * 		Number of players kept after a match.
	 */
	public int getNumberKept()
	{	return numberKept;
	}
	
	/**
	 * Changes the number of players kepts
	 * after each match.
	 * 
	 * @param numberKept
	 * 		New number of players kept after a match.
	 */
	public void setNumberKept(int numberKept)
	{	this.numberKept = numberKept;
	}

	/////////////////////////////////////////////////////////////////
	// STATS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void recordStatsAsText() throws FileNotFoundException, UnsupportedEncodingException
	{	// get data
		Ranks orderedPlayers = getOrderedPlayers();
		List<Profile> absoluteList = orderedPlayers.getAbsoluteOrderList();
		float points[] = stats.getPoints();
			
		// get file name
		String fileBase = stats.getFilePath();
		int confNbr = playedMatches.size();
		String filePath = fileBase + "." + FileNames.FILE_TOURNAMENT + FileNames.EXTENSION_TEXT;
			
		// open text stream
		FileOutputStream fileOut = new FileOutputStream(filePath);
		BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
		OutputStreamWriter outSW = new OutputStreamWriter(outBuff, "UTF-8");
		PrintWriter writer = new PrintWriter(outSW);
			
		// write general info
		writer.println("Tournament: "+getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
		Date startDate = stats.getStartDate();
		writer.println("Start: "+sdf.format(startDate));
		Date endDate = stats.getEndDate();
		long duration;
		if(endDate==null)
		{	writer.println("End: N/A");
			duration = System.currentTimeMillis() - startDate.getTime();
		}
		else
		{	writer.println("End: "+sdf.format(endDate));
			duration = endDate.getTime() - startDate.getTime();
		}
		String durationStr = TimeTools.formatTime(duration, TimeUnit.MINUTE, TimeUnit.MILLISECOND, false);
		writer.println("Duration: "+durationStr);
		writer.println();

		// write headers
		writer.print("Rank\t");
		writer.print("Name\t");
		writer.print("Color\t");
//			writer.print("Id\t");
		writer.print("Bombs\t");
		writer.print("Items\t");
		writer.print("Bombeds\t");
		writer.print("Selfies\t");
		writer.print("Bombings\t");
		writer.print("Played\t");
		writer.print("Won\t");
		writer.print("Drawn\t");
		writer.print("Lost\t");
		writer.print("1st\t");
		writer.print("2nd\t");
		writer.print("3rd\t");
		writer.print("4th\t");
		writer.print("+5th\t");
		writer.print("Points\t");
		for(int i=0;i<confNbr;i++)
			writer.print("M"+(i+1)+"\t");
		writer.println();

		// write data
		int played[] = stats.getPlayedCounts();
		int[][] confs = stats.getConfrontationCounts();
		int[][] ranks = stats.getRankCounts();
		for(int i=0;i<points.length;i++)
		{	// set profile stuff
			Profile profile = absoluteList.get(i);
			int profileIndex = profiles.indexOf(profile);

			// rank
			{	int rank = orderedPlayers.getRankForProfile(profile);
				writer.print(rank+".\t");
			}
			
			// name
			{	String name = profile.getName();
				writer.print(name+"\t");
			}
			
			// color
			{	PredefinedColor color = profile.getSpriteColor();
				writer.print(color+"\t");
			}
			
			// id
//				{	String id = playersIds.get(profileIndex);
//					writer.print(name+"\t");
//				}
			
			// bombs dropped
			{	long scores[] = stats.getScores(Score.BOMBS);
				long bombs = scores[profileIndex];
				writer.print(bombs+"\t");
			}
			
			// items pîcked
			{	long scores[] = stats.getScores(Score.ITEMS);
				long items = scores[profileIndex];
				writer.print(items+"\t");
			}
			
			// times bombed
			{	long scores[] = stats.getScores(Score.BOMBEDS);
				long bombeds = scores[profileIndex];
				writer.print(bombeds+"\t");
			}
			
			// self-bombings
			{	long scores[] = stats.getScores(Score.SELF_BOMBINGS);
				long selfies = scores[profileIndex];
				writer.print(selfies+"\t");
			}
			
			// players bombed
			{	long scores[] = stats.getScores(Score.BOMBINGS);
				long bombings = scores[profileIndex];
				writer.print(bombings+"\t");
			}
			
			// played
			{	int pld = played[profileIndex];
				writer.print(pld+"\t");
			}
			
			// won
			{	int won = confs[profileIndex][0];
				writer.print(won+"\t");
			}
			
			// drawn
			{	int drawn = confs[profileIndex][1];
				writer.print(drawn+"\t");
			}
			
			// lost
			{	int lost = confs[profileIndex][2];
				writer.print(lost+"\t");
			}
			
			// first
			{	int first = ranks[profileIndex][0];
				writer.print(first+"\t");
			}
			
			// second
			{	int second = ranks[profileIndex][1];
				writer.print(second+"\t");
			}
			
			// third
			{	int third = ranks[profileIndex][2];
				writer.print(third+"\t");
			}
			
			// fourth
			{	int fourth = ranks[profileIndex][3];
				writer.print(fourth+"\t");
			}
			
			// fifth or more
			{	int more = ranks[profileIndex][4];
				writer.print(more+"\t");
			}
			
			// points
			{	float total[] = stats.getTotal();
				float pts = total[profileIndex];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String ptsStr = nf.format(pts);
				writer.print(ptsStr+"\t");
			}
			
			// confrontations
			{	List<StatisticBase> statMatches = stats.getConfrontationStats();
				for(StatisticBase statMatch: statMatches)
				{	List<String> playerIds = statMatch.getPlayersIds();
					String playerId = profile.getId();
					int index = playerIds.indexOf(playerId);
					float pts = 0;
					if(index!=-1)
						pts = statMatch.getPoints()[index];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String ptsStr = nf.format(pts);
					writer.print(ptsStr+"\t");
				}
			}
			
			writer.println();
		}
		
		// close stream
		writer.close();
	}
	
	/////////////////////////////////////////////////////////////////
	// RESULTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Returns the ranks for this tournament.
	 * 
	 * @param pts
	 * 		Points scored by the players.
	 * @return
	 * 		Corresponding player ranks.
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
}
