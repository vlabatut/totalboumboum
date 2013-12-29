package org.totalboumboum.game.tournament.single;

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
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.configuration.ai.AisConfiguration;
import org.totalboumboum.engine.container.level.hollow.HollowLevel;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.game.limit.Comparisons;
import org.totalboumboum.game.limit.LimitConfrontation;
import org.totalboumboum.game.limit.Limits;
import org.totalboumboum.game.limit.MatchLimit;
import org.totalboumboum.game.limit.RoundLimit;
import org.totalboumboum.game.match.Match;
import org.totalboumboum.game.points.AbstractPointsProcessor;
import org.totalboumboum.game.points.PointsProcessorTotal;
import org.totalboumboum.game.profile.Profile;
import org.totalboumboum.game.profile.ProfileLoader;
import org.totalboumboum.game.rank.Ranks;
import org.totalboumboum.game.round.Round;
import org.totalboumboum.game.tournament.AbstractTournament;
import org.totalboumboum.statistics.detailed.Score;
import org.totalboumboum.statistics.detailed.StatisticBase;
import org.totalboumboum.statistics.detailed.StatisticMatch;
import org.totalboumboum.statistics.detailed.StatisticTournament;
import org.totalboumboum.stream.file.replay.FileClientStream;
import org.totalboumboum.stream.network.data.host.HostState;
import org.totalboumboum.stream.network.server.ServerGeneralConnection;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.images.PredefinedColor;
import org.totalboumboum.tools.time.TimeTools;
import org.totalboumboum.tools.time.TimeUnit;
import org.xml.sax.SAXException;

/**
 * Represents a tournament containing
 * only one match, i.e. the simplest form
 * of tournament.
 * 
 * @author Vincent Labatut
 */
public class SingleTournament extends AbstractTournament
{	/** Class id */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Builds a standard tournament.
	 */
	public SingleTournament()
	{	
	}

	/**
	 * Builds a tournament to replay
	 * a game.
	 * 
	 * @param replay
	 * 		The replay access. 
	 * @throws IOException 
	 * 		Problem while reading the file.
	 * @throws ClassNotFoundException 
	 * 		Problem while reading the file.
	 * @throws IllegalArgumentException 
	 * 		Problem while reading the file.
	 * @throws SecurityException 
	 * 		Problem while reading the file.
	 * @throws ParserConfigurationException 
	 * 		Problem while reading the file.
	 * @throws SAXException 
	 * 		Problem while reading the file.
	 * @throws IllegalAccessException 
	 * 		Problem while reading the file.
	 * @throws NoSuchFieldException 
	 * 		Problem while reading the file.
	 */
	public SingleTournament(FileClientStream replay) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	replay.initStreams();
		replay.initRound();
		setName("Replay");
		setAuthor("Replay");
		
		// profiles
		List<Profile> readProfiles = replay.getProfiles();
		for(Profile p: readProfiles)
			ProfileLoader.reloadPortraits(p);
		profiles.addAll(readProfiles);

		// one round match
		Match match = new Match(this);
		match.setAuthor("Replay");
		{	// notes
			List<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			match.setNotes(notes);
		}
		{	// limits
			AbstractPointsProcessor pointProcessor = new PointsProcessorTotal();
			Limits<MatchLimit> limits = new Limits<MatchLimit>();
			MatchLimit limit = new LimitConfrontation(1,Comparisons.GREATEREQ,pointProcessor);
			limits.addLimit(limit);
			match.setLimits(limits);
		}
		setMatch(match);
		
		// round
		Round round = new Round(match);
		match.setAuthor("Replay");
	    round.setInputStream(replay);
		{	// notes
			List<String> notes = new ArrayList<String>();
			notes.add("auto-generated notes");
			round.setNotes(notes);
		}
		{	// limits
			Limits<RoundLimit> limits = replay.getRoundLimits();
			round.setLimits(limits);
		}
		match.addRound(round);
		
		// level
		HollowLevel hollowLevel = new HollowLevel();
		LevelInfo levelInfo = replay.getLevelInfo();
		hollowLevel.setLevelInfo(levelInfo);
		Instance instance = new Instance(levelInfo.getInstanceName());
		hollowLevel.setInstance(instance);
		HashMap<String,Integer> readItemCounts = replay.getItemCounts();
		hollowLevel.setItemCounts(readItemCounts);
		// no need for Players nor Zone objects here
		round.setHollowLevel(hollowLevel);
		
		// progress
		init();
	    progress();
	    match = getCurrentMatch();
	    match.progress();
	}
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void init()
	{	begun = true;
		playedMatches.clear();
		// NOTE vérifier si le nombre de joueurs sélectionnés correspond
		stats = new StatisticTournament(this);
		stats.initStartDate();
	}

	@Override
	public void progress()
	{	if(!isOver())
		{	if(currentMatch.getProfiles().size()==0)
				currentMatch.init(profiles);
			playedMatches.add(currentMatch);
		}
	}

	@Override
	public void finish()
	{	//NOTE et les matches ? (dans SequenceTournament aussi)
	}

	/////////////////////////////////////////////////////////////////
	// MATCH			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Changes the currently ongoing match.
	 * 
	 * @param match
	 * 		New current match for this tournament.
	 */
	public void setMatch(Match match)
	{	this.currentMatch = match;
	}

	@Override
	public void matchOver()
	{	// update stats
		StatisticMatch statsMatch = currentMatch.getStats();
		stats.addStatisticMatch(statsMatch);
		float[] points = stats.getTotal();
		stats.setPoints(points);
		
		setOver(true);
		
		if(panel!=null)
		{	panel.tournamentOver();
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
		}
//NOTE ou bien : panel.matchOver();		
		// server connection
		ServerGeneralConnection serverConnection = Configuration.getConnectionsConfiguration().getServerConnection();
		if(serverConnection!=null)
			serverConnection.updateHostState(HostState.FINISHED);
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
	{	Set<Integer> result = currentMatch.getAllowedPlayerNumbers();
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
//		writer.print("Id\t");
		writer.print("Bombs\t");
		writer.print("Items\t");
		writer.print("Bombeds\t");
		writer.print("Selfies\t");
		writer.print("Bombings\t");
		for(int i=0;i<playedMatches.size();i++)
			writer.print("M"+(i+1)+"\t");
		writer.print("Total\t");
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
//			{	String id = playersIds.get(profileIndex);
//				writer.print(name+"\t");
//			}
			
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
