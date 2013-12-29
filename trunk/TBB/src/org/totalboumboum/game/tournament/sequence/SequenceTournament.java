package org.totalboumboum.game.tournament.sequence;

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

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.TournamentLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;

/**
 * In this tournament, all selected
 * players play a series of matches.
 * kind of like a league, but all players
 * play all matches.
 * 
 * @author Vincent Labatut
 */
public class SequenceTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;

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
		
		// matches
		initMatches();
		
		// stats
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	Match match = matches.get(currentIndex);
			currentIndex++;
			currentMatch = match.copy();
			currentMatch.init(profiles);
			playedMatches.add(currentMatch);
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
	 * Initializes the matches to
	 * be played in this tournament.
	 */
	private void initMatches()
	{	// are matches in random order ?
		if(randomizeMatches)
			randomizeMatches();
		
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		currentIndex = 0;
		playedMatches.clear();
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
	public void matchOver()
	{	// update stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		
		// iterate over matches
		if(currentIndex>=matches.size())
		{	if(randomizeMatches)
				randomizeMatches();
			currentIndex = 0;
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
	public void roundOver()
	{	panel.roundOver();
	}

	/////////////////////////////////////////////////////////////////
	// PLAYERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	@Override
	public Set<Integer> getAllowedPlayerNumbers()
	{	TreeSet<Integer> result = new TreeSet<Integer>();
		for(int i=0;i<=GameData.MAX_PROFILES_COUNT;i++)
			result.add(i);
		for(Match m:matches)
		{	Set<Integer> temp = m.getAllowedPlayerNumbers();
			result.retainAll(temp);			
		}
		return result;			
	}

	/////////////////////////////////////////////////////////////////
	// STATS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void recordStatsAsText() throws FileNotFoundException
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
		OutputStreamWriter outSW = new OutputStreamWriter(outBuff);
		PrintWriter writer = new PrintWriter(outSW);
			
		// write general info
		writer.println("Tournament: "+getName());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
		Date startDate = stats.getStartDate();
		writer.println("Start: "+sdf.format(startDate));
		Date endDate = stats.getEndDate();
		writer.println("End: "+sdf.format(endDate));
		long duration = endDate.getTime() - startDate.getTime();
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
		for(int i=0;i<confNbr;i++)
			writer.print("M"+(i+1)+"\t");
		writer.print("Total\t");
		writer.print("Points\t");
		writer.println();

		// write data
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
			
			// confrontations
			{	List<StatisticBase> statMatches = stats.getConfrontationStats();
				for(StatisticBase statMatch: statMatches)
				{	float pts = statMatch.getPoints()[profileIndex];
					NumberFormat nf = NumberFormat.getInstance();
					nf.setMaximumFractionDigits(2);
					nf.setMinimumFractionDigits(0);
					String ptsStr = nf.format(pts);
					writer.print(ptsStr+"\t");
				}
			}
			
			// total
			{	float total[] = stats.getTotal();
				float pts = total[profileIndex];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String ptsStr = nf.format(pts);
				writer.print(ptsStr+"\t");
			}
			
			// points scored
			{	double pts = points[profileIndex];
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(2);
				nf.setMinimumFractionDigits(0);
				String ptsStr = nf.format(pts);
				writer.print(ptsStr+"\t");
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
